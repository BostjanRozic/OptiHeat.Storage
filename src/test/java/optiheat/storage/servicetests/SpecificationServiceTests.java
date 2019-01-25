package optiheat.storage.servicetests;

import optiheat.storage.MockData;
import optiheat.storage.controller.exception.BadRequestException;
import optiheat.storage.controller.exception.ConflictException;
import optiheat.storage.controller.exception.InternalServerErrorException;
import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.Room;
import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.repository.RoomRepository;
import optiheat.storage.repository.UnitRepository;
import optiheat.storage.repository.UserRepository;
import optiheat.storage.service.SpecificationService;
import optiheat.storage.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SpecificationServiceTests
{
    Logger logger = LoggerFactory.getLogger(UserService.class);
    MockData mockDataPool;


    @Before
    public void setData()
    {
        try
        {
            if (mockDataPool.users == null || mockDataPool.users.isEmpty())
            {
                mockDataPool = new MockData();
                mockDataPool.populateMockDataFromFiles(UUID.randomUUID().toString());
            }
            //initMocks(this);
        }
        catch (Exception ex)
        {
            logger.error("Could not read mock data from files!", ex);
        }
    }

    @Mock
    UserRepository userRepositoryMock;

    @Mock
    UnitRepository unitRepositoryMock;

    @Mock
    RoomRepository roomRepositoryMock;

    @Autowired
    @InjectMocks
    SpecificationService specificationServiceMock;

    @Test
    public void createUnitTest()
    {
        // 1: bad request - empty userID
        try
        {
            specificationServiceMock.createUnit(null, mockDataPool.users.get(0).units.get(0));
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: bad request - empty unit
        try
        {
            specificationServiceMock.createUnit(mockDataPool.users.get(0).id, null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 3: bad request - unit without ID
        try
        {
            specificationServiceMock.createUnit(mockDataPool.users.get(0).id, new Unit());
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 4: conflict - Unit already exists
        when(unitRepositoryMock.findById(mockDataPool.users.get(0).units.get(0).id)).thenReturn(mockDataPool.users.get(0).units.get(0));
        try
        {
            specificationServiceMock.createUnit(mockDataPool.users.get(0).id, mockDataPool.users.get(0).units.get(0));
            Assert.fail("Excpected ConflictException");
        }
        catch (ConflictException ex){}

        // 5: user not found
        when(unitRepositoryMock.findById(mockDataPool.users.get(0).units.get(0).id)).thenReturn(null);
        when(userRepositoryMock.findById(mockDataPool.users.get(0).id)).thenReturn(null);
        try
        {
            specificationServiceMock.createUnit(mockDataPool.users.get(0).id, mockDataPool.users.get(0).units.get(0));
            Assert.fail("Excpected ConflictException");
        }
        catch (NotFoundException ex){}

        // 6: request ok
        when(userRepositoryMock.findById(mockDataPool.users.get(0).id)).thenReturn(mockDataPool.users.get(0));
        when(unitRepositoryMock.save(mockDataPool.users.get(0).units.get(0))).thenReturn(mockDataPool.users.get(0).units.get(0));
        specificationServiceMock.createUnit(mockDataPool.users.get(0).id, mockDataPool.users.get(0).units.get(0));
    }

    @Test
    public void createRoomTest()
    {
        // 1: bad request - room = null
        try
        {
            specificationServiceMock.createRoom(null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: bad request - room without id
        Room room = new Room();
        try
        {
            specificationServiceMock.createRoom(room);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 3: bad request - room without unit
        room.id = UUID.randomUUID().toString();
        try
        {
            specificationServiceMock.createRoom(room);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 4: bad request - room with unit.id = null
        room.unit = new Unit();
        try
        {
            specificationServiceMock.createRoom(room);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 5: unit does not exist
        room = mockDataPool.users.get(0).units.get(0).rooms.get(0);
        when(unitRepositoryMock.findById(room.unit.id)).thenReturn(null);
        try
        {
            specificationServiceMock.createRoom(room);
            Assert.fail("Excpected NotFoundExpcetion");
        }
        catch (NotFoundException ex){}

        // 6: conflict - room with same ID already exists
        when(unitRepositoryMock.findById(room.unit.id)).thenReturn(room.unit);
        when(roomRepositoryMock.findById(room.id)).thenReturn(room);
        try
        {
            specificationServiceMock.createRoom(room);
            Assert.fail("Excpected ConflictExpcetion");
        }
        catch (ConflictException ex){}

        // 6: request ok
        when(roomRepositoryMock.findById(room.id)).thenReturn(null);
        when(roomRepositoryMock.save(room)).thenReturn(room);
        specificationServiceMock.createRoom(room);
    }

    @Test
    public void updateUnitTest()
    {
        Unit mockUnit = mockDataPool.users.get(0).units.get(0);
        // 1: bad request - empty unit
        try
        {
            specificationServiceMock.updateUnit(null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: bad request - unit without ID
        try
        {
            specificationServiceMock.updateUnit(new Unit());
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 3: not found - unit does not exist
        when(unitRepositoryMock.findById(mockUnit.id)).thenReturn(null);
        try
        {
            specificationServiceMock.updateUnit(mockUnit);
            Assert.fail("Excpected NotFoundException");
        }
        catch (NotFoundException ex){}

        // 4: request ok
        when(unitRepositoryMock.findById(mockUnit.id)).thenReturn(mockUnit);
        when(unitRepositoryMock.save(mockUnit)).thenReturn(mockUnit);
        specificationServiceMock.updateUnit(mockUnit);
    }

    @Test
    public void updateRoomTest()
    {
        Room mockRoom = mockDataPool.users.get(0).units.get(0).rooms.get(0);
        // 1: bad request - room = null
        try
        {
            specificationServiceMock.updateRoom(null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: bad request - room without id
        try
        {
            specificationServiceMock.updateRoom(new Room());
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 3: not found - room does not exist
        when(roomRepositoryMock.findById(mockRoom.id)).thenReturn(null);
        try
        {
            specificationServiceMock.updateRoom(mockRoom);
            Assert.fail("Excpected NotFoundExceptionException");
        }
        catch (NotFoundException ex){}

        // 4: request ok
        when(roomRepositoryMock.findById(mockRoom.id)).thenReturn(mockRoom);
        when(roomRepositoryMock.save(mockRoom)).thenReturn(mockRoom);
        specificationServiceMock.updateRoom(mockRoom);
    }

    @Test
    public void deleteUnitTest()
    {
        Unit mockUnit = mockDataPool.users.get(0).units.get(0);
        // 1: bad request - unitId = null
        try
        {
            specificationServiceMock.deleteUnit(null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: not found - unit with the specified id does not exist
        when(unitRepositoryMock.findById(mockUnit.id)).thenReturn(null);
        try
        {
            specificationServiceMock.deleteUnit(mockUnit.id);
            Assert.fail("Excpected NotFoundException");
        }
        catch (NotFoundException ex){}

        // 3: request ok
        when(unitRepositoryMock.findById(mockUnit.id)).thenReturn(mockUnit);
        for (Room room : mockUnit.rooms)
        {
            when(roomRepositoryMock.findById(room.id)).thenReturn(room);
        }
        specificationServiceMock.deleteUnit(mockUnit.id);
    }

    @Test
    public void deleteRoomTest()
    {
        Room mockRoom = mockDataPool.users.get(0).units.get(0).rooms.get(0);
        // 1: bad request - roomId = null
        try
        {
            specificationServiceMock.deleteRoom(null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: not found - unit with the specified id does not exist
        when(unitRepositoryMock.findById(mockRoom.id)).thenReturn(null);
        try
        {
            specificationServiceMock.deleteUnit(mockRoom.id);
            Assert.fail("Excpected NotFoundException");
        }
        catch (NotFoundException ex){}

        // 3: request ok
        when(roomRepositoryMock.findById(mockRoom.id)).thenReturn(mockRoom);
        specificationServiceMock.deleteRoom(mockRoom.id);
    }

    @Test
    public void getUnit()
    {
        Unit mockUnit = mockDataPool.users.get(0).units.get(0);
        // 1: bad request - unitId = null
        try
        {
            specificationServiceMock.deleteUnit(null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: request ok - no test because it doesnt make sense
    }

    @Test
    public void getRoomTest()
    {
        Room mockRoom = mockDataPool.users.get(0).units.get(0).rooms.get(0);
        // 1: bad request - userId = null
        try
        {
            specificationServiceMock.deleteRoom(null);
            Assert.fail("Excpected BadRequestException");
        }
        catch (BadRequestException ex){}

        // 2: request ok - no test becuase it doesnt make sense
    }
}

