package com.helius.dao;

import java.util.List;

import com.helius.entities.Employee;

public class LocalizationAmMapONGivenDate {
	
	private String accountManager;
	
	private String workPermitName;
	
	private List<Employee> employee =  null;

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getWorkPermitName() {
		return workPermitName;
	}

	public void setWorkPermitName(String workPermitName) {
		this.workPermitName = workPermitName;
	}

	public List<Employee> getEmployee() {
		return employee;
	}

	public void setEmployee(List<Employee> employee) {
		this.employee = employee;
	}
 	
	

}
