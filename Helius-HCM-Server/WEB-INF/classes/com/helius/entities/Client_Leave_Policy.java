package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Client_Leave_Policy database table.
 * 
 */
@Entity
@NamedQuery(name="Client_Leave_Policy.findAll", query="SELECT c FROM Client_Leave_Policy c")
public class Client_Leave_Policy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_leave_policy_id")
	private int clientLeavePolicyId;

	@Column(name="annual_leave")
	private int annualLeave;

	@Column(name="casual_leave")
	private int casualLeave;

	@Column(name="compensatory_off_leave_allowed")
	private String compensatoryOffLeaveAllowed;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_modified_date")
	private Date lastModifiedDate;

	@Column(name="leave_encashments")
	private int leaveEncashments;

	@Column(name="maternity_leave")
	private int maternityLeave;

	@Column(name="medical_leave")
	private int medicalLeave;

	@Column(name="paternity_leave")
	private int paternityLeave;

	@Column(name="same_as_helius_policy")
	private String sameAsHeliusPolicy;

	

	public int getClientLeavePolicyId() {
		return this.clientLeavePolicyId;
	}

	public void setClientLeavePolicyId(int clientLeavePolicyId) {
		this.clientLeavePolicyId = clientLeavePolicyId;
	}

	public int getAnnualLeave() {
		return this.annualLeave;
	}

	public void setAnnualLeave(int annualLeave) {
		this.annualLeave = annualLeave;
	}

	public int getCasualLeave() {
		return this.casualLeave;
	}

	public void setCasualLeave(int casualLeave) {
		this.casualLeave = casualLeave;
	}

	public String getCompensatoryOffLeaveAllowed() {
		return this.compensatoryOffLeaveAllowed;
	}

	public void setCompensatoryOffLeaveAllowed(String compensatoryOffLeaveAllowed) {
		this.compensatoryOffLeaveAllowed = compensatoryOffLeaveAllowed;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public int getLeaveEncashments() {
		return this.leaveEncashments;
	}

	public void setLeaveEncashments(int leaveEncashments) {
		this.leaveEncashments = leaveEncashments;
	}

	public int getMaternityLeave() {
		return this.maternityLeave;
	}

	public void setMaternityLeave(int maternityLeave) {
		this.maternityLeave = maternityLeave;
	}

	public int getMedicalLeave() {
		return this.medicalLeave;
	}

	public void setMedicalLeave(int medicalLeave) {
		this.medicalLeave = medicalLeave;
	}

	public int getPaternityLeave() {
		return this.paternityLeave;
	}

	public void setPaternityLeave(int paternityLeave) {
		this.paternityLeave = paternityLeave;
	}

	public String getSameAsHeliusPolicy() {
		return this.sameAsHeliusPolicy;
	}

	public void setSameAsHeliusPolicy(String sameAsHeliusPolicy) {
		this.sameAsHeliusPolicy = sameAsHeliusPolicy;
	}

	
}