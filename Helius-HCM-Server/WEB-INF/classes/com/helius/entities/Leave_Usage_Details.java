package com.helius.entities;

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
@Audited
@Table(name="Leave_Usage_Details")
public class Leave_Usage_Details {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int leave_usage_details_id;
	
	@Column
	private String employee_id;
	@Column
	private int client_id;
	
	public int getClient_id() {
		return client_id;
	}

	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	@Column
	private String type_of_leave;
	
	public String getType_of_leave() {
		return type_of_leave;
	}

	public void setType_of_leave(String type_of_leave) {
		this.type_of_leave = type_of_leave;
	}
	@Column
	private Timestamp usageMonth;
	
	@Column
	private float leaves_accrued;
	
	@Column
	private float leaves_used;
	
	@Column
	@CreationTimestamp
	private Timestamp create_date;

	@Column
	private String created_by;
	
	@Column
	@NotAudited
	private String last_modified_by;

	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;

	public int getLeave_usage_details_id() {
		return leave_usage_details_id;
	}

	public void setLeave_usage_details_id(int leave_usage_details_id) {
		this.leave_usage_details_id = leave_usage_details_id;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public float getLeaves_accrued() {
		return leaves_accrued;
	}

	public void setLeaves_accrued(float leaves_accrued) {
		this.leaves_accrued = leaves_accrued;
	}

	public float getLeaves_used() {
		return leaves_used;
	}

	public void setLeaves_used(float leaves_used) {
		this.leaves_used = leaves_used;
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

	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}

	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public Timestamp getUsageMonth() {
		return usageMonth;
	}

	public void setUsageMonth(Timestamp usageMonth) {
		this.usageMonth = usageMonth;
	}


}
