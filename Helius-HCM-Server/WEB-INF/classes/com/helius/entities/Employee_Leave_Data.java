package com.helius.entities;

import java.util.List;

public class Employee_Leave_Data {

	private List<Leave_Eligibility_Details> leavesEligibility;
	
	private List<Leave_Usage_Details> leaveUsageDetails;
	
	private List<Leave_Record_Details> leaveRecordDetails;

	private Leave_Record_Details deleteLeaveRecord;
	
	private Leave_Eligibility_Details deleteLeaveEligibility;


	public Leave_Eligibility_Details getDeleteLeaveEligibility() {
		return deleteLeaveEligibility;
	}

	public void setDeleteLeaveEligibility(Leave_Eligibility_Details deleteLeaveEligibility) {
		this.deleteLeaveEligibility = deleteLeaveEligibility;
	}

	public Leave_Record_Details getDeleteLeaveRecord() {
		return deleteLeaveRecord;
	}

	public void setDeleteLeaveRecord(Leave_Record_Details deleteLeaveRecord) {
		this.deleteLeaveRecord = deleteLeaveRecord;
	}

	public List<Leave_Record_Details> getLeaveRecordDetails() {
		return leaveRecordDetails;
	}

	public void setLeaveRecordDetails(List<Leave_Record_Details> leaveRecordDetails) {
		this.leaveRecordDetails = leaveRecordDetails;
	}

	public List<Leave_Eligibility_Details> getLeavesEligibility() {
		return leavesEligibility;
	}

	public void setLeavesEligibility(List<Leave_Eligibility_Details> leavesEligibility) {
		this.leavesEligibility = leavesEligibility;
	}

	public List<Leave_Usage_Details> getLeaveUsageDetails() {
		return leaveUsageDetails;
	}

	public void setLeaveUsageDetails(List<Leave_Usage_Details> leaveUsageDetails) {
		this.leaveUsageDetails = leaveUsageDetails;
	}

	
	
	
}
 