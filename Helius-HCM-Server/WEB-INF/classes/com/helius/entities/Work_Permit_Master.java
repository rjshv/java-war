package com.helius.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Work_Permit_Master")
public class Work_Permit_Master extends HeliusEntity{
	
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int work_permit_master_id;
	@Column
	private String country_code;
	@Column
	private String work_location;
	@Column
	private String work_permit_name;
	@Column
	private String display_fields;
	
	public String getWork_permit_name() {
		return work_permit_name;
	}
	public void setWork_permit_name(String work_permit_name) {
		this.work_permit_name = work_permit_name;
	}
	/*@ManyToOne 
	@JoinColumn(name = "country_code") 
	private Country_Master countryMaster;
	
	public Country_Master getCountryMaster() {
		return countryMaster;
	}
	public void setCountryMaster(Country_Master countryMaster) {
		this.countryMaster = countryMaster;
	}*/
	
	public int getWork_permit_master_id() {
		return work_permit_master_id;
	}
	public void setWork_permit_master_id(int work_permit_master_id) {
		this.work_permit_master_id = work_permit_master_id;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getWork_location() {
		return work_location;
	}
	public void setWork_location(String work_location) {
		this.work_location = work_location;
	}
	
	public String getDisplay_fields() {
		return display_fields;
	}
	public void setDisplay_fields(String display_fields) {
		this.display_fields = display_fields;
	}
	

}
