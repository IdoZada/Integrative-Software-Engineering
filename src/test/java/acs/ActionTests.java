package acs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundary.ActionBoundary;
import acs.boundary.boundaryUtils.Element;
import acs.boundary.boundaryUtils.ElementId;
import acs.boundary.boundaryUtils.InvokedBy;
import acs.boundary.boundaryUtils.UserId;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionTests {
	private int port;
	private String url;
	private String domain;
	private String adminEmail;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@Value("${spring.application.name:2020b.daniel.zusev}")
	public void setProjectName(String projectName) {
		this.domain = projectName;
		this.adminEmail = "admin@" + this.domain + ".com";
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/acs";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void setup() {
		// do nothing
	}
	
	@AfterEach
	public void teardown() {
		this.restTemplate
			.delete(this.url + "/admin/actions/{domain}/{email}",domain,adminEmail);
	}
	
	@Test
	public void testInvokeNewActionAndValidateTheDatabaseContainsASingleAction(){
		ActionBoundary action = new ActionBoundary();
		action.setElement(new Element(new ElementId(domain,"1234")));
		action.setType("dugma");
		action.setInvokedBy(new InvokedBy(new UserId(domain,adminEmail)));
		action.setActionAttributes(new HashMap<String, Object>());
		this.restTemplate.postForObject(this.url + "/actions",action,Object.class);
		ActionBoundary[] actions = this.restTemplate.getForObject(this.url + "/admin/actions/{domain}/{email}",
				ActionBoundary[].class,domain,adminEmail);
		assertThat(actions).hasSize(1);
	}
	
	@Test
	public void tesstGetAllActionsAndValidateTheDatabaseIsEmpty() {
		ActionBoundary[] actions = this.restTemplate.getForObject(this.url + "/admin/actions/{domain}/{email}",
				ActionBoundary[].class,domain,adminEmail);
		assertThat(actions).isEmpty();
	}
	
}
