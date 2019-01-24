package optiheat.storage.integrationtests;


import com.fasterxml.jackson.databind.ObjectMapper;
import optiheat.storage.MockData;
import optiheat.storage.controller.exception.ConflictException;
import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.Room;
import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.repository.UnitRepository;
import optiheat.storage.repository.UserRepository;
import optiheat.storage.service.GenericService;
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
import javax.ws.rs.core.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SpecificationServiceControllerTests
{
    Logger logger = LoggerFactory.getLogger(UserServiceControllerTests.class);
    MockData mockDataPool;

    @Before
    public void setEnvironment()
    {
        try
        {
            genericService.deleteEntireDatabase();
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
    UnitRepository unitRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    GenericService genericService;

    private MockMvc mvc;

    @Test
    public void createUnitTest() throws Exception
    {
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        // 1: not found - user does not exist
        ResultActions result = mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(NotFoundException.class, result.andReturn().getResolvedException().getClass());

        // 3: request ok - unit is entered into db
        result = mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNull(result.andReturn().getResolvedException());
        Unit unitInDB = unitRepository.findById(mockUnit.id);
        Assert.assertEquals(mockUnit.id, unitInDB.id);

        // 3: conflict - unit already exists
        result = mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(ConflictException.class, result.andReturn().getResolvedException().getClass());
    }

    @Test
    public void createRoomTest() throws Exception
    {
        Room mockRoom = mockDataPool.copyRoomDirected(mockDataPool.users.get(0).units.get(0).rooms.get(0));
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

