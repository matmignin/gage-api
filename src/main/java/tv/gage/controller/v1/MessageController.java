package tv.gage.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import tv.gage.controller.service.GameService;

@Controller
public class MessageController {
	
	@Autowired
	private GameService gameService;
	
	@MessageMapping("/command/game/{gameCode}")
	public void gameCommand(
			@DestinationVariable String gameCode,
			String jsonCommand) {
		gameService.sendGameCommand(gameCode, jsonCommand);
	}
	
	@MessageMapping("/command/game/{gameCode}/player/{playerCode}")
	public void playerCommand(
			@DestinationVariable String gameCode, 
			@DestinationVariable String playerCode, 
			String jsonCommand) throws Exception {
		gameService.sendPlayerCommand(gameCode, playerCode, jsonCommand);
	}

}
