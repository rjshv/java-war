package com.helius.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the Sow_Details database table.
 * 
 */
@Entity
//@Table(name="Sow_Details",uniqueConstraints=@UniqueConstraint(columnNames = { "sow_details_id","helius_reference_number" }))
@Table(name="Sow_Details")
@NamedQuery(name="Sow_Details.findAll", query="SELECT s FROM Sow_Details s")
@Audited
public class Sow_Details implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="sow_details_id")
	private int sowDetailsId;

	@Column(name="assign_sow_to_employee")
	private String assignSowToEmployee;

	private String currency;

	@Column(name="employees_leaves_billable")
	private String employeesLeavesBillable;
	
	@Column(name="helius_reference_number")
	private String heliusReferenceNumber;
	
	@Column(name="po_number")
	private String poNumber;
	
	@Column(name="sow_resources")
	private String sowResources;

	@Column(name="po_end_date")
	private Timestamp poEndDate;

	@Column(name="po_start_date")
	private Timestamp poStartDate;

	@Column(name="realized_sow_value")
	private String realizedSowValue;

	@Column(name="renewal_status_description")
	private String renewalStatusDescription;

	@Column(name="sow_client_reference_number")
	private String sowClientReferenceNumber;

	@Column(name="sow_expiry_date")
	private Timestamp sowExpiryDate;

	@Column(name="sow_quantity")
	private float sowQuantity;

	@Column(name="sow_rate_for_unit")
	private String sowRateForUnit;

	@Column(name="sow_renewal_status")
	private String sowRenewalStatus;

	@Column(name="sow_start_date")
	private Timestamp sowStartDate;

	@Column(name="sow_total_value")
	private String sowTotalValue;

	@Column(name="sow_type")
	private String sowType;

	@Column(name="unrealized_sow_value")
	private String unrealizedSowValue;
	
	@Column(name="sow_path")
	private String sowPath;
	
	@Column(name="sow_initial_cost")
	private String sowInitialCost;

	@Column(name="sow_initial_cost_amount")
	private float sowInitialCostAmount;

	@Column(name="po_path")
	private String poPath;
	
	@Column(name="bonus_reimbursible")
	private String bonusReimbursible;
	
	@Column(name="bonus_reimbursible_amount")
	private float bonusReimbursibleAmount;
	
	@Column(name="bonus_frequency")
	private String bonusFrequency;
	
	@Column(name="client_contact")
	private String clientContact;

	@Column(name="client_contact_type")
	private String clientContactType;
	
	@Column(name="sow_client")
	private String sowClient;
	
	@Column(name="sow_status")
	private String sowStatus;
	
	@Column
	private String previous_sownumber;
	
	@Column
	private String sow_override;
	
	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	@Column
	@NotAudited
	private String last_modified_by;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	private String created_by;
	
	@Column(name="sow_client_group")
	private String sowClientGroup;
	
	@Column(name="helius_account_manager")
	private String heliusAccountManager;
	
	@Column(name="force_closure_reason")
	private String force_closure_reason;
	
	@Column(name="types_of_conflict")
	private String typesOfConflict;
	
	@Column(name="resolved_status")
	private String resolvedStatus;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column
	private String futureOrAdendumSowId;
	
	@Column
	private String notes;
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getFutureOrAdendumSowId() {
		return futureOrAdendumSowId;
	}

	public void setFutureOrAdendumSowId(String futureOrAdendumSowId) {
		this.futureOrAdendumSowId = futureOrAdendumSowId;
	}

	public String getTypesOfConflict() {
		return typesOfConflict;
	}

	public void setTypesOfConflict(String typesOfConflict) {
		this.typesOfConflict = typesOfConflict;
	}

	public String getResolvedStatus() {
		return resolvedStatus;
	}

	public void setResolvedStatus(String resolvedStatus) {
		this.resolvedStatus = resolvedStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the force_closure_reason
	 */
	public String getForce_closure_reason() {
		return force_closure_reason;
	}

	/**
	 * @param force_closure_reason the force_closure_reason to set
	 */
	public void setForce_closure_reason(String force_closure_reason) {
		this.force_closure_reason = force_closure_reason;
	}

	/**
	 * @return the force_closure_date
	 */
	public Timestamp getForce_closure_date() {
		return force_closure_date;
	}

	/**
	 * @param force_closure_date the force_closure_date to set
	 */
	public void setForce_closure_date(Timestamp force_closure_date) {
		this.force_closure_date = force_closure_date;
	}
	@Column(name="force_closure_date")
	private Timestamp force_closure_date;
	
	
	
	public String getSow_override() {
		return sow_override;
	}

	public void setSow_override(String sow_override) {
		this.sow_override = sow_override;
	}

	public String getHeliusAccountManager() {
		return heliusAccountManager;
	}

	public void setHeliusAccountManager(String heliusAccountManager) {
		this.heliusAccountManager = heliusAccountManager;
	}
	//bi-directional many-to-one association to Sow_Details_History
	//@JsonIgnore
	@OneToMany(mappedBy="sowDetailsId",fetch=FetchType.LAZY)
	private List<Sow_Details_History> sowDetailsHistories;

	public List<Sow_Details_History> getSowDetailsHistories() {
		return sowDetailsHistories;
	}

	public void setSowDetailsHistories(List<Sow_Details_History> sowDetailsHistories) {
		this.sowDetailsHistories = sowDetailsHistories;
	}
	
	@OneToMany(mappedBy="sowDetailsId",fetch=FetchType.LAZY)
	private List<Sow_Billing_Schedule> sowBillingSchedule;
	
	public List<Sow_Billing_Schedule> getSowBillingSchedule() {
		return sowBillingSchedule;
	}

	public void setSowBillingSchedule(List<Sow_Billing_Schedule> sowBillingSchedule) {
		this.sowBillingSchedule = sowBillingSchedule;
	}
	
	@OneToMany(mappedBy="sowDetailsId",fetch=FetchType.LAZY)
	private List<Sow_Employee_Association> sowEmpAssoc;
	
	
	public String getSowStatus() {
		return sowStatus;
	}

	public void setSowStatus(String sowStatus) {
		this.sowStatus = sowStatus;
	}

	public String getPrevious_sownumber() {
		return previous_sownumber;
	}

	public void setPrevious_sownumber(String previous_sownumber) {
		this.previous_sownumber = previous_sownumber;
	}

	public String getSowClient() {
		return sowClient;
	}

	public void setSowClient(String sowClient) {
		this.sowClient = sowClient;
	}

	public String getSowResources() {
		return sowResources;
	}

	public void setSowResources(String sowResources) {
		this.sowResources = sowResources;
	}

	public String getSowClientGroup() {
		return sowClientGroup;
	}

	public void setSowClientGroup(String sowClientGroup) {
		this.sowClientGroup = sowClientGroup;
	}


	public String getSowInitialCost() {
		return sowInitialCost;
	}

	public void setSowInitialCost(String sowInitialCost) {
		this.sowInitialCost = sowInitialCost;
	}

	public float getSowInitialCostAmount() {
		return sowInitialCostAmount;
	}

	public void setSowInitialCostAmount(float sowInitialCostAmount) {
		this.sowInitialCostAmount = sowInitialCostAmount;
	}

	public String getPoPath() {
		return poPath;
	}

	public void setPoPath(String poPath) {
		this.poPath = poPath;
	}

	public String getClientContact() {
		return clientContact;
	}

	public void setClientContact(String clientContact) {
		this.clientContact = clientContact;
	}

	public String getBonusReimbursible() {
		return bonusReimbursible;
	}

	public void setBonusReimbursible(String bonusReimbursible) {
		this.bonusReimbursible = bonusReimbursible;
	}

	public float getBonusReimbursibleAmount() {
		return bonusReimbursibleAmount;
	}

	public void setBonusReimbursibleAmount(float bonusReimbursibleAmount) {
		this.bonusReimbursibleAmount = bonusReimbursibleAmount;
	}

	public String getBonusFrequency() {
		return bonusFrequency;
	}

	public void setBonusFrequency(String bonusFrequency) {
		this.bonusFrequency = bonusFrequency;
	}

	public String getClientContactType() {
		return clientContactType;
	}

	public void setClientContactType(String clientContactType) {
		this.clientContactType = clientContactType;
	}

	public String getSowPath() {
		return sowPath;
	}

	public void setSowPath(String sowPath) {
		this.sowPath = sowPath;
	}

	public Sow_Details() {
	}

	public int getSowDetailsId() {
		return this.sowDetailsId;
	}

	public void setSowDetailsId(int sowDetailsId) {
		this.sowDetailsId = sowDetailsId;
	}

	public String getAssignSowToEmployee() {
		return this.assignSowToEmployee;
	}

	public void setAssignSowToEmployee(String assignSowToEmployee) {
		this.assignSowToEmployee = assignSowToEmployee;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEmployeesLeavesBillable() {
		return this.employeesLeavesBillable;
	}

	public void setEmployeesLeavesBillable(String employeesLeavesBillable) {
		this.employeesLeavesBillable = employeesLeavesBillable;
	}

	public String getHeliusReferenceNumber() {
		return this.heliusReferenceNumber;
	}

	public void setHeliusReferenceNumber(String heliusReferenceNumber) {
		this.heliusReferenceNumber = heliusReferenceNumber;
	}

	public Timestamp getPoEndDate() {
		return this.poEndDate;
	}

	public void setPoEndDate(Timestamp poEndDate) {
		this.poEndDate = poEndDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	

	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}

	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public String getLast_modified_by() {
		return last_modified_by;
	}

	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Timestamp getPoStartDate() {
		return this.poStartDate;
	}

	public void setPoStartDate(Timestamp poStartDate) {
		this.poStartDate = poStartDate;
	}

	public String getRealizedSowValue() {
		return this.realizedSowValue;
	}

	public void setRealizedSowValue(String realizedSowValue) {
		this.realizedSowValue = realizedSowValue;
	}

	public String getRenewalStatusDescription() {
		return this.renewalStatusDescription;
	}

	public void setRenewalStatusDescription(String renewalStatusDescription) {
		this.renewalStatusDescription = renewalStatusDescription;
	}

	public String getSowClientReferenceNumber() {
		return this.sowClientReferenceNumber;
	}

	public void setSowClientReferenceNumber(String sowClientReferenceNumber) {
		this.sowClientReferenceNumber = sowClientReferenceNumber;
	}

	public Timestamp getSowExpiryDate() {
		return this.sowExpiryDate;
	}

	public void setSowExpiryDate(Timestamp sowExpiryDate) {
		this.sowExpiryDate = sowExpiryDate;
	}

	public float getSowQuantity() {
		return this.sowQuantity;
	}

	public void setSowQuantity(float sowQuantity) {
		this.sowQuantity = sowQuantity;
	}

	public String getSowRateForUnit() {
		return this.sowRateForUnit;
	}

	public void setSowRateForUnit(String sowRateForUnit) {
		this.sowRateForUnit = sowRateForUnit;
	}

	public String getSowRenewalStatus() {
		return this.sowRenewalStatus;
	}

	public void setSowRenewalStatus(String sowRenewalStatus) {
		this.sowRenewalStatus = sowRenewalStatus;
	}

	public Timestamp getSowStartDate() {
		return this.sowStartDate;
	}

	public void setSowStartDate(Timestamp sowStartDate) {
		this.sowStartDate = sowStartDate;
	}

	public String getSowTotalValue() {
		return this.sowTotalValue;
	}

	public void setSowTotalValue(String sowTotalValue) {
		this.sowTotalValue = sowTotalValue;
	}

	public String getSowType() {
		return this.sowType;
	}

	public void setSowType(String sowType) {
		this.sowType = sowType;
	}

	public String getUnrealizedSowValue() {
		return this.unrealizedSowValue;
	}

	public void setUnrealizedSowValue(String unrealizedSowValue) {
		this.unrealizedSowValue = unrealizedSowValue;
	}

	

	public List<Sow_Employee_Association> getSowEmpAssoc() {
		return sowEmpAssoc;
	}

	public void setSowEmpAssoc(List<Sow_Employee_Association> sowEmpAssoc) {
		this.sowEmpAssoc = sowEmpAssoc;
	}

	/*public Sow_Details_History addSowDetailsHistory(Sow_Details_History sowDetailsHistory) {
		getSowDetailsHistories().add(sowDetailsHistory);
		sowDetailsHistory.setSowDetail(this);

		return sowDetailsHistory;
	}

	public Sow_Details_History removeSowDetailsHistory(Sow_Details_History sowDetailsHistory) {
		getSowDetailsHistories().remove(sowDetailsHistory);
		sowDetailsHistory.setSowDetail(null);

		return sowDetailsHistory;
	}*/

}