package acs;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import acs.boundary.UserBoundary;
import acs.data.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class User_tests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		// /acs/users
		this.url = "http://localhost:" + this.port + "/acs/users";
		this.restTemplate = new RestTemplate();
	}
	
	
	@AfterEach
	public void teardown() { //Admin URL : Delete All Users
		this.restTemplate
			.delete("http://localhost:" + this.port + "/acs/admin/users/2020b.daniel.zusev/admin@gmail.com");
	}
	
	@Test
	public void testPostNewUserReturnUserWithId() throws Exception {
		// GIVEN the server is up AND database is empty
		//WHEN I POST /acs/users AND send a user boundary without Id
		NewUserDetails input = new NewUserDetails(UserRole.PLAYER, "", "tester@gmail.com", "");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		
		// THEN the server returns status 2xx 
		// AND retrieves a user with a new Id 
		if(output.getUserId() == null) {
			throw new Exception("Not null Id Was Expected");
		}
	}

	@Test
	public void testUpdateUserDetailsAndValidation() {
		// GIVEN the server is up AND database is empty
		//WHEN I POST /acs/users AND send a new user details
		NewUserDetails input = new NewUserDetails(UserRole.PLAYER, "", "tester@gmail.com", "");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		String domain = output.getUserId().getDomain();
		String email = output.getUserId().getEmail();
		
		// WHEN I update avatar to ":-)"
		// AND update role to "PLAYER"
		// AND update user name to "Tester"
		output.setAvatar(":-)");
		output.setRole(UserRole.PLAYER);
		output.setUserName("Tester");
		
		this.restTemplate.put(this.url + "/{domain}/{email}" , output, domain, email);
		// THEN the database is updated with the new value	
		UserBoundary userBoundary = this.restTemplate.getForObject(this.url + "/login/{domain}/{email}", UserBoundary.class, domain,email);
		assertThat(userBoundary).usingRecursiveComparison().isEqualTo(output);
	}
	
	@Test
	public void testUserLoginAndCheckDetails() throws Exception { // TODO test not working
		// GIVEN the server is up AND database is empty
		//WHEN I POST /acs/users AND send a new user details
		NewUserDetails input = new NewUserDetails(UserRole.PLAYER,"Tester","tester@gmail.com",";-)");
		UserBoundary output = this.restTemplate.postForObject(this.url, input, UserBoundary.class);
		String domain = output.getUserId().getDomain();
		String email = output.getUserId().getEmail();
		UserBoundary retrievenUser = this.restTemplate.getForObject(this.url + "/login/{domain}/{email}", UserBoundary.class, domain,email);

		assertThat(retrievenUser)
				.extracting("role","userName", "avatar")
				.containsExactlyInAnyOrder(output.getRole(), output.getUserName(), output.getAvatar());
	}
}
