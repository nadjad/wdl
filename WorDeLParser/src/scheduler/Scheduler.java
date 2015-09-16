package scheduler;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.Connection;
import representation.Node;
import representation.Port;
import representation.nodes.FlowNode;
import representation.nodes.Simulation;
import representation.values.Value;
import support.xml.ConfigParser;
import support.xml.Properties;
import dao.DataAccess;
import dao.InputDAO;
import datamodel.Input;
import datamodel.Task;
import datamodel.Workflow;

public class Scheduler {
	private String base_path;
	private String temp_path;
	private String res_path;
	private Integer io = 156;
	private Properties props;
	private String in_path;
	private InputDAO id = new InputDAO();
	private DataAccess dac = new DataAccess();

	public List<String> scheduleSimulation(Simulation sim) {
		// Establish the location of the intermediate/temporary and result files
		props = ConfigParser.parse("config.xml");
		base_path = props.getBasePath() + "workflows\\";

		// create the workflow
		FlowNode f = sim.getFlowNode();
		Collection<Node> nodes = f.getNodes().values();

		Integer wid = dac.insertWorkflow(f.getId(), new Date(), "running", 1);
		Workflow wf = dac.getWorkflow(wid);
		System.out.println("workflow:" + wf.getId());
		base_path = base_path + wf.getId() + "\\";
		temp_path = base_path + "temp\\";
		res_path = base_path + "res\\";
		in_path = base_path + "input\\";
		// derive the commands from each operation node
		for (Node node : nodes) {
			scheduleNode(wf, node, sim.getInMappings(), sim.getOutMappings());
		}
		return null;
	}

	/**
	 * This method populates the database with the task and input dependencies
	 * derived from a workflow node.
	 * 
	 * @param wf
	 *            The parent workflow, containing the node. This is the subject
	 *            of the simulation.
	 * @param node
	 *            The node whose execution is to be scheduled.
	 * @param ims
	 *            A mapping of the input values extracted from the simulation.
	 *            Each parameter is mapped to a string representing the name of
	 *            the port on which it is assigned.
	 * @param outs
	 *            A mapping of the the output parameters of the simulation. Each
	 *            parameter is mapped to a string representing the name of the
	 *            port on which it is assigned.
	 */
	private void scheduleNode(Workflow wf, Node node, Map<String, Value> ims,
			Map<String, String> outs) {

		List<Port> inPortList = node.getOperatorType().getiList();
		List<Port> outPortList = node.getOperatorType().getoList();

		Set<Input> inputs = new HashSet<Input>();

		String inParams = "";

		// create input parameter list for the executable task
		for (Port ip : inPortList) {
			Connection conn = node.getInConnection(ip.getName());
			if (conn != null) {
				/*
				 * if the connection originates from one of the flow's ports,
				 * then we have direct access to the value
				 */
				if (conn.comesFromFlowPort()) {
					String inFilePath = in_path
							+ ims.get(conn.getOrigin().getNodePortId())
									.getValue();
					inParams = inParams + " -i " + inFilePath;
					Input inp = id.getInput(inFilePath.trim());
					if (inp == null)
						inp = new Input(inFilePath.trim(), false, wf);
					inputs.add(inp);
				}/*
				 * if not, we create an input file to hold the intermediate
				 * value to be passed to the next operator
				 */
				else {
					String tempFilePath = temp_path + conn.getConnectionId()
							+ ".itm";
					// all the inputs will be added to the database shortly
					// after we get access to the task object
					Input inp = id.getInput(tempFilePath.trim());
					if (inp == null)
						inp = new Input(tempFilePath.trim(), false, wf);
					inputs.add(inp);
					inParams = inParams + " -i " + tempFilePath;
				}
			}
		}
		// System.out.println(node.getId() + ":input:" + inParams);

		// create output parameter list for the current executable task
		String outParams = "";
		for (Port op : outPortList) {
			Connection conn = node.getOutConnection(op.getName());
			if (conn != null) {
				String id;
				/*
				 * if the connection leads to one of the flow's output ports, we
				 * treat that end as a result
				 */
				if ((id = conn.leadsToFlowPort()) != null) {
					outParams = outParams + " -o " + res_path + outs.get(id);
				}
				/*
				 * otherwise, we assign that connection to an intermediate file
				 */
				else {
					outParams = outParams + " -o " + temp_path
							+ conn.getConnectionId() + ".itm";
				}
			}
		}
		// System.out.println(node.getId() + ":output:" + outParams);

		// Create, initialize and insert the Task record into the database
		// Task t = new Task();
		// t.setStatus("waiting");
		// t.setId(io);
		// t.setVm_id(2);
		// t.setInputs(inputs);
		// io++;
		// t.setCommand_line(node.getOperatorType().getPath() + " " + inParams
		// + " " + outParams);
		//
		// t.setWorkflow(wf);
		//
		// // insert the task into the database
		// TaskDAO td = new TaskDAO();
		// try {
		// td.insertTask(t);
		// } catch (Exception e) {
		//
		// }
		String command = node.getOperatorType().getPath() + " " + inParams
				+ " " + outParams;
		int tid = dac.insertTask(wf, 3, command, "waiting", inputs);

		Task t = dac.getTask(tid);
		// InputDAO id = new InputDAO();
		// Iterator<Input> it = inputs.iterator();
		// Input iii = it.next();
		// id.insertInput(iii);
		// System.out.println("insert input:" + iii);
		System.out.println("inserted command: " + t.getCommand_line());

		// insert the intermediate files and the dependencies into the database
		// InputDAO id = new InputDAO();
		// for (Input i : inputs) {
		// id.insertInput(i);
		// }

		// try to execute the task
		// System.out.println("**********************************************");
		// ExecutableTask et = new ExecutableTask(this, t);
		// et.run();

	}

	private void createDirectory(String name) {
		File theDir = new File(name);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			try {
				theDir.mkdir();
			} catch (SecurityException se) {
				System.out.println("Could not create directory " + name + "\n");
				se.printStackTrace();
			}
		}
	}

}
