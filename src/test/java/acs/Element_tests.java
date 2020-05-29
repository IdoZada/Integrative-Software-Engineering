package acs;

import org.junit.AfterClass;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Element_tests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String projectName;
	private String managerEmail;
	private ElementBoundary gardenElement1;
	private ElementBoundary gardenElement2;

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
		NewUserDetails adminUser = new NewUserDetails(UserRole.ADMIN, "Admin", "Admin@gmail.com", ";-)");
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", adminUser, UserBoundary.class);
	}
	
	@BeforeEach
	public void dbInjection() {
		NewUserDetails managerUser = new NewUserDetails(UserRole.MANAGER, "Manager", "Manager@gmail.com", ":-)");
		NewUserDetails simpleUser1 = new NewUserDetails(UserRole.PLAYER, "Player1", "Player1@gmail.com", ":)");
		NewUserDetails simpleUser2 = new NewUserDetails(UserRole.PLAYER, "Player2", "Player2@gmail.com", ":>)");
		NewUserDetails simpleUser3 = new NewUserDetails(UserRole.PLAYER, "Player3", "Player3@gmail.com", ":->)");
		
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", managerUser, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", simpleUser1, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", simpleUser2, UserBoundary.class);
		this.restTemplate.postForObject("http://localhost:" + this.port + "/acs/users", simpleUser3, UserBoundary.class);
		
		ElementBoundary element1 = new ElementBoundary(new ElementId(this.projectName, ""), "Garden", false, "rotshild", null,
				null, new Location(31.877854, 34.816920), new HashMap<>());
		ElementBoundary element2 = new ElementBoundary(new ElementId(this.projectName, ""), "Garden", true, "tel aviv", null,
				null, new Location(31.878774, 34.812328), new HashMap<>());
		
		gardenElement1 = this.restTemplate.postForObject(this.url, element1, ElementBoundary.class,this.projectName, this.managerEmail);
		gardenElement2 = this.restTemplate.postForObject(this.url, element2, ElementBoundary.class,this.projectName, this.managerEmail);
	}
	
	
	@AfterEach
	public void teardown() {
		this.restTemplate.delete("http://localhost:" + this.port + "/acs/admin/elements/{adminDomain}/{adminEmail}",
				this.projectName, "Admin@gmail.com");
		this.restTemplate.delete("http://localhost:" + this.port + "/acs/admin/users/{adminDomain}/{adminEmail}",
				this.projectName, "Admin@gmail.com");
	}
	
	@AfterClass
	public void deleteUsers() {
		this.restTemplate.delete("http://localhost:" + this.port + "/acs/admin/users/{adminDomain}/{adminEmail}", this.projectName, "Admin@gmail.com");
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
		ElementBoundary input = new ElementBoundary(new ElementId(projectName, null), "", null, "Ido", null,
				new CreatedBy(new UserId(this.projectName, this.managerEmail)), new Location(3.3, 4.5), new HashMap<>());

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
					new ElementId(projectName,"tester"), "tm", true, "Ido", null, new CreatedBy(new UserId(this.projectName, this.managerEmail)), new Location(23.3,25.1), new HashMap<>());
		
		String managerDomain = input.getCreatedBy().getUserId().getDomain();
		String managerEmail = input.getCreatedBy().getUserId().getEmail();
		ElementBoundary elementBoundary = 
				this.restTemplate
								.postForObject(
										this.url, 
										input, 
										ElementBoundary.class,managerDomain,managerEmail);
								
		// THEN server contains this Element in the database
		// AND it's element attribute is similar input's
		ElementBoundary output = 
		  this.restTemplate
			.getForObject(this.url + "/{elementDomain}/{elementId}" ,
					ElementBoundary.class,
					managerDomain, managerEmail, elementBoundary.getElementId().getDomain(), elementBoundary.getElementId().getId());
		
					
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
	public void testGetAllElementsByManagerWithDistanceOfOneKm() {
		// GIVEN server is up
		//AND DB server is up and have to elements
		//WHEN i GET "/acs/elements/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}"
		
		
		ElementBoundary[] element = this.restTemplate.getForObject(
				"http://localhost:" + this.port + "/acs/elements/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}?size=5&page=0"
				, ElementBoundary[].class
				, this.projectName,this.managerEmail,31.878501, 34.814066,1,2,0);
		
		//THEN we get 2 elements
		assertThat(element).hasSize(2);
		
//		"/acs/elements/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}"
//		31.879249, 34.814715 
//		
//		31.861808, 34.804417
		
	}
	

	@Test
	public void testUpdateElementWithActiveByManger() {
		
		ElementBoundary element3 = new ElementBoundary(new ElementId(this.projectName, ""), "garden", false, "tel aviv", null,
				null, new Location(31.880408,  34.812803), new HashMap<>());
		ElementBoundary update = new ElementBoundary(null, "garden", true, "tel aviv", null,
				null, null, null);
		
		
		ElementBoundary elementBoundary = this.restTemplate.postForObject(this.url, element3, ElementBoundary.class,this.projectName, this.managerEmail);
		this.restTemplate.put(this.url + "/{elementDomain}/{elementId}",
				update,
				this.projectName,
				this.managerEmail,
				elementBoundary.getElementId().getDomain(),
				elementBoundary.getElementId().getId());
		
		ElementBoundary output = this.restTemplate.getForObject(
				this.url + "/{elementDomain}/{elementId}",
				ElementBoundary.class,
				this.projectName,
				this.managerEmail,
				elementBoundary.getElementId().getDomain(),
				elementBoundary.getElementId().getId());
		
		assertThat(output).extracting("active").isEqualTo(true);
		
	}
	
	@Test
	public void testBindElement() {
		ElementBoundary facilityElement1 = new ElementBoundary(new ElementId(this.projectName, ""), "Facility", true, "tel aviv", null,
				null, new Location(31.878774, 34.812328), new HashMap<>());
		facilityElement1 = this.restTemplate.postForObject(this.url, facilityElement1, ElementBoundary.class,this.projectName, this.managerEmail);

		this.restTemplate.put(this.url + 
				"/{elementDomain}/{elementId}/children",
				facilityElement1.getElementId(),
				this.projectName,
				this.managerEmail,
				this.projectName,
				this.gardenElement1.getElementId().getId());
		assertThat(this.restTemplate.getForObject(this.url + 
				"/{elementDomain}/{elementId}/children",
				ElementBoundary[].class,
				this.projectName,
				this.managerEmail,
				this.projectName,
				this.gardenElement1.getElementId().getId()))
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactly(facilityElement1);
	}
}
