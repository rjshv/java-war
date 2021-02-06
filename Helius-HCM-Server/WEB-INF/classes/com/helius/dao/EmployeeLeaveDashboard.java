package com.helius.dao;

import java.sql.Timestamp;

import javax.persistence.Column;

public class EmployeeLeaveDashboard {

	private String employee_id;
	private String employee_name;
	private int client_id;
	private String client;
	private String type_of_leave;
	private Timestamp startdate;
	private Timestamp enddate;
	private float leaves_used;
	private String ampm;
	private String remarks;
	private String payroll_entity;
	private String account_manager;
	private String work_country;
	private String timesheet_error;
	private String approval;
	private float hours;
	private String period;
	
	
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
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
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public String getType_of_leave() {
		return type_of_leave;
	}
	public void setType_of_leave(String type_of_leave) {
		this.type_of_leave = type_of_leave;
	}
	public Timestamp getStartdate() {
		return startdate;
	}
	public void setStartdate(Timestamp startdate) {
		this.startdate = startdate;
	}
	public Timestamp getEnddate() {
		return enddate;
	}
	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}
	public float getLeaves_used() {
		return leaves_used;
	}
	public void setLeaves_used(float leaves_used) {
		this.leaves_used = leaves_used;
	}
	public String getAmpm() {
		return ampm;
	}
	public void setAmpm(String ampm) {
		this.ampm = ampm;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getPayroll_entity() {
		return payroll_entity;
	}
	public void setPayroll_entity(String payroll_entity) {
		this.payroll_entity = payroll_entity;
	}
	public String getAccount_manager() {
		return account_manager;
	}
	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}
	public String getWork_country() {
		return work_country;
	}
	public void setWork_country(String work_country) {
		this.work_country = work_country;
	}
	public String getTimesheet_error() {
		return timesheet_error;
	}
	public void setTimesheet_error(String timesheet_error) {
		this.timesheet_error = timesheet_error;
	}
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	public float getHours() {
		return hours;
	}
	public void setHours(float hours) {
		this.hours = hours;
	}
	
}
