package com.helius.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;


public class Sowdashboard {
	
	@Column
	private int sow_details_id;

	@Column
	private String assign_sow_to_employee;

	private String currency;

	@Column
	private String employees_leaves_billable;
	
	@Column
	private String helius_reference_number;
	
	@Column
	private String po_number;
	
	@Column
	private Integer sow_resources;

	@Column
	private Timestamp po_end_date;

	@Column
	private Timestamp po_start_date;

	@Column
	private String realized_sow_value;

	@Column
	private String renewal_status_description;

	@Column
	private String sow_client_reference_number;

	@Column
	private Timestamp sow_expiry_date;

	@Column
	private float sow_quantity;

	@Column
	private String sow_rate_for_unit;

	@Column
	private String sow_renewal_status;

	@Column
	private Timestamp sow_start_date;

	@Column
	private String sow_total_value;

	@Column
	private String sow_type;

	@Column
	private String unrealized_sow_value;
	
	@Column
	private String sow_path;
	
	@Column
	private String sow_initial_cost;

	@Column
	private float sow_initial_cost_amount;

	@Column
	private String po_path;
	
	@Column
	private String bonus_reimbursible;
	
	@Column
	private float bonus_reimbursible_amount;
	
	@Column
	private String bonus_frequency;
	
	@Column
	private String client_contact;

	@Column
	private String client_contact_type;
	
	@Column
	private String sow_client;
	
	@Column
	private String sow_status;
	
	@Column
	private String previous_sownumber;
	
	@Column
	private Timestamp last_modified_date;
	@Column
	private String last_modified_by;
	@Column
	private Timestamp create_date;
	@Column
	private String created_by;
	
	@Column
	private String sow_client_group;
	
	@Column
	private String helius_account_manager;
	
	@Column
	private String force_closure_reason;
	
	@Column
	private String types_of_conflict;
	
	@Column
	private String resolved_status;
	
	@Column
	private String remarks;
	
	@Column
	private String notes;
	
	@Column
	private String employee_status;
	
	
	/**
	 * @return the employee_status
	 */
	public String getEmployee_status() {
		return employee_status;
	}

	/**
	 * @param employee_status the employee_status to set
	 */
	public void setEmployee_status(String employee_status) {
		this.employee_status = employee_status;
	}

	/**
	 * @return the relieving_date
	 */
	public Timestamp getRelieving_date() {
		return relieving_date;
	}

	/**
	 * @param relieving_date the relieving_date to set
	 */
	public void setRelieving_date(Timestamp relieving_date) {
		this.relieving_date = relieving_date;
	}

	@Column 
	private Timestamp relieving_date;
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column
	private String futureOrAdendumSowId;
	
	public String getFutureOrAdendumSowId() {
		return futureOrAdendumSowId;
	}

	public void setFutureOrAdendumSowId(String futureOrAdendumSowId) {
		this.futureOrAdendumSowId = futureOrAdendumSowId;
	}
	
	@Column
	private String employee_id;
	
	@Column
	private Integer offer_id;
	
	@Column
	private BigDecimal margin;
	
	@Column
	private Timestamp force_closure_date;

	@Column
	private String sow_override;


	public Integer getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(Integer offer_id) {
		this.offer_id = offer_id;
	}

	public String getSow_override() {
		return sow_override;
	}

	public void setSow_override(String sow_override) {
		this.sow_override = sow_override;
	}

	public Timestamp getForce_closure_date() {
		return force_closure_date;
	}

	public void setForce_closure_date(Timestamp force_closure_date) {
		this.force_closure_date = force_closure_date;
	}

	public int getSow_details_id() {
		return sow_details_id;
	}

	public void setSow_details_id(int sow_details_id) {
		this.sow_details_id = sow_details_id;
	}

	public String getTypes_of_conflict() {
		return types_of_conflict;
	}

	public void setTypes_of_conflict(String types_of_conflict) {
		this.types_of_conflict = types_of_conflict;
	}

	public String getResolved_status() {
		return resolved_status;
	}

