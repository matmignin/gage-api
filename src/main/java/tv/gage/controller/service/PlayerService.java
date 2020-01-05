package tv.gage.controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tv.gage.common.Response;
import tv.gage.common.exception.PlayerNotUniqueException;
import tv.gage.common.exception.PlayerRosterFullException;
import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.exception.UnknownPlayerException;
import tv.gage.common.game.CodeGenerator;
import tv.gage.common.game.Game;
import tv.gage.common.game.Player;

@Service
public class PlayerService {

	@Autowired
	private HubService hubService;
	
	public Response addPlayer(String gameCode, String name) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			if (!isUniquePlayerName(game, name)) {
				throw new PlayerNotUniqueException("Player Name Already in Use");
			}
			String playerCode = new CodeGenerator().generateUniquePlayerCode(game.getPlayers());
			return addPlayer(game, name, playerCode);
		}
		catch (UnknownGameException | PlayerNotUniqueException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response addPlayer(String gameCode, String name, String playerCode) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			return addPlayer(game, name, playerCode);
		}
		catch (UnknownGameException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	private Response addPlayer(Game game, String name, String playerCode) {
		try {
			Player player = Player.builder()
					.name(name)
					.playerCode(playerCode)
					.gameCode(game.getGameCode())
					.build();
			game.addPlayer(player);
			return Response.builder()
					.result(player)
					.build();
		}
		catch (PlayerRosterFullException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response removePlayer(String gameCode, String playerCode) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			Player player = findPlayerByCode(game, playerCode);
			game.removePlayer(player);
			return Response.builder()
					.result(game)
					.build();
		}
		catch (UnknownGameException | UnknownPlayerException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response getPlayers(String gameCode) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			List<Player> players = game.getPlayers();
			return Response.builder()
					.result(players)
					.build();
		}
		catch (UnknownGameException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public boolean isUniquePlayerName(Game game, String name) {
		long numberOfPlayers = game.getPlayers().stream()
				.filter(player -> player.getName().equals(name))
				.count();
		return numberOfPlayers == 0;
	}
	
	public Player findPlayerByCode(Game game, String playerCode) throws UnknownPlayerException {
		Player existingPlayer = game.getPlayers().stream()
				.filter(player -> player.getPlayerCode().equals(playerCode))
				.findFirst()
				.orElse(null);
		if (existingPlayer == null) {
			throw new UnknownPlayerException(String.format("Unknown Player Code %s for Game Code %s", playerCode, game.getGameCode()));
		}
		return existingPlayer;
	}
	
}
