package com.helius.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.entities.Employee_Leave_Data;
import com.helius.entities.Leave_Eligibility_Details;
import com.helius.entities.Leave_Usage_Details;

public interface LeaveService {
	

	public Employee_Leave_Data getEmployeeLeaveData(String employee_id)throws Throwable;

	public void saveOrUpdateEmployeeLeaveData(String jsondata,MultipartHttpServletRequest request) throws Throwable;

	public List<Leave_Eligibility_Details> populateClientLeaveEligibility(String adoj, String clientname,
			String work_location) throws Throwable;
	
	public List<Leave_Usage_Details> newEmployeeLeaveUsage(Timestamp adoj,String location,List<Leave_Eligibility_Details> eligibility)throws Throwable;
	
	public boolean checkProbationPeriod(LocalDate ADOJ);
	
	public void runServiceToUpdateEmpLeaveEligibilityForIndia(MultipartHttpServletRequest cfleaveFile) throws Throwable;

	public void runServiceToUpdateEmpLeaveEligibilityForSingapore(MultipartHttpServletRequest cfleaveFile) throws Throwable;
	
	public void runServiceToUpdateEmpLeavUsageForIndia() throws Throwable;
	
	public void runServiceToUpdateEmpLeavUsageForSingapore() throws Throwable;
	
	public void runDailyServiceToUpdateSickLeavEligibilityAndUsageForSingapore() throws Throwable;

	public List<IndiaLeaveBulkReport> indiaLeaveBulkReport(String leaveYear) throws Throwable;

	public List<EmployeeLeaveDashboard> getEmployeeLeaveDashBoard(String leaveMonth) throws Throwable;

	public ResponseEntity<byte[]> getLeaveRecordFile(String url);

	public void settakenleaves(String month);

	public void verifyLeaveRecordHistoryAndLeaveUsage(String date);

}
