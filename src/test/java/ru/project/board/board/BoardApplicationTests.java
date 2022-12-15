package ru.project.board.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.project.board.board.entity.Advertisement;
import ru.project.board.board.entity.User;
import ru.project.board.board.repository.UserRepo;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardApplicationTests {
	@Autowired
 private UserRepo userRepo;

	@Test
	@Transactional
	void AddNewUserTest() {
		User user = new User();
		User user1;

		user.setName("Иван");
		user.setSurname("Фукин");
		user.setPassword("1234");
		user.setPhoneNumber("+79622114167");
		userRepo.save(user);
		user1 = userRepo.findByPhoneNumber("+79622114167");
		assertThat(user1.getName()).isEqualTo("Иван");
		assertThat(user1.getSurname()).isEqualTo("Фукин");
	}

	@Test
	@Transactional
	void deleteUser() {
		User user = new User();

		user.setName("Иван");
		user.setSurname("Фукин");
		user.setPassword("1234");
		user.setPhoneNumber("+79622114167");
		user = userRepo.save(user);
		userRepo.delete(user);
		assertFalse(userRepo.findById(user.getId()).isPresent());
	}

	@Test
	@Transactional
	void auf() {
		User user = new User();
		Advertisement advertisement = new Advertisement();
		user.setName("Иван");
		user.setSurname("Фукин");
		user.setPassword("1234");
		user.setPhoneNumber("+79622114167");
		user = userRepo.save(user);

		userRepo.delete(user);

		assertFalse(userRepo.findById(user.getId()).isPresent());
	}

}
