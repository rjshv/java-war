/**
 * 
 */
package com.helius.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.helius.dao.EmployeeExitCheck;

/**
 * @author Tirumala
 * 22-Feb-2018
 */
public class Employee extends HeliusEntity {

	
	private Employee_Personal_Details employeePersonalDetails;
	private Employee_Salary_Details employeeSalaryDetails;
	private Employee_Work_Permit_Details employeeWorkPermitDetails;
	private List<Employee_Professional_Details> employeeProfessionalDetails;
//	private Employee_Professional_Details employeeProfessionalDetails;
	private Employee_Assignment_Details employeeAssignmentDetails;
	private List<Sow_Ctc_Breakup> sowCtcBreakup;
	private Collection<List<Sow_Ctc_Breakup>> sowCtcBreakupHistory;
	private List<Sow_Employee_Association> sowEmployeeAssoc;
	private List<Contact_Address_Details> employeeContactAddressDetails;
	private Emergency_Contact_Details emergencyContactDetails;
	private Employee_Appraisal_Details employeeAppraisalDetails;
	private Employee_Bank_Details employeeBankDetails;
	private Employee_Offer_Details employeeOfferDetails;
	private Employee_Terms_And_Conditions employeeTermsAndConditions;
	private List<Leave_Eligibility_Details> leavesEligibility;
	private Indian_Employees_Insurance_Details indianEmployeesInsuranceDetails;
	private Singapore_Employee_Insurance_Details singaporeEmployeeInsuranceDetails;
	private List<Skill> skills;
	private List<Employee_Identification_Details> employeeIdentificationDetails;
	//private List<Contact_Address_Details> employeeContactAddressDetails;
	private List<Certification> cerfifications;
	private Sendemail sendemail;
	private List<DeleteSingaporeEmployeeFamilyMember> deleteSingaporeEmployeeFamilyMember;
	private List<DeleteIndianEmployeeFamilyMember> deleteIndianEmployeeFamilyMember;
	private EmployeeExitCheck employeeExitCheck;
	private List<Sow_Ctc_Breakup> deletedsowCtcBreakup;
	
	
	
	public List<Sow_Ctc_Breakup> getDeletedsowCtcBreakup() {
		return deletedsowCtcBreakup;
	}

	public void setDeletedsowCtcBreakup(List<Sow_Ctc_Breakup> deletedsowCtcBreakup) {
		this.deletedsowCtcBreakup = deletedsowCtcBreakup;
	}

	public EmployeeExitCheck getEmployeeExitCheck() {
		return employeeExitCheck;
	}

	public void setEmployeeExitCheck(EmployeeExitCheck employeeExitCheck) {
		this.employeeExitCheck = employeeExitCheck;
	}

	public List<Employee_Identification_Details> getEmployeeIdentificationDetails() {
		return employeeIdentificationDetails;
	}

	public Collection<List<Sow_Ctc_Breakup>> getSowCtcBreakupHistory() {
		return sowCtcBreakupHistory;
	}

	public List<Leave_Eligibility_Details> getLeavesEligibility() {
		return leavesEligibility;
	}

	public void setLeavesEligibility(List<Leave_Eligibility_Details> leavesEligibility) {
		this.leavesEligibility = leavesEligibility;
	}

	public void setSowCtcBreakupHistory(Collection<List<Sow_Ctc_Breakup>> sowCtcBreakupHistory) {
		this.sowCtcBreakupHistory = sowCtcBreakupHistory;
	}

	public List<Sow_Employee_Association> getSowEmployeeAssoc() {
		return sowEmployeeAssoc;
	}



	public void setSowEmployeeAssoc(List<Sow_Employee_Association> sowEmployeeAssoc) {
		this.sowEmployeeAssoc = sowEmployeeAssoc;
	}



	public List<Sow_Ctc_Breakup> getSowCtcBreakup() {
		return sowCtcBreakup;
	}



	public void setSowCtcBreakup(List<Sow_Ctc_Breakup> sowCtcBreakup) {
		this.sowCtcBreakup = sowCtcBreakup;
	}

	public List<DeleteSingaporeEmployeeFamilyMember> getDeleteSingaporeEmployeeFamilyMember() {
		return deleteSingaporeEmployeeFamilyMember;
	}


	public void setDeleteSingaporeEmployeeFamilyMember(
			List<DeleteSingaporeEmployeeFamilyMember> deleteSingaporeEmployeeFamilyMember) {
		this.deleteSingaporeEmployeeFamilyMember = deleteSingaporeEmployeeFamilyMember;
	}



	public List<DeleteIndianEmployeeFamilyMember> getDeleteIndianEmployeeFamilyMember() {
		return deleteIndianEmployeeFamilyMember;
	}



	public void setDeleteIndianEmployeeFamilyMember(
			List<DeleteIndianEmployeeFamilyMember> deleteIndianEmployeeFamilyMember) {
		this.deleteIndianEmployeeFamilyMember = deleteIndianEmployeeFamilyMember;
	}



	public Indian_Employees_Insurance_Details getIndianEmployeesInsuranceDetails() {
		return indianEmployeesInsuranceDetails;
	}

	public void setIndianEmployeesInsuranceDetails(Indian_Employees_Insurance_Details indianEmployeesInsuranceDetails) {
		this.indianEmployeesInsuranceDetails = indianEmployeesInsuranceDetails;
	}

	public Singapore_Employee_Insurance_Details getSingaporeEmployeeInsuranceDetails() {
		return singaporeEmployeeInsuranceDetails;
	}

