package com.helius.utils;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the client_group_reimbursement_policy database table.
 * 
 */
@Entity
@Table(name="client_group_reimbursement_policy")
@NamedQuery(name="ClientGroupReimbursementPolicy.findAll", query="SELECT c FROM ClientGroupReimbursementPolicy c")
public class ClientGroupReimbursementPolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)

	@Column(name="client_group_reimbursement_policyid")
	private int clientGroupReimbursementPolicyid=0;
	
	/**
	 * @return the clientGroupReimbursementPolicyid
	 */
	public int getClientGroupReimbursementPolicyid() {
		return clientGroupReimbursementPolicyid;
	}

	/**
	 * @param clientGroupReimbursementPolicyid the clientGroupReimbursementPolicyid to set
	 */
	public void setClientGroupReimbursementPolicyid(int clientGroupReimbursementPolicyid) {
		this.clientGroupReimbursementPolicyid = clientGroupReimbursementPolicyid;
	}


	@Column(name="client_group_details_id")
	private int clientGroupDetailsId;

	@Column(name="create_date")
	private Timestamp createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="sameas_client__policy")
	private String sameasClientPolicy;

	//bi-directional one-to-one association to ClientGroupDetail
	@OneToOne(mappedBy="clientGroupReimbursementPolicy")
	private ClientGroupDetail clientGroupDetail;

	public ClientGroupReimbursementPolicy() {
	}

	public int getClientGroupDetailsId() {
		return this.clientGroupDetailsId;
	}

	public void setClientGroupDetailsId(int clientGroupDetailsId) {
		this.clientGroupDetailsId = clientGroupDetailsId;
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

	public String getSameasClientPolicy() {
		return this.sameasClientPolicy;
	}

	public void setSameasClientPolicy(String sameasClientPolicy) {
		this.sameasClientPolicy = sameasClientPolicy;
	}

	public ClientGroupDetail getClientGroupDetail() {
		return this.clientGroupDetail;
	}

	public void setClientGroupDetail(ClientGroupDetail clientGroupDetail) {
		this.clientGroupDetail = clientGroupDetail;
	}

}