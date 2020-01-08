package tv.gage.hub;

import tv.gage.common.game.Game;
import tv.gage.common.game.Player;
import tv.gage.common.messaging.BroadcastService;

public class DefaultGame extends Game {

	public DefaultGame(BroadcastService broadcastService, String gameCode) {
		super(broadcastService, gameCode);
	}

	@Override
	public void receiveGameCommand(String json) {}

	@Override
	public void receivePlayerCommand(Player player, String json) {}

}
