package com.lib.dao;

import com.lib.entity.Library;
import com.lib.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class LibraryDao {
	private EntityManagerFactory factory = JPAUtil.getFactory();

	public Library createLib(Library library) {
		EntityManager em = factory.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		try {

			tx.begin();

			em.persist(library);

			tx.commit();
			return library;

		} catch (Exception e) {
			if (tx.isActive() && tx != null)
				tx.rollback();
			e.printStackTrace();
			throw new RuntimeException("Something Went Wrong");

		} finally {
			em.close();
		}
	}
}
