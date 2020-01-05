package tv.gage.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tv.gage.common.Response;
import tv.gage.controller.service.PlayerService;

@RestController
@Api(tags="Player")
@RequestMapping("/api/v1/player")
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
	@CrossOrigin
	@ApiOperation(value="Add Player")
	@PostMapping(value="/add")
	public Response addPlayer(
			@RequestParam(value = "gameCode", required = true) String gameCode,
			@RequestParam(value = "name", required = true) String name) {
		return playerService.addPlayer(gameCode, name);
	}
	
	@CrossOrigin
	@ApiOperation(value="Remove Player")
	@PostMapping(value="/remove")
	public Response removePlayer(
			@RequestParam(value = "gameCode", required = true) String gameCode,
			@RequestParam(value = "playerCode", required = true) String playerCode) {
		return playerService.removePlayer(gameCode, playerCode);
	}
	
	@CrossOrigin
	@ApiOperation(value="List Players")
	@PostMapping(value="/list")
	public Response listPlayers(
			@RequestParam(value = "gameCode", required = true) String gameCode) {
		return playerService.getPlayers(gameCode);
	}
	
}
