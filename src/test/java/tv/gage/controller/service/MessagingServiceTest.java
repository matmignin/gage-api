package tv.gage.controller.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import tv.gage.common.game.Player;
import tv.gage.common.messaging.Message;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class MessagingServiceTest {
	
	@Autowired
	private MessagingService messagingService;
	
	@MockBean
	SimpMessagingTemplate templateMock;
	
	@Before
	public void setup() {
		Mockito.doNothing().when(templateMock).convertAndSend(Mockito.anyString(), Mockito.any(Object.class));
	}

	@Test
	public void sendGameMessageTest() {
		Message message = Message.builder().payload("test").build();
		messagingService.sendGameMessage(message);
	}
	
	@Test
	public void sendPlayerMessageTest() {
		Message message = Message.builder()
				.payload("test")
				.player(
						Player.builder().playerCode("PLYR")
						.build())
				.build();
		messagingService.sendPlayerMessage(message);
	}

}
