/**
 * 
 */
package com.helius.utils;

import java.util.List;

/**
 * @author Tirumala
 * 17-Dec-2018
 */
public class AllClientDetails {

	/**
	 * 
	 */
	public AllClientDetails() {
		// TODO Auto-generated constructor stub
	}
	
	private List<String> allClients;
	/**
	 * @return the allClients
	 */
	public List<String> getAllClients() {
		return allClients;
	}
	/**
	 * @param allClients the allClients to set
	 */
	public void setAllClients(List<String> allClients) {
		this.allClients = allClients;
	}
	/**
	 * @return the client_Group_HiringManagerDetails_List
	 */
	public List<Client_Group_HiringManagerDetails> getClient_Group_HiringManagerDetails_List() {
		return client_Group_HiringManagerDetails_List;
	}
	/**
	 * @param client_Group_HiringManagerDetails_List the client_Group_HiringManagerDetails_List to set
	 */
	public void setClient_Group_HiringManagerDetails_List(
			List<Client_Group_HiringManagerDetails> client_Group_HiringManagerDetails_List) {
		this.client_Group_HiringManagerDetails_List = client_Group_HiringManagerDetails_List;
	}

	private List<Client_Group_HiringManagerDetails>  client_Group_HiringManagerDetails_List;

}
