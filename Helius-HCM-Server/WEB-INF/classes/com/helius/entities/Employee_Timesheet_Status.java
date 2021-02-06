package com.helius.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * The persistent class for the Employee_Timesheet_Status database table. 
 */
@Entity
@Audited
@NamedQuery(name="Employee_Timesheet_Status.findAll", query="SELECT e FROM Employee_Timesheet_Status e")
public class Employee_Timesheet_Status implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="employee_timesheet_status_id")
	private int employeeTimesheetStatusId;

	private String comments;

	@Column(name="date_of_final_submission")
	private Timestamp dateOfFinalSubmission;

	@Column(name="date_of_receipt")
	private Timestamp dateOfReceipt;

	@Column(name="employee_id")
	private String employeeId;

	@Column(name="employee_name")
	private String employeeName;
	
	@Column(name="lop_type")
	private String lopType;
	
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

	@Column(name="manager_approval_document_path")
	private String managerApprovalDocumentPath;

	@Column(name="supporting_upload_doument_path")
	private String supportingUploadDoumentPath;

	@Column(name="timesheet_email")
	private String timesheetEmail;
	
	@Column(name="sets")
	private String sets;
	
	@Column(name="salaryProcessingStatus")
	private boolean salaryProcessingStatus;

	@Column(name="salaryProcessedDate")
	private Timestamp salaryProcessedDate;
	
	@Column(name="timesheet_error")
	private String timesheetError;

	@Column(name="timesheet_month")
	private Timestamp timesheetMonth;

	@Column(name="timesheet_upload_path")
	private String timesheetUploadPath;
	
	@Column(name="lop_days")
	private float lopDays;

	public Employee_Timesheet_Status() {
	}

	public int getEmployeeTimesheetStatusId() {
		return this.employeeTimesheetStatusId;
	}

	public void setEmployeeTimesheetStatusId(int employeeTimesheetStatusId) {
		this.employeeTimesheetStatusId = employeeTimesheetStatusId;
	}

	public float getLopDays() {
		return lopDays;
	}

	public void setLopDays(float lopDays) {
		this.lopDays = lopDays;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getLopType() {
		return lopType;
	}

	public void setLopType(String lopType) {
		this.lopType = lopType;
	}

	public Timestamp getDateOfFinalSubmission() {
		return this.dateOfFinalSubmission;
	}

	public void setDateOfFinalSubmission(Timestamp dateOfFinalSubmission) {
		this.dateOfFinalSubmission = dateOfFinalSubmission;
	}

	public boolean isSalaryProcessingStatus() {
		return salaryProcessingStatus;
	}

	public void setSalaryProcessingStatus(boolean salaryProcessingStatus) {
		this.salaryProcessingStatus = salaryProcessingStatus;
	}

	public Timestamp getSalaryProcessedDate() {
		return salaryProcessedDate;
	}

	public void setSalaryProcessedDate(Timestamp salaryProcessedDate) {
		this.salaryProcessedDate = salaryProcessedDate;
	}

	public Timestamp getDateOfReceipt() {
		return this.dateOfReceipt;
	}

	public void setDateOfReceipt(Timestamp dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	public String getSets() {
		return sets;
	}

	public void setSets(String sets) {
		this.sets = sets;
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

	public String getManagerApprovalDocumentPath() {
		return this.managerApprovalDocumentPath;
	}

	public void setManagerApprovalDocumentPath(String managerApprovalDocumentPath) {
		this.managerApprovalDocumentPath = managerApprovalDocumentPath;
	}

	public String getSupportingUploadDoumentPath() {
		return this.supportingUploadDoumentPath;
	}

	public void setSupportingUploadDoumentPath(String supportingUploadDoumentPath) {
		this.supportingUploadDoumentPath = supportingUploadDoumentPath;
	}

	public String getTimesheetEmail() {
		return this.timesheetEmail;
	}

	public void setTimesheetEmail(String timesheetEmail) {
		this.timesheetEmail = timesheetEmail;
	}

	public String getTimesheetError() {
		return this.timesheetError;
	}

	public void setTimesheetError(String timesheetError) {
		this.timesheetError = timesheetError;
	}

	public Timestamp getTimesheetMonth() {
		return this.timesheetMonth;
	}

	public void setTimesheetMonth(Timestamp timesheetMonth) {
		this.timesheetMonth = timesheetMonth;
	}

	public String getTimesheetUploadPath() {
		return this.timesheetUploadPath;
	}

	public void setTimesheetUploadPath(String timesheetUploadPath) {
		this.timesheetUploadPath = timesheetUploadPath;
	}

}