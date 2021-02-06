/**
 * 
 */
package com.helius.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tirumala
 * 17-Dec-2018
 */
public class Client_Group_HiringManagerDetails {

	/**
	 * 
	 */
	public Client_Group_HiringManagerDetails() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	
	private String clientname;
	
	private List<String> clientgroups;

	private List<String> hiringmanagers;
	
	private List<String> budgetowners;
	
	private Map<String, List<String>> lobList;

	
	
	public Map<String, List<String>> getLobList() {
		return lobList;
	}

	public void setLobList(Map<String, List<String>> lobList) {
		this.lobList = lobList;
	}

	/**
	 * @return the budgetowners
	 */
	public List<String> getBudgetowners() {
		return budgetowners;
	}

	/**
	 * @param budgetowners the budgetowners to set
	 */
	public void setBudgetowners(List<String> budgetowners) {
		this.budgetowners = budgetowners;
	}

	/**
	 * @return the clientname
	 */
	public String getClientname() {
		return clientname;
	}

	/**
	 * @param clientname the clientname to set
	 */
	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	/**
	 * @return the clientgroups
	 */
	public List<String> getClientgroups() {
		return clientgroups;
	}

	/**
	 * @param clientgroups the clientgroups to set
	 */
	public void setClientgroups(List<String> clientgroups) {
		this.clientgroups = clientgroups;
	}

	/**
	 * @return the hiringmanagers
	 */
	public List<String> getHiringmanagers() {
		return hiringmanagers;
	}

	/**
	 * @param hiringmanagers the hiringmanagers to set
	 */
	public void setHiringmanagers(List<String> hiringmanagers) {
		this.hiringmanagers = hiringmanagers;
	}
	
	
	
}
