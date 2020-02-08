package tv.gage.controller.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

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
public class PlayerServiceTest {
	
	private BroadcastService broadcastService = new BroadcastService() {
		protected void sendPlayerMessage(Message message) {}
		protected void sendGameMessage(Message message) {}
	};
	
	@Autowired
	private PlayerService playerService;
	
	@MockBean
	private HubService hubServiceMock;
	
	@Before
	public void setup() throws UnknownGameException {
		Mockito.when(hubServiceMock.findGameByCode("DFLT")).thenReturn(new DefaultGame(broadcastService, "DFLT"));
		Mockito.when(hubServiceMock.findGameByCode("UNKN")).thenThrow(new UnknownGameException("unknown game"));
	}
	
	@Test
	public void addPlayerTest() throws UnknownGameException {
		String gameCode = "DFLT";
		String name = "Player1";
		Response response = playerService.addPlayer(gameCode, name);
		String actual = ((Player) response.getResult()).getName();
		assertEquals(name, actual);
	}

	@Test
	public void addPlayerUnknownGameTest() throws UnknownGameException {
		String name = "Player1";
		Response response = playerService.addPlayer("UNKN", name);
		assertEquals("unknown game", response.getError());
	}
	
	@Test
	public void addPlayerNonUniqueTest() {
		String gameCode = "DFLT";
		String player1 = "Player1";
		playerService.addPlayer(gameCode, player1);
		Response response = playerService.addPlayer(gameCode, player1);
		assertEquals("Player Name Already in Use", response.getError());
	}
	
	@Test
	public void addPlayerWithCodeTest() {
		String gameCode = "DFLT";
		String name = "Player1";
		String playerCode = "PLYR";
		Response response = playerService.addPlayer(gameCode, name, playerCode);
		String actual = ((Player) response.getResult()).getName();
		assertEquals(name, actual);
	}

	@Test
	public void addPlayerWithCodeUnknownGameTest() throws UnknownGameException {
		String name = "Player1";
		String playerCode = "PLYR";
		Response response = playerService.addPlayer("UNKN", name, playerCode);
		assertEquals("unknown game", response.getError());
	}
	
	@Test
	public void removePlayerTest() {
		String gameCode = "DFLT";
		String player1 = "Player1";
		Response addResponse = playerService.addPlayer(gameCode, player1);
		Player player = (Player) addResponse.getResult();
		Response removeResponse = playerService.removePlayer(gameCode, player.getPlayerCode());
		Game game = (Game) removeResponse.getResult();
		assertEquals(0, game.getPlayers().size());
	}
	
	@Test
	public void removePlayerUnknownGameTest() throws UnknownGameException {
		String name = "Player1";
		Response response = playerService.removePlayer("UNKN", name);
		assertEquals("unknown game", response.getError());
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPlayersTest() {
		String gameCode = "DFLT";
		String player1 = "Player1";
		String player2 = "Player2";
		playerService.addPlayer(gameCode, player1);
		playerService.addPlayer(gameCode, player2);
		Response response = playerService.getPlayers(gameCode);
		List<Player> players = (List<Player>) response.getResult();
		assertEquals(2, players.size());
	}
	
	@Test
	public void getPlayersUnknownGameTest() {
		Response response = playerService.getPlayers("UNKN");
		assertEquals("unknown game", response.getError());
	}
	
	@Test
	public void findPlayerByCodeTest() throws UnknownPlayerException {
		String gameCode = "DFLT";
		String name = "Player1";
		Response addResponse = playerService.addPlayer(gameCode, name);
		Player player = (Player) addResponse.getResult();
		Game game = new DefaultGame(broadcastService, gameCode);
		game.addPlayer(player);
		Player foundPlayer = playerService.findPlayerByCode(game, player.getPlayerCode());
		assertEquals(player, foundPlayer);
	}
	
	@Test(expected=UnknownPlayerException.class)
	public void findPlayerByCodeNotFoundTest() throws UnknownPlayerException {
		String gameCode = "DFLT";
		String name = "Player1";
		Response addResponse = playerService.addPlayer(gameCode, name);
		Player player = (Player) addResponse.getResult();
		Game game = new DefaultGame(broadcastService, gameCode);
		game.addPlayer(player);
		playerService.findPlayerByCode(game, "");
	}
	
}
