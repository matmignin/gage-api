package tv.gage.hub;

import org.junit.Test;

import tv.gage.common.game.Game;
import tv.gage.common.game.Player;
import tv.gage.common.messaging.BroadcastService;
import tv.gage.common.messaging.Message;

public class DefaultGameTest {

	private Game game = new DefaultGame(new BroadcastService() {
		protected void sendPlayerMessage(Message message) {}
		protected void sendGameMessage(Message message) {}
	}, "DFLT");
	
	@Test
	public void receiveGameCommandTest() {
		game.receiveGameCommand("test");
	}
	
	@Test 
	public void receivePlayerCommandTest() {
		Player player = Player.builder().playerCode("PLYR").build();
		game.receivePlayerCommand(player, "test");
	}

}
