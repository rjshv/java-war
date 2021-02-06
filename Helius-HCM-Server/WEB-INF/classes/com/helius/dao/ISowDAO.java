/**
 * 
 */
package com.helius.dao;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.helius.entities.Sow_Details;

public interface ISowDAO {
	public void saveorUpdateSOW(Sow sow,MultipartHttpServletRequest request) throws Throwable;
//	public void sowRenewal(Sow_Details sow) throws Throwable;
	public ResponseEntity<byte[]> getSowFiles(String sowDetailsId,String filetype);	
	public Sow_Details getSowDetails(String sowDetailsId) throws Throwable;
	public String getSowPicklist();
	public List<Sowdashboard> getAllSow() throws Throwable;
	public List<Sow_Details> getListOfSowDetails(String sowList) throws Throwable;
	public void convertFutureSowToActive(String futureSowDetailsId, String activeDetailsId) throws Throwable;
	public void updateSOWResolveStatus(JSONObject Json) throws Throwable;
	public ResponseEntity<byte[]> deleteSowPoFile(String sowDetailsId, String filetype);
	}
