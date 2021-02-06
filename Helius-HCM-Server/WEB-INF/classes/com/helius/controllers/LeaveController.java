package com.helius.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.dao.EmployeeLeaveDashboard;
import com.helius.dao.IndiaLeaveBulkReport;
import com.helius.dao.LeaveServiceImpl;
import com.helius.dao.Sowdashboard;
import com.helius.entities.Employee_Leave_Data;
import com.helius.entities.Leave_Eligibility_Details;
import com.helius.managers.EmployeeManager;
import com.helius.managers.LeaveManager;
import com.helius.utils.Status;

@RestController
public class LeaveController {
	
	@Autowired
	LeaveManager leaveManager;
	@Autowired
	Status status;
	@CrossOrigin
	@RequestMapping(value = "populateClientLeavesToEmp", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> populateClientLeavesToEmp(@RequestParam String adoj,@RequestParam String clientname,String work_location) {
		ResponseEntity<String> response = null;
		try{
		List<Leave_Eligibility_Details> leaveEligibLIst = leaveManager.populateClientLeavesToEmp(adoj,clientname,work_location);
		if (leaveEligibLIst != null && !leaveEligibLIst.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String result = om.writeValueAsString(leaveEligibLIst);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getEmployeeLeaveData", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getEmployeeLeaveData(@RequestParam String employee_id) {
		ResponseEntity<String> response = null;
		try{
			Employee_Leave_Data employeeLeaveData = leaveManager.getEmployeeLeaveData(employee_id);
		if (employeeLeaveData != null) {
			ObjectMapper om = new ObjectMapper();
			String result = om.writeValueAsString(employeeLeaveData);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
	@CrossOrigin
	@RequestMapping(value ="saveOrUpdateEmployeeLeaveData", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveOrUpdateEmployeeLeaveData(@RequestParam("model") String jsondata,MultipartHttpServletRequest request) {	
		status = leaveManager.saveOrUpdateEmployeeLeaveData(jsondata,request);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "getLeaveRecordFile", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> getLeaveRecordFile(@RequestParam String leaveRecordPath) {
		ResponseEntity<byte[]> responseEntity = leaveManager.getLeaveRecord(leaveRecordPath);	
		return responseEntity;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getAllLeaveRecords", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllLeaveRecords(@RequestParam String employee_id) {
		ResponseEntity<String> response = null;
		try{
			Employee_Leave_Data employeeLeaveData = leaveManager.getEmployeeLeaveData(employee_id);
		if (employeeLeaveData != null) {
			ObjectMapper om = new ObjectMapper();
			String result = om.writeValueAsString(employeeLeaveData);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	@CrossOrigin
	@RequestMapping(value ="runServiceToUpdateEmpLeavUsageForIndia", method = RequestMethod.GET, produces = "application/json")
	public String runServiceToUpdateEmpLeavUsageForIndia() {	
		status = leaveManager.runServiceToUpdateEmpLeavUsageForIndia();
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value ="runServiceToUpdateEmpLeavUsageForSingapore", method = RequestMethod.GET, produces = "application/json")
	public String runServiceToUpdateEmpLeavUsageForSingapore() {	
		status = leaveManager.runServiceToUpdateEmpLeavUsageForSingapore();
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}

	@CrossOrigin
	@RequestMapping(value ="runServiceToUpdateEmpLeaveEligibilityForIndia", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String runServiceToUpdateEmpLeaveEligibilityForIndia(MultipartHttpServletRequest request) {	
		status = leaveManager.runServiceToUpdateEmpLeaveEligibilityForIndia(request);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value ="runServiceToUpdateEmpLeaveEligibilityForSingapore", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String runServiceToUpdateEmpLeaveEligibilityForSingapore(MultipartHttpServletRequest request) {	
		status = leaveManager.runServiceToUpdateEmpLeaveEligibilityForSingapore(request);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value ="runDailySickLeaveServiceForSingapore", method = RequestMethod.GET, produces = "application/json")
	public String runDailySickLeaveServiceForSingapore() {	
		status = leaveManager.runDailySickLeaveServiceForSingapore();
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value ="settakenleaves", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String settakenleaves(@RequestParam String month) {	
		leaveManager.settakenleaves(month);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value ="verifyLeaveRecordHistoryAndLeaveUsage", method = RequestMethod.GET, produces = "application/json")
	public String verifyLeaveRecordHistoryAndLeaveUsage(String date) {	
		leaveManager.verifyLeaveRecordHistoryAndLeaveUsage(date);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	@CrossOrigin
	@RequestMapping(value = "indiaLeaveBulkReport", method = RequestMethod.GET)
    public ResponseEntity<String> indiaLeaveBulkReport(String leaveYear) {
		ResponseEntity<String> leaveDashboard = null;
		try{
			List<IndiaLeaveBulkReport> results = leaveManager.getIndiaLeavebulkReport(leaveYear);
			if (results != null && !results.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String result = om.writeValueAsString(results);
			leaveDashboard = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			leaveDashboard = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return leaveDashboard;
    }
    
    @CrossOrigin
	@RequestMapping(value = "employeeLeaveDashBoard", method = RequestMethod.GET)
    public ResponseEntity<String> employeeLeaveDashBoard(String leaveMonth) {
		ResponseEntity<String> leaveDashboard = null;
		try{
			List<EmployeeLeaveDashboard> results = leaveManager.employeeLeaveDashBoard(leaveMonth);
			if (results != null && !results.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String result = om.writeValueAsString(results);
			leaveDashboard = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			leaveDashboard = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return leaveDashboard;
    }
    
    public String tempservice(){
    	String month = "";
    	
    	return null;
    }
	
}
