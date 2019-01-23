package optiheat.storage;

import optiheat.storage.controller.exception.BadRequestException;
import optiheat.storage.controller.exception.ConflictException;
import optiheat.storage.controller.exception.InternalServerErrorException;
import optiheat.storage.controller.exception.NotFoundException;
import optiheat.storage.model.User;
import optiheat.storage.repository.UserRepository;
import optiheat.storage.service.UserService;
import org.junit.Before;
import org.junit.Test;
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
public class ServiceTests
{
    Logger logger = LoggerFactory.getLogger(UserService.class);
    MockData mockDataPool;


    @Before
    public void setData()
    {
        try
        {
            mockDataPool = new MockData();
            mockDataPool.populateMockDataFromFiles(UUID.randomUUID().toString());
            //initMocks(this);
        }
        catch (Exception ex)
        {
            logger.error("Could not read mock data from files!", ex);
        }
    }

    @Mock
    UserRepository userRepositoryMock;

    @Autowired
    @InjectMocks
    UserService userServiceMock;

    @Test
    public void testUserService()
    {
        //--------> 1. createUser
        // 1: User does not exist in DB. Id empty. Expecting status 400 (Bad Request)
        Boolean badRequestExceptionThrown = false;
        try
        {
            userServiceMock.createUser(new User());
        }
        catch (BadRequestException ex)
        {
            badRequestExceptionThrown = true;
        }
        assertTrue(badRequestExceptionThrown);

        // 2: User does not exist in DB. GRAPH payload. Expecting status 400 (Bad Request)
        User user = mockDataPool.users.get(0);
        badRequestExceptionThrown = false;
        try
        {
            userServiceMock.createUser(user);
        }
        catch (BadRequestException ex)
        {
            badRequestExceptionThrown = true;
        }
        assertTrue(badRequestExceptionThrown);

        // 3: User does not exist in DB. FLAT payload. Expecting user To be created in DB
        user.units = null;
        when(userRepositoryMock.save(user)).thenReturn(user);
        when(userRepositoryMock.findById(user.id)).thenReturn(null);
        userServiceMock.createUser(user);

        // 4: User Exists in DB. Expecting status 400 (Bad Request)
        Boolean conflictExceptionThrown = false;
        when(userRepositoryMock.findById(user.id)).thenReturn(user);
        try
        {
            userServiceMock.createUser(user);
        }
        catch (ConflictException ex)
        {
            conflictExceptionThrown = true;
        }
        assertTrue(conflictExceptionThrown);

        //----------> 2. getUser
        // 1: Request with empty argument. Expecting status 400 (Bad Request)
        badRequestExceptionThrown = false;
        try
        {
            userServiceMock.getUser(null);
        }
        catch (BadRequestException ex)
        {
            badRequestExceptionThrown = true;
        }
        assertTrue(badRequestExceptionThrown);

        // 2: User does not exist in DB. Expecting null.
        String id = UUID.randomUUID().toString();
        when(userRepositoryMock.findById(id)).thenReturn(null);
        assertEquals(null, userServiceMock.getUser(id));

        // 3: User exists in DB. Expecting FLAT payload as a result
        User user_FLAT = mockDataPool.users.get(0);
        user_FLAT.units = null;
        when(userRepositoryMock.findById(user_FLAT.id)).thenReturn(user_FLAT);
        assertEquals(user_FLAT, userServiceMock.getUser(user_FLAT.id));

        //----------> 3. deleteUser
        // 1: Request with empty argument. Expecting status 400 (Bad Request)
        badRequestExceptionThrown = false;
        try
        {
            userServiceMock.deleteUser(null);
        }
        catch (BadRequestException ex)
        {
            badRequestExceptionThrown = true;
        }
        assertTrue(badRequestExceptionThrown);

        // 2: User does not exist in DB. Expecting status 500 (Internal Server Error)
        String randomId = UUID.randomUUID().toString();
        //when(userRepositoryMock.findById(randomId)).thenReturn(null);
        Boolean notFoundExceptionThrown = false;
        try
        {
            userServiceMock.deleteUser(UUID.randomUUID().toString());
        }
        catch (NotFoundException ex)
        {
            notFoundExceptionThrown = true;
        }
        assertTrue(notFoundExceptionThrown);

        // 3: User that exists in DB. Expecting no meaningful response
        when(userRepositoryMock.findById(mockDataPool.users.get(0).id)).thenReturn(mockDataPool.users.get(0));
        Mockito.doNothing().when(userRepositoryMock).deleteUser(mockDataPool.users.get(0).id);
        Mockito.doNothing().when(userRepositoryMock).deleteAllUnitsForUser(mockDataPool.users.get(0).id);
        Mockito.doNothing().when(userRepositoryMock).deleteAllRoomsForUser(mockDataPool.users.get(0).id);
        userServiceMock.deleteUser(mockDataPool.users.get(0).id);

        //---------> 4. delete all users. Expect no meaningfull response
        Mockito.doNothing().when(userRepositoryMock).deleteAll();
        userServiceMock.deleteAll();
    }
}
