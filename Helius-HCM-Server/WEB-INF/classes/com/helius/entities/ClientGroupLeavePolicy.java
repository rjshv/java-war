package com.helius.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


	@Entity
	@Table(name="client_group_leave_policy_new")
	public class ClientGroupLeavePolicy {

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column
		private int CLient_gorup_leave_policyid;
		
		@Column
		private int client_group_details_id;
		@Column
		private String typeofleave;
		@Column
		private float number_days;
		@Column
		private String compensatory_off_leave_allowed;
		/**
		 * @return the compensatory_off_leave_allowed
		 */
		public String getCompensatory_off_leave_allowed() {
			return compensatory_off_leave_allowed;
		}


		/**
		 * @param compensatory_off_leave_allowed the compensatory_off_leave_allowed to set
		 */
		public void setCompensatory_off_leave_allowed(String compensatory_off_leave_allowed) {
			this.compensatory_off_leave_allowed = compensatory_off_leave_allowed;
		}


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
		private String sameas_client_policy;


		

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


		/**
		 * @return the cLient_gorup_leave_policyid
		 */
		public int getCLient_gorup_leave_policyid() {
			return CLient_gorup_leave_policyid;
		}


		/**
		 * @param cLient_gorup_leave_policyid the cLient_gorup_leave_policyid to set
		 */
		public void setCLient_gorup_leave_policyid(int cLient_gorup_leave_policyid) {
			CLient_gorup_leave_policyid = cLient_gorup_leave_policyid;
		}


		/**
		 * @return the client_group_details_id
		 */
		public int getClient_group_details_id() {
			return client_group_details_id;
		}


		/**
		 * @param client_group_details_id the client_group_details_id to set
		 */
		public void setClient_group_details_id(int client_group_details_id) {
			this.client_group_details_id = client_group_details_id;
		}


		/**
		 * @return the compensatiry_leave_allowed
		 */
		


		/**
		 * @return the sameas_client_policy
		 */
		public String getSameas_client_policy() {
			return sameas_client_policy;
		}


		/**
		 * @param sameas_client_policy the sameas_client_policy to set
		 */
		public void setSameas_client_policy(String sameas_client_policy) {
			this.sameas_client_policy = sameas_client_policy;
		}
	
}
