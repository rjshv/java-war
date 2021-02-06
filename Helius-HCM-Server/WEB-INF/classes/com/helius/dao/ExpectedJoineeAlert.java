package com.helius.dao;

import javax.persistence.Column;

public class ExpectedJoineeAlert {

	
	private int offer_id;
	private String employee_name;
	private String account_manager;
	private String helius_recruiter;
	private String client;
	private String payroll_entity;
	private String work_country;
	private String bgv_required;
	private String bgv_completed;
	public int getOffer_id() {
		return offer_id;
	}
	public void setOffer_id(int offer_id) {
		this.offer_id = offer_id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getAccount_manager() {
		return account_manager;
	}
	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}
	public String getHelius_recruiter() {
		return helius_recruiter;
	}
	public void setHelius_recruiter(String helius_recruiter) {
		this.helius_recruiter = helius_recruiter;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPayroll_entity() {
		return payroll_entity;
	}
	public void setPayroll_entity(String payroll_entity) {
		this.payroll_entity = payroll_entity;
	}
	public String getWork_country() {
		return work_country;
	}
	public void setWork_country(String work_country) {
		this.work_country = work_country;
	}
	public String getBgv_required() {
		return bgv_required;
	}
	public void setBgv_required(String bgv_required) {
		this.bgv_required = bgv_required;
	}
	public String getBgv_completed() {
		return bgv_completed;
	}
	public void setBgv_completed(String bgv_completed) {
		this.bgv_completed = bgv_completed;
	}
	
	
}
