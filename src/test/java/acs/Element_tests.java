package acs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import acs.logic.UnauthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;

import javax.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Element_tests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String projectName;
	private String managerEmail;

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
		this.managerEmail = "Manager@gmail.com";
		this.restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	public void dbInjection() {
		NewUserDetails adminUser = new NewUserDetails(UserRole.ADMIN, "Admin", "Admin@gmail.com", ";-)");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", adminUser, UserBoundary.class);
		NewUserDetails managerUser = new NewUserDetails(UserRole.MANAGER, "Manager", "Manager@gmail.com", ":-)");
		NewUserDetails simpleUser1 = new NewUserDetails(UserRole.PLAYER, "Player1", "Player1@gmail.com", ":)");
		NewUserDetails simpleUser2 = new NewUserDetails(UserRole.PLAYER, "Player2", "Player2@gmail.com", ":>)");
		NewUserDetails simpleUser3 = new NewUserDetails(UserRole.PLAYER, "Player3", "Player3@gmail.com", ":->)");
		
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", managerUser, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", simpleUser1, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", simpleUser2, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", simpleUser3, UserBoundary.class);
		
		ElementBoundary element1 = new ElementBoundary(new ElementId(this.projectName, ""), "garden", false, "rotshild", null,
				null, new Location(3.3, 4.5), new HashMap<>());
		ElementBoundary element2 = new ElementBoundary(new ElementId(this.projectName, ""), "garden", true, "tel aviv", null,
				null, new Location(3.3, 4.5), new HashMap<>());
		
		this.restTemplate.postForObject(this.url, element1, ElementBoundary.class,this.projectName, this.managerEmail);
		this.restTemplate.postForObject(this.url, element2, ElementBoundary.class,this.projectName, this.managerEmail);
		
	}
	
	
//	@AfterEach
	public void teardown() {
		this.restTemplate.delete("http://localhost:" + this.port + "/acs/admin/elements/{adminDomain}/{adminEmail}",
				this.projectName, "Admin@gmail.com");
	}

	@Test
	public void testCreateElementAndValidateThatReturnElementWithId() {

		// GIVEN the server is up AND database is empty
		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} AND send a element
		// boundary without Id
		ElementBoundary input = new ElementBoundary(new ElementId(this.projectName, ""), "test", true, "Element", null,
				null, new Location(3.3, 4.5), new HashMap<>());
		ElementBoundary output = this.restTemplate.postForObject(this.url, input, ElementBoundary.class,
				this.projectName, this.managerEmail);

		// THEN the server returns status 2xx
		// AND retrieves a element with a new Id
		assertThat(output.getElementId().getId()).isNotEmpty();

	}

	@Test
	public void testUpdateInvaildElementAndValidateThatExceptionIsThrow() {

		// GIVEN the server is up AND database is empty
		// WHEN I PUT /acs/elements/{managerDomain}/{managerEmail} AND send a element
		// boundary with null elementId
		ElementBoundary input = new ElementBoundary(null, "test", true, "Element", null, null, new Location(3.3, 4.5),
				new HashMap<>());

		
		// THEN the server returns status 4xx
		// AND throw exception
		assertThrows(Exception.class, () -> this.restTemplate.put(this.url, input, ElementBoundary.class,
				this.projectName, this.managerEmail));

	}

	@Test
	public void testPostNewElementReturnElementWithSameElementNameAttribute() throws Exception {
		// GIVEN server is up

		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} with new element
		ElementBoundary input = new ElementBoundary(new ElementId(projectName, null), "", null, "tomer", null,
				new CreatedBy(new UserId(this.projectName, this.managerEmail)), null, new HashMap<>());

		String managerDomain = input.getCreatedBy().getUserId().getDomain();
		String managerEmail = input.getCreatedBy().getUserId().getEmail();

		ElementBoundary output = this.restTemplate.postForObject(this.url, input, ElementBoundary.class, managerDomain,
				managerEmail);

		// THEN the server returns status 2xx
		// AND retrieves a element with same name as sent to server
		if (!output.getName().equals(input.getName())) {
			throw new Exception("expected simplar name to input but received: " + output.getName());
		}
	}

	@Test
	public void testPostNewElementAndValidateTheDatabseContainsElementWithTheSameElementAttribute() throws Exception {
		// GIVEN server is up
		
		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} with new element
		ElementBoundary input = new ElementBoundary(
					new ElementId(projectName,"tester"), "tm", true, "tomer", null, new CreatedBy(new UserId(this.projectName, this.managerEmail)), null, new HashMap<>());
		
		String managerDomain = input.getCreatedBy().getUserId().getDomain();
		String managerEmail = input.getCreatedBy().getUserId().getEmail();
		this.restTemplate
		.postForObject(
				this.url, 
				input, 
				ElementBoundary.class,managerDomain,managerEmail);
		
		// THEN server contains this Element in the database
		// AND it's element attribute is similar input's
		ElementBoundary output = 
		  this.restTemplate
			.getForObject(this.url + "{elementDomain}/{elementId}" , ElementBoundary.class,managerDomain, managerEmail, input.getElementId().getDomain(), input.getElementId().getId());
		
					
		assertThat(output)
			.extracting("name", "type","active")
			.containsExactlyInAnyOrder(input.getName(),input.getType(),input.getActive());

	}
	
	@Test
	public void testCreateNewElementByPlayerAndTrowsException() throws Exception{
		ElementBoundary element = new ElementBoundary(new ElementId(this.projectName, ""), "garden", false, "rehovot", null,
				new CreatedBy(new UserId(this.projectName, "Player12@gmail.com")), new Location(3.35, 4.545), new HashMap<>());
		
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, element, ElementBoundary.class, this.projectName, "Player1@gmail.com"));
		
	}
	
	@Test
	public void testUpdateElementByPlayerAndThrowsException() throws UnauthorizedException{
			
	}
}
