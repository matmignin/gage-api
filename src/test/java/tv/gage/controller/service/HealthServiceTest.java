package tv.gage.controller.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tv.gage.common.Response;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class HealthServiceTest {
	
	@Autowired
	private HealthService healthService;

	@Test
	public void statusTest() {
		Response response = healthService.status();
		String actual = (String) response.getResult();
		assertEquals("running", actual);
	}

}
