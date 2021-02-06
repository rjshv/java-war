package com.helius.dao;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Employee_Leave_Data;
import com.helius.entities.Employee_Personal_Details;
import com.helius.entities.Leave_Eligibility_Details;
import com.helius.entities.Leave_Record_Details;
import com.helius.entities.Leave_Usage_Details;
import com.helius.service.EmailService;
import com.helius.utils.ClientDetail;
import com.helius.utils.ClientLeavePolicy;
import com.helius.utils.FilecopyStatus;
import com.helius.utils.Utils;

public class LeaveServiceImpl implements LeaveService{
	
	@Autowired
	ApplicationContext context;
	private org.hibernate.internal.SessionFactoryImpl sessionFactory;
	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Autowired
	EmployeeDAOImpl employeeDAO;
	@Autowired
	private EmailService emailService;	
	
	private List<String> copied_with_success = new ArrayList<String>();

	public float roundLeaveBalnc(float leave) {
		//DecimalFormat formatter = new DecimalFormat(".##");
		//String x = formatter.format(leave);
		String q = String.valueOf(leave);
		String x = q.substring(q.indexOf("."));
		x=x.substring(1,2);
		int j = (int) leave;
		int y = Integer.parseInt(x);
		float z = 0;
		if(y==0){
			z = z +(float)j;
		}
		if (y > 0 && y <= 5) {
			z = z + (float) (j + 0.5);
		}
		if (y > 5) {
			z = z + (float) (j + 1);
		}
		return z;
	}
				
