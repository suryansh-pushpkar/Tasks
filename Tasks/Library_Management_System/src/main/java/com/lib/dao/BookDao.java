package com.lib.dao;

import java.util.List;
import com.lib.entity.Book;
import com.lib.util.JPAUtil;
import jakarta.persistence.*;

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
			if (tx.isActive())
				tx.rollback();
			throw new RuntimeException(e);
		} finally {
			em.close();
		}
	}

	public long getAvailableBookCountByLibrary(int libId) {
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT COUNT(b) FROM Book b WHERE b.library.id = :libId AND b.id NOT IN "
					+ "(SELECT r.book.id FROM IssueRecord r WHERE r.status IN ('ISSUED', 'PENDING', 'RENEW_REQUESTED'))";
			return em.createQuery(jpql, Long.class).setParameter("libId", libId).getSingleResult();
		} finally {
			em.close();
		}
	}

	public List<Object[]> getBooksWithQuantities(int libraryId) {
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT b.name, b.author, COUNT(b) " + "FROM Book b " + "WHERE b.library.id = :libId "
					+ "AND b.id NOT IN (SELECT r.book.id FROM IssueRecord r WHERE r.status IN ('ISSUED', 'PENDING')) "
					+ "GROUP BY b.name, b.author";

			return em.createQuery(jpql, Object[].class).setParameter("libId", libraryId).getResultList();
		} finally {
			em.close();
		}
	}

	public List<Object[]> getAllBooksWithStatus(int libId) {
		EntityManager em = factory.createEntityManager();
		try {
			
			String jpql = "SELECT b.name, b.author, b.isbn, "
					+ "(SELECT r.status FROM IssueRecord r WHERE r.book.id = b.id "
					+ "AND r.status IN ('ISSUED', 'PENDING', 'RENEW_REQUESTED') ORDER BY r.id DESC) "
					+ "FROM Book b WHERE b.library.id = :libId";

			return em.createQuery(jpql, Object[].class).setParameter("libId", libId).getResultList();
		} finally {
			em.close();
		}
	}

	public Book findById(int id) {
		EntityManager em = factory.createEntityManager();
		try {
			return em.find(Book.class, id);
		} finally {
			em.close();
		}
	}

	public List<Book> searchBooks(String query) {
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT b FROM Book b JOIN FETCH b.library WHERE LOWER(b.name) LIKE LOWER(:query) "
					+ "OR LOWER(b.author) LIKE LOWER(:query)";
			return em.createQuery(jpql, Book.class).setParameter("query", "%" + query + "%").getResultList();
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
			em.createQuery(jpql).setParameter("name", name).setParameter("author", author)
					.setParameter("libId", libraryId).executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	public Book findBookByNameAuthorAndLibrary(String name, String author, int libId) {
		EntityManager em = factory.createEntityManager();
		try {
			String jpql = "SELECT b FROM Book b WHERE b.name = :name "
					+ "AND b.author = :author AND b.library.id = :libId";

			return em.createQuery(jpql, Book.class).setParameter("name", name).setParameter("author", author)
					.setParameter("libId", libId).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public long getTotalBookCountByLibrary(int libId) {
		EntityManager em = factory.createEntityManager();
		try {
			return em.createQuery("SELECT COUNT(b) FROM Book b WHERE b.library.id = :libId", Long.class)
					.setParameter("libId", libId).getSingleResult();
		} finally {
			em.close();
		}
	}

	public void updateBookDetails(String oldName, String oldAuthor, String newName, String newAuthor, String newIsbn,
			int libId) {
		EntityManager em = factory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			String jpql = "UPDATE Book b SET b.name = :newName, b.author = :newAuthor, b.isbn = :newIsbn "
					+ "WHERE b.name = :oldName AND b.author = :oldAuthor AND b.library.id = :libId";

			em.createQuery(jpql).setParameter("newName", newName).setParameter("newAuthor", newAuthor)
					.setParameter("newIsbn", newIsbn).setParameter("oldName", oldName)
					.setParameter("oldAuthor", oldAuthor).setParameter("libId", libId).executeUpdate();
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

}