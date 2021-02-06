package com.helius.service;

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
import com.helius.dao.EmployeeDAOImpl;
import com.helius.dao.LocalizationReport;
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

public class ReportDAOImpl implements ReportService {

	private static org.hibernate.internal.SessionFactoryImpl sessionFactory;

	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// public static Properties hapProp;
	@Override
	public String getReports(String filterdata) {
		java.util.List vn = null;
		java.util.List reportlist = null;
		Session session = sessionFactory.openSession();
		JSONObject mapJson = (JSONObject) JSONValue.parse(filterdata);
		mapJson.get("type");
		String account_manager = (String) mapJson.get("report_type");
		String empjson = "";
		java.util.List offerlist = null;
		if ("vendor".equalsIgnoreCase(account_manager)) {
			try {
				String offer_query = "select * from Employee_Offer_Details where offer_status = 'rolled_out'";
				offerlist = session.createSQLQuery(offer_query).addEntity(Employee_Offer_Details.class).list();
				session.close();
			} catch (Exception e) {
				session.close();
				e.printStackTrace();
			}
		}
		if ("vendorsummary".equalsIgnoreCase(account_manager)) {
			try {
				String query = "select count(*) from Employee_Personal_Details where offer_status = 'rolled_out'";
				offerlist = session.createSQLQuery(query).addEntity(Employee_Personal_Details.class).list();
				session.close();
			} catch (Exception e) {
				session.close();
				e.printStackTrace();
			}
		}
		try {
			ObjectMapper om = new ObjectMapper();

			empjson = om.writeValueAsString(offerlist);

			// empdetailsjson = empjson.replaceAll(":null",
			// ":\"-\"").replaceAll(":\"\"", ":\"-\"");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empjson;
	}

	public ArrayList<Object> getOfferObj() throws Throwable {
		Session session = null;
		java.util.List offerList = null;
		java.util.List salaryDetailList = null;
		JSONObject js = new JSONObject();
		ArrayList<Object> offerDetailList = new ArrayList<Object>();
		try {
			session = sessionFactory.openSession();
			String offerQuery = "select * from Employee_Offer_Details";
			offerList = session.createSQLQuery(offerQuery).addEntity(Employee_Offer_Details.class).list();
			String salaryQuery = "select * from Employee_Salary_Details";
			salaryDetailList = session.createSQLQuery(salaryQuery).addEntity(Employee_Salary_Details.class).list();
			session.close();
			Employee_Offer_Details offer = null;
			Employee_Salary_Details offSalary = null;
			HashMap<String, Object> offerDetailsMap = new HashMap<String, Object>();
			HashMap<String, Object> salaryDetailMap = new HashMap<String, Object>();
			for (Object salary : salaryDetailList) {
				offSalary = (Employee_Salary_Details) salary;
				salaryDetailMap.put(Integer.toString(offSalary.getOffer_id()), offSalary);
			}
			for (Object offerdetails : offerList) {
				offer = (Employee_Offer_Details) offerdetails;
				String offer_id = Integer.toString(offer.getOffer_id());
				HashMap<String, Object> map = new HashMap<String, Object>();
				Employee employ = new Employee();
				employ.setEmployeeOfferDetails(offer);
				if (salaryDetailMap.containsKey(offer_id)) {
					Employee_Salary_Details employee_Salary_Details = (Employee_Salary_Details) salaryDetailMap
							.get(offer_id);
					if (employee_Salary_Details != null) {
						employ.setEmployeeSalaryDetails(employee_Salary_Details);
					}
				} else {
					Employee_Salary_Details employee_Salary_Details = new Employee_Salary_Details();
					employ.setEmployeeSalaryDetails(employee_Salary_Details);
				}
				offerDetailList.add(employ);
			}
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			throw new Throwable("Exception in fetching offer details" + e.getCause().getMessage());
		}
		return offerDetailList;
	}

	public ArrayList<Object> getEmployeeObj() throws Throwable {
		Session session = null;
		java.util.List personalList = null;
		java.util.List offerList = null;
		java.util.List termsAndCondList = null;
		java.util.List assignmentList = null;
		java.util.List appraisalList = null;
		java.util.List workpermitist = null;
		java.util.List contactAddList = null;
		java.util.List bankDetailsList = null;
		java.util.List leaveList = null;
		java.util.List salaryDetailList = null;
		java.util.List indianInsuranceList = null;
		java.util.List singaporeInsuranceList = null;
		JSONObject js = new JSONObject();
		ArrayList<Object> employeeDetailList = new ArrayList<Object>();
		try {
			session = sessionFactory.openSession();
			String personalQuery = "select * from Employee_Personal_Details";
			personalList = session.createSQLQuery(personalQuery).addEntity(Employee_Personal_Details.class).list();
			String offerQuery = "select * from Employee_Offer_Details";
			offerList = session.createSQLQuery(offerQuery).addEntity(Employee_Offer_Details.class).list();
			String termsCondQuery = "select * from Employee_Terms_And_Conditions";
			termsAndCondList = session.createSQLQuery(termsCondQuery).addEntity(Employee_Terms_And_Conditions.class)
					.list();
			String assignmentQuery = "select * from Employee_Assignment_Details";
			assignmentList = session.createSQLQuery(assignmentQuery).addEntity(Employee_Assignment_Details.class)
					.list();
			String appraisalQuery = "select * from Employee_Appraisal_Details";
			appraisalList = session.createSQLQuery(appraisalQuery).addEntity(Employee_Appraisal_Details.class).list();
			String workpermitQuery = "select * from Employee_Work_Permit_Details";
			workpermitist = session.createSQLQuery(workpermitQuery).addEntity(Employee_Work_Permit_Details.class)
					.list();
			String contactAddQuery = "select * from Contact_Address_Details";
			contactAddList = session.createSQLQuery(contactAddQuery).addEntity(Contact_Address_Details.class).list();
			String bankDetailsQuery = "select * from Employee_Bank_Details";
			bankDetailsList = session.createSQLQuery(bankDetailsQuery).addEntity(Employee_Bank_Details.class).list();
			String leaveDetailsQuery = "select * from Employee_Leaves_Eligibility";
			leaveList = session.createSQLQuery(leaveDetailsQuery).addEntity(Employee_Leaves_Eligibility.class).list();
			String salaryQuery = "select * from Employee_Salary_Details";
			salaryDetailList = session.createSQLQuery(salaryQuery).addEntity(Employee_Salary_Details.class).list();
			session.close();
			Employee_Personal_Details personalDetails = null;
			Employee_Offer_Details offer = null;
			Employee_Terms_And_Conditions termsAndCond = null;
			Employee_Assignment_Details assignment = null;
			Employee_Appraisal_Details appraisal = null;
			Employee_Work_Permit_Details workpermit = null;
			Contact_Address_Details contactaddress = null;
			//Employee_Leaves_Eligibility leavesEligibility = null;
			Employee_Bank_Details bankDetails = null;
			Employee_Salary_Details empSalary = null;
			Indian_Employees_Insurance_Details indianInsurance = null;
			Singapore_Employee_Insurance_Details singaporeInsurance = null;
			HashMap<String, Object> offerDetailsMap = new HashMap<String, Object>();
			HashMap<String, Object> termsAndCondMap = new HashMap<String, Object>();
			HashMap<String, Object> assignmentMap = new HashMap<String, Object>();
			HashMap<String, Object> appraisalMap = new HashMap<String, Object>();
			HashMap<String, Object> workpermitMap = new HashMap<String, Object>();
			HashMap<String, Object> contactAddMap = new HashMap<String, Object>();
			//HashMap<String, Object> leaveDetailMap = new HashMap<String, Object>();
			HashMap<String, Object> bankDetailMap = new HashMap<String, Object>();
			HashMap<String, Object> salaryDetailMap = new HashMap<String, Object>();
			HashMap<String, Object> indianInsuranceMap = new HashMap<String, Object>();
			HashMap<String, Object> singaporeInsuranceMap = new HashMap<String, Object>();
			for (Object offerdetails : offerList) {
				offer = (Employee_Offer_Details) offerdetails;
				offerDetailsMap.put(offer.getEmployee_id(), offer);
			}
			for (Object termsandcond : termsAndCondList) {
				termsAndCond = (Employee_Terms_And_Conditions) termsandcond;
				termsAndCondMap.put(termsAndCond.getEmployee_id(), termsAndCond);
			}
			for (Object assignmentdetails : assignmentList) {
				assignment = (Employee_Assignment_Details) assignmentdetails;
				assignmentMap.put(assignment.getEmployee_id(), assignment);
			}
			for (Object appraisaldetails : appraisalList) {
				appraisal = (Employee_Appraisal_Details) appraisaldetails;
				appraisalMap.put(appraisal.getEmployee_id(), appraisal);
			}
			for (Object workpermittails : workpermitist) {
				workpermit = (Employee_Work_Permit_Details) workpermittails;
				workpermitMap.put(workpermit.getEmployee_id(), workpermit);
			}
			List<Contact_Address_Details> employee_Contact_Address_DetailsList = new ArrayList<Contact_Address_Details>();
			for (Object contactadd : contactAddList) {
				contactaddress = (Contact_Address_Details) contactadd;
				if (contactAddMap.containsKey(contactaddress.getEmployee_id())) {
					employee_Contact_Address_DetailsList = (List<Contact_Address_Details>) contactAddMap
							.get(contactaddress.getEmployee_id());
					employee_Contact_Address_DetailsList.add(contactaddress);
				} else {
					employee_Contact_Address_DetailsList = new ArrayList<Contact_Address_Details>();
					employee_Contact_Address_DetailsList.add(contactaddress);
					contactAddMap.put(contactaddress.getEmployee_id(), employee_Contact_Address_DetailsList);
				}
			}
			/*for (Object leavedetails : leaveList) {
				leavesEligibility = (Employee_Leaves_Eligibility) leavedetails;
				leaveDetailMap.put(leavesEligibility.getEmployee_id(), leavesEligibility);
			}*/
			for (Object empbankdetail : bankDetailsList) {
				bankDetails = (Employee_Bank_Details) empbankdetail;
				bankDetailMap.put(bankDetails.getEmployee_id(), bankDetails);
			}
			for (Object salary : salaryDetailList) {
				empSalary = (Employee_Salary_Details) salary;
				salaryDetailMap.put(empSalary.getEmployee_id(), empSalary);
			}
			for (Object emp : personalList) {
				personalDetails = (Employee_Personal_Details) emp;
				String empid = personalDetails.getEmployee_id();
				HashMap<String, Object> map = new HashMap<String, Object>();
				Employee employ = new Employee();
				employ.setEmployeePersonalDetails(personalDetails);
				if (termsAndCondMap.containsKey(empid)) {
					Employee_Terms_And_Conditions employee_Terms_And_Conditions = (Employee_Terms_And_Conditions) termsAndCondMap
							.get(empid);
					if (employee_Terms_And_Conditions != null) {
						employ.setEmployeeTermsAndConditions(employee_Terms_And_Conditions);
					}
				} else {
					Employee_Terms_And_Conditions employee_Terms_And_Conditions = new Employee_Terms_And_Conditions();
					employ.setEmployeeTermsAndConditions(employee_Terms_And_Conditions);
				}
				if (workpermitMap.containsKey(empid)) {
					Employee_Work_Permit_Details employee_Work_Permit_Details = (Employee_Work_Permit_Details) workpermitMap
							.get(empid);
					if (employee_Work_Permit_Details != null) {
						employ.setEmployeeWorkPermitDetails(employee_Work_Permit_Details);
					}
				} else {
					Employee_Work_Permit_Details employee_Work_Permit_Details = new Employee_Work_Permit_Details();
					employ.setEmployeeWorkPermitDetails(employee_Work_Permit_Details);
				}
				if (offerDetailsMap.containsKey(empid)) {
					Employee_Offer_Details offer_details = (Employee_Offer_Details) offerDetailsMap.get(empid);
					if (offer_details != null) {
						employ.setEmployeeOfferDetails(offer_details);
					}
				} else {
					Employee_Offer_Details offer_details = new Employee_Offer_Details();
					employ.setEmployeeOfferDetails(offer_details);
				}
				if (assignmentMap.containsKey(empid)) {
					Employee_Assignment_Details employee_Assignment_Details = (Employee_Assignment_Details) assignmentMap
							.get(empid);
					if (employee_Assignment_Details != null) {
						employ.setEmployeeAssignmentDetails(employee_Assignment_Details);
					}
				} else {
					Employee_Assignment_Details employee_Assignment_Details = new Employee_Assignment_Details();
					employ.setEmployeeAssignmentDetails(employee_Assignment_Details);
				}
				if (appraisalMap.containsKey(empid)) {
					Employee_Appraisal_Details employee_Appraisal_Details = (Employee_Appraisal_Details) appraisalMap
							.get(empid);
					if (employee_Appraisal_Details != null) {
						employ.setEmployeeAppraisalDetails(employee_Appraisal_Details);
					}
				} else {
					Employee_Appraisal_Details employee_Appraisal_Details = new Employee_Appraisal_Details();
					employ.setEmployeeAppraisalDetails(employee_Appraisal_Details);
				}
				if (contactAddMap.containsKey(empid)) {
					List<Contact_Address_Details> contact_Address_Details = new ArrayList<Contact_Address_Details>();
					contact_Address_Details = (List<Contact_Address_Details>) contactAddMap.get(empid);
					if (contact_Address_Details != null) {
						employ.setEmployeeContactAddressDetails(contact_Address_Details);
					}
				} else {
					List<Contact_Address_Details> contact_Address_Details = new ArrayList<Contact_Address_Details>();
					employ.setEmployeeContactAddressDetails(contact_Address_Details);
				}
				if (bankDetailMap.containsKey(empid)) {
					Employee_Bank_Details employee_Bank_Details = (Employee_Bank_Details) bankDetailMap.get(empid);
					if (employee_Bank_Details != null) {
						employ.setEmployeeBankDetails(employee_Bank_Details);
					}
				} else {
					Employee_Bank_Details employee_Bank_Details = new Employee_Bank_Details();
					employ.setEmployeeBankDetails(employee_Bank_Details);
				}
				/*if (leaveDetailMap.containsKey(empid)) {
					Employee_Leaves_Eligibility employee_Leaves_Eligibility = (Employee_Leaves_Eligibility) leaveDetailMap
							.get(empid);
					if (employee_Leaves_Eligibility != null) {
						employ.setEmployeeLeavesEligibility(employee_Leaves_Eligibility);
					}
				} else {
					Employee_Leaves_Eligibility employee_Leaves_Eligibility = new Employee_Leaves_Eligibility();
					employ.setEmployeeLeavesEligibility(employee_Leaves_Eligibility);
				}*/
				if (salaryDetailMap.containsKey(empid)) {
					Employee_Salary_Details employee_Salary_Details = (Employee_Salary_Details) salaryDetailMap
							.get(empid);
					if (employee_Salary_Details != null) {
						employ.setEmployeeSalaryDetails(employee_Salary_Details);
					}
				} else {
					Employee_Salary_Details employee_Salary_Details = new Employee_Salary_Details();
					employ.setEmployeeSalaryDetails(employee_Salary_Details);
				}
				employeeDetailList.add(employ);
			}
			offerDetailsMap.clear();
			termsAndCondMap.clear();
			assignmentMap.clear();
			appraisalMap.clear();
			workpermitMap.clear();
			salaryDetailMap.clear();
			contactAddMap.clear();
		//	leaveDetailMap.clear();
			bankDetailMap.clear();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			throw new Throwable("Exception while fetching employee details" + e.getCause().getMessage());
		}
		return employeeDetailList;
	}

	@Override
	public ResponseEntity<byte[]> getLocalizationReport(JSONObject Json) throws Throwable {
		ResponseEntity<byte[]> responseEntity = null;
		try {
			ArrayList<Object> employeeDetailList = getEmployeeObj();
			ArrayList<Object> offerDetailList = getOfferObj();
			LocalizationReport lp = new LocalizationReport();
			responseEntity = lp.localizationReport(Json, employeeDetailList, offerDetailList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseEntity;
	}
}
