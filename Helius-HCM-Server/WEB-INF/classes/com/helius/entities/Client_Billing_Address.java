package com.helius.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Client_Billing_Address database table.
 * 
 */
@Entity
@NamedQuery(name="Client_Billing_Address.findAll", query="SELECT c FROM Client_Billing_Address c")
public class Client_Billing_Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="client_billing_address_id")
	private int clientBillingAddressId;

	private String address2;

	private String adrress1;

	private String city;

	private String country;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="last_modified_by")
	private String lastModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_modified_date")
	private Date lastModifiedDate;

	private String pincode;

	private String state;

	@ManyToOne
	@JoinColumn(name="client_id", insertable=false, updatable=false )
	private Client_Detail clientDetail;

	public Client_Billing_Address() {
	}

	public int getClientBillingAddressId() {
		return this.clientBillingAddressId;
	}

	public void setClientBillingAddressId(int clientBillingAddressId) {
		this.clientBillingAddressId = clientBillingAddressId;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAdrress1() {
		return this.adrress1;
	}

	public void setAdrress1(String adrress1) {
		this.adrress1 = adrress1;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
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

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Client_Detail getClientDetail() {
		return this.clientDetail;
	}

	public void setClientDetail(Client_Detail clientDetail) {
		this.clientDetail = clientDetail;
	}

}