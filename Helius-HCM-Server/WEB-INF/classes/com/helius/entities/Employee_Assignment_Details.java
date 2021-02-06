/**
 * 
 */
package com.helius.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
/**
 * @author Tirumala
 * 22-Feb-2018
 */

@Entity
@Table(name="Employee_Assignment_Details")
@Audited
public class Employee_Assignment_Details {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int employee_assignment_details_id;
	@Column
	private String employee_id;
	@Column
	private String client;
	@Column
	private String client_group;
	@Column
	private String client_email_id;
	@Column
	private String helius_email_id;
	@Column
	private String client_hiring_manager;
	@Column
	private String pes_checks;
	@Column	
	private Timestamp pes_completion_date;
	@Column
	private Timestamp sow_expiry_date;
	@Column
	private Timestamp sow_start_date;
	@Column
	private Timestamp po_date;
	@Column
	private String po_number;
	@Column
	private String claim_reimbursement;
	@Column
	private String renewal_sow_confirmed;
	@Column
	private String timesheet_type;
	@Column
	private String lob;
	@Column
	private String assign_employee_to_sow;
	@Column
	private String account_manager;
	@Column
	private String quiz_scores;
	@Column
	private String outside_appointment;
	@Column
	private String outside_appointment_comments;
	@Column	
	private Timestamp admic_check_doc_uploaded_date;
	@Column	
	private Timestamp admic_check_doc_received_date;
	@Column
	@UpdateTimestamp
	@NotAudited
	private Timestamp last_modified_date;
	@Column
	@NotAudited
	private String last_modified_by;
	@Column
	@CreationTimestamp
	private Timestamp create_date;
	@Column
	private String created_by;
	
	
	
	public String getOutside_appointment_comments() {
		return outside_appointment_comments;
	}


	public void setOutside_appointment_comments(String outside_appointment_comments) {
		this.outside_appointment_comments = outside_appointment_comments;
	}


	public String getLob() {
		return lob;
	}


	public void setLob(String lob) {
		this.lob = lob;
	}


	public String getOutside_appointment() {
		return outside_appointment;
	}


	public void setOutside_appointment(String outside_appointment) {
		this.outside_appointment = outside_appointment;
	}


	public String getQuiz_scores() {
		return quiz_scores;
	}


	public void setQuiz_scores(String quiz_scores) {
		this.quiz_scores = quiz_scores;
	}


	public Timestamp getAdmic_check_doc_uploaded_date() {
		return admic_check_doc_uploaded_date;
	}


	public void setAdmic_check_doc_uploaded_date(Timestamp admic_check_doc_uploaded_date) {
		this.admic_check_doc_uploaded_date = admic_check_doc_uploaded_date;
	}


	public Timestamp getAdmic_check_doc_received_date() {
		return admic_check_doc_received_date;
	}


	public void setAdmic_check_doc_received_date(Timestamp admic_check_doc_received_date) {
		this.admic_check_doc_received_date = admic_check_doc_received_date;
	}


	public Timestamp getPes_completion_date() {
		return pes_completion_date;
	}


	public void setPes_completion_date(Timestamp pes_completion_date) {
		this.pes_completion_date = pes_completion_date;
	}


	public String getPes_checks() {
		return pes_checks;
	}


	public void setPes_checks(String pes_checks) {
		this.pes_checks = pes_checks;
	}


	public String getClient_hiring_manager() {
		return client_hiring_manager;
	}


	public void setClient_hiring_manager(String client_hiring_manager) {
		this.client_hiring_manager = client_hiring_manager;
	}


	public String getAccount_manager() {
		return account_manager;
	}


	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}


	public String getClient_email_id() {
		return client_email_id;
	}


	public void setClient_email_id(String client_email_id) {
		this.client_email_id = client_email_id;
	}

	public String getHelius_email_id() {
		return helius_email_id;
	}


	public void setHelius_email_id(String helius_email_id) {
		this.helius_email_id = helius_email_id;
	}

	public Timestamp getSow_expiry_date() {
		return sow_expiry_date;
	}


	public void setSow_expiry_date(Timestamp sow_expiry_date) {
		this.sow_expiry_date = sow_expiry_date;
	}


	public Timestamp getSow_start_date() {
		return sow_start_date;
	}


	public void setSow_start_date(Timestamp sow_start_date) {
		this.sow_start_date = sow_start_date;
	}


	public Timestamp getPo_date() {
		return po_date;
	}


	public void setPo_date(Timestamp po_date) {
		this.po_date = po_date;
	}


	public String getPo_number() {
		return po_number;
	}


	public void setPo_number(String po_number) {
		this.po_number = po_number;
	}


	public String getClaim_reimbursement() {
		return claim_reimbursement;
	}


	public void setClaim_reimbursement(String claim_reimbursement) {
		this.claim_reimbursement = claim_reimbursement;
	}

	public String getRenewal_sow_confirmed() {
		return renewal_sow_confirmed;
	}


	public void setRenewal_sow_confirmed(String renewal_sow_confirmed) {
		this.renewal_sow_confirmed = renewal_sow_confirmed;
	}


	public Timestamp getLast_modified_date() {
		return last_modified_date;
	}


	public void setLast_modified_date(Timestamp last_modified_date) {
		this.last_modified_date = last_modified_date;
	}


	public Timestamp getCreate_date() {
		return create_date;
	}


	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}


	public String getLast_modified_by() {
		return last_modified_by;
	}


	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}

	public String getCreated_by() {
		return created_by;
	}


	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}


	

	public int getEmployee_assignment_details_id() {
		return employee_assignment_details_id;
	}


	public void setEmployee_assignment_details_id(int employee_assignment_details_id) {
		this.employee_assignment_details_id = employee_assignment_details_id;
	}


	public String getEmployee_id() {
		return employee_id;
	}


	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}


	public String getClient() {
		return client;
	}


	public void setClient(String client) {
		this.client = client;
	}
	
	public String getClient_group() {
		return client_group;
	}


	public void setClient_group(String client_group) {
		this.client_group = client_group;
	}



	public String getTimesheet_type() {
		return timesheet_type;
	}


	public void setTimesheet_type(String timesheet_type) {
		this.timesheet_type = timesheet_type;
	}


	public String getAssign_employee_to_sow() {
		return assign_employee_to_sow;
	}


	public void setAssign_employee_to_sow(String assign_employee_to_sow) {
		this.assign_employee_to_sow = assign_employee_to_sow;
	}


	/* (non-Javadoc)
	 * @see com.helius.entities.IEntity#toJSON(
	 */
	//@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
