package com.helius.dao;

public class EmployeeExitCheck {
	
	private boolean employeeexit_changed;
	private boolean employeerelievingdate_changed;
	
	public boolean isEmployeeexit_changed() {
		return employeeexit_changed;
	}
	public void setEmployeeexit_changed(boolean employeeexit_changed) {
		this.employeeexit_changed = employeeexit_changed;
	}
	public boolean isEmployeerelievingdate_changed() {
		return employeerelievingdate_changed;
	}
	public void setEmployeerelievingdate_changed(boolean employeerelievingdate_changed) {
		this.employeerelievingdate_changed = employeerelievingdate_changed;
	}
	
}
