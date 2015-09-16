package dao;

import java.util.Date;
import java.util.Set;

import datamodel.Input;
import datamodel.Task;
import datamodel.Workflow;

public class DataAccess {

	private InputDAO idao;
	private TaskDAO tdao;
	private WorkflowDAO wdao;

	public DataAccess() {
		idao = new InputDAO();
		tdao = new TaskDAO();
		wdao = new WorkflowDAO();
	}

	/************************* Workflow methods **************************/

	public int insertWorkflow(String flowId, Date startDate, String status,
			int userID) {
		Workflow wf = new Workflow(flowId, new Date(), "running");
		wf.setUid(1);
		return wdao.insertWorkflow(wf);
	}

	public Workflow getWorkflow(Integer wid) {
		return wdao.getWorkflow(wid);
	}

	/************************* Task methods **************************/
	public int insertTask(Workflow wf, Integer vm_id, String command,
			String status, Set<Input> inputs) {
		Task t = new Task(wf, vm_id, command, status, inputs);
		return tdao.insertTask(t);
	}

	public Task getTask(int tid) {
		return tdao.getTask(tid);
	}

}
