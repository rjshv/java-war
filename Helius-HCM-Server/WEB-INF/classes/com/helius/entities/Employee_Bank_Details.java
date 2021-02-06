package com.helius.entities;

import java.sql.Date;
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
@Table(name="Employee_Bank_Details")
@Audited
public class Employee_Bank_Details {

	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_bank_details_id;
	@Column
	private String employee_id;
	@Column
	private String employee_name_on_bank_records;
	@Column
	private String bank_name;
	@Column
	private String branch_code;
	@Column
	private String bank_account_number;
	@Column
	private String bank_ifsc_code;
	@Column
	private String cancelled_cheque_path;
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

	
	public String getCancelled_cheque_path() {
		return cancelled_cheque_path;
	}
	public void setCancelled_cheque_path(String cancelled_cheque_path) {
		this.cancelled_cheque_path = cancelled_cheque_path;
	}

	public String getBranch_code() {
		return branch_code;
	}
	public void setBranch_code(String branch_code) {
		this.branch_code = branch_code;
	}
	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}
	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public Timestamp getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}
	public String getLast_modified_by() {
		return last_modified_by;
	}
	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}
	
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public int getEmployee_bank_details_id() {
		return employee_bank_details_id;
	}
	public void setEmployee_bank_details_id(int employee_bank_details_id) {
		this.employee_bank_details_id = employee_bank_details_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getEmployee_name_on_bank_records() {
		return employee_name_on_bank_records;
	}
	public void setEmployee_name_on_bank_records(String employee_name_on_bank_records) {
		this.employee_name_on_bank_records = employee_name_on_bank_records;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_account_number() {
		return bank_account_number;
	}
	public void setBank_account_number(String bank_account_number) {
		this.bank_account_number = bank_account_number;
	}
	
	public String getBank_ifsc_code() {
		return bank_ifsc_code;
	}
	public void setBank_ifsc_code(String bank_ifsc_code) {
		this.bank_ifsc_code = bank_ifsc_code;
	}
	
}
