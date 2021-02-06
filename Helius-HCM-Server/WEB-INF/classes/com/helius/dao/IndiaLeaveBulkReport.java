package com.helius.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class IndiaLeaveBulkReport {

	private String employee_id;
	private String employee_name;
	private int client_id;
	private String client;
	private String type_of_leave;
	private float leaves_accrued;
	private float leaves_used;
	private String payroll_entity;
	private String account_manager;
	private String work_country;
	private Float cfLeave;
	private Timestamp actual_date_of_joining;
	private Timestamp usageMonth;
	private Double specialLeave;
	private Double monthlyLeavebalance;
	
	
	public Double getMonthlyLeavebalance() {
		return monthlyLeavebalance;
	}
	public void setMonthlyLeavebalance(Double monthlyLeavebalance) {
		this.monthlyLeavebalance = monthlyLeavebalance;
	}
	public Double getSpecialLeave() {
		return specialLeave;
	}
	public void setSpecialLeave(Double specialLeave) {
		this.specialLeave = specialLeave;
	}
	public Timestamp getUsageMonth() {
		return usageMonth;
	}
	public void setUsageMonth(Timestamp usageMonth) {
		this.usageMonth = usageMonth;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	public Float getCfLeave() {
		return cfLeave;
	}
	public void setCfLeave(Float cfLeave) {
		this.cfLeave = cfLeave;
	}
	public Timestamp getActual_date_of_joining() {
		return actual_date_of_joining;
	}
	public void setActual_date_of_joining(Timestamp actual_date_of_joining) {
		this.actual_date_of_joining = actual_date_of_joining;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
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
	public float getLeaves_accrued() {
		return leaves_accrued;
	}
	public void setLeaves_accrued(float leaves_accrued) {
		this.leaves_accrued = leaves_accrued;
	}
	
	public float getLeaves_used() {
		return leaves_used;
	}
	public void setLeaves_used(float leaves_used) {
		this.leaves_used = leaves_used;
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
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	
	
}
