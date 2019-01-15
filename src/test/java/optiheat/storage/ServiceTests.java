package optiheat.storage;

import optiheat.storage.repository.UserRepository;
import optiheat.storage.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ServiceTests
{
    Logger logger = LoggerFactory.getLogger(UserService.class);
    MockData mockDataPool;

    @Mock
    UserRepository userRepositoryMock;

    @Autowired
    @InjectMocks
    UserService userServiceMock;

    @Before
    public void setData()
    {
        try
        {
            mockDataPool = new MockData();
            mockDataPool.populateMockDataFromFiles(UUID.randomUUID().toString());
        }
        catch (Exception ex)
        {
            logger.error("Could not read mock data from files!", ex);
        }
    }

    @Test
    public void testUserService()
    {
        //--------> 1. createUser

    }
}
