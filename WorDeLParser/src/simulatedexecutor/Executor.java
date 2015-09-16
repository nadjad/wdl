package simulatedexecutor;

import gui.InfoFrame;

import java.util.List;

import dao.InputDAO;
import dao.TaskDAO;
import dao.WorkflowDAO;
import datamodel.Input;
import datamodel.Task;

public class Executor extends Thread {
	private boolean stopped;
	private boolean pause;
	private TaskDAO td;
	private InputDAO id;
	private WorkflowDAO wd;

	private int runs = 1;
	private int calls = 0;
	private int nrTasks = 0;
	private int steps = 0;

	public static long time;
	private String log = "";
	private InfoFrame iff;

	public Executor() {
		td = new TaskDAO();
		id = new InputDAO();
		wd = new WorkflowDAO();
		System.out
				.println("############################ Executor ################################");
		iff = new InfoFrame();
	}

	@Override
	public synchronized void start() {
		super.start();
		time = System.nanoTime();
	}

	public void step() {
		if (runs > 0) {
			List<Task> tsks = td.getWaitingTasks();

			String msg = "------------------------------Iteratie: " + steps
					+ ", tasks-uri ramase:" + tsks.size()
					+ ", iteratii ramase:" + runs
					+ "---------------------------";
			System.out.println(msg);
			log = log + "\n" + msg;
			refreshFrame();
			for (Task t : tsks) {
				List<Input> inputs = id.getInputsByTask(t);
				if (inputs == null || inputs.isEmpty()
						|| availableInputs(inputs)) {
					// System.out.println("tasks:" + tsks.size());
					// if (inputs == null)
					// System.out.println("cauza executiei: input null");
					// else if (inputs.isEmpty())
					// System.out
					// .println("cauza executiei: lista de input-uri nu are elemente:");
					// else if (availableInputs(inputs))
					// System.out
					// .println("cauza executiei: toate input-urile disponibile");
					nrTasks++;
					ExecutableTask e = new ExecutableTask(this, t);
					// e.start()
					msg = "**** Se executa task: (" + t.getId()
							+ ") cu linia de comanda: java -jar "
							+ t.getCommand_line();
					System.out.println(msg);
					log = log + "\n" + msg;
					refreshFrame();
				}
			}
			runs--;
			steps++;

		}
	}

	private boolean availableInputs(List<Input> inputs) {
		for (Input i : inputs) {
			if (!i.getAvailable())
				return false;
		}
		return true;
	}

	@Override
	public void run() {
		while (Thread.currentThread() == this && !stopped) {
			if (!pause) {
				step();
				if (this.nrTasks == this.calls) {
					id.deleteAll();
					this.stop();
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// used by the executable tasks to signal the completion of a task
	public void wakeUp() {
		this.runs++;
		this.calls++;

	}

	public void refreshFrame() {
		this.iff.reset();
		this.iff.addText(log);
	}

	public void stopThread() {
		this.stopped = true;
	}

	public void pauseThread() {
		this.pause = true;
	}

	public void resumeThread() {
		this.pause = false;
	}

	public void putToLog(String string) {
		this.log = this.log + "\n" + string;
	}

	public String getLog() {
		return log;
	}

}
