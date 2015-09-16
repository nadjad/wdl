package test;

import java.util.Date;

import dao.WorkflowDAO;
import datamodel.Workflow;

public class Test {

	public Test() {
		super();
		// testInsertInput();
		testInsert();
		// testDelete();
		// testSearch();
	}

	//
	// public void testSearch() {
	// InputDAO idao = new InputDAO();
	// TaskDAO tdao = new TaskDAO();
	// WorkflowDAO wdao = new WorkflowDAO();
	// Task t = tdao.getTask(456);
	// System.out.println("task:" + t.getCommand_line());
	// Workflow w = wdao.getWorkflow("pppp132");
	// System.out.println("wf:" + w.getTitle());
	// }

	// public void testInsertInput() {
	// InputDAO idao = new InputDAO();
	// Workflow w = new Workflow();
	// w.setStatus("running");
	// w.setStart(new Date());
	// w.setTitle("pppp21123dd32");
	// Task t = new Task();
	// t.setId(456);
	// t.setId_string("op1");
	// t.setStatus("running");
	// t.setCommand_line("oooo3oo");
	// t.setWorkflow(w);
	// Input ii = new Input();
	// ii.setAvailable(false);
	// ii.setFilePath("poooorereroo");
	// ii.setTask(t);
	// idao.insertInput(ii);
	//
	// }
	//
	public void testInsert() {
		WorkflowDAO wdao = new WorkflowDAO();
		Workflow w = new datamodel.Workflow("dddd", new Date(), "running");
		w.setStatus("running");
		// w.setStartTime(new Date());
		w.setTitle("pppp211");
		wdao.insertWorkflow(w);
		w.setTitle("pppp139");
		wdao.insertWorkflow(w);
	}

	//
	// public void testDelete() {
	// WorkflowDAO wdao = new WorkflowDAO();
	// Workflow w = new Workflow();
	// w.setStatus("running");
	// w.setStart(new Date());
	// w.setTitle("pppp");
	// wdao.insertWorkflow(w);
	// wdao.deleteWorkflow("pppp");
	// }

	public static void main(String[] args) {
		new Test();
	}
}