	public void setSingaporeEmployeeInsuranceDetails(
			Singapore_Employee_Insurance_Details singaporeEmployeeInsuranceDetails) {
		this.singaporeEmployeeInsuranceDetails = singaporeEmployeeInsuranceDetails;
	}

	

	public List<Contact_Address_Details> getEmployeeContactAddressDetails() {
		return employeeContactAddressDetails;
	}

	public void setEmployeeContactAddressDetails(List<Contact_Address_Details> employeeContactAddressDetails) {
		this.employeeContactAddressDetails = employeeContactAddressDetails;
	}

	public Emergency_Contact_Details getEmergencyContactDetails() {
		return emergencyContactDetails;
	}

	public void setEmergencyContactDetails(Emergency_Contact_Details emergencyContactDetails) {
		this.emergencyContactDetails = emergencyContactDetails;
	}

	public Employee_Appraisal_Details getEmployeeAppraisalDetails() {
		return employeeAppraisalDetails;
	}

	public void setEmployeeAppraisalDetails(Employee_Appraisal_Details employeeAppraisalDetails) {
		this.employeeAppraisalDetails = employeeAppraisalDetails;
	}

	public Employee_Bank_Details getEmployeeBankDetails() {
		return employeeBankDetails;
	}

	public void setEmployeeBankDetails(Employee_Bank_Details employeeBankDetails) {
		this.employeeBankDetails = employeeBankDetails;
	}

	public Employee_Offer_Details getEmployeeOfferDetails() {
		return employeeOfferDetails;
	}

	public void setEmployeeOfferDetails(Employee_Offer_Details employeeOfferDetails) {
		this.employeeOfferDetails = employeeOfferDetails;
	}

	public Employee_Terms_And_Conditions getEmployeeTermsAndConditions() {
		return employeeTermsAndConditions;
	}

	public void setEmployeeTermsAndConditions(Employee_Terms_And_Conditions employeeTermsAndConditions) {
		this.employeeTermsAndConditions = employeeTermsAndConditions;
	}

	
	public void setEmployeeIdentificationDetails(List<Employee_Identification_Details> employeeIdentificationDetails) {
		this.employeeIdentificationDetails = employeeIdentificationDetails;
	}

	/*public List<Contact_Address_Details> getEmployeeContactAddressDetails() {
		return employeeContactAddressDetails;
	}

	public void setEmployeeContactAddressDetails(List<Contact_Address_Details> employeeContactAddressDetails) {
		this.employeeContactAddressDetails = employeeContactAddressDetails;
	}*/

	public Employee_Work_Permit_Details getEmployeeWorkPermitDetails() {
		return employeeWorkPermitDetails;
	}

	public void setEmployeeWorkPermitDetails(Employee_Work_Permit_Details employeeWorkPermitDetails) {
		this.employeeWorkPermitDetails = employeeWorkPermitDetails;
	}
	
	public Sendemail getSendemail() {
		return sendemail;
	}

	public void setSendemail(Sendemail sendemail) {
		this.sendemail = sendemail;
	}

	/**
	 * @return the employeeSalaryDetails
	 */
	public Employee_Salary_Details getEmployeeSalaryDetails() {
		return employeeSalaryDetails;
	}

	/**
	 * @param employeeSalaryDetails the employeeSalaryDetails to set
	 */
	public void setEmployeeSalaryDetails(Employee_Salary_Details employeeSalaryDetails) {
		this.employeeSalaryDetails = employeeSalaryDetails;
	}
	/**
	 * 
	 */
	public Employee() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the employeePersonalDetails
	 */
	public Employee_Personal_Details getEmployeePersonalDetails() {
		return employeePersonalDetails;
	}

	/**
	 * @param employeePersonalDetails the employeePersonalDetails to set
	 */
	public void setEmployeePersonalDetails(Employee_Personal_Details employeePersonalDetails) {
		this.employeePersonalDetails = employeePersonalDetails;
	}

	

	/**
	 * @return the employeeProfessionalDetails
	 */
	public List<Employee_Professional_Details> getEmployeeProfessionalDetails() {
		return employeeProfessionalDetails;
	}

	public void setEmployeeProfessionalDetails(List<Employee_Professional_Details> employeeProfessionalDetails) {
		this.employeeProfessionalDetails = employeeProfessionalDetails;
	}
	/*public Employee_Professional_Details getEmployeeProfessionalDetails() {
		return employeeProfessionalDetails;
	}

	
	public void setEmployeeProfessionalDetails(Employee_Professional_Details employeeProfessionalDetails) {
		this.employeeProfessionalDetails = employeeProfessionalDetails;
	}*/

	/**
	 * @return the employeeAssignmentDetails
	 */
	public Employee_Assignment_Details getEmployeeAssignmentDetails() {
		return employeeAssignmentDetails;
	}

	

	public void setEmployeeAssignmentDetails(Employee_Assignment_Details employeeAssignmentDetails) {
		this.employeeAssignmentDetails = employeeAssignmentDetails;
	}
	
	

	/**
	 * @return the skills
	 */
	public List<Skill> getSkills() {
		return skills;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	/**
	 * @return the cerfifications
	 */
	public List<Certification> getCerfifications() {
		return cerfifications;
	}

	/**
	 * @param cerfifications the cerfifications to set
	 */
	public void setCerfifications(List<Certification> cerfifications) {
		this.cerfifications = cerfifications;
	}

	/* (non-Javadoc)
	 * @see com.helius.entities.IEntity#toJSON()
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

}
