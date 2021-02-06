package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Client_Reimbursement_Policy database table.
 * 
 */
@Entity
@NamedQuery(name="Client_Reimbursement_Policy.findAll", query="SELECT c FROM Client_Reimbursement_Policy c")
public class Client_Reimbursement_Policy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_reimbursement_policy_id")
	private int clientReimbursementPolicyId;

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

	@Column(name="same_as_helius_policy")
	private String sameAsHeliusPolicy;


	public Client_Reimbursement_Policy() {
	}

	public int getClientReimbursementPolicyId() {
		return this.clientReimbursementPolicyId;
	}

	public void setClientReimbursementPolicyId(int clientReimbursementPolicyId) {
		this.clientReimbursementPolicyId = clientReimbursementPolicyId;
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

	public String getSameAsHeliusPolicy() {
		return this.sameAsHeliusPolicy;
	}

	public void setSameAsHeliusPolicy(String sameAsHeliusPolicy) {
		this.sameAsHeliusPolicy = sameAsHeliusPolicy;
	}


}