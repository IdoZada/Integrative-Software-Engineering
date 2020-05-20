package acs;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundary.ElementBoundary;
import acs.boundary.UserBoundary;
import acs.boundary.boundaryUtils.CreatedBy;
import acs.boundary.boundaryUtils.ElementId;
import acs.boundary.boundaryUtils.Location;
import acs.boundary.boundaryUtils.NewUserDetails;
import acs.boundary.boundaryUtils.UserId;
import acs.data.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Element_tests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String projectName;
	private String playerEmail;
	private String managerEmail;
	private String adminEmail;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	// inject value from configuration or use default value
	@Value("${spring.application.name:2020b.daniel.zusev}")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@PostConstruct
	public void init() {// url - > /acs/elements/{managerDomain}/{managerEmail}
		this.url = "http://localhost:" + this.port + "/acs/elements/{managerDomain}/{managerEmail}";
		this.adminEmail = "admin@gmail.com";
		this.managerEmail = "manager@gmail.com";
		this.playerEmail = "player@gmail.com";
		this.restTemplate = new RestTemplate();
		
		
//		Create Admin
		NewUserDetails admin = new NewUserDetails();
		admin.setAvatar(":)");
		admin.setRole(UserRole.ADMIN);
		admin.setEmail(this.adminEmail);
		admin.setUsername("admin");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", admin, UserBoundary.class);
		
//		Create Manager
		NewUserDetails manager = new NewUserDetails();
		manager.setAvatar(":)");
		manager.setRole(UserRole.MANAGER);
		manager.setEmail(this.managerEmail);
		manager.setUsername("manager");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", manager, UserBoundary.class);
		
	
		//Create Player 
		NewUserDetails player = new NewUserDetails();
		player.setAvatar(":)");
		player.setRole(UserRole.PLAYER);
		player.setEmail(this.playerEmail);
		player.setUsername("player");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", player, UserBoundary.class);
		
	}
	

	@AfterEach
	public void teardown() {
		this.restTemplate.delete("http://localhost:" + this.port + "/acs/admin/elements/{adminDomain}/{adminEmail}",
				this.projectName, this.adminEmail);
	}
	
	@Test
	public void testCreateElementAndValidateThatReturnElementWithId() {

		// GIVEN the server is up AND database is empty
		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} AND send a element
		// boundary without Id
		ElementBoundary input = new ElementBoundary(new ElementId(this.projectName,null), "test", true, "Element", null,
				new CreatedBy(new UserId(this.projectName, this.managerEmail)), new Location(3.3, 4.5), new HashMap<>());
		ElementBoundary output = this.restTemplate.postForObject(this.url, input, ElementBoundary.class,
				this.projectName, this.managerEmail);

		// THEN the server returns status 2xx
		// AND retrieves a element with a new Id
		assertThat(output.getElementId().getId()).isNotEmpty();

	}
	
	@Test
	public void testUpdateInvaildElementAndValidateThatExceptionIsThrow() {

		// GIVEN the server is up AND database is empty
		// WHEN I PUT /acs/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId} AND send a element
		// boundary with null elementId
		ElementBoundary input = new ElementBoundary(new ElementId(this.projectName,null), "test", true, "Element",null , new CreatedBy(new UserId(this.projectName, this.playerEmail)), new Location(3.3, 4.5),new HashMap<>());

		
		// THEN the server returns status 4xx
		// AND throw exception
		assertThrows(Exception.class, () -> this.restTemplate.put(this.url + "/{elementDomain}/{elementId}", input, ElementBoundary.class,
				this.projectName, this.managerEmail,this.projectName , "abcde"));

	}

	@Test
	public void testPostNewElementReturnElementWithSameElementNameAttribute() throws Exception {
		// GIVEN server is up

		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} with new element
		ElementBoundary input = new ElementBoundary(new ElementId(projectName, null), "NewElement", true, "tomer", null,
				new CreatedBy(new UserId(this.projectName, this.managerEmail)), new Location(3.3, 4.5), new HashMap<>());

		

		ElementBoundary output = this.restTemplate.postForObject(this.url, input, ElementBoundary.class, this.projectName,
				this.managerEmail);

		// THEN the server returns status 2xx
		// AND retrieves a element with same name as sent to server
		if (!output.getName().equals(input.getName())) {
			throw new Exception("expected simplar name to input but received: " + output.getName());
		}
	}
		
	@Test
	public void testPostNewElementAndValidateTheDatabseContainsASingleElementWithTheSameElementAttribute() throws Exception {
		// GIVEN server is up
		
		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} with new element
		ElementBoundary input = new ElementBoundary(new ElementId(this.projectName,null), "tm", true, "tomer", null, new CreatedBy(new UserId(this.projectName, this.managerEmail)), null, new HashMap<>());
		
//		String managerDomain = input.getCreatedBy().getUserId().getDomain();
//		String managerEmail = input.getCreatedBy().getUserId().getEmail();
		this.restTemplate
		.postForObject(
				this.url, 
				input, 
				ElementBoundary.class,this.projectName,managerEmail);
		
		// THEN server contains a single Element in the database
		// AND it's element attribute is similar input's
		ElementBoundary[] output = 
		  this.restTemplate
			.getForObject(this.url +"?size=5&page=0", ElementBoundary[].class,this.projectName, this.managerEmail);
		
		assertThat(output).hasSize(1);
					
		assertThat(output[0])
			.extracting(
				"name", 
				"type", 
				"active")
			.containsExactly(
				input.getName(),
				input.getType(),
				input.getActive());

	}
	
	@Test
	public void testGetAllElementsByPlayerNotContainsNonActiveElements() throws Exception{
		// GIVEN the database contains 5 elements
				// POST /acs/elements/{managerDomain}/{managerEmail}
				List<ElementBoundary> databaseContent = IntStream.range(1, 5) // Stream<Integer> 1,2,3,4,5
						.mapToObj(i -> "Element" + i) // Stream<String>
						.map(newElement -> new ElementBoundary(new ElementId(this.projectName,null), newElement, true, newElement, null, new CreatedBy(new UserId(this.projectName, this.managerEmail)), null, new HashMap<>()))// Stream<ElementBoundary>
						.map(newElement -> this.restTemplate.postForObject(this.url, newElement, ElementBoundary.class , this.projectName,this.managerEmail))// Stream<UserBoundary>
						.collect(Collectors.toList());// List<UserBoundary>
				
		//AND Add 2 inactive elements to database 
				 
				 databaseContent.add(
						  this.restTemplate
							.postForObject(this.url, new ElementBoundary(new ElementId(this.projectName,null), "Element 5", false, "Element 5", null, new CreatedBy(new UserId(this.projectName, this.managerEmail)), null, new HashMap<>()), 
									ElementBoundary.class, this.projectName,this.managerEmail));
				 
				 databaseContent.add(
						  this.restTemplate
							.postForObject(this.url, new ElementBoundary(new ElementId(this.projectName,null), "Element 6", false, "Element 6", null, new CreatedBy(new UserId(this.projectName, this.managerEmail)), null, new HashMap<>()), 
									ElementBoundary.class, this.projectName,this.managerEmail));
				 assertThat(databaseContent).hasSize(6);	 
	}
	
	
//	public void testGetAllElementsWithPagination() {
//		//GIVEN server is up and database contains 4 elements 
//		//WHEN we GET "/acs/elements/{userDomain}/{userEmail}?size=4&page=0"
//		
//		
//		//THEN 
//		
//			
//	}
	
	
}
