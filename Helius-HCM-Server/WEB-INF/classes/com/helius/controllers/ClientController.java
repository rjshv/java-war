/**
 * 
 */
package com.helius.controllers;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.ClientDetail;
import com.helius.managers.ClientManager;
import com.helius.managers.EmployeeManager;
import com.helius.utils.AllClientDetails;
import com.helius.utils.Client_Group_HiringManagerDetails;
import com.helius.utils.Status;

/**
 * @author Tirumala
 * 25-Jul-2018
 */

@RestController
public class ClientController {

	
	@Autowired
	ApplicationContext context;
	@Autowired
	ClientManager clientManager;
	/**
	 * 
	 */
	public ClientController() {
		// TODO Auto-generated constructor stub
	}
	
	@CrossOrigin
	@RequestMapping(value = "client/save", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String saveclient(@RequestParam("model") String jsondata,
			MultipartHttpServletRequest request) {
		System.out.println("clientjsondata:" + jsondata.toString());
		Status status = clientManager.addClient(jsondata, request);
	
		return "{\"response\":\"" + status.getMessage() + "\"}";

		
	}
	@CrossOrigin
	@RequestMapping(value = "client/update", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public String updateclient(@RequestParam("model") String jsondata,
			MultipartHttpServletRequest request) {
		System.out.println("clientjsondata:" + jsondata.toString());
		Status status = clientManager.updateClient(jsondata, request);
	
		return "{\"response\":\"" + status.getMessage() + "\"}";

		
	}
	@CrossOrigin
	@RequestMapping(value = "client/get", method = RequestMethod.GET, produces = { "multipart/form-data" })
	public  @ResponseBody String getClient(@RequestParam("clientname") String clientname) {
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		try {
			
			com.helius.utils.ClientDetail clientdetail = clientManager.getClient(clientname);
		
			response = obm.writeValueAsString(clientdetail);

		} catch(Exception e) {
			 response = "Could not retrieve the client details for " + clientname + "\n" + e.getMessage();
			 return response;
		} catch (Throwable e) {
			response = "Could not retrieve the client details for " + clientname + "\n" + e.getMessage();
			return response;
		}
		
		return response;
	}

	@CrossOrigin
	@RequestMapping(value = "client/heliusdata", method = RequestMethod.GET, produces = { "multipart/form-data" })
	public  @ResponseBody String getHeliusData() {
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		try {
			List<com.helius.utils.ClientDetail> clientdetails = clientManager.getHeliusData();	
			response = obm.writeValueAsString(clientdetails);
		} catch(Exception e) {
			 response = "Could not retrieve the helius data \n" + e.getMessage();
			 return response;
		} catch (Throwable e) {
			response = "Could not retrieve the helius data \n" + e.getMessage();
			return response;
		}	
		return response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "client/getAllClientNames", method = RequestMethod.GET, produces = { "multipart/form-data" })
	public  @ResponseBody String getAllClientNames() {
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		try {
			
			
		    List<String> clientNames = clientManager.getAllClientNames();
			response = obm.writeValueAsString(clientNames);

		} catch(Exception e) {
			 response = "Could not retrieve the client names "+ "\n" + e.getMessage();
			 return response;
		} catch (Throwable e) {
			response = "Could not retrieve the client names "+ "\n" + e.getMessage();
			return response;
		}
		
		return response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "client/getAllClientwithGroups", method = RequestMethod.GET, produces = { "multipart/form-data" })
	public  @ResponseBody String getAllClientwithGroups() {
		ObjectMapper obm = new ObjectMapper();
		String response  = null;
		try {
			
			
			AllClientDetails allClientDetails = clientManager.getClient_Group_HiringManagerDetails();
		    response = obm.writeValueAsString(allClientDetails);

		} catch(Exception e) {
			 response = "Could not retrieve the client with rgoups and hiring managers "+ "\n" + e.getMessage();
			 return response;
		} catch (Throwable e) {
			response = "Could not retrieve the client with rgoups and hiring managers "+ "\n" + e.getMessage();
			return response;
		}
		
		return response;
	}
	
	public String saveclienttest( String jsondata,
			MultipartHttpServletRequest request) {
		System.out.println("clientjsondata:" + jsondata.toString());
		/*ObjectMapper om = new ObjectMapper();
		
		
		JSONObject clientJsonObj = (JSONObject) JSONValue.parse(jsondata);
		ClientDetail clientdetail = null;
		try {
			clientdetail = om.readValue(jsondata, ClientDetail.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		ApplicationContext context1 =
		        new ClassPathXmlApplicationContext("helius-hcm-servlet.xml");
		ClientManager clientManager = (ClientManager)context1.getBean("clientManager");
		Status status = clientManager.addClient(jsondata, request);
	
		
		String filename = null;
		String imageUrl = null;
		
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}


	
	@CrossOrigin
	@RequestMapping(value = "client/getFile", method = RequestMethod.GET, produces = "multipart/form-data")
	public ResponseEntity<byte[]> getOfferFiles(@RequestParam String filename) {
		
		ResponseEntity<byte[]> responseEntity = clientManager.getFile(filename);
		return responseEntity;	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String onlyclientdetails = "{\"clientCountry\":\"Singapore\",\"applicableSowType\":\"fixedprice\",\"clientTimesheet\":\"automated\",\"clientName\":\"DBSS\",\"clientShortName\":\"dbss\",\"clientMasterAgreementReference\":\"aaaa\",\"clientMasterAgreementExpiresOn\":\"2018-08-30T18:30:00.000Z\"}";
		String onlyclientjson = "{\"clientGroupDetails\":[],\"clientReimbursementCategories\":[{\"type\":\"TaxiClaims–Domestic\",\"allowed\":\"YES\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"no\"},{\"type\":\"TaxiClaims–International\",\"allowed\":\"YES\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MeanClaims–International\",\"allowed\":\"YES\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Intracity Travel\",\"allowed\":\"YES\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"}],\"clientCountry\":\"Singapore\",\"applicableSowType\":\"fixedprice\",\"clientTimesheet\":\"automated\",\"clientName\":\"DBSS\",\"clientShortName\":\"dbss\",\"clientMasterAgreementReference\":\"aaaa\",\"clientMasterAgreementExpiresOn\":\"2018-08-30T18:30:00.000Z\",\"clientLeavePolicy\":{\"annualLeave\":\"10\",\"casualLeave\":\"10\",\"medicalLeave\":\"10\",\"maternityLeave\":\"10\",\"paternityLeave\":\"10\",\"compensatoryOffLeaveAllowed\":\"yes\",\"leaveEncashments\":\"5\",\"sameAsHeliusPolicy\":\"YES\"},\"clientReimbursementPolicy\":{\"sameAsHeliusPolicy\":\"YES\"}}";
		String client_with_leave = "{\"clientGroupDetails\":[],\"clientCountry\":\"Singapore\",\"applicableSowType\":\"fixedprice\",\"clientTimesheet\":\"automated\",\"clientName\":\"DBSS\",\"clientShortName\":\"dbss\",\"clientMasterAgreementReference\":\"aaaa\",\"clientMasterAgreementExpiresOn\":\"2018-08-30T18:30:00.000Z\",\"clientLeavePolicy\":{\"annualLeave\":\"10\",\"casualLeave\":\"10\",\"medicalLeave\":\"10\",\"maternityLeave\":\"10\",\"paternityLeave\":\"10\",\"compensatoryOffLeaveAllowed\":\"yes\",\"leaveEncashments\":\"5\",\"sameAsHeliusPolicy\":\"YES\"}}";
		//String onlyclientjson = "{\"clientGroupDetails\":[],\"clientCountry\":\"Singapore\",\"applicableSowType\":\"fixedprice\",\"clientTimesheet\":\"automated\",\"clientName\":\"DBSS\",\"clientShortName\":\"dbss\",\"clientMasterAgreementReference\":\"aaaa\",\"clientMasterAgreementExpiresOn\":\"2018-08-30T18:30:00.000Z\",\"clientLeavePolicy\":{\"annualLeave\":\"10\",\"casualLeave\":\"10\",\"medicalLeave\":\"10\",\"maternityLeave\":\"10\",\"paternityLeave\":\"10\",\"compensatoryOffLeaveAllowed\":\"yes\",\"leaveEncashments\":\"5\",\"sameAsHeliusPolicy\":\"YES\"},\"clientReimbursementPolicy\":{\"sameAsHeliusPolicy\":\"YES\"}}";
		String clientwithgroup = "{\"clientLeavePolicy\":{\"compensatoryOffLeaveAllowed\":\"yes\",\"sameAsHeliusPolicy\":\"yes\",\"annualLeave\":\"10\",\"casualLeave\":\"10\",\"medicalLeave\":\"10\"},\"clientGroupDetails\":[{\"clientGroup\":\"DIgibank\",\"clientGroupAgreementReference\":\"Digibank\",\"clientGroupAgreementExpiresOn\":\"2018-09-29T18:30:00.000Z\",\"uploadGroupAgreement\":null,\"timesheetApprover\":\"\",\"clientGroupTimesheet\":\"manual\",\"clientGroupLeavePolicy\":{\"sameasClientPolicy\":false,\"annualLeave\":\"10\",\"casualLeave\":\"10\",\"maternityLeave\":\"\",\"paternityLeave\":\"\",\"compensatiryLeaveAllowed\":\"yes\",\"leaveEncashments\":\"\",\"medicalLeave\":\"10\"},\"clientGroupReimbursementPolicy\":{\"sameasClientPolicy\":false},\"clientGroupReimbursementCategories\":[{\"type\":\"TaxiClaims–Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MealClaims–Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"TaxiClaims–International\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MeanClaims–International\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Intracity Travel\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Visa Charges\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Flight Ticket Fare\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Hotel Accomodation\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"}]}],\"clientReimbursementCategories\":[{\"type\":\"TaxiClaims–Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MealClaims–Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"TaxiClaims–International\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MeanClaims–International\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Intracity Travel\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Visa Charges\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Flight Ticket Fare\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Hotel Accomodation\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"}],\"clientCountry\":\"Singapore\",\"applicableSowType\":\"fixed\",\"clientTimesheet\":\"automated\",\"clientReimbursementPolicy\":{\"sameAsHeliusPolicy\":\"yes\"},\"clientName\":\"DBSS\",\"clientShortName\":\"dbss\",\"clientMasterAgreementReference\":\"aaaa\",\"clientMasterAgreementExpiresOn\":\"2018-09-29T18:30:00.000Z\"}";;
		String clientwithgroup_timesheetapproversall = " {\"clientLeavePolicy\":{\"compensatoryOffLeaveAllowed\":\"yes\",\"annualLeave\":\"10\",\"casualLeave\":\"10\",\"medicalLeave\":\"10\"},\"clientGroupDetails\":[{\"clientGroup\":\"digibank\",\"clientGroupAgreementReference\":\"digibank\",\"clientGroupAgreementExpiresOn\":\"2018-09-29T18:30:00.000Z\",\"uploadGroupAgreement\":null,\"timesheetApprover\":\"Tirumala \",\"clientGroupTimesheet\":\"automated\",\"clientGroupLeavePolicy\":{\"sameasClientPolicy\":\"no\",\"annualLeave\":\"10\",\"casualLeave\":\"10\",\"maternityLeave\":\"\",\"paternityLeave\":\"\",\"compensatiryLeaveAllowed\":\"yes\",\"leaveEncashments\":\"5\"},\"clientGroupReimbursementPolicy\":{\"sameasClientPolicy\":\"no\"},\"clientGroupReimbursementCategories\":[{\"type\":\"TaxiClaimsâ€“Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MealClaimsâ€“Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"TaxiClaimsâ€“International\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MeanClaimsâ€“International\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Intracity Travel\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Visa Charges\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Flight Ticket Fare\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Hotel Accomodation\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"}]}],\"clientReimbursementCategories\":[{\"type\":\"TaxiClaimsâ€“Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MealClaimsâ€“Domestic\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"TaxiClaimsâ€“International\",\"allowed\":\"yes\",\"limitAmount\":\"100\",\"requiresSupportingProofs\":\"yes\"},{\"type\":\"MeanClaimsâ€“International\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Intracity Travel\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Visa Charges\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Flight Ticket Fare\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"},{\"type\":\"Hotel Accomodation\",\"allowed\":\"yes\",\"limitAmount\":null,\"requiresSupportingProofs\":\"yes\"}],\"clientCountry\":\"Singapore\",\"applicableSowType\":\"fixed\",\"timesheetApprover\":\"Umashankar \",\"clientTimesheet\":\"automated\",\"clientName\":\"DBSS\",\"clientShortName\":\"dbss\",\"clientMasterAgreementReference\":\"aaaa\",\"clientMasterAgreementExpiresOn\":\"2018-09-29T18:30:00.000Z\",\"timesheetApproversAll\":\",Umashankar,Tirumala\"}";
		
		ClientController cc = new ClientController();
		cc.saveclienttest(clientwithgroup_timesheetapproversall, null);

		// TODO Auto-generated method stub
		// This place is to test the code

	}

}
