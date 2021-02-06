package com.helius.utils;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the client_group_details database table.
 * 
 */
@Entity
@Table(name="client_group_details")
@NamedQuery(name="ClientGroupDetail.findAll", query="SELECT c FROM ClientGroupDetail c")
public class ClientGroupDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_group_details_id")
	private int clientGroupDetailsId=0;

	@Column(name="client_group")
	private String clientGroup;

	@Temporal(TemporalType.DATE)
	@Column(name="client_group_agreement_expires_on")
	private Date clientGroupAgreementExpiresOn;

	@Column(name="client_group_agreement_reference")
	private String clientGroupAgreementReference;

	@Column(name="client_group_timesheet")
	private String clientGroupTimesheet;

	@Column(name="client_name")
	private String clientName;

	@Column(name="create_date")
	private Timestamp createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="timesheet_approver")
	private String timesheetApprover;

	@Column(name="upload_group_agreement")
	private String uploadGroupAgreement;
	
	@Column(name="line_of_business")
	private String lobList;
	
	@Column
	private String lob;

	//bi-directional many-to-one association to ClientDetail
	@ManyToOne
	@JoinColumn(name="client_id")
	private ClientDetail clientDetail;

	//bi-directional one-to-one association to ClientGroupLeavePolicy
	@OneToMany
	@JoinColumn(name="client_group_details_id", referencedColumnName="client_group_details_id")
	private List<ClientGroupLeavePolicy> clientGroupLeavePolicy;

	//bi-directional one-to-one association to ClientGroupReimbursementPolicy
	@OneToOne
	@JoinColumn(name="client_group_details_id")
	private ClientGroupReimbursementPolicy clientGroupReimbursementPolicy;

	//bi-directional many-to-one association to ClientGroupReimbursementCategory
	@OneToMany(mappedBy="clientGroupDetail")
	private List<ClientGroupReimbursementCategory> clientGroupReimbursementCategories;

	public ClientGroupDetail() {
	}

	public int getClientGroupDetailsId() {
		return this.clientGroupDetailsId;
	}

	public void setClientGroupDetailsId(int clientGroupDetailsId) {
		this.clientGroupDetailsId = clientGroupDetailsId;
	}

	public String getClientGroup() {
		return this.clientGroup;
	}

	public void setClientGroup(String clientGroup) {
		this.clientGroup = clientGroup;
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

	

	public String getLobList() {
		return lobList;
	}

	public void setLobList(String lobList) {
		this.lobList = lobList;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public void setClientGroupAgreementReference(String clientGroupAgreementReference) {
		this.clientGroupAgreementReference = clientGroupAgreementReference;
	}

	public String getClientGroupTimesheet() {
		return this.clientGroupTimesheet;
	}

	public void setClientGroupTimesheet(String clientGroupTimesheet) {
		this.clientGroupTimesheet = clientGroupTimesheet;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public String getTimesheetApprover() {
		return this.timesheetApprover;
	}

	public void setTimesheetApprover(String timesheetApprover) {
		this.timesheetApprover = timesheetApprover;
	}

	public String getUploadGroupAgreement() {
		return this.uploadGroupAgreement;
	}

	public void setUploadGroupAgreement(String uploadGroupAgreement) {
		this.uploadGroupAgreement = uploadGroupAgreement;
	}

	public ClientDetail getClientDetail() {
		return this.clientDetail;
	}

	public void setClientDetail(ClientDetail clientDetail) {
		this.clientDetail = clientDetail;
	}

	public List<ClientGroupLeavePolicy> getClientGroupLeavePolicy() {
		return this.clientGroupLeavePolicy;
	}

	public void setClientGroupLeavePolicy(List<ClientGroupLeavePolicy> clientGroupLeavePolicy) {
		this.clientGroupLeavePolicy = clientGroupLeavePolicy;
	}

	public ClientGroupReimbursementPolicy getClientGroupReimbursementPolicy() {
		return this.clientGroupReimbursementPolicy;
	}

	public void setClientGroupReimbursementPolicy(ClientGroupReimbursementPolicy clientGroupReimbursementPolicy) {
		this.clientGroupReimbursementPolicy = clientGroupReimbursementPolicy;
	}

	public List<ClientGroupReimbursementCategory> getClientGroupReimbursementCategories() {
		return this.clientGroupReimbursementCategories;
	}

	public void setClientGroupReimbursementCategories(List<ClientGroupReimbursementCategory> clientGroupReimbursementCategories) {
		this.clientGroupReimbursementCategories = clientGroupReimbursementCategories;
	}

	
}