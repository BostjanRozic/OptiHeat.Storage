package optiheat.storage;

import optiheat.storage.model.Room;
import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.service.GenericService;
import optiheat.storage.service.SpecificationService;
import optiheat.storage.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	SpecificationService specificationService;

	@Autowired
	GenericService genericService;

	@Test
	public void contextLoads()
	{
		genericService.deleteEntireDatabase();

		String id1 = UUID.randomUUID().toString();
		String id2 = UUID.randomUUID().toString();
		String id3 = UUID.randomUUID().toString();

		User user1 = new User();
		user1.id = id1;

		Unit unit1 = new Unit();
		unit1.id  = id2;
		unit1.name = "unit 1";

		Room room1 = new Room();
		room1.id = id3;
		room1.name = "room 1";

		unit1.rooms = new HashSet<>();
		unit1.rooms.add(room1);
		user1.units = new HashSet<>();
		user1.units.add(unit1);
		userService.createUser(user1);
		User user = userService.getUser(id1);
		Unit unit = specificationService.getUnit(id2);
		//userService.deleteAll();
		userService.deleteUser(id1);
		user = userService.getUser(id1);
		unit = specificationService.getUnit(id2);
		int a = 1;
	}

}

