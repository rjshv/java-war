package com.helius.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


/**
 * The persistent class for the Sow_Employee_Association database table.
 * 
 */
@Entity
@NamedQuery(name="Sow_Employee_Association.findAll", query="SELECT s FROM Sow_Employee_Association s")
@Audited
public class Sow_Employee_Association implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="association_id")
	private int associationId;
	@Column
	private int offer_id;
	
	@Column(name="employee_id")
	private String employeeId;

	@Column(name="sow_client_reference_number")
	private String sowClientReferenceNumber;
	@Column(name="helius_reference_number")
	private String heliusReferenceNumber;

	/**
	 * @return the heliusReferenceNumber
	 */
	public String getHeliusReferenceNumber() {
		return heliusReferenceNumber;
	}

	/**
	 * @param heliusReferenceNumber the heliusReferenceNumber to set
	 */
	public void setHeliusReferenceNumber(String heliusReferenceNumber) {
		this.heliusReferenceNumber = heliusReferenceNumber;
	}

	@Column(name="sow_details_id")
	private int sowDetailsId;

	private String status;

	public int getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(int offer_id) {
		this.offer_id = offer_id;
	}

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

	public Sow_Employee_Association() {
	}

	public int getAssociationId() {
		return this.associationId;
	}

	public void setAssociationId(int associationId) {
		this.associationId = associationId;
	}


	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getSowClientReferenceNumber() {
		return this.sowClientReferenceNumber;
	}

	public void setSowClientReferenceNumber(String sowClientReferenceNumber) {
		this.sowClientReferenceNumber = sowClientReferenceNumber;
	}

	public int getSowDetailsId() {
		return this.sowDetailsId;
	}

	public void setSowDetailsId(int sowDetailsId) {
		this.sowDetailsId = sowDetailsId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}