package com.helius.dao;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BeelineTimesheetDashboard {

	@Column
	private String employee_id;
	@Column
	private String employee_name;
	@Column
	private String assignment_id;
	@Column
	private int employee_beeline_timesheet_id;
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
	private String client;
	@Column
	private String bank_ifsc_code;
	@Column
	private String client_group;
	@Column
	private String account_manager;
	@Column
	private String client_email_id;
	@Column
	private String personal_email_id;
	@Column
	private String payroll_entity;
	
	
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getAssignment_id() {
		return assignment_id;
	}
	public void setAssignment_id(String assignment_id) {
		this.assignment_id = assignment_id;
	}
	public String getDbs_manager() {
		return dbs_manager;
	}
	
	public String getBank_ifsc_code() {
		return bank_ifsc_code;
	}
	public void setBank_ifsc_code(String bank_ifsc_code) {
		this.bank_ifsc_code = bank_ifsc_code;
	}
	public int getEmployee_beeline_timesheet_id() {
		return employee_beeline_timesheet_id;
	}
	public void setEmployee_beeline_timesheet_id(int employee_beeline_timesheet_id) {
		this.employee_beeline_timesheet_id = employee_beeline_timesheet_id;
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
	public Timestamp getTimesheet_month() {
		return timesheet_month;
	}
	public void setTimesheet_month(Timestamp timesheet_month) {
		this.timesheet_month = timesheet_month;
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
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getClient_group() {
		return client_group;
	}
	public void setClient_group(String client_group) {
		this.client_group = client_group;
	}
	public String getAccount_manager() {
		return account_manager;
	}
	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}
	public String getClient_email_id() {
		return client_email_id;
	}
	public void setClient_email_id(String client_email_id) {
		this.client_email_id = client_email_id;
	}
	public String getPersonal_email_id() {
		return personal_email_id;
	}
	public void setPersonal_email_id(String personal_email_id) {
		this.personal_email_id = personal_email_id;
	}
	public String getPayroll_entity() {
		return payroll_entity;
	}
	public void setPayroll_entity(String payroll_entity) {
		this.payroll_entity = payroll_entity;
	}
	
	
}
