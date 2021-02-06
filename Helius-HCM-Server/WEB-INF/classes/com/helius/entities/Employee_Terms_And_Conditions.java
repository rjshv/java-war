package com.helius.entities;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Date;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.print.attribute.standard.DateTimeAtCreation;

import org.apache.poi.poifs.crypt.dsig.services.TimeStampService;
import org.apache.poi.ss.usermodel.DateUtil;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.type.TimestampType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.joda.DateTimeParser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer;

@Entity
@Table(name="Employee_Terms_And_Conditions")
@Audited
public class Employee_Terms_And_Conditions {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_terms_and_conditions_id;
	@Column
	private String employee_id;
	@Column
	private String training_eligible;
	//@Column
	//private String onetime_bonus_eligible;
	@Column
	private String notice_period;
	@Column
	private String probation_period;
	@Column
	private String reservist_leave_allowed;
	@Column
	private String probation_duration;
	@Column
	private String waiving_of_commitment_amount;
	@Column
	private String career_supportive_programme;
	@Column
	private String commitment_amount;
	@Column
	private String commitment_period;
	//@Column
	//private String onetime_bonus_amount;
	@Column
	private String compensatory_off_leave_allowed;
	@Column
	private String leave_encashments;
	@Column
	private String training_months;
	//@Column(name = "created", nullable = false, updatable=false)
	//@Temporal(TemporalType.TIMESTAMP)
	@Column	
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	//private Timestamp last_modified_date;
	@Column
	@NotAudited
	private String last_modified_by;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column	
	private String created_by;

	
	public String getWaiving_of_commitment_amount() {
		return waiving_of_commitment_amount;
	}
	public void setWaiving_of_commitment_amount(String waiving_of_commitment_amount) {
		this.waiving_of_commitment_amount = waiving_of_commitment_amount;
	}
	
	public String getCareer_supportive_programme() {
		return career_supportive_programme;
	}
	public void setCareer_supportive_programme(String career_supportive_programme) {
		this.career_supportive_programme = career_supportive_programme;
	}
	public String getCommitment_amount() {
		return commitment_amount;
	}
	public void setCommitment_amount(String commitment_amount) {
		this.commitment_amount = commitment_amount;
	}	
	public String getCommitment_period() {
		return commitment_period;
	}
	public void setCommitment_period(String commitment_period) {
		this.commitment_period = commitment_period;
	}
	public String getLeave_encashments() {
		return leave_encashments;
	}
	public void setLeave_encashments(String leave_encashments) {
		this.leave_encashments = leave_encashments;
	}
	public String getCompensatory_off_leave_allowed() {
		return compensatory_off_leave_allowed;
	}
	public void setCompensatory_off_leave_allowed(String compensatory_off_leave_allowed) {
		this.compensatory_off_leave_allowed = compensatory_off_leave_allowed;
	}
	public String getProbation_duration() {
		return probation_duration;
	}
	public void setProbation_duration(String probation_duration) {
		this.probation_duration = probation_duration;
	}
	
	public String getTraining_months() {
		return training_months;
	}
	public void setTraining_months(String training_months) {
		this.training_months = training_months;
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
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getLast_modified_by() {
		return last_modified_by;
	}
	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}
	public int getEmployee_terms_and_conditions_id() {
		return employee_terms_and_conditions_id;
	}
	public void setEmployee_terms_and_conditions_id(int employee_terms_and_conditions_id) {
		this.employee_terms_and_conditions_id = employee_terms_and_conditions_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getTraining_eligible() {
		return training_eligible;
	}
	public void setTraining_eligible(String training_eligible) {
		this.training_eligible = training_eligible;
	}
	
	public String getNotice_period() {
		return notice_period;
	}
	public void setNotice_period(String notice_period) {
		this.notice_period = notice_period;
	}
	public String getProbation_period() {
		return probation_period;
	}
	public void setProbation_period(String probation_period) {
		this.probation_period = probation_period;
	}
	public String getReservist_leave_allowed() {
		return reservist_leave_allowed;
	}
	public void setReservist_leave_allowed(String reservist_leave_allowed) {
		this.reservist_leave_allowed = reservist_leave_allowed;
	}
	
	
	
}
