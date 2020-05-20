package acs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import acs.boundary.UserBoundary;
import acs.boundary.boundaryUtils.NewUserDetails;
import acs.data.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class User_tests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String projectName;
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
	public void init() {
		// /acs/users
		this.url = "http://localhost:" + this.port + "/acs/users";
		this.adminEmail = "admin@gmail.com";
		this.restTemplate = new RestTemplate();

//		Create Admin to delete all action tests
		NewUserDetails admin = new NewUserDetails();
		admin.setAvatar(":)");
		admin.setRole(UserRole.ADMIN);
		admin.setEmail(this.adminEmail);
		admin.setUsername("admin");
		this.restTemplate.postForObject(this.url, admin, UserBoundary.class);

	}

	@AfterEach
	public void teardown() { // Admin URL : Delete All Users
		System.err.println("enterterDown");
		this.restTemplate.delete("http://localhost:" + this.port + "/acs/admin/users/{adminDomain}/{adminEmail}",
				this.projectName, this.adminEmail);
	}

	@Test
	public void testPostNewUserReturnUserWithId() throws Exception {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /acs/users AND send a user boundary without Id
		NewUserDetails input = new NewUserDetails(UserRole.PLAYER, "Test", "tester@gmail.com", ":-)");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);

		// THEN the server returns status 2xx
		// AND retrieves a user with a new Id
		if (output.getUserId() == null) {
			throw new Exception("Not null Id Was Expected");
		}
	}

	@Test
	public void testUpdateUserDetailsAndValidation() {
		// GIVEN the server is up AND database is empty
		// WHEN I POST /acs/users AND send a new user details
		NewUserDetails input = new NewUserDetails(UserRole.PLAYER, "Tester", "tester@gmail.com", ";--)");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		String domain = output.getUserId().getDomain();
		String email = output.getUserId().getEmail();

		// WHEN I update avatar to ":-)"
		// AND update role to "PLAYER"
		// AND update user name to "Tester"
		output.setAvatar(":-)");
		output.setRole(UserRole.PLAYER);
		output.setUsername("Test Successful");

		this.restTemplate.put(this.url + "/{domain}/{email}", output, domain, email);
		// THEN the database is updated with the new value
		UserBoundary userBoundary = this.restTemplate.getForObject(this.url + "/login/{domain}/{email}",
				UserBoundary.class, domain, email);
		assertThat(userBoundary).usingRecursiveComparison().isEqualTo(output);
	}

	@Test
	public void testUserLoginAndCheckDetails() throws Exception { // TODO test not working
		// GIVEN the server is up AND database is empty
		// WHEN I POST /acs/users AND send a new user details
		
		NewUserDetails input = new NewUserDetails(UserRole.PLAYER, "Tester", "tester@gmail.com", ";-)");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		String domain = output.getUserId().getDomain();
		String email = output.getUserId().getEmail();
		UserBoundary retrievenUser = this.restTemplate.getForObject(this.url + "/login/{domain}/{email}",
				UserBoundary.class, domain, email);

		assertThat(retrievenUser).extracting("role", "username", "avatar").containsExactlyInAnyOrder(output.getRole(),
				output.getUsername(), output.getAvatar());
	}

	@Test
	public void testGetAllUsersAfterDatabaseIsInitializedWith6UsersWithoutAdmin() {
		// GIVEN the database contains 5 users
		// POST /acs/users
		List<UserBoundary> databaseContent = IntStream.range(1, 6) // Stream<Integer> 1,2,3,4,5
				.mapToObj(i -> "User" + i) // Stream<String>
				.map(newUser -> new NewUserDetails(UserRole.PLAYER, newUser, newUser + "@gmail.com", ":-P"))// Stream<NewUserDetails>
				.map(newUser -> this.restTemplate.postForObject(this.url, newUser, UserBoundary.class))// Stream<UserBoundary>
				.collect(Collectors.toList());// List<UserBoundary>

		String url = "http://localhost:" + this.port + "/acs/admin/users/{adminDomain}/{adminEmail}";
		// WHEN I GET /acs/admin/users/{adminDomain}/{adminEmail}
		UserBoundary[] result = this.restTemplate.getForObject(url, UserBoundary[].class, this.projectName, adminEmail);
		
		List<UserBoundary> users = 
				Arrays.stream(result)
				.filter(user->user.getRole() != UserRole.ADMIN)
				.collect(Collectors.toList());

		
		// THEN The server returns status 2xx
		// AND the response contains the exact 5 users in the database without user Admin
		assertThat(users).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyInAnyOrderElementsOf(databaseContent);
	}
}
