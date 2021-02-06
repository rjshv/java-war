package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the Sow_Billing_Schedule database table.
 * 
 */
@Entity
@NamedQuery(name="Sow_Billing_Schedule.findAll", query="SELECT s FROM Sow_Billing_Schedule s")
@Audited
public class Sow_Billing_Schedule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="sow_billing_schedule_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sowBillingScheduleId;

	@Column
	private BigDecimal margin;

	@Column
	private BigDecimal bonus_amount;

	@Column
	private BigDecimal salary;
	

	
	@Column(name="sow_details_id")
	private int sowDetailsId;

	@Column
	private String month;
	@Column(name="helius_reference_number")
	private String heliusReferenceNumber;
	@Column
	private BigDecimal amount;
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

	public Sow_Billing_Schedule() {
	}

	public String getHeliusReferenceNumber() {
		return heliusReferenceNumber;
	}

	public void setHeliusReferenceNumber(String heliusReferenceNumber) {
		this.heliusReferenceNumber = heliusReferenceNumber;
	}

	public int getSowBillingScheduleId() {
		return this.sowBillingScheduleId;
	}

	public void setSowBillingScheduleId(int sowBillingScheduleId) {
		this.sowBillingScheduleId = sowBillingScheduleId;
	}

	public BigDecimal getBonus_amount() {
		return bonus_amount;
	}

	public void setBonus_amount(BigDecimal bonus_amount) {
		this.bonus_amount = bonus_amount;
	}

	public BigDecimal getMargin() {
		return margin;
	}

	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getSowDetailsId() {
		return this.sowDetailsId;
	}

	public void setSowDetailsId(int sowDetailsId) {
		this.sowDetailsId = sowDetailsId;
	}

	
}