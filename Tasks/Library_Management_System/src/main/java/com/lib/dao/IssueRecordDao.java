package com.lib.dao;

import com.lib.entity.IssueRecord;
import com.lib.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IssueRecordDao {

	public List<IssueRecord> getRenewalRequestsByLibrary(int libId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			String jpql = "SELECT r FROM IssueRecord r JOIN r.book b "
					+ "WHERE b.library.id = :libId AND r.status = 'RENEW_REQUESTED'";
			return em.createQuery(jpql, IssueRecord.class).setParameter("libId", libId).getResultList();
		} finally {
			em.close();
		}
	}

	public boolean isBookRequestedByOthers(int bookId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {

			String jpql = "SELECT COUNT(r) FROM IssueRecord r WHERE r.book.id = :bId AND r.status = 'PENDING'";
			Long count = em.createQuery(jpql, Long.class).setParameter("bId", bookId).getSingleResult();
			return count > 0;
		} finally {
			em.close();
		}
	}

	public boolean updateRenewalDate(int recordId, int daysToAdd) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			IssueRecord record = em.find(IssueRecord.class, recordId);

			if (record != null) {
				// Calculate the new date
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.setTime(record.getEndDate());
				cal.add(java.util.Calendar.DAY_OF_MONTH, daysToAdd);

				// Update the record
				record.setEndDate(cal.getTime());
				record.setStatus("ISSUED");

				em.merge(record);
				tx.commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	public IssueRecord findById(int id) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			return em.find(IssueRecord.class, id);
		} finally {
			em.close();
		}
	}

	public void update(IssueRecord record) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		em.getTransaction().begin();
		em.merge(record);
		em.getTransaction().commit();
		em.close();
	}

	public void delete(int id) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		em.getTransaction().begin();
		IssueRecord r = em.find(IssueRecord.class, id);
		if (r != null)
			em.remove(r);
		em.getTransaction().commit();
		em.close();
	}

	public List<IssueRecord> getPendingRequests() {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			return em.createQuery("SELECT r FROM IssueRecord r WHERE r.status = 'PENDING' ORDER BY r.startDate ASC",
					IssueRecord.class).getResultList();
		} finally {
			em.close();
		}
	}

	public long getActiveBookCount(int userId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			String jpql = "SELECT COUNT(r) FROM IssueRecord r WHERE r.user.id = :uId "
					+ "AND (r.status = 'ISSUED' OR r.status = 'PENDING')";
			return em.createQuery(jpql, Long.class).setParameter("uId", userId).getSingleResult();
		} finally {
			em.close();
		}
	}

	public String returnBook(int recordId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			IssueRecord record = em.find(IssueRecord.class, recordId);

			if (record != null && !"RETURNED".equals(record.getStatus())) {
				Date today = new Date();
				double fineAmount = 0.0;

				if (today.after(record.getEndDate())) {
					long diffInMillies = Math.abs(today.getTime() - record.getEndDate().getTime());
					long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
					fineAmount = diffInDays * 5.0;
				}

				record.setStatus("RETURNED");

				em.merge(record);
				tx.commit();

				return fineAmount > 0 ? "Book returned late. Total Fine: ₹" + fineAmount
						: "Book returned successfully on time.";
			}
			return "Record not found or already returned.";
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			e.printStackTrace();
			return "Error: " + e.getMessage();
		} finally {
			em.close();
		}
	}

	public List<IssueRecord> getActiveIssuesByLibrary(int libId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			String jpql = "SELECT r FROM IssueRecord r JOIN r.book b "
					+ "WHERE b.library.id = :libId AND r.status = 'ISSUED' " + "ORDER BY r.endDate ASC";
			return em.createQuery(jpql, IssueRecord.class).setParameter("libId", libId).getResultList();
		} finally {
			em.close();
		}
	}

	public List<IssueRecord> getActiveIssuesByUser(int userId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			String jpql = "SELECT r FROM IssueRecord r JOIN FETCH r.book "
					+ "WHERE r.user.id = :uId AND r.status = 'ISSUED'";

			return em.createQuery(jpql, IssueRecord.class).setParameter("uId", userId).getResultList();
		} finally {
			em.close();
		}
	}

	public List<IssueRecord> getAllPendingReturns() {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			return em.createQuery("SELECT r FROM IssueRecord r WHERE r.status = 'ISSUED' ORDER BY r.endDate ASC",
					IssueRecord.class).getResultList();
		} finally {
			em.close();
		}
	}

	public List<IssueRecord> getAllIssuesByUser(int userId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			
			String jpql = "SELECT r FROM IssueRecord r " + "JOIN FETCH r.book b " + "JOIN FETCH b.library "
					+ "WHERE r.user.id = :uId " + "ORDER BY r.startDate DESC";

			return em.createQuery(jpql, IssueRecord.class).setParameter("uId", userId).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new java.util.ArrayList<>(); // Return empty list instead of null to avoid JSP crashes
		} finally {
			em.close();
		}
	}

	public boolean isBookCurrentlyIssued(int bookId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			Long count = em
					.createQuery("SELECT COUNT(r) FROM IssueRecord r WHERE r.book.id = :bId AND r.status = 'ISSUED'",
							Long.class)
					.setParameter("bId", bookId).getSingleResult();
			return count > 0;
		} finally {
			em.close();
		}
	}

	public List<IssueRecord> getPendingRequestsByLibrary(int libId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			// We filter by b.library.id because the book belongs to the library
			String jpql = "SELECT r FROM IssueRecord r JOIN r.book b "
					+ "WHERE b.library.id = :libId AND r.status = 'PENDING'";
			return em.createQuery(jpql, IssueRecord.class).setParameter("libId", libId).getResultList();
		} finally {
			em.close();
		}
	}

	public void save(IssueRecord record) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(record);
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