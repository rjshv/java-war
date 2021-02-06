package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.sql.Timestamp;


/**
 * The persistent class for the client_reimbursement_policy database table.
 * 
 */
@Entity
@Audited
@Table(name="client_reimbursement_policy")
@NamedQuery(name="ClientReimbursementPolicy.findAll", query="SELECT c FROM ClientReimbursementPolicy c")
public class ClientReimbursementPolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_reimbursement_policyid")
	private int clientReimbursementPolicyid;

	@Column(name="create_date")
	@CreationTimestamp
	private Timestamp createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="last_modified_by")
	@NotAudited
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	@UpdateTimestamp
	@NotAudited
	private Timestamp lastModifiedDate;

	@Column(name="same_as_helius_policy")
	private String sameAsHeliusPolicy;

	
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
	public ClientReimbursementPolicy() {
	}

	public int getClientReimbursementPolicyid() {
		return this.clientReimbursementPolicyid;
	}

	public void setClientReimbursementPolicyid(int clientReimbursementPolicyid) {
		this.clientReimbursementPolicyid = clientReimbursementPolicyid;
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

	public String getSameAsHeliusPolicy() {
		return this.sameAsHeliusPolicy;
	}

	public void setSameAsHeliusPolicy(String sameAsHeliusPolicy) {
		this.sameAsHeliusPolicy = sameAsHeliusPolicy;
	}

	
}