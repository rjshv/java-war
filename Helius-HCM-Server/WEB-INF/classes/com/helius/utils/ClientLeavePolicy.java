package com.helius.utils;

import java.sql.Timestamp;



	
	public class ClientLeavePolicy {

		private int clientLeavePolicyId=0;
		
		 
		/**
		 * @return the clientLeavePolicyId
		 */
		public int getClientLeavePolicyId() {
			return clientLeavePolicyId;
		}


		/**
		 * @param clientLeavePolicyId the clientLeavePolicyId to set
		 */
		public void setClientLeavePolicyId(int clientLeavePolicyId) {
			this.clientLeavePolicyId = clientLeavePolicyId;
		}


		/**
		 * @return the clientId
		 */
		public int getClientId() {
			return clientId;
		}


		/**
		 * @param clientId the clientId to set
		 */
		public void setClientId(int clientId) {
			this.clientId = clientId;
		}


		private int clientId;
		 
		private String typeofleave;
		 
		private float number_days;
		 
		private String compensatory_off_leave_allowed;
		 
		private String customerSOW_payforemployeeleaves;
		 

		private Timestamp create_date;

		 
		private String created_by;
		
		 

		private String last_modified_by;

		 

		private Timestamp last_modified_date;

		 
		private String leave_encashments;

		
		 
		private String same_as_helius_policy;


		public String getTypeofleave() {
			return typeofleave;
		}


		public void setTypeofleave(String typeofleave) {
			this.typeofleave = typeofleave;
		}


		public float getNumber_days() {
			return number_days;
		}


		public void setNumber_days(float number_days) {
			this.number_days = number_days;
		}


		public String getCompensatory_off_leave_allowed() {
			return compensatory_off_leave_allowed;
		}


		public void setCompensatory_off_leave_allowed(String compensatory_off_leave_allowed) {
			this.compensatory_off_leave_allowed = compensatory_off_leave_allowed;
		}


		public String getCustomerSOW_payforemployeeleaves() {
			return customerSOW_payforemployeeleaves;
		}


		public void setCustomerSOW_payforemployeeleaves(String customerSOW_payforemployeeleaves) {
			this.customerSOW_payforemployeeleaves = customerSOW_payforemployeeleaves;
		}


		public Timestamp getCreate_date() {
			return create_date;
		}


		public void setCreate_date(Timestamp create_date) {
			this.create_date = create_date;
		}


		public String getCreated_by() {
			return created_by;
		}


		public void setCreated_by(String created_by) {
			this.created_by = created_by;
		}


		public String getLast_modified_by() {
			return last_modified_by;
		}


		public void setLast_modified_by(String last_modified_by) {
			this.last_modified_by = last_modified_by;
		}


		public Timestamp getLast_modified_date() {
			return last_modified_date;
		}


		public void setLast_modified_date(Timestamp last_modified_date) {
			this.last_modified_date = last_modified_date;
		}


		public String getLeave_encashments() {
			return leave_encashments;
		}


		public void setLeave_encashments(String leave_encashments) {
			this.leave_encashments = leave_encashments;
		}


		public String getSame_as_helius_policy() {
			return same_as_helius_policy;
		}


		public void setSame_as_helius_policy(String same_as_helius_policy) {
			this.same_as_helius_policy = same_as_helius_policy;
		}


		

	
	
}