	public void setResolved_status(String resolved_status) {
		this.resolved_status = resolved_status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAssign_sow_to_employee() {
		return assign_sow_to_employee;
	}

	public void setAssign_sow_to_employee(String assign_sow_to_employee) {
		this.assign_sow_to_employee = assign_sow_to_employee;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEmployees_leaves_billable() {
		return employees_leaves_billable;
	}

	public void setEmployees_leaves_billable(String employees_leaves_billable) {
		this.employees_leaves_billable = employees_leaves_billable;
	}

	public String getHelius_reference_number() {
		return helius_reference_number;
	}

	public void setHelius_reference_number(String helius_reference_number) {
		this.helius_reference_number = helius_reference_number;
	}

	public String getPo_number() {
		return po_number;
	}

	public void setPo_number(String po_number) {
		this.po_number = po_number;
	}

	

	public Integer getSow_resources() {
		return sow_resources;
	}

	public void setSow_resources(Integer sow_resources) {
		this.sow_resources = sow_resources;
	}

	public Timestamp getPo_end_date() {
		return po_end_date;
	}

	public void setPo_end_date(Timestamp po_end_date) {
		this.po_end_date = po_end_date;
	}

	public Timestamp getPo_start_date() {
		return po_start_date;
	}

	public void setPo_start_date(Timestamp po_start_date) {
		this.po_start_date = po_start_date;
	}

	public String getRealized_sow_value() {
		return realized_sow_value;
	}

	public void setRealized_sow_value(String realized_sow_value) {
		this.realized_sow_value = realized_sow_value;
	}

	public String getRenewal_status_description() {
		return renewal_status_description;
	}

	public void setRenewal_status_description(String renewal_status_description) {
		this.renewal_status_description = renewal_status_description;
	}

	public String getSow_client_reference_number() {
		return sow_client_reference_number;
	}

	public void setSow_client_reference_number(String sow_client_reference_number) {
		this.sow_client_reference_number = sow_client_reference_number;
	}

	public Timestamp getSow_expiry_date() {
		return sow_expiry_date;
	}

	public void setSow_expiry_date(Timestamp sow_expiry_date) {
		this.sow_expiry_date = sow_expiry_date;
	}

	public float getSow_quantity() {
		return sow_quantity;
	}

	public void setSow_quantity(float sow_quantity) {
		this.sow_quantity = sow_quantity;
	}

	public String getSow_rate_for_unit() {
		return sow_rate_for_unit;
	}

	public void setSow_rate_for_unit(String sow_rate_for_unit) {
		this.sow_rate_for_unit = sow_rate_for_unit;
	}

	public String getSow_renewal_status() {
		return sow_renewal_status;
	}

	public void setSow_renewal_status(String sow_renewal_status) {
		this.sow_renewal_status = sow_renewal_status;
	}

	public Timestamp getSow_start_date() {
		return sow_start_date;
	}

	public void setSow_start_date(Timestamp sow_start_date) {
		this.sow_start_date = sow_start_date;
	}

	public String getSow_total_value() {
		return sow_total_value;
	}

	public void setSow_total_value(String sow_total_value) {
		this.sow_total_value = sow_total_value;
	}

	public String getSow_type() {
		return sow_type;
	}

	public void setSow_type(String sow_type) {
		this.sow_type = sow_type;
	}

	public String getUnrealized_sow_value() {
		return unrealized_sow_value;
	}

	public void setUnrealized_sow_value(String unrealized_sow_value) {
		this.unrealized_sow_value = unrealized_sow_value;
	}

	public String getSow_path() {
		return sow_path;
	}

	public void setSow_path(String sow_path) {
		this.sow_path = sow_path;
	}

	public String getSow_initial_cost() {
		return sow_initial_cost;
	}

	public void setSow_initial_cost(String sow_initial_cost) {
		this.sow_initial_cost = sow_initial_cost;
	}

	public float getSow_initial_cost_amount() {
		return sow_initial_cost_amount;
	}

	public void setSow_initial_cost_amount(float sow_initial_cost_amount) {
		this.sow_initial_cost_amount = sow_initial_cost_amount;
	}

	public String getPo_path() {
		return po_path;
	}

	public void setPo_path(String po_path) {
		this.po_path = po_path;
	}

	public String getBonus_reimbursible() {
		return bonus_reimbursible;
	}

	public void setBonus_reimbursible(String bonus_reimbursible) {
		this.bonus_reimbursible = bonus_reimbursible;
	}

	public float getBonus_reimbursible_amount() {
		return bonus_reimbursible_amount;
	}

	public void setBonus_reimbursible_amount(float bonus_reimbursible_amount) {
		this.bonus_reimbursible_amount = bonus_reimbursible_amount;
	}

	public String getBonus_frequency() {
		return bonus_frequency;
	}

	public void setBonus_frequency(String bonus_frequency) {
		this.bonus_frequency = bonus_frequency;
	}

	public String getClient_contact() {
		return client_contact;
	}

	public void setClient_contact(String client_contact) {
		this.client_contact = client_contact;
	}

	public String getClient_contact_type() {
		return client_contact_type;
	}

	public void setClient_contact_type(String client_contact_type) {
		this.client_contact_type = client_contact_type;
	}

	public String getSow_client() {
		return sow_client;
	}

	public void setSow_client(String sow_client) {
		this.sow_client = sow_client;
	}

	public String getSow_status() {
		return sow_status;
	}

	public void setSow_status(String sow_status) {
		this.sow_status = sow_status;
	}

	public String getPrevious_sownumber() {
		return previous_sownumber;
	}

	public void setPrevious_sownumber(String previous_sownumber) {
		this.previous_sownumber = previous_sownumber;
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

	public String getSow_client_group() {
		return sow_client_group;
	}

	public void setSow_client_group(String sow_client_group) {
		this.sow_client_group = sow_client_group;
	}

	public String getHelius_account_manager() {
		return helius_account_manager;
	}

	public void setHelius_account_manager(String helius_account_manager) {
		this.helius_account_manager = helius_account_manager;
	}

	public String getForce_closure_reason() {
		return force_closure_reason;
	}

	public void setForce_closure_reason(String force_closure_reason) {
		this.force_closure_reason = force_closure_reason;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	

	

	
}
