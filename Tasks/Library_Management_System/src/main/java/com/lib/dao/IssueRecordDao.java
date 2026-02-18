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

			if (isBookRequestedByOthers(recordId)) {
				tx.begin();
				IssueRecord record = em.find(IssueRecord.class, recordId);

				if (record != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(record.getEndDate());
					cal.add(Calendar.DAY_OF_MONTH, daysToAdd);

					record.setEndDate(cal.getTime());
					em.merge(record);

					tx.commit();
					return true;
				}
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

	public List<IssueRecord> getActiveIssuesByUser(int userId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			return em.createQuery("SELECT r FROM IssueRecord r WHERE r.user.id = :uId AND r.status = 'ISSUED'",
					IssueRecord.class).setParameter("uId", userId).getResultList();
		} finally {
			em.close();
		}
	}

	public List<IssueRecord> getAllIssuesByUser(int userId) {
		EntityManager em = JPAUtil.getFactory().createEntityManager();
		try {
			return em.createQuery("SELECT r FROM IssueRecord r WHERE r.user.id = :uId ORDER BY r.startDate DESC",
					IssueRecord.class).setParameter("uId", userId).getResultList();
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