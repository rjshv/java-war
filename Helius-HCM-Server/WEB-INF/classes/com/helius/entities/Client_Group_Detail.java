package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Client_Group_Details database table.
 * 
 */
@Entity
@Table(name="Client_Group_Details")
@NamedQuery(name="Client_Group_Detail.findAll", query="SELECT c FROM Client_Group_Detail c")
public class Client_Group_Detail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_group_details_id")
	private int clientGroupDetailsId;

	@Column(name="budget_owner")
	private String budgetOwner;
	
	@Column(name="client_group")
	private String clientGroup;

	

	@Temporal(TemporalType.DATE)
	@Column(name="client_group_agreement_expires_on")
	private Date clientGroupAgreementExpiresOn;

	@Column(name="client_group_agreement_reference")
	private String clientGroupAgreementReference;

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

	@Column(name="timesheet_approver")
	private String timesheetApprover;

	@Column(name="timesheet_due_on")
	private int timesheetDueOn;

	@Column(name="type_of_client_group_timesheet")
	private String typeOfClientGroupTimesheet;

	@Column(name="upload_group_agreement")
	private String uploadGroupAgreement;

	//bi-directional many-to-one association to Client_Sub_Group_Detail
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinColumn(name="client_group")
	private List<Client_Sub_Group_Detail> clientSubGroupDetails;

	public Client_Group_Detail() {
	}

	public int getClientGroupDetailsId() {
		return this.clientGroupDetailsId;
	}

	public void setClientGroupDetailsId(int clientGroupDetailsId) {
		this.clientGroupDetailsId = clientGroupDetailsId;
	}

	public String getBudgetOwner() {
		return this.budgetOwner;
	}

	public void setBudgetOwner(String budgetOwner) {
		this.budgetOwner = budgetOwner;
	}

	public Date getClientGroupAgreementExpiresOn() {
		return this.clientGroupAgreementExpiresOn;
	}

	public void setClientGroupAgreementExpiresOn(Date clientGroupAgreementExpiresOn) {
		this.clientGroupAgreementExpiresOn = clientGroupAgreementExpiresOn;
	}

	public String getClientGroupAgreementReference() {
		return this.clientGroupAgreementReference;
	}

	public void setClientGroupAgreementReference(String clientGroupAgreementReference) {
		this.clientGroupAgreementReference = clientGroupAgreementReference;
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

	public String getTimesheetApprover() {
		return this.timesheetApprover;
	}

	public void setTimesheetApprover(String timesheetApprover) {
		this.timesheetApprover = timesheetApprover;
	}

	public int getTimesheetDueOn() {
		return this.timesheetDueOn;
	}

	public void setTimesheetDueOn(int timesheetDueOn) {
		this.timesheetDueOn = timesheetDueOn;
	}

	public String getTypeOfClientGroupTimesheet() {
		return this.typeOfClientGroupTimesheet;
	}

	public void setTypeOfClientGroupTimesheet(String typeOfClientGroupTimesheet) {
		this.typeOfClientGroupTimesheet = typeOfClientGroupTimesheet;
	}

	public String getUploadGroupAgreement() {
		return this.uploadGroupAgreement;
	}

	public void setUploadGroupAgreement(String uploadGroupAgreement) {
		this.uploadGroupAgreement = uploadGroupAgreement;
	}

	

	public List<Client_Sub_Group_Detail> getClientSubGroupDetails() {
		return this.clientSubGroupDetails;
	}

	public void setClientSubGroupDetails(List<Client_Sub_Group_Detail> clientSubGroupDetails) {
		this.clientSubGroupDetails = clientSubGroupDetails;
	}

	public Client_Sub_Group_Detail addClientSubGroupDetail(Client_Sub_Group_Detail clientSubGroupDetail) {
		getClientSubGroupDetails().add(clientSubGroupDetail);
		clientSubGroupDetail.setClientGroupDetail(this);

		return clientSubGroupDetail;
	}

	public Client_Sub_Group_Detail removeClientSubGroupDetail(Client_Sub_Group_Detail clientSubGroupDetail) {
		getClientSubGroupDetails().remove(clientSubGroupDetail);
		clientSubGroupDetail.setClientGroupDetail(null);

		return clientSubGroupDetail;
	}
	/**
	 * @return the clientGroup
	 */
	public String getClientGroup() {
		return clientGroup;
	}

	/**
	 * @param clientGroup the clientGroup to set
	 */
	public void setClientGroup(String clientGroup) {
		this.clientGroup = clientGroup;
	}

}