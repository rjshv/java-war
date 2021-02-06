package com.helius.entities;

import java.sql.Timestamp;

public class EmployeeChecklistItemAndMandatoryitems {

	private int employeeChecklistItemsId;
	private String checklistName;
	private String checklistType;
	private String employeeId;
	private String offerId;
	private String submited;
	private String uploadDocumentPath;
	private Timestamp lastReminder;
	private String createdBy;
	private Timestamp createDate;
	private String lastModifiedBy;
	private Timestamp lastModifiedDate;
	private String mandatory;
	public int getEmployeeChecklistItemsId() {
		return employeeChecklistItemsId;
	}
	public void setEmployeeChecklistItemsId(int employeeChecklistItemsId) {
		this.employeeChecklistItemsId = employeeChecklistItemsId;
	}
	public String getChecklistName() {
		return checklistName;
	}
	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}
	public String getChecklistType() {
		return checklistType;
	}
	public void setChecklistType(String checklistType) {
		this.checklistType = checklistType;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public String getSubmited() {
		return submited;
	}
	public void setSubmited(String submited) {
		this.submited = submited;
	}
	public String getUploadDocumentPath() {
		return uploadDocumentPath;
	}
	public void setUploadDocumentPath(String uploadDocumentPath) {
		this.uploadDocumentPath = uploadDocumentPath;
	}
	public Timestamp getLastReminder() {
		return lastReminder;
	}
	public void setLastReminder(Timestamp lastReminder) {
		this.lastReminder = lastReminder;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	
}
