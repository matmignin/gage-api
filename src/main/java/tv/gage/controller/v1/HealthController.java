package tv.gage.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tv.gage.common.Response;
import tv.gage.controller.service.HealthService;

@RestController
@RequestMapping("/api/v1/health")
@Api(tags="Health Check")
public class HealthController {

	@Autowired
	private HealthService healthService;
	
	@CrossOrigin
	@GetMapping(value="/status")
	@ApiOperation(value="Status")
	public Response status() {
		return healthService.status();
	}
	
}
