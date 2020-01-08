package tv.gage.controller.service;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tv.gage.common.Response;
import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.exception.UnknownPlayerException;
import tv.gage.common.game.Game;
import tv.gage.common.game.Player;

@Service
public class GameService {
	
	@Autowired
	private HubService hubService;
	
	@Autowired
	private PlayerService playerService;
	
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
	
	public Response sendGameCommand(String gameCode, String json) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			game.receiveGameCommand(json);
			return Response.builder()
					.result("received")
					.build();
		}
		catch (UnknownGameException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
	public Response sendPlayerCommand(String gameCode, String playerCode, String json) {
		try {
			Game game = hubService.findGameByCode(gameCode);
			Player player = playerService.findPlayerByCode(game, playerCode);
			game.receivePlayerCommand(player, json);
			return Response.builder()
					.result("received")
					.build();
		}
		catch (UnknownGameException | UnknownPlayerException e) {
			return Response.builder()
					.error(e.getMessage())
					.build();
		}
	}
	
}
