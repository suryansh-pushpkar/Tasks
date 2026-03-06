package com.test.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.entity.Person;
@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
	boolean existsById(Integer id);
}