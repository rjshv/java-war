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
@Table(name="Leave_Record_Details")
public class Leave_Record_Details {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int leave_record_details_id;
	
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
	public int getLeave_record_details_id() {
		return leave_record_details_id;
	}

	public void setLeave_record_details_id(int leave_record_details_id) {
		this.leave_record_details_id = leave_record_details_id;
	}
	@Column
	private String type_of_leave;
	@Column
	private String ampm;
	@Column
	private String remarks;
	@Column
	private Timestamp leaveMonth;
	@Column
	private Timestamp startdate;
	@Column
	private Timestamp enddate;
	@Column
	private float leaves_used;	
	@Column
	private String leaveRecordPath;
	public String getLeaveRecordPath() {
		return leaveRecordPath;
	}

	public void setLeaveRecordPath(String leaveRecordPath) {
		this.leaveRecordPath = leaveRecordPath;
	}
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
	
	public String getAmpm() {
		return ampm;
	}

	public void setAmpm(String ampm) {
		this.ampm = ampm;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Timestamp getLeaveMonth() {
		return leaveMonth;
	}

	public void setLeaveMonth(Timestamp leaveMonth) {
		this.leaveMonth = leaveMonth;
	}

	public String getType_of_leave() {
		return type_of_leave;
	}

	public void setType_of_leave(String type_of_leave) {
		this.type_of_leave = type_of_leave;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public Timestamp getStartdate() {
		return startdate;
	}

	public void setStartdate(Timestamp startdate) {
		this.startdate = startdate;
	}

	public Timestamp getEnddate() {
		return enddate;
	}

	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
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



}
