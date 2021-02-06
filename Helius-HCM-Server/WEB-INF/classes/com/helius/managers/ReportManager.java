package com.helius.managers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

import com.helius.service.ReportService;

public class ReportManager {

	ReportService reportService;
	
	
	public ReportService getReportService() {
		return reportService;
	}


	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}


	public String getReports(String filterdata) {
		String reportDetails = null;
		try {
			reportDetails = reportService.getReports(filterdata);
		} catch (Throwable e) {
			return reportDetails = "unable to fetch report Details list";
		}
		return reportDetails;
	}


	public ResponseEntity<byte[]> getLocalizationReport( JSONObject Json) {
		// TODO Auto-generated method stub
		ResponseEntity<byte[]> result = null;
		try {
			result = reportService.getLocalizationReport(Json);
		} catch (Throwable e) {
			return result;
		}
		return result;
	}
}
