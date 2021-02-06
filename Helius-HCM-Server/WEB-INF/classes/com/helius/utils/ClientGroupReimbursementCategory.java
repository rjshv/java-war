package com.helius.utils;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the client_group_reimbursement_categories database table.
 * 
 */
@Entity
@Table(name="client_group_reimbursement_categories")
@NamedQuery(name="ClientGroupReimbursementCategory.findAll", query="SELECT c FROM ClientGroupReimbursementCategory c")
public class ClientGroupReimbursementCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_group_reimbursement_category_id")
	private int clientGroupReimbursementCategoryId=0;

	private String allowed;

	@Column(name="create_date")
	private Timestamp createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="limit_amount")
	private int limitAmount;

	@Column(name="requires_supporting_proofs")
	private String requiresSupportingProofs;

	private String type;

	@Column(name="client_group_details_id")
	private int clientGroupGetailsId;

	/**
	 * @return the clientGroupGetailsId
	 */
	public int getClientGroupGetailsId() {
		return clientGroupGetailsId;
	}

	/**
	 * @param clientGroupGetailsId the clientGroupGetailsId to set
	 */
	public void setClientGroupGetailsId(int clientGroupGetailsId) {
		this.clientGroupGetailsId = clientGroupGetailsId;
	}

	public ClientGroupReimbursementCategory() {
	}

	public int getClientGroupReimbursementCategoryId() {
		return this.clientGroupReimbursementCategoryId;
	}

	public void setClientGroupReimbursementCategoryId(int clientGroupReimbursementCategoryId) {
		this.clientGroupReimbursementCategoryId = clientGroupReimbursementCategoryId;
	}

	public String getAllowed() {
		return this.allowed;
	}

	public void setAllowed(String allowed) {
		this.allowed = allowed;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
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

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

}