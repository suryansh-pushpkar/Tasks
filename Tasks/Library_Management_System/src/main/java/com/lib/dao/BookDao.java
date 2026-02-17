package com.lib.dao;


import com.lib.entity.Book;
import com.lib.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class BookDao {

	private EntityManagerFactory factory = JPAUtil.getFactory();

	public Book saveBook(Book book) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(book);
			tx.commit();
			return book;
		} catch (Exception e) {
			if (tx.isActive() && (tx != null))
				tx.rollback();
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
