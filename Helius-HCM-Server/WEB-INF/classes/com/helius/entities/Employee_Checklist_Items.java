package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the Employee_Checklist_Items database table.
 * 
 */
@Entity
@Table(name="Employee_Checklist_Items")
@Audited
@NamedQuery(name="Employee_Checklist_Items.findAll", query="SELECT e FROM Employee_Checklist_Items e")
public class Employee_Checklist_Items implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="employee_checklist_items_id")
	private int employeeChecklistItemsId;

	@Column(name="checklist_name")
	private String checklistName;

	@Column(name="checklist_type")
	private String checklistType;

	@Column(name="create_date")
	@CreationTimestamp
	private Timestamp createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="employee_id")
	private String employeeId;

	@Column(name="last_modified_by")
	@NotAudited
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	@UpdateTimestamp
	@NotAudited
	private Timestamp lastModifiedDate;

	@Column(name="last_reminder")
	private Timestamp lastReminder;

	@Column(name="offer_id")
	private String offerId;

	private String submited;

	@Column(name="upload_document_path")
	private String uploadDocumentPath;

	public Employee_Checklist_Items() {
	}

	public int getEmployeeChecklistItemsId() {
		return this.employeeChecklistItemsId;
	}

	public void setEmployeeChecklistItemsId(int employeeChecklistItemsId) {
		this.employeeChecklistItemsId = employeeChecklistItemsId;
	}

	public String getChecklistName() {
		return this.checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}

	public String getChecklistType() {
		return this.checklistType;
	}

	public void setChecklistType(String checklistType) {
		this.checklistType = checklistType;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	

	public Timestamp getLastReminder() {
		return lastReminder;
	}

	public void setLastReminder(Timestamp lastReminder) {
		this.lastReminder = lastReminder;
	}

	public String getOfferId() {
		return this.offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getSubmited() {
		return this.submited;
	}

	public void setSubmited(String submited) {
		this.submited = submited;
	}

	public String getUploadDocumentPath() {
		return this.uploadDocumentPath;
	}

	public void setUploadDocumentPath(String uploadDocumentPath) {
		this.uploadDocumentPath = uploadDocumentPath;
	}

}