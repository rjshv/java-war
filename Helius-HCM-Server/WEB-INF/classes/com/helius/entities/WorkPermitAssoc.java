package com.helius.entities;

import java.util.List;



public class WorkPermitAssoc {
	
	
	private String nationality;
	private String worklocation;
	private List<workpermit> workpermits = null;
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getWorklocation() {
		return worklocation;
	}
	public void setWorklocation(String worklocation) {
		this.worklocation = worklocation;
	}
	public List<workpermit> getWorkpermits() {
		return workpermits;
	}
	public void setWorkpermits(List<workpermit> workpermits) {
		this.workpermits = workpermits;
	}
	

}
