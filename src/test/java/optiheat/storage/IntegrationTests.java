package optiheat.storage;


import com.fasterxml.jackson.databind.ObjectMapper;
import optiheat.storage.controller.exception.ConflictException;
import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.User;
import optiheat.storage.repository.UserRepository;
import org.junit.Assert;
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
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IntegrationTests
{
    Logger logger = LoggerFactory.getLogger(IntegrationTests.class);
    MockData mockDataPool;

    @Before
    public void setEnvironment()
    {
        try
        {
            mvc = MockMvcBuilders
                    .webAppContextSetup(context)
                    .build();
            mockDataPool = new MockData();
            mockDataPool.populateMockDataFromFiles(UUID.randomUUID().toString());
        }
        catch (Exception ex)
        {
            logger.error("Could not read mock data from files!", ex);
        }
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @WithMockUser(value = "spring")
    @Test
    public void testUserService() throws Exception
    {
        // ----------------> createUser
        // 1: User does not exist in DB
        User user_FLAT = mockDataPool.users.get(0);
        user_FLAT.units = null;
        ResultActions result = mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(user_FLAT)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNull(result.andReturn().getResolvedException());
        User userInDB = userRepository.findById(user_FLAT.id);
        Assert.assertNotNull(userInDB);
        Assert.assertEquals(user_FLAT.id, userInDB.id);

        // 2: User exsists in DB
        Boolean conflictExceptionThrown = false;
        result = mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(user_FLAT)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(ConflictException.class, result.andReturn().getResolvedException().getClass());

        // -----------------> getUser
        // 1: User does not exist in DB
        result = mvc.perform(get("/Storage/UserService/getUser").param("userId",UUID.randomUUID().toString()).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        String resultStr = result.andReturn().getResponse().getContentAsString();
        Assert.assertEquals("", resultStr);

        // 2: User exists in DB
        result = mvc.perform(get("/Storage/UserService/getUser").param("userId", user_FLAT.id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        userInDB = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), User.class);
        Assert.assertEquals(user_FLAT.id, userInDB.id);

        // -----------------> deleteUser
        // 1: User does not exist in DB
        result = mvc.perform(delete("/Storage/UserService/deleteUser").param("userId",UUID.randomUUID().toString()).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(NotFoundException.class, result.andReturn().getResolvedException().getClass());

        // 2: User exists in DB
        result = mvc.perform(delete("/Storage/UserService/deleteUser").param("userId", user_FLAT.id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        userInDB = userRepository.findById(user_FLAT.id);
        Assert.assertNull(userInDB);
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
