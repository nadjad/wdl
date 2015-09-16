package datamodel;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "dependency")
public class Dependency {

	private Integer id;
	private Task task;
	private Input input;
	private boolean accomplished;

	public Dependency(Task task, Input input, boolean accomplished) {
		super();
		this.task = task;
		this.input = input;
	}

	public Dependency() {
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
	@JoinColumn(name = "task_id", nullable = false)
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "input_id", nullable = false)
	public Input getInput() {
		return input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	@Column(name = "accomplished", nullable = false)
	public boolean getAccomplished() {
		return accomplished;
	}

	public void setAccomplished(Boolean accomplished) {
		this.accomplished = accomplished;
	}

}
