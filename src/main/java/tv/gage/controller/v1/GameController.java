package tv.gage.controller.v1;

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
import tv.gage.controller.service.GameService;

@RestController
@Api(tags="Game")
@RequestMapping("/api/v1/game")
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@CrossOrigin
	@ApiOperation(value="List Available Games")
	@GetMapping(value="/list")
	public Response availableGames() {
		return gameService.availableGames();
	}
	
	@CrossOrigin
	@ApiOperation(value="Add Game")
	@PostMapping(value="/add")
	public Response addGame(
			@RequestParam(value = "gameName", required = true) String gameName) {
		return gameService.addGame(gameName);
	}
	
	@CrossOrigin
	@ApiOperation(value="Remove Game")
	@PostMapping(value="/remove")
	public Response removeGame(
			@RequestParam(value = "gameCode", required = true) String gameCode) {
		return gameService.removeGame(gameCode);
	}
	
	@CrossOrigin
	@ApiOperation(value="Is Game Ready")
	@PostMapping(value="/isReadyToPlay")
	public Response isReadyToPlay(
			@RequestParam(value = "gameCode", required = true) String gameCode) {
		return gameService.isReadyToPlay(gameCode);
	}

}
