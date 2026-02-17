package com.lib.dao;

import com.lib.entity.User;
import com.lib.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class UserDao {
	private final EntityManagerFactory factory = JPAUtil.getFactory();

	public User saveUser(User user) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(user);
			tx.commit();
			return user;

		} catch (Exception e) {
			if (tx.isActive() && (tx != null))
				tx.rollback();
			throw new RuntimeException(e.getMessage());
		}
	}

	public User userLogin(String membershipNo, String password) {
		EntityManagerFactory factory = JPAUtil.getFactory();
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.membershipNo = :mNo AND u.password = :pwd";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("mNo", membershipNo);
			query.setParameter("pwd", password);

			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public User getUserByMnoAndEmail(String membershipNo, String mail) {
		EntityManagerFactory factory = JPAUtil.getFactory();
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT u FROM User u WHERE u.membershipNo = :mNo AND u.mail = :mail";
			TypedQuery<User> query = em.createQuery(jpql, User.class);
			query.setParameter("mNo", membershipNo);
			query.setParameter("mail", mail);

			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}
}
