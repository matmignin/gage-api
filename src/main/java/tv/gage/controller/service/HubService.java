package tv.gage.controller.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.game.CodeGenerator;
import tv.gage.common.game.Game;
import tv.gage.common.messaging.BroadcastService;
import tv.gage.hub.Hub;

@Service
public class HubService {
	
	@Autowired
	private MessagingService messagingService;
	
	private Hub hub = Hub.singleton();

	public Game addGame(String gameName) throws InstantiationException, IllegalAccessException, UnknownGameException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String gameCode = new CodeGenerator().generateUniqueGameCode(hub.getGames());
		return addGame(gameName, gameCode);
	}

	public Game addGame(String gameName, String gameCode) throws InstantiationException, IllegalAccessException, UnknownGameException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Game game = instantiateGameByName(gameName, gameCode);
		hub.addGame(game);
		return game;
	}
	
	public void removeGame(String gameCode) throws UnknownGameException {
		Game game = findGameByCode(gameCode);
		hub.removeGame(game);
	}

	public List<Game> activeGames() {
		return hub.getGames();
	}

	public Game findGameByCode(String gameCode) throws UnknownGameException {
		return hub.findGameByCode(gameCode);
	}
	
	private Game instantiateGameByName(String gameName, String gameCode) throws UnknownGameException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> clazz = hub.getRegisteredGames().stream()
				.filter(registeredGame -> registeredGame.getSimpleName().equals(gameName))
				.findFirst()
				.orElse(null);
		if (clazz == null) {
			throw new UnknownGameException("Unknown Game");
		}
		
		return (Game) clazz.getDeclaredConstructor(BroadcastService.class, String.class).newInstance(messagingService, gameCode);
	}

}