	public List<Leave_Eligibility_Details> populateClientLeaveEligibility(String adoj,String clientName,String location) throws Throwable {
		Session session = null;
		List<Leave_Eligibility_Details> leaveEligibleList = new ArrayList<Leave_Eligibility_Details>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date selectedDate = sdf.parse(adoj);
			java.sql.Date date = new java.sql.Date(selectedDate.getTime());
			LocalDate startday = date.toLocalDate();
			session = sessionFactory.openSession();	
			ClientDAOImpl clientDAOImpl = (ClientDAOImpl) context.getBean("clientDAO");
			ClientDetail clientDetail = clientDAOImpl.get(clientName);
			List<ClientLeavePolicy> Client_Leave_Policy = clientDetail.getClientLeavePolicy();
			if("India".equalsIgnoreCase(location)){
			CalcIndiaLeaveEligibilityAndUsage calcIndElig = new CalcIndiaLeaveEligibilityAndUsage(); 
			if(Client_Leave_Policy != null && !Client_Leave_Policy.isEmpty()){
			leaveEligibleList = calcIndElig.getLeaveEligibility(startday,Client_Leave_Policy);
			}
			}
			if("Singapore".equalsIgnoreCase(location)){
				CalcSingaporeLeaveEligibilityAndUsage calcSingElig = new CalcSingaporeLeaveEligibilityAndUsage(); 
				if(Client_Leave_Policy != null && !Client_Leave_Policy.isEmpty()){
					leaveEligibleList = calcSingElig.getLeaveEligibility(startday,Client_Leave_Policy);
				}
			}
			DecimalFormat formatter = new DecimalFormat(".##");	
				if(leaveEligibleList != null){
				for(Leave_Eligibility_Details leaveEligibility : leaveEligibleList){
					float eligval = leaveEligibility.getNumber_of_days();
					String eligible = formatter.format(eligval);
					leaveEligibility.setNumber_of_days(Float.valueOf(eligible));
					}
				}
			} catch (Throwable e) {
			e.printStackTrace();
			throw new Throwable("Failed To Populate Employee Leaves" + e.getMessage());
		}finally{
			session.close();	
		}
		return leaveEligibleList;	
	}
	
	public List<Leave_Usage_Details> newEmployeeLeaveUsage(Timestamp adoj,String location,List<Leave_Eligibility_Details> eligibility) throws Throwable{
		List<Leave_Usage_Details> usageDetails = null;
		try {
			if("India".equalsIgnoreCase(location)){
				CalcIndiaLeaveEligibilityAndUsage calcInd = new CalcIndiaLeaveEligibilityAndUsage(); 
				usageDetails = calcInd.newEmployeeLeaveUsage(adoj,eligibility);
			}
			if("Singapore".equalsIgnoreCase(location)){
				CalcSingaporeLeaveEligibilityAndUsage calcSing = new CalcSingaporeLeaveEligibilityAndUsage();
				usageDetails = calcSing.newEmployeeLeaveUsage(adoj, eligibility);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Throwable("Failed to create leave usage details" + e.getMessage());
		}
		return usageDetails;	
	}
	
	public HashMap<String,Object> readCarryForwardLeave(OPCPackage pkg) throws Throwable{
		Sheet sheet = null;
		HashMap<String,Object> CFLeave = new HashMap<String,Object>();
		try{
			Workbook workbook = new XSSFWorkbook(pkg);
			sheet = workbook.getSheetAt(0);
			int i = 1;
			for (Row row : sheet) {
				if (row.getRowNum() >= i) {
					if (row.getCell(1) != null && !row.getCell(1).toString().isEmpty()) {
						String empid = row.getCell(1).toString().trim();
						CFLeave.put(empid, row.getCell(3));
					}
				}
				
			}
			workbook.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new Throwable("Unable to Calculate Carry Forward Leave ");
		}
		return CFLeave;
	}
	
	public void runServiceToUpdateEmpLeaveEligibilityForIndia(MultipartHttpServletRequest carryForwardLeaveFile) throws Throwable{
		Session session = null;
		java.util.List employeeList = null;
		Transaction transaction = null;
		HashMap<String, Object> leaveMapData = null;
		Set<String> unprocessedEmployeeLeave = new HashSet<String>();
		String emailsubject = " India Leave Eligibility service Status";
		String emailto = Utils.getProperty("emailUserName");
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();			
			try {
				OPCPackage	pkg = null;		
				Map<String, List<MultipartFile>> allfiles = carryForwardLeaveFile.getMultiFileMap();
				if(allfiles.size() > 0){
				for (List<MultipartFile> files : allfiles.values()) {
					for (MultipartFile file : files) {
						InputStream xlsxContentStream = file.getInputStream();
						pkg = OPCPackage.open(xlsxContentStream);
					}
				}
			if (pkg != null) {		
					 leaveMapData =	readCarryForwardLeave(pkg);
				}
			 else {
				throw new Throwable("Unable to Calculate India Leave Eligibility ");
			}
			}
			}catch (InvalidFormatException e) {
				e.printStackTrace();
				throw new Throwable("Unable to Calculate India Leave Eligibility Fail to read " + e.getMessage());
			}
			String Query = "SELECT a.employee_id,a.actual_date_of_joining,b.client,c.client_id,d.work_country FROM Employee_Personal_Details a LEFT JOIN Employee_Assignment_Details b ON a.employee_id = b.employee_id LEFT JOIN client_details c ON b.client = c.client_name LEFT JOIN Employee_Work_Permit_Details d ON a.employee_id=d.employee_id WHERE a.employee_status='Active' AND d.work_country = 'India'";
			employeeList = session.createSQLQuery(Query).setResultTransformer(Transformers.aliasToBean(FetchEmployeeLeavesfromClientPolicy.class)).list();
			Set<String> noCFEmpMapped = new HashSet<String>(leaveMapData.keySet());
			for(Object obj : employeeList){
					FetchEmployeeLeavesfromClientPolicy employeeDetail = (FetchEmployeeLeavesfromClientPolicy) obj;	
					if(leaveMapData.containsKey(employeeDetail.getEmployee_id())){
						noCFEmpMapped.remove(employeeDetail.getEmployee_id());
					}
			}	

			ClientDAOImpl clientDAOImpl = (ClientDAOImpl) context.getBean("clientDAO");
			Map<Integer, List<ClientLeavePolicy>> client_Leave_Policy_Map = clientDAOImpl.getAllLeavePolicies();
			if(client_Leave_Policy_Map == null || client_Leave_Policy_Map.isEmpty()){
				throw new Throwable("Failed to Process India Leave Eligibility Service ");
			}
			int i = 0;
			CalcIndiaLeaveEligibilityAndUsage calcIndLeaveElig = new CalcIndiaLeaveEligibilityAndUsage(); 
			CalcSingaporeLeaveEligibilityAndUsage calcSingLeaveElig = new CalcSingaporeLeaveEligibilityAndUsage(); 
			List<Leave_Eligibility_Details> leaveEligibleListResult = null;
			for(Object empObj : employeeList){
				FetchEmployeeLeavesfromClientPolicy employeeDetail = null;
				try{
				 employeeDetail = (FetchEmployeeLeavesfromClientPolicy) empObj;
					if(employeeDetail.getActual_date_of_joining() != null ){
						java.sql.Date date = new java.sql.Date(employeeDetail.getActual_date_of_joining().getTime());
						LocalDate startday = date.toLocalDate();
						if(client_Leave_Policy_Map.get(employeeDetail.getClient_id()) != null && !client_Leave_Policy_Map.get(employeeDetail.getClient_id()).isEmpty()){
						leaveEligibleListResult = calcIndLeaveElig.getLeaveEligibility(startday, client_Leave_Policy_Map.get(employeeDetail.getClient_id()));
						if(leaveEligibleListResult != null){
						for(Leave_Eligibility_Details resultEligible : leaveEligibleListResult){
							resultEligible.setEmployee_id(employeeDetail.getEmployee_id());
							resultEligible.setCreated_by("HAP");
							session.save(resultEligible);
							i++;
						}	
						float cfLeave = 0;
						if(leaveMapData.containsKey(employeeDetail.getEmployee_id())){
						cfLeave = Float.parseFloat(leaveMapData.get(employeeDetail.getEmployee_id()).toString());
						Leave_Eligibility_Details createCFLv = new Leave_Eligibility_Details();
						createCFLv.setClient_id(employeeDetail.getClient_id());
						createCFLv.setEmployee_id(employeeDetail.getEmployee_id());
						createCFLv.setCreated_by("HAP");
						createCFLv.setYear(LocalDate.now().getYear());
						createCFLv.setType_of_leave(LeaveTypeConstants.india_CF);
						createCFLv.setNumber_of_days(cfLeave);
						session.save(createCFLv);
						}
					}else{
						unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
					}
						}	else{
							unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
						}
					}else{
						unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
					}
					if(i%50 == 0) {
					  session.flush();
					  session.clear(); 
				 }
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
				}	
			}
			//try{
				transaction.commit();
				/*}catch(Exception e){
					if(transaction != null){
						transaction.rollback();
					}
					e.printStackTrace();
				}*/
			try{
			if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+"\n\n" + "Total number of India Employees to be processed is : " +employeeList.size() + "\n\n"
					+ "\n\n" + "Issue in calculating India leave eligibility for employees : " + "\n\n");
			for(String empid : unprocessedEmployeeLeave){
			message.append(empid+ "," + "\n");
			}
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			}
			if(noCFEmpMapped != null && !noCFEmpMapped.isEmpty()){
				StringBuffer message = new StringBuffer();
				message.append("Hi,"
						+ "\n\n" + "Below Employee's from carryforward file list are not found in Hap Employee list : " + "\n\n");
				for(String empid : noCFEmpMapped){
				message.append(empid+ "," + "\n");
				}
				message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
				emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
				}
			}catch(Exception e){
				e.printStackTrace();
				if (noCFEmpMapped != null && !noCFEmpMapped.isEmpty()) {
					for (String empid : noCFEmpMapped) {
						System.out.println("carryforward employee from cf file list not found in helius list========= " + empid);
					}
				}
				if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
					for(String empid : unprocessedEmployeeLeave){
						System.out.println("Issue in calculating India leave eligibility for employees====== " + empid);
						}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+ "\n\n" + "Issue in Running India leave eligibility service: " + "\n\n");
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			throw new Throwable("Failed to Process India Leave Eligibility" + e.getMessage());
		}finally{
			session.close();
		}
	}
	
	public void runServiceToUpdateEmpLeaveEligibilityForSingapore(MultipartHttpServletRequest carryForwardLeaveFile) throws Throwable{
		Session session = null;
		java.util.List employeeList = null;
		Transaction transaction = null;
		HashMap<String, Object> leaveMapData = null;
		Set<String> unprocessedEmployeeLeave = new HashSet<String>();
		String emailsubject = "Singapore Leave Eligibility service Status";
		String emailto = Utils.getProperty("emailUserName");
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();			
			try {
				OPCPackage	pkg = null;		
				Map<String, List<MultipartFile>> allfiles = carryForwardLeaveFile.getMultiFileMap();
				if(allfiles.size() > 0){
				for (List<MultipartFile> files : allfiles.values()) {
					for (MultipartFile file : files) {
						InputStream xlsxContentStream = file.getInputStream();
						pkg = OPCPackage.open(xlsxContentStream);
					}
				}
			if (pkg != null) {		
					 leaveMapData =	readCarryForwardLeave(pkg);
				}
			 else {
				throw new Throwable("Unable to Calculate Singapore Leave Eligibility ");
			}
			}
			}catch (InvalidFormatException e) {
				e.printStackTrace();
				throw new Throwable("Unable to Calculate Singapore Leave Eligibility Fail to read " + e.getMessage());
			}
			String Query = "SELECT a.employee_id,a.actual_date_of_joining,b.client,c.client_id,d.work_country FROM Employee_Personal_Details a LEFT JOIN Employee_Assignment_Details b ON a.employee_id = b.employee_id LEFT JOIN client_details c ON b.client = c.client_name LEFT JOIN Employee_Work_Permit_Details d ON a.employee_id=d.employee_id WHERE a.employee_status='Active' AND d.work_country = 'Singapore'";
			employeeList = session.createSQLQuery(Query).setResultTransformer(Transformers.aliasToBean(FetchEmployeeLeavesfromClientPolicy.class)).list();
			Set<String> noCFEmpMapped = new HashSet<String>(leaveMapData.keySet());
			for(Object obj : employeeList){
					FetchEmployeeLeavesfromClientPolicy employeeDetail = (FetchEmployeeLeavesfromClientPolicy) obj;	
					if(leaveMapData.containsKey(employeeDetail.getEmployee_id())){
						noCFEmpMapped.remove(employeeDetail.getEmployee_id());
					}
			}	

			ClientDAOImpl clientDAOImpl = (ClientDAOImpl) context.getBean("clientDAO");
			Map<Integer, List<ClientLeavePolicy>> client_Leave_Policy_Map = clientDAOImpl.getAllLeavePolicies();
			if(client_Leave_Policy_Map == null || client_Leave_Policy_Map.isEmpty()){
				throw new Throwable("Failed to Process Singapore Leave Eligibility Service ");
			}
			int i = 0;
			CalcSingaporeLeaveEligibilityAndUsage calcSingLeaveElig = new CalcSingaporeLeaveEligibilityAndUsage(); 
			List<Leave_Eligibility_Details> leaveEligibleListResult = null;
			for(Object empObj : employeeList){
				FetchEmployeeLeavesfromClientPolicy employeeDetail = null;
				try{
				 employeeDetail = (FetchEmployeeLeavesfromClientPolicy) empObj;
					if(employeeDetail.getActual_date_of_joining() != null){
						java.sql.Date date = new java.sql.Date(employeeDetail.getActual_date_of_joining().getTime());
						LocalDate startday = date.toLocalDate();
						if(client_Leave_Policy_Map.get(employeeDetail.getClient_id()) != null && !client_Leave_Policy_Map.get(employeeDetail.getClient_id()).isEmpty()){
						leaveEligibleListResult = calcSingLeaveElig.getLeaveEligibility(startday, client_Leave_Policy_Map.get(employeeDetail.getClient_id()));
						if(leaveEligibleListResult != null){
						for(Leave_Eligibility_Details resultEligible : leaveEligibleListResult){
							resultEligible.setEmployee_id(employeeDetail.getEmployee_id());
							resultEligible.setCreated_by("HAP");
								session.save(resultEligible);
								i++;
							}	
						float cfLeave = 0;
						if(leaveMapData.containsKey(employeeDetail.getEmployee_id())){
						cfLeave = Float.parseFloat(leaveMapData.get(employeeDetail.getEmployee_id()).toString());
						Leave_Eligibility_Details createCFLv = new Leave_Eligibility_Details();
						createCFLv.setClient_id(employeeDetail.getClient_id());
						createCFLv.setEmployee_id(employeeDetail.getEmployee_id());
						createCFLv.setCreated_by("HAP");
						createCFLv.setYear(LocalDate.now().getYear());
						createCFLv.setType_of_leave(LeaveTypeConstants.singapore_CF);
						createCFLv.setNumber_of_days(cfLeave);
						session.save(createCFLv);
						}							 
					}else{
						unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
					}
						}else{
							unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
						}
						}else{
						unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
					}
					if(i%50 == 0) {
					  session.flush();
					  session.clear(); 
				 }
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
				}	
			}
			//try{
				transaction.commit();
				/*}catch(Exception e){
					if(transaction != null){
						transaction.rollback();
					}
					e.printStackTrace();
				}*/
			try{
			if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+"\n\n" + "Total number of Singapore Employees to be processed is : " +employeeList.size() + "\n\n"
					+ "\n\n" + "Issue in calculating Singapore leave eligibility for employees : " + "\n\n");
			for(String empid : unprocessedEmployeeLeave){
			message.append(empid+ "," + "\n");
			}
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			}
			if(noCFEmpMapped != null && !noCFEmpMapped.isEmpty()){
				StringBuffer message = new StringBuffer();
				message.append("Hi,"
						+ "\n\n" + "Below Employee's from carryforward file list are not found in Hap Employee list : " + "\n\n");
				for(String empid : noCFEmpMapped){
				message.append(empid+ "," + "\n");
				}
				message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
				emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
				}
			}catch(Exception e){
				e.printStackTrace();
				if (noCFEmpMapped != null && !noCFEmpMapped.isEmpty()) {
					for (String empid : noCFEmpMapped) {
						System.out.println("carryforward employee from cf file list not found in helius list========= " + empid);
					}
				}
				if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
					for(String empid : unprocessedEmployeeLeave){
						System.out.println("Issue in calculating Singapore leave eligibility for employees====== " + empid);
						}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+ "\n\n" + "Issue in Running Singapore leave eligibility service: " + "\n\n");
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			throw new Throwable("Failed to Process Singapore Leave Eligibility" + e.getMessage());
		}finally{
			session.close();
		}
	}
	
	
	@Scheduled(cron = "0 0 5 1 * ?")
	public void runServiceToUpdateEmpLeavUsageForIndia() throws Throwable{
		Session session = null;
		java.util.List employeeList = null;
		Transaction transaction = null;
		List<Leave_Eligibility_Details> leaveEligibleList = null;
		List<Leave_Usage_Details> leaveUsageList = null;
		Set<String> unprocessedEmployeeLeave = new HashSet<String>();
		String emailsubject = "India Leave Usage service Status";
		String emailto = Utils.getProperty("emailUserName");
		try {
			/*if(LocalDate.now().getMonthValue() == 1){
				throw new Throwable("Cannot run service for the Month January Must run the service Manually for January Month "); 
			}*/
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			String Query = "SELECT a.employee_id,a.actual_date_of_joining,b.client,c.client_id,d.work_country FROM Employee_Personal_Details a LEFT JOIN Employee_Assignment_Details b ON a.employee_id = b.employee_id LEFT JOIN client_details c ON b.client = c.client_name LEFT JOIN Employee_Work_Permit_Details d ON a.employee_id=d.employee_id WHERE a.employee_status='Active' AND d.work_country = 'India'";
			employeeList = session.createSQLQuery(Query).setResultTransformer(Transformers.aliasToBean(FetchEmployeeLeavesfromClientPolicy.class)).list();
			
			int year = LocalDate.now().getYear();
			String QueryLeavesfromEmployee = "SELECT * FROM Leave_Eligibility_Details where year = :year"; 
			leaveEligibleList = session.createSQLQuery(QueryLeavesfromEmployee).addEntity(Leave_Eligibility_Details.class)
					.setParameter("year", year).list();
			List<Leave_Eligibility_Details> employee_Leaves_Eligibility = null;
			HashMap<String, List<Leave_Eligibility_Details>> employee_Leaves_EligibilityMap = new HashMap<String, List<Leave_Eligibility_Details>>();
			for (Leave_Eligibility_Details employeeLeavesDetails : leaveEligibleList) {
				if(employee_Leaves_EligibilityMap.containsKey(employeeLeavesDetails.getEmployee_id())){
					employee_Leaves_Eligibility = employee_Leaves_EligibilityMap.get(employeeLeavesDetails.getEmployee_id());
					employee_Leaves_Eligibility.add(employeeLeavesDetails);
				}else{
					employee_Leaves_Eligibility = new ArrayList<Leave_Eligibility_Details>();
					employee_Leaves_Eligibility.add(employeeLeavesDetails);
					employee_Leaves_EligibilityMap.put(employeeLeavesDetails.getEmployee_id(), employee_Leaves_Eligibility);
				}
			}
			
			String leaveUsageQuery = "SELECT * FROM Leave_Usage_Details where YEAR(usageMonth)= :year"; 
			leaveUsageList = session.createSQLQuery(leaveUsageQuery).addEntity(Leave_Usage_Details.class)
					.setParameter("year", year).list();
			List<Leave_Usage_Details> employee_Leaves_Usage = null;
			HashMap<String, List<Leave_Usage_Details>> employee_Leaves_UsageMap = new HashMap<String, List<Leave_Usage_Details>>();
			for (Leave_Usage_Details employeeLeavesDetails : leaveUsageList) {
				if(employee_Leaves_UsageMap.containsKey(employeeLeavesDetails.getEmployee_id())){
					employee_Leaves_Usage = employee_Leaves_UsageMap.get(employeeLeavesDetails.getEmployee_id());
					employee_Leaves_Usage.add(employeeLeavesDetails);
				}else{
					employee_Leaves_Usage = new ArrayList<Leave_Usage_Details>();
					employee_Leaves_Usage.add(employeeLeavesDetails);
					employee_Leaves_UsageMap.put(employeeLeavesDetails.getEmployee_id(), employee_Leaves_Usage);
				}
			}
			
			int i = 0;
			try {
				CalcIndiaLeaveEligibilityAndUsage calcIndLeaveUsage = new CalcIndiaLeaveEligibilityAndUsage();
				for (Object empObj : employeeList) {
					FetchEmployeeLeavesfromClientPolicy employeeDetail = null;
					try{
					employeeDetail = (FetchEmployeeLeavesfromClientPolicy) empObj;
						if(employeeDetail.getActual_date_of_joining() != null &&
								employee_Leaves_EligibilityMap.get(employeeDetail.getEmployee_id()) != null
								&& !employee_Leaves_EligibilityMap.get(employeeDetail.getEmployee_id()).isEmpty()){						
							List<Leave_Usage_Details> resultUsage = calcIndLeaveUsage.calcLeaveUsageService(employeeDetail.getActual_date_of_joining(), employee_Leaves_EligibilityMap.get(employeeDetail.getEmployee_id()),employee_Leaves_UsageMap.get(employeeDetail.getEmployee_id()));
							if(resultUsage != null){
							for(Leave_Usage_Details usage : resultUsage){
								usage.setClient_id(employeeDetail.getClient_id());
								usage.setEmployee_id(employeeDetail.getEmployee_id());
								session.save(usage);
								i++;
							}
							}else{
								unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
							}
						}else{
							unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
						}
					if (i%50 == 0) {
						  session.flush();
						  session.clear(); 
					 }
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
					}
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
			transaction.commit();
			}catch(HibernateException e){
				if(transaction != null){
					transaction.rollback();
				}
				e.printStackTrace();
			}
			try{
			if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+"\n\n" + "Total number of India Employees to be processed is : " +employeeList.size() + "\n\n"
					+ "\n\n" + "Issue in calculating india leave usage for employees : " + "\n\n");
			for(String empid : unprocessedEmployeeLeave){
			message.append(empid+ " ," + "\n");
			}
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			}
			}catch(Exception e){
				e.printStackTrace();
				if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
					for(String empid : unprocessedEmployeeLeave){
						System.out.println("Issue in calculating india leave usage for employees =======" + empid);
						}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+ "\n\n" + "Issue in Running india leave usage  service: " + "\n\n");
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			throw new Throwable("Failed to Process Leave usage " + e.getMessage());
		}finally{
			session.close();
		}
	}
	
	@Scheduled(cron = "0 0 5 1 * ?")
	public void runServiceToUpdateEmpLeavUsageForSingapore() throws Throwable{
		Session session = null;
		java.util.List employeeList = null;
		Transaction transaction = null;
		List<Leave_Eligibility_Details> leaveEligibleList = null;
		List<Leave_Usage_Details> leaveUsageList = null;
		Set<String> unprocessedEmployeeLeave = new HashSet<String>();
		String emailsubject = "Singapore leave usage service status";
		String emailto = Utils.getProperty("emailUserName");
		try {
			/*if(LocalDate.now().getMonthValue() == 1){
				throw new Throwable("Cannot run leave usage service for the Month January Must run the service Manually for January Month "); 
			}*/
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			String Query = "SELECT a.employee_id,a.actual_date_of_joining,b.client,c.client_id,d.work_country FROM Employee_Personal_Details a LEFT JOIN Employee_Assignment_Details b ON a.employee_id = b.employee_id LEFT JOIN client_details c ON b.client = c.client_name LEFT JOIN Employee_Work_Permit_Details d ON a.employee_id=d.employee_id WHERE a.employee_status='Active' AND d.work_country = 'Singapore' AND b.client != 'Helius'";
			employeeList = session.createSQLQuery(Query).setResultTransformer(Transformers.aliasToBean(FetchEmployeeLeavesfromClientPolicy.class)).list();
			
			int year = LocalDate.now().getYear();
			String QueryLeavesfromEmployee = "SELECT * FROM Leave_Eligibility_Details where year = :year"; 
			leaveEligibleList = session.createSQLQuery(QueryLeavesfromEmployee).addEntity(Leave_Eligibility_Details.class)
					.setParameter("year", year).list();
			List<Leave_Eligibility_Details> employee_Leaves_Eligibility = null;
			HashMap<String, List<Leave_Eligibility_Details>> employee_Leaves_EligibilityMap = new HashMap<String, List<Leave_Eligibility_Details>>();
			for (Leave_Eligibility_Details employeeLeavesDetails : leaveEligibleList) {
				if(employee_Leaves_EligibilityMap.containsKey(employeeLeavesDetails.getEmployee_id())){
					employee_Leaves_Eligibility = employee_Leaves_EligibilityMap.get(employeeLeavesDetails.getEmployee_id());
					employee_Leaves_Eligibility.add(employeeLeavesDetails);
				}else{
					employee_Leaves_Eligibility = new ArrayList<Leave_Eligibility_Details>();
					employee_Leaves_Eligibility.add(employeeLeavesDetails);
					employee_Leaves_EligibilityMap.put(employeeLeavesDetails.getEmployee_id(), employee_Leaves_Eligibility);
				}
			}
			
			LocalDate prevDate = LocalDate.now().minusMonths(1).with(firstDayOfMonth());
			java.sql.Date previousMonth = java.sql.Date.valueOf(prevDate);
			String leaveUsageQuery = "SELECT * FROM Leave_Usage_Details where DATE(usageMonth) = :previousMonth"; 
			leaveUsageList = session.createSQLQuery(leaveUsageQuery).addEntity(Leave_Usage_Details.class).setParameter("previousMonth",previousMonth).list();
			List<Leave_Usage_Details> employee_Leaves_Usage = null;
			HashMap<String, List<Leave_Usage_Details>> employee_Leaves_UsageMap = new HashMap<String, List<Leave_Usage_Details>>();
			for (Leave_Usage_Details employeeLeavesDetails : leaveUsageList) {
				if(employee_Leaves_UsageMap.containsKey(employeeLeavesDetails.getEmployee_id())){
					employee_Leaves_Usage = employee_Leaves_UsageMap.get(employeeLeavesDetails.getEmployee_id());
					employee_Leaves_Usage.add(employeeLeavesDetails);
				}else{
					employee_Leaves_Usage = new ArrayList<Leave_Usage_Details>();
					employee_Leaves_Usage.add(employeeLeavesDetails);
					employee_Leaves_UsageMap.put(employeeLeavesDetails.getEmployee_id(), employee_Leaves_Usage);
				}
			}
			
			int i = 0;
			try {
				CalcSingaporeLeaveEligibilityAndUsage calcSingLeaveUsage = new CalcSingaporeLeaveEligibilityAndUsage();
				for (Object empObj : employeeList) {
					FetchEmployeeLeavesfromClientPolicy employeeDetail = null;
					try{
					employeeDetail = (FetchEmployeeLeavesfromClientPolicy) empObj;
						if(employeeDetail.getActual_date_of_joining() != null &&
								employee_Leaves_EligibilityMap.get(employeeDetail.getEmployee_id()) != null
								&& !employee_Leaves_EligibilityMap.get(employeeDetail.getEmployee_id()).isEmpty()){
							List<Leave_Usage_Details> resultUsage = calcSingLeaveUsage.calcLeaveUsageService(employeeDetail.getActual_date_of_joining(), employee_Leaves_EligibilityMap.get(employeeDetail.getEmployee_id()),employee_Leaves_UsageMap.get(employeeDetail.getEmployee_id()));
							if(resultUsage != null){
							for(Leave_Usage_Details usage : resultUsage){
								usage.setClient_id(employeeDetail.getClient_id());
								usage.setEmployee_id(employeeDetail.getEmployee_id());
								session.save(usage);
								i++;
							}
							}else{
								unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
							}
						}else{
							unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
						}					
					if (i%50 == 0) {
						  session.flush();
						  session.clear(); 
					 }
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						unprocessedEmployeeLeave.add(employeeDetail.getEmployee_id());
					}
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
			transaction.commit();
			}catch(HibernateException e){
				if(transaction != null){
					transaction.rollback();
				}
				e.printStackTrace();
			}
			try{
			if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+"\n\n" + "Total number of Singapore Employees to be processed is : " +employeeList.size() + "\n\n"
					+ "\n\n" + "Issue in calculating leave usage for employees : " + "\n\n");
			for(String empid : unprocessedEmployeeLeave){
			message.append(empid+ " ," + "\n");
			}
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Total number of Singapore Employees to be processed is : " +employeeList.size());
				if(unprocessedEmployeeLeave != null && !unprocessedEmployeeLeave.isEmpty()){
					for(String empid : unprocessedEmployeeLeave){
						System.out.println("Issue in calculating leave usage for employees =======" + empid);
						}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+ "\n\n" + "Issue in Running leave usage  service: " + "\n\n");
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			throw new Throwable("Failed to Process Leave usage " + e.getMessage());
		}finally{
			session.close();
		}
	}
	
	/**
	 * @author vinay
	 * 
	 * this service will run everyday will reset the employee sick leave eligibility and usage for employees who are completing 
	 * their 3,4,5,6 months on current day and leaves allotted as given as per months eligible
	 * 
	 * NOTE : if any employee is given special leave that will not be tracked it will be replaced according to singapore MOM Rules.
	 * **/
	@Scheduled(cron = "0 0 6 * * ?")
	public void runDailyServiceToUpdateSickLeavEligibilityAndUsageForSingapore() throws Throwable{
		Session session = null;
		Transaction transaction = null;
		String emailsubject = "Daily Sick Leave Service Status";
		String emailto = Utils.getProperty("emailUserName");
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			LocalDate currdate = LocalDate.now();
			LocalDate before3months = currdate.minusMonths(3);
			LocalDate before4months = currdate.minusMonths(4);
			LocalDate before5months = currdate.minusMonths(5);
			LocalDate before6months = currdate.minusMonths(6);
			ArrayList<java.sql.Date> duration = new ArrayList<java.sql.Date>();
			duration.add(java.sql.Date.valueOf(before3months));
			duration.add(java.sql.Date.valueOf(before4months));
			duration.add(java.sql.Date.valueOf(before5months));
			duration.add(java.sql.Date.valueOf(before6months));
		String Query = "SELECT * FROM Employee_Personal_Details a  LEFT JOIN Employee_Work_Permit_Details b ON a.employee_id=b.employee_id WHERE a.employee_status='Active' AND b.work_country = 'Singapore' AND DATE(a.actual_date_of_joining) IN (:actual_date_of_joining)";
		List<Employee_Personal_Details> employeeList = session.createSQLQuery(Query).addEntity(Employee_Personal_Details.class).setParameterList("actual_date_of_joining", duration).list();
		Set<String> dailyServProcessedLeavEligibility = new HashSet<String>();
		Set<String> dailyServProcessedLeavUsage = new HashSet<String>();
		Set<String> issueInProcessingLeavElig = new HashSet<String>();
		Set<String> issueInProcessingLeavUsage = new HashSet<String>();
		for(Employee_Personal_Details emp : employeeList){
			try{
			LocalDateTime date = emp.getActual_date_of_joining().toLocalDateTime();
			float sickLeaveVal = 0;
			if(before3months.equals(date.toLocalDate())){
				sickLeaveVal = LeaveTypeConstants.After3MOnths;
			}
			if(before4months.equals(date.toLocalDate())){
				sickLeaveVal = LeaveTypeConstants.After4MOnths;
			}if(before5months.equals(date.toLocalDate())){
				sickLeaveVal = LeaveTypeConstants.After5MOnths;
			}if(before6months.equals(date.toLocalDate())){
				sickLeaveVal = LeaveTypeConstants.After6MOnths;
			}
			Employee_Leave_Data leaveData = getEmployeeLeaveData(emp.getEmployee_id());
			List<Leave_Eligibility_Details> eligibilityLV = leaveData.getLeavesEligibility();
			if(eligibilityLV != null){
			for(Leave_Eligibility_Details elig : eligibilityLV){
				try{	
				if(LeaveTypeConstants.singapore_Sick.equalsIgnoreCase(elig.getType_of_leave())){
					elig.setNumber_of_days(sickLeaveVal);
					session.update(elig);
					dailyServProcessedLeavEligibility.add(elig.getEmployee_id());
				}
				}catch(Exception e){
					e.printStackTrace();
					issueInProcessingLeavElig.add(elig.getEmployee_id());
				}
			}
			}else{
				issueInProcessingLeavElig.add(emp.getEmployee_id());
			}
			List<Leave_Usage_Details> usageDetails = leaveData.getLeaveUsageDetails();
			if(usageDetails != null){
			for(Leave_Usage_Details usage : usageDetails){
				try{
				if(LeaveTypeConstants.singapore_Sick.equalsIgnoreCase(usage.getType_of_leave())){
					LocalDateTime usagemonth = usage.getUsageMonth().toLocalDateTime();
					if(currdate.getMonthValue() == usagemonth.getMonthValue()){
					usage.setLeaves_accrued(sickLeaveVal);
					session.update(usage);
					dailyServProcessedLeavUsage.add(usage.getEmployee_id());
					}
				}
				}catch(Exception e){
					e.printStackTrace();
					issueInProcessingLeavUsage.add(usage.getEmployee_id());
				}
			}}else{
				issueInProcessingLeavUsage.add(emp.getEmployee_id());
			}
		}catch(Exception e){
			e.printStackTrace();
			issueInProcessingLeavUsage.add(emp.getEmployee_id());
		}
		}
		transaction.commit();
		try{
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+ "\n\n" + "Daily Sick Leave Service Updated Employee Sick leave Eligibility : " + "\n\n");
			if(dailyServProcessedLeavEligibility != null && !dailyServProcessedLeavEligibility.isEmpty()){
			for(String empid : dailyServProcessedLeavEligibility){
			message.append(empid+ " ," + "\n");
			}
			message.append("\n\n" + "Daily Sick Leave Service Updated Employees Sick leave usage : " + "\n\n");
			if(dailyServProcessedLeavUsage != null && !dailyServProcessedLeavUsage.isEmpty()){
				for(String empid : dailyServProcessedLeavUsage){
				message.append(empid+ " ," + "\n");
				}
			}
			message.append("\n\n" + "issue  in updating leave Eligibility for employees : " + "\n\n");
			if(issueInProcessingLeavElig != null && !issueInProcessingLeavElig.isEmpty()){
				for(String empid : issueInProcessingLeavElig){
				message.append(empid+ " ," + "\n");
				}
			}
			message.append("\n\n" + "issue in updating leave usage for employees : " + "\n\n");
			if(issueInProcessingLeavUsage != null && !issueInProcessingLeavUsage.isEmpty()){
				for(String empid : issueInProcessingLeavUsage){
				message.append(empid+ " ," + "\n");
				}
			}
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			}
			}catch(Exception e){
				e.printStackTrace();
				if(dailyServProcessedLeavEligibility != null && !dailyServProcessedLeavEligibility.isEmpty()){
					for(String empid : dailyServProcessedLeavEligibility){
						System.out.println("daily Sick Leave Serv updates eligibility for employee =======" + empid);
						}
				}
				if(dailyServProcessedLeavUsage != null && !dailyServProcessedLeavUsage.isEmpty()){
					for(String empid : dailyServProcessedLeavUsage){
						System.out.println("daily Sick Leave Serv updates usage for employee =======" + empid);
						}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			StringBuffer message = new StringBuffer();
			message.append("Hi,"
					+ "\n\n" + "Issue in Running Daily Sick leave eligibility and usage  service: " + "\n\n");
			message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendEmail(emailto,null,null,emailsubject, message.toString());
			throw new Throwable("Failed to Process Sick Leave Service " + e.getMessage());
		}finally{
			session.close();
		}
	}
	
	
	@Override
	public Employee_Leave_Data getEmployeeLeaveData(String employee_id) throws Throwable {
		Employee_Leave_Data employeeLeaveData = new Employee_Leave_Data();
		Session session = null;
		LocalDate date = LocalDate.now();
		int year = date.getYear();
		try {
			session = sessionFactory.openSession();	
			// Leave Eligibility details
			String leaveEligibility = "select * from  Leave_Eligibility_Details where employee_id = :employee_id AND year = :year";
			List<Leave_Eligibility_Details> eligibilityList = session.createSQLQuery(leaveEligibility)
					.addEntity(Leave_Eligibility_Details.class).setParameter("employee_id", employee_id).setParameter("year",year).list();
			List<Leave_Eligibility_Details> leave_Eligibility_DetailsList = new ArrayList<Leave_Eligibility_Details>();
			if (!eligibilityList.isEmpty()) {
				for (Leave_Eligibility_Details leave_Eligibility : eligibilityList) {
					leave_Eligibility_DetailsList.add(leave_Eligibility);
				}
			}
			if (leave_Eligibility_DetailsList != null && !leave_Eligibility_DetailsList.isEmpty()) {
				employeeLeaveData.setLeavesEligibility(leave_Eligibility_DetailsList);
			}
			
			String leaveUsageDetails = "select * from  Leave_Usage_Details where employee_id = :employee_id AND YEAR(usageMonth)= :year ORDER BY usageMonth DESC";
			java.util.List leaveUsageDetailsList = session.createSQLQuery(leaveUsageDetails)
					.addEntity(Leave_Usage_Details.class).setParameter("employee_id", employee_id).setParameter("year",year).list();
			ArrayList<Leave_Usage_Details> leave_Usage_DetailsList = new ArrayList<Leave_Usage_Details>();
			Leave_Usage_Details leave_Usage_Details = null;
			if(leaveUsageDetailsList != null){
				for(Object obj : leaveUsageDetailsList){
				leave_Usage_Details = (Leave_Usage_Details)obj;
				leave_Usage_DetailsList.add(leave_Usage_Details);
				}
				if(leave_Usage_DetailsList != null && !leave_Usage_DetailsList.isEmpty()){
				employeeLeaveData.setLeaveUsageDetails(leave_Usage_DetailsList);
				}
			}
			
			String recordQuery = "select * from  Leave_Record_Details where employee_id = :employee_id AND YEAR(leaveMonth) = :year ORDER BY leaveMonth DESC";
			java.util.List recordQueryList = session.createSQLQuery(recordQuery)
					.addEntity(Leave_Record_Details.class).setParameter("employee_id", employee_id).setParameter("year", year).list();
			ArrayList<Leave_Record_Details> leave_Record_DetailsList = new ArrayList<Leave_Record_Details>();
			Leave_Record_Details leaveRecordDetails = null;
			if(leaveUsageDetailsList != null){
				for(Object obj : recordQueryList){
					leaveRecordDetails = (Leave_Record_Details)obj;
					leave_Record_DetailsList.add(leaveRecordDetails);
				}
				if(leave_Record_DetailsList != null && !leave_Record_DetailsList.isEmpty()){
				employeeLeaveData.setLeaveRecordDetails(leave_Record_DetailsList);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Throwable("Failed to fetch Employee Leave Details");
		}finally{
			session.close();
		}
		return employeeLeaveData;		
	}
	
	
	public void saveOrUpdateEmployeeLeaveData(String jsondata, MultipartHttpServletRequest request) throws Throwable{
		Session session = null;
		Transaction transaction = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Map<String, String> templateFilenames = new HashMap<String, String>();
			Map<String, MultipartFile> files = null;
			files = request.getFileMap();
			ObjectMapper om = new ObjectMapper();
			Employee_Leave_Data employee_Leave_Data = null;
			employee_Leave_Data = om.readValue(jsondata, Employee_Leave_Data.class);
			if (employee_Leave_Data != null) {
				if(employee_Leave_Data.getLeavesEligibility() != null){
					Iterator<Leave_Eligibility_Details> itr = employee_Leave_Data.getLeavesEligibility().iterator();
					while(itr.hasNext()){
						Leave_Eligibility_Details leaveEligibility = itr.next();
						if(leaveEligibility.getLeave_eligibility_details_id() == 0){
							session.save(leaveEligibility);
						}else{
							session.evict(leaveEligibility);
							session.merge(leaveEligibility);
						}
					}
				}			
				if(employee_Leave_Data.getLeaveUsageDetails() != null){
					Iterator<Leave_Usage_Details> itr = employee_Leave_Data.getLeaveUsageDetails().iterator();
					while(itr.hasNext()){
						Leave_Usage_Details leave_Usage_Details = itr.next();
						java.util.Date selectedMonth = sdfMonth.parse(leave_Usage_Details.getUsageMonth().toString());
						leave_Usage_Details.setUsageMonth(new Timestamp(selectedMonth.getTime()));
						if(leave_Usage_Details.getLeave_usage_details_id() == 0){
							session.save(leave_Usage_Details);
						}else{
						session.evict(leave_Usage_Details);
						session.merge(leave_Usage_Details);
						}
					}
				}
				
				if(employee_Leave_Data.getLeaveRecordDetails() != null){
					Iterator<Leave_Record_Details> itr = employee_Leave_Data.getLeaveRecordDetails().iterator();
				while(itr.hasNext()){
					Leave_Record_Details leaverecord = itr.next();
					java.util.Date selectedMonth = sdfMonth.parse(leaverecord.getLeaveMonth().toString());
					leaverecord.setLeaveMonth(new Timestamp(selectedMonth.getTime()));
					if(leaverecord.getLeave_record_details_id() == 0){
						if (files.values().size() > 0) {
							String url = leaverecord.getEmployee_id()+"_"+LocalDateTime.now().toString().replaceAll(":","_")+"_"+leaverecord.getLeaveRecordPath();
							templateFilenames.put(leaverecord.getLeaveRecordPath(),url);
							leaverecord.setLeaveRecordPath(url);
						}
						session.save(leaverecord);
					}else{
						if (files.values().size() > 0) {
							String url = leaverecord.getEmployee_id()+"_"+LocalDateTime.now().toString().replaceAll(":","_")+"_"+leaverecord.getLeaveRecordPath();
							templateFilenames.put(leaverecord.getLeaveRecordPath(),url);
							leaverecord.setLeaveRecordPath(url);
						}
					session.evict(leaverecord);
					session.merge(leaverecord);
					}
				}
				if (files.values().size() > 0) {
					FilecopyStatus status = Utils.copyFiles(request, templateFilenames, "leaverecords");
					copied_with_success = status.getCopied_with_success();
				}
				}
				
				if(employee_Leave_Data.getDeleteLeaveRecord() != null){
					Leave_Record_Details leaverecord = employee_Leave_Data.getDeleteLeaveRecord();
					session.delete(leaverecord);
				}
				if(employee_Leave_Data.getDeleteLeaveEligibility() != null){
					Leave_Eligibility_Details leaverEligibility = employee_Leave_Data.getDeleteLeaveEligibility();
					session.delete(leaverEligibility);
				}
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to update leave details" + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public ResponseEntity<byte[]> getLeaveRecordFile(String url) {
		byte[] files = null;
		FileInputStream fi = null;
		try {
			String fileUrl = Utils.getProperty("fileLocation")+ File.separator +"leaverecords" + File.separator +url;
			File file = new File(fileUrl);
			if (file.exists()) {
				fi = new FileInputStream(fileUrl);
				files = IOUtils.toByteArray(fi);
				fi.close();
			} else {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
		return responseEntity;
	}
	
	public boolean checkProbationPeriod(LocalDate ADOJ){
		// approach 1
		LocalDate probationDate = ADOJ.plusMonths(3);
		LocalDate currentDate = LocalDate.now();
		if(currentDate.isBefore(probationDate)){
			return true;
		}
		//  approch 2
		/*LocalDate currentDate = LocalDate.now();
		float uu =	ChronoUnit.MONTHS.between(ADOJ,currentDate);
		if(uu < 3){
			return true;
		}*/
		return false;	
	}
	
	public boolean checkProbationInSameMonthOrAfter(LocalDate ADOJ){
		// approach 1
		LocalDate probationDate = ADOJ.plusMonths(3);
		LocalDate currentDate = LocalDate.now().with(firstDayOfMonth());
		if(probationDate.isBefore(currentDate)){
			return false;
		}
		return true;	
	}
	
	@Override
	public List<IndiaLeaveBulkReport> indiaLeaveBulkReport(String Year) throws Throwable {
		Session session = null;
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp leaveMonths = null;
		java.util.Date selectedDate = sdf.parse(leaveYear);
		leaveMonths = new Timestamp(selectedDate.getTime());*/
		List<IndiaLeaveBulkReport> results = null;
		int leaveYear = Integer.parseInt(Year);
		try {
			String indiaAnnual = LeaveTypeConstants.india_Annual;
			String indiaSick = LeaveTypeConstants.india_Sick;
			String CFLeave = LeaveTypeConstants.india_CF;
			session = sessionFactory.openSession();
			String sowAss_query = "SELECT a.employee_id,f.employee_name,f.actual_date_of_joining,a.client_id,a.usageMonth,a.type_of_leave,a.leaves_accrued,a.leaves_used,a.leaves_accrued - a.leaves_used as monthlyLeavebalance,"
					+ "b.payroll_entity,c.account_manager,c.client,d.work_country,e.cfLeave,g.specialLeave FROM Leave_Usage_Details a "
					+ "LEFT JOIN Employee_Salary_Details b ON a.employee_id = b.employee_id "
					+ "LEFT JOIN Employee_Assignment_Details c ON a.employee_id=c.employee_id "
					+ "LEFT JOIN Employee_Work_Permit_Details d ON a.employee_id = d.employee_id "
					+ "LEFT JOIN (SELECT employee_id,number_of_days AS cfLeave FROM Leave_Eligibility_Details WHERE type_of_leave = :type_of_leave AND year = :year) e ON a.employee_id= e.employee_id "
					+ "LEFT JOIN (SELECT employee_id,SUM(leaves_used) AS specialLeave FROM Leave_Record_Details WHERE type_of_leave != :indiaAnnual AND type_of_leave != :indiaSick AND YEAR(leaveMonth) = :year GROUP BY employee_id) g ON a.employee_id= g.employee_id "
					+ "LEFT JOIN Employee_Personal_Details f ON a.employee_id = f.employee_id "
					+ "WHERE d.work_country = 'India' AND  YEAR(a.usageMonth) = :usageMonth ORDER BY a.employee_id";
			results = session.createSQLQuery(sowAss_query)
					.setResultTransformer(Transformers.aliasToBean(IndiaLeaveBulkReport.class))
					.setParameter("usageMonth", leaveYear).setParameter("year", leaveYear).setParameter("type_of_leave",CFLeave)
					.setParameter("indiaAnnual",indiaAnnual).setParameter("indiaSick",indiaSick).list();
			/*Iterator<IndiaLeaveBulkReport> itr = results.iterator();
			while(itr.hasNext()){
				IndiaLeaveBulkReport leaverecord = itr.next();
				leaverecord.setMonthlyLeavebalance(leaverecord.getLeaves_accrued() - leaverecord.getLeaves_used());
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch  SOW Details ");
		}finally{
			session.close();
		}
		return results;
	}
	
	@Override
	public List<EmployeeLeaveDashboard> getEmployeeLeaveDashBoard(String leaveMonth) throws Throwable {
		Session session = null;
		List<EmployeeLeaveDashboard> results = null;
		try {
			session = sessionFactory.openSession();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Timestamp leaveMonths = null;
			java.util.Date selectedDate = sdf.parse(leaveMonth);
			leaveMonths = new Timestamp(selectedDate.getTime());
			LocalDateTime year = leaveMonths.toLocalDateTime();
			String period = year.getYear()+String.format("%02d", year.getMonthValue());
			String sowAss_query = "SELECT a.employee_id,f.employee_name,a.client_id,a.type_of_leave,a.startdate,"
					+ "a.enddate,a.ampm,a.remarks,a.leaves_used,b.payroll_entity,c.account_manager,"
					+ "c.client,d.work_country,e.timesheet_error FROM Leave_Record_Details a "
					+ "LEFT JOIN Employee_Salary_Details b ON a.employee_id = b.employee_id "
					+ "LEFT JOIN Employee_Assignment_Details c ON a.employee_id=c.employee_id "
					+ "LEFT JOIN Employee_Work_Permit_Details d ON a.employee_id = d.employee_id "
					+ "LEFT JOIN Employee_Personal_Details f ON a.employee_id = f.employee_id "
					+ "LEFT JOIN (SELECT employee_id,timesheet_error FROM Employee_Timesheet_Status WHERE timesheet_month = :timesheet_month) e ON a.employee_id= e.employee_id "
					+ "WHERE a.leaveMonth = :leaveMonth ORDER BY a.employee_id";
			results = session.createSQLQuery(sowAss_query)
					.setResultTransformer(Transformers.aliasToBean(EmployeeLeaveDashboard.class))
					.setParameter("leaveMonth", leaveMonths).setParameter("timesheet_month", leaveMonths).list();	
			HashMap<String,String> singaporeleaveCodes = LeaveTypeConstants.getSingaporeLeaveCode();
			Iterator<EmployeeLeaveDashboard> itr = results.iterator();
			while(itr.hasNext()){
				EmployeeLeaveDashboard leaverecord = itr.next();
				if (leaverecord.getType_of_leave() != null && leaverecord.getWork_country() != null) {
					if ("Singapore".equalsIgnoreCase(leaverecord.getWork_country())) {
						String leaveCode = singaporeleaveCodes.get(leaverecord.getType_of_leave());
						if (leaveCode != null && !leaveCode.isEmpty()) {
							leaverecord.setType_of_leave(leaveCode);
						}
					}
				}
				if(leaverecord.getTimesheet_error() == null || "".equalsIgnoreCase(leaverecord.getTimesheet_error())){
					leaverecord.setTimesheet_error("Unprocessed");
				}
				leaverecord.setHours(0);
				leaverecord.setApproval("Y");
				leaverecord.setPeriod(period);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch Leave Dadhboard Details ");
		}finally {
			session.close();
		}
		return results;
	}
	
	@Override
	public void settakenleaves(String month){
		Session session = null;
		Transaction transaction = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Timestamp leaveMonth = null;
			java.util.Date selectedDate = sdf.parse(month);
			leaveMonth = new Timestamp(selectedDate.getTime());
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String leaveUsageDetails = "select * from  Leave_Usage_Details where DATE(usageMonth)=:leaveMonth AND type_of_leave='Annual Leave' ";
			java.util.List<Leave_Usage_Details> leaveUsageDetailsList = session.createSQLQuery(leaveUsageDetails)
					.addEntity(Leave_Usage_Details.class).setParameter("leaveMonth", leaveMonth).list();
			HashMap<String,Leave_Usage_Details> leave_Usage_AnnualMap = new HashMap<String,Leave_Usage_Details>();
			if(leaveUsageDetailsList != null){
				for(Leave_Usage_Details obj : leaveUsageDetailsList){
					leave_Usage_AnnualMap.put(obj.getEmployee_id(), obj);
				}
			}
			
			String leavesickDetails = "select * from  Leave_Usage_Details where DATE(usageMonth)=:leaveMonth AND type_of_leave='Sick Leave' ";
			java.util.List<Leave_Usage_Details> leaveUsagesickList = session.createSQLQuery(leavesickDetails)
					.addEntity(Leave_Usage_Details.class).setParameter("leaveMonth",leaveMonth).list();
			HashMap<String,Leave_Usage_Details> leave_Usage_SickMap = new HashMap<String,Leave_Usage_Details>();
			if(leaveUsagesickList != null){
				for(Leave_Usage_Details obj : leaveUsagesickList){
					leave_Usage_SickMap.put(obj.getEmployee_id(), obj);
				}
			}
			
			
			Set<String> successAnnual = new HashSet<String>();
			Set<String> successSick = new HashSet<String>();
			Set<String> failedAnnual = new HashSet<String>();
			Set<String> failedSick = new HashSet<String>();

		String recordQuery = "SELECT a.employee_id,a.type_of_leave,SUM(a.leaves_used) AS total FROM  Leave_Record_Details a  LEFT JOIN Employee_Work_Permit_Details b ON a.employee_id = b.employee_id WHERE b.work_country='Singapore' AND DATE(leaveMonth) = :leaveMonth AND type_of_leave IN ('Annual Leave','Sick Leave') GROUP BY employee_id,type_of_leave";
		java.util.List<Object[]> recordQueryList = session.createSQLQuery(recordQuery).setParameter("leaveMonth", leaveMonth).list();
		if(recordQueryList != null){
			for(Object[] obj : recordQueryList){
				try{
				Leave_Usage_Details leaveusage = null;
				if("Annual Leave".equalsIgnoreCase(obj[1].toString())){
					if(leave_Usage_AnnualMap.containsKey(obj[0].toString())){
						leaveusage = leave_Usage_AnnualMap.get(obj[0].toString());
						leaveusage.setLeaves_used(Float.parseFloat(obj[2].toString()));
						session.update(leaveusage);
						successAnnual.add(obj[0].toString());
					}else{
						failedAnnual.add(obj[0].toString());
					}
				}
				if("Sick Leave".equalsIgnoreCase(obj[1].toString())){
					if(leave_Usage_SickMap.containsKey(obj[0].toString())){
						leaveusage = leave_Usage_SickMap.get(obj[0].toString());
						leaveusage.setLeaves_used(Float.parseFloat(obj[2].toString()));
						session.update(leaveusage);
						successSick.add(obj[0].toString());
					}else{
						failedSick.add(obj[0].toString());
					}
				}
			}catch(Exception e){
				System.out.println("unable to setup used lv for "+obj[0]);
			}
			}
			transaction.commit();
				System.out.println("====success Annual====="+successAnnual);
				System.out.println("====success Sick====="+successSick);
				System.out.println("====failed Annual====="+failedAnnual);
				System.out.println("====failed Sick====="+failedSick);			
		}
		System.out.println("-------success setup----");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void verifyLeaveRecordHistoryAndLeaveUsage(String date) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			String leaveUsageDetails = "select * from  Leave_Usage_Details where DATE(usageMonth)='"+date+"' AND type_of_leave='Annual Leave' ";
			java.util.List<Leave_Usage_Details> leaveUsageDetailsList = session.createSQLQuery(leaveUsageDetails)
					.addEntity(Leave_Usage_Details.class).list();
			HashMap<String,Leave_Usage_Details> leave_Usage_AnnualMap = new HashMap<String,Leave_Usage_Details>();
			if(leaveUsageDetailsList != null){
				for(Leave_Usage_Details obj : leaveUsageDetailsList){
					leave_Usage_AnnualMap.put(obj.getEmployee_id(), obj);
				}
			}
			
			String leavesickDetails = "select * from  Leave_Usage_Details where DATE(usageMonth)='"+date+"' AND type_of_leave='Sick Leave' ";
			java.util.List<Leave_Usage_Details> leaveUsagesickList = session.createSQLQuery(leavesickDetails)
					.addEntity(Leave_Usage_Details.class).list();
			HashMap<String,Leave_Usage_Details> leave_Usage_SickMap = new HashMap<String,Leave_Usage_Details>();
			if(leaveUsagesickList != null){
				for(Leave_Usage_Details obj : leaveUsagesickList){
					leave_Usage_SickMap.put(obj.getEmployee_id(), obj);
				}
			}
			
			
			Set<String> successAnnual = new HashSet<String>();
			Set<String> successSick = new HashSet<String>();
			Set<String> failedAnnual = new HashSet<String>();
			Set<String> failedSick = new HashSet<String>();
			Set<String> correctSick = new HashSet<String>();
			Set<String> incorrectSick = new HashSet<String>();
			Set<String> correctAnnual = new HashSet<String>();
			Set<String> incorrectAnnual = new HashSet<String>();

		String recordQuery = "SELECT a.employee_id,a.type_of_leave,SUM(a.leaves_used) AS total FROM  Leave_Record_Details a  LEFT JOIN Employee_Work_Permit_Details b ON a.employee_id = b.employee_id WHERE b.work_country='Singapore' AND DATE(leaveMonth) = '"+date+"' AND type_of_leave IN ('Annual Leave','Sick Leave') GROUP BY employee_id,type_of_leave";
		java.util.List<Object[]> recordQueryList = session.createSQLQuery(recordQuery).list();
		if(recordQueryList != null){
			for(Object[] obj : recordQueryList){
				try{
				Leave_Usage_Details leaveusage = null;
				if("Annual Leave".equalsIgnoreCase(obj[1].toString())){
					if(leave_Usage_AnnualMap.containsKey(obj[0].toString())){
						leaveusage = leave_Usage_AnnualMap.get(obj[0].toString());
						if(leaveusage.getLeaves_used()==Float.parseFloat(obj[2].toString())){
							correctAnnual.add(obj[0].toString());
							successAnnual.add(obj[0].toString());
						}else{
							incorrectAnnual.add(obj[0].toString());
						}
					}else{
						failedAnnual.add(obj[0].toString());
					}
				}
				if("Sick Leave".equalsIgnoreCase(obj[1].toString())){
					if(leave_Usage_SickMap.containsKey(obj[0].toString())){
						leaveusage = leave_Usage_SickMap.get(obj[0].toString());
						if(leaveusage.getLeaves_used()==Float.parseFloat(obj[2].toString())){
							float f1 = leaveusage.getLeaves_used();
							float f2 = Float.parseFloat(obj[2].toString());
							correctSick.add(obj[0].toString());
							successSick.add(obj[0].toString());
						}else{
							incorrectSick.add(obj[0].toString());
						}
						successSick.add(obj[0].toString());
					}else{
						failedSick.add(obj[0].toString());
					}
				}
			}catch(Exception e){
				System.out.println("unable to setup used lv for "+obj[0]);
			}
			}
			System.out.println("====correct annual====="+correctAnnual);		
			System.out.println("====correct sick====="+correctSick);
			System.out.println("====incorrect annual====="+incorrectAnnual);		
			System.out.println("====incorrect sick====="+incorrectSick);		
			System.out.println("==== correct annual size====="+correctAnnual.size());	
			System.out.println("==== correct sick size====="+correctSick.size());	
			System.out.println("==== incorrect annual size====="+incorrectAnnual.size());	
			System.out.println("==== incorrect sick size====="+incorrectSick.size());	

				System.out.println("====success Annual====="+successAnnual);
				System.out.println("====success Sick====="+successSick);
				System.out.println("====failed Annual====="+failedAnnual);
				System.out.println("====failed Sick====="+failedSick);		
				System.out.println("====sucess annual size====="+successAnnual.size());	
				System.out.println("====success Sick size====="+successSick.size());			
				System.out.println("====failed anuual size====="+failedAnnual.size());			
				System.out.println("====failed Sick size====="+failedSick.size());			


		}
		System.out.println("-------success setup----");
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
}
