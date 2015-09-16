package simulatedexecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dao.InputDAO;
import dao.TaskDAO;
import datamodel.Input;
import datamodel.Task;

public class ExecutableTask extends Thread {
	private Task task;
	private Process process;
	private String ar[];
	private InputDAO idao;
	private Executor executor;
	private TaskDAO tdao;

	public ExecutableTask(Executor executor, Task task) {
		super();
		this.executor = executor;
		this.task = task;
		ar = this.task.getCommand_line().trim().split("\\s+");
		this.idao = new InputDAO();
		this.tdao = new TaskDAO();
		this.start();

	}

	@Override
	public void run() {
		String[] params = new String[ar.length + 2];
		params[0] = "java";
		params[1] = "-jar";
		params[2] = ar[0];

		for (int i = 0; i < ar.length - 1; i++) {
			params[3 + i] = ar[i + 1];
		}

		try {
			List<String> inFiles = new ArrayList<String>();
			for (int i = 0; i < this.task.getInputs().size(); i++)
				inFiles.add(ar[i + 1]);
			while (!inputsAvailable(inFiles)) {

				System.out
						.println("$$$$$$$$$$$$$$$$$$$$$$$$$Not all files are available -------------------- waiting ....");
				Thread.sleep(100);
			}

			this.task.setStatus("running");
			tdao.updateTask(this.task);
			List<String> lp = Arrays.asList(params);
			ProcessBuilder p = new ProcessBuilder();
			p.command(lp);

			long tstmp1 = System.nanoTime();
			long tme = (tstmp1 - Executor.time) / 1000000;
			String msg = "Start task: " + task.getId() + ", timp: " + tme
					+ " ms";
			System.out.println(msg);
			executor.putToLog(msg);

			process = p.start();
			// /*************************************/
			// System.out
			// .println("*************logggggggggggggggggggggggggggggggg "
			// + task.getId());
			// DataInputStream dis = new
			// DataInputStream(process.getInputStream());
			// String line = "";
			// while ((line = dis.readLine()) != null) {
			// System.out.println(line);
			// }
			// dis.close();
			// System.out.println("******************************end log "
			// + task.getId());
			// /****************************************************/
			// process.getInputStream();
			// long t1 = System.nanoTime();
			process.waitFor();

			long tstmp = System.nanoTime();
			tme = (tstmp - Executor.time) / 1000000;
			long duration = (tstmp - tstmp1) / 1000000;
			msg = "Terminat task: " + task.getId() + ", timp:" + tme + " ms, "
					+ "durata: " + duration;
			System.out.println(msg);
			executor.putToLog(msg);
			// update the dependency records for all the task's output files
			// determine the output files
			for (int i = task.getInputs().size(); i < ar.length; i++) {
				// get the list of dependencies for each output file
				List<Input> inlist = idao.getInputs(ar[i].trim());
				for (Input in : inlist) {
					in.setAvailable(true);
					idao.updateInput(in);

				}
			}

			task.setStatus("completed");
			tdao.updateTask(task);

			// long t2 = System.nanoTime();
			// double t = (t2 - t1) / 1000000.0;
			// System.out.println("Finished task (" + task.getId()
			// + "). Time taken:" + t);

			// send the wakeUp signal
			this.executor.wakeUp();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean inputsAvailable(List<String> inFiles) {
		for (String s : inFiles) {
			File f = new File(s);
			if (!f.exists())
				return false;
		}
		return true;
	}

	public Task getTask() {
		return task;
	}
}
