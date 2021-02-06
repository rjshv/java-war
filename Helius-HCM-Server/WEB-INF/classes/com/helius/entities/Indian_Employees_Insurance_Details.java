package com.helius.entities;


//import java.sql.Date;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.print.attribute.standard.DateTimeAtCreation;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name="Indian_Employees_Insurance_Details")
@Audited
public class Indian_Employees_Insurance_Details {

	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int indian_employees_insurance_details_id;
	@Column
	private String employee_id;
	@Column
	private int sum_insured_medical;
	@Column
	private int sum_insured_accidental;
	@Column
	private Timestamp effective_from;
	@Column
	private Timestamp policy_issued_on;
	@Column
	private String policy_number;
	public String getPolicy_number() {
		return policy_number;
	}
	public void setPolicy_number(String policy_number) {
		this.policy_number = policy_number;
	}
	public String getPayable_by() {
		return payable_by;
	}
	public void setPayable_by(String payable_by) {
		this.payable_by = payable_by;
	}
	@Column
	private String payable_by;
	@Column
	@NotAudited
	private String last_modified_by;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	@Column
	private String created_by;
	
	
	
	
	public Timestamp getEffective_from() {
		return effective_from;
	}
	public void setEffective_from(Timestamp effective_from) {
		this.effective_from = effective_from;
	}
	public Timestamp getPolicy_issued_on() {
		return policy_issued_on;
	}
	public void setPolicy_issued_on(Timestamp policy_issued_on) {
		this.policy_issued_on = policy_issued_on;
	}
	public Timestamp getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}
	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}
	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	@OneToMany(targetEntity=Indian_Employee_Family_Member.class, mappedBy="employee_id", fetch=FetchType.EAGER)
	private List<Indian_Employee_Family_Member> indianEmployeeFamilyMember;

	public List<Indian_Employee_Family_Member> getIndianEmployeeFamilyMember() {
		return indianEmployeeFamilyMember;
	}
	public void setIndianEmployeeFamilyMember(List<Indian_Employee_Family_Member> indianEmployeeFamilyMember) {
		this.indianEmployeeFamilyMember = indianEmployeeFamilyMember;
	}
	public int getIndian_employees_insurance_details_id() {
		return indian_employees_insurance_details_id;
	}
	public void setIndian_employees_insurance_details_id(int indian_employees_insurance_details_id) {
		this.indian_employees_insurance_details_id = indian_employees_insurance_details_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public int getSum_insured_medical() {
		return sum_insured_medical;
	}
	public void setSum_insured_medical(int sum_insured_medical) {
		this.sum_insured_medical = sum_insured_medical;
	}
	
	public int getSum_insured_accidental() {
		return sum_insured_accidental;
	}
	public void setSum_insured_accidental(int sum_insured_accidental) {
		this.sum_insured_accidental = sum_insured_accidental;
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
	
	
}
