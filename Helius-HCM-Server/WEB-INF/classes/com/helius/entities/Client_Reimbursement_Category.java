package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Client_Reimbursement_Categories database table.
 * 
 */
@Entity
@Table(name="Client_Reimbursement_Categories")
@NamedQuery(name="Client_Reimbursement_Category.findAll", query="SELECT c FROM Client_Reimbursement_Category c")
public class Client_Reimbursement_Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_reimbursement_category_id")
	private int clientReimbursemenCategoryId;
	
	/**
	 * @return the clientReimbursemenCategoryId
	 */
	public int getClientReimbursemenCategoryId() {
		return clientReimbursemenCategoryId;
	}

	/**
	 * @param clientReimbursemenCategoryId the clientReimbursemenCategoryId to set
	 */
	public void setClientReimbursemenCategoryId(int clientReimbursemenCategoryId) {
		this.clientReimbursemenCategoryId = clientReimbursemenCategoryId;
	}

	

	private String allowed;

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

	@Column(name="client_limit")
	private String limit;

	@Column(name="limit_amount")
	private int limitAmount;

	@Column(name="requires_supporting_proofs")
	private String requiresSupportingProofs;

	@Column(name="timesheet_approver_limit")
	private String timesheetApproverLimit;
	
	
	@ManyToOne
	@JoinColumn(name="client_id", insertable=false, updatable=false )
	private Client_Detail clientDetail;

	private String type;

	

	public Client_Reimbursement_Category() {
	}

	

	public String getAllowed() {
		return this.allowed;
	}

	public void setAllowed(String allowed) {
		this.allowed = allowed;
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

	public String getLimit() {
		return this.limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public int getLimitAmount() {
		return this.limitAmount;
	}

	public void setLimitAmount(int limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getRequiresSupportingProofs() {
		return this.requiresSupportingProofs;
	}

	public void setRequiresSupportingProofs(String requiresSupportingProofs) {
		this.requiresSupportingProofs = requiresSupportingProofs;
	}

	public String getTimesheetApproverLimit() {
		return this.timesheetApproverLimit;
	}

	public void setTimesheetApproverLimit(String timesheetApproverLimit) {
		this.timesheetApproverLimit = timesheetApproverLimit;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	
}