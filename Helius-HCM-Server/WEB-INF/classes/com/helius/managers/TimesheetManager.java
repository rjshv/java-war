package com.helius.managers;

import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.entities.Checklist;
import com.helius.entities.Employee_Beeline_Timesheet;
import com.helius.entities.Employee_Timesheet_Status;
import com.helius.entities.Users;
import com.helius.dao.BeelineTimesheetDashboard;
import com.helius.dao.TimesheetService;
import com.helius.utils.Status;

public class TimesheetManager {
	
	TimesheetService timesheetService;

	public TimesheetService getTimesheetService() {
		return timesheetService;
	}

	public void setTimesheetService(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}
	
	public ResponseEntity<byte[]> getTimesheetFile(String url) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	timesheetService.getTimesheetFile(url);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	public ResponseEntity<byte[]> getAllTimesheetFiles(JSONObject Json) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	timesheetService.getAllTimesheetFiles(Json);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	
	public Employee_Timesheet_Status getTimesheetStatus(String employeeId,String timesheetMonth) throws Throwable {
		Employee_Timesheet_Status timesheetStatus = null;
		try {
			timesheetStatus = timesheetService.getTimesheetStatus(employeeId,timesheetMonth);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch  Timesheet Status Details");
		}
		return timesheetStatus;
	}
	public List<Object> getTimesheetDashboardDetails(String timesheetMonth) throws Throwable {
		List<Object> timesheetStatus = null;
		try {
			timesheetStatus = timesheetService.getTimesheetDashboardDetails(timesheetMonth);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch  Timesheet Status Details");
		}
		return timesheetStatus;
	}
	public List<Employee_Timesheet_Status> getAllTimesheetStatus(String timesheetMonth) throws Throwable {
		List<Employee_Timesheet_Status> timesheetStatus = null;
		try {
			timesheetStatus = timesheetService.getAllTimesheetStatus(timesheetMonth);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch  Timesheet Status Details");
		}
		return timesheetStatus;
	}
	public String TimesheetPicklist(String timesheetMonth) {
		String picklist = null;
		try {
			picklist = timesheetService.TimesheetPicklist(timesheetMonth);
		} catch (Throwable e) {
			return picklist = "unable to fetch data";
		}
		return picklist;
	}
	
	public Status saveorupdateTimesheetEmailId(String jsondata) {
		try {
			timesheetService.saveorupdateTimesheetEmailId(jsondata);
		} catch (Throwable e) {
			return new Status(false," "+e.getMessage());
		}
		return new Status(true, "Timesheet Email-Id saved successfully");
	}
	
	public Status updateSalaryProcessing(String jsondata) {
		try {
			timesheetService.updateSalaryProcessing(jsondata);
		} catch (Throwable e) {
			return new Status(false," "+e.getMessage());
		}
		return new Status(true, "Salary Processing Status Updated successfully");
	}
	public Status saveTimeSheetStatus(String jsondata, MultipartHttpServletRequest request) {
		try {
			timesheetService.saveTimeSheetStatus(jsondata, request);
		} catch (Throwable e) {
			return new Status(false," "+e.getMessage());
		}
		return new Status(true, "Timesheet saved successfully");
	}
	
	public Status updateTimeSheetStatus(String jsondata, MultipartHttpServletRequest request,String email) {
		try {
			timesheetService.updateTimeSheetStatus(jsondata, request,email);
		} catch (Throwable e) {
			return new Status(false," "+e.getMessage());
		}
		return new Status(true, "Timesheet Updated successfully");
	}
	public Status sendEmailNotification(String jsondata,MultipartHttpServletRequest request) {
		try {
			timesheetService.sendEmailNotification(jsondata,request);
		} catch (Throwable e) {
			return new Status(false," Failed to send Email Notification ");
		}
		return new Status(true, "Email sent successfully !!");
	}

	public String validateRTSTimesheet(String json, MultipartHttpServletRequest request) throws Throwable {
		String result = null;
		try {
			result = timesheetService.validateRTSTimesheet(json,request);
		} catch (Throwable e) {
			throw new Throwable("Failed to Process RTS Timesheet ");
		}
		return result;
	}
	public String saveRTSTimesheet(String json, MultipartHttpServletRequest RTSFile)throws Throwable {
		String result = null;
		try {
			result = timesheetService.saveRTSTimesheet(json, RTSFile);
		}  catch (Throwable e) {
			return " Failed to process RTS Timesheet "+e.getMessage();
		}
		return result;
	}
	public ResponseEntity<byte[]> getBeelineTimesheetFile(String timesheetId,String fileType) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	timesheetService.getBeelineTimesheetFile(timesheetId, fileType);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
		public String saveBeelineTimesheetSummaryFile(String json, MultipartHttpServletRequest beelineFIle)throws Throwable {
			String result = null;
			try {
				result = timesheetService.saveBeelineTimesheetSummaryFile(json, beelineFIle);
			}  catch (Throwable e) {
				return " Failed to process Beeline Timesheet "+e.getMessage();
			}
			return result;
		}
		
		public String saveBeelineTimesheetDetailsFiles(String json, MultipartHttpServletRequest beelineFIle)throws Throwable {
			String result = null;
			try {
				result = timesheetService.saveBeelineTimesheetDetailsFiles(json, beelineFIle);
			}  catch (Throwable e) {
				return " Failed to process Beeline Timesheet "+e.getMessage();
			}
			return result;
		}
		public String beelineBulkEmailService(String month,String statusType) throws Throwable{
			String result = null;
			try {
				result = timesheetService.beelineBulkEmailService(month,statusType);
			}  catch (Throwable e) {
				return "  "+e.getMessage();
			}
			return result;
		}

		public Status saveBeelineTimeSheet(String jsondata, MultipartHttpServletRequest request) {
			try {
				timesheetService.saveBeelineTimeSheet(jsondata, request);
			} catch (Throwable e) {
				return new Status(false," "+e.getMessage());
			}
			return new Status(true, "Timesheet saved successfully");
		}
		
		public Status updateBeelineTimeSheet(String jsondata, MultipartHttpServletRequest request,String sendEmail) {
			try {
				timesheetService.updateBeelineTimeSheet(jsondata, request,sendEmail);
			} catch (Throwable e) {
				return new Status(false," "+e.getMessage());
			}
			return new Status(true, "Timesheet Updated successfully");
		}
		
		public Employee_Beeline_Timesheet getBeelineTimesheet(String employeeId,String timesheetMonth) throws Throwable {
			Employee_Beeline_Timesheet timesheetStatus = null;
			try {
				timesheetStatus = timesheetService.getBeelineTimesheet(employeeId,timesheetMonth);
			} catch (Throwable e) {
				throw new Throwable("Failed to fetch  Timesheet Status Details");
			}
			return timesheetStatus;
		}
		public List<Employee_Beeline_Timesheet> getAllBeelineTimesheet(String timesheetMonth) throws Throwable {
			List<Employee_Beeline_Timesheet> timesheetStatus = null;
			try {
				timesheetStatus = timesheetService.getAllBeelineTimesheet(timesheetMonth);
			} catch (Throwable e) {
				throw new Throwable("Failed to fetch  Timesheet Status Details");
			}
			return timesheetStatus;
		}
		public List<BeelineTimesheetDashboard> getBeelineDashboardDetails(String timesheetMonth) throws Throwable {
			List<BeelineTimesheetDashboard> timesheetStatus = null;
			try {
				timesheetStatus = timesheetService.getBeelineDashboard(timesheetMonth);
			} catch (Throwable e) {
				throw new Throwable("Failed to fetch  beeline dashboard Details");
			}
			return timesheetStatus;
		}
}
