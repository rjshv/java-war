/**
 * 
 */
package com.helius.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Checklist;
import com.helius.entities.Employee;
import com.helius.entities.EmployeeChecklistItemAndMandatoryitems;
import com.helius.entities.Employee_CheckList;
import com.helius.entities.Employee_Checklist_Items;
import com.helius.entities.Employee_Checklist_Master;
import com.helius.managers.ChecklistManager;
import com.helius.managers.EmployeeManager;
import com.helius.utils.Status;




// end timesheet



/**
 * @author Tirumala 23-Feb-2018
 * 
 */
@RestController
public class ChecklistController {

	private org.hibernate.internal.SessionFactoryImpl sessionFactory;

	
	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}


	@Autowired
	ApplicationContext context;
	@Autowired
	Status status;
	@CrossOrigin
	@RequestMapping(value = "getAllChecklist", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getAllChecklist() {
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		try {
		    List<Employee_Checklist_Master> allChecklist = checklistmanager.getAllChecklist();
			response = obm.writeValueAsString(allChecklist);
		} catch(Exception e) {
			 response = "Could Not Retrieve Checklist Data "+ "\n" + e.getMessage();
			 return response;
		} catch (Throwable e) {
			response = "Could Not Retrieve Checklist Data "+ "\n" + e.getMessage();
			return response;
		}
		return response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getChecklist", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getChecklist(@RequestParam String masterId) {
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		try {
			Employee_Checklist_Master allChecklistitems = checklistmanager.getChecklist(masterId);
			response = obm.writeValueAsString(allChecklistitems);
		} catch(Exception e) {
			 response = "Could Not Retrieve Checklist Items Data "+ "\n" + e.getMessage();
			 return response;
		} catch (Throwable e) {
			response = "Could Not Retrieve Checklist Items Data "+ "\n" + e.getMessage();
			return response;
		}
		return response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "saveChecklist", method = RequestMethod.POST, consumes = {"multipart/form-data" })
		public String saveChecklist(@RequestParam("model") String jsondata,
		MultipartHttpServletRequest request){
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		Checklist masterChecklist = null;
		try {
			masterChecklist = obm.readValue(jsondata, Checklist.class);  
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//return "Unable to Save Checklist Master Item" +e.getMessage();
				status.setMessage("Unable to Save Checklist Master Item");
				return "{\"response\":\"" + status.getMessage() + "\"}";
			}		
		try {
			status = checklistmanager.saveChecklist(masterChecklist,request);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "sendChecklistEmailNotification", method = RequestMethod.POST, consumes = {"multipart/form-data" })
		public String sendChecklistEmailNotification(@RequestParam("model") String jsondata,@RequestParam String offerId,@RequestParam String checkListType){
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");	
		//Status status = new Status();
		String response  = null;
		Checklist masterChecklist = null;
		try {
				status = checklistmanager.sendChecklistEmailNotification(jsondata,offerId,checkListType);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	/*@CrossOrigin
	@RequestMapping(value = "sendChecklistReminder", method = RequestMethod.POST, consumes = {"multipart/form-data" })
		public String sendChecklistReminder(@RequestParam String offerId,@RequestParam String checkListType){
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
	//	Status status = new Status();
		String response  = null;
		Checklist masterChecklist = null;
		try {
				status = checklistmanager.sendChecklistReminder(offerId,checkListType);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}*/
	
	
	@CrossOrigin
	@RequestMapping(value = "getCheckListFiles", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> getCheckListFiles(@RequestParam String employeeChecklistMasterId) {
		ChecklistManager checklistManager = (ChecklistManager) context.getBean("checklistManager");
		ResponseEntity<byte[]> responseEntity = checklistManager.getCheckListFiles(employeeChecklistMasterId);
		return responseEntity;
	}

	@CrossOrigin
	@RequestMapping(value = "getDefaultEmployeeChecklistItems", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getEmployeeChecklistItems(@RequestParam String offerId,
			@RequestParam String checkListType) {
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
		ObjectMapper obm = new ObjectMapper();
		String response = null;
		try {
			List<EmployeeChecklistItemAndMandatoryitems> allChecklist = checklistmanager.getEmployeeChecklistItems(offerId, checkListType);
			if (allChecklist.size() == 0) {
			//	return "Data not found";
				status.setMessage("Data not found");
				return "{\"response\":\"" + status.getMessage() + "\"}";
			}
			String response1 = obm.writeValueAsString(allChecklist);
			response = "{\"employeeChecklistItem\":" + response1 + "}";
		} catch (Exception e) {
			response = "Could Not Retrieve Checklist Data " + "\n" + e.getMessage();
			return response;
		} catch (Throwable e) {
			response = "Could Not Retrieve Checklist Data " + "\n" + e.getMessage();
			return response;
		}
		return response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "saveEmployeeChecklistItem", method = RequestMethod.POST, consumes = {
			"multipart/form-data" })
	public String saveEmployeeChecklistItem(@RequestParam("model") String jsondata,
			MultipartHttpServletRequest request) {
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
		//Status status = new Status();
		ObjectMapper obm = new ObjectMapper();
		String response = null;
		Employee_Checklist_Items employeeChecklistItems = null;
		Employee_CheckList employee_Checklist = null;
		try {
			String jsondata1 = jsondata.replaceAll(":\"-\"", ":null");
			JSONObject employeeMapJson = (JSONObject) JSONValue.parse(jsondata1);
			List<Object> saveItems = new ArrayList<Object>();
			JSONArray jsonArr = (JSONArray) employeeMapJson.get("employeeChecklistItem");
			for (int k = 0; k < jsonArr.size(); k++) {
				HashMap<String, Object> arrrayEntity = (HashMap<String, Object>) jsonArr.get(k);
				Iterator<Map.Entry<String, Object>> itr = arrrayEntity.entrySet().iterator();
				while (itr.hasNext()) {
					Map.Entry<String, Object> entry = itr.next();
					String key = entry.getKey();
					if ("mandatory".equalsIgnoreCase(key)) {
						itr.remove();
					}
				}
				saveItems.add(arrrayEntity);
			}
			String saveItemsJson = "{ \"employeeChecklistItem\":" + saveItems + "}";
			employee_Checklist = obm.readValue(saveItemsJson, Employee_CheckList.class);
			status = checklistmanager.saveEmployeeChecklistItem(employee_Checklist, request);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "updateEmployeeChecklistItem", method = RequestMethod.POST, consumes = { "multipart/form-data" })
		public String updateEmployeeChecklistItem(@RequestParam("model") String jsondata,
		MultipartHttpServletRequest request){
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
		//Status status = new Status();
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		Employee_CheckList employeeChecklist = null;
		try {
			employeeChecklist = obm.readValue(jsondata, Employee_CheckList.class);  
				status = checklistmanager.updateEmployeeChecklistItem(employeeChecklist,request);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "getItemFiles", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> getItemFiles(@RequestParam String employeeChecklistItemsId) {
		ChecklistManager checklistManager = (ChecklistManager) context.getBean("checklistManager");
		ResponseEntity<byte[]> responseEntity = checklistManager.getItemFiles(employeeChecklistItemsId);
		return responseEntity;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getEmployeeItems", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getEmployeeItems(@RequestParam String offerId,@RequestParam String checkListType) {
		ChecklistManager checklistmanager = (ChecklistManager) context.getBean("checklistManager");
		
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		try {
			List<Employee_Checklist_Items> allChecklistitems = checklistmanager.getEmployeeItems(offerId,checkListType);
			response = obm.writeValueAsString(allChecklistitems);
		} catch(Exception e) {
			 response = "Could Not Retrieve Checklist Items Data "+ "\n" + e.getMessage();
			 return response;
		} catch (Throwable e) {
			response = "Could Not Retrieve Checklist Items Data "+ "\n" + e.getMessage();
			return response;
		}
		return response;
	}
	
}
