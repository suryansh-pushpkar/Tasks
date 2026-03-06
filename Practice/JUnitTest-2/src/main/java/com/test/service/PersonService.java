package com.test.service;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.test.entity.Person;
import com.test.repo.PersonRepo;
@Service
public class PersonService {

    private final PersonRepo repo;
    public PersonService(PersonRepo repo)
    {
        this.repo = repo;
    }

    public ResponseEntity<?> getAllPerson()
    {
        return ResponseEntity.ok(repo.findAll());
    }
    
    public ResponseEntity<?>savePerson(Person person){
    	return ResponseEntity.ok(repo.save(person));
    }
}