/**
 * 
 */
package com.helius.managers;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.dao.ISowDAO;
import com.helius.dao.Sow;
import com.helius.dao.Sowdashboard;
import com.helius.entities.Sow_Details;
import com.helius.utils.Status;

public class SowManager {

	ISowDAO sowDAO = null;
	
	public ISowDAO getSowDAO() {
		return sowDAO;
	}

	public void setSowDAO(ISowDAO sowDAO) {
		this.sowDAO = sowDAO;
	}

	public SowManager() {
		// TODO Auto-generated constructor stub
	}
	
	public Sow_Details getSow(String sowDetailsId) throws Throwable{
		Sow_Details sowDetails = null;
		try {
			sowDetails = sowDAO.getSowDetails(sowDetailsId);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch  Sow Details");
		}
		return sowDetails;
	}
	
	public List<Sow_Details> getListOfSowDetails(String sowList) throws Throwable {
		List<Sow_Details> sowDetails = null;
		try {
			sowDetails = sowDAO.getListOfSowDetails(sowList);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch  Sow Details");
		}
		return sowDetails;
	}
	
	public String getSowPicklist() {
		String sowDetails = null;
		try {
			sowDetails = sowDAO.getSowPicklist();
		} catch (Throwable e) {
			return sowDetails = "unable to fetch sow list";
		}
		return sowDetails;
	}
	
	public List<Sowdashboard> getAllSow() throws Throwable {
		List<Sowdashboard> sowDetails = null;
		try {
			sowDetails = sowDAO.getAllSow();
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch  Sow Details");
		}
		return sowDetails;
	}
	
	public ResponseEntity<byte[]> getSowFiles(String sowDetailsId, String filetype) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	sowDAO.getSowFiles(sowDetailsId,filetype);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	
	public ResponseEntity<byte[]> deleteSowPoFile(String sowDetailsId, String filetype){
		ResponseEntity<byte[]> res = null;
		try {
			 res =	sowDAO.deleteSowPoFile(sowDetailsId,filetype);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	public Status convertFutureSowToActive(String futureSowDetailsId, String activeDetailsId) {
		try {
			sowDAO.convertFutureSowToActive(futureSowDetailsId,activeDetailsId);
		} catch (Throwable e) {
			return new Status(false," "+e.getMessage());
		}
		return new Status(true, "SOW Activated Successfully.!");
	}
	
	public Status saveorUpdateSOW(Sow sow,MultipartHttpServletRequest request) {
		try {
			sowDAO.saveorUpdateSOW(sow,request);
		} catch (Throwable e) {
			return new Status(false, "Employee SOW Details Not Saved .!");
		}
		return new Status(true, "Employee SOW Details Saved Successfully.!");
	}
	public Status updateSOWResolveStatus(JSONObject Json) {
		try {
			sowDAO.updateSOWResolveStatus(Json);
		} catch (Throwable e) {
			return new Status(false, "SOW Resolve Status is not Saved .!");
		}
		return new Status(true, "SOW Resolved Status is Saved Successfully.!");
	}
	/*public Status sowRenewal(Sow_Details sow) {
		try {
			sowDAO.sowRenewal(sow);
		} catch (Throwable e) {
			return new Status(false, "Employee SOW is not Renewed .!");
		}
		return new Status(true, "Employee SOW Details Renewed Successfully.!");
	}*/

}
