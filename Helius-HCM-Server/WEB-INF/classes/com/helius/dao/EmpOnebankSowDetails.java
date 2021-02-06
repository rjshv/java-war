package com.helius.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class EmpOnebankSowDetails {
	
		@Column
		private String employee_id;
		@Column
		private Timestamp actual_date_of_joining;
		@Column
		private Timestamp relieving_date;
		@Column
		private String onebankId;
		@Column
		private String po_number;
		@Column
		private String pc_code;
		@Column
		private String sow_type;
		@Column
		private Timestamp po_start_date;
		@Column
		private Timestamp po_end_date;
		@Column
		private String sow_rate_for_unit;
		@Column
		private String current_monthly_basic;
		@Column
		private String new_monthly_basic;
		@Column
		private String account_manager;
		@Column
		private String helius_recruiter;
		
		
		
		public String getPc_code() {
			return pc_code;
		}
		public void setPc_code(String pc_code) {
			this.pc_code = pc_code;
		}
		public String getEmployee_id() {
			return employee_id;
		}
		public void setEmployee_id(String employee_id) {
			this.employee_id = employee_id;
		}
		public String getOnebankId() {
			return onebankId;
		}
		public void setOnebankId(String onebankId) {
			this.onebankId = onebankId;
		}
		
		public String getSow_type() {
			return sow_type;
		}
		public void setSow_type(String sow_type) {
			this.sow_type = sow_type;
		}
		public String getCurrent_monthly_basic() {
			return current_monthly_basic;
		}
		public void setCurrent_monthly_basic(String current_monthly_basic) {
			this.current_monthly_basic = current_monthly_basic;
		}
		public String getPo_number() {
			return po_number;
		}
		public void setPo_number(String po_number) {
			this.po_number = po_number;
		}
		
		public Timestamp getPo_start_date() {
			return po_start_date;
		}
		public void setPo_start_date(Timestamp po_start_date) {
			this.po_start_date = po_start_date;
		}
		public Timestamp getPo_end_date() {
			return po_end_date;
		}
		public void setPo_end_date(Timestamp po_end_date) {
			this.po_end_date = po_end_date;
		}
		public String getSow_rate_for_unit() {
			return sow_rate_for_unit;
		}
		public void setSow_rate_for_unit(String sow_rate_for_unit) {
			this.sow_rate_for_unit = sow_rate_for_unit;
		}
		public Timestamp getActual_date_of_joining() {
			return actual_date_of_joining;
		}
		public void setActual_date_of_joining(Timestamp actual_date_of_joining) {
			this.actual_date_of_joining = actual_date_of_joining;
		}
		public Timestamp getRelieving_date() {
			return relieving_date;
		}
		public void setRelieving_date(Timestamp relieving_date) {
			this.relieving_date = relieving_date;
		}
		public String getNew_monthly_basic() {
			return new_monthly_basic;
		}
		public void setNew_monthly_basic(String new_monthly_basic) {
			this.new_monthly_basic = new_monthly_basic;
		}
		public String getAccount_manager() {
			return account_manager;
		}
		public void setAccount_manager(String account_manager) {
			this.account_manager = account_manager;
		}
		public String getHelius_recruiter() {
			return helius_recruiter;
		}
		public void setHelius_recruiter(String helius_recruiter) {
			this.helius_recruiter = helius_recruiter;
		}
		
		
}
