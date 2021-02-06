package com.helius.entities;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name="Employee_Work_Permit_Details")
@Audited
public class Employee_Work_Permit_Details extends HeliusEntity {
	
	@Id
	@Column
	private int employee_work_permit_id;
	@Column
	private String employee_id;
	@Column
	private String nationality;
	@Column
	private String national_id;
	@Column
	private String work_country;
	@Column
	private String aadhar_number;
	@Column
	private String pan_number;
	@Column
	private String chinese_id_18_digits;
	@Column
	private String work_permit_name;
	@Column
	private String work_permit_number;
	@Column
	private Timestamp work_permit_name_issued_date;
	@Column
	private Timestamp work_permit_name_expiry_date;
	@Column
	private String passport_number;
	@Column
	private Timestamp passport_issued_date;
	@Column
	private Timestamp passport_expiry_date;
	@Column
	private String workpermit_status;
	@Column
	private Timestamp workpermit_cancel_date;
	@Column
	private String special_approval;
	@Column
	private Timestamp approval_period;
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
	
	
	
	public String getWorkpermit_status() {
		return workpermit_status;
	}
	public void setWorkpermit_status(String workpermit_status) {
		this.workpermit_status = workpermit_status;
	}
	public Timestamp getWorkpermit_cancel_date() {
		return workpermit_cancel_date;
	}
	public void setWorkpermit_cancel_date(Timestamp workpermit_cancel_date) {
		this.workpermit_cancel_date = workpermit_cancel_date;
	}
	public String getSpecial_approval() {
		return special_approval;
	}
	public void setSpecial_approval(String special_approval) {
		this.special_approval = special_approval;
	}
	
	public Timestamp getApproval_period() {
		return approval_period;
	}
	public void setApproval_period(Timestamp approval_period) {
		this.approval_period = approval_period;
	}
	public String getNational_id() {
		return national_id;
	}
	public void setNational_id(String national_id) {
		this.national_id = national_id;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getWork_country() {
		return work_country;
	}
	public void setWork_country(String work_country) {
		this.work_country = work_country;
	}
	public String getAadhar_number() {
		return aadhar_number;
	}
	public void setAadhar_number(String aadhar_number) {
		this.aadhar_number = aadhar_number;
	}
	public String getPan_number() {
		return pan_number;
	}
	public void setPan_number(String pan_number) {
		this.pan_number = pan_number;
	}
	public String getChinese_id_18_digits() {
		return chinese_id_18_digits;
	}
	public void setChinese_id_18_digits(String chinese_id_18_digits) {
		this.chinese_id_18_digits = chinese_id_18_digits;
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
	public int getEmployee_work_permit_id() {
		return employee_work_permit_id;
	}
	public void setEmployee_work_permit_id(int employee_work_permit_id) {
		this.employee_work_permit_id = employee_work_permit_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
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
	
	public String getPassport_number() {
		return passport_number;
	}
	public void setPassport_number(String passport_number) {
		this.passport_number = passport_number;
	}
	public Timestamp getWork_permit_name_issued_date() {
		return work_permit_name_issued_date;
	}
	public void setWork_permit_name_issued_date(Timestamp work_permit_name_issued_date) {
		this.work_permit_name_issued_date = work_permit_name_issued_date;
	}
	public Timestamp getWork_permit_name_expiry_date() {
		return work_permit_name_expiry_date;
	}
	public void setWork_permit_name_expiry_date(Timestamp work_permit_name_expiry_date) {
		this.work_permit_name_expiry_date = work_permit_name_expiry_date;
	}
	public Timestamp getPassport_issued_date() {
		return passport_issued_date;
	}
	public void setPassport_issued_date(Timestamp passport_issued_date) {
		this.passport_issued_date = passport_issued_date;
	}
	public Timestamp getPassport_expiry_date() {
		return passport_expiry_date;
	}
	public void setPassport_expiry_date(Timestamp passport_expiry_date) {
		this.passport_expiry_date = passport_expiry_date;
	}
	
	

}
