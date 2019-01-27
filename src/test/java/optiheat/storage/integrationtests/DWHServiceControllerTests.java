package optiheat.storage.integrationtests;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import optiheat.storage.MockData;
import optiheat.storage.controller.exception.ConflictException;
import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.*;
import optiheat.storage.repository.IterationRepository;
import optiheat.storage.repository.RoomRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DWHServiceControllerTests
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
    RoomRepository roomRepository;

    @Autowired
    IterationRepository iterationRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    GenericService genericService;

    private MockMvc mvc;

    @Test
    public void createIterationTest() throws Exception
    {
        // data prepatartion
        ResultActions result;
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        List<Iteration> mockIterations = mockDataPool.users.get(0).units.get(0).iterations;
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));

        // testing iteration insertion
        int i = 1;  // starting with 1 since one iteration is already in DB
        for (Iteration mockIteration : mockIterations.stream().collect(Collectors.toList()))
        {
            Iteration mockIterationDirected = mockDataPool.copyIterationDirected(mockIteration);
            result = mvc.perform(post("/Storage/DWHService/createIteration").content(asJsonString(mockIterationDirected)).contentType(MediaType.APPLICATION_JSON));
            assertNull(result.andReturn().getResolvedException());
            Iteration iterationInDB = iterationRepository.findById(mockIteration.id);
            assertNotNull(iterationInDB);
            // check that sequence is incremented and that this increment was returned as a response to operation call
            assertEquals(i, iterationInDB.sequence);
            assertEquals(i, Integer.parseInt(result.andReturn().getResponse().getContentAsString()));
            i++;
        }
    }

    @Test
    public void getIterationsTest() throws Exception
    {
        // data preparation
        ResultActions result;
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        List<Iteration> mockIterations = mockDataPool.users.get(0).units.get(0).iterations;
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        for (Iteration mockIteration : mockIterations.stream().collect(Collectors.toList()))
        {
            Iteration mockIterationDirected = mockDataPool.copyIterationDirected(mockIteration);
            mvc.perform(post("/Storage/DWHService/createIteration").content(asJsonString(mockIterationDirected)).contentType(MediaType.APPLICATION_JSON));
        }

        // testing iterations query
        result = mvc.perform(get("/Storage/DWHService/getIterations").param("unitId", mockUnit.id).contentType(MediaType.TEXT_PLAIN));
        assertNull(result.andReturn().getResolvedException());
        List<Iteration> iterationsInDB = new ArrayList<>();
        iterationsInDB = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<List<Iteration>>(){});
        assertNotNull(iterationsInDB);
        assertTrue(!iterationsInDB.isEmpty());
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


