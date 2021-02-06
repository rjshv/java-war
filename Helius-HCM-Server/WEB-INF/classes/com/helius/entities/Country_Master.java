package com.helius.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.helius.entities.HeliusEntity;

@Entity
@Table(name="Country_Master")
public class Country_Master extends HeliusEntity {
	
	@Id
	@Column
	private String country_code;
	@Column
	private String country_name;
	@Column
	private String nationality;
	@Column
	private String identification_ids;
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getIdentification_ids() {
		return identification_ids;
	}
	public void setIdentification_ids(String identification_ids) {
		this.identification_ids = identification_ids;
	}
	
	

}
