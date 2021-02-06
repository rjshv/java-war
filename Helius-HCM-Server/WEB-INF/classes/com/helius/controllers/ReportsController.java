package com.helius.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helius.managers.ReportManager;



@RestController
public class ReportsController {

	@Autowired
	ApplicationContext context;
	
	@CrossOrigin
	@RequestMapping(value = "getvendorreports", method = RequestMethod.POST, produces = "multipart/form-data" )
	public String getvendorreports(@RequestParam("model") String data) {
		ReportManager reportManager = (ReportManager) context.getBean("reportManager");
		String offerdetailsByStatus = reportManager.getReports(data);
		return offerdetailsByStatus;
	}
	
	/*@RequestMapping(value = "helloReport1", method = RequestMethod.GET)
	  @ResponseBody
	  public void getRpt1(HttpServletResponse response) throws JRException, IOException {
	    InputStream jasperStream = this.getClass().getResourceAsStream("/jasperreports/HelloWorld1.jasper");
	 //  Map<String,Object> params = new HashMap<>();
		ReportManager reportManager = (ReportManager) context.getBean("reportManager");
		ReportDAOImpl reportDAOImpl = (ReportDAOImpl) context.getBean("reportService");
	String result =	reportDAOImpl.getvendorreport();
	    Map<String,Object> params = reportManager.getcsvReports();

	    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

	    response.setContentType("application/x-pdf");
	    response.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");

	    final OutputStream outStream = response.getOutputStream();
	    JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	  }*/
	
	@CrossOrigin
	@RequestMapping(value = "getLocalizationReport", method = RequestMethod.POST,produces = "multipart/form-data")
	  public ResponseEntity<byte[]> getLocalizationReport(@RequestParam("model") String data) throws  IOException {
	//public ResponseEntity<byte[]> getVendorPopulationReport(@RequestParam String employeeid,@RequestParam List<String> manager) throws  IOException {

		  //  InputStream jasperStream = this.getClass().getResourceAsStream("/jasperreports/HelloWorld1.jasper");
	 //  Map<String,Object> params = new HashMap<>();
	//	ReportManager reportManager = (ReportManager) context.getBean("reportManager");
		  JSONObject Json = (JSONObject) JSONValue.parse(data);
			ReportManager reportManager = (ReportManager) context.getBean("reportManager");
			ResponseEntity<byte[]> result =	reportManager.getLocalizationReport(Json);
	 /*   Map<String,Object> params = reportManager.getcsvReports();

	    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
	    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

	    response.setContentType("application/x-pdf");
	    response.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");

	    final OutputStream outStream = response.getOutputStream();
	    JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);*/
	    return result;
	  }
	
	
	
}
