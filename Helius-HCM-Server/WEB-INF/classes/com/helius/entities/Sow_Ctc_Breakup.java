package com.helius.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name="Sow_Ctc_Breakup")
@Audited
public class Sow_Ctc_Breakup {

		@Id
		@GeneratedValue(strategy=GenerationType.AUTO)
		@Column(name="sow_ctc_breakup_id")
		private int sowCtcBreakupId;

		private BigDecimal amount;

		

		@Column(name="employee_id")
		private String employeeId;

		private String frequency;
		
		@Column(name="group_id")
		private int groupId;

		private String status;

		private String currency;

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		@Column
		@UpdateTimestamp
		@NotAudited
		private Timestamp last_modified_date;
		@Column
		//@NotAudited
		private String last_modified_by;
		@Column
		@CreationTimestamp
		private Timestamp create_date;
		@Column
		private String created_by;
		@Column(name="offer_id")
		private int offerId;

		@Column(name="sow_field")
		private String sowFieldName;

		public Sow_Ctc_Breakup() {
		}

		public int getSowCtcBreakupId() {
			return sowCtcBreakupId;
		}

		public void setSowCtcBreakupId(int sowCtcBreakupId) {
			this.sowCtcBreakupId = sowCtcBreakupId;
		}

		public int getGroupId() {
			return groupId;
		}

		public void setGroupId(int groupId) {
			this.groupId = groupId;
		}

		

		public String getStatus() {
			return status;
		}

		
		
		public Timestamp getLast_modified_date() {
			return last_modified_date;
		}

		public void setLast_modified_date(Timestamp last_modified_date) {
			this.last_modified_date = last_modified_date;
		}

		public String getLast_modified_by() {
			return last_modified_by;
		}

		public void setLast_modified_by(String last_modified_by) {
			this.last_modified_by = last_modified_by;
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

		public void setStatus(String status) {
			this.status = status;
		}

		

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public String getEmployeeId() {
			return this.employeeId;
		}

		public void setEmployeeId(String employeeId) {
			this.employeeId = employeeId;
		}

		public String getFrequency() {
			return this.frequency;
		}

		public void setFrequency(String frequency) {
			this.frequency = frequency;
		}

		public int getOfferId() {
			return this.offerId;
		}

		public void setOfferId(int offerId) {
			this.offerId = offerId;
		}

		public String getSowFieldName() {
			return sowFieldName;
		}

		public void setSowFieldName(String sowFieldName) {
			this.sowFieldName = sowFieldName;
		}


	}