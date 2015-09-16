package main;

import hibernate.HibernateUtil;

import java.util.Date;

import org.hibernate.Session;

import datamodel.Input;
import datamodel.Task;
import datamodel.Workflow;

public class App {
	public static void main(String[] args) {
		System.out.println("Maven + Hibernate + MySQL");
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();
		// Workflow w = new Workflow();
		// w.setStatus("running");
		// w.setStart(new Date());
		// w.setTitle("pppp");
		// session.save(w);
		// Task t = new Task(12, "aaa", "waiting", 12);
		// t.setWorkflow(w);

		// stock.setStockCode("4715");
		// stock.setStockName("GENM");
		Input i = new Input();
		i.setFilePath("ooooooooooooooooooo");
		i.setAvailable(false);
		session.save(i);

		Workflow ww = new Workflow();
		ww.setStart(new Date());
		ww.setTitle("utytiut");
		ww.setUid(655);
		ww.setStatus("completed");
		session.save(ww);

		Task t = new Task();
		t.setId(6543);
		t.setCommand_line("calfjsdkfjsl);dkjfs");
		t.addInput(i);
		t.setStatus("running");
		t.setVm_id(65);
		t.setWorkflow(ww);
		session.save(t);
		session.getTransaction().commit();
	}
}
