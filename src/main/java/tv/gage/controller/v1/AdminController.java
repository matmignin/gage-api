package tv.gage.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tv.gage.common.Response;
import tv.gage.common.game.Game;
import tv.gage.controller.service.GameService;
import tv.gage.controller.service.HubService;
import tv.gage.controller.service.PlayerService;

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
	
	@CrossOrigin
	@ApiOperation(value="List Active Games")
	@GetMapping(value="/game/list")
	public Response activeGames() {
		List<Game> games = hubService.activeGames();
		return Response.builder()
				.result(games)
				.build();

	}
	
	@CrossOrigin
	@ApiOperation(value="Add Game")
	@PostMapping(value="/game/add")
	public Response addGame(
			@RequestParam(value = "gameName", required = true) String gameName,
			@RequestParam(value = "gameCode", required = true) String gameCode) {
		return gameService.addGame(gameName, gameCode);
	}

	
	@CrossOrigin
	@ApiOperation(value="Add Player")
	@PostMapping(value="/player/add")
	public Response addPlayer(
			@RequestParam(value = "gameCode", required = true) String gameCode,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "playerCode", required = true) String playerCode) {
		return playerService.addPlayer(gameCode, name, playerCode);
	}
	
}
