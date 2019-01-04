package optiheat.storage;

import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.service.UnitService;
import optiheat.storage.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
public class StorageApplicationTests
{
	@Autowired
	UserService userService;

	@Autowired
	UnitService unitService;

	@Test
	public void contextLoads()
	{
		userService.deleteAll();
		unitService.deleteAll();
		String id1 = UUID.randomUUID().toString();
		String id2 = UUID.randomUUID().toString();
		User user1 = new User();
		user1.id = id1;
		Unit unit1 = new Unit();
		unit1.id  = id2;
		unit1.name = "unit 1";
		user1.units = new HashSet<>();
		user1.units.add(unit1);
		userService.createUser(user1);
		User user = userService.getUser(id1);
		Unit unit = unitService.getUnit(id2);
		userService.deleteAll();
		user = userService.getUser(id1);
		unit = unitService.getUnit(id2);
		int a = 1;
	}

}

