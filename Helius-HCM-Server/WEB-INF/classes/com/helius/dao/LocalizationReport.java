package com.helius.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Contact_Address_Details;
import com.helius.entities.Employee;
import com.helius.entities.Employee_Appraisal_Details;
import com.helius.entities.Employee_Assignment_Details;
import com.helius.entities.Employee_Bank_Details;
import com.helius.entities.Employee_Leaves_Eligibility;
import com.helius.entities.Employee_Offer_Details;
import com.helius.entities.Employee_Personal_Details;
import com.helius.entities.Employee_Salary_Details;
import com.helius.entities.Employee_Terms_And_Conditions;
import com.helius.entities.Employee_Work_Permit_Details;
import com.helius.entities.Indian_Employees_Insurance_Details;
import com.helius.entities.Singapore_Employee_Insurance_Details;
import com.helius.entities.User;
import com.helius.utils.Utils;

public class LocalizationReport  {
	
private static org.hibernate.internal.SessionFactoryImpl sessionFactory;
	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public HashMap<String, HashMap<String, ArrayList<Object>>> returnROAccountManagerMap(HashMap<String, ArrayList<Employee>> accManagerROEmpList) throws Exception{
		HashMap<String, HashMap<String, ArrayList<Object>>> tempMAp = new HashMap<String, HashMap<String, ArrayList<Object>>>();
		for (String m : accManagerROEmpList.keySet()) {
			System.out.println("=======accman======" + m);
			ArrayList<Employee> val = accManagerROEmpList.get(m);
			System.out.println("=======accmanvalsize======" + val.size());
			int AccMgrCitizen = 0;
			HashMap<String, ArrayList<Object>> temp = new HashMap<String, ArrayList<Object>>();
			ArrayList<Object> ala = new ArrayList<Object>();
			for (Employee emp : val) {
				String visaExist = null;
				if ("Citizen".equalsIgnoreCase(emp.getEmployeeOfferDetails().getVisa_status())
						|| "PR".equalsIgnoreCase(emp.getEmployeeOfferDetails().getVisa_status())) {
					visaExist = emp.getEmployeeOfferDetails().getVisa_status();
				} else {
					visaExist = "Others";
				}
				if (temp.containsKey(visaExist)) {
					if ("Citizen".equalsIgnoreCase(emp.getEmployeeOfferDetails().getVisa_status())
							|| "PR".equalsIgnoreCase(
									emp.getEmployeeOfferDetails().getVisa_status())) {
						ala = temp.get(emp.getEmployeeOfferDetails().getVisa_status());
					} else {
						ala = temp.get("Others");
					}
					HashMap<String, Object> resultmap = new HashMap<String, Object>();
					resultmap.put("Id", emp.getEmployeeOfferDetails().getOffer_id());
					resultmap.put("name", emp.getEmployeeOfferDetails().getEmployee_name());
					resultmap.put("expectedDOJ", emp.getEmployeeOfferDetails().getExpected_date_of_joining());
					ala.add(resultmap);
				} else {
					ala = new ArrayList<Object>();
					HashMap<String, Object> resultmap = new HashMap<String, Object>();
					resultmap.put("Id", emp.getEmployeeOfferDetails().getOffer_id());
					resultmap.put("name", emp.getEmployeeOfferDetails().getEmployee_name());
					resultmap.put("expectedDOJ", emp.getEmployeeOfferDetails().getExpected_date_of_joining());
					ala.add(resultmap);
					if ("Citizen".equalsIgnoreCase(emp.getEmployeeOfferDetails().getVisa_status())
							|| "PR".equalsIgnoreCase(emp.getEmployeeOfferDetails().getVisa_status())) {
						temp.put(emp.getEmployeeOfferDetails().getVisa_status(), ala);
					} else {
						temp.put("Others", ala);
					}
				}
			}
			tempMAp.put(m, temp);
		}
		return tempMAp;
	
	}
	public HashMap<String, HashMap<String, ArrayList<Object>>> returnAccountManagerMap(HashMap<String, ArrayList<Employee>> accManagerEmpList) throws Exception{
		HashMap<String, HashMap<String, ArrayList<Object>>> tempMAp = new HashMap<String, HashMap<String, ArrayList<Object>>>();
		for (String m : accManagerEmpList.keySet()) {
			System.out.println("=======accman======" + m);
			ArrayList<Employee> val = accManagerEmpList.get(m);
			System.out.println("=======accmanvalsize======" + val.size());
			int AccMgrCitizen = 0;
			HashMap<String, ArrayList<Object>> temp = new HashMap<String, ArrayList<Object>>();
			ArrayList<Object> ala = new ArrayList<Object>();
			for (Employee emp : val) {
				String visaExist = null;
				if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
						|| "PR".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
					visaExist = emp.getEmployeeWorkPermitDetails().getWork_permit_name();
				} else {
					visaExist = "Others";
				}
				if (temp.containsKey(visaExist)) {
					if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
							|| "PR".equalsIgnoreCase(
									emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						ala = temp.get(emp.getEmployeeWorkPermitDetails().getWork_permit_name());
					} else {
						ala = temp.get("Others");
					}
					HashMap<String, Object> resultmap = new HashMap<String, Object>();
					resultmap.put("Id", emp.getEmployeePersonalDetails().getEmployee_id());
					resultmap.put("name", emp.getEmployeePersonalDetails().getEmployee_name());
					resultmap.put("lastWorkingdate", emp.getEmployeePersonalDetails().getRelieving_date());
					ala.add(resultmap);
				} else {
					ala = new ArrayList<Object>();
					HashMap<String, Object> resultmap = new HashMap<String, Object>();
					resultmap.put("Id", emp.getEmployeePersonalDetails().getEmployee_id());
					resultmap.put("name", emp.getEmployeePersonalDetails().getEmployee_name());
					resultmap.put("lastWorkingdate", emp.getEmployeePersonalDetails().getRelieving_date());
					ala.add(resultmap);
					if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
							|| "PR".equalsIgnoreCase(
									emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						temp.put(emp.getEmployeeWorkPermitDetails().getWork_permit_name(), ala);
					} else {
						temp.put("Others", ala);
					}
				}
			}
			tempMAp.put(m, temp);
		}
		return tempMAp;
	
	}
	
	public HashMap<String, Object> getAccMngrMApOngivendate(ArrayList<Object> employeeDetailList,ArrayList<Object> offerDetailList,String onGivenDate) throws Exception{
		HashMap<String, ArrayList<Employee>> accmgrmap = new HashMap<String, ArrayList<Employee>>();
		ArrayList<Employee> accmgrlst = new ArrayList<Employee>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp givendate = null;
			java.util.Date selectedDate = sdf.parse(onGivenDate);
			givendate = new Timestamp(selectedDate.getTime());
		int OGDTotal = 0;
		int OGDTotalCitizens = 0;
		int OGDTotalPR = 0;
		int OGDTotalOthers = 0;
		int OGDResignTotal = 0;
		int OGDResignTotalCitizens = 0;
		int OGDResignTotalPR = 0;
		int OGDResignTotalOthers = 0;
		boolean futureDate1 = false;
		LocalDate nowlocalDate = java.time.LocalDate.now();
		Date checkFutureDate = java.sql.Date.valueOf(nowlocalDate);
		boolean futureDate = false;
		Calendar now = Calendar.getInstance();
		int yyyy = now.get(Calendar.YEAR);
		int mm = now.get(Calendar.MONTH) + 1;
		now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date dd = now.getTime();
        String d2= sdf.format(dd);
        Timestamp endDate = null;
        Date endDate1 = null;
		try {
			 endDate1 = sdf.parse(d2);
			 endDate = new Timestamp(endDate1.getTime()); 
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Date startdat1 = sdf.parse(sdf.format(new Date()));
        Timestamp startdat = null;
        startdat = new Timestamp(startdat1.getTime());
        if(givendate.after(startdat)){
        	futureDate = true;
		}
      
       
		
		/* if given date is before or equal to current date 
		 * we are calculating total acctive no of employees on given date + exited employees on or after given date - actual date of joing after given date  */
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (!employeeDetailList.isEmpty()) {
			if(futureDate == false){
			Iterator itr = employeeDetailList.iterator();
			while (itr.hasNext()) {
				Employee employee = (Employee) itr.next();
				boolean check = false;	
				if ("Active".equalsIgnoreCase(employee.getEmployeePersonalDetails().getEmployee_status())
						&& "Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())
						&& "Helius".equalsIgnoreCase(employee.getEmployeeSalaryDetails().getPayroll_entity())
						){
				//	Timestamp actualDOJ = employee.getEmployeePersonalDetails().getActual_date_of_joining();
					if(employee.getEmployeePersonalDetails().getActual_date_of_joining()!=null){
						Timestamp actualDOJ = employee.getEmployeePersonalDetails().getActual_date_of_joining();
						actualDOJ.setHours(0);
						actualDOJ.setMinutes(0);
						actualDOJ.setSeconds(0);
						if(givendate.after(actualDOJ) || givendate.equals(actualDOJ)){
							check=true;			
						}
						}else if(employee.getEmployeeOfferDetails().getExpected_date_of_joining()!=null){
							Timestamp	actualDOJ = employee.getEmployeeOfferDetails().getExpected_date_of_joining();
							actualDOJ.setHours(0);
							actualDOJ.setMinutes(0);
							actualDOJ.setSeconds(0);
							if(givendate.after(actualDOJ) || givendate.equals(actualDOJ) ){
								check=true;			
							}
							}else{
								check=false;
								continue;
						}
					/**
					 * checks for emp  actual date of joining  which is after given date will be ignored if incase ADOJ is null den check for
					 * expected date of joinging or else ignore
					 * **/
					if(check==true){
					if ("Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDTotalCitizens++;
					}
					if ("PR".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDTotalPR++;
					}
					if (!"Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())
							&& !"PR".equalsIgnoreCase(
									employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDTotalOthers++;
					}
					if (employee.getEmployeeAssignmentDetails().getAccount_manager() == null
							|| "".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())
							|| "-".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
						if (accmgrmap.containsKey("No Am")) {
							accmgrlst = accmgrmap.get("No Am");
							accmgrlst.add(employee);
						} else {
							accmgrlst = new ArrayList<Employee>();
							accmgrlst.add(employee);
							accmgrmap.put("No Am", accmgrlst);
						}
					} else {
						if (accmgrmap.containsKey(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
							accmgrlst = accmgrmap.get(employee.getEmployeeAssignmentDetails().getAccount_manager());
							accmgrlst.add(employee);
						} else {
							accmgrlst = new ArrayList<Employee>();
							accmgrlst.add(employee);
							accmgrmap.put(employee.getEmployeeAssignmentDetails().getAccount_manager(), accmgrlst);
						}
					}
					OGDTotal++;
					// }
				}
			}
				// checking for exied employees whose relieving date is on or after given date will be included
				boolean exitcheck = false;
				if ("Exited".equalsIgnoreCase(employee.getEmployeePersonalDetails().getEmployee_status())
						&& "Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())
						&& "Helius".equalsIgnoreCase(employee.getEmployeeSalaryDetails().getPayroll_entity())){
					if(employee.getEmployeePersonalDetails().getRelieving_date()!=null){
						Timestamp lastWorKDay = employee.getEmployeePersonalDetails().getRelieving_date();
						lastWorKDay.setHours(0);
						lastWorKDay.setMinutes(0);
						lastWorKDay.setSeconds(0);
						if(lastWorKDay.after(givendate) || lastWorKDay.equals(givendate)){
							exitcheck=true;			
						}
						}else{
							exitcheck = false;
							continue;
						}
					if(exitcheck==true){
					if ("Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDTotalCitizens++;
					}
					if ("PR".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDTotalPR++;
					}
					if (!"Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())
							&& !"PR".equalsIgnoreCase(
									employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDTotalOthers++;
					}
					if (employee.getEmployeeAssignmentDetails().getAccount_manager() == null
							|| "".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())
							|| "-".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
						if (accmgrmap.containsKey("No Am")) {
							accmgrlst = accmgrmap.get("No Am");
							accmgrlst.add(employee);
						} else {
							accmgrlst = new ArrayList<Employee>();
							accmgrlst.add(employee);
							accmgrmap.put("No Am", accmgrlst);
						}
					} else {
						if (accmgrmap.containsKey(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
							accmgrlst = accmgrmap.get(employee.getEmployeeAssignmentDetails().getAccount_manager());
							accmgrlst.add(employee);
						} else {
							accmgrlst = new ArrayList<Employee>();
							accmgrlst.add(employee);
							accmgrmap.put(employee.getEmployeeAssignmentDetails().getAccount_manager(), accmgrlst);
						}
					}
					OGDTotal++;
					// }
					}
				}
		
			}
			HashMap<String, HashMap<String, ArrayList<Object>>> tempfinOGD =	returnAccountManagerMap(accmgrmap);
			map.put("OGDTotal",OGDTotal);
			map.put("OGDTotalCitizens",OGDTotalCitizens);
			map.put("OGDTotalPR",OGDTotalPR);
			map.put("OGDTotalOthers",OGDTotalOthers);
			map.put("OGDaccmgrmap",accmgrmap);
			map.put("tempfinOGD",tempfinOGD);
		}else{
			Iterator itr = employeeDetailList.iterator();
			while (itr.hasNext()) {
				boolean resignstatus = false;
				Employee employee = (Employee) itr.next();
				if (employee.getEmployeePersonalDetails().getRelieving_date() != null) {
					Timestamp date = employee.getEmployeePersonalDetails().getRelieving_date();
					date.setHours(0);
					date.setMinutes(0);
					date.setSeconds(0);
					if(date.after(startdat) || date.equals(startdat)){
						if(date.before(givendate) || date.equals(givendate)){	
							resignstatus = true;
					}
					}
				}
				/*
				 * resignstatus = true when lastworking date is between i.e., after or equal to current date AND before or equal to givendate
				 * */
				if (resignstatus == true && "Active".equalsIgnoreCase(employee.getEmployeePersonalDetails().getEmployee_status())
						&& "Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())
						&& "Helius".equalsIgnoreCase(employee.getEmployeeSalaryDetails().getPayroll_entity())
						&& "YES".equalsIgnoreCase(employee.getEmployeePersonalDetails().getOn_notice_period())){
					if ("Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDResignTotalCitizens++;
					}
					if ("PR".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDResignTotalPR++;
					}
					if (!"Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())
							&& !"PR".equalsIgnoreCase(
									employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
						OGDResignTotalOthers++;
					}
					if (employee.getEmployeeAssignmentDetails().getAccount_manager() == null
							|| "".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())
							|| "-".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
						if (accmgrmap.containsKey("No Am")) {
							accmgrlst = accmgrmap.get("No Am");
							accmgrlst.add(employee);
						} else {
							accmgrlst = new ArrayList<Employee>();
							accmgrlst.add(employee);
							accmgrmap.put("No Am", accmgrlst);
						}
					} else {
						if (accmgrmap.containsKey(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
							accmgrlst = accmgrmap.get(employee.getEmployeeAssignmentDetails().getAccount_manager());
							accmgrlst.add(employee);
						} else {
							accmgrlst = new ArrayList<Employee>();
							accmgrlst.add(employee);
							accmgrmap.put(employee.getEmployeeAssignmentDetails().getAccount_manager(), accmgrlst);
						}
					}
					OGDResignTotal++;
					// }
				}
			}
			HashMap<String, HashMap<String, ArrayList<Object>>> tempfinResignOGD =	returnAccountManagerMap(accmgrmap);
			map.put("OGDResignTotal",OGDResignTotal);
			map.put("OGDResignTotalCitizens",OGDResignTotalCitizens);
			map.put("OGDResignTotalPR",OGDResignTotalPR);
			map.put("OGDResignTotalOthers",OGDResignTotalOthers);
			map.put("OGDResignaccmgrmap",accmgrmap);
			map.put("tempfinOGDResign",tempfinResignOGD);
			
			int roTotal = 0;
			int roTotalCitizens = 0;
			int roTotalPR = 0;
			int roTotalOthers = 0;
			Calendar expectedCal = Calendar.getInstance();
			HashMap<String, ArrayList<Employee>> offerAccmgrmap = new HashMap<String, ArrayList<Employee>>();
			ArrayList<Employee> offerAccmgrlst = new ArrayList<Employee>();
			if (!offerDetailList.isEmpty()) {
				Iterator roitr = offerDetailList.iterator();
				while (roitr.hasNext()) {
					Employee offer = (Employee) roitr.next();
					boolean joiningstatus = false;
					
					if (offer.getEmployeeOfferDetails().getExpected_date_of_joining() != null) {
						Timestamp date = offer.getEmployeeOfferDetails().getExpected_date_of_joining();
						date.setHours(0);
						date.setMinutes(0);
						date.setSeconds(0);
						if(date.after(startdat) || date.equals(startdat)){
							if(date.before(givendate) || date.equals(givendate)){	
							joiningstatus = true;
						}
						}
					}
					if ("rolled_out".equalsIgnoreCase(offer.getEmployeeOfferDetails().getOffer_status())
							&& "Singapore".equalsIgnoreCase(offer.getEmployeeOfferDetails().getWork_country())
							&& "Helius".equalsIgnoreCase(offer.getEmployeeSalaryDetails().getPayroll_entity()) && joiningstatus == true) {
						if ("Citizen".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())) {
							roTotalCitizens++;
						}
						if ("PR".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())) {
							roTotalPR++;
						}
						if (!"Citizen".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())
								&& !"PR".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())) {
							roTotalOthers++;
						}
						if (offer.getEmployeeOfferDetails().getAccount_manager() == null
								|| "".equalsIgnoreCase(offer.getEmployeeOfferDetails().getAccount_manager())
								|| "-".equalsIgnoreCase(offer.getEmployeeOfferDetails().getAccount_manager())) {
							if (offerAccmgrmap.containsKey("No Am")) {
								offerAccmgrlst = offerAccmgrmap.get("No Am");
								offerAccmgrlst.add(offer);
							} else {
								offerAccmgrlst = new ArrayList<Employee>();
								offerAccmgrlst.add(offer);
								offerAccmgrmap.put("No Am", offerAccmgrlst);
							}
						} else {
							if (offerAccmgrmap.containsKey(offer.getEmployeeOfferDetails().getAccount_manager())) {
								offerAccmgrlst = offerAccmgrmap
										.get(offer.getEmployeeOfferDetails().getAccount_manager());
								offerAccmgrlst.add(offer);
							} else {
								offerAccmgrlst = new ArrayList<Employee>();
								offerAccmgrlst.add(offer);
								offerAccmgrmap.put(offer.getEmployeeOfferDetails().getAccount_manager(),
										offerAccmgrlst);
							}
						}
						roTotal++;
					}
				}
				HashMap<String, HashMap<String, ArrayList<Object>>> tempfinROOGD =	returnROAccountManagerMap(offerAccmgrmap);
				map.put("OGDROTotal",roTotal);
				map.put("OGDROTotalCitizens",roTotalCitizens);
				map.put("OGDROTotalPR",roTotalPR);
				map.put("OGDROTotalOthers",roTotalOthers);
				map.put("OGDROaccmgrmap",offerAccmgrmap);
				map.put("tempfinOGDRO",tempfinROOGD);
			}
		}		
		}
		return map;
	}

	
	public ResponseEntity<byte[]> localizationReport(JSONObject Json,ArrayList<Object> employeeDetailList,ArrayList<Object> offerDetailList) throws Exception  {
	//	ArrayList<Object> employeeDetailList = getEmployeeObj();
	//	ArrayList<Object> offerDetailList = getOfferObj();
		String employeeListJson = null;
		String empdetailsjson = "";
		String empjson = "";
		int Total = 0;
		int TotalCitizens = 0;
		int TotalPR = 0;
		int TotalOthers = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ResponseEntity<byte[]> responseEntity = null;
		List<String> accountmanager = new ArrayList<String>();
		HttpHeaders headers = new HttpHeaders();
		HashMap<String,Object> totalmapOnGivenDate=null;
		/**
		 *@author vinay
		 * future date boolean is true when given date is after current date else false if its before
		 * if future date is false then only current percentage on given date is calulated
		 * whereas if its true then the projected percentage will be calculated between current date to given date
		 *  **/
		boolean futureDate = false;
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date dd = now.getTime();
        String d2= sdf.format(dd);
        Date endDate1 = null;
        Timestamp endDate = null;
		try {
			 endDate1 = sdf.parse(d2);
			 endDate = new Timestamp(endDate1.getTime());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Date startdat1 = sdf.parse(sdf.format(new Date()));
        Timestamp startdat = null;
        startdat = new Timestamp(startdat1.getTime());
        String onGivenDate = null;
		java.util.Date selectedDate = null;
		Timestamp givendate = null;
		if(Json.get("fromDate")!=null){
			onGivenDate = Json.get("fromDate").toString();
			selectedDate = sdf.parse(onGivenDate);
			givendate = new Timestamp(selectedDate.getTime());
			if(givendate.after(startdat)){
	        	futureDate = true;
			}
			}
        /*if(selectedDate.after(startdat)){
        	futureDate = true;
		}*/
        
		List hd = new ArrayList<String>();
		try {
		ArrayList<String> acmngrJson = (ArrayList<String>) Json.get("accountmanager");
		int OGDTotal = 0;
		int OGDTotalCitizens = 0;
		int OGDTotalPR = 0;
		int OGDTotalOthers = 0;
		int OGDResignTotal = 0;
		int OGDResignTotalCitizens = 0;
		int OGDResignTotalPR = 0;
		int OGDResignTotalOthers = 0;
		int OGDROTotal = 0;
		int OGDROTotalCitizens = 0;
		int OGDROTotalPR = 0;
		int OGDROTotalOthers = 0;
		/**
		 * @author vinay
		 * if the date is selected 
		 * preparing AccountManager map for the givendate to be calculated based on its period past or future date
		 * returns map with key accountmanager, and values as workpermit type and emp obj
		 * **/
		/* Calculating total active for the given date if its past
		 * if its future date then it returns open offers and resignations as well  between current and future date 
		 * */
		if(onGivenDate!=null){
		 totalmapOnGivenDate = getAccMngrMApOngivendate(employeeDetailList,offerDetailList,onGivenDate);
		 if(futureDate == false){
		 OGDTotal = (int) totalmapOnGivenDate.get("OGDTotal");
		 OGDTotalCitizens = (int) totalmapOnGivenDate.get("OGDTotalCitizens");
		 OGDTotalPR = (int) totalmapOnGivenDate.get("OGDTotalPR");
		 OGDTotalOthers = (int)totalmapOnGivenDate.get("OGDTotalOthers");
		 }else{
			 OGDResignTotal = (int) totalmapOnGivenDate.get("OGDResignTotal");
			 OGDResignTotalCitizens = (int) totalmapOnGivenDate.get("OGDResignTotalCitizens");
			 OGDResignTotalPR = (int) totalmapOnGivenDate.get("OGDResignTotalPR");
			 OGDResignTotalOthers = (int)totalmapOnGivenDate.get("OGDResignTotalOthers");
			 OGDROTotal = (int) totalmapOnGivenDate.get("OGDROTotal");
			 OGDROTotalCitizens = (int) totalmapOnGivenDate.get("OGDROTotalCitizens");
			 OGDROTotalPR = (int) totalmapOnGivenDate.get("OGDROTotalPR");
			 OGDROTotalOthers = (int)totalmapOnGivenDate.get("OGDROTotalOthers");
		 }
		}
			HashMap<String, ArrayList<Employee>> accmgrmap = new HashMap<String, ArrayList<Employee>>();
			ArrayList<Employee> accmgrlst = new ArrayList<Employee>();
			HashMap<String, HashMap<String, ArrayList<Object>>> tempfin = new HashMap<String, HashMap<String, ArrayList<Object>>>();
		/*	if(acmngrJson.contains("All")){	
				String Amlists = Utils.getHapProperty("VendorAMList");
				String[] splitAMs = Amlists.split(",");
				for(String splitAM : splitAMs){		
					accountmanager.add(splitAM);
				}
				*/		
			/**
			 * fetching every active employee till current date and forming map of accountmanager and its respective emp 
			 * filtering  with Citizen,Pr and remaining employees whose accMgr is null or empty will be added into "NO AM" map key  
			 * **/
			if (!employeeDetailList.isEmpty()) {
				Iterator itr = employeeDetailList.iterator();
				while (itr.hasNext()) {
					Employee employee = (Employee) itr.next();
					if ("Active".equalsIgnoreCase(employee.getEmployeePersonalDetails().getEmployee_status())
							&& "Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())
							&& "Helius".equalsIgnoreCase(employee.getEmployeeSalaryDetails().getPayroll_entity())) {
						if ("Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
							TotalCitizens++;
						}
						if ("PR".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
							TotalPR++;
						}
						if (!"Citizen".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_permit_name())
								&& !"PR".equalsIgnoreCase(
										employee.getEmployeeWorkPermitDetails().getWork_permit_name())) {
							TotalOthers++;
						}
						if (employee.getEmployeeAssignmentDetails().getAccount_manager() == null
								|| "".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())
								|| "-".equalsIgnoreCase(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
							if (accmgrmap.containsKey("No Am")) {
								accmgrlst = accmgrmap.get("No Am");
								accmgrlst.add(employee);
							} else {
								accmgrlst = new ArrayList<Employee>();
								accmgrlst.add(employee);
								accmgrmap.put("No Am", accmgrlst);
							}
						} else {
							if (accmgrmap.containsKey(employee.getEmployeeAssignmentDetails().getAccount_manager())) {
								accmgrlst = accmgrmap.get(employee.getEmployeeAssignmentDetails().getAccount_manager());
								accmgrlst.add(employee);
							} else {
								accmgrlst = new ArrayList<Employee>();
								accmgrlst.add(employee);
								accmgrmap.put(employee.getEmployeeAssignmentDetails().getAccount_manager(), accmgrlst);
							}
						}
						Total++;
					}
				}
				System.out.println("=======Total++======" + Total);
				System.out.println("=======TotalOthers++======" + TotalOthers);
				System.out.println("=======TotalPR++======" + TotalPR);
				System.out.println("=======TotalCitizens++======" + TotalCitizens);
				for (String m : accmgrmap.keySet()) {
					System.out.println("=======accman======" + m);
					ArrayList<Employee> val = accmgrmap.get(m);
					System.out.println("=======accmanvalsize======" + val.size());
					int AccMgrCitizen = 0;
					HashMap<String, ArrayList<Object>> temp = new HashMap<String, ArrayList<Object>>();
					ArrayList<Object> ala = new ArrayList<Object>();
					for (Employee emp : val) {
						String visaExist = null;
						if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
								|| "PR".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
							visaExist = emp.getEmployeeWorkPermitDetails().getWork_permit_name();
						} else {
							visaExist = "Others";
						}
						if (temp.containsKey(visaExist)) {
							if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
									|| "PR".equalsIgnoreCase(
											emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
								ala = temp.get(emp.getEmployeeWorkPermitDetails().getWork_permit_name());
							} else {
								ala = temp.get("Others");
							}
							HashMap<String, Object> resultmap = new HashMap<String, Object>();
							resultmap.put("Id", emp.getEmployeePersonalDetails().getEmployee_id());
							resultmap.put("name", emp.getEmployeePersonalDetails().getEmployee_name());
							ala.add(resultmap);
						} else {
							ala = new ArrayList<Object>();
							HashMap<String, Object> resultmap = new HashMap<String, Object>();
							resultmap.put("Id", emp.getEmployeePersonalDetails().getEmployee_id());
							resultmap.put("name", emp.getEmployeePersonalDetails().getEmployee_name());
							ala.add(resultmap);
							if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
									|| "PR".equalsIgnoreCase(
											emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
								temp.put(emp.getEmployeeWorkPermitDetails().getWork_permit_name(), ala);
							} else {
								temp.put("Others", ala);
							}
						}
					}
					tempfin.put(m, temp);
				}
			}
			int roTotal = 0;
			int roTotalCitizens = 0;
			int roTotalPR = 0;
			int roTotalOthers = 0;
			/**
			 * calculating rolled out offers from current date to end of the month where offer expected joining is  equal or later then the current
			 * date and before or on end of the month 
			 * **/
			HashMap<String, ArrayList<Employee>> offerAccmgrmap = new HashMap<String, ArrayList<Employee>>();
			ArrayList<Employee> offerAccmgrlst = new ArrayList<Employee>();
			HashMap<String, ArrayList<Employee>> accmgrVisaStatusmap = new HashMap<String, ArrayList<Employee>>();
			ArrayList<Employee> accmgrVisaStatuslst = new ArrayList<Employee>();
			HashMap<String, HashMap<String, ArrayList<Object>>> tempfinRO = new HashMap<String, HashMap<String, ArrayList<Object>>>();
			if (!offerDetailList.isEmpty()) {
				Iterator itr = offerDetailList.iterator();
				while (itr.hasNext()) {
					Employee offer = (Employee) itr.next();
					boolean joiningstatus = false;
					if (offer.getEmployeeOfferDetails().getExpected_date_of_joining() != null) {
						Timestamp date = offer.getEmployeeOfferDetails().getExpected_date_of_joining();
						date.setHours(0);
						date.setMinutes(0);
						date.setSeconds(0);
						if(date.after(startdat) || date.equals(startdat)){
							if(date.before(endDate) || date.equals(endDate)){	
							joiningstatus = true;
						}
						}
					}
					if ("rolled_out".equalsIgnoreCase(offer.getEmployeeOfferDetails().getOffer_status())
							&& "Singapore".equalsIgnoreCase(offer.getEmployeeOfferDetails().getWork_country())
							&& "Helius".equalsIgnoreCase(offer.getEmployeeSalaryDetails().getPayroll_entity()) && joiningstatus == true) {
						if ("Citizen".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())) {
							roTotalCitizens++;
						}
						if ("PR".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())) {
							roTotalPR++;
						}
						if (!"Citizen".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())
								&& !"PR".equalsIgnoreCase(offer.getEmployeeOfferDetails().getVisa_status())) {
							roTotalOthers++;
						}
						if (offer.getEmployeeOfferDetails().getAccount_manager() == null
								|| "".equalsIgnoreCase(offer.getEmployeeOfferDetails().getAccount_manager())
								|| "-".equalsIgnoreCase(offer.getEmployeeOfferDetails().getAccount_manager())) {
							if (offerAccmgrmap.containsKey("No Am")) {
								offerAccmgrlst = offerAccmgrmap.get("No Am");
								offerAccmgrlst.add(offer);
							} else {
								offerAccmgrlst = new ArrayList<Employee>();
								offerAccmgrlst.add(offer);
								offerAccmgrmap.put("No Am", offerAccmgrlst);
							}
						} else {
							if (offerAccmgrmap.containsKey(offer.getEmployeeOfferDetails().getAccount_manager())) {
								offerAccmgrlst = offerAccmgrmap
										.get(offer.getEmployeeOfferDetails().getAccount_manager());
								offerAccmgrlst.add(offer);
							} else {
								offerAccmgrlst = new ArrayList<Employee>();
								offerAccmgrlst.add(offer);
								offerAccmgrmap.put(offer.getEmployeeOfferDetails().getAccount_manager(),
										offerAccmgrlst);
							}
						}
						roTotal++;
					}
				}
				System.out.println("=======r0Total++======" + roTotal);
				System.out.println("=======roTotalOthers++======" + roTotalOthers);
				System.out.println("=======roTotalPR++======" + roTotalPR);
				System.out.println("=======roTotalCitizens++======" + roTotalCitizens);
				for (String roAm : offerAccmgrmap.keySet()) {
					System.out.println("=======offaccman======" + roAm);
					ArrayList<Employee> offval = offerAccmgrmap.get(roAm);
					System.out.println("=======offaccmanvalsize======" + offval.size());
					HashMap<String, ArrayList<Object>> rotemp = new HashMap<String, ArrayList<Object>>();
					ArrayList<Object> ala = new ArrayList<Object>();
					for (Employee off : offval) {
						if ("Citizen".equalsIgnoreCase(off.getEmployeeOfferDetails().getVisa_status())
								|| "PR".equalsIgnoreCase(off.getEmployeeOfferDetails().getVisa_status())) {
							if (rotemp.containsKey(off.getEmployeeOfferDetails().getVisa_status())) {
								ala = rotemp.get(off.getEmployeeOfferDetails().getVisa_status());
								HashMap<String, Object> resultmap = new HashMap<String, Object>();
								resultmap.put("Id", off.getEmployeeOfferDetails().getOffer_id());
								resultmap.put("name", off.getEmployeeOfferDetails().getEmployee_name());
								resultmap.put("expectedDOJ", off.getEmployeeOfferDetails().getExpected_date_of_joining());
								ala.add(resultmap);
							} else {
								ala = new ArrayList<Object>();
								HashMap<String, Object> resultmap = new HashMap<String, Object>();
								resultmap.put("Id", off.getEmployeeOfferDetails().getOffer_id());
								resultmap.put("name", off.getEmployeeOfferDetails().getEmployee_name());
								resultmap.put("expectedDOJ", off.getEmployeeOfferDetails().getExpected_date_of_joining());
								ala.add(resultmap);
								rotemp.put(off.getEmployeeOfferDetails().getVisa_status(), ala);
							}
						} else {
							if (rotemp.containsKey("Others")) {
								ala = rotemp.get("Others");
								HashMap<String, Object> resultmap = new HashMap<String, Object>();
								resultmap.put("Id", off.getEmployeeOfferDetails().getOffer_id());
								resultmap.put("name", off.getEmployeeOfferDetails().getEmployee_name());
								resultmap.put("expectedDOJ", off.getEmployeeOfferDetails().getExpected_date_of_joining());
								ala.add(resultmap);
							} else {
								ala = new ArrayList<Object>();
								HashMap<String, Object> resultmap = new HashMap<String, Object>();
								resultmap.put("Id", off.getEmployeeOfferDetails().getOffer_id());
								resultmap.put("name", off.getEmployeeOfferDetails().getEmployee_name());
								resultmap.put("expectedDOJ", off.getEmployeeOfferDetails().getExpected_date_of_joining());
								ala.add(resultmap);
								rotemp.put("Others", ala);
							}
						}
					}
					tempfinRO.put(roAm, rotemp);
				}
			}
				
			int resignTotal = 0;
			int resignTotalCitizens = 0;
			int resignTotalPR = 0;
			int resignTotalOthers = 0;
			/**
			 * calculating resignations of the active employees whose last working date is on or after current date and before or on end of the current month
			 * **/
			HashMap<String, ArrayList<Employee>> resignAccmgrmap = new HashMap<String, ArrayList<Employee>>();
			HashMap<String, ArrayList<Employee>> resaccmgrVisaStatusmap = new HashMap<String, ArrayList<Employee>>();
			ArrayList<Employee> resaccmgrVisaStatuslst = new ArrayList<Employee>();
			HashMap<String, HashMap<String, ArrayList<Object>>> tempfinResign = new HashMap<String, HashMap<String, ArrayList<Object>>>();
			if (!accmgrmap.isEmpty()) {
				String accMgr = null;
				for (String AM : accmgrmap.keySet()) {
					accMgr = AM;
					ArrayList<Employee> resignAccmgrlst = new ArrayList<Employee>();
					ArrayList<Employee> resEmp = accmgrmap.get(AM);
					Iterator itr = resEmp.iterator();
					while (itr.hasNext()) {
						Employee resignemp = (Employee) itr.next();
						boolean resignstatus = false;
						if (resignemp.getEmployeePersonalDetails().getRelieving_date() != null) {
							Timestamp date = resignemp.getEmployeePersonalDetails().getRelieving_date();
							date.setHours(0);
							date.setMinutes(0);
							date.setSeconds(0);
							if(date.after(startdat) || date.equals(startdat)){
								if(date.before(endDate) || date.equals(endDate)){	
									resignstatus = true;
							}
							}
						}
							if ("YES".equalsIgnoreCase(resignemp.getEmployeePersonalDetails().getOn_notice_period()) && resignstatus == true) {
							if ("Citizen"
									.equalsIgnoreCase(resignemp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
								resignTotalCitizens++;
							}
							if ("PR".equalsIgnoreCase(resignemp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
								resignTotalPR++;
							}
							if (!"Citizen"
									.equalsIgnoreCase(resignemp.getEmployeeWorkPermitDetails().getWork_permit_name())
									&& !"PR".equalsIgnoreCase(
											resignemp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
								resignTotalOthers++;
							}
							resignAccmgrlst.add(resignemp);
							resignTotal++;
						}
					}
					if(!resignAccmgrlst.isEmpty()){
					resignAccmgrmap.put(AM, resignAccmgrlst);
					}
				}
				System.out.println("=======resignTotal++======" + resignTotal);
				System.out.println("=======resignTotalOthers++======" + resignTotalOthers);
				System.out.println("=======resignTotalPR++======" + resignTotalPR);
				System.out.println("=======resignTotalCitizens++======" + resignTotalCitizens);
				for (String m : resignAccmgrmap.keySet()) {
					System.out.println("=======resignaccman======" + m);
					ArrayList<Employee> val = resignAccmgrmap.get(m);
					System.out.println("=======resignaccmanvalsize======" + val.size());
					int AccMgrCitizen = 0;
					HashMap<String, ArrayList<Object>> temp = new HashMap<String, ArrayList<Object>>();
					ArrayList<Object> ala = new ArrayList<Object>();
					for (Employee emp : val) {
						String visaExist = null;
						if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
								|| "PR".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
							visaExist = emp.getEmployeeWorkPermitDetails().getWork_permit_name();
						} else {
							visaExist = "Others";
						}
						if (temp.containsKey(visaExist)) {
							if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
									|| "PR".equalsIgnoreCase(
											emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
								ala = temp.get(emp.getEmployeeWorkPermitDetails().getWork_permit_name());
							} else {
								ala = temp.get("Others");
							}
							HashMap<String, Object> resultmap = new HashMap<String, Object>();
							resultmap.put("Id", emp.getEmployeePersonalDetails().getEmployee_id());
							resultmap.put("name", emp.getEmployeePersonalDetails().getEmployee_name());
							resultmap.put("lastWorkingdate", emp.getEmployeePersonalDetails().getRelieving_date());

							ala.add(resultmap);
						} else {
							ala = new ArrayList<Object>();
							HashMap<String, Object> resultmap = new HashMap<String, Object>();
							resultmap.put("Id", emp.getEmployeePersonalDetails().getEmployee_id());
							resultmap.put("name", emp.getEmployeePersonalDetails().getEmployee_name());
							resultmap.put("lastWorkingdate", emp.getEmployeePersonalDetails().getRelieving_date());
							ala.add(resultmap);
							if ("Citizen".equalsIgnoreCase(emp.getEmployeeWorkPermitDetails().getWork_permit_name())
									|| "PR".equalsIgnoreCase(
											emp.getEmployeeWorkPermitDetails().getWork_permit_name())) {
								temp.put(emp.getEmployeeWorkPermitDetails().getWork_permit_name(), ala);
							} else {
								temp.put("Others", ala);
							}
						}
					}
					tempfinResign.put(m, temp);
				}
			}
	
			LocalDate dateTime= java.time.LocalDate.now(); 
			XSSFWorkbook workbook = new XSSFWorkbook();
			// Create a blank sheet
			XSSFSheet sheet = workbook.createSheet("Localization Details");
			CreationHelper creationHelper = workbook.getCreationHelper();
			CellStyle dateStyle = workbook.createCellStyle(); 
			dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("mm-dd-yyyy hh:mm:ss"));
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderLeft(BorderStyle.HAIR);
			Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);
			int rownum = 0;
			Row header = sheet.createRow(0);
			Cell headerCell = header.createCell(0);
			headerCell.setCellValue(" ");
			headerCell.setCellStyle(headerStyle);
			Row header2 = sheet.createRow(1);
			Cell header2Cell = header2.createCell(0);
			header2Cell.setCellValue("Total am Report As On "+dateTime);
			CellStyle header2Style = workbook.createCellStyle();
			header2Style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			header2Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			header2Style.setBorderLeft(BorderStyle.HAIR);
			header2Style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
			header2Cell.setCellStyle(header2Style);
			Row header3 = sheet.createRow(2);
			Cell header3Cell = header3.createCell(0);
			header3Cell.setCellValue("Citizens");
			Row header4 = sheet.createRow(3);
			Cell header4Cell = header4.createCell(0);
			header4Cell.setCellValue("PR");
			Row header5 = sheet.createRow(4);
			Cell header5Cell = header5.createCell(0);
			header5Cell.setCellValue("Others");
			Row header7 = sheet.createRow(6);
			Cell header7Cell = header7.createCell(0);
			header7Cell.setCellValue("Offer rolled out");
			header7Cell.setCellStyle(header2Style);
			Row header8 = sheet.createRow(7);
			Cell header8Cell = header8.createCell(0);
			header8Cell.setCellValue("Citizen");
			Row header9 = sheet.createRow(8);
			Cell header9Cell = header9.createCell(0);
			header9Cell.setCellValue("PR");
			Row header10 = sheet.createRow(9);
			Cell header10Cell = header10.createCell(0);
			header10Cell.setCellValue("Others");
			Row header12 = sheet.createRow(11);
			Cell header12Cell = header12.createCell(0);
			header12Cell.setCellValue("Resignations");
			header12Cell.setCellStyle(header2Style);
			Row header13 = sheet.createRow(12);
			Cell header13Cell = header13.createCell(0);
			header13Cell.setCellValue("Citizen");
			Row header14 = sheet.createRow(13);
			Cell header14Cell = header14.createCell(0);
			header14Cell.setCellValue("PR");
			Row header15 = sheet.createRow(14);
			Cell header15Cell = header15.createCell(0);
			header15Cell.setCellValue("Others");
			Row header18 = sheet.createRow(17);
			Cell header18Cell = header18.createCell(0);
			header18Cell.setCellValue("current SG+PR /Total resources As On "+dateTime +" (%)");
			//sheet.autoSizeColumn(header18Cell.getColumnIndex());
			header18Cell.setCellStyle(header2Style);
			Row header20 = sheet.createRow(19);
			Cell header20Cell = header20.createCell(0);
			header20Cell.setCellValue("Projected SG+PR /Total resources From "+dateTime+" To "+d2 +" (%)");
			sheet.autoSizeColumn(header20Cell.getColumnIndex());
			header20Cell.setCellStyle(header2Style);
			Row header22 = null;
			Cell header22Cell = null;
			Row header23 = null;
			Cell header23Cell = null;
			Row header24 = null;
			Cell header24Cell = null;
			Row header25 = null;
			Cell header25Cell = null;
			Row header27 = null;
			Cell header27Cell = null;
			Row header28 = null;
			Cell header28Cell = null;
			Row header29 = null;
			Cell header29Cell = null;
			Row header30 = null;
			Cell header30Cell = null;
			Row header33 = null;
			Cell header33Cell = null;
			/**
			 * the ongivendate and future date are used to display the past and future date report 
			 * if ongivendate is null only default as on date and projection for the current month will be displayed
			 * otherwise based on future date it will distinguish to print only past active emplee percentage as in when futuredate is false
			 * when futureDate is true will print the projection of selected date
			 * **/
			if(onGivenDate!=null){
			 header22 = sheet.createRow(21);
			 header22Cell = header22.createCell(0);
			 if(futureDate == false){
			 header22Cell.setCellValue("Total am Report As On " +onGivenDate);
			 }else{
				 header22Cell.setCellValue("Resigned Total From "+dateTime+" To "+onGivenDate);	 
			 }
			 header22Cell.setCellStyle(header2Style);
			 header23 = sheet.createRow(22);
			 header23Cell = header23.createCell(0);
			 header23Cell.setCellValue("Citizen");
			 header24 = sheet.createRow(23);
			 header24Cell = header24.createCell(0);
			 header24Cell.setCellValue("PR");
			 header25 = sheet.createRow(24);
			 header25Cell = header25.createCell(0);
			 header25Cell.setCellValue("Others");
			 if(futureDate == false){
				 header27 = sheet.createRow(26);
				 header27Cell = header27.createCell(0);
			 header27Cell.setCellValue("SG+PR /Total resources From "+dateTime+" To "+onGivenDate +" (%)");
			 header27Cell.setCellStyle(header2Style);
			 }else{
				 header27 = sheet.createRow(26);
				 header27Cell = header27.createCell(0);
				 header27Cell.setCellValue("Offer rolled out From "+dateTime+" To "+onGivenDate);
				 header27Cell.setCellStyle(header2Style);
				 header28 = sheet.createRow(27);
				 header28Cell = header28.createCell(0);
				 header28Cell.setCellValue("Citizen");
				 header29 = sheet.createRow(28);
				 header29Cell = header29.createCell(0);
				 header29Cell.setCellValue("PR");
				 header30 = sheet.createRow(29);
				 header30Cell = header30.createCell(0);
				 header30Cell.setCellValue("Others");
				 header33 = sheet.createRow(32);
				 header33Cell = header33.createCell(0);
				 header33Cell.setCellValue("Projected SG+PR /Total resources From "+dateTime+" To "+onGivenDate +" (%)");
				 header33Cell.setCellStyle(header2Style);
			 }
			}
			if(acmngrJson.contains("All")){	
				/*
				String Amlists = Utils.getHapProperty("VendorAMList");
				String[] splitAMs = Amlists.split(",");
				for(String splitAM : splitAMs){		
					accountmanager.add(splitAM);
				}
		*/	
		/*		Query AccMgrQuery = session.createSQLQuery("SELECT picklist_name from Picklist_Items WHERE picklist_type='accountmanager'");
				List<String> AccMgrlists = AccMgrQuery.list();
				Query query = session.createSQLQuery("select * from user").addEntity(com.helius.entities.User.class);
				List users = query.list();
				if (!users.isEmpty()) {
					Iterator iter = users.iterator();
					while (iter.hasNext()) {
						User user = (User) iter.next();
						String[] countries = user.getCountry().split(",");
						for (String country : countries) {
							if ("Singapore".equalsIgnoreCase(country)){
								if(AccMgrlists.contains(user.getUsername()))
									accountmanager.add(user.getUsername());	
								}
							}
						}	
				accountmanager.add("No Am");
				session.close();
				}*/
				/**
				 * forming set of accountmanager key from the available data of acctive,rolledout,and
				 *  resignations map if the date is selected themn its respective map key also added
				 * **/
				Set<String> amset = new HashSet<String>();
				if (!resignAccmgrmap.isEmpty()) {
					for (String am : resignAccmgrmap.keySet()) {
						amset.add(am);
					}
				}
				if (!offerAccmgrmap.isEmpty()) {
					for (String am : offerAccmgrmap.keySet()) {
						amset.add(am);
					}
				}
				if (!accmgrmap.isEmpty()) {
					for (String am : accmgrmap.keySet()) {
						amset.add(am);
					}
				}
				if(onGivenDate != null){
				if (futureDate == true) {
					HashMap<String, ArrayList<Employee>> OGDResignaccmgrmap = (HashMap<String, ArrayList<Employee>>) totalmapOnGivenDate
							.get("OGDResignaccmgrmap");
					if (!OGDResignaccmgrmap.isEmpty()) {
						for (String am : OGDResignaccmgrmap.keySet()) {
							amset.add(am);
						}
					}
					HashMap<String, ArrayList<Employee>> OGDROaccmgrmap = (HashMap<String, ArrayList<Employee>>) totalmapOnGivenDate
							.get("OGDROaccmgrmap");
					if (!OGDROaccmgrmap.isEmpty()) {
						for (String am : OGDROaccmgrmap.keySet()) {
							amset.add(am);
						}
					}
				} else {
					HashMap<String, ArrayList<Employee>> OGDaccmgrmap = (HashMap<String, ArrayList<Employee>>) totalmapOnGivenDate
							.get("OGDaccmgrmap");
					if (!OGDaccmgrmap.isEmpty()) {
						for (String am : OGDaccmgrmap.keySet()) {
							amset.add(am);
						}
					}
				}
				}
				for (String amSet : amset) {
					accountmanager.add(amSet);
				}
			} else {
				for (String amFilter : acmngrJson) {
					accountmanager.add(amFilter);
				}
		}
			
			/*
			 * cellnum = 1 is declared as row '0' is used for header
			 * */
			/**
			 * All the consturcted maps will be looped based on the key account manager and will print colum wise data 
			 * calculating size for the each workpermit and and total employees for the account manager 
			 * in all active map and rolledout and resignations map
			 * **/
			int cellnum = 1;			
			for (String key : accountmanager) {
				int citizenPercentage = 0;
				int PRPercentage = 0;
				int roCitizenPercentage = 0;
				int roPRPercentage = 0;
				int resignCitizenPercentage = 0;
				int resignPRPercentage = 0;
				int ogdCitizenPercentage = 0;
				int ogdPRPercentage = 0;
				int ogdResCitizenPercentage = 0;
				int ogdResPRPercentage = 0;
				int ogdRoCitizenPercentage = 0;
				int ogdRoPRPercentage = 0;
				int valsize = 0;
				int valRout = 0;
				int valRes = 0;
				int valOGDay = 0;
				int valOGDRES = 0;
				int valOGDRO = 0;
				System.out.println("==qqq====" + key);
				headerCell = header.createCell(cellnum++);
				headerCell.setCellValue(key);
				sheet.autoSizeColumn(headerCell.getColumnIndex());
				headerCell.setCellStyle(headerStyle);
				if (accmgrmap.containsKey(key)) {
					ArrayList<Employee> val = accmgrmap.get(key);
					valsize = val.size();
					header2Cell = header2.createCell(cellnum - 1);
					header2Cell.setCellValue(valsize);
					header2Cell.setCellStyle(header2Style);
					HashMap<String, ArrayList<Object>> hm = (HashMap<String, ArrayList<Object>>) tempfin.get(key);
					for (String visa : hm.keySet()) {
						ArrayList<Object> visastat = hm.get(visa);
						int index = cellnum - 1;
						int visaSize = visastat.size();
						if ("Citizen".equalsIgnoreCase(visa)) {
							header3Cell = header3.createCell(index);
							header3Cell.setCellValue((Integer) visaSize);
							citizenPercentage = (Integer) visaSize;
						}
						if ("PR".equalsIgnoreCase(visa)) {
							header4Cell = header4.createCell(index);
							header4Cell.setCellValue((Integer) visaSize);
							PRPercentage = (Integer) visaSize;
						}
						if ("Others".equalsIgnoreCase(visa)) {
							header5Cell = header5.createCell(index);
							header5Cell.setCellValue((Integer) visaSize);
						}
					}
					/*calculating active employees*/
					header18Cell = header18.createCell(cellnum - 1);
					float currentPercentage = (float)(citizenPercentage + PRPercentage) / valsize;
					header18Cell.setCellValue(currentPercentage*100);
					header18Cell.setCellStyle(dataStyle);
				} else {
					header2Cell = header2.createCell(cellnum - 1);
					header2Cell.setCellValue(0);
					header2Cell.setCellStyle(header2Style);
					header3Cell = header3.createCell(cellnum - 1);
					header3Cell.setCellValue(0);
					header4Cell = header4.createCell(cellnum - 1);
					header4Cell.setCellValue(0);
					header5Cell = header5.createCell(cellnum - 1);
					header5Cell.setCellValue(0);
					header18Cell = header18.createCell(cellnum - 1);
					header18Cell.setCellValue(0);
				//	header18Cell.setCellStyle(header2Style);
				}
				if (offerAccmgrmap.containsKey(key)) {
					ArrayList<Employee> valRO = offerAccmgrmap.get(key);
					 valRout = valRO.size();
					header7Cell = header7.createCell(cellnum - 1);
					header7Cell.setCellValue(valRout);
					header7Cell.setCellStyle(header2Style);
					HashMap<String, ArrayList<Object>> hmRO = (HashMap<String, ArrayList<Object>>) tempfinRO.get(key);
					for (String visa : hmRO.keySet()) {
						ArrayList<Object> visastat = hmRO.get(visa);
						int index = cellnum - 1;
						int visaSize = visastat.size();
						if ("Citizen".equalsIgnoreCase(visa)) {
							header8Cell = header8.createCell(index);
							header8Cell.setCellValue((Integer) visaSize);
							roCitizenPercentage = (Integer) visaSize;
						}
						if ("PR".equalsIgnoreCase(visa)) {
							header9Cell = header9.createCell(index);
							header9Cell.setCellValue((Integer) visaSize);
							roPRPercentage = (Integer) visaSize;
						}
						if ("Others".equalsIgnoreCase(visa)) {
							header10Cell = header10.createCell(index);
							header10Cell.setCellValue((Integer) visaSize);
						}
					}
				} else {
					header7Cell = header7.createCell(cellnum - 1);
					header7Cell.setCellValue(0);
					header7Cell.setCellStyle(header2Style);
					header8Cell = header8.createCell(cellnum - 1);
					header8Cell.setCellValue(0);
					header9Cell = header9.createCell(cellnum - 1);
					header9Cell.setCellValue(0);
					header10Cell = header10.createCell(cellnum - 1);
					header10Cell.setCellValue(0);
				}
				if (resignAccmgrmap.containsKey(key)) {
					ArrayList<Employee> valResign = resignAccmgrmap.get(key);
					valRes = valResign.size();
					header12Cell = header12.createCell(cellnum - 1);
					header12Cell.setCellValue(valRes);
					header12Cell.setCellStyle(header2Style);
					HashMap<String, ArrayList<Object>> hmResign = (HashMap<String, ArrayList<Object>>) tempfinResign
							.get(key);
					for (String visaResign : hmResign.keySet()) {
						ArrayList<Object> visastat = hmResign.get(visaResign);
						int index = cellnum - 1;
						int visaSize = visastat.size();
						if ("Citizen".equalsIgnoreCase(visaResign)) {
							header13Cell = header13.createCell(index);
							header13Cell.setCellValue((Integer) visaSize);
							resignCitizenPercentage = (Integer) visaSize;
						}
						if ("PR".equalsIgnoreCase(visaResign)) {
							header14Cell = header14.createCell(index);
							header14Cell.setCellValue((Integer) visaSize);
							resignPRPercentage = (Integer) visaSize;
						}
						if ("Others".equalsIgnoreCase(visaResign)) {
							header15Cell = header15.createCell(index);
							header15Cell.setCellValue((Integer) visaSize);
						}
					}
				} else {
					header12Cell = header12.createCell(cellnum - 1);
					header12Cell.setCellValue(0);
					header12Cell.setCellStyle(header2Style);
					header13Cell = header13.createCell(cellnum - 1);
					header13Cell.setCellValue(0);
					header14Cell = header14.createCell(cellnum - 1);
					header14Cell.setCellValue(0);
					header15Cell = header15.createCell(cellnum - 1);
					header15Cell.setCellValue(0);
				}
				/**
				 * calculating projected percentage for current month totalacctivemp + totalrolledoutoffers - totalresignations/total emp
				 * **/
				header20Cell = header20.createCell(cellnum-1);
				int approxCitizens = citizenPercentage + roCitizenPercentage - resignCitizenPercentage;
				int approxPR = PRPercentage + roPRPercentage - resignPRPercentage;
				int approxTotal = valsize + valRout - valRes;
				float projAMtotal = (float)(approxCitizens + approxPR) / approxTotal;
				Float projVal = Float.valueOf(projAMtotal);
				if (projVal.isNaN()){
					header20Cell.setCellValue(0.00);
				//	header20Cell.setCellStyle(header2Style);
				}else{ 
					header20Cell.setCellValue(projAMtotal*100);	
					header20Cell.setCellStyle(dataStyle);
				    }
				if(onGivenDate!=null){
					/**this block executes only when date is selected
					 * if futureDate is false then only past current percentage is calculated
					 * **/
					if(futureDate == false){
				HashMap<String, ArrayList<Employee>> OGDaccmgrmap = (HashMap<String, ArrayList<Employee>>) totalmapOnGivenDate.get("OGDaccmgrmap");
				if (OGDaccmgrmap.containsKey(key)) {
					ArrayList<Employee> valOGD = OGDaccmgrmap.get(key);
					valOGDay = valOGD.size();
					header22Cell = header22.createCell(cellnum - 1);
					header22Cell.setCellValue(valOGDay);
					header22Cell.setCellStyle(header2Style);
					HashMap<String, HashMap<String, ArrayList<Object>>> hmOgd = (HashMap<String, HashMap<String, ArrayList<Object>>>) totalmapOnGivenDate.get("tempfinOGD");
					HashMap<String, ArrayList<Object>> hmm = hmOgd.get(key);
					for (String visaOGD : hmm.keySet()) {
						ArrayList<Object> visastat = hmm.get(visaOGD);
						int index = cellnum - 1;
						int visaSize = visastat.size();
						if ("Citizen".equalsIgnoreCase(visaOGD)) {
							header23Cell = header23.createCell(index);
							header23Cell.setCellValue((Integer) visaSize);
							ogdCitizenPercentage = (Integer) visaSize;
						}
						if ("PR".equalsIgnoreCase(visaOGD)) {
							header24Cell = header24.createCell(index);
							header24Cell.setCellValue((Integer) visaSize);
							ogdPRPercentage = (Integer) visaSize;
						}
						if ("Others".equalsIgnoreCase(visaOGD)) {
							header25Cell = header25.createCell(index);
							header25Cell.setCellValue((Integer) visaSize);
						}
					}
					header27Cell = header27.createCell(cellnum - 1);
					float ogdCurrentPercentage = (float)(ogdCitizenPercentage + ogdPRPercentage) / valOGDay;
					header27Cell.setCellValue(ogdCurrentPercentage*100);
					header27Cell.setCellStyle(dataStyle);
				} else {
					/* this block executes when the accountmanager has no employees then printing its colum as zero*/
					header22Cell = header22.createCell(cellnum - 1);
					header22Cell.setCellValue(0);
					header22Cell.setCellStyle(header2Style);
					header23Cell = header23.createCell(cellnum - 1);
					header23Cell.setCellValue(0);
					header24Cell = header24.createCell(cellnum - 1);
					header24Cell.setCellValue(0);
					header25Cell = header25.createCell(cellnum - 1);
					header25Cell.setCellValue(0);
					header27Cell = header27.createCell(cellnum - 1);
					header27Cell.setCellValue(0);
				//	header27Cell.setCellStyle(header2Style);
				}
					}else{
						HashMap<String, ArrayList<Employee>> OGDResignaccmgrmap = (HashMap<String, ArrayList<Employee>>) totalmapOnGivenDate.get("OGDResignaccmgrmap");
						if (OGDResignaccmgrmap.containsKey(key)) {
							ArrayList<Employee> valOGDRes = OGDResignaccmgrmap.get(key);
							valOGDRES = valOGDRes.size();
							header22Cell = header22.createCell(cellnum - 1);
							header22Cell.setCellValue(valOGDRES);
							header22Cell.setCellStyle(header2Style);
							HashMap<String, HashMap<String, ArrayList<Object>>> tempfinOGDResign = (HashMap<String, HashMap<String, ArrayList<Object>>>) totalmapOnGivenDate.get("tempfinOGDResign");
							HashMap<String, ArrayList<Object>> hmm = tempfinOGDResign.get(key);
							for (String visaOGD : hmm.keySet()) {
								ArrayList<Object> visastat = hmm.get(visaOGD);
								int index = cellnum - 1;
								int visaSize = visastat.size();
								if ("Citizen".equalsIgnoreCase(visaOGD)) {
									header23Cell = header23.createCell(index);
									header23Cell.setCellValue((Integer) visaSize);
									ogdResCitizenPercentage = (Integer) visaSize;
								}
								if ("PR".equalsIgnoreCase(visaOGD)) {
									header24Cell = header24.createCell(index);
									header24Cell.setCellValue((Integer) visaSize);
									ogdResPRPercentage = (Integer) visaSize;
								}
								if ("Others".equalsIgnoreCase(visaOGD)) {
									header25Cell = header25.createCell(index);
									header25Cell.setCellValue((Integer) visaSize);
								}
							}
							
						} else {
							header22Cell = header22.createCell(cellnum - 1);
							header22Cell.setCellValue(0);
							header22Cell.setCellStyle(header2Style);
							header23Cell = header23.createCell(cellnum - 1);
							header23Cell.setCellValue(0);
							header24Cell = header24.createCell(cellnum - 1);
							header24Cell.setCellValue(0);
							header25Cell = header25.createCell(cellnum - 1);
							header25Cell.setCellValue(0);	
					}
						HashMap<String, ArrayList<Employee>> OGDRROaccmgrmap = (HashMap<String, ArrayList<Employee>>) totalmapOnGivenDate.get("OGDROaccmgrmap");
						if (OGDRROaccmgrmap.containsKey(key)) {
							ArrayList<Employee> valOGDRo = OGDRROaccmgrmap.get(key);
							valOGDRO = valOGDRo.size();
							header27Cell = header27.createCell(cellnum - 1);
							header27Cell.setCellValue(valOGDRO);
							header27Cell.setCellStyle(header2Style);
							HashMap<String, HashMap<String, ArrayList<Object>>> tempfinOGDRO = (HashMap<String, HashMap<String, ArrayList<Object>>>) totalmapOnGivenDate.get("tempfinOGDRO");
							HashMap<String, ArrayList<Object>> hmm = tempfinOGDRO.get(key);
							for (String visaOGD : hmm.keySet()) {
								ArrayList<Object> visastat = hmm.get(visaOGD);
								int index = cellnum - 1;
								int visaSize = visastat.size();
								if ("Citizen".equalsIgnoreCase(visaOGD)) {
									header28Cell = header28.createCell(index);
									header28Cell.setCellValue((Integer) visaSize);
									ogdRoCitizenPercentage = (Integer) visaSize;
								}
								if ("PR".equalsIgnoreCase(visaOGD)) {
									header29Cell = header29.createCell(index);
									header29Cell.setCellValue((Integer) visaSize);
									ogdRoPRPercentage = (Integer) visaSize;
								}
								if ("Others".equalsIgnoreCase(visaOGD)) {
									header30Cell = header30.createCell(index);
									header30Cell.setCellValue((Integer) visaSize);
								}
							}
							
						} else {
							header27Cell = header27.createCell(cellnum - 1);
							header27Cell.setCellValue(0);
							header27Cell.setCellStyle(header2Style);
							header28Cell = header28.createCell(cellnum - 1);
							header28Cell.setCellValue(0);
							header29Cell = header29.createCell(cellnum - 1);
							header29Cell.setCellValue(0);
							header30Cell = header30.createCell(cellnum - 1);
							header30Cell.setCellValue(0);	
					}
						header33Cell = header33.createCell(cellnum-1);
						/**
						 * calculating future date projected percentage 
						 * using current as on date total active emp + givendate total offers - givendate total resignation / total value
						 * **/
						int OGDProjCitizens = citizenPercentage + ogdRoCitizenPercentage - ogdResCitizenPercentage;
						int OGDProjPR = PRPercentage + ogdRoPRPercentage - ogdResPRPercentage;
						int OGDProjTotal = valsize + valOGDRO - valOGDRES;
						float ogdProjAMtotal = (float)(OGDProjCitizens + OGDProjPR) / OGDProjTotal;
						Float ogdProjVal = Float.valueOf(ogdProjAMtotal);
						if (ogdProjVal.isNaN()){
							header33Cell.setCellValue(0.00);
						//	header20Cell.setCellStyle(header2Style);
						}else{ 
							header33Cell.setCellValue(ogdProjAMtotal*100);	
							header33Cell.setCellStyle(dataStyle);
						    }
						
				}	
				}
				
			}
			headerCell = header.createCell(cellnum);
			headerCell.setCellValue("Total");
			headerCell.setCellStyle(headerStyle);
			header2Cell = header2.createCell(cellnum);
			header2Cell.setCellValue(Total);
			header2Cell.setCellStyle(header2Style);
			header3Cell = header3.createCell(cellnum);
			header3Cell.setCellValue(TotalCitizens);
			header4Cell = header4.createCell(cellnum);
			header4Cell.setCellValue(TotalPR);
			header5Cell = header5.createCell(cellnum);
			header5Cell.setCellValue(TotalOthers);
			header7Cell = header7.createCell(cellnum);
			header7Cell.setCellValue(roTotal);
			header7Cell.setCellStyle(header2Style);
			header8Cell = header8.createCell(cellnum);
			header8Cell.setCellValue(roTotalCitizens);
			header9Cell = header9.createCell(cellnum);
			header9Cell.setCellValue(roTotalPR);
			header10Cell = header10.createCell(cellnum);
			header10Cell.setCellValue(roTotalOthers);
			header12Cell = header12.createCell(cellnum);
			header12Cell.setCellValue(resignTotal);
			header12Cell.setCellStyle(header2Style);
			header13Cell = header13.createCell(cellnum);
			header13Cell.setCellValue(resignTotalCitizens);
			header14Cell = header14.createCell(cellnum);
			header14Cell.setCellValue(resignTotalPR);
			header15Cell = header15.createCell(cellnum);
			header15Cell.setCellValue(resignTotalOthers);
			header18Cell = header18.createCell(cellnum);
			float currentPercentageTotal = (float)(TotalCitizens + TotalPR) / Total;
			// currentPercentageTotal is the as on date current active emp
			currentPercentageTotal = currentPercentageTotal*100;
			header18Cell.setCellValue(currentPercentageTotal);
			header18Cell.setCellStyle(header2Style);
			header18Cell.setCellStyle(dataStyle);
			header20Cell = header20.createCell(cellnum);
			int projectedTotalCitizen = TotalCitizens + roTotalCitizens - resignTotalCitizens;
			int projectedTotalPR = TotalPR + roTotalPR - resignTotalPR;
			int projectedGrandTotal = Total + roTotal - resignTotal;
			float projectedPercentageTotal = (float)(projectedTotalCitizen + projectedTotalPR) / projectedGrandTotal;
			//projectedPercentageTotal is the total projected percentage for the current month
			projectedPercentageTotal = projectedPercentageTotal*100;
			header20Cell.setCellValue(projectedPercentageTotal);
			header20Cell.setCellStyle(header2Style);
			header20Cell.setCellStyle(dataStyle);
			float currentOGDPercentageTotal = 0;
			float ogdProjectedPercentageTotal = 0;
			//int ogdProjectedTotalCitizen = 0;
			//int ogdProjectedTotalPR = 0;
			int ogdProjectedGrandTotal = 0;
			if(onGivenDate!=null){
				if(futureDate == false){
			header22Cell = header22.createCell(cellnum);
			header22Cell.setCellValue(OGDTotal);
			header22Cell.setCellStyle(header2Style);
			header23Cell = header23.createCell(cellnum);
			header23Cell.setCellValue(OGDTotalCitizens);
			header24Cell = header24.createCell(cellnum);
			header24Cell.setCellValue(OGDTotalPR);
			header25Cell = header25.createCell(cellnum);
			header25Cell.setCellValue(OGDTotalOthers);
			currentOGDPercentageTotal = (float)(OGDTotalCitizens + OGDTotalPR) / OGDTotal;
			currentOGDPercentageTotal = currentOGDPercentageTotal*100;		
			header27Cell = header27.createCell(cellnum);
			header27Cell.setCellValue(currentOGDPercentageTotal);
			header27Cell.setCellStyle(header2Style);
			header27Cell.setCellStyle(dataStyle);
				}else{
					header22Cell = header22.createCell(cellnum);
					header22Cell.setCellValue(OGDResignTotal);
					header22Cell.setCellStyle(header2Style);
					header23Cell = header23.createCell(cellnum);
					header23Cell.setCellValue(OGDResignTotalCitizens);
					header24Cell = header24.createCell(cellnum);
					header24Cell.setCellValue(OGDResignTotalPR);
					header25Cell = header25.createCell(cellnum);
					header25Cell.setCellValue(OGDResignTotalOthers);
					header27Cell = header27.createCell(cellnum);
					header27Cell.setCellValue(OGDROTotal);
					header27Cell.setCellStyle(header2Style);
					header28Cell = header28.createCell(cellnum);
					header28Cell.setCellValue(OGDROTotalCitizens);
					header29Cell = header29.createCell(cellnum);
					header29Cell.setCellValue(OGDROTotalPR);
					header30Cell = header30.createCell(cellnum);
					header30Cell.setCellValue(OGDROTotalOthers);
					int ogdProjectedTotalCitizen = TotalCitizens + OGDROTotalCitizens - OGDResignTotalCitizens;
					int ogdProjectedTotalPR = TotalPR + OGDROTotalPR - OGDResignTotalPR;
					ogdProjectedGrandTotal = Total + OGDROTotal - OGDResignTotal;
					ogdProjectedPercentageTotal = (float)(ogdProjectedTotalCitizen + ogdProjectedTotalPR) / ogdProjectedGrandTotal;
					// ogdProjectedPercentageTotal is the total projected value for the future selected date
					ogdProjectedPercentageTotal = ogdProjectedPercentageTotal*100;
					header33Cell = header33.createCell(cellnum);
					header33Cell.setCellValue(ogdProjectedPercentageTotal);
					header33Cell.setCellStyle(header2Style);
					header33Cell.setCellStyle(dataStyle);
			}
			}
			XSSFSheet sheet1 = workbook.createSheet("Active Employee As ON"+dateTime);
			XSSFCellStyle AMheaderStyle = workbook.createCellStyle();
			AMheaderStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			Row AMheader = sheet1.createRow(0);
			Cell AMheaderCell = AMheader.createCell(0);
			AMheaderCell.setCellValue("Name");
			sheet1.autoSizeColumn(0);
			AMheaderCell.setCellStyle(headerStyle);
			AMheaderCell = AMheader.createCell(1);
			AMheaderCell.setCellValue("WorkPermit Type");
			AMheaderCell.setCellStyle(headerStyle);
			AMheaderCell = AMheader.createCell(2);
			AMheaderCell.setCellValue("EmpId");
			AMheaderCell.setCellStyle(headerStyle);
			AMheaderCell = AMheader.createCell(3);
			AMheaderCell.setCellValue("Emp Name");
			AMheaderCell.setCellStyle(headerStyle);
			int AMrownum = 1;
			for (Map.Entry<String, HashMap<String, ArrayList<Object>>> entry : tempfin.entrySet()) {
				String AM = entry.getKey();
				Cell amCell;
				HashMap<String, ArrayList<Object>>	valuez= entry.getValue();
				Set<String> visa = valuez.keySet();
				for(String vis : visa){
				ArrayList<Object> al = valuez.get(vis);
				Iterator itr = al.iterator();
				int empRow = 0;
				while (itr.hasNext()) {
					HashMap<String, Object> hm =  (HashMap<String, Object>) itr.next();
					Row amheader = sheet1.createRow(AMrownum++);
					amCell =amheader.createCell(0);
					amCell.setCellValue(AM);
					amCell =amheader.createCell(1);
					amCell.setCellValue(vis);
					amCell =amheader.createCell(2);
					amCell.setCellValue((String)hm.get("Id"));
					amCell =amheader.createCell(3);
					amCell.setCellValue((String)hm.get("name"));
					empRow++;
				}
				}
				}
			
			XSSFSheet sheet2 = workbook.createSheet("Offers Rolledout From "+dateTime+" To "+onGivenDate);
			XSSFCellStyle sheet2headerStyle = workbook.createCellStyle();
			sheet2headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			Row offerheader = sheet2.createRow(0);
			Cell offerheaderCell = offerheader.createCell(0);
			offerheaderCell.setCellValue("Name");
			sheet2.autoSizeColumn(0);
			offerheaderCell.setCellStyle(headerStyle);
			offerheaderCell = offerheader.createCell(1);
			offerheaderCell.setCellValue("WorkPermit Type");
			sheet2.autoSizeColumn(1);
			offerheaderCell.setCellStyle(headerStyle);
			offerheaderCell = offerheader.createCell(2);
			offerheaderCell.setCellValue("EmpId");
			sheet2.autoSizeColumn(2);
			offerheaderCell.setCellStyle(headerStyle);
			offerheaderCell = offerheader.createCell(3);
			offerheaderCell.setCellValue("Emp Name");
			sheet2.autoSizeColumn(3);
			offerheaderCell.setCellStyle(headerStyle);
			offerheaderCell = offerheader.createCell(4);
			offerheaderCell.setCellValue("Expected DOJ");
			sheet2.autoSizeColumn(4);
			offerheaderCell.setCellStyle(headerStyle);
			int offerrownum = 1;
			for (Map.Entry<String, HashMap<String, ArrayList<Object>>> entry : tempfinRO.entrySet()) {
				String AM = entry.getKey();
				HashMap<String, ArrayList<Object>>	valuez= entry.getValue();
				Set<String> visa = valuez.keySet();
				for(String vis : visa){
				ArrayList<Object> al = valuez.get(vis);
				Iterator itr = al.iterator();
				while (itr.hasNext()) {
					HashMap<String, Object> hm =  (HashMap<String, Object>) itr.next();
					offerheader = sheet2.createRow(offerrownum++);
					offerheaderCell =offerheader.createCell(0);
					offerheaderCell.setCellValue(AM);
					offerheaderCell =offerheader.createCell(1);
					offerheaderCell.setCellValue(vis);
					offerheaderCell =offerheader.createCell(2);
					offerheaderCell.setCellValue((Integer)hm.get("Id"));
					offerheaderCell =offerheader.createCell(3);
					offerheaderCell.setCellValue((String)hm.get("name"));
					offerheaderCell =offerheader.createCell(4);
					offerheaderCell.setCellValue((Timestamp)hm.get("expectedDOJ"));
					offerheaderCell.setCellStyle(dateStyle);
				}
				}
				}
			
			XSSFSheet sheet3 = workbook.createSheet("Emp Resignations From "+dateTime+" To "+onGivenDate);
			XSSFCellStyle sheet3headerStyle = workbook.createCellStyle();
			sheet3headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			Row resignheader = sheet3.createRow(0);
			Cell resignheaderCell = resignheader.createCell(0);
			resignheaderCell.setCellValue("Name");
			sheet3.autoSizeColumn(0);
			resignheaderCell.setCellStyle(headerStyle);
			resignheaderCell = resignheader.createCell(1);
			resignheaderCell.setCellValue("WorkPermit Type");
			sheet3.autoSizeColumn(1);
			resignheaderCell.setCellStyle(headerStyle);
			resignheaderCell = resignheader.createCell(2);
			resignheaderCell.setCellValue("EmpId");
			sheet3.autoSizeColumn(2);
			resignheaderCell.setCellStyle(headerStyle);
			resignheaderCell = resignheader.createCell(3);
			resignheaderCell.setCellValue("Emp Name");
			sheet3.autoSizeColumn(3);
			resignheaderCell.setCellStyle(headerStyle);
			resignheaderCell = resignheader.createCell(4);
			resignheaderCell.setCellValue("Last Working Date");
			sheet3.autoSizeColumn(4);
			resignheaderCell.setCellStyle(headerStyle);
			int resignrownum = 1; 
			for (Map.Entry<String, HashMap<String, ArrayList<Object>>> entry : tempfinResign.entrySet()) {
				String AM = entry.getKey();
				HashMap<String, ArrayList<Object>>	valuez= entry.getValue();
				Set<String> visa = valuez.keySet();
				for(String vis : visa){
				ArrayList<Object> al = valuez.get(vis);
				Iterator itr = al.iterator();
				while (itr.hasNext()) {
					HashMap<String, Object> hm =  (HashMap<String, Object>) itr.next();
					resignheader = sheet3.createRow(resignrownum++);
					resignheaderCell =resignheader.createCell(0);
					resignheaderCell.setCellValue(AM);
					resignheaderCell =resignheader.createCell(1);
					resignheaderCell.setCellValue(vis);
					resignheaderCell =resignheader.createCell(2);
					resignheaderCell.setCellValue((String)hm.get("Id"));
					resignheaderCell =resignheader.createCell(3);
					resignheaderCell.setCellValue((String)hm.get("name"));		
					resignheaderCell =resignheader.createCell(4);			
					resignheaderCell.setCellValue((Timestamp)hm.get("lastWorkingdate"));
					resignheaderCell.setCellStyle(dateStyle);	
					}
					}
				}
			if(onGivenDate!=null){
				if(futureDate == false){
			XSSFSheet sheet4 = workbook.createSheet("Active Emloyee as on "+onGivenDate);
			XSSFCellStyle ogdAMheaderStyle = workbook.createCellStyle();
			ogdAMheaderStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
			Row ogdAMheader = sheet4.createRow(0);
			Cell ogdAMheaderCell = ogdAMheader.createCell(0);
			ogdAMheaderCell.setCellValue("Active Employee As On "+onGivenDate);
			sheet4.autoSizeColumn(0);
			ogdAMheaderCell.setCellStyle(headerStyle);
			ogdAMheader = sheet4.createRow(1);
			ogdAMheaderCell = ogdAMheader.createCell(0);
			ogdAMheaderCell.setCellValue("Name");
			sheet4.autoSizeColumn(ogdAMheaderCell.getColumnIndex());
			ogdAMheaderCell.setCellStyle(headerStyle);
			ogdAMheaderCell = ogdAMheader.createCell(1);
			ogdAMheaderCell.setCellValue("WorkPermit Type");
			sheet4.autoSizeColumn(ogdAMheaderCell.getColumnIndex());
			ogdAMheaderCell.setCellStyle(headerStyle);
			ogdAMheaderCell = ogdAMheader.createCell(2);
			ogdAMheaderCell.setCellValue("EmpId");
			sheet4.autoSizeColumn(ogdAMheaderCell.getColumnIndex());
			ogdAMheaderCell.setCellStyle(headerStyle);
			ogdAMheaderCell = ogdAMheader.createCell(3);
			ogdAMheaderCell.setCellValue("Emp Name");
			sheet4.autoSizeColumn(ogdAMheaderCell.getColumnIndex());
			ogdAMheaderCell.setCellStyle(headerStyle);
			int ogdAMrownum = 2;
			HashMap<String, HashMap<String, ArrayList<Object>>> hmOgd = (HashMap<String, HashMap<String, ArrayList<Object>>>) totalmapOnGivenDate.get("tempfinOGD");
			for (Map.Entry<String, HashMap<String, ArrayList<Object>>> entry : hmOgd.entrySet()) {
				String AM = entry.getKey();
				Cell amCell;
				HashMap<String, ArrayList<Object>>	valuez= entry.getValue();
				Set<String> visa = valuez.keySet();
				for(String vis : visa){
				ArrayList<Object> al = valuez.get(vis);
				Iterator itr = al.iterator();
				int empRow = 0;
				while (itr.hasNext()) {
					HashMap<String, Object> hm =  (HashMap<String, Object>) itr.next();
					Row amheader = sheet4.createRow(ogdAMrownum++);
					amCell =amheader.createCell(0);
					amCell.setCellValue(AM);
					amCell =amheader.createCell(1);
					amCell.setCellValue(vis);
					amCell =amheader.createCell(2);
					amCell.setCellValue((String)hm.get("Id"));
					amCell =amheader.createCell(3);
					amCell.setCellValue((String)hm.get("name"));
					empRow++;
				}
				}
				}
				}else{
					XSSFSheet sheet5 = workbook.createSheet("Offer rolled out From "+dateTime+" To "+onGivenDate);
					XSSFCellStyle ogdROAMheaderStyle = workbook.createCellStyle();
					ogdROAMheaderStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
					Row ogdROAMheader = sheet5.createRow(0);
					Cell ogdROAMheaderCell = ogdROAMheader.createCell(0);
					ogdROAMheaderCell.setCellValue("Offer rolled out From "+dateTime+" To "+onGivenDate);
					sheet5.autoSizeColumn(0);
					ogdROAMheaderCell.setCellStyle(headerStyle);
					ogdROAMheader = sheet5.createRow(1);
					ogdROAMheaderCell = ogdROAMheader.createCell(0);
					ogdROAMheaderCell.setCellValue("Name");
					sheet5.autoSizeColumn(ogdROAMheaderCell.getColumnIndex());
					ogdROAMheaderCell.setCellStyle(headerStyle);
					ogdROAMheaderCell = ogdROAMheader.createCell(1);
					ogdROAMheaderCell.setCellValue("WorkPermit Type");
					sheet5.autoSizeColumn(ogdROAMheaderCell.getColumnIndex());
					ogdROAMheaderCell.setCellStyle(headerStyle);
					ogdROAMheaderCell = ogdROAMheader.createCell(2);
					ogdROAMheaderCell.setCellValue("EmpId");
					sheet5.autoSizeColumn(ogdROAMheaderCell.getColumnIndex());
					ogdROAMheaderCell.setCellStyle(headerStyle);
					ogdROAMheaderCell = ogdROAMheader.createCell(3);
					ogdROAMheaderCell.setCellValue("Emp Name");
					sheet5.autoSizeColumn(ogdROAMheaderCell.getColumnIndex());
					ogdROAMheaderCell.setCellStyle(headerStyle);
					ogdROAMheaderCell = ogdROAMheader.createCell(4);
					ogdROAMheaderCell.setCellValue("Expected DOJ");
					sheet5.autoSizeColumn(ogdROAMheaderCell.getColumnIndex());
					ogdROAMheaderCell.setCellStyle(headerStyle);
					int ogdROAMrownum = 2;
					HashMap<String, HashMap<String, ArrayList<Object>>> hmOgdRo = (HashMap<String, HashMap<String, ArrayList<Object>>>) totalmapOnGivenDate.get("tempfinOGDRO");
					for (Map.Entry<String, HashMap<String, ArrayList<Object>>> entry : hmOgdRo.entrySet()) {
						String AM = entry.getKey();
						Cell amCell;
						HashMap<String, ArrayList<Object>>	valuez= entry.getValue();
						Set<String> visa = valuez.keySet();
						for(String vis : visa){
						ArrayList<Object> al = valuez.get(vis);
						Iterator itr = al.iterator();
						int empRow = 0;
						while (itr.hasNext()) {
							HashMap<String, Object> hm =  (HashMap<String, Object>) itr.next();
							Row amheader = sheet5.createRow(ogdROAMrownum++);
							amCell =amheader.createCell(0);
							amCell.setCellValue(AM);
							amCell =amheader.createCell(1);
							amCell.setCellValue(vis);
							amCell =amheader.createCell(2);
							amCell.setCellValue((Integer)hm.get("Id"));
							amCell =amheader.createCell(3);
							amCell.setCellValue((String)hm.get("name"));
							amCell =amheader.createCell(4);
							amCell.setCellValue((Timestamp)hm.get("expectedDOJ"));
							amCell.setCellStyle(dateStyle);	
							empRow++;
						}
						}
						}
					XSSFSheet sheet6 = workbook.createSheet("Total Resignation From "+dateTime+" To "+onGivenDate);
					XSSFCellStyle ogdResAMheaderStyle = workbook.createCellStyle();
					ogdResAMheaderStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
					Row ogdResAMheader = sheet6.createRow(0);
					Cell ogdResAMheaderCell = ogdResAMheader.createCell(0);
					ogdResAMheaderCell.setCellValue("Resignations From "+dateTime+" To "+onGivenDate);
					sheet6.autoSizeColumn(0);
					ogdResAMheaderCell.setCellStyle(headerStyle);
					ogdResAMheader = sheet6.createRow(1);
					ogdResAMheaderCell = ogdResAMheader.createCell(0);
					ogdResAMheaderCell.setCellValue("Name");
					sheet6.autoSizeColumn(ogdResAMheaderCell.getColumnIndex());
					ogdResAMheaderCell.setCellStyle(headerStyle);
					ogdResAMheaderCell = ogdResAMheader.createCell(1);
					ogdROAMheaderCell.setCellValue("WorkPermit Type");
					sheet6.autoSizeColumn(ogdROAMheaderCell.getColumnIndex());
					ogdResAMheaderCell.setCellStyle(headerStyle);
					ogdResAMheaderCell = ogdResAMheader.createCell(2);
					ogdResAMheaderCell.setCellValue("EmpId");
					sheet6.autoSizeColumn(ogdResAMheaderCell.getColumnIndex());
					ogdResAMheaderCell.setCellStyle(headerStyle);
					ogdResAMheaderCell = ogdResAMheader.createCell(3);
					ogdResAMheaderCell.setCellValue("Emp Name");
					sheet6.autoSizeColumn(ogdResAMheaderCell.getColumnIndex());
					ogdResAMheaderCell.setCellStyle(headerStyle);
					ogdResAMheaderCell = ogdResAMheader.createCell(4);
					ogdResAMheaderCell.setCellValue("Expected DOJ");
					sheet6.autoSizeColumn(ogdResAMheaderCell.getColumnIndex());
					ogdResAMheaderCell.setCellStyle(headerStyle);
					int ogdResAMrownum = 2;
					HashMap<String, HashMap<String, ArrayList<Object>>> hmOgdRes = (HashMap<String, HashMap<String, ArrayList<Object>>>) totalmapOnGivenDate.get("tempfinOGDResign");
					for (Map.Entry<String, HashMap<String, ArrayList<Object>>> entry : hmOgdRes.entrySet()) {
						String AM = entry.getKey();
						Cell amCell;
						HashMap<String, ArrayList<Object>>	valuez= entry.getValue();
						Set<String> visa = valuez.keySet();
						for(String vis : visa){
						ArrayList<Object> al = valuez.get(vis);
						Iterator itr = al.iterator();
						int empRow = 0;
						while (itr.hasNext()) {
							HashMap<String, Object> hm =  (HashMap<String, Object>) itr.next();
							ogdResAMheader = sheet6.createRow(ogdResAMrownum++);
							ogdResAMheaderCell =ogdResAMheader.createCell(0);
							ogdResAMheaderCell.setCellValue(AM);
							ogdResAMheaderCell =ogdResAMheader.createCell(1);
							ogdResAMheaderCell.setCellValue(vis);
							ogdResAMheaderCell =ogdResAMheader.createCell(2);
							ogdResAMheaderCell.setCellValue((String)hm.get("Id"));
							ogdResAMheaderCell =ogdResAMheader.createCell(3);
							ogdResAMheaderCell.setCellValue((String)hm.get("name"));
							ogdResAMheaderCell =ogdResAMheader.createCell(4);
							ogdResAMheaderCell.setCellValue((Timestamp)hm.get("lastWorkingdate"));
							ogdResAMheaderCell.setCellStyle(dateStyle);	
							empRow++;
						}
						}
						}
					
				}
				
			}
			String fileloc = Utils.getProperty("Reports")+ File.separator +"localizationreports";
			try {
				File fileDir = new File(fileloc);
				if (!fileDir.exists()) {
					boolean iscreated = fileDir.mkdirs();
					if (!iscreated) {
						throw new Exception("Failed to copy files Directory not available");
					}
				}
				LocalDate datime= java.time.LocalDate.now(); 
				fileloc = fileloc+ File.separator + "localizationreport_"+datime+".xlsx";
 				FileOutputStream out = new FileOutputStream(fileloc);
				workbook.write(out);
				workbook.close();
				System.out.println("localization Report written successfully on disk.");
			} catch (Exception e) {
				e.printStackTrace();
				headers.add("fileExist", "no");
				hd.add("fileExist");
				return new ResponseEntity<byte[]>(headers,HttpStatus.NOT_FOUND);
			}
			byte[] files = null;
			FileInputStream fi = null;
			try {
				fi = new FileInputStream(fileloc);
				files = IOUtils.toByteArray(fi);
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
				headers.add("fileExist", "no");
				hd.add("fileExist");
				return new ResponseEntity<byte[]>(headers,HttpStatus.NOT_FOUND);
			} catch (Exception e) {
				e.printStackTrace();
				headers.add("fileExist", "no");
				hd.add("fileExist");
				return new ResponseEntity<byte[]>(headers,HttpStatus.NOT_FOUND);
			}
			headers.add("TotalEmp", Integer.toString(Total));
			headers.add("TotalCitizen", Integer.toString(TotalCitizens));
			headers.add("TotalPR", Integer.toString(TotalPR));
			headers.add("TotalOthers", Integer.toString(TotalOthers));
			headers.add("TotaloffersRolledout", Integer.toString(roTotal));
			headers.add("TotalOfferCitizen", Integer.toString(roTotalCitizens));
			headers.add("TotalOfferPR", Integer.toString(roTotalPR));
			headers.add("TotalOfferOthers", Integer.toString(roTotalOthers));
			headers.add("TotalEmpResigned", Integer.toString(resignTotal));
			headers.add("TotalResignedCitizen", Integer.toString(resignTotalCitizens));
			headers.add("TotalResignedPR", Integer.toString(resignTotalPR));
			headers.add("TotalResignedOthers", Integer.toString(resignTotalOthers));
			headers.add("currentPercentage", Float.toString(currentPercentageTotal));
			headers.add("projectedPercentage", Float.toString(projectedPercentageTotal));
			headers.add("fileExist", "yes");
			hd.add("TotalEmp");
			hd.add("TotalCitizen");
			hd.add("TotalPR");
			hd.add("TotalOthers");
			hd.add("TotaloffersRolledout");
			hd.add("TotalOfferCitizen");
			hd.add("TotalOfferPR");
			hd.add("TotalOfferOthers");
			hd.add("TotalEmpResigned");
			hd.add("TotalResignedCitizen");
			hd.add("TotalResignedPR");
			hd.add("TotalResignedOthers");
			hd.add("currentPercentage");
			hd.add("projectedPercentage");
			hd.add("fileExist");
			if(onGivenDate != null){
				if(futureDate== false){
				headers.add("OGDTotalEmp", Integer.toString(OGDTotal));
				headers.add("OGDTotalCitizen", Integer.toString(OGDTotalCitizens));
				headers.add("OGDTotalPR", Integer.toString(OGDTotalPR));
				headers.add("OGDTotalOthers", Integer.toString(OGDTotalOthers));
				headers.add("OGDPercentage", Float.toString(currentOGDPercentageTotal));
				hd.add("OGDTotalEmp");
				hd.add("OGDTotalCitizen");
				hd.add("OGDTotalPR");
				hd.add("OGDTotalOthers");
				hd.add("OGDPercentage");
				}else{
					headers.add("OGDRoTotalEmp", Integer.toString(OGDROTotal));
					headers.add("OGDRoTotalCitizen", Integer.toString(OGDROTotalCitizens));
					headers.add("OGDRoTotalPR", Integer.toString(OGDROTotalPR));
					headers.add("OGDRoTotalOthers", Integer.toString(OGDROTotalOthers));
					headers.add("OGDResignTotalEmp", Integer.toString(OGDResignTotal));
					headers.add("OGDResignTotalCitizen", Integer.toString(OGDResignTotalCitizens));
					headers.add("OGDResignTotalPR", Integer.toString(OGDResignTotalPR));
					headers.add("OGDResignTotalOthers", Integer.toString(OGDResignTotalOthers));
					headers.add("OGDProjectedPercentage", Float.toString(ogdProjectedPercentageTotal));	
					hd.add("OGDRoTotalEmp");
					hd.add("OGDRoTotalCitizen");
					hd.add("OGDRoTotalPR");
					hd.add("OGDRoTotalOthers");
					hd.add("OGDPercentage");
					hd.add("OGDResignTotalEmp");
					hd.add("OGDResignTotalCitizen");
					hd.add("OGDResignTotalPR");
					hd.add("OGDResignTotalOthers");
					hd.add("OGDProjectedPercentage");
				}
				}
			headers.setAccessControlExposeHeaders(hd);
			responseEntity = new ResponseEntity<byte[]>(files,headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			headers.add("fileExist", "no");
			hd.add(" fileExist");
			return new ResponseEntity<byte[]>(headers,HttpStatus.NOT_FOUND);
		} 
		return responseEntity;
	}
} 
	
	
	
	

