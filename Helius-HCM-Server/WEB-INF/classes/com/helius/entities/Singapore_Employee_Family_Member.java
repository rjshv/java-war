package com.helius.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name="Singapore_Employee_Family_Member")
@Audited
public class Singapore_Employee_Family_Member {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int singapore_employee_family_member_id;
	@Column
	private String employee_id;
	@Column
	private String member_name;
	@Column
	private Timestamp date_of_birth;
	@Column
	private String relation;
	@Column
	private String visa_status;
	@Column
	private String visa_status_number;
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

	
	public String getVisa_status_number() {
		return visa_status_number;
	}
	public void setVisa_status_number(String visa_status_number) {
		this.visa_status_number = visa_status_number;
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
	public int getSingapore_employee_family_member_id() {
		return singapore_employee_family_member_id;
	}
	public void setSingapore_employee_family_member_id(int singapore_employee_family_member_id) {
		this.singapore_employee_family_member_id = singapore_employee_family_member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	
	public Timestamp getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Timestamp date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getVisa_status() {
		return visa_status;
	}
	public void setVisa_status(String visa_status) {
		this.visa_status = visa_status;
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
}
