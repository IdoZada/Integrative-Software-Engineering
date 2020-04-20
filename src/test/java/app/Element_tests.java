package app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.Application;

import javax.annotation.PostConstruct;


@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class Element_tests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		///acs/elements/{managerDomain}/{managerEmail}
		this.url = "http://localhost:" + this.port + "/acs/admin/elements/hjfk/jff";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		// do nothing
	}
	
	@AfterEach
	public void teardown() {
		this.restTemplate
			.delete(this.url);
	}
	
	@Test
	public void testContext() {
		
	}
}
