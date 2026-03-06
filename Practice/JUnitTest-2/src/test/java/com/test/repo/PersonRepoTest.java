package com.test.repo;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.test.entity.Person;
@SpringBootTest
@Transactional
public class PersonRepoTest {
	@Autowired
	private  PersonRepo repo;
	@Test
	public void TestExistById() {
		Person person = new Person();
		person.setPersonCity("Indore");
		person.setPersonName("Cheeku");
        person = repo.save(person);
        boolean exist = repo.existsById(person.getPersonId());
        
        assertThat(exist).isTrue();

	}

}
