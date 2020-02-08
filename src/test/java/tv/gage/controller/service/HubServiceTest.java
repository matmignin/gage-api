package tv.gage.controller.service;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Test;

import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.game.Game;
import tv.gage.hub.DefaultGame;

public class HubServiceTest {
	
	private HubService hubService = new HubService();
	private String gameName = "DefaultGame";
	private Game game = new DefaultGame(null, "DFLT");

	@Test
	public void addGameTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnknownGameException {
		hubService.removeAllGames();
		hubService.addGame(gameName);
		assertEquals(1, hubService.activeGames().size());
	}
	
	@Test(expected=UnknownGameException.class)
	public void addGameExceptionTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnknownGameException {
		hubService.addGame("nonsense");
	}

	@Test
	public void removeGameTest() throws UnknownGameException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		hubService.removeAllGames();
		hubService.addGame(gameName);
		List<Game> activeGames = hubService.activeGames();
		hubService.removeGame(activeGames.get(0).getGameCode());
		assertEquals(0, hubService.activeGames().size());
	}
	
	@Test
	public void addGameWithCodeTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnknownGameException {
		hubService.removeAllGames();
		hubService.addGame("DefaultGame", game.getGameCode());
		assertEquals(game.getGameCode(), hubService.activeGames().get(0).getGameCode());
	}
	
	@Test
	public void activeGamesTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnknownGameException {
		hubService.removeAllGames();
		hubService.addGame(gameName);
		assertEquals(1, hubService.activeGames().size());
	}
	
}
