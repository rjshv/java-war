package com.helius.entities;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name="Employee_Appraisal_Details")
@Audited
public class Employee_Appraisal_Details {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_appraisal_details_id;
	@Column
	private String employee_id;
	@Column
	private Timestamp employee_merit_increase_date;
	@Column
	private Timestamp effective_from;
	@Column
	private String current_monthly_basic;
	@Column
	private String new_monthly_basic;
	@Column
	private Timestamp appraisal_due_on;
	@Column
	private Timestamp increment_eligible_on;
	@Column
	private float increment_percent;
	@Column
	private String bonus_frequency;
	@Column
	private String bonus_amount;
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
		
	public String getBonus_amount() {
		return bonus_amount;
	}
	public void setBonus_amount(String bonus_amount) {
		this.bonus_amount = bonus_amount;
	}
	public String getCurrent_monthly_basic() {
		return current_monthly_basic;
	}
	public void setCurrent_monthly_basic(String current_monthly_basic) {
		this.current_monthly_basic = current_monthly_basic;
	}
	public String getNew_monthly_basic() {
		return new_monthly_basic;
	}
	public void setNew_monthly_basic(String new_monthly_basic) {
		this.new_monthly_basic = new_monthly_basic;
	}
	public Timestamp getAppraisal_due_on() {
		return appraisal_due_on;
	}
	public void setAppraisal_due_on(Timestamp appraisal_due_on) {
		this.appraisal_due_on = appraisal_due_on;
	}
	
	public Timestamp getIncrement_eligible_on() {
		return increment_eligible_on;
	}
	public void setIncrement_eligible_on(Timestamp increment_eligible_on) {
		this.increment_eligible_on = increment_eligible_on;
	}
	public float getIncrement_percent() {
		return increment_percent;
	}
	public void setIncrement_percent(float increment_percent) {
		this.increment_percent = increment_percent;
	}
	public String getBonus_frequency() {
		return bonus_frequency;
	}
	public void setBonus_frequency(String bonus_frequency) {
		this.bonus_frequency = bonus_frequency;
	}
	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}
	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public Timestamp getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}
	public String getLast_modified_by() {
		return last_modified_by;
	}
	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}
	
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public int getEmployee_appraisal_details_id() {
		return employee_appraisal_details_id;
	}
	public void setEmployee_appraisal_details_id(int employee_appraisal_details_id) {
		this.employee_appraisal_details_id = employee_appraisal_details_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public Timestamp getEmployee_merit_increase_date() {
		return employee_merit_increase_date;
	}
	public void setEmployee_merit_increase_date(Timestamp employee_merit_increase_date) {
		this.employee_merit_increase_date = employee_merit_increase_date;
	}
	public Timestamp getEffective_from() {
		return effective_from;
	}
	public void setEffective_from(Timestamp effective_from) {
		this.effective_from = effective_from;
	}
	
	
	
}
