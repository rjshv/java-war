package com.helius.entities;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
/**
 * @author Tirumala
 * 22-Feb-2018
 */
@Entity
@Audited
@Table(name="Employee_Personal_Details")
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Employee_Personal_Details extends HeliusEntity{

	@Id
	@Column
	private String employee_id;
	
	/**
	 * @return the employee_id
	 */
	public String getEmployee_id() {
		return employee_id;
	}

	/**
	 * @param employee_id the employee_id to set
	 */
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	@Column
	private String employee_name;
	@Column
	private String name_in_chinese_characters;
	@Column
	private Timestamp date_of_birth;
	@Column
	private String marital_status;
	@Column
	private Timestamp actual_date_of_joining;
	@Column
	private String photo;
	@Column
	private String communication_email_id;
	@Column
	private String languages_known;
	@Column
	private String on_notice_period;
	@Column
	private String employee_status;
	@Column
	private String emp_race;
	@Column
	private String religion;
	@Column
	private String skills;
	@Column
	private Timestamp resignation_date;
	@Column
	private Timestamp contractual_working_date;
	@Column
	private Timestamp relieving_date;
	@Column
	private String resignation_reason;
	@Column
	private String designation;
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
	private String primary_skills;
	@Column
	private String secondary_skills;
	@Column
	private String resource_type;

	public String getPrimary_skills() {
		return primary_skills;
	}

	public void setPrimary_skills(String primary_skills) {
		this.primary_skills = primary_skills;
	}

	public String getEmp_race() {
		return emp_race;
	}

	public void setEmp_race(String emp_race) {
		this.emp_race = emp_race;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getSecondary_skills() {
		return secondary_skills;
	}

	public void setSecondary_skills(String secondary_skills) {
		this.secondary_skills = secondary_skills;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getResignation_reason() {
		return resignation_reason;
	}

	public void setResignation_reason(String resignation_reason) {
		this.resignation_reason = resignation_reason;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public Timestamp getResignation_date() {
		return resignation_date;
	}

	public void setResignation_date(Timestamp resignation_date) {
		this.resignation_date = resignation_date;
	}

	public Timestamp getContractual_working_date() {
		return contractual_working_date;
	}

	public void setContractual_working_date(Timestamp contractual_working_date) {
		this.contractual_working_date = contractual_working_date;
	}

	public Timestamp getRelieving_date() {
		return relieving_date;
	}

	public void setRelieving_date(Timestamp relieving_date) {
		this.relieving_date = relieving_date;
	}

	public String getOn_notice_period() {
		return on_notice_period;
	}

	public void setOn_notice_period(String on_notice_period) {
		this.on_notice_period = on_notice_period;
	}

	public String getEmployee_status() {
		return employee_status;
	}

	public void setEmployee_status(String employee_status) {
		this.employee_status = employee_status;
	}

	public Timestamp getCreate_date() {
		return create_date;
	}

	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getName_in_chinese_characters() {
		return name_in_chinese_characters;
	}

	public void setName_in_chinese_characters(String name_in_chinese_characters) {
		this.name_in_chinese_characters = name_in_chinese_characters;
	}

	public String getCommunication_email_id() {
		return communication_email_id;
	}

	public void setCommunication_email_id(String communication_email_id) {
		this.communication_email_id = communication_email_id;
	}

	public String getLanguages_known() {
		return languages_known;
	}

	public void setLanguages_known(String languages_known) {
		this.languages_known = languages_known;
	}

	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	
	
	public Timestamp getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Timestamp date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public Timestamp getActual_date_of_joining() {
		return actual_date_of_joining;
	}

	public void setActual_date_of_joining(Timestamp actual_date_of_joining) {
		this.actual_date_of_joining = actual_date_of_joining;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	/* (non-Javadoc)
	 * @see com.helius.entities.IEntity#toJSON()
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
