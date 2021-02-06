package com.helius.entities;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="Employee_Beeline_Timesheet")
public class Employee_Beeline_Timesheet {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_beeline_timesheet_id;
	@Column
	private String employee_id;
	@Column
	private String employee_name;
	@Column
	private String assignment_id;
	@Column
	private String dbs_manager;
	@Column
	private Timestamp received_date;
	@Column
	private Timestamp final_submission_date;
	@Column
	private String timesheet_status;
	@Column
	private Timestamp timesheet_month;
	@Column
	private String timesheet_document_path;
	@Column
	private String supporting_document_path;
	@Column
	private boolean approved_email_send;
	@Column
	private String comments;
	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	@Column
	@NotAudited
	private String last_modified_by;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	private String created_by;
	public int getEmployee_beeline_timesheet_id() {
		return employee_beeline_timesheet_id;
	}
	public void setEmployee_beeline_timesheet_id(int employee_beeline_timesheet_id) {
		this.employee_beeline_timesheet_id = employee_beeline_timesheet_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getAssignment_id() {
		return assignment_id;
	}
	public void setAssignment_id(String assignment_id) {
		this.assignment_id = assignment_id;
	}
	public Timestamp getTimesheet_month() {
		return timesheet_month;
	}
	public void setTimesheet_month(Timestamp timesheet_month) {
		this.timesheet_month = timesheet_month;
	}
	public String getDbs_manager() {
		return dbs_manager;
	}
	public void setDbs_manager(String dbs_manager) {
		this.dbs_manager = dbs_manager;
	}
	public Timestamp getReceived_date() {
		return received_date;
	}
	public void setReceived_date(Timestamp received_date) {
		this.received_date = received_date;
	}
	
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Timestamp getFinal_submission_date() {
		return final_submission_date;
	}
	public void setFinal_submission_date(Timestamp final_submission_date) {
		this.final_submission_date = final_submission_date;
	}
	public String getTimesheet_status() {
		return timesheet_status;
	}
	public void setTimesheet_status(String timesheet_status) {
		this.timesheet_status = timesheet_status;
	}
	
	public String getTimesheet_document_path() {
		return timesheet_document_path;
	}
	public void setTimesheet_document_path(String timesheet_document_path) {
		this.timesheet_document_path = timesheet_document_path;
	}
	public String getSupporting_document_path() {
		return supporting_document_path;
	}
	public void setSupporting_document_path(String supporting_document_path) {
		this.supporting_document_path = supporting_document_path;
	}
	public boolean isApproved_email_send() {
		return approved_email_send;
	}
	public void setApproved_email_send(boolean approved_email_send) {
		this.approved_email_send = approved_email_send;
	}
	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}
	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public String getLast_modified_by() {
		return last_modified_by;
	}
	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
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
	
	
}
