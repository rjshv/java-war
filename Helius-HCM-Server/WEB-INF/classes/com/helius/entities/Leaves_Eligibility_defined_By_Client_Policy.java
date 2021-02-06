package com.helius.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Leaves_Eligibility_defined_By_Client_Policy")
public class Leaves_Eligibility_defined_By_Client_Policy {

	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int leaves_eligibility_id;
	@Column
	private String employee_id;
	@Column
	private String annual_leave;
	@Column
	private String medical_leave;
	@Column
	private String casual_leave;
	@Column
	private String maternity_leave;
	@Column
	private String paternity_leave;
	@Column
	private String compensatory_off_leave_allowed;
	@Column
	private String leave_encashments;
	public int getLeaves_eligibility_id() {
		return leaves_eligibility_id;
	}
	public void setLeaves_eligibility_id(int leaves_eligibility_id) {
		this.leaves_eligibility_id = leaves_eligibility_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getAnnual_leave() {
		return annual_leave;
	}
	public void setAnnual_leave(String annual_leave) {
		this.annual_leave = annual_leave;
	}
	public String getMedical_leave() {
		return medical_leave;
	}
	public void setMedical_leave(String medical_leave) {
		this.medical_leave = medical_leave;
	}
	public String getCasual_leave() {
		return casual_leave;
	}
	public void setCasual_leave(String casual_leave) {
		this.casual_leave = casual_leave;
	}
	public String getMaternity_leave() {
		return maternity_leave;
	}
	public void setMaternity_leave(String maternity_leave) {
		this.maternity_leave = maternity_leave;
	}
	public String getPaternity_leave() {
		return paternity_leave;
	}
	public void setPaternity_leave(String paternity_leave) {
		this.paternity_leave = paternity_leave;
	}
	public String getCompensatory_off_leave_allowed() {
		return compensatory_off_leave_allowed;
	}
	public void setCompensatory_off_leave_allowed(String compensatory_off_leave_allowed) {
		this.compensatory_off_leave_allowed = compensatory_off_leave_allowed;
	}
	public String getLeave_encashments() {
		return leave_encashments;
	}
	public void setLeave_encashments(String leave_encashments) {
		this.leave_encashments = leave_encashments;
	}
	
	
}
