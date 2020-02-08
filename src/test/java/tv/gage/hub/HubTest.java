package tv.gage.hub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.game.Game;

public class HubTest {
	
	private Hub hub = Hub.singleton();
	private Game game = new DefaultGame(null, "DFLT");
	
	@Test
	public void singletonTest() {
		assertEquals(hub, Hub.singleton());
	}
	
	@Test
	public void getRegisteredGamesTest() {
		List<Class<?>> registeredGames = hub.getRegisteredGames();
		assertTrue(registeredGames.contains(DefaultGame.class));
	}
	
	@Test
	public void getGamesTest() {
		hub.removeAllGames();
		hub.addGame(game);
		assertEquals(1, hub.getGames().size());
	}
	
	@Test
	public void addGameTest() {
		hub.removeAllGames();
		hub.addGame(game);
		assertEquals(1, hub.getGames().size());
	}
	
	@Test
	public void removeGameTest() {
		hub.removeAllGames();
		hub.addGame(game);
		hub.removeGame(game);
		assertEquals(0, hub.getGames().size());
	}
	
	@Test
	public void findGameByCodeTest() throws UnknownGameException {
		Game foundGame = hub.findGameByCode("DFLT");
		assertEquals(game, foundGame);
	}
	
	@Test(expected=UnknownGameException.class)
	public void findGameByCodeExceptionTest() throws UnknownGameException {
		hub.findGameByCode("UNKN");
	}
	
}
