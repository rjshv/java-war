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
import com.helius.dao.BeelineTimesheetDashboard;
import com.helius.entities.InvoiceAnnexure;
import com.helius.managers.InvoiceManager;

@RestController
public class InvoiceController {
	
	@Autowired
	InvoiceManager invoiceManager;

	@CrossOrigin
	@RequestMapping(value = "generateAnnexureForDah2", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<String> generateAnnexureForDah2(@RequestParam String month,@RequestParam String action,MultipartHttpServletRequest request) throws Exception {
		ResponseEntity<String> response = null;
		String result;
		try {
			result = invoiceManager.generateAnnexureForDah2(month,action, request);
		}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);	
		}catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response = new ResponseEntity<String>(result, HttpStatus.OK);
		return  response;
	}
	
	@CrossOrigin
	@RequestMapping(value = "getDah2AnnexureDashboard", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getDah2AnnexureDashboard(@RequestParam String month) {	
		ResponseEntity<String> response = null;
		try{
		List<InvoiceAnnexure> annexure = invoiceManager.getDah2AnnexureDashboard(month);
		if (annexure != null && !annexure.isEmpty() ) {
			ObjectMapper om = new ObjectMapper();
			String annexure1 = om.writeValueAsString(annexure);
			response = new ResponseEntity<String>(annexure1, HttpStatus.OK);
		} else {
			response = new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}}catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Throwable e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}
	
}
