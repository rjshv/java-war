package com.helius.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.dao.BeelineTimesheetDashboard;
import com.helius.dao.EmployeeDAOImpl;
import com.helius.dao.TimeSheetAndLeaveServiceImpl;
import com.helius.entities.Checklist;
import com.helius.entities.Employee;
import com.helius.entities.Employee_Beeline_Timesheet;
import com.helius.entities.Employee_Timesheet_Status;
import com.helius.managers.ChecklistManager;
import com.helius.managers.EmployeeManager;
import com.helius.managers.TimesheetManager;
import com.helius.utils.Status;

@RestController
public class TimesheetController {
	@Autowired
	TimesheetManager timesheetManager;
	@Autowired
	Status status;
	
	@CrossOrigin
	@RequestMapping(value = "getTimesheetStatus", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getTimesheetStatus(@RequestParam String employeeId,@RequestParam String timesheetMonth) {	
		ResponseEntity<String> timesheet = null;
		try{
		Employee_Timesheet_Status TimesheetStatus = timesheetManager.getTimesheetStatus(employeeId,timesheetMonth);
		if(TimesheetStatus != null){
		ObjectMapper om = new ObjectMapper();
		String timesheet1 = om.writeValueAsString(TimesheetStatus);
		timesheet = new ResponseEntity<String>(timesheet1, HttpStatus.OK);
		}else{
		timesheet = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return timesheet;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getAllTimesheetStatus", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllTimesheetStatus(@RequestParam String timesheetMonth) {	
		ResponseEntity<String> timesheet = null;
		try{
		List<Employee_Timesheet_Status> TimesheetStatus = timesheetManager.getAllTimesheetStatus(timesheetMonth);
		if (TimesheetStatus != null && !TimesheetStatus.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String timesheet1 = om.writeValueAsString(TimesheetStatus);
			timesheet = new ResponseEntity<String>(timesheet1, HttpStatus.OK);
		} else {
			timesheet = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return timesheet;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getTimesheetDashboardDetails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getTimesheetDashboardDetails(@RequestParam String timesheetMonth) {	
		ResponseEntity<String> timesheet = null;
		try{
		List<Object> TimesheetStatus = timesheetManager.getTimesheetDashboardDetails(timesheetMonth);
		if (TimesheetStatus != null && !TimesheetStatus.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String timesheet1 = om.writeValueAsString(TimesheetStatus);
			timesheet = new ResponseEntity<String>(timesheet1, HttpStatus.OK);
		} else {
			timesheet = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return timesheet;
	}
	
	@CrossOrigin
	@RequestMapping(value = "TimesheetPicklist", method = RequestMethod.GET, produces = "application/json")
	public String TimesheetPicklist(String timesheetMonth) {	
		String timesheetPicklist = timesheetManager.TimesheetPicklist(timesheetMonth);
		return timesheetPicklist;
	}
	
	@CrossOrigin
	@RequestMapping(value = "saveorupdateTimesheetEmailId", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveorupdateTimesheetEmailId(@RequestParam("model") String jsondata) {	
		status = timesheetManager.saveorupdateTimesheetEmailId(jsondata);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "updateSalaryProcessing", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String updateSalaryProcessing(@RequestParam("model") String jsondata) {	
		status = timesheetManager.updateSalaryProcessing(jsondata);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "saveTimesheetStatus", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveTimesheetStatus(@RequestParam("model") String jsondata, MultipartHttpServletRequest request) {	
		status = timesheetManager.saveTimeSheetStatus(jsondata, request);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "updateTimesheetStatus", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String updateTimesheetStatus(@RequestParam("model") String jsondata, MultipartHttpServletRequest request,String email) {	
		status = timesheetManager.updateTimeSheetStatus(jsondata, request,email);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	@CrossOrigin
	@RequestMapping(value = "getTimesheetFile", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> getTimesheetFile(@RequestParam String url) {
		ResponseEntity<byte[]> responseEntity = timesheetManager.getTimesheetFile(url);
		return responseEntity;
	}
	@CrossOrigin
	@RequestMapping(value = "getAllTimesheetFiles", method = RequestMethod.POST, produces = "application/zip")
	public ResponseEntity<byte[]> getAllTimesheetFiles(@RequestParam("model") String data) {
		JSONObject Json = (JSONObject) JSONValue.parse(data);
		ResponseEntity<byte[]> responseEntity = timesheetManager.getAllTimesheetFiles(Json);
		return responseEntity;
	}
	
	@CrossOrigin
	@RequestMapping(value = "sendTimesheetStatusNotification", method = RequestMethod.POST, produces = "application/json")
		public String sendTimesheetStatusNotification(@RequestParam("model") String jsondata,MultipartHttpServletRequest request){	
		try {
				status = timesheetManager.sendEmailNotification(jsondata,request);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "validateRTSTimesheet", method = RequestMethod.POST, consumes = { "multipart/form-data" }, produces ={})
	public ResponseEntity<String> validateRTSTimesheet(@RequestParam("model") String json,MultipartHttpServletRequest RTSFile) throws Exception {
		ResponseEntity<String> response = null;
		String result;
		try {
			result = timesheetManager.validateRTSTimesheet(json, RTSFile);
		}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);	
		}catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<String>(result, HttpStatus.OK);
		return  response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "saveRTStimesheet", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<String> saveRTStimesheet(@RequestParam("model") String json,MultipartHttpServletRequest RTSFile) throws Exception {
		ResponseEntity<String> response = null;
		String result;
		try {
			result = timesheetManager.saveRTSTimesheet(json, RTSFile);
		}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);	
		}catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<String>(result, HttpStatus.OK);
		return  response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "runBeelineTimesheetSummaryService", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<String> runBeelineTimesheetSummaryService(@RequestParam String timesheetMonth,MultipartHttpServletRequest request) throws Exception {
		ResponseEntity<String> response = null;
		String result = null;
		try {
			result = timesheetManager.saveBeelineTimesheetSummaryFile(timesheetMonth, request);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);	
		}catch (Throwable e) {
			return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "runBeelineTimesheetDetailsFileService", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<String> runBeelineTimesheetDetailsFileService(@RequestParam String timesheetMonth,MultipartHttpServletRequest request) throws Exception {
		ResponseEntity<String> response = null;
		String result = null;
		try {
			result = timesheetManager.saveBeelineTimesheetDetailsFiles(timesheetMonth, request);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);	
		}catch (Throwable e) {
			return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  response;
	}
	@CrossOrigin
	@RequestMapping(value = "beelineBulkEmailService", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<String> beelineBulkEmailService(@RequestParam String month,String statusType) throws Exception {
		ResponseEntity<String> response = null;
		String result = null;
		try {
			result = timesheetManager.beelineBulkEmailService(month,statusType);
			response = new ResponseEntity<String>(result, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);	
		}catch (Throwable e) {
			return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  response;
	}
	@CrossOrigin
	@RequestMapping(value = "getBeelineTimesheetFile", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> getBeelineTimesheetFile(@RequestParam String timesheetId,@RequestParam String fileType) {
		ResponseEntity<byte[]> responseEntity = timesheetManager.getBeelineTimesheetFile(timesheetId, fileType);
		return responseEntity;
	}

	@CrossOrigin
	@RequestMapping(value = "saveBeelineTimeSheet", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveBeelineTimeSheet(@RequestParam("model") String jsondata, MultipartHttpServletRequest request) {	
		status = timesheetManager.saveBeelineTimeSheet(jsondata, request);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "updateBeelineTimeSheet", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String updateBeelineTimeSheet(@RequestParam("model") String jsondata, MultipartHttpServletRequest request,String sendEmail) {	
		status = timesheetManager.updateBeelineTimeSheet(jsondata, request,sendEmail);
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "getBeelineTimesheet", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getBeelineTimesheet(@RequestParam String employeeId,@RequestParam String timesheetMonth) {	
		ResponseEntity<String> timesheet = null;
		try{
			Employee_Beeline_Timesheet TimesheetStatus = timesheetManager.getBeelineTimesheet(employeeId,timesheetMonth);
		if(TimesheetStatus != null){
		ObjectMapper om = new ObjectMapper();
		String timesheet1 = om.writeValueAsString(TimesheetStatus);
		timesheet = new ResponseEntity<String>(timesheet1, HttpStatus.OK);
		}else{
		timesheet = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return timesheet;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getBeelineDashboard", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getBeelineDashboard(@RequestParam String timesheetMonth) {	
		ResponseEntity<String> timesheet = null;
		try{
		List<BeelineTimesheetDashboard> TimesheetStatus = timesheetManager.getBeelineDashboardDetails(timesheetMonth);
		if (TimesheetStatus != null && !TimesheetStatus.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String timesheet1 = om.writeValueAsString(TimesheetStatus);
			timesheet = new ResponseEntity<String>(timesheet1, HttpStatus.OK);
		} else {
			timesheet = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return timesheet;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getAllBeelineTimesheet", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllBeelineTimesheet(@RequestParam String timesheetMonth) {	
		ResponseEntity<String> timesheet = null;
		try{
		List<Employee_Beeline_Timesheet> TimesheetStatus = timesheetManager.getAllBeelineTimesheet(timesheetMonth);
		if (TimesheetStatus != null && !TimesheetStatus.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String timesheet1 = om.writeValueAsString(TimesheetStatus);
			timesheet = new ResponseEntity<String>(timesheet1, HttpStatus.OK);
		} else {
			timesheet = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return timesheet;
	}
	
	
	/*@CrossOrigin
	@RequestMapping(value = "saveleaves", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveleaves(@RequestParam(value = "file", required = false) MultipartFile RTSFile) throws IOException {
		String base64Strings= null;
		InputStream xlsxContentStream = null;
		OPCPackage pkg = null;	
		String filename = null;
		String imageUrl = null;
		Status status = new Status();
		if (RTSFile != null) {
			filename = RTSFile.getOriginalFilename();
            xlsxContentStream = RTSFile.getInputStream();
            try {
				pkg = OPCPackage.open(xlsxContentStream);
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         //  TimeSheetAndLeaveService timeSheetAndLeaveService = (TimeSheetAndLeaveService) context.getBean("timeSheetAndLeaveService");

			 try {
			//  timeSheetAndLeaveServiceImpl.resourceLeaveDetails(pkg);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return "{\"response\":\"" + base64Strings + "\"}";
	}
*/
}
