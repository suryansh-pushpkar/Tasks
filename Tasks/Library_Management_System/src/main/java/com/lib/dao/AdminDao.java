package com.lib.dao;

import com.lib.entity.Admin;
import com.lib.entity.Library;
import com.lib.util.AdminIdAssigner;
import com.lib.util.EmailUtil;
import com.lib.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transaction;

public class AdminDao {

	public static boolean existsByMembershipNo(String membershipNo) {
		EntityManagerFactory factory = JPAUtil.getFactory();
		EntityManager em = factory.createEntityManager();

		try {
			String jpql = "SELECT COUNT(a) FROM Admin a WHERE a.membershipNo = :mNo";
			Long count = em.createQuery(jpql, Long.class).setParameter("mNo", membershipNo).getSingleResult();
			return count > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean register(Admin admin) {
		EntityManagerFactory factory = JPAUtil.getFactory();
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			AdminDao dao = new AdminDao();
			tx.begin();
			admin.setMembershipNo(AdminIdAssigner.assignUniqueId(admin));
			em.persist(admin);
			tx.commit();
			// EmailUtil.sendWelcomeEmail(admin.getMail(), admin.getName(),
			// admin.getMembershipNo(), admin.getPassword());
			return true;
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
			return false;
		}
	}

	public Admin login(String mNo, String password) {
		EntityManagerFactory factory = JPAUtil.getFactory();
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT a FROM Admin a WHERE a.membershipNo = :mNo AND a.password = :pwd";
			TypedQuery<Admin> query = em.createQuery(jpql, Admin.class);
			query.setParameter("mNo", mNo);
			query.setParameter("pwd", password);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void update(Admin admin) {
		EntityManagerFactory factory = JPAUtil.getFactory();
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {

			tx.begin();
			em.merge(admin);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		}
	}

	public Library getLibrary(Admin admin) {
		EntityManagerFactory factory = JPAUtil.getFactory();
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT l FROM Library l WHERE l.owner = :admin";
			TypedQuery<Library> query = em.createQuery(jpql, Library.class);
			query.setParameter("admin", admin);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}
}