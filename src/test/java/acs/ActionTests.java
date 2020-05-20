package acs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundary.ActionBoundary;
import acs.boundary.ElementBoundary;
import acs.boundary.UserBoundary;
import acs.boundary.boundaryUtils.Element;
import acs.boundary.boundaryUtils.ElementId;
import acs.boundary.boundaryUtils.InvokedBy;
import acs.boundary.boundaryUtils.Location;
import acs.boundary.boundaryUtils.NewUserDetails;
import acs.boundary.boundaryUtils.UserId;
import acs.data.UserRole;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActionTests {
	private int port;
	private String url;
	private String domain;
	private String adminEmail;
	private String managerEmail;
	private String playerEmail;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@Value("${spring.application.name:2020b.daniel.zusev}")
	public void setProjectName(String projectName) {
		this.domain = projectName;
		this.adminEmail = "admin@" + this.domain + ".com";
		this.managerEmail = "manager@" + this.domain + ".com";
		this.playerEmail = "player@" + this.domain + ".com";
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port + "/acs";
		this.restTemplate = new RestTemplate();
		// Create Admin
		NewUserDetails admin = new NewUserDetails();
		admin.setAvatar(":)");
		admin.setRole(UserRole.ADMIN);
		admin.setEmail(this.adminEmail);
		admin.setUsername("admin");
		this.restTemplate.postForObject(this.url + "/users", admin, UserBoundary.class);
		// Create Manager
		NewUserDetails manager = new NewUserDetails();
		manager.setAvatar(":)");
		manager.setRole(UserRole.MANAGER);
		manager.setEmail(this.managerEmail);
		manager.setUsername("manager");
		this.restTemplate.postForObject(this.url + "/users", manager, UserBoundary.class);
		//Create Player
		NewUserDetails player = new NewUserDetails();
		player.setAvatar(":)");
		player.setRole(UserRole.PLAYER);
		player.setEmail(this.playerEmail);
		player.setUsername("player");
		this.restTemplate.postForObject(this.url + "/users", player, UserBoundary.class);
	}
	
//	@AfterClass
//	public void deleteUsers() {
//		this.restTemplate.delete(url + "/admin/users/{domain}/{email}"
//				,this.domain,this.adminEmail);
//	}
	
	
	@AfterEach
	public void teardown() {
		this.restTemplate
		.delete(this.url + "/admin/actions/{domain}/{email}",domain,adminEmail);
	}
	
	@Test
	public void testInvokeNewActionAndValidateTheDatabaseContainsASingleAction(){
		ElementBoundary elementBoundary = new ElementBoundary(null, "regular", true,
				"dugma", new Date(), null, new Location(25.3, 13.2),
				new HashMap<String,Object>());
		 ElementBoundary elementBoundary1 = this.restTemplate.postForObject(this.url + "/elements/{managerDomain}/{managerEmail}"
				, elementBoundary, ElementBoundary.class, this.domain, this.managerEmail);
		ActionBoundary action = new ActionBoundary();
		action.setElement(new Element(new ElementId(this.domain,elementBoundary1.getElementId().getId())));
		action.setType("dugma");
		action.setInvokedBy(new InvokedBy(new UserId(this.domain, this.playerEmail)));
		action.setActionAttributes(new HashMap<String, Object>());
		this.restTemplate.postForObject(this.url + "/actions",action,Object.class);
		ActionBoundary[] actions = this.restTemplate.getForObject(this.url + "/admin/actions/{adminDomain}/{adminEmail}",
				ActionBoundary[].class,this.domain,this.adminEmail);
		assertThat(actions).hasSize(1);
	}
	
	@Test
	public void tesstGetAllActionsAndValidateTheDatabaseIsEmpty() {
		ActionBoundary[] actions = this.restTemplate.getForObject(this.url + "/admin/actions/{domain}/{email}",
				ActionBoundary[].class,domain,adminEmail);
		assertThat(actions).isEmpty();
	}
	
}
