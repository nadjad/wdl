package datamodel;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task implements java.io.Serializable {
	private Integer id;
	private Workflow workflow;
	private Integer vm_id;
	private String command_line;
	private String status;
	private Set<Input> inputs;

	// private Integer same_vm_as;

	public Task(String command_line, String status) {
		super();
		this.command_line = command_line;
		this.status = status;
	}

	public Task(Workflow wf, Integer vm_id, String command, String status,
			Set<Input> inputs) {
		super();
		this.workflow = wf;
		this.vm_id = vm_id;
		this.command_line = command;
		this.status = status;
		this.inputs = inputs;
	}

	public Task() {
		super();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "workflow_id", nullable = false)
	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	@Column(name = "vm_id", unique = false, nullable = true)
	public Integer getVm_id() {
		return vm_id;
	}

	public void setVm_id(Integer vmid) {
		this.vm_id = vmid;
	}

	@Column(name = "command_line", unique = false, nullable = true)
	public String getCommand_line() {
		return command_line;
	}

	public void setCommand_line(String command_line) {
		this.command_line = command_line;
	}

	@Column(name = "status", unique = false, nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "taskInput", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "input_id"))
	public Set<Input> getInputs() {
		return this.inputs;
	}

	public void setInputs(Set<Input> ins) {
		this.inputs = ins;
	}

	public void addInput(Input i) {
		if (this.inputs == null)
			this.inputs = new HashSet<Input>();
		this.inputs.add(i);
	}
}
