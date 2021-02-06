package com.helius.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name="Singapore_Employee_Insurance_Details")
@Audited
public class Singapore_Employee_Insurance_Details {

	@Id
	@Column
	private int singapore_employee_insurance_details_id;
	@Column
	private String employee_id;
	@Column
	private String sum_insured;
	@Column
	private Timestamp policy_issued_on;
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
	private Timestamp effective_from;
	@Column
	private String policy_number;
	@Column
	private String payable_by;
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
	
	@OneToMany(targetEntity=Singapore_Employee_Family_Member.class, mappedBy="employee_id", fetch=FetchType.EAGER)
	private List<Singapore_Employee_Family_Member> singaporeEmployeeFamilyMember;
	
	
	
	public Timestamp getPolicy_issued_on() {
		return policy_issued_on;
	}
	public void setPolicy_issued_on(Timestamp policy_issued_on) {
		this.policy_issued_on = policy_issued_on;
	}
	public Timestamp getEffective_from() {
		return effective_from;
	}
	public void setEffective_from(Timestamp effective_from) {
		this.effective_from = effective_from;
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
	public List<Singapore_Employee_Family_Member> getSingaporeEmployeeFamilyMember() {
		return singaporeEmployeeFamilyMember;
	}
	public void setSingaporeEmployeeFamilyMember(List<Singapore_Employee_Family_Member> singaporeEmployeeFamilyMember) {
		this.singaporeEmployeeFamilyMember = singaporeEmployeeFamilyMember;
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
	public int getSingapore_employee_insurance_details_id() {
		return singapore_employee_insurance_details_id;
	}
	public void setSingapore_employee_insurance_details_id(int singapore_employee_insurance_details_id) {
		this.singapore_employee_insurance_details_id = singapore_employee_insurance_details_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getSum_insured() {
		return sum_insured;
	}
	public void setSum_insured(String sum_insured) {
		this.sum_insured = sum_insured;
	}
	
}
