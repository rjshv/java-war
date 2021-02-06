package com.helius.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Employee_SOW_Details")
public class Employee_SOW_Details  {
	
	@Id
	@Column
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_sow_id;	
	@Column
	private String employee_id;
	@Column
	private int employee_assignment_id;
	@Column
	private Date sow_start_date;
	@Column
	private Date sow_end_date;
	@Column
	private int sow_duration;
	@Column
	private String current_sow_number;
	@Column
	private float sow_amount;
	@Column
	private String sow_reference;
	@Column
	private String po_number;
	@Column
	private float agency_fee;
	@Column
	private float total_without_agency_fee;
	@Column
	private float total_agency_fee;
	@Column
	private String sow_active;
	@Column
	private String currency;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSow_active() {
		return sow_active;
	}
	public void setSow_active(String sow_active) {
		this.sow_active = sow_active;
	}
	public int getEmployee_assignment_id() {
		return employee_assignment_id;
	}
	public void setEmployee_assignment_id(int employee_assignment_id) {
		this.employee_assignment_id = employee_assignment_id;
	}
	public int getEmployee_sow_id() {
		return employee_sow_id;
	}
	public void setEmployee_sow_id(int employee_sow_id) {
		this.employee_sow_id = employee_sow_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	
	public Date getSow_start_date() {
		return sow_start_date;
	}
	public void setSow_start_date(Date sow_start_date) {
		this.sow_start_date = sow_start_date;
	}
	public Date getSow_end_date() {
		return sow_end_date;
	}
	public void setSow_end_date(Date sow_end_date) {
		this.sow_end_date = sow_end_date;
	}
	public int getSow_duration() {
		return sow_duration;
	}
	public void setSow_duration(int sow_duration) {
		this.sow_duration = sow_duration;
	}
	public String getCurrent_sow_number() {
		return current_sow_number;
	}
	public void setCurrent_sow_number(String current_sow_number) {
		this.current_sow_number = current_sow_number;
	}
	public float getSow_amount() {
		return sow_amount;
	}
	public void setSow_amount(float sow_amount) {
		this.sow_amount = sow_amount;
	}
	public String getSow_reference() {
		return sow_reference;
	}
	public void setSow_reference(String sow_reference) {
		this.sow_reference = sow_reference;
	}
	public String getPo_number() {
		return po_number;
	}
	public void setPo_number(String po_number) {
		this.po_number = po_number;
	}
	public float getAgency_fee() {
		return agency_fee;
	}
	public void setAgency_fee(float agency_fee) {
		this.agency_fee = agency_fee;
	}
	public float getTotal_without_agency_fee() {
		return total_without_agency_fee;
	}
	public void setTotal_without_agency_fee(float total_without_agency_fee) {
		this.total_without_agency_fee = total_without_agency_fee;
	}
	public float getTotal_agency_fee() {
		return total_agency_fee;
	}
	public void setTotal_agency_fee(float total_agency_fee) {
		this.total_agency_fee = total_agency_fee;
	}
	
	
}
