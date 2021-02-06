package com.helius.entities;

public class workpermit {

	public workpermit(String workpermit_name, String[] display_details) {
		super();
		this.workpermit_name = workpermit_name;
		this.display_details = display_details;
	}
	private String workpermit_name;
	private String[] display_details;
	
	public String getWorkpermit_name() {
		return workpermit_name;
	}
	public void setWorkpermit_name(String workpermit_name) {
		this.workpermit_name = workpermit_name;
	}
	public String[] getDisplay_details() {
		return display_details;
	}
	public void setDisplay_details(String[] display_details) {
		this.display_details = display_details;
	}
	

	
	
}
