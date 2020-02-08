package tv.gage.controller.service;

import org.springframework.stereotype.Service;

import tv.gage.common.Response;

@Service
public class HealthService {

	public Response status() {
		return Response.builder()
				.result("running")
				.build();
	}
	
}
