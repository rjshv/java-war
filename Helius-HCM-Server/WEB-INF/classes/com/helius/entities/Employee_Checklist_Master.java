package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
//import org.hibernate.envers.Audited;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.sql.Timestamp;


/**
 * The persistent class for the Employee_Checklist_Master database table.
 * 
 */
@Entity
@Audited
@NamedQuery(name="Employee_Checklist_Master.findAll", query="SELECT e FROM Employee_Checklist_Master e")
public class Employee_Checklist_Master implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="employee_checklist_master_id")
	private int employeeChecklistMasterId;

	private String active;
	@Column(name="checklist_description")
	private String checklistDescription;

	@Column(name="checklist_name")
	private String checklistName;

	@Column(name="checklist_type")
	private String checklistType;

	@Column(name="client_specific")
	private String clientSpecific;

	@Column(name="create_date")
	@CreationTimestamp
	private Timestamp createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="last_modified_by")
	@NotAudited
	private String lastModifiedBy;

	@Column(name="last_modified_date")
	@UpdateTimestamp
	@NotAudited
	private Timestamp lastModifiedDate;

	private String mandatory;

	@Column(name="nationality_applicable")
	private String nationalityApplicable;

	@Column(name="region_applicable")
	private String regionApplicable;

	@Column(name="template_path")
	private String templatePath;

	public Employee_Checklist_Master() {
	}

	public int getEmployeeChecklistMasterId() {
		return this.employeeChecklistMasterId;
	}

	public void setEmployeeChecklistMasterId(int employeeChecklistMasterId) {
		this.employeeChecklistMasterId = employeeChecklistMasterId;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getChecklistDescription() {
		return this.checklistDescription;
	}

	public void setChecklistDescription(String checklistDescription) {
		this.checklistDescription = checklistDescription;
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

	public String getClientSpecific() {
		return this.clientSpecific;
	}

	public void setClientSpecific(String clientSpecific) {
		this.clientSpecific = clientSpecific;
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

	public String getMandatory() {
		return this.mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getNationalityApplicable() {
		return this.nationalityApplicable;
	}

	public void setNationalityApplicable(String nationalityApplicable) {
		this.nationalityApplicable = nationalityApplicable;
	}

	
	public String getRegionApplicable() {
		return regionApplicable;
	}

	public void setRegionApplicable(String regionApplicable) {
		this.regionApplicable = regionApplicable;
	}

	public String getTemplatePath() {
		return this.templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

}