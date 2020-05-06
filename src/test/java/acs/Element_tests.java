package acs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundary.ElementBoundary;

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
	private String email;

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
		this.email = "daniel@gmail.com";
		this.restTemplate = new RestTemplate();
	}

	@AfterEach
	public void teardown() {
		this.restTemplate.delete("http://localhost:" + this.port + "/acs/admin/elements/{adminDomain}/{adminEmail}",
				this.projectName, this.email);
	}

	@Test
	public void testCreateElementAndValidateThatReturnElementWithId() {

		// GIVEN the server is up AND database is empty
		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} AND send a element
		// boundary without Id
		ElementBoundary input = new ElementBoundary(new ElementId(this.projectName, ""), "test", true, "Element", null,
				null, new Location(3.3, 4.5), new HashMap<>());
		ElementBoundary output = this.restTemplate.postForObject(this.url, input, ElementBoundary.class,
				this.projectName, this.email);

		// THEN the server returns status 2xx
		// AND retrieves a element with a new Id
		assertThat(output.getElementId().getId()).isNotEmpty();

	}

	@Test
	public void testCreateInvaildElementAndValidateThatExceptionIsThrow() {

		// GIVEN the server is up AND database is empty
		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} AND send a element
		// boundary with null elementId
		ElementBoundary input = new ElementBoundary(null, "test", true, "Element", null, null, new Location(3.3, 4.5),
				new HashMap<>());

		// THEN the server returns status 5xx
		// AND throw exception
		assertThrows(Exception.class, () -> this.restTemplate.postForObject(this.url, input, ElementBoundary.class,
				this.projectName, this.email));

	}

	@Test
	public void testPostNewElementReturnElementWithSameElementNameAttribute() throws Exception {
		// GIVEN server is up

		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} with new element
		ElementBoundary input = new ElementBoundary(new ElementId(projectName, null), "", null, "tomer", null,
				new CreatedBy(new UserId("2020t", "tomer@gmail.com")), null, new HashMap<>());

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
	public void testPostNewElementAndValidateTheDatabseContainsASingleElementWithTheSameElementAttribute() throws Exception {
		// GIVEN server is up
		
		// WHEN I POST /acs/elements/{managerDomain}/{managerEmail} with new element
		ElementBoundary input = new ElementBoundary(new ElementId(projectName,null), "tm", true, "tomer", null, new CreatedBy(new UserId("2020t", "tomer@gmail.com")), null, new HashMap<>());
		
		String managerDomain = input.getCreatedBy().getUserId().getDomain();
		String managerEmail = input.getCreatedBy().getUserId().getEmail();
		this.restTemplate
		.postForObject(
				this.url, 
				input, 
				ElementBoundary.class,managerDomain,managerEmail);
		
		// THEN server contains a single Element in the database
		// AND it's element attribute is similar input's
		ElementBoundary[] output = 
		  this.restTemplate
			.getForObject(this.url, ElementBoundary[].class,managerDomain, managerEmail);
		
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
	
	
}
