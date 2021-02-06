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
@Table(name="Employee_Offer_Details")
@Audited
public class Employee_Offer_Details {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int offer_id;
	@Column
	private String employee_id;
	@Column
	private String employee_name;
	@Column
	private Timestamp date_of_birth;
	@Column
	private String name_in_chinese_characters;
	@Column
	private String gender;
	@Column
	private String personal_email_id;
	@Column
	private String country_code;
	@Column
	private String mobile_number;
	@Column
	private String nationality;
	@Column
	private String work_country;
	@Column
	private String client;
	@Column
	private String lob;
	@Column
	private String client_group;
	//@Column
	//private String client_sub_group;
	@Column
	private Timestamp offer_made_on;
	@Column
	private String designation;
	@Column
	private Timestamp expected_date_of_joining;
	@Column
	private String offer_status;
	@Column
	private String helius_recruiter;
	@Column
	private String comments;
	//@Column
	//private String background_verification_required;
	//@Column
	//private String pre_employment_screening_required;
	@Column
	private String offer_declined_reason;
	@Column
	@NotAudited
	private String last_modified_by;
	@Column
	private String offer_path;
	@Column
	private String job_desc_path;
	@Column
	private String ep_path;
	@Column
	private String paid_by;
	@Column
	private String relevant_experience_years;
	@Column
	private String relevant_experience_months;
	@Column
	private String years_of_experience_months;
	@Column
	private String years_of_experience_years;
	//@Column
	//private Date onetime_bonus_payable_on;
	@Column
	private String bonus_payable;
	@Column
	private String onetime_bonus_amount;
	@Column
	private String account_manager;
	@Column
	private String hiring_manager;
	@Column
	private String budget_owner;
	@Column
	private String visa_status;
	@Column
	private String national_id;
	@Column
	private String relocation_allowance;
	@Column
	private String pc_code;
	@Column
	private String emp_race;
	@Column
	private String religion;
	@Column
	private String dept_code;
	@Column
	private String bgv_report_path;
	@Column
	private String bgv_required;
	@Column
	private String bgv_completed;
	@Column
	private Timestamp bgv_completion_date;
	@Column
	private String pes_check_required;
	@Column
	private String pes_check_status;
	@Column
	private String primary_skills;
	@Column
	private String secondary_skills;
	@Column
	private String resource_type;
	@Column
	private Timestamp pes_completion_date;
	@Column
	private Timestamp cbs_completion_date;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	private String created_by;
	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	@Column
	private String seating_location;
	
	
	
