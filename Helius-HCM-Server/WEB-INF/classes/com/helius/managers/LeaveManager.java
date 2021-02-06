package com.helius.managers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.dao.EmployeeLeaveDashboard;
import com.helius.dao.IndiaLeaveBulkReport;
import com.helius.dao.LeaveService;
import com.helius.entities.Employee_Leave_Data;
import com.helius.entities.Leave_Eligibility_Details;
import com.helius.utils.Status;

public class LeaveManager {
	
	LeaveService leaveService;
	
	
	public LeaveService getLeaveService() {
		return leaveService;
	}


	public void setLeaveService(LeaveService leaveService) {
		this.leaveService = leaveService;
	}


	public List<Leave_Eligibility_Details> populateClientLeavesToEmp(String adoj,String clientname,String work_location) throws Throwable {
		List<Leave_Eligibility_Details> leaveEligibLIst = null;
		try {
			leaveEligibLIst = leaveService.populateClientLeaveEligibility(adoj,clientname,work_location);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch Leave Eligibility  Details");
		}
		return leaveEligibLIst;
	}


	public Employee_Leave_Data getEmployeeLeaveData(String employee_id) throws Throwable {
		Employee_Leave_Data employeeLeaveData = null;
		try {
			employeeLeaveData = leaveService.getEmployeeLeaveData(employee_id);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch Employee Leave Details");
		}
		return employeeLeaveData;

	}
	
	public Status saveOrUpdateEmployeeLeaveData(String jsondata,MultipartHttpServletRequest request) {
			try {
				leaveService.saveOrUpdateEmployeeLeaveData(jsondata,request);
			} catch (Throwable e) {
				return new Status(false, "Failed to update Leave Details .! "+e.getMessage());
			}
			return new Status(true, "Leave Details Updated Successfully.!");
		}

	public ResponseEntity<byte[]> getLeaveRecord(String url) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	leaveService.getLeaveRecordFile(url);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	
	public Status runServiceToUpdateEmpLeaveEligibilityForIndia(MultipartHttpServletRequest request) {
		try{
			leaveService.runServiceToUpdateEmpLeaveEligibilityForIndia(request);
		}catch (Throwable e) {
			return new Status(false, "Failed to update India Leave Eligibility Details .! "+e.getMessage());
		}
		return new Status(true, "India Leave Eligibility Details Updated Successfully.!");
	}
	
	public Status runServiceToUpdateEmpLeaveEligibilityForSingapore(MultipartHttpServletRequest request) {
		try{
			leaveService.runServiceToUpdateEmpLeaveEligibilityForSingapore(request);
		}catch (Throwable e) {
			return new Status(false, "Failed to update Singapore Leave Eligibility Details .! "+e.getMessage());
		}
		return new Status(true, "Singapore Leave Eligibility Details Updated Successfully.!");
	}
	
	public Status runServiceToUpdateEmpLeavUsageForIndia() {
		try{
			leaveService.runServiceToUpdateEmpLeavUsageForIndia();
		}catch (Throwable e) {
			return new Status(false, "Failed to update India Leave Usage Details .! "+e.getMessage());
		}
		return new Status(true, "India Leave Usage Details Updated Successfully.!");
	}
	
	public Status runServiceToUpdateEmpLeavUsageForSingapore() {
		try{
			leaveService.runServiceToUpdateEmpLeavUsageForSingapore();
		}catch (Throwable e) {
			return new Status(false, "Failed to update Singapor Leave Usage Details .! "+e.getMessage());
		}
		return new Status(true, "Singapore Leave Usage Details Updated Successfully.!");
	}
	
	public Status runDailySickLeaveServiceForSingapore() {
		try{
			leaveService.runDailyServiceToUpdateSickLeavEligibilityAndUsageForSingapore();
		}catch (Throwable e) {
			return new Status(false, "Failed to run Daily Singapore Leave Service .! "+e.getMessage());
		}
		return new Status(true, "Singapore Daily Service Leave Details Updated Successfully.!");
	}

	
	public List<IndiaLeaveBulkReport> getIndiaLeavebulkReport(String leaveYear) throws Throwable {
		List<IndiaLeaveBulkReport> leaveDetails = null;
		try {
			leaveDetails = leaveService.indiaLeaveBulkReport(leaveYear);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch leave Details");
		}
		return leaveDetails;
	}
	public List<EmployeeLeaveDashboard> employeeLeaveDashBoard(String leaveMonth) throws Throwable {
		List<EmployeeLeaveDashboard> leaveDetails = null;
		try {
			leaveDetails = leaveService.getEmployeeLeaveDashBoard(leaveMonth);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch leave Details");
		}
		return leaveDetails;
	}


	public void settakenleaves(String month) {
		// TODO Auto-generated method stub
		leaveService.settakenleaves(month);
	}
	
	public void verifyLeaveRecordHistoryAndLeaveUsage(String date) {
		// TODO Auto-generated method stub
		leaveService.verifyLeaveRecordHistoryAndLeaveUsage(date);
	}
}
