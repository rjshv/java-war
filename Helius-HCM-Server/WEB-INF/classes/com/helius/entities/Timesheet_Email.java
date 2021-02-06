package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.sql.Timestamp;


/**
 * The persistent class for the Timesheet_Email database table.
 * 
 */
@Entity
@Audited
@NamedQuery(name="Timesheet_Email.findAll", query="SELECT t FROM Timesheet_Email t")
public class Timesheet_Email implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="employee_timesheet_email_id")
	private int employeeTimesheetEmailId;

	@Column
	@CreationTimestamp
	private Timestamp create_date;

	@Column
	private String created_by;

	@Column(name="employee_id")
	private String employeeId;

	@Column
	@NotAudited
	private String last_modified_by;

	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;

	@Column(name="timesheet_email_id")
	private String timesheetEmailId;

	public Timesheet_Email() {
	}

	public int getEmployeeTimesheetEmailId() {
		return this.employeeTimesheetEmailId;
	}

	public void setEmployeeTimesheetEmailId(int employeeTimesheetEmailId) {
		this.employeeTimesheetEmailId = employeeTimesheetEmailId;
	}


	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getLast_modified_by() {
		return last_modified_by;
	}

	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}

	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}

	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public String getTimesheetEmailId() {
		return this.timesheetEmailId;
	}

	public void setTimesheetEmailId(String timesheetEmailId) {
		this.timesheetEmailId = timesheetEmailId;
	}

}