/**
 * 
 */
package com.helius.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tirumala
 * 05-Nov-2018
 */
public class FilecopyStatus extends Status {

	
	private List<String> copied_with_success = new ArrayList<String>();
	/**
	 * @return the copied_with_success
	 */
	public List<String> getCopied_with_success() {
		return copied_with_success;
	}
	/**
	 * @param copied_with_success the copied_with_success to set
	 */
	public void setCopied_with_success(List<String> copied_with_success) {
		this.copied_with_success = copied_with_success;
	}
	/**
	 * 
	 */
	public FilecopyStatus() {
		// TODO Auto-generated constructor stub
	}
	
	

}
