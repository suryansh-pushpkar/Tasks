package com.lib.dao;

import com.lib.util.JPAUtil;

import jakarta.persistence.EntityManagerFactory;

public class UserDao {
	private final EntityManagerFactory factory = JPAUtil.getFactory();
	
	

}
