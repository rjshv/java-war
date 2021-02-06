package com.helius.dao;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewJoineeDashboard {
	private String employee_id;
	private String employee_name;
	private String gender;
	private String client;
	private String ctc_per_month;
	private String payroll_entity;
	private String nationality;
	private String account_manager;
	private String helius_recruiter;
	private String work_country;
	private String work_permit_name;
	private String work_permit_number;
	private Timestamp work_permit_name_issued_date;
	private Date date_of_birth;
	private Timestamp actual_date_of_joining;
	private String designation;
	private String marital_status;
	private String helius_email_id;
	private String personal_email_id;
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getMarital_status() {
		return marital_status;
	}
	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}
	public String getHelius_email_id() {
		return helius_email_id;
	}
	public void setHelius_email_id(String helius_email_id) {
		this.helius_email_id = helius_email_id;
	}
	public String getPersonal_email_id() {
		return personal_email_id;
	}
	public void setPersonal_email_id(String personal_email_id) {
		this.personal_email_id = personal_email_id;
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
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	public String getWork_country() {
		return work_country;
	}
	public void setWork_country(String work_country) {
		this.work_country = work_country;
	}
	public String getCtc_per_month() {
		return ctc_per_month;
	}
	public void setCtc_per_month(String ctc_per_month) {
		this.ctc_per_month = ctc_per_month;
	}
	public String getPayroll_entity() {
		return payroll_entity;
	}
	public void setPayroll_entity(String payroll_entity) {
		this.payroll_entity = payroll_entity;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getWork_permit_name() {
		return work_permit_name;
	}
	public void setWork_permit_name(String work_permit_name) {
		this.work_permit_name = work_permit_name;
	}
	public String getWork_permit_number() {
		return work_permit_number;
	}
	public void setWork_permit_number(String work_permit_number) {
		this.work_permit_number = work_permit_number;
	}
	public Timestamp getWork_permit_name_issued_date() {
		return work_permit_name_issued_date;
	}
	public void setWork_permit_name_issued_date(Timestamp work_permit_name_issued_date) {
		this.work_permit_name_issued_date = work_permit_name_issued_date;
	}
	
	public Date getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public Timestamp getActual_date_of_joining() {
		return actual_date_of_joining;
	}
	public void setActual_date_of_joining(Timestamp actual_date_of_joining) {
		this.actual_date_of_joining = actual_date_of_joining;
	}
	
	

}
