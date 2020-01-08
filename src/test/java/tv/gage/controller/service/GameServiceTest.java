package tv.gage.controller.service;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import tv.gage.common.Response;
import tv.gage.common.exception.UnknownGameException;
import tv.gage.common.exception.UnknownPlayerException;
import tv.gage.common.game.Game;
import tv.gage.common.game.Player;
import tv.gage.common.messaging.BroadcastService;
import tv.gage.common.messaging.Message;
import tv.gage.hub.DefaultGame;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class GameServiceTest {
	
	private BroadcastService broadcastService = new BroadcastService() {
		protected void sendPlayerMessage(Message message) {}
		protected void sendGameMessage(Message message) {}
	};
	
	@Autowired
	private GameService gameService;
	
	@MockBean
	private HubService hubServiceMock;
	
	@MockBean
	private PlayerService playerServiceMock;
	
	@Before
	public void setup() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnknownGameException, UnknownPlayerException {
		Mockito.when(hubServiceMock.addGame("DefaultGame")).thenReturn(new DefaultGame(broadcastService, "DFLT"));
		Mockito.when(hubServiceMock.addGame("DefaultGame", "DFLT")).thenReturn(new DefaultGame(broadcastService, "DFLT"));
		Mockito.when(hubServiceMock.addGame("Unknown")).thenThrow(new UnknownGameException("unknown game"));
		Mockito.when(hubServiceMock.addGame("Unknown", "DFLT")).thenThrow(new UnknownGameException("unknown game"));
		Mockito.doNothing().when(hubServiceMock).removeGame("DFLT");
		Mockito.doThrow(new UnknownGameException("unknown game")).when(hubServiceMock).removeGame("UNKN");
		Mockito.when(hubServiceMock.findGameByCode("DFLT")).thenReturn(new DefaultGame(broadcastService, "DFLT"));
		Mockito.when(hubServiceMock.findGameByCode("UNKN")).thenThrow(new UnknownGameException("unknown game"));
		Mockito.when(playerServiceMock.findPlayerByCode(Mockito.any(Game.class), Mockito.eq("PLYR"))).thenReturn(Player.builder().playerCode("PLYR").build());
		Mockito.when(playerServiceMock.findPlayerByCode(Mockito.any(Game.class), Mockito.eq("UNKN"))).thenThrow(new UnknownPlayerException("unknown player"));
	}

	@Test
	public void addGameTest() {
		Response response = gameService.addGame("DefaultGame");
		Game game = (Game) response.getResult();
		assertEquals(DefaultGame.class, game.getClass());
	}
	
	@Test
	public void addGameUnknownGameTest() {
		Response response = gameService.addGame("Unknown");
		String error = response.getError();
		assertEquals("unknown game", error);
	}
	
	@Test
	public void addGameWithGameCodeTest() {
		Response response = gameService.addGame("DefaultGame", "DFLT");
		Game game = (Game) response.getResult();
		assertEquals(DefaultGame.class, game.getClass());
	}

	@Test
	public void addGameWithGameCodeUnknownGameTest() {
		Response response = gameService.addGame("Unknown", "DFLT");
		String error = response.getError();
		assertEquals("unknown game", error);
	}
	
	@Test
	public void removeGameTest() {
		Response response = gameService.removeGame("DFLT");
		String expected = String.format("%s removed", "DFLT");
		String actual = (String) response.getResult();
		assertEquals(expected, actual);
	}
	
	@Test
	public void removeGameUnknownGameTest() {
		Response response = gameService.removeGame("UNKN");
		String actual = response.getError();
		assertEquals("unknown game", actual);
	}
	
	@Test
	public void sendGameCommandTest() {
		Response response = gameService.sendGameCommand("DFLT", "test");
		String actual = (String) response.getResult();
		assertEquals("received", actual);
	}
	
	@Test
	public void sendGameCommandUnknownGameTest() {
		Response response = gameService.sendGameCommand("UNKN", "test");
		String actual = response.getError();
		assertEquals("unknown game", actual);
	}
	
	@Test
	public void sendPlayerCommandTest() {
		Response response = gameService.sendPlayerCommand("DFLT", "PLYR", "test");
		String actual = (String) response.getResult();
		assertEquals("received", actual);
	}
	
	@Test
	public void sendPlayerCommandUnknownGameTest() {
		Response response = gameService.sendPlayerCommand("UNKN", "PLYR", "test");
		String actual = response.getError();
		assertEquals("unknown game", actual);
	}
	
	@Test
	public void sendPlayerCommandUnknowPlayerTest() {
		Response response = gameService.sendPlayerCommand("DFLT", "UNKN", "test");
		String actual = response.getError();
		assertEquals("unknown player", actual);
	}
	
}
