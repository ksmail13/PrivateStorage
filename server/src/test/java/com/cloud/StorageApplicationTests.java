package com.cloud;

import com.cloud.account.LoginInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StorageApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void loginTest() throws Exception {
		LoginInfo info = new LoginInfo();
		info.setUserId("test");
		info.setPassword("1111");
		ObjectMapper om = new ObjectMapper();
		mvc.perform(MockMvcRequestBuilders.post("/access_token")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(om.writeValueAsString(info)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(info.getUserId())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.authorities[*].authority", CoreMatchers.hasItem("USER")));
	}

}
