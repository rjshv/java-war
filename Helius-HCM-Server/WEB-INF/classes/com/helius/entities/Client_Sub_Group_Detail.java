package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Client_Sub_Group_Details database table.
 * 
 */
@Entity
@Table(name="Client_Sub_Group_Details")
@NamedQuery(name="Client_Sub_Group_Detail.findAll", query="SELECT c FROM Client_Sub_Group_Detail c")
public class Client_Sub_Group_Detail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_sub_group_details_id")
	private int clientSubGroupDetailsId;

	@Column(name="budget_owner")
	private String budgetOwner;

	@Column(name="client_id")
	private int clientId;

	@Column(name="client_sub_group_agreement_expires_on")
	private String clientSubGroupAgreementExpiresOn;

	@Column(name="client_sub_group_agreement_reference")
	private String clientSubGroupAgreementReference;

	@Column(name="client_sub_group_name")
	private String clientSubGroupName;

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

	@Temporal(TemporalType.DATE)
	@Column(name="timesheet_due_on")
	private Date timesheetDueOn;

	@Column(name="type_of_client_sub_group_timesheet")
	private String typeOfClientSubGroupTimesheet;

	@Column(name="Upload_sub_group_agreement")
	private String upload_sub_group_agreement;

	//bi-directional many-to-one association to Client_Group_Detail
	@ManyToOne
	@JoinColumn(name="client_group", referencedColumnName="client_group")
	private Client_Group_Detail clientGroupDetail;

	public Client_Sub_Group_Detail() {
	}

	public int getClientSubGroupDetailsId() {
		return this.clientSubGroupDetailsId;
	}

	public void setClientSubGroupDetailsId(int clientSubGroupDetailsId) {
		this.clientSubGroupDetailsId = clientSubGroupDetailsId;
	}

	public String getBudgetOwner() {
		return this.budgetOwner;
	}

	public void setBudgetOwner(String budgetOwner) {
		this.budgetOwner = budgetOwner;
	}

	public int getClientId() {
		return this.clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientSubGroupAgreementExpiresOn() {
		return this.clientSubGroupAgreementExpiresOn;
	}

	public void setClientSubGroupAgreementExpiresOn(String clientSubGroupAgreementExpiresOn) {
		this.clientSubGroupAgreementExpiresOn = clientSubGroupAgreementExpiresOn;
	}

	public String getClientSubGroupAgreementReference() {
		return this.clientSubGroupAgreementReference;
	}

	public void setClientSubGroupAgreementReference(String clientSubGroupAgreementReference) {
		this.clientSubGroupAgreementReference = clientSubGroupAgreementReference;
	}

	public String getClientSubGroupName() {
		return this.clientSubGroupName;
	}

	public void setClientSubGroupName(String clientSubGroupName) {
		this.clientSubGroupName = clientSubGroupName;
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

	public Date getTimesheetDueOn() {
		return this.timesheetDueOn;
	}

	public void setTimesheetDueOn(Date timesheetDueOn) {
		this.timesheetDueOn = timesheetDueOn;
	}

	public String getTypeOfClientSubGroupTimesheet() {
		return this.typeOfClientSubGroupTimesheet;
	}

	public void setTypeOfClientSubGroupTimesheet(String typeOfClientSubGroupTimesheet) {
		this.typeOfClientSubGroupTimesheet = typeOfClientSubGroupTimesheet;
	}

	public String getUpload_sub_group_agreement() {
		return this.upload_sub_group_agreement;
	}

	public void setUpload_sub_group_agreement(String upload_sub_group_agreement) {
		this.upload_sub_group_agreement = upload_sub_group_agreement;
	}

	public Client_Group_Detail getClientGroupDetail() {
		return this.clientGroupDetail;
	}

	public void setClientGroupDetail(Client_Group_Detail clientGroupDetail) {
		this.clientGroupDetail = clientGroupDetail;
	}

}