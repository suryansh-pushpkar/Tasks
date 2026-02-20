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

	public void updateQuantity(int bookId, int amount) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			Book book = em.find(Book.class, bookId);
			if (book != null) {
				book.setQuantity(book.getQuantity() + amount);
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
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

	public void updateBook(Book book) {
	    EntityManager em = factory.createEntityManager();
	    try {
	        em.getTransaction().begin();
	        
	        // 1. Fetch the LIVE entity from the database using the ID
	        Book dbBook = em.find(Book.class, book.getId());
	        
	        if (dbBook != null) {
	            // 2. Map the updated values from your form (book) to the database object (dbBook)
	            dbBook.setName(book.getName());
	            dbBook.setAuthor(book.getAuthor());
	            dbBook.setEdition(book.getEdition());
	            dbBook.setQuantity(book.getQuantity());
	            
	            // 3. Commit the transaction
	            // Dirty Checking will automatically detect changes in 'dbBook' and push the SQL UPDATE
	            em.getTransaction().commit();
	            System.out.println("DEBUG: Database updated for ID " + book.getId());
	        } else {
	            System.out.println("DEBUG: Book not found in DB for ID " + book.getId());
	        }
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	}

	public void deleteBook(int id) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		try {
			Book b = em.find(Book.class, id);
			if (b != null) {
				em.remove(b);
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
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

	public List<Book> getLibraryInventory(int libId) {
		EntityManager em = factory.createEntityManager();
		try {
			// We use JPQL to select the Book objects where the associated
			// library's ID matches the logged-in admin's library ID.
			String jpql = "SELECT b FROM Book b WHERE b.library.id = :libId";

			return em.createQuery(jpql, Book.class).setParameter("libId", libId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new java.util.ArrayList<>();
		} finally {
			em.close();
		}
	}

}