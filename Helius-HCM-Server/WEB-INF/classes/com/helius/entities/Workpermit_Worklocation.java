package com.helius.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Workpermit_Worklocation {
	
	
	private String nationality = "";
	private String worklocation = "";
	public Workpermit_Worklocation(String nationality, String worklocation) {
		super();
		this.nationality = nationality;
		this.worklocation = worklocation;
	}
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
	List<workpermit> workpermits;
	public List<workpermit> getWorkpermits() {
		return workpermits;
	}
	public void setWorkpermits(List<workpermit> workpermits) {
		this.workpermits = workpermits;
	}

	public Set<String> getIds() {
		return ids;
	}
	public void setIds(Set<String> ids) {
		this.ids = ids;
	}
	Set<String> ids;

	public void addworkpermit(workpermit wp) {
		if( workpermits == null) {
			workpermits = new ArrayList<workpermit>();
		}	
		workpermits.add(wp);
	}
	
	public void addid(String id) {
		if( ids == null) {
			ids =  new HashSet<String>();
		}		
		ids.add(id);
	}
}
