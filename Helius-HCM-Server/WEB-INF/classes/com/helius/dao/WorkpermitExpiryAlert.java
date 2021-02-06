package com.helius.dao;

import java.sql.Date;
import java.sql.Timestamp;

public class WorkpermitExpiryAlert {
	
	private String employee_id;
	private String employee_name;
	private String account_manager;
	private String helius_email_id;
	private Date work_permit_name_expiry_date;
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
	public String getAccount_manager() {
		return account_manager;
	}
	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}
	public String getHelius_email_id() {
		return helius_email_id;
	}
	public void setHelius_email_id(String helius_email_id) {
		this.helius_email_id = helius_email_id;
	}
	public Date getWork_permit_name_expiry_date() {
		return work_permit_name_expiry_date;
	}
	public void setWork_permit_name_expiry_date(Date work_permit_name_expiry_date) {
		this.work_permit_name_expiry_date = work_permit_name_expiry_date;
	}

	
}
