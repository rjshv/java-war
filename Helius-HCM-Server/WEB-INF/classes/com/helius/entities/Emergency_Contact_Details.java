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
@Table(name="Emergency_Contact_Details")
@Audited
public class Emergency_Contact_Details {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int emergency_contact_details_id;
	@Column
	private String employee_id;
	@Column
	private String contact_person;
	@Column
	private String employee_blood_group;
	@Column
	private String relation_with_employee;
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
	
	
	
	public String getRelation_with_employee() {
		return relation_with_employee;
	}
	public void setRelation_with_employee(String relation_with_employee) {
		this.relation_with_employee = relation_with_employee;
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
	public int getEmergency_contact_details_id() {
		return emergency_contact_details_id;
	}
	public void setEmergency_contact_details_id(int emergency_contact_details_id) {
		this.emergency_contact_details_id = emergency_contact_details_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getContact_person() {
		return contact_person;
	}
	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}
	public String getEmployee_blood_group() {
		return employee_blood_group;
	}
	public void setEmployee_blood_group(String employee_blood_group) {
		this.employee_blood_group = employee_blood_group;
	}
}
