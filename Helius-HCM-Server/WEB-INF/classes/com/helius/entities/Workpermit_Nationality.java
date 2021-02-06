package com.helius.entities;

import java.util.ArrayList;
import java.util.List;

public class Workpermit_Nationality {
	
	public Workpermit_Nationality(String nationality, List<Workpermit_Worklocation> worklocation) {
		super();
		this.nationality = nationality;
		this.worklocation = worklocation;
	}

	String nationality;
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	List<Workpermit_Worklocation> worklocation;

	

	public List<Workpermit_Worklocation> getWorklocation() {
		return worklocation;
	}

	public void setWorklocation(List<Workpermit_Worklocation> worklocation) {
		this.worklocation = worklocation;
	}
	
	public void addworkpermit_worklocation(Workpermit_Worklocation ww) {
		if(worklocation == null ) {
			worklocation = new ArrayList<Workpermit_Worklocation>();
			
		}
		
		worklocation.add(ww);
	}

}
