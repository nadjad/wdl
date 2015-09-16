package dao;

import hibernate.HibernateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import datamodel.Input;
import datamodel.Task;

public class InputDAO {

	private org.hibernate.classic.Session session;

	public InputDAO() {
		super();
	}

	public int insertInput(Input input) {
		session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			int id = (int) session.save(input);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			return -5;
		}
	}

	public Input getInput(Integer id) {
		session = HibernateUtil.getSessionFactory().openSession();
		return (Input) session.get(Input.class, id);
	}

	public List<Input> getInputs(String filepath) {
		session = HibernateUtil.getSessionFactory().openSession();
		Query q = session.createQuery("from Input where file_name = :flp ");
		q.setParameter("flp", filepath.trim());
		List<Object> l = q.list();
		List<Input> inputs = new ArrayList<Input>();
		for (Object o : l)
			inputs.add((Input) o);
		return inputs;
	}

	public Input getInput(String filepath) {
		session = HibernateUtil.getSessionFactory().openSession();
		Query q = session.createQuery("from Input where file_name = :flp ");
		q.setParameter("flp", filepath.trim());
		List<Object> l = q.list();
		if (l != null && !l.isEmpty())
			return (Input) l.get(0);
		return null;
	}

	public List<Input> getInputsByTask(Task task) {
		session = HibernateUtil.getSessionFactory().openSession();
		Query q = session.createQuery("from Input where task_id = :tid ");
		q.setParameter("tid", task.getId());
		List<Object> l = q.list();
		List<Input> inputs = new ArrayList<Input>();
		for (Object o : l)
			inputs.add((Input) o);
		return inputs;
	}

	public void updateInput(Input input) {
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(input);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public void deleteAll() {
		session = HibernateUtil.getSessionFactory().openSession();
		String hql = String.format("from Input");
		System.out.println("deleteeeeeeeeeeeeeeeeeeeee");
		Query query = session.createQuery(hql);
		List<Object> l = query.list();
		for (Object o : l) {
			Input i = (Input) o;
			File f = new File(i.getFilePath().trim());
			if (f.exists())
				f.delete();
		}
	}
}
