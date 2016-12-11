package com.cloud;

import com.cloud.util.ImageUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

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
	public void imageUtilTest() throws Exception {

	    String resizedImage = ImageUtil.createThumbnail(new File("/Users/micky/Downloads/005K1QsUgw1esq6037glzj30xc1e0ws7.jpg"), 200);
		System.out.println(String.format("image size %d image payload %s", resizedImage.length(), resizedImage));
	}

}
