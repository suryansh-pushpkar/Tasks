package com.lib.dao;

import java.util.List;

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

	public List<Object[]> getBooksWithQuantities(int libraryId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			System.out.println("Searching books for Library ID: " + libraryId);

			String jpql = "SELECT b.name, b.author, COUNT(b), b.isbn " + "FROM Book b WHERE b.library.id = :libId "
					+ "GROUP BY b.name, b.author, b.isbn";

			List<Object[]> results = em.createQuery(jpql, Object[].class).setParameter("libId", libraryId)
					.getResultList();

			System.out.println("Books found: " + results.size());
			return results;
		} finally {
			em.close();
		}
	}

	public Book findBookByNameAndAuthor(String name, String author) {
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT b FROM Book b WHERE b.name = :name AND b.author = :author";
			return em.createQuery(jpql, Book.class).setParameter("name", name).setParameter("author", author)
					.setMaxResults(1) 
					.getSingleResult();
		} catch (Exception e) {
			return null;
		} finally {
			em.close();
		}
	}

	public void updateBookDetails(String oldName, String oldAuthor, String newName, String newAuthor, String newIsbn) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			String jpql = "UPDATE Book b SET b.name = :newName, b.author = :newAuthor, b.isbn = :newIsbn "
					+ "WHERE b.name = :oldName AND b.author = :oldAuthor";
			em.createQuery(jpql).setParameter("newName", newName).setParameter("newAuthor", newAuthor)
					.setParameter("newIsbn", newIsbn).setParameter("oldName", oldName)
					.setParameter("oldAuthor", oldAuthor).executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public void deleteBooksByNameAndAuthor(String name, String author, int libraryId) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			
			String jpql = "DELETE FROM Book b WHERE b.name = :name AND b.author = :author AND b.library.id = :libId";

			int deletedCount = em.createQuery(jpql).setParameter("name", name).setParameter("author", author)
					.setParameter("libId", libraryId).executeUpdate();

			tx.commit();
			System.out.println("Deleted " + deletedCount + " copies of " + name);
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			em.close();
		}
	}
}
