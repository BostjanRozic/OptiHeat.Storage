package optiheat.storage;

import optiheat.storage.model.Unit;
import optiheat.storage.model.User;
import optiheat.storage.repository.UnitRepository;
import optiheat.storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.util.UUID;

@SpringBootApplication
@EnableNeo4jRepositories
public class StorageApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(StorageApplication.class, args);
	}

	//@Bean
	CommandLineRunner demo(UserRepository userRepository)
	{
		return args ->
		{
			String id1 = UUID.randomUUID().toString();
			String id2 = UUID.randomUUID().toString();
			userRepository.deleteAll();

			User user1 = new User();
			user1.id = id1;
			Unit unit1 = new Unit();
			unit1.id  = id2;
			unit1.name = "unit 1";
			user1.units.add(unit1);
			userRepository.save(user1);

			User user = userRepository.findById(id1);
			int a =1;
		};
	}
}

