package optiheat.storage;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.ws.rs.core.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IntegrationTests
{
    Logger logger = LoggerFactory.getLogger(IntegrationTests.class);

    @Before
    public void setEnvironment()
    {
        try
        {
            mvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .build();
        }
        catch (Exception ex)
        {
            logger.error("Could not read mock data from files!", ex);
        }
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @WithMockUser(value = "spring")
    @Test
    public void testUserService() throws Exception
    {
        ResultActions ra = mvc.perform(post("/addRoom").contentType(MediaType.TEXT_PLAIN));
        /*ResultActions ra = mvc.perform(post("/addRoom").contentType(MediaType.TEXT_PLAIN));
        String userId = UUID.randomUUID().toString();
        ResultActions res1 = mvc.perform(get("/Engine/UserService/getAuthenticatedUserByParameter/").param("userId", userId).contentType(MediaType.APPLICATION_JSON));
        ResultActions res2 = mvc.perform(get("/Engine/UserService/getAuthenticatedUser").content(userId).contentType(MediaType.APPLICATION_JSON));*/
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
