package datamodel;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "workflows")
public class Workflow implements Serializable {

	private Integer wid;
	private Integer uid;
	private String title;
	private Date start;
	private String status;

	// private Integer same_vm_as;

	public Workflow() {
		super();
	}

	public Workflow(Integer id, String title, Date start, String status) {
		super();
		this.wid = id;
		this.title = title;
		this.start = start;
		this.status = status;
	}

	public Workflow(String title, Date start, String status) {
		super();
		this.title = title;
		this.start = start;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return wid;
	}

	public void setId(Integer id) {
		this.wid = id;
	}

	@Column(name = "user_id", nullable = false)
	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer id) {
		this.uid = id;
	}

	@Column(name = "title", unique = true, nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_time", nullable = false)
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@Column(name = "status", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((wid == null) ? 0 : wid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Workflow other = (Workflow) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (wid == null) {
			if (other.wid != null)
				return false;
		} else if (!wid.equals(other.wid))
			return false;
		return true;
	}

}
