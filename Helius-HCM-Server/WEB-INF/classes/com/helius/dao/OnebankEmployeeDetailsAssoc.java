package com.helius.dao;

import javax.persistence.Column;

public class OnebankEmployeeDetailsAssoc {
	
	@Column
	private String onebankId;
	
	@Column
	private String employee_id;
	
	@Column
	private String employee_name;
	
	@Column
	private String client_email_id;
	
	@Column
	private String personal_email_id;

	public String getOnebankId() {
		return onebankId;
	}

	public void setOnebankId(String onebankId) {
		this.onebankId = onebankId;
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
	
	

}
