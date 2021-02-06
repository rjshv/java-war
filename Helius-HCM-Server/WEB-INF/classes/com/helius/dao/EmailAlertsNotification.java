package com.helius.dao;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;

import com.helius.entities.Employee_Checklist_Items;
import com.helius.entities.Employee_Offer_Details;
import com.helius.service.EmailService;
import com.helius.utils.Utils;

public class EmailAlertsNotification {
	
	@Autowired
	private EmailService emailService;
	@Autowired
	private ChecklistDAOImpl checklistDAO;
	
	private static org.hibernate.internal.SessionFactoryImpl sessionFactory;

	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Scheduled(cron = "0 0 7 1 * ?")
	public void sendWorkpermitExpiryAlerts() throws Throwable {
		Session session = null;
		List wpQuerylst = null;
		try {
			session = sessionFactory.openSession();
			String wpQuery = "SELECT a.employee_id,a.employee_name,d.helius_email_id,b.account_manager,DATE(c.work_permit_name_expiry_date) as work_permit_name_expiry_date FROM Employee_Personal_Details a,Employee_Assignment_Details b,Employee_Work_Permit_Details c,pickllistNameAndEmployeeNameAssoc d WHERE a.employee_status='Active' AND a.employee_id=b.employee_id AND a.employee_id=c.employee_id AND d.picklist_name=b.account_manager AND MONTH(c.work_permit_name_expiry_date) = MONTH(CURRENT_TIMESTAMP) AND YEAR(c.work_permit_name_expiry_date) =YEAR(CURRENT_TIMESTAMP)";  
			wpQuerylst = session.createSQLQuery(wpQuery).setResultTransformer(Transformers.aliasToBean(WorkpermitExpiryAlert.class)).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}
		HashMap<String,List<WorkpermitExpiryAlert>> accountmgrMap = new HashMap<String,List<WorkpermitExpiryAlert>>();
		List<WorkpermitExpiryAlert> WorkpermitAlert = new ArrayList<WorkpermitExpiryAlert>();
		for (Object data : wpQuerylst) {
			WorkpermitExpiryAlert workpermitExpiryAlert = (WorkpermitExpiryAlert) data;
			if (workpermitExpiryAlert.getAccount_manager() != null
					&& !"".equalsIgnoreCase(workpermitExpiryAlert.getAccount_manager())) {
				String am = workpermitExpiryAlert.getAccount_manager();
				if (accountmgrMap.containsKey(am)) {
					WorkpermitAlert.add(workpermitExpiryAlert);
				} else {
					WorkpermitAlert = new ArrayList<WorkpermitExpiryAlert>();
					WorkpermitAlert.add(workpermitExpiryAlert);
					accountmgrMap.put(am, WorkpermitAlert);
				}
			}
		}
		for (Map.Entry<String,List<WorkpermitExpiryAlert>> AMObject : accountmgrMap.entrySet()) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("wplist", AMObject.getValue());
			model.put("toName", AMObject.getKey().toString());
			Mail mail = new Mail();
			mail.setMailTo(AMObject.getValue().get(0).getHelius_email_id());
			mail.setMailSubject("workpermit expiry list in current month");
			mail.setModel(model);
			try{
			emailService.sendEmailAlert(mail);
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	
	
	@Scheduled(cron = "0 0 7 1 * ?")
	public void sendExpectedJoiningAlerts() throws Throwable {
		Session session = null;
		List<ExpectedJoineeAlert> Querylst = null;
		try {
			session = sessionFactory.openSession();
			LocalDate join = LocalDate.now().plusDays(2);
			String Query = "SELECT a.offer_id,a.employee_name,a.account_manager,a.helius_recruiter,a.client,a.bgv_required,a.bgv_completed,b.payroll_entity,a.work_country from Employee_Offer_Details a,Employee_Salary_Details b  WHERE a.offer_id=b.offer_id AND a.offer_status='rolled_out' AND DAY(a.expected_date_of_joining)=:day AND MONTH(a.expected_date_of_joining)=:month AND YEAR(a.expected_date_of_joining)=:year";  
			Querylst = session.createSQLQuery(Query).setResultTransformer(Transformers.aliasToBean(ExpectedJoineeAlert.class)).setParameter("day", join.getDayOfMonth()).setParameter("month", join.getMonthValue()).setParameter("year", join.getYear()).list();
			for(ExpectedJoineeAlert obj : Querylst){
				SimpleMailMessage Email = new SimpleMailMessage();
				String[] cc = null;
				String recruiterName = obj.getHelius_recruiter();
				String accountManagerName = obj.getAccount_manager();
				String client = obj.getClient();
				String payroll = obj.getPayroll_entity();
				String location = obj.getWork_country();
				String[] picklistNames = {recruiterName,accountManagerName};	
				String bgv_status = "";// checking bgv status
				if("YES".equalsIgnoreCase(obj.getBgv_required())){
					if("YES".equalsIgnoreCase(obj.getBgv_completed())){
						bgv_status = "Complete";
					}else{
						bgv_status = "Incomplete";
					}
				}
				if("NO".equalsIgnoreCase(obj.getBgv_required())){
					bgv_status = "Not Required";
				}
				//checking nboarding status
				List<Employee_Checklist_Items> docs = checklistDAO.getemployeeItems(String.valueOf(obj.getOffer_id()), "Onboarding");
				StringBuffer message = new StringBuffer();
				String onboardingStatus = "complete";
				for(Employee_Checklist_Items item : docs){
					if("NO".equalsIgnoreCase(item.getSubmited())){
						onboardingStatus = "Incomplete";
						message.append(item.getChecklistName()+"\n");
					}
				}

				if("Singapore".equalsIgnoreCase(location)){
					HashMap<String,String> emailids = Utils.getEmailIdFromPickllistNameAndEmployeeNameAssoc(picklistNames, session);
					Email.setTo(Utils.getHapProperty("notifyEmployeeIdGeneratedForSingapore-TO"));
					String getCC = Utils.getHapProperty("notifyEmployeeIdGeneratedToSingaporeCCHr-CC");				
						if(emailids!=null && emailids.get(recruiterName) != null && !emailids.get(recruiterName).isEmpty()){
							getCC = getCC+","+emailids.get(recruiterName);
						}
						if(emailids!=null && emailids.get(accountManagerName) != null && !emailids.get(accountManagerName).isEmpty()){
							getCC = getCC+","+emailids.get(accountManagerName);
						}
						if("DBS".equalsIgnoreCase(client) && "Helius".equalsIgnoreCase(payroll)){
							getCC = getCC + ","+Utils.getHapProperty("notifyEmployeeIdGeneratedForSingaporeCC-DBS-Helius");	
						}
						if("DBS".equalsIgnoreCase(client) && !"Helius".equalsIgnoreCase(payroll)){
							getCC = getCC + ","+Utils.getHapProperty("notifyEmployeeIdGeneratedForSingaporeCC-DBS-nonHelius");	
						}
						if(!"DBS".equalsIgnoreCase(client)){
							getCC = getCC + ","+Utils.getHapProperty("notifyEmployeeIdGeneratedForSingaporeCC-nonDBS");	
						}
					if (getCC != null && !getCC.isEmpty()) {	
 						cc = getCC.split(",");
					}
				}
				if("India".equalsIgnoreCase(location)){
					Email.setTo(Utils.getHapProperty("notifyEmployeeIdGeneratedToIndiaPayrollUser-TO"));
					String getCC = Utils.getHapProperty("notifyEmployeeIdGeneratedToIndiaCCHr-CC");
					HashMap<String,String> emailids = Utils.getEmailIdFromPickllistNameAndEmployeeNameAssoc(picklistNames, session);
					if("DAH2".equalsIgnoreCase(client) && "Helius".equalsIgnoreCase(payroll)){
						getCC = getCC + ","+Utils.getHapProperty("notifyEmployeeIdGeneratedForIndiaCC-DAH2-Helius");	
						if(emailids!=null && emailids.get(recruiterName) != null && !emailids.get(recruiterName).isEmpty()){
							getCC = getCC+","+emailids.get(recruiterName);
						}
						if(emailids!=null && emailids.get(accountManagerName) != null && !emailids.get(accountManagerName).isEmpty()){
							getCC = getCC+","+emailids.get(accountManagerName);
						}
					}
					if("DAH2".equalsIgnoreCase(client) && !"Helius".equalsIgnoreCase(payroll)){
						getCC = getCC + ","+Utils.getHapProperty("notifyEmployeeIdGeneratedForIndiaCC-DAH2-nonHelius");	
					}
					if(!"DAH2".equalsIgnoreCase(client)){
						getCC = getCC + ","+Utils.getHapProperty("notifyEmployeeIdGeneratedForIndiaCC-nonDAH2");	
						if(emailids!=null && emailids.get(recruiterName) != null && !emailids.get(recruiterName).isEmpty()){
							getCC = getCC+","+emailids.get(recruiterName);
						}
						if(emailids!=null && emailids.get(accountManagerName) != null && !emailids.get(accountManagerName).isEmpty()){
							getCC = getCC+","+emailids.get(accountManagerName);
						}
					}
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
				}				
				if("Thailand".equalsIgnoreCase(location)){
					Email.setTo(Utils.getHapProperty("notifyEmployeeIdGeneratedToThailandPayrollUser-TO"));
					String getCC = Utils.getHapProperty("notifyEmployeeIdGeneratedToThailandCCHr-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
				}
				Email.setCc(cc);
				Email.setSubject("Onboarding documents "+onboardingStatus+" and BGV "+bgv_status+" for candidate - "+obj.getEmployee_name());
				Email.setText("Hi,"+"\n\n"
						+ "Prior alert for new candidate joining"+"\n\n" 
						+ "Candidate Name -  "+obj.getEmployee_name()+ "\n"
						+ "Expected DOJ - "+join.getDayOfMonth()+"-"+join.getMonthValue()+"-"+join.getYear()+"\n"
						+ "Location - "+location +"\n"
						+ "Client - "+client +"\n"
						+ "Account Manager - "+accountManagerName +"\n"
						+ "Recruiter - "+recruiterName +"\n"
						+ "Payroll - "+payroll +"\n"
						+ "Background verification - " + bgv_status +"\n"
						+ "Onboarding Status - " + onboardingStatus +"\n\n"
						+ "Pending onboarding items are : "+"\n\n"
						+ message.toString()+"\n\n"
						+ "Thanks," + "\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
				emailService.sendEmail(Email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
			}
	
}
