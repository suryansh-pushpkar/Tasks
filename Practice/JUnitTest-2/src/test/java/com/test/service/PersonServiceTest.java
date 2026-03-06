package com.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.test.entity.Person;

@SpringBootTest
@Transactional
public class PersonServiceTest {
	
	@Autowired
	private PersonService service;
	@Test
	
	public void testSavePerson() {
		Person person = new Person();
		person.setPersonCity("Prithvipur");
		person.setPersonName("Anmol");
		
	ResponseEntity<Person> p1=	(ResponseEntity<Person>) service.savePerson(person);
	assertThat(p1).isNotNull();
	}
	
	@Test
	public void testGetAllPerson() {
		ResponseEntity<List<Person>> personList=(ResponseEntity<List<Person>>) service.getAllPerson();
		
		assertThat(personList).isNotNull();
	}

}
