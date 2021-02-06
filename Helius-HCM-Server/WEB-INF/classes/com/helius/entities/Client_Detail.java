package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Cascade;
import  org.hibernate.annotations.CascadeType;


/**
 * The persistent class for the Client_Details database table.
 * 
 */
@Entity
@Table(name="Client_Details")
@NamedQuery(name="Client_Detail.findAll", query="SELECT c FROM Client_Detail c")
public class Client_Detail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_id")
	private int clientId;

	@Column(name="applicable_sow_type")
	private String applicableSowType;

	@Column(name="client_location")
	private String clientLocation;

	@Temporal(TemporalType.DATE)
	@Column(name="client_master_agreement_expires_on")
	private Date clientMasterAgreementExpiresOn;

	@Column(name="client_master_agreement_reference")
	private String clientMasterAgreementReference;

	@Column(name="client_name")
	private String clientName;

	@Column(name="client_short_name")
	private String clientShortName;

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

	@Column(name="upload_client_logo_path")
	private String uploadClientLogoPath;

	@Column(name="upload_master_agreement_path")
	private String uploadMasterAgreementPath;

	//bi-directional one-to-one association to Client_Billing_Address
	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="client_id", referencedColumnName="client_id")
	private Client_Billing_Address clientBillingAddress;

	

	/**
	 * @return the clientBillingAddress
	 */
	public Client_Billing_Address getClientBillingAddress() {
		return clientBillingAddress;
	}

	/**
	 * @param clientBillingAddress the clientBillingAddress to set
	 */
	public void setClientBillingAddress(Client_Billing_Address clientBillingAddress) {
		this.clientBillingAddress = clientBillingAddress;
	}

	//bi-directional many-to-one association to Client_Group_Detail
	@OneToMany
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="client_id", referencedColumnName="client_id")
	private List<Client_Group_Detail> clientGroupDetails;

	//bi-directional one-to-one association to Client_Invoicing
	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="client_id", referencedColumnName="client_id")
	private Client_Invoicing clientInvoicing;

	//bi-directional one-to-one association to Client_Leave_Policy
	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="client_id", referencedColumnName="client_id")
	private Client_Leave_Policy clientLeavePolicy;

	//bi-directional one-to-one association to Client_Reimbursement_Policy
	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="client_id", referencedColumnName="client_id")
	private Client_Reimbursement_Policy clientReimbursementPolicy;

	//bi-directional many-to-one association to Client_Reimbursement_Category
	@OneToMany
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="client_id", referencedColumnName="client_id")
	private List<Client_Reimbursement_Category> clientReimbursementCategories;

	public Client_Detail() {
	}

	public int getClientId() {
		return this.clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getApplicableSowType() {
		return this.applicableSowType;
	}

	public void setApplicableSowType(String applicableSowType) {
		this.applicableSowType = applicableSowType;
	}

	public String getClientLocation() {
		return this.clientLocation;
	}

	public void setClientLocation(String clientLocation) {
		this.clientLocation = clientLocation;
	}

	public Date getClientMasterAgreementExpiresOn() {
		return this.clientMasterAgreementExpiresOn;
	}

	public void setClientMasterAgreementExpiresOn(Date clientMasterAgreementExpiresOn) {
		this.clientMasterAgreementExpiresOn = clientMasterAgreementExpiresOn;
	}

	public String getClientMasterAgreementReference() {
		return this.clientMasterAgreementReference;
	}

	public void setClientMasterAgreementReference(String clientMasterAgreementReference) {
		this.clientMasterAgreementReference = clientMasterAgreementReference;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientShortName() {
		return this.clientShortName;
	}

	public void setClientShortName(String clientShortName) {
		this.clientShortName = clientShortName;
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

	public String getUploadClientLogoPath() {
		return this.uploadClientLogoPath;
	}

	public void setUploadClientLogoPath(String uploadClientLogoPath) {
		this.uploadClientLogoPath = uploadClientLogoPath;
	}

	public String getUploadMasterAgreementPath() {
		return this.uploadMasterAgreementPath;
	}

	public void setUploadMasterAgreementPath(String uploadMasterAgreementPath) {
		this.uploadMasterAgreementPath = uploadMasterAgreementPath;
	}

	

	public List<Client_Group_Detail> getClientGroupDetails() {
		return this.clientGroupDetails;
	}

	public void setClientGroupDetails(List<Client_Group_Detail> clientGroupDetails) {
		this.clientGroupDetails = clientGroupDetails;
	}

	public Client_Group_Detail addClientGroupDetail(Client_Group_Detail clientGroupDetail) {
		getClientGroupDetails().add(clientGroupDetail);
		return clientGroupDetail;
	}

	public Client_Group_Detail removeClientGroupDetail(Client_Group_Detail clientGroupDetail) {
		getClientGroupDetails().remove(clientGroupDetail);
	

		return clientGroupDetail;
	}

	public Client_Invoicing getClientInvoicing() {
		return this.clientInvoicing;
	}

	public void setClientInvoicing(Client_Invoicing clientInvoicing) {
		this.clientInvoicing = clientInvoicing;
	}

	public Client_Leave_Policy getClientLeavePolicy() {
		return this.clientLeavePolicy;
	}

	public void setClientLeavePolicy(Client_Leave_Policy clientLeavePolicy) {
		this.clientLeavePolicy = clientLeavePolicy;
	}

	public Client_Reimbursement_Policy getClientReimbursementPolicy() {
		return this.clientReimbursementPolicy;
	}

	public void setClientReimbursementPolicy(Client_Reimbursement_Policy clientReimbursementPolicy) {
		this.clientReimbursementPolicy = clientReimbursementPolicy;
	}

	public List<Client_Reimbursement_Category> getClientReimbursementCategories() {
		return this.clientReimbursementCategories;
	}

	public void setClientReimbursementCategories(List<Client_Reimbursement_Category> clientReimbursementCategories) {
		this.clientReimbursementCategories = clientReimbursementCategories;
	}

	public Client_Reimbursement_Category addClientReimbursementCategory(Client_Reimbursement_Category clientReimbursementCategory) {
		getClientReimbursementCategories().add(clientReimbursementCategory);
		
		return clientReimbursementCategory;
	}

	public Client_Reimbursement_Category removeClientReimbursementCategory(Client_Reimbursement_Category clientReimbursementCategory) {
		getClientReimbursementCategories().remove(clientReimbursementCategory);
		

		return clientReimbursementCategory;
	}

}