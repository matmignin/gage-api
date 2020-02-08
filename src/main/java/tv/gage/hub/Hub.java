package tv.gage.hub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tv.gage.cardsAgainstHumanity.Humanity;
import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.game.Game;
import tv.gage.simon.Simon;

public class Hub {

	private List<Class<?>> registeredGames = Arrays.asList(
			DefaultGame.class,
			Simon.class,
			Humanity.class
	);
	
	private static Hub hub = null;
	private static List<Game> games = new ArrayList<Game>();

	public static Hub singleton() {
		if (hub == null) {
			hub = new Hub();
		}
		return hub;
	}
	
	public List<Class<?>> getRegisteredGames() {
		return registeredGames;
	}
	
	public List<Game> getGames() {
		return games;
	}
	
	public void addGame(Game game) {
		games.add(game);
	}
	
	public void removeGame(Game game) {
		games.remove(game);
	}
	
	public void removeAllGames() {
		games.clear();
	}
	
	public Game findGameByCode(String gameCode) throws UnknownGameException {
		Game existingGame = games.stream()
				.filter(game -> game.getGameCode().equals(gameCode))
				.findFirst()
				.orElse(null);
		if (existingGame == null) {
			throw new UnknownGameException(String.format("Unknown Game Code %s", gameCode));
		}
		return existingGame;
	}
	
}
