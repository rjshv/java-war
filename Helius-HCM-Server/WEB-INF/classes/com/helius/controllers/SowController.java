/**
 * 
 */
package com.helius.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.dao.Sow;
import com.helius.dao.SowDAOImpl;
import com.helius.dao.Sowdashboard;
import com.helius.entities.Sow_Details;
import com.helius.managers.SowManager;
import com.helius.utils.Status;

@RestController
public class SowController {

	private org.hibernate.internal.SessionFactoryImpl sessionFactory;
	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Autowired
	ApplicationContext context;
	@Autowired
	SowManager sowManager;
	@Autowired
	Status status;
	
	public SowController() {
		// TODO Auto-generated constructor stub
	}
	
	@CrossOrigin
	@RequestMapping(value = "getSow/", method = RequestMethod.GET)
	public ResponseEntity<String>  getSow(@RequestParam String sowDetailsId) {
		// TODO call EmployeeManager to get the employee details for the
		// employee id and create json and send it back
		ResponseEntity<String> result;
		String sowsjson = "";
		try {
		Sow_Details sowDetails = sowManager.getSow(sowDetailsId);
		/*if (sowDetails == null) {
			status.setMessage("sow does not exist or unable to fetch the details");
			return "{\"response\":\"" + status.getMessage() + "\"}";
		}*/
		if (sowDetails == null) {
			result = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			return result;
		}
		ObjectMapper om = new ObjectMapper();		
		Sow sow = new Sow();	
			sow.setSowdetails(sowDetails);
			sowsjson = om.writeValueAsString(sow);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			result = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			return result;
		}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//String sowjson = "{ \"sowdetails\":" + sowsjson + "}";
		result = new ResponseEntity<String>(sowsjson,HttpStatus.OK);
		return result;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getListOfSowDetails", method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity<String> getListOfSowDetails(@RequestParam("model") String jsonData) {
		ResponseEntity<String> sowdetailList = null;
		try{
			//JSONObject data = (JSONObject) JSONValue.parse(jsonData);
			List<Sow_Details> results = sowManager.getListOfSowDetails(jsonData);
			if (results != null && !results.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String result = om.writeValueAsString(results);
			sowdetailList = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			sowdetailList = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return sowdetailList;
    }
	
	@CrossOrigin
	@RequestMapping(value = "getAllSow", method = RequestMethod.GET)
    public ResponseEntity<String> getAllSow() {
		ResponseEntity<String> sowDashboard = null;
		try{
			List<Sowdashboard> results = sowManager.getAllSow();
			if (results != null && !results.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String result = om.writeValueAsString(results);
			sowDashboard = new ResponseEntity<String>(result, HttpStatus.OK);
		} else {
			sowDashboard = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return sowDashboard;
    }
	
	
	
	@CrossOrigin
	@RequestMapping(value = "getSowPicklist", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getSowPicklist() {
		ObjectMapper objectMapper = new ObjectMapper();
		String sowClientList = sowManager.getSowPicklist();
		return sowClientList;
	}
		
	/*@CrossOrigin
	@RequestMapping(value = "sowRenewal", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String sowRenewal(@RequestParam("model") String jsondata,MultipartHttpServletRequest request) {
		ObjectMapper om = new ObjectMapper();
		Sow_Details sow = null;
		try {
			sow = om.readValue(jsondata, Sow_Details.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EmployeeManager employeemanager = (EmployeeManager) context.getBean("employeeManager");
		Status status1 = employeemanager.sowRenewal(sow);
		
		return "{\"response\":\"" + status1.getMessage() + "\"}";
	}*/
	
	@CrossOrigin
	@RequestMapping(value = "updateSOWResolveStatus", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String updateSOWResolveStatus(@RequestParam("model") String jsondata) {
		/*ObjectMapper om = new ObjectMapper();
		Sowdashboard sowdashboard = null;
		try {
			sowdashboard = om.readValue(jsondata, Sowdashboard.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status.setMessage("Unable to map data Invalid json ");
			return "{\"response\":\"" + status.getMessage() + "\"}";		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status.setMessage("Unable to map data Invalid json ");
			return "{\"response\":\"" + status.getMessage() + "\"}";		
		}*/
		JSONObject Json = (JSONObject) JSONValue.parse(jsondata);
		 status = sowManager.updateSOWResolveStatus(Json);	
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "saveSOW", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveSOW(@RequestParam("model") String jsondata,MultipartHttpServletRequest request) {
		System.out.println("sowjsondata:======" + jsondata.toString());	
		ObjectMapper om = new ObjectMapper();
		Sow sow = null;
		try {
			String convertedValue = new String(jsondata.getBytes("ISO8859_1"), "UTF8");	
			sow = om.readValue(convertedValue, Sow.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status.setMessage("Unable to map data Invalid json ");
			return "{\"response\":\"" + status.getMessage() + "\"}";		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status.setMessage("Unable to map data Invalid json ");
			return "{\"response\":\"" + status.getMessage() + "\"}";		
		}
		String heliusReferenceNumber = sow.getSowdetails().getHeliusReferenceNumber();
		 status = sowManager.saveorUpdateSOW(sow,request);	
		return "{\"response\":\"" + status.getMessage() + "\",\"heliusReferenceNumber\":\"" + heliusReferenceNumber + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "runserv", method = RequestMethod.GET)
	public void runserv() {
		SowDAOImpl sowDAOImpl=(SowDAOImpl)context.getBean("sowDAO");
		try {
			sowDAOImpl.runservice();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "convertFutureSow", method = RequestMethod.GET)
	public String convertFutureSow(@RequestParam String futureSowDetailsId,@RequestParam String activeDetailsId) {		
		 status = sowManager.convertFutureSowToActive(futureSowDetailsId,activeDetailsId);	
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}
	
	@CrossOrigin
	@RequestMapping(value = "getSowFiles", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> getSowFiles(@RequestParam String sowDetailsId,String filetype) {
		ResponseEntity<byte[]> responseEntity = sowManager.getSowFiles(sowDetailsId,filetype);
		return responseEntity;
	}
	
	@CrossOrigin
	@RequestMapping(value = "deleteSowPoFile", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> deleteSowPoFile(@RequestParam String sowDetailsId,String filetype) {
		ResponseEntity<byte[]> responseEntity = sowManager.deleteSowPoFile(sowDetailsId,filetype);
		return responseEntity;
	}
}
