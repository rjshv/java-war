package com.helius.dao;

import java.sql.Timestamp;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExitEmployeeDashboard {
	private String employee_id;
	private String employee_name;
	private String employee_status;
	private String nationality;
	private String helius_recruiter;
	private String client;
	private Timestamp actual_date_of_joining;
	@JsonProperty("payroll")
	private String payroll_entity;
	private String client_group;
	@JsonProperty("ctc_month")
	private String new_monthly_basic;
	private String created_by;
	private String work_country;
	private String account_manager;
	@JsonProperty("visa_status")
	private String work_permit_name;
	private String client_hiring_manager;
	private String skills;
	@JsonProperty("nric")
	private String visa_status;
	private String work_permit_number;
	private Timestamp work_permit_name_issued_date;
	private Timestamp work_permit_name_expiry_date;
	private Timestamp last_modified_date;
	private Timestamp relieving_date;
	private Timestamp resignation_date;
	private Timestamp contractual_working_date;
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getEmployee_name() {
		return employee_name;
	}	
	public String getEmployee_status() {
		return employee_status;
	}
	public void setEmployee_status(String employee_status) {
		this.employee_status = employee_status;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getHelius_recruiter() {
		return helius_recruiter;
	}
	public void setHelius_recruiter(String helius_recruiter) {
		this.helius_recruiter = helius_recruiter;
	}	
	public String getWork_permit_number() {
		return work_permit_number;
	}
	public void setWork_permit_number(String work_permit_number) {
		this.work_permit_number = work_permit_number;
	}
	public String getVisa_status() {
		return visa_status;
	}
	public void setVisa_status(String visa_status) {
		this.visa_status = visa_status;
	}
	public Timestamp getWork_permit_name_issued_date() {
		return work_permit_name_issued_date;
	}
	public void setWork_permit_name_issued_date(Timestamp work_permit_name_issued_date) {
		this.work_permit_name_issued_date = work_permit_name_issued_date;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Timestamp getActual_date_of_joining() {
		return actual_date_of_joining;
	}
	public void setActual_date_of_joining(Timestamp actual_date_of_joining) {
		this.actual_date_of_joining = actual_date_of_joining;
	}
	public String getPayroll_entity() {
		return payroll_entity;
	}
	public void setPayroll_entity(String payroll_entity) {
		this.payroll_entity = payroll_entity;
	}
	public String getClient_group() {
		return client_group;
	}
	public void setClient_group(String client_group) {
		this.client_group = client_group;
	}	
	public String getNew_monthly_basic() {
		return new_monthly_basic;
	}
	public void setNew_monthly_basic(String new_monthly_basic) {
		this.new_monthly_basic = new_monthly_basic;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getWork_country() {
		return work_country;
	}
	public void setWork_country(String work_country) {
		this.work_country = work_country;
	}
	public String getAccount_manager() {
		return account_manager;
	}
	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}
	public String getWork_permit_name() {
		return work_permit_name;
	}
	public void setWork_permit_name(String work_permit_name) {
		this.work_permit_name = work_permit_name;
	}
	public String getClient_hiring_manager() {
		return client_hiring_manager;
	}
	public void setClient_hiring_manager(String client_hiring_manager) {
		this.client_hiring_manager = client_hiring_manager;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public Timestamp getWork_permit_name_expiry_date() {
		return work_permit_name_expiry_date;
	}
	public void setWork_permit_name_expiry_date(Timestamp work_permit_name_expiry_date) {
		this.work_permit_name_expiry_date = work_permit_name_expiry_date;
	}
	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}
	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public Timestamp getRelieving_date() {
		return relieving_date;
	}
	public void setRelieving_date(Timestamp relieving_date) {
		this.relieving_date = relieving_date;
	}
	public Timestamp getResignation_date() {
		return resignation_date;
	}
	public void setResignation_date(Timestamp resignation_date) {
		this.resignation_date = resignation_date;
	}
	public Timestamp getContractual_working_date() {
		return contractual_working_date;
	}
	public void setContractual_working_date(Timestamp contractual_working_date) {
		this.contractual_working_date = contractual_working_date;
	}
	
	
}
