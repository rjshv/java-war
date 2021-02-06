package com.helius.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


	@Entity
	@Table(name="Client_Leave_Policy_New")
	public class Client_Leave_Policy_New  {

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column
		private int client_leave_policy_id;
		
		@Column
		private int client_id;
		@Column
		private String typeofleave;
		@Column
		private float number_days;
		@Column
		private String compensatory_off_leave_allowed;
		@Column
		private String customerSOW_payforemployeeleaves;
		@Column
//		@CreationTimestamp
		private Timestamp create_date;

		@Column
		private String created_by;
		
		@Column
//		@NotAudited
		private String last_modified_by;

		@Column
//		@UpdateTimestamp
//		@NotAudited
		private Timestamp last_modified_date;

		@Column
		private String leave_encashments;

		
		@Column
		private String same_as_helius_policy;


		public int getClient_leave_policy_id() {
			return client_leave_policy_id;
		}


		public void setClient_leave_policy_id(int client_leave_policy_id) {
			this.client_leave_policy_id = client_leave_policy_id;
		}


		public int getClient_id() {
			return client_id;
		}


		public void setClient_id(int client_id) {
			this.client_id = client_id;
		}


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
