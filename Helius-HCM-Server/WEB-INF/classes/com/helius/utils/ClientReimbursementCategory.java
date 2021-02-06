package com.helius.utils;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the client_reimbursement_categories database table.
 * 
 */
@Entity
@Table(name="client_reimbursement_categories")
@NamedQuery(name="ClientReimbursementCategory.findAll", query="SELECT c FROM ClientReimbursementCategory c")
public class ClientReimbursementCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_reimbursement_category_id")
	private int clientReimbursementCategoryId=0;

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

	@Column(name="client_id")
	private int clientId;
	/**
	 * @return the clientid
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * @param clientid the clientid to set
	 */
	public void setClientId(int clientid) {
		this.clientId = clientid;
	}
	public ClientReimbursementCategory() {
	}

	public int getClientReimbursementCategoryId() {
		return this.clientReimbursementCategoryId;
	}

	public void setClientReimbursementCategoryId(int clientReimbursementCategoryId) {
		this.clientReimbursementCategoryId = clientReimbursementCategoryId;
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