package tv.gage.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tv.gage.common.Response;
import tv.gage.common.game.Game;
import tv.gage.common.game.Player;
import tv.gage.common.messaging.Message;
import tv.gage.common.util.JsonUtils;
import tv.gage.controller.service.GameService;
import tv.gage.controller.service.HubService;
import tv.gage.controller.service.MessagingService;
import tv.gage.controller.service.PlayerService;
import tv.gage.simon.engine.GameCommand;
import tv.gage.simon.engine.GameCommand.CommandType;

@RestController
@Api(tags="Admin")
@RequestMapping("/api/v1/admin")
public class AdminController {

	@Autowired
	private HubService hubService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private MessagingService messagingService;
	
	@CrossOrigin
	@ApiOperation(value="List Active Games")
	@GetMapping(value="/list")
	public Response activeGames() {
		List<Game> games = hubService.activeGames();
		return Response.builder()
				.result(games)
				.build();

	}
	
	@CrossOrigin
	@ApiOperation(value="Add Game")
	@PostMapping(value="/add/game")
	public Response addGame(
			@RequestParam(value = "gameName", required = true) String gameName,
			@RequestParam(value = "gameCode", required = true) String gameCode) {
		return gameService.addGame(gameName, gameCode);
	}

	
	@CrossOrigin
	@ApiOperation(value="Add Player")
	@PostMapping(value="/add/player")
	public Response addPlayer(
			@RequestParam(value = "gameCode", required = true) String gameCode,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "playerCode", required = true) String playerCode) {
		return playerService.addPlayer(gameCode, name, playerCode);
	}
	
	@CrossOrigin
	@ApiOperation(value="Start Game")
	@PostMapping(value="/game/start")
	public void startGame(
			@RequestParam(value = "gameCode", required = true) String gameCode) throws JsonProcessingException {
		GameCommand command = new GameCommand(CommandType.START);
		String jsonCommand = JsonUtils.ObjectToJson(command);
		gameService.sendGameCommand(gameCode, jsonCommand);
	}
	
	@CrossOrigin
	@ApiOperation(value="Out of Time")
	@PostMapping(value="/game/outOfTime")
	public void outOfTime(
			@RequestParam(value = "gameCode", required = true) String gameCode) throws JsonProcessingException {
		GameCommand command = new GameCommand(CommandType.OUT_OF_TIME);
		String jsonCommand = JsonUtils.ObjectToJson(command);
		gameService.sendGameCommand(gameCode, jsonCommand);
	}
	
	@CrossOrigin
	@ApiOperation(value="Send Message to Player")
	@PostMapping(value="/player/message")
	public void sendMessageToPlayer(
			@RequestParam(value = "gameCode", required = true) String gameCode,
			@RequestParam(value = "playerCode", required = true) String playerCode,
			@RequestParam(value = "payload", required = true) String payload) {
		Player player = Player.builder().playerCode(playerCode).gameCode(gameCode).build();
		Message message = Message.builder()
				.player(player)
				.payload(payload)
				.build();
		messagingService.sendPlayerMessage(message);
	}
	
}
