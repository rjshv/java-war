package com.helius.dao;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.entities.Employee_Beeline_Timesheet;
import com.helius.entities.Employee_Timesheet_Status;

public interface TimesheetService {
	
	public String saveRTSTimesheet(String json, MultipartHttpServletRequest RTSFile) throws Throwable;
	public String validateRTSTimesheet(String json, MultipartHttpServletRequest request)throws Throwable;
	public ResponseEntity<byte[]> getTimesheetFile(String timesheetId);
	public ResponseEntity<byte[]> getAllTimesheetFiles(JSONObject json);
	public void saveTimeSheetStatus(String jsondata, MultipartHttpServletRequest request) throws Throwable;
	public void updateTimeSheetStatus(String jsondata, MultipartHttpServletRequest request,String email) throws Throwable;
	public Employee_Timesheet_Status getTimesheetStatus(String employeeId,String timesheetMonth) throws Throwable;
	public List<Employee_Timesheet_Status> getAllTimesheetStatus(String timesheetMonth) throws Throwable;
	public List<Object> getTimesheetDashboardDetails(String timesheetMonth) throws Throwable;
	public void sendEmailNotification(String jsonData) throws Throwable;
	public String TimesheetPicklist(String timesheetMonth) throws Throwable;
	public void saveorupdateTimesheetEmailId(String jsondata) throws Throwable;
	public void updateSalaryProcessing(String jsondata) throws Throwable;
	void sendEmailNotification(String jsonData, MultipartHttpServletRequest request) throws Throwable;
	
	public String saveBeelineTimesheetSummaryFile(String json,MultipartHttpServletRequest request) throws Throwable;	
	public String saveBeelineTimesheetDetailsFiles(String json,MultipartHttpServletRequest request) throws Throwable;
	public void saveBeelineTimeSheet(String jsondata, MultipartHttpServletRequest request) throws Throwable;
	public void updateBeelineTimeSheet(String jsondata, MultipartHttpServletRequest request,String sendEMail) throws Throwable;
	public Employee_Beeline_Timesheet getBeelineTimesheet(String employeeId,String timesheetMonth) throws Throwable;
	public List<Employee_Beeline_Timesheet> getAllBeelineTimesheet(String timesheetMonth) throws Throwable;
	public ResponseEntity<byte[]> getBeelineTimesheetFile(String timesheetId,String fileType);
	public List<BeelineTimesheetDashboard> getBeelineDashboard(String month) throws Throwable;
	public String beelineBulkEmailService(String month,String statusType) throws Throwable;


}
