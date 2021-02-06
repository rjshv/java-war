/**
 * 
 */
package com.helius.utils;

/**
 * @author Tirumala
 * 07-Mar-2018
 */
public class Status {

	
	private boolean ok = false;
	private String message = "";
	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}
	/**
	 * @param ok the ok to set
	 */
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * 
	 */
	public Status() {
		// TODO Auto-generated constructor stub
	}
	
	public Status(boolean ok, String message) {
		this.ok = ok;
		this.message = message;
	}

}
