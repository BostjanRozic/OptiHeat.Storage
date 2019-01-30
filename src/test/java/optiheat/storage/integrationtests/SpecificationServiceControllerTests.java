package optiheat.storage.integrationtests;


import com.fasterxml.jackson.databind.ObjectMapper;
import optiheat.storage.MockData;
import optiheat.storage.controller.exception.ConflictException;
import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.*;
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
    RoomRepository roomRepository;

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
        List<UnitSetting> allUnitSettings = mockUnit.unitSettings;
        mockUnit.unitSettings = new ArrayList<>();
        mockUnit.unitSettings.add(allUnitSettings.get(0));
        for (Room room : mockUnit.rooms)
        {
            List<RoomSetting> allRoomSettings = room.roomSettings;
            room.roomSettings = new ArrayList<>();
            room.roomSettings.add(allRoomSettings.get(0));
        }

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
        Room mockRoom = mockDataPool.copyRoomDirected(mockDataPool.users.get(0).units.get(0).rooms.get(0), null);
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        ResultActions result;
        // Bad requests are not tested - Service tests are for that...

        // 1: notfound - unit does not exist
        genericService.deleteEntireDatabase();
        result = mvc.perform(post("/Storage/SpecificationService/createRoom").content(asJsonString(mockRoom)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(NotFoundException.class, result.andReturn().getResolvedException().getClass());

        // 2: conflict - room already exists
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(post("/Storage/SpecificationService/createRoom").content(asJsonString(mockRoom)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(ConflictException.class, result.andReturn().getResolvedException().getClass());

        // 3: room doesn't exist it is sucessfully entered
        genericService.deleteEntireDatabase();
        mockUnit.rooms = null;
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(post("/Storage/SpecificationService/createRoom").content(asJsonString(mockRoom)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNull(result.andReturn().getResolvedException());
        Room roomInDB = roomRepository.findById(mockRoom.id);
        Assert.assertNotNull(roomInDB.unit);
        Assert.assertNotNull(roomInDB.roomSettings);
    }

    @Test
    public void updateUnitTest() throws Exception
    {
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        Unit updatedUnit = new Unit();
        updatedUnit.id = mockUnit.id;
        updatedUnit.name = "nov naziv";
        ResultActions result;

        // 1: not found - unit does not exist
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(put("/Storage/SpecificationService/updateUnit").content(asJsonString(updatedUnit)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(NotFoundException.class, result.andReturn().getResolvedException().getClass());

        // 2: request ok
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(put("/Storage/SpecificationService/updateUnit").content(asJsonString(updatedUnit)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNull(result.andReturn().getResolvedException());
        Unit unitInDB = unitRepository.findById(mockUnit.id);
        Assert.assertEquals("nov naziv", unitInDB.name);
    }

    @Test
    public void updateRoomTest() throws Exception
    {
        Room mockRoom = mockDataPool.copyRoomDirected(mockDataPool.users.get(0).units.get(0).rooms.get(0), null);
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        Room updatedRoom = new Room();
        updatedRoom.id = mockRoom.id;
        updatedRoom.name = "nov naziv";
        ResultActions result;

        // 1: not found - room does not exist
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(put("/Storage/SpecificationService/updateRoom").content(asJsonString(updatedRoom)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(NotFoundException.class, result.andReturn().getResolvedException().getClass());

        // 2: request ok
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(put("/Storage/SpecificationService/updateRoom").content(asJsonString(updatedRoom)).contentType(MediaType.APPLICATION_JSON));
        Assert.assertNull(result.andReturn().getResolvedException());
        Room roomInDB = roomRepository.findById(mockRoom.id);
        Assert.assertEquals("nov naziv", roomInDB.name);
    }

    @Test
    public void deleteUnitTest() throws Exception
    {
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        ResultActions result;

        // 1: not found - unit does not exist
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(delete("/Storage/SpecificationService/deleteUnit").param("unitId", mockUnit.id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(NotFoundException.class, result.andReturn().getResolvedException().getClass());

        // 2: unit exists, request ok
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        Unit unitInDB = unitRepository.findById(mockUnit.id);
        Assert.assertNotNull(unitInDB);
        result = mvc.perform(delete("/Storage/SpecificationService/deleteUnit").param("unitId", mockUnit.id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        unitInDB = unitRepository.findById(mockUnit.id);
        Assert.assertNull(unitInDB);
    }

    @Test
    public void deleteRoomTest() throws Exception
    {
        Room mockRoom = mockDataPool.copyRoomDirected(mockDataPool.users.get(0).units.get(0).rooms.get(0), null);
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        ResultActions result;

        // 1: not found - room does not exist
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(delete("/Storage/SpecificationService/deleteRoom").param("roomId", mockRoom.id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNotNull(result.andReturn().getResolvedException());
        Assert.assertEquals(NotFoundException.class, result.andReturn().getResolvedException().getClass());

        // 2: request ok
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(delete("/Storage/SpecificationService/deleteRoom").param("roomId", mockRoom.id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        Room roomInDB = roomRepository.findById(mockRoom.id);
        Assert.assertNull(roomInDB);
    }

    @Test
    public void getUnitTest() throws Exception
    {
        Unit mockUnit;
        ResultActions result;
        Unit unitInDB;

        // Data preparation
        genericService.deleteEntireDatabase();

        // 1: Unit does not exist - reply ok, empty payload
        result = mvc.perform(get("/Storage/SpecificationService/MATCHgetUnit").param("unitId", UUID.randomUUID().toString()).contentType(MediaType.TEXT_PLAIN));
        Assert.assertEquals("",result.andReturn().getResponse().getContentAsString());

        // 2: Unit exists but has no Rooms and no UnitSettings
        // data preparation
        genericService.deleteEntireDatabase();
        mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        mockUnit.rooms = null;
        mockUnit.unitSettings = null;
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        // test execution and result assertion
        result = mvc.perform(get("/Storage/SpecificationService/getUnit").param("unitId", mockDataPool.users.get(0).units.get(0).id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        unitInDB = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), Unit.class);
        Assert.assertNotNull(unitInDB);
        Assert.assertNull(unitInDB.rooms);
        Assert.assertNull(unitInDB.unitSettings);
        Assert.assertNull(unitInDB.unitMeasurements);
        Assert.assertNull(unitInDB.iterations);


        // 3: Unit exists and has several UnitSettings (only the last should be returned) but no Rooms
        // data preparation
        genericService.deleteEntireDatabase();
        mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        mockUnit.rooms = null;
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        // test execution and result assertion
        result = mvc.perform(get("/Storage/SpecificationService/getUnit").param("unitId", mockDataPool.users.get(0).units.get(0).id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        unitInDB = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), Unit.class);
        Assert.assertNotNull(unitInDB);
        Assert.assertNotNull(unitInDB.unitSettings);
        Assert.assertEquals(1, unitInDB.unitSettings.size());
        Assert.assertNull(unitInDB.rooms);
        Assert.assertNull(unitInDB.unitMeasurements);
        Assert.assertNull(unitInDB.iterations);

        // 4: Unit exists and has several UnitSettings (only the last should be returned) and several Rooms (none of them have any RoomSettings)
        genericService.deleteEntireDatabase();
        mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        for (Room room : mockUnit.rooms)
        {
            room.roomSettings = null;
        }
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        // test execution and result assertion
        result = mvc.perform(get("/Storage/SpecificationService/getUnit").param("unitId", mockDataPool.users.get(0).units.get(0).id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        unitInDB = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), Unit.class);
        Assert.assertNotNull(unitInDB);
        Assert.assertNotNull(unitInDB.unitSettings);
        Assert.assertEquals(1, unitInDB.unitSettings.size());
        Assert.assertNotNull(unitInDB.rooms);
        Assert.assertEquals(4, unitInDB.rooms.size());
        for (Room room : unitInDB.rooms)
        {
            Assert.assertNull(room.roomSettings);
            Assert.assertNull(room.roomMeasurements);
            Assert.assertNull(room.unit);
        }
        Assert.assertNull(unitInDB.unitMeasurements);
        Assert.assertNull(unitInDB.iterations);

        // 5: Unit exists and has several UnitSettings (only the last should be returned) and several Rooms (each of them has several RoomSettings)
        genericService.deleteEntireDatabase();
        mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockUnit)).contentType(MediaType.APPLICATION_JSON));
        // test execution and result assertion
        result = mvc.perform(get("/Storage/SpecificationService/getUnit").param("unitId", mockDataPool.users.get(0).units.get(0).id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        unitInDB = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), Unit.class);
        Assert.assertNotNull(unitInDB);
        Assert.assertNotNull(unitInDB.unitSettings);
        Assert.assertEquals(1, unitInDB.unitSettings.size());
        Assert.assertNotNull(unitInDB.rooms);
        Assert.assertEquals(4, unitInDB.rooms.size());
        for (Room room : unitInDB.rooms)
        {
            Assert.assertNotNull(room.roomSettings);
            Assert.assertEquals(1, room.roomSettings.size());
            Assert.assertNull(room.roomMeasurements);
            Assert.assertNull(room.unit);
        }
        Assert.assertNull(unitInDB.unitMeasurements);
        Assert.assertNull(unitInDB.iterations);
    }

    @Test
    public void getUnitsTest() throws Exception
    {
        Unit mockUnit = mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0));
        ResultActions result;
        Unit unitInDB;

        // 1: No user exists in DB
        genericService.deleteEntireDatabase();
        result = mvc.perform(get("/Storage/SpecificationService/getUnits").param("userId", mockDataPool.users.get(0).id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        Assert.assertEquals("[]",result.andReturn().getResponse().getContentAsString());

        // 2: User Exist but has no units
        genericService.deleteEntireDatabase();
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(get("/Storage/SpecificationService/getUnits").param("userId", mockDataPool.users.get(0).id).contentType(MediaType.TEXT_PLAIN));
        Assert.assertNull(result.andReturn().getResolvedException());
        Assert.assertEquals("[]",result.andReturn().getResponse().getContentAsString());

        // 3: User exists with several units
        genericService.deleteEntireDatabase();
        Unit newUnit = new Unit();
        newUnit.id = UUID.randomUUID().toString();
        newUnit.user = mockDataPool.users.get(0);
        mockDataPool.users.get(0).units.add(newUnit);
        mvc.perform(post("/Storage/UserService/createUser").content(asJsonString(mockUnit.user)).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(0)))).contentType(MediaType.APPLICATION_JSON));
        mvc.perform(post("/Storage/SpecificationService/createUnit").param("userId", mockUnit.user.id).content(asJsonString(mockDataPool.copyUnitDirected(mockDataPool.users.get(0).units.get(1)))).contentType(MediaType.APPLICATION_JSON));
        result = mvc.perform(get("/Storage/SpecificationService/getUnits").param("userId", mockDataPool.users.get(0).id).contentType(MediaType.TEXT_PLAIN));
        List<Unit> units = new ArrayList<>();
        units = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsString(), units.getClass());
        Assert.assertNotNull(units);
        Assert.assertEquals(2, units.size());
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

