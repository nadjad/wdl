package dao;

import hibernate.HibernateUtil;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import datamodel.Task;

public class TaskDAO {
	private org.hibernate.classic.Session session;

	public int insertTask(Task task) {
		session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			// Task t = getTask(task.getId());
			// if (t == null) {
			int id = (int) session.save(task);
			session.getTransaction().commit();
			return id;
			// }
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return -5;
		}
	}

	public Task getTask(Integer id) {
		session = HibernateUtil.getSessionFactory().openSession();
		return (Task) session.get(Task.class, id);
	}

	public List<Task> getWaitingTasks() {
		session = HibernateUtil.getSessionFactory().openSession();
		Query q = session.createQuery("from Task where status = :idstr ");
		q.setParameter("idstr", "waiting");
		return q.list();
	}

	public void updateTask(Task task) {
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(task);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public boolean deleteTask(Integer id) {
		session = HibernateUtil.getSessionFactory().openSession();
		try {
			Transaction t = session.beginTransaction();
			Task task = (Task) session.load(Task.class, id);
			session.delete(task);
			t.commit();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
