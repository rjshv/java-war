package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the client_details database table.
 * 
 */


@Entity
@Audited
@Table(name="client_details")
@NamedNativeQuery(name="findClientByName", query="select * from client_details c where c.client_name=:client_name")
public class ClientDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="client_id")
	private int clientId;

	@Column(name="applicable_sow_type")
	private String applicableSowType;

	@Column(name="client_country")
	private String clientCountry;

	@Temporal(TemporalType.DATE)
	@Column(name="client_master_agreement_expires_on")
	private Date clientMasterAgreementExpiresOn;

	@Column(name="client_master_agreement_reference")
	private String clientMasterAgreementReference;

	@Column(name="client_name")
	private String clientName;

	@Column(name="client_short_name")
	private String clientShortName;

	@Column(name="client_timesheet")
	private String clientTimesheet;

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

	@Column(name="timesheet_approver")
	private String timesheetApprover;

	@Column(name="upload_client_logo_path")
	private String uploadClientLogoPath;

	@Column(name="upload_master_agreement_path")
	private String uploadMasterAgreementPath;
	
	
	@Column(name="isheliusclient")
	private String isheliusclient;
	
	@Column(name="timesheetApprovers_all")
	private String timesheetApproversAll;

	
	@Column(name="budgetowner")
	private String budgetowner;
	/**
	 * @return the budgetowner
	 */
	public String getBudgetowner() {
		return budgetowner;
	}

	/**
	 * @param budgetowner the budgetowner to set
	 */
	public void setBudgetowner(String budgetowner) {
		this.budgetowner = budgetowner;
	}

	/**
	 * @return the budgetownerList
	 */
	public String getBudgetownerList() {
		return budgetownerList;
	}

	/**
	 * @param budgetownerList the budgetownerList to set
	 */
	public void setBudgetownerList(String budgetownerList) {
		this.budgetownerList = budgetownerList;
	}
	@Column(name="budgetownerList")
	private String budgetownerList;
	/**
	 * @return the timesheetApproversAll
	 */
	public String getTimesheetApproversAll() {
		return timesheetApproversAll;
	}

	/**
	 * @param timesheetApproversAll the timesheetApproversAll to set
	 */
	public void setTimesheetApproversAll(String timesheetApproversAll) {
		this.timesheetApproversAll = timesheetApproversAll;
	}

	public ClientDetail() {
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

	public String getClientCountry() {
		return this.clientCountry;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
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

	public String getClientTimesheet() {
		return this.clientTimesheet;
	}

	public void setClientTimesheet(String clientTimesheet) {
		this.clientTimesheet = clientTimesheet;
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
	
	/**
	 * @return the isheliusclient
	 */
	public String getIsheliusclient() {
		return isheliusclient;
	}

	/**
	 * @param isheliusclient the isheliusclient to set
	 */
	public void setIsheliusclient(String isheliusclient) {
		this.isheliusclient = isheliusclient;
	}

	
}