	public String getSeating_location() {
		return seating_location;
	}
	public void setSeating_location(String seating_location) {
		this.seating_location = seating_location;
	}
	public String getPrimary_skills() {
		return primary_skills;
	}
	public void setPrimary_skills(String primary_skills) {
		this.primary_skills = primary_skills;
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
	public Timestamp getCbs_completion_date() {
		return cbs_completion_date;
	}
	public void setCbs_completion_date(Timestamp cbs_completion_date) {
		this.cbs_completion_date = cbs_completion_date;
	}
	public String getPes_check_required() {
		return pes_check_required;
	}
	public void setPes_check_required(String pes_check_required) {
		this.pes_check_required = pes_check_required;
	}
	public String getPes_check_status() {
		return pes_check_status;
	}
	public void setPes_check_status(String pes_check_status) {
		this.pes_check_status = pes_check_status;
	}
	public Timestamp getPes_completion_date() {
		return pes_completion_date;
	}
	public Timestamp getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Timestamp date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public void setPes_completion_date(Timestamp pes_completion_date) {
		this.pes_completion_date = pes_completion_date;
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
	public String getPc_code() {
		return pc_code;
	}
	public void setPc_code(String pc_code) {
		this.pc_code = pc_code;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
	public String getBgv_report_path() {
		return bgv_report_path;
	}
	public void setBgv_report_path(String bgv_report_path) {
		this.bgv_report_path = bgv_report_path;
	}
	public String getBgv_required() {
		return bgv_required;
	}
	public void setBgv_required(String bgv_required) {
		this.bgv_required = bgv_required;
	}
	public String getBgv_completed() {
		return bgv_completed;
	}
	public void setBgv_completed(String bgv_completed) {
		this.bgv_completed = bgv_completed;
	}
	public Timestamp getBgv_completion_date() {
		return bgv_completion_date;
	}
	public void setBgv_completion_date(Timestamp bgv_completion_date) {
		this.bgv_completion_date = bgv_completion_date;
	}
	public String getName_in_chinese_characters() {
		return name_in_chinese_characters;
	}
	public void setName_in_chinese_characters(String name_in_chinese_characters) {
		this.name_in_chinese_characters = name_in_chinese_characters;
	}
	public String getNational_id() {
		return national_id;
	}
	public void setNational_id(String national_id) {
		this.national_id = national_id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getBonus_payable() {
		return bonus_payable;
	}
	public void setBonus_payable(String bonus_payable) {
		this.bonus_payable = bonus_payable;
	}
	public String getAccount_manager() {
		return account_manager;
	}
	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}
		public String getHiring_manager() {
		return hiring_manager;
	}
	public void setHiring_manager(String hiring_manager) {
		this.hiring_manager = hiring_manager;
	}
	public String getBudget_owner() {
		return budget_owner;
	}
	public void setBudget_owner(String budget_owner) {
		this.budget_owner = budget_owner;
	}
	public String getVisa_status() {
		return visa_status;
	}
	public void setVisa_status(String visa_status) {
		this.visa_status = visa_status;
	}
	public String getOnetime_bonus_amount() {
		return onetime_bonus_amount;
	}
	public void setOnetime_bonus_amount(String onetime_bonus_amount) {
		this.onetime_bonus_amount = onetime_bonus_amount;
	}
	public String getEp_path() {
		return ep_path;
	}
	public void setEp_path(String ep_path) {
		this.ep_path = ep_path;
	}
	public String getYears_of_experience_months() {
		return years_of_experience_months;
	}
	public void setYears_of_experience_months(String years_of_experience_months) {
		this.years_of_experience_months = years_of_experience_months;
	}
	public String getYears_of_experience_years() {
		return years_of_experience_years;
	}
	public void setYears_of_experience_years(String years_of_experience_years) {
		this.years_of_experience_years = years_of_experience_years;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public String getPaid_by() {
		return paid_by;
	}
	public void setPaid_by(String paid_by) {
		this.paid_by = paid_by;
	}
	public String getRelevant_experience_years() {
		return relevant_experience_years;
	}
	public void setRelevant_experience_years(String relevant_experience_years) {
		this.relevant_experience_years = relevant_experience_years;
	}
	public String getRelevant_experience_months() {
		return relevant_experience_months;
	}
	public void setRelevant_experience_months(String relevant_experience_months) {
		this.relevant_experience_months = relevant_experience_months;
	}
	public String getRelocation_allowance() {
		return relocation_allowance;
	}
	public void setRelocation_allowance(String relocation_allowance) {
		this.relocation_allowance = relocation_allowance;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public int getOffer_id() {
		return offer_id;
	}
	public void setOffer_id(int offer_id) {
		this.offer_id = offer_id;
	}
	public String getOffer_path() {
		return offer_path;
	}
	public void setOffer_path(String offer_path) {
		this.offer_path = offer_path;
	}
	public String getJob_desc_path() {
		return job_desc_path;
	}
	public void setJob_desc_path(String job_desc_path) {
		this.job_desc_path = job_desc_path;
	}
	
	public Timestamp getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
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
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPersonal_email_id() {
		return personal_email_id;
	}
	public void setPersonal_email_id(String personal_email_id) {
		this.personal_email_id = personal_email_id;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getWork_country() {
		return work_country;
	}
	public void setWork_country(String work_country) {
		this.work_country = work_country;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	public String getClient_group() {
		return client_group;
	}
	public void setClient_group(String client_group) {
		this.client_group = client_group;
	}	
	public String getOffer_declined_reason() {
		return offer_declined_reason;
	}
	public void setOffer_declined_reason(String offer_declined_reason) {
		this.offer_declined_reason = offer_declined_reason;
	}
	
	
	public Timestamp getOffer_made_on() {
		return offer_made_on;
	}
	public void setOffer_made_on(Timestamp offer_made_on) {
		this.offer_made_on = offer_made_on;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public Timestamp getExpected_date_of_joining() {
		return expected_date_of_joining;
	}
	public void setExpected_date_of_joining(Timestamp expected_date_of_joining) {
		this.expected_date_of_joining = expected_date_of_joining;
	}
	public String getOffer_status() {
		return offer_status;
	}
	public void setOffer_status(String offer_status) {
		this.offer_status = offer_status;
	}
	public String getHelius_recruiter() {
		return helius_recruiter;
	}
	public void setHelius_recruiter(String helius_recruiter) {
		this.helius_recruiter = helius_recruiter;
	}
}
