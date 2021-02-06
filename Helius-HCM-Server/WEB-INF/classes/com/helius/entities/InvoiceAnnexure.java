package com.helius.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.NotAudited;
@Entity
public class InvoiceAnnexure {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int invoiceAnnextureId;
	@Column
	private String employee_id;
	@Column
	private String employee_name;
	@Column
	private String onebankId;
	@Column
	private String po_number;
	@Column
	private String pc_code;
	@Column
	private Timestamp po_start_date;
	@Column
	private Timestamp po_end_date;
	@Column
	private Timestamp month;
	@Column
	private float sow_rate_for_unit;
	@Column
	private float monthly_ctc;
	@Column
	private float margin;
	@Column
	private float noOfMandays;
	@Column
	private float rateForTotalWorkedDays;
	@Column
	private String account_manager;
	@Column
	private String helius_recruiter;
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
	
	@Column
	private Timestamp date_of_joining;
	
	
	public String getPc_code() {
		return pc_code;
	}
	public void setPc_code(String pc_code) {
		this.pc_code = pc_code;
	}
	/**
	 * @return the date_of_joining
	 */
	public Timestamp getDate_of_joining() {
		return date_of_joining;
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
	/**
	 * @param date_of_joining the date_of_joining to set
	 */
	public void setDate_of_joining(Timestamp date_of_joining) {
		this.date_of_joining = date_of_joining;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public Timestamp getMonth() {
		return month;
	}
	public void setMonth(Timestamp month) {
		this.month = month;
	}
	public void setPo_start_date(Timestamp po_start_date) {
		this.po_start_date = po_start_date;
	}
	public void setPo_end_date(Timestamp po_end_date) {
		this.po_end_date = po_end_date;
	}
	public int getInvoiceAnnextureId() {
		return invoiceAnnextureId;
	}
	public void setInvoiceAnnextureId(int invoiceAnnextureId) {
		this.invoiceAnnextureId = invoiceAnnextureId;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getOnebankId() {
		return onebankId;
	}
	public void setOnebankId(String onebankId) {
		this.onebankId = onebankId;
	}
	public float getSow_rate_for_unit() {
		return sow_rate_for_unit;
	}
	public void setSow_rate_for_unit(float sow_rate_for_unit) {
		this.sow_rate_for_unit = sow_rate_for_unit;
	}
	public float getMonthly_ctc() {
		return monthly_ctc;
	}
	public void setMonthly_ctc(float monthly_ctc) {
		this.monthly_ctc = monthly_ctc;
	}
	public float getMargin() {
		return margin;
	}
	public void setMargin(float margin) {
		this.margin = margin;
	}
	public float getNoOfMandays() {
		return noOfMandays;
	}
	public void setNoOfMandays(float noOfMandays) {
		this.noOfMandays = noOfMandays;
	}
	public float getRateForTotalWorkedDays() {
		return rateForTotalWorkedDays;
	}
	public void setRateForTotalWorkedDays(float rateForTotalWorkedDays) {
		this.rateForTotalWorkedDays = rateForTotalWorkedDays;
	}
	public String getPo_number() {
		return po_number;
	}
	public void setPo_number(String po_number) {
		this.po_number = po_number;
	}
	
	
	public Timestamp getPo_start_date() {
		return po_start_date;
	}
	public Timestamp getPo_end_date() {
		return po_end_date;
	}
	
	public String getAccount_manager() {
		return account_manager;
	}
	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}
	public String getHelius_recruiter() {
		return helius_recruiter;
	}
	public void setHelius_recruiter(String helius_recruiter) {
		this.helius_recruiter = helius_recruiter;
	}
	 
	
}
