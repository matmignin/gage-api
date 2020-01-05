package tv.gage.controller.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tv.gage.common.Response;
import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.exception.UnknownPlayerException;
import tv.gage.common.game.Game;
import tv.gage.common.game.GameInfo;
import tv.gage.common.game.Player;
import tv.gage.simon.Simon;

@Service
public class GameService {
	
	@Autowired
	private HubService hubService;
	
	@Autowired
	private PlayerService playerService;
	
	public Response availableGames() {
		return Response.builder()
				.result(Arrays.asList(new GameInfo[] {
						new Simon(null, null).gameInfo()
				}))
				.build();
	}
	
	public Response addGame(String gameName) {
		try {
			Game game = hubService.addGame(gameName);
			return Response.builder()
					.result(game)
					.build();
		}
		catch (InstantiationException | IllegalAccessException | UnknownGameException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response addGame(String gameName, String gameCode) {
		try {
			Game game = hubService.addGame(gameName, gameCode);
			return Response.builder()
					.result(game)
					.build();
		}
		catch (InstantiationException | IllegalAccessException | UnknownGameException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response removeGame(String gameCode) {
		try {
			hubService.removeGame(gameCode);
			return Response.builder()
					.result(String.format("%s removed", gameCode))
					.build();
		}
		catch (UnknownGameException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response isReadyToPlay(String gameCode) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			return Response.builder()
					.result(game.isReadyToPlay())
					.build();
		} 
		catch (UnknownGameException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response sendGameCommand(String gameCode, String jsonCommand) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			game.receiveGameCommand(jsonCommand);
			return Response.builder()
					.result("Received")
					.build();
		}
		catch (UnknownGameException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response sendPlayerCommand(String gameCode, String playerCode, String jsonCommand) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			Player player = playerService.findPlayerByCode(game, playerCode);
			game.receivePlayerCommand(player, jsonCommand);
			return Response.builder()
					.result("Received")
					.build();
		}
		catch (UnknownGameException | UnknownPlayerException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
}
