package com.helius.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;

public interface ReportService {

	public String getReports(String filterdata);
	
	public ResponseEntity<byte[]> getLocalizationReport(JSONObject Json) throws Throwable;


}
