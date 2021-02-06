package com.helius.dao;

import java.sql.Timestamp;

public class FetchEmployeeLeavesfromClientPolicy {

	private String employee_id;
	private String client;
	private Integer client_id;
	private String work_country;
	private Timestamp actual_date_of_joining;
	
	public Timestamp getActual_date_of_joining() {
		return actual_date_of_joining;
	}
	public void setActual_date_of_joining(Timestamp actual_date_of_joining) {
		this.actual_date_of_joining = actual_date_of_joining;
	}
	public String getWork_country() {
		return work_country;
	}
	public void setWork_country(String work_country) {
		this.work_country = work_country;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Integer getClient_id() {
		return client_id;
	}
	public void setClient_id(Integer client_id) {
		this.client_id = client_id;
	}
	
	
}
