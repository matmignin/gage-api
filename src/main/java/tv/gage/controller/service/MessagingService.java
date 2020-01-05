package tv.gage.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import tv.gage.common.messaging.Message;
import tv.gage.common.socket.SocketService;

@Service
public class MessagingService implements SocketService {
	
	private final String gameUrl = "/topic/game/%s";
	private final String playerUrl = "/topic/game/%s/player/%s";

	@Autowired
	private SimpMessagingTemplate template;

	public void sendGameMessage(Message message) {
		String destination = String.format(gameUrl, message.getGameCode());
		template.convertAndSend(destination, message.getPayload());
	}
	
	public void sendPlayerMessage(Message message) {
		message.getPlayers().forEach(player -> {
			String destination = String.format(playerUrl, player.getGameCode(), player.getPlayerCode());
			template.convertAndSend(destination, message.getPayload());
		});
	}
	
}
