/**
 * 
 */
package com.helius.dao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Contact_Address_Details;
import com.helius.entities.DeleteIndianEmployeeFamilyMember;
import com.helius.entities.DeleteSingaporeEmployeeFamilyMember;
import com.helius.entities.EmailScreen;
import com.helius.entities.Emergency_Contact_Details;
import com.helius.entities.Employee;
import com.helius.entities.Employee_Appraisal_Details;
import com.helius.entities.Employee_Assignment_Details;
import com.helius.entities.Employee_Bank_Details;
import com.helius.entities.Employee_Identification_Details;
import com.helius.entities.Employee_Offer_Details;
import com.helius.entities.Employee_Personal_Details;
import com.helius.entities.Employee_Salary_Details;
import com.helius.entities.Employee_Selfcare_Users;
import com.helius.entities.Employee_Terms_And_Conditions;
import com.helius.entities.Employee_Work_Permit_Details;
import com.helius.entities.Help_Videos;
import com.helius.entities.Indian_Employee_Family_Member;
import com.helius.entities.Indian_Employees_Insurance_Details;
import com.helius.entities.Leave_Eligibility_Details;
import com.helius.entities.Leave_Usage_Details;
import com.helius.entities.Singapore_Employee_Family_Member;
import com.helius.entities.Singapore_Employee_Insurance_Details;
import com.helius.entities.Sow_Ctc_Breakup;
import com.helius.entities.Sow_Employee_Association;
import com.helius.entities.Timesheet_Email;
import com.helius.entities.Work_Permit_Master;
import com.helius.managers.ClientManager;
import com.helius.service.EmailService;
import com.helius.utils.AllClientDetails;
import com.helius.utils.FilecopyStatus;
import com.helius.utils.Utils;
/**
 * @author Tirumala 22-Feb-2018
 */
public class EmployeeDAOImpl implements IEmployeeDAO {

	private final String hql1 = "delete from Employee_Personal_Details where employee_id = :employee_id";
	private final String hql2 = "delete from Employee_Salary_Details where employee_id = :employee_id";
	private final String hql3 = "delete from Employee_Assignments where employee_id = :employee_id";
	private final String hql4 = "delete from Employee_Professional_Details where employee_id = :employee_id";
	private final String hql5 = "delete from Employee_Insurance_Details where employee_id = :employee_id";

	/**
	 * @return the sessionFactory
	 */
	private org.hibernate.internal.SessionFactoryImpl sessionFactory;

	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 
	 */
	public EmployeeDAOImpl() {
		// TODO Auto-generated constructor stub
	}
	@Autowired
	ApplicationContext context;
	@Autowired
	private EmailService emailService;
	@Autowired
	private LeaveServiceImpl leaveserv;
	private List<String> copied_with_success = new ArrayList<String>();

	/** returns employee details **/
	@SuppressWarnings("unchecked")
	@Override
	public Employee get(String employeeid) {
		Employee emp = new Employee();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			// Employee personal Details
			Employee_Personal_Details employee_Personal_Details = (Employee_Personal_Details) session
					.get(Employee_Personal_Details.class, employeeid);
			if (employee_Personal_Details == null) {
				session.close();
				return null;
			} else {
				emp.setEmployeePersonalDetails(employee_Personal_Details);
			}
			
			// Salary details
			String employee_salary_query = "select salary.* from Employee_Salary_Details salary where employee_id = :employee_id ";
			java.util.List salaryList = session.createSQLQuery(employee_salary_query)
					.addEntity(Employee_Salary_Details.class).setParameter("employee_id", employeeid).list();
			Employee_Salary_Details employee_Salary_Details = null;
			if (!salaryList.isEmpty()) {
				employee_Salary_Details = (Employee_Salary_Details) salaryList.iterator().next();
			}
			if (employee_Salary_Details != null) {
				emp.setEmployeeSalaryDetails(employee_Salary_Details);
			}

			// Assignment Details
			String employee_assignment_query = "select assignments.* from Employee_Assignment_Details assignments where employee_id = :employee_id ";
			java.util.List assignmentList = session.createSQLQuery(employee_assignment_query)
					.addEntity(Employee_Assignment_Details.class).setParameter("employee_id", employeeid).list();
			Employee_Assignment_Details employee_Assigmnent_Details = null;
			if (!assignmentList.isEmpty()) {
				employee_Assigmnent_Details = (Employee_Assignment_Details) assignmentList.iterator().next();
			}
			if (employee_Assigmnent_Details != null) {
				emp.setEmployeeAssignmentDetails(employee_Assigmnent_Details);
			}

			// Employee bank details
			String bankdetails_query = "select bankdetails.* from Employee_Bank_Details bankdetails where employee_id = :employee_id ";
			java.util.List bankList = session.createSQLQuery(bankdetails_query).addEntity(Employee_Bank_Details.class)
					.setParameter("employee_id", employeeid).list();
			Employee_Bank_Details employee_Bank_Details = null;
			if (!bankList.isEmpty()) {
				employee_Bank_Details = (Employee_Bank_Details) bankList.iterator().next();
			}
			if (employee_Bank_Details != null) {
				emp.setEmployeeBankDetails(employee_Bank_Details);
			}

			// Employee appraisal details
			  String appraisal_query = "select appraisal.* from Employee_Appraisal_Details appraisal where employee_id = :employee_id "; 
			  java.util.List appraisalList = session.createSQLQuery(appraisal_query).addEntity(Employee_Appraisal_Details.class).setParameter("employee_id", employeeid).list();
			  Employee_Appraisal_Details employee_Appraisal_Details = null; 
			  if(!appraisalList.isEmpty()) {
			  employee_Appraisal_Details = (Employee_Appraisal_Details) appraisalList.iterator().next();
			  }
			  if(employee_Appraisal_Details!= null) {
			  emp.setEmployeeAppraisalDetails(employee_Appraisal_Details); 
			  }
			  
			  String sowbreakup_query = "select breakup.* from Sow_Ctc_Breakup breakup where employee_id = :employee_id AND status = :status"; 
			  java.util.List breakupList = session.createSQLQuery(sowbreakup_query).addEntity(Sow_Ctc_Breakup.class).setParameter("employee_id", employeeid).setParameter("status", "active").list();
			  Sow_Ctc_Breakup sowCtcBreakup = null; 
				List<Sow_Ctc_Breakup> sow_Ctc_BreakupList = new ArrayList<Sow_Ctc_Breakup>();
			/*	Collections.sort(breakupList,new Comparator<Sow_Ctc_Breakup>() {
					@Override
					public int compare(Sow_Ctc_Breakup o1, Sow_Ctc_Breakup o2) {
						// TODO Auto-generated method stub
						//return o1.getGroupId().compareTo(o2.getGroupId());  
						return Integer.compare(o1.getGroupId(),o2.getGroupId());  
					}
				}); */
			  if(!breakupList.isEmpty()) {
				  for (Object sowbreakup : breakupList) {
					  sowCtcBreakup = (Sow_Ctc_Breakup) sowbreakup;
					  sow_Ctc_BreakupList.add(sowCtcBreakup);
					}			  }
			  if(sow_Ctc_BreakupList!= null) {
			  emp.setSowCtcBreakup(sow_Ctc_BreakupList); 
			  }
			  
			  String sowbreakuphis_query = "select breakup.* from Sow_Ctc_Breakup breakup where employee_id = :employee_id AND status = :status"; 
			  java.util.List breakuphisList = session.createSQLQuery(sowbreakuphis_query).addEntity(Sow_Ctc_Breakup.class).setParameter("employee_id", employeeid).setParameter("status", "inactive").list();
			  Sow_Ctc_Breakup sowCtcBreakupHis = null; 
			//	List<Object> sow_Ctc_BreakupHisList = new ArrayList<Object>();
				HashMap<String, List<Sow_Ctc_Breakup>> map = new HashMap<String, List<Sow_Ctc_Breakup>>();
				List<Sow_Ctc_Breakup> grp = new ArrayList<Sow_Ctc_Breakup>();
			  if(!breakuphisList.isEmpty()) {
				  for (Object sowbreakuphislst : breakuphisList) {
					  sowCtcBreakupHis = (Sow_Ctc_Breakup) sowbreakuphislst;
					  String key = Integer.toString(sowCtcBreakupHis.getGroupId());
					if(map.containsKey(key)){
						 grp =  map.get(key);
						 grp.add(sowCtcBreakupHis);
					}else{
						 grp = new ArrayList<Sow_Ctc_Breakup>();
						 grp.add(sowCtcBreakupHis);
						 String key1 = Integer.toString(sowCtcBreakupHis.getGroupId());
						 map.put(key1,grp);
					}
					}			  }
			  if(!map.isEmpty()) {
			  emp.setSowCtcBreakupHistory(map.values()); 
			  }
			 
			  String sowEmpAssoc_query = "select empAssoc.* from Sow_Employee_Association empAssoc where employee_id = :employee_id"; 
			  java.util.List empAssocList = session.createSQLQuery(sowEmpAssoc_query).addEntity(Sow_Employee_Association.class).setParameter("employee_id", employeeid).list();
			  Sow_Employee_Association sowAssoc = null; 
				List<Sow_Employee_Association> sow_EmpAssoscList = new ArrayList<Sow_Employee_Association>();
			  if(!empAssocList.isEmpty()) {
				  for (Object empassoc : empAssocList) {
					  sowAssoc = (Sow_Employee_Association) empassoc;
					  sow_EmpAssoscList.add(sowAssoc);
					}			  }
			  if(sow_EmpAssoscList!= null) {
			  emp.setSowEmployeeAssoc(sow_EmpAssoscList); 
			  }	  
			// Employee leave eligibility
			LocalDate date = LocalDate.now();
			int year = date.getYear();
			String leaveeligibility_query = "select leaveeligible.* from Leave_Eligibility_Details leaveeligible where employee_id = :employee_id AND year = :year";
			java.util.List leaveeligibilityList = session.createSQLQuery(leaveeligibility_query)
					.addEntity(Leave_Eligibility_Details.class).setParameter("employee_id", employeeid).setParameter("year",year).list();
			Leave_Eligibility_Details leave_Eligibility_Details = null;
			List<Leave_Eligibility_Details> leave_Eligibility_DetailsList = new ArrayList<Leave_Eligibility_Details>();
			if (!leaveeligibilityList.isEmpty()) {
				for (Object leave_Eligibility : leaveeligibilityList) {
					leave_Eligibility_Details = (Leave_Eligibility_Details) leave_Eligibility;
					leave_Eligibility_DetailsList.add(leave_Eligibility_Details);
				}
			}
			if (leave_Eligibility_DetailsList != null) {
				emp.setLeavesEligibility(leave_Eligibility_DetailsList);
			}
			// Employee offer details
			String employee_offer_query = "select offer.* from Employee_Offer_Details offer where employee_id = :employee_id ";
			java.util.List offerList = session.createSQLQuery(employee_offer_query)
					.addEntity(Employee_Offer_Details.class).setParameter("employee_id", employeeid).list();
			Employee_Offer_Details employee_offer_Details = null;
			if (!offerList.isEmpty()) {
				employee_offer_Details = (Employee_Offer_Details) offerList.iterator().next();
			}
			if (employee_offer_Details != null) {
				emp.setEmployeeOfferDetails(employee_offer_Details);
			}
			// Employee Terms and Conditions
			String employee_TandC_query = "select TandC.* from Employee_Terms_And_Conditions TandC where employee_id = :employee_id ";
			java.util.List TandCList = session.createSQLQuery(employee_TandC_query)
					.addEntity(Employee_Terms_And_Conditions.class).setParameter("employee_id", employeeid).list();
			Employee_Terms_And_Conditions Employee_Terms_And_Conditions = null;
			if (!TandCList.isEmpty()) {
				Employee_Terms_And_Conditions = (Employee_Terms_And_Conditions) TandCList.iterator().next();
			}
			if (Employee_Terms_And_Conditions != null) {
				emp.setEmployeeTermsAndConditions(Employee_Terms_And_Conditions);
			}

			// Employee Singapore insurance details
			if (emp.getEmployeeOfferDetails() != null) {
				if (emp.getEmployeeOfferDetails().getWork_country().equals("Singapore")) {
					String employee_SG_insurance_query = "select insurance.* from Singapore_Employee_Insurance_Details insurance where employee_id = :employee_id ";
					java.util.List sgInsuranceList = session.createSQLQuery(employee_SG_insurance_query)
							.addEntity(Singapore_Employee_Insurance_Details.class)
							.setParameter("employee_id", employeeid).list();
					Singapore_Employee_Insurance_Details Singapore_Insurance_Details = null;
					if (!sgInsuranceList.isEmpty()) {
						Singapore_Insurance_Details = (Singapore_Employee_Insurance_Details) sgInsuranceList.iterator()
								.next();
					}
					if (Singapore_Insurance_Details != null) {
						String sg_members_query = "select SGMembers.* from Singapore_Employee_Family_Member SGMembers where employee_id = :employee_id ";
						java.util.List memberlist = session.createSQLQuery(sg_members_query)
								.addEntity(Singapore_Employee_Family_Member.class)
								.setParameter("employee_id", employeeid).list();
						Singapore_Employee_Family_Member singaporemember = null;
						List<Singapore_Employee_Family_Member> singaporememberList = new ArrayList<Singapore_Employee_Family_Member>();
						for (Object singaporemembers : memberlist) {
							singaporemember = (Singapore_Employee_Family_Member) singaporemembers;
							singaporememberList.add(singaporemember);
						}
						Singapore_Insurance_Details.setSingaporeEmployeeFamilyMember(singaporememberList);
						emp.setSingaporeEmployeeInsuranceDetails(Singapore_Insurance_Details);
					}
				}
			}

			// Employee india insurance details
			if (emp.getEmployeeOfferDetails() != null) {
				if (emp.getEmployeeOfferDetails().getWork_country().equals("India"))
					;
				{
					String employee_IN_insurance_query = "select insurance.* from Indian_Employees_Insurance_Details insurance where employee_id = :employee_id ";
					java.util.List inInsuranceList = session.createSQLQuery(employee_IN_insurance_query)
							.addEntity(Indian_Employees_Insurance_Details.class).setParameter("employee_id", employeeid)
							.list();
					Indian_Employees_Insurance_Details indian_Insurance_Details = null;
					if (!inInsuranceList.isEmpty()) {
						indian_Insurance_Details = (Indian_Employees_Insurance_Details) inInsuranceList.iterator()
								.next();
					}

					if (indian_Insurance_Details != null) {
						String in_members_query = "select inMembers.* from Indian_Employee_Family_Member inMembers where employee_id = :employee_id ";
						java.util.List memberlist = session.createSQLQuery(in_members_query)
								.addEntity(Indian_Employee_Family_Member.class).setParameter("employee_id", employeeid)
								.list();
						Indian_Employee_Family_Member indianmember = null;
						List<Indian_Employee_Family_Member> indianmemberList = new ArrayList<Indian_Employee_Family_Member>();
						for (Object indianmembers : memberlist) {
							indianmember = (Indian_Employee_Family_Member) indianmembers;
							indianmemberList.add(indianmember);
						}
						indian_Insurance_Details.setIndianEmployeeFamilyMember(indianmemberList);
						emp.setIndianEmployeesInsuranceDetails(indian_Insurance_Details);
					}
				}
			}

			// Employee identification details
			String employee_identification_details_query = "select identificationdetails.* from Employee_Identification_Details identificationdetails where employee_id = :employee_id ";
			java.util.List identificationDetails_List = session.createSQLQuery(employee_identification_details_query)
					.addEntity(Employee_Identification_Details.class).setParameter("employee_id", employeeid).list();
			Employee_Identification_Details employee_Identification_Details = null;
			List<Employee_Identification_Details> employee_Identification_DetailsList = new ArrayList<Employee_Identification_Details>();
			for (Object employee_Identification : identificationDetails_List) {
				employee_Identification_Details = (Employee_Identification_Details) employee_Identification;
				employee_Identification_DetailsList.add(employee_Identification_Details);
			}
			emp.setEmployeeIdentificationDetails(employee_Identification_DetailsList);

			// Employee Contact address details
			String employee_contact_addressdetails_query = "select contactaddressdetails.* from Contact_Address_Details contactaddressdetails where employee_id = :employee_id ";
			java.util.List contactaddress_List = session.createSQLQuery(employee_contact_addressdetails_query)
					.addEntity(Contact_Address_Details.class).setParameter("employee_id", employeeid).list();
			Contact_Address_Details employee_Contact_Address_Details = null;
			List<Contact_Address_Details> employee_Contact_Address_DetailsList = new ArrayList<Contact_Address_Details>();
			if (!contactaddress_List.isEmpty()) {
				for (Object employee_ContactAddress : contactaddress_List) {
					employee_Contact_Address_Details = (Contact_Address_Details) employee_ContactAddress;
					employee_Contact_Address_DetailsList.add(employee_Contact_Address_Details);
				}
			}
			if (employee_Contact_Address_DetailsList != null) {
				emp.setEmployeeContactAddressDetails(employee_Contact_Address_DetailsList);
			}

			// Emergency Details
			String employee_emergency_query = "select emergency.* from Emergency_Contact_Details emergency where employee_id = :employee_id ";
			java.util.List emergencyList = session.createSQLQuery(employee_emergency_query)
					.addEntity(Emergency_Contact_Details.class).setParameter("employee_id", employeeid).list();
			Emergency_Contact_Details employee_Emergency_Details = null;
			if (!emergencyList.isEmpty()) {
				employee_Emergency_Details = (Emergency_Contact_Details) emergencyList.iterator().next();
			}
			if (employee_Emergency_Details != null) {
				emp.setEmergencyContactDetails(employee_Emergency_Details);
			}
			
			// Employee work permit details
			String employee_work_permit_details_query = "select workpermitdetails.* from Employee_Work_Permit_Details workpermitdetails where employee_id = :employee_id ";
			java.util.List workpermitDetails_List = session.createSQLQuery(employee_work_permit_details_query)
					.addEntity(Employee_Work_Permit_Details.class).setParameter("employee_id", employeeid).list();
			Employee_Work_Permit_Details employee_Work_Permit_Details = null;
			if (!workpermitDetails_List.isEmpty()) {
				employee_Work_Permit_Details = (Employee_Work_Permit_Details) workpermitDetails_List.iterator().next();
			}
			if (employee_Work_Permit_Details != null) {
				emp.setEmployeeWorkPermitDetails(employee_Work_Permit_Details);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return emp;
	}

	public String WorkLocationPicklist() {
		Session session = null;
		String workPermitJson = null;
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT Country_Master.country_code, Country_Master.nationality,Work_Permit_Master.work_location, Work_Permit_Master.work_permit_master_id,Work_Permit_Master.display_fields,Work_Permit_Master.work_permit_name,Country_Master.identification_ids FROM Country_Master LEFT JOIN Work_Permit_Master ON Country_Master.country_code = Work_Permit_Master.country_code ORDER BY Country_Master.country_code");
			String work_Permit_Master_query = "SELECT Country_Master.country_code, Country_Master.nationality,Work_Permit_Master.work_location, Work_Permit_Master.work_permit_master_id,Work_Permit_Master.display_fields,Work_Permit_Master.work_permit_name,Country_Master.identification_ids FROM Country_Master LEFT JOIN Work_Permit_Master ON Country_Master.country_code = Work_Permit_Master.country_code ORDER BY Country_Master.country_code";
			java.util.List work_Permit_MasterList = session.createSQLQuery(work_Permit_Master_query)
					.addEntity(Work_Permit_Master.class).list();
			Work_Permit_Master work_Permit_Master = null;
			List<Object[]> work_Permit_Mstr = query.list();
			workPermitJson = Utils.jsonWorkPermitPicklist(work_Permit_Mstr);
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"workPermitJson\" : " + workPermitJson + "}";
	}

	/** Picklist for employee master **/
	@SuppressWarnings("unchecked")
	@Override
	public String masterpicklist() {
		Session session = null;
		String clientGroupsJson = null;
		String groupJson = null;
		String subgroupJson = null;
		String budgetownerJson = null;
		String timesheetapproverJson = null;
		String clientHiringManagerJson = null;
		String nationalityJson = null;
		String countryWLJson = null;
		String workpermits = null;
		String workPermitJson = null;
		String recruiterJson = null;
		String accountmanagerJson = null;
		String checklistJson = null;
		String allClientDetails = null;
		String hrJson = null;
		String payrollEntity = null;
		ObjectMapper om = new ObjectMapper();
		session = sessionFactory.openSession();
		try {
			try {
				SQLQuery querydropdown = session
						.createSQLQuery("SELECT client_name,group_team,sub_group from Client_Master");
				List<Object[]> Clientlists = querydropdown.list();
				Set<String> client_name = new HashSet<String>();
				Set<String> group = new HashSet<String>();
				Set<String> sub_group = new HashSet<String>();
				for (Object[] Clientlist : Clientlists) {
					if (Clientlist[0] != null) {
						String client = Clientlist[0].toString();
						if (client.length() > 0) {
							client_name.add(client);
						}
					}
					if (Clientlist[1] != null) {
						String grouplist = Clientlist[1].toString();
						if (grouplist.length() > 0) {
							group.add(grouplist);
						}
					}
					if (Clientlist[2] != null) {
						String subgrouplist = Clientlist[2].toString();
						if (subgrouplist.length() > 0) {
							sub_group.add(subgrouplist);
						}
					}
				}
				
				SQLQuery recruitQuery = session.createSQLQuery("SELECT picklist_name from Picklist_Items WHERE picklist_type='recruiter'");
				List<String> recruitlists = recruitQuery.list();
				Set<String> recruiter = new HashSet<String>();
				for (String recruiterlist : recruitlists) {
					if (recruiterlist != null) {
						String recruitlist = recruiterlist;
						if (recruitlist.length() > 0) {
							recruiter.add(recruitlist);
						}
					}
				}

				SQLQuery AccMgrQuery = session.createSQLQuery("SELECT picklist_name from Picklist_Items WHERE picklist_type='accountmanager'");
				List<String> AccMgrlists = AccMgrQuery.list();
				Set<String> AccountManagerList = new HashSet<String>();
				for (String AccountMgrlist : AccMgrlists) {
					if (AccountMgrlist != null) {
						String AccountMgrlists = AccountMgrlist;
						if (AccountMgrlists.length() > 0) {
							AccountManagerList.add(AccountMgrlists);
						}
					}
				}
				
				SQLQuery hrQuery = session.createSQLQuery("SELECT picklist_name from Picklist_Items WHERE picklist_type='hr'");
				List<String> hrlists = hrQuery.list();
				Set<String> HrList = new HashSet<String>();
				for (String hrrlist : hrlists) {
					if (hrrlist != null) {
						String hr = hrrlist;
						if (hr.length() > 0) {
							HrList.add(hr);
						}
					}
				}
				
				SQLQuery query = session
						.createSQLQuery("SELECT manager_name from Client_Manager_Master WHERE role='budget_owner'");
				List<String> querylists = query.list();
				Set<String> manager_name = new HashSet<String>();
				for (String ClientMgrlist : querylists) {
					if (ClientMgrlist != null) {
						String mgrlist = ClientMgrlist;
						if (mgrlist.length() > 0) {
							manager_name.add(mgrlist);
						}
					}
				}
				SQLQuery queryies = session.createSQLQuery(
						"SELECT manager_name from Client_Manager_Master WHERE role='timesheet_approver'");
				List<String> queryieslists = queryies.list();
				Set<String> approver = new HashSet<String>();
				for (String approverlist : queryieslists) {
					if (approverlist != null) {
						String approverslist = approverlist;
						if (approverslist.length() > 0) {
							approver.add(approverslist);
						}
					}
				}
				SQLQuery cliHM = session.createSQLQuery(
						"SELECT client_hiring_manager from Employee_Assignment_Details");
				List<String> cliHMLists = cliHM.list();
				Set<String> cliHm = new HashSet<String>();
				for (String cliHMList : cliHMLists) {
					if (cliHMList != null) {
						String cliHMlist = cliHMList;
						if (cliHMlist.length() > 0) {
							//if(!cliHMlist.equalsIgnoreCase("-")){
							cliHm.add(cliHMlist);
							cliHm.remove("-");
							//}
						}
					}
				}
				SQLQuery queryWPList = session.createSQLQuery("SELECT nationality,country_name from Country_Master");
				List<Object[]> WLlists = queryWPList.list();
				Set<String> nationality = new HashSet<String>();
				Set<String> countryWL = new HashSet<String>();
				for (Object[] WLlist : WLlists) {
					if (WLlist[0] != null) {
						String Nation = WLlist[0].toString();
						if (Nation.length() > 0) {
							nationality.add(Nation);
						}
					}
					countryWL.add("Singapore");
					countryWL.add("India");
					countryWL.add("Malaysia");
					countryWL.add("People's Republic of China");
					countryWL.add("Thailand");
					/*if (WLlist[1] != null) {
						String WL = WLlist[1].toString();
						if (WL.length() > 0) {
							countryWL.add(WL);
						}
					}*/
				}
				SQLQuery WPQuery = session.createSQLQuery("SELECT work_permit_name from Work_Permit_Master");
				List<String> WPlists = WPQuery.list();
				Set<String> workpermit = new HashSet<String>();
				for (String WPlist : WPlists) {
					if (WPlist != null) {
						String permit = WPlist;
						if (permit.length() > 0) {
							workpermit.add(permit);
						}
					}
				}
				
				SQLQuery payrolQuery = session.createSQLQuery("SELECT payroll_entity_name,payroll_display_name from Payroll_Entity_Master");
				List<Object[]> payrollists = payrolQuery.list();
				HashMap<String,String> payrolEntity = new HashMap<String,String>();
				for (Object[] payrollist : payrollists) {
					if (payrollist[0] != null) {
						String payrol = payrollist[0].toString();
						String payrolValue = null;
						if (payrol.length() > 0) {
							if(payrollist[1] != null){
								payrolValue = payrollist[1].toString();
							}
							payrolEntity.put(payrol,payrolValue);
						}
					}
				}
					payrollEntity = om.writeValueAsString(payrolEntity);
				/*
				Set<String> AmReportlist = new HashSet<String>();
				String Amlists = Utils.getHapProperty("VendorAMList");
				String[] splitAMs = Amlists.split(",");
				for(String splitAM : splitAMs){		
					AmReportlist.add(splitAM);
				}
				AMReportListJson = Utils.jsonPicklist(AmReportlist);
				*/
				accountmanagerJson = Utils.jsonPicklist(AccountManagerList);
				recruiterJson = Utils.jsonPicklist(recruiter);
				clientGroupsJson = Utils.jsonPicklist(client_name);
				groupJson = Utils.jsonPicklist(group);
				subgroupJson = Utils.jsonPicklist(sub_group);
				budgetownerJson = Utils.jsonPicklist(manager_name);
				timesheetapproverJson = Utils.jsonPicklist(approver);
				clientHiringManagerJson = Utils.jsonPicklist(cliHm);
				nationalityJson = Utils.jsonPicklist(nationality);
				countryWLJson = Utils.jsonPicklist(countryWL);
				workpermits = Utils.jsonPicklist(workpermit);
				hrJson = Utils.jsonPicklist(HrList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
				SQLQuery checklistQuery = session.createSQLQuery("SELECT checklist_type from Employee_Checklist_Master");
				List<String> checklistQuerylst = checklistQuery.list();
				Set<String> checklist = new HashSet<String>();
				for (String checklistList : checklistQuerylst) {
					if (checklistList != null) {
						String checklists = checklistList;
						if (checklists.length() > 0) {
							checklist.add(checklists);
						}
					}
				}
				checklistJson = Utils.jsonPicklist(checklist);
				}catch (Exception e) {
					e.printStackTrace();
				}
			try {
				SQLQuery WPJSONquery = session.createSQLQuery(
						"SELECT Country_Master.country_code, Country_Master.nationality,Work_Permit_Master.work_location, Work_Permit_Master.work_permit_master_id,Work_Permit_Master.display_fields,Work_Permit_Master.work_permit_name,Country_Master.identification_ids FROM Country_Master LEFT JOIN Work_Permit_Master ON Country_Master.country_code = Work_Permit_Master.country_code ORDER BY Country_Master.country_code");
				String work_Permit_Master_query = "SELECT Country_Master.country_code, Country_Master.nationality,Work_Permit_Master.work_location, Work_Permit_Master.work_permit_master_id,Work_Permit_Master.display_fields,Work_Permit_Master.work_permit_name,Country_Master.identification_ids FROM Country_Master LEFT JOIN Work_Permit_Master ON Country_Master.country_code = Work_Permit_Master.country_code ORDER BY Country_Master.country_code";
				java.util.List work_Permit_MasterList = session.createSQLQuery(work_Permit_Master_query)
						.addEntity(Work_Permit_Master.class).list();
				Work_Permit_Master work_Permit_Master = null;
				List<Object[]> work_Permit_Mstr = WPJSONquery.list();
			//	workPermitJson = Utils.jsonWorkPermitPicklist(work_Permit_Mstr);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ClientManager clientMgr = (ClientManager) context.getBean("clientManager");
			try {
				AllClientDetails allclipicklist = clientMgr.getClient_Group_HiringManagerDetails();
			allClientDetails = om.writeValueAsString(allclipicklist);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			session.close();
		}
		return "{\"client\" : " + clientGroupsJson + ",\"group\" :" + groupJson + ",\"subgroup\" : " + subgroupJson
				+ ",\"budgetowner\" : " + budgetownerJson + ",\"timesheetapprover\" : " + timesheetapproverJson
				+ ",\"heliusrecruiter\" :" + recruiterJson + ",\"hr\" :" + hrJson + ",\"clientHiringManager\" :" + clientHiringManagerJson + ",\"accountmanager\" :" + accountmanagerJson + ",\"nationality\" : " + nationalityJson
				+ ",\"worklocation\" :" + countryWLJson + ",\"workpermit\" :" + workpermits + ",\"workPermitJson\" :"
				+ workPermitJson +",\"payrollEntity\" :" + payrollEntity + ",\"checklistJson\" :" + checklistJson + ",\"allClientDetails\" :" + allClientDetails + "}";
	}

	@Override
	public void update(Employee employee, EmployeeSections employeesection, Map<String, Object> changedfields) {
		// TODO Auto-generated method stub

	}
	/** Returns offer details using offerid **/
	public Employee getOfferDetails(String offerid) {
		Employee emp = new Employee();
		Session session = null;
		try {
			session = sessionFactory.openSession();
			String offer_query = "select * from Employee_Offer_Details where offer_id = :offer_id ";
			java.util.List offerlist = session.createSQLQuery(offer_query).addEntity(Employee_Offer_Details.class)
					.setParameter("offer_id", offerid).list();
			Employee_Offer_Details check_Employee_OfferDetails = null;
			if (!offerlist.isEmpty()) {
				check_Employee_OfferDetails = (Employee_Offer_Details) offerlist.iterator().next();
			}
			if (check_Employee_OfferDetails != null) {
				emp.setEmployeeOfferDetails(check_Employee_OfferDetails);
			}
			String employee_salary_query = "select salary.* from Employee_Salary_Details salary where offer_id = :offer_id ";
			java.util.List salaryList = session.createSQLQuery(employee_salary_query)
					.addEntity(Employee_Salary_Details.class).setParameter("offer_id", offerid).list();
			Employee_Salary_Details employee_Salary_Details = null;
			if (!salaryList.isEmpty()) {
				employee_Salary_Details = (Employee_Salary_Details) salaryList.iterator().next();
			}
			if (employee_Salary_Details != null) {
				emp.setEmployeeSalaryDetails(employee_Salary_Details);
			}
			
			String sowbreakup_query = "select breakup.* from Sow_Ctc_Breakup breakup where offer_id = :offer_id AND group_id =:group_id"; 
			  java.util.List breakupList = session.createSQLQuery(sowbreakup_query).addEntity(Sow_Ctc_Breakup.class).setParameter("offer_id", offerid).setParameter("group_id", 1).list();
			  Sow_Ctc_Breakup sowCtcBreakup = null; 
				List<Sow_Ctc_Breakup> sow_Ctc_BreakupList = new ArrayList<Sow_Ctc_Breakup>();
			  if(!breakupList.isEmpty()) {
				  for (Object sowbreakup : breakupList) {
					  sowCtcBreakup = (Sow_Ctc_Breakup) sowbreakup;
					  sow_Ctc_BreakupList.add(sowCtcBreakup);
					}			  }
			  if(sow_Ctc_BreakupList!= null) {
			  emp.setSowCtcBreakup(sow_Ctc_BreakupList); 
			  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		return emp;
	}
	
	/** Return offer details by name **/
	@Override
	public List<Employee_Offer_Details> getOfferDetailsByName(String offerName,String type) {
		Session session = null;
		int offerid = 0;
		List<Employee_Offer_Details> employee_Offer_Details = new ArrayList<>();
		java.util.List offerlist = null;
		try {
			session = sessionFactory.openSession();
			if(type.equalsIgnoreCase("NAME")){
			String offer_query = "select * from Employee_Offer_Details where employee_name LIKE :offerName";
			 offerlist = session.createSQLQuery(offer_query).addEntity(Employee_Offer_Details.class)
					.setParameter("offerName", "%"+ offerName + "%").list();
			}else{
				String offer_query = "select * from Employee_Offer_Details where offer_id = :offer_id";
				 offerlist = session.createSQLQuery(offer_query).addEntity(Employee_Offer_Details.class)
						.setParameter("offer_id", offerName).list();
			}
			Employee_Offer_Details check_Employee_OfferDetails = null;
			for (Object Employee_Offer_Detail : offerlist) {
				check_Employee_OfferDetails = (Employee_Offer_Details) Employee_Offer_Detail;
				employee_Offer_Details.add(check_Employee_OfferDetails);
			}
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employee_Offer_Details;
	}

	/**
	 * @author Vinay 
	 * employee_id  is generated based on work country and payroll entity
	 * 
	 */
//	@Override
	public String generateEmployeeId1(String offerId) {
		Employee employee = getOfferDetails(offerId);
		String location = null;
		String payroll = null;
		Session session = null;
		String prefix = null;
		int empIdStartRange = 0;
		int empIdEndRange = 0;
		try{
		if(employee.getEmployeeOfferDetails().getWork_country()!=null){
		 location = employee.getEmployeeOfferDetails().getWork_country();
		}
		if(employee.getEmployeeSalaryDetails().getPayroll_entity()!=null){
		 payroll = employee.getEmployeeSalaryDetails().getPayroll_entity();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		if (location != null) {
			if (location.equals("Singapore") && "Helius".equals(payroll)) {
				prefix = "SG-";
				empIdStartRange = 2001;
				empIdEndRange = 5000;
			}
			if (location.equals("Singapore") && !"Helius".equals(payroll)) {
				prefix = "SGNH-";
				empIdStartRange = 5001;
				empIdEndRange = 6000;
			}
			if (location.equals("Singapore") && payroll == null) {
				prefix = "SG-";
				empIdStartRange = 2001;
				empIdEndRange = 5000;
			}
			if (location.equals("India")) {
				prefix = "IN-";
				empIdStartRange = 7001;
				empIdEndRange = 9000;
			}
			if (location.equals("Malaysia")) {
				prefix = "MA-";
				empIdStartRange = 6001;
				empIdEndRange = 7000;
			}
		} else {
			return "unable to generate Employee ID as worklocation is not available";
		}
		ArrayList<String> al = new ArrayList<String>();
		String empids = null;
		String employee_id = null;
		try {
			session = sessionFactory.openSession();
			SQLQuery querydropdown = session
					.createSQLQuery("SELECT employee_id from Employee_Personal_Details where employee_id LIKE :prefix");
			querydropdown.setParameter("prefix", prefix + "%");
			List<String> empidsss = querydropdown.list();
			al.addAll(empidsss);
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(al);
		for (String st : al) {
			empids = st;
		}
		if (empids == null) {
			int endRange = empIdStartRange - 1;
			empids = prefix + endRange;
		}
		String[] values = empids.split("-", 2);
		int usrId = Integer.parseInt(values[1]) + 1;
		String generatedempid = values[0] + "-" + usrId;
		if (usrId < empIdEndRange) {
			employee_id = generatedempid;
		} else {
			return "series limit is reached";
		}
		return employee_id;
	}
	
	@Override
	public String generateEmployeeId(String offerId) {
		Employee employee = getOfferDetails(offerId);
		String location = null;
		String payroll = null;
		Session session = null;
		int empIdStartRange = 0;
		int empIdEndRange = 0;
		String employee_id = null;
		try{
			session = sessionFactory.openSession();
		if(employee.getEmployeeOfferDetails().getWork_country()!=null){
		 location = employee.getEmployeeOfferDetails().getWork_country();
		}
		if(employee.getEmployeeSalaryDetails().getPayroll_entity()!=null){
			 payroll = employee.getEmployeeSalaryDetails().getPayroll_entity();
			}
		if (location != null) {
			if ("Singapore".equals(location) && "Helius".equals(payroll)) {
				empIdStartRange =1;
				empIdEndRange = 6000;
			}	
			else if("Singapore".equals(location) && !"Helius".equals(payroll)) {
				empIdStartRange =10001;
				empIdEndRange = 12000;
			}	
			else if("India".equals(location) && "Helius_India".equals(payroll)) {
				empIdStartRange = 7001;
				empIdEndRange = 9000;
			}
			else if("India".equals(location) && !"Helius_India".equals(payroll)) {
				empIdStartRange = 12001;
				empIdEndRange = 13000;
			}
			else if("Malaysia".equals(location)) {
				empIdStartRange = 6001;
				empIdEndRange = 7000;
			}
			else if ("Thailand".equals(location)) {
				empIdStartRange = 9001;
				empIdEndRange = 10000;
			}
			else {
				return  "Employee ID generation is not possible for " + location;
			}
		} else {
			return "unable to generate Employee ID as worklocation is not available in offer";
		}
		ArrayList<Integer> al = new ArrayList<Integer>();
		String empids = null;
			SQLQuery querydropdown = session
					.createSQLQuery("SELECT employee_id from Employee_Personal_Details where employee_id BETWEEN :empIdStartRange AND :empIdEndRange ");
			querydropdown.setParameter("empIdStartRange", empIdStartRange).setParameter("empIdEndRange", empIdEndRange);
			List<String> empidList = querydropdown.list();		
			for(String s1 :empidList ){
				int i1 = Integer.parseInt(s1);
				al.add(i1);
			}
			Collections.sort(al);
			if(!al.isEmpty()){
			empids = Integer.toString(al.get(al.size()-1));
			}
		if (empids == null) {
			empids = Integer.toString(empIdStartRange-1);
		}
		int emplyId = Integer.parseInt(empids);
		if (emplyId <= empIdEndRange) {
			employee_id = Integer.toString(emplyId+1);
		} else {
			return "series limit is reached";
		}
		} catch (Exception e) {
			e.printStackTrace();
			return "Unable to generate employeeId";
		}finally{
			session.close();
		}
		return employee_id;
	}
	
	@Override
	public String getAllEmployees() {
		Session session = null;
		java.util.List personalList = null;
		java.util.List offerList = null;
		java.util.List termsAndCondList = null;
		java.util.List assignmentList = null;
		java.util.List appraisalList = null;
		java.util.List workpermitist = null;
		java.util.List empSowList = null;
		java.util.List salaryList= null;
		JSONObject js = new JSONObject();
		ArrayList<Object> employeeDetailList = new ArrayList<Object>();
		try {
			session = sessionFactory.openSession();
			String personalQuery = "select * from Employee_Personal_Details where employee_status ='Active'";
			personalList = session.createSQLQuery(personalQuery).addEntity(Employee_Personal_Details.class).list();
			String offerQuery = "select * from Employee_Offer_Details";
			offerList = session.createSQLQuery(offerQuery).addEntity(Employee_Offer_Details.class).list();
			String termsCondQuery = "select * from Employee_Terms_And_Conditions";
			termsAndCondList = session.createSQLQuery(termsCondQuery).addEntity(Employee_Terms_And_Conditions.class).list();
			String assignmentQuery = "select * from Employee_Assignment_Details";
			assignmentList = session.createSQLQuery(assignmentQuery).addEntity(Employee_Assignment_Details.class).list();
			String appraisalQuery = "select * from Employee_Appraisal_Details";
			appraisalList = session.createSQLQuery(appraisalQuery).addEntity(Employee_Appraisal_Details.class).list();
			String workpermitQuery = "select * from Employee_Work_Permit_Details";
			workpermitist = session.createSQLQuery(workpermitQuery).addEntity(Employee_Work_Permit_Details.class).list();
			String sowEmpAssoc_query = "select b.employee_id,a.* from Sow_Details a LEFT JOIN Sow_Employee_Association b ON a.helius_reference_number=b.helius_reference_number where a.sow_status = 'active'"; 
			empSowList = session.createSQLQuery(sowEmpAssoc_query).setResultTransformer(Transformers.aliasToBean(Sowdashboard.class)).list();
			String salaryQuery = "select * from Employee_Salary_Details";
			salaryList = session.createSQLQuery(salaryQuery).addEntity(Employee_Salary_Details.class).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}
		Employee_Personal_Details personalDetails = null;
		Employee_Offer_Details offer = null;
		Employee_Terms_And_Conditions termsAndCond = null;
		Employee_Assignment_Details assignment = null;
		Employee_Appraisal_Details appraisal = null;
		Employee_Work_Permit_Details workpermit = null;
		Sowdashboard sowdashboard = null;
		HashMap<String, Object> offerDetailsMap = new HashMap<String, Object>();
		HashMap<String, Object> termsAndCondMap = new HashMap<String, Object>();
		HashMap<String, Object> assignmentMap = new HashMap<String, Object>();
		HashMap<String, Object> appraisalMap = new HashMap<String, Object>();
		HashMap<String, Object> workpermitMap = new HashMap<String, Object>();
		HashMap<String, Object> sowMap = new HashMap<String, Object>();
		HashMap<String, Object> salaryDetailsMap = new HashMap<String, Object>();
		for (Object sowList : empSowList) {
			sowdashboard = (Sowdashboard) sowList;
			sowMap.put(sowdashboard.getEmployee_id(), sowdashboard);
		}
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
		Employee_Salary_Details salary = null;		
		for (Object salarydetails : salaryList) {
			salary = (Employee_Salary_Details) salarydetails;
			salaryDetailsMap.put(salary.getEmployee_id(), salary);
		}
		
		for (Object emp : personalList) {
			String name = "";
			String nationality = "";
			String heliusrecruiter = "";
			String client = "";
			Date actualdateofjoining = null;
			String group = "";
			//String sowassigned = "";
			String ctc_month = "";
			String createdby = "";
			String work_country = "";
			String accountmanager = "";
			String visaStatus = "";
			String hiringmanager = "";
			String commitment_period = "";
			String pes_checks = "";
			String skills = "";
			String on_notice_period = "";
			String helius_reference_number = "";
			String sow_client_reference_number = "";
			String sow_path = "";
			String sow_details_id = "";
			Date expectedJoining = null;
			Timestamp sowExpiryDate = null;
			String renewal_sow_confirmed = "";
			String personal_email_id = "";
			Date workpassExpiryDate = null;
			Timestamp lastModifiedDate = null;
			Timestamp appraisalDueOn = null;
			Timestamp relievingDate = null;
			Timestamp resignationDate = null;
			Timestamp contractualWorkingDate = null;
			personalDetails = (Employee_Personal_Details) emp;
			String empid = personalDetails.getEmployee_id();
			HashMap<String, Object> map = new HashMap<String, Object>();
			String payroll = "";
			String gender="";
			if (personalDetails.getEmployee_name() != null) {
				name = personalDetails.getEmployee_name();
			}
			if (personalDetails.getSkills() != null) {
				skills = personalDetails.getSkills();
			}
			if (personalDetails.getRelieving_date() != null) {
				relievingDate = personalDetails.getRelieving_date();
			}if (personalDetails.getResignation_date() != null) {
				resignationDate = personalDetails.getResignation_date();
			}
			if (personalDetails.getContractual_working_date() != null) {
				contractualWorkingDate = personalDetails.getContractual_working_date();
			}
			if (personalDetails.getOn_notice_period() != null) {
				on_notice_period = personalDetails.getOn_notice_period();
			}
			if (personalDetails.getActual_date_of_joining() != null) {
				actualdateofjoining = personalDetails.getActual_date_of_joining();
			}
			if (personalDetails.getCreated_by() != null) {
				createdby = personalDetails.getCreated_by();
			}
			if (personalDetails.getLast_modified_date() != null) {
				lastModifiedDate = personalDetails.getLast_modified_date();
			}
			if (termsAndCondMap.containsKey(empid)) {
				Employee_Terms_And_Conditions employee_Terms_And_Conditions = (Employee_Terms_And_Conditions) termsAndCondMap
						.get(empid);
				if (employee_Terms_And_Conditions.getCommitment_period() != null) {
					commitment_period = employee_Terms_And_Conditions.getCommitment_period();
				}
			}
			if (workpermitMap.containsKey(empid)) {
				Employee_Work_Permit_Details employee_Work_Permit_Details = (Employee_Work_Permit_Details) workpermitMap
			.get(empid);
				if (employee_Work_Permit_Details.getWork_permit_name() != null) {
					visaStatus = employee_Work_Permit_Details.getWork_permit_name();
				}
				if (employee_Work_Permit_Details.getWork_permit_name_expiry_date() != null) {
					workpassExpiryDate = employee_Work_Permit_Details.getWork_permit_name_expiry_date();
				}
			}
			if (offerDetailsMap.containsKey(empid)) {
				Employee_Offer_Details offer_details = (Employee_Offer_Details) offerDetailsMap.get(empid);
				if (offer_details.getExpected_date_of_joining() != null) {
					expectedJoining = offer_details.getExpected_date_of_joining();
				}
				if (offer_details.getNationality() != null) {
					nationality = offer_details.getNationality();
				}
				if (offer_details.getHelius_recruiter() != null) {
					heliusrecruiter = offer_details.getHelius_recruiter();
				}
				if (offer_details.getWork_country() != null) {
					work_country = offer_details.getWork_country();
				}
				if (offer_details.getGender() != null) {
					gender = offer_details.getGender();
				}
				if(offer_details.getPersonal_email_id() != null){
					personal_email_id = offer_details.getPersonal_email_id();
				}
			}
			if (assignmentMap.containsKey(empid)) {
				Employee_Assignment_Details employee_Assignment_Details = (Employee_Assignment_Details) assignmentMap
						.get(empid);
				if (employee_Assignment_Details.getAccount_manager() != null) {
					accountmanager = employee_Assignment_Details.getAccount_manager();
				}
				if (employee_Assignment_Details.getPes_checks() != null) {
					pes_checks = employee_Assignment_Details.getPes_checks();
				}
				/*if (employee_Assignment_Details.getSow_expiry_date() != null) {
					sowExpiryDate = employee_Assignment_Details.getSow_expiry_date();
				}*/
				if (employee_Assignment_Details.getClient_hiring_manager() != null) {
					hiringmanager = employee_Assignment_Details.getClient_hiring_manager();
				}
				if (employee_Assignment_Details.getClient() != null) {
					client = employee_Assignment_Details.getClient();
				}
				if (employee_Assignment_Details.getRenewal_sow_confirmed() != null) {
					renewal_sow_confirmed = employee_Assignment_Details.getRenewal_sow_confirmed();
				}
				if (employee_Assignment_Details.getClient_group() != null) {
					group = employee_Assignment_Details.getClient_group();
				}
				/*if (employee_Assignment_Details.getAssign_employee_to_sow() != null) {
					sowassigned = employee_Assignment_Details.getAssign_employee_to_sow();
				}*/
			}
			if (appraisalMap.containsKey(empid)) {
				Employee_Appraisal_Details employee_Appraisal_Details = (Employee_Appraisal_Details) appraisalMap
						.get(empid);
				if (employee_Appraisal_Details.getAppraisal_due_on() != null) {
					appraisalDueOn = employee_Appraisal_Details.getAppraisal_due_on();
				}
				if (employee_Appraisal_Details.getNew_monthly_basic() != null) {
					ctc_month = employee_Appraisal_Details.getNew_monthly_basic();
				}
			}
			if (sowMap.containsKey(empid)) {
				Sowdashboard sowdashboards = (Sowdashboard) sowMap
						.get(empid);
				if (sowdashboards.getHelius_reference_number() != null) {
					helius_reference_number = sowdashboards.getHelius_reference_number();
				}
				if (sowdashboards.getSow_client_reference_number() != null) {
					sow_client_reference_number = sowdashboards.getSow_client_reference_number();
				}
				if (sowdashboards.getSow_path() != null) {
					sow_path = sowdashboards.getSow_path();
				}
				if (sowdashboards.getSow_details_id() != 0) {
					sow_details_id = Integer.toString(sowdashboards.getSow_details_id());
				}
				if (sowdashboards.getSow_expiry_date() != null) {
					sowExpiryDate = sowdashboards.getSow_expiry_date();
				}
			}
			if (salaryDetailsMap.containsKey(empid)) {
				Employee_Salary_Details employee_Salary_Details = (Employee_Salary_Details) salaryDetailsMap.get(empid);
				if (employee_Salary_Details.getPayroll_entity() != null) {
					payroll = employee_Salary_Details.getPayroll_entity();
				}
			}
			map.put("employee_id", empid);
			map.put("employee_name", name);
			map.put("nationality", nationality);
			map.put("helius_recruiter", heliusrecruiter);
			map.put("personal_email_id", personal_email_id);
			map.put("client", client);
			map.put("client_group", group);
			map.put("actual_date_of_joining", actualdateofjoining);
			//map.put("assign_employee_to_sow", sowassigned);
			map.put("work_country", work_country);
			map.put("created_by", createdby);
			map.put("last_modified_date", lastModifiedDate);
			map.put("account_manager", accountmanager);
			map.put("work_permit_name_expiry_date", workpassExpiryDate);
			map.put("sow_expiry_date", sowExpiryDate);
			map.put("visa_status", visaStatus);
			map.put("appraisal_due_on", appraisalDueOn);
			map.put("client_hiring_manager", hiringmanager);
			map.put("expected_date_of_joining", expectedJoining);
			map.put("commitment_period", commitment_period);
			map.put("pes_checks", pes_checks);
			map.put("skills", skills);
			map.put("on_notice_period", on_notice_period);
			map.put("renewal_sow_confirmed", renewal_sow_confirmed);
			map.put("ctc_month", ctc_month);
			map.put("relieving_date", relievingDate);
			map.put("resignation_reason", resignationDate);
			map.put("contractual_working_date", contractualWorkingDate);
			map.put("helius_reference_number", helius_reference_number);
			map.put("sow_client_reference_number", sow_client_reference_number);
			map.put("sow_path", sow_path);
			map.put("sow_details_id", sow_details_id);
			map.put("payroll", payroll);
			map.put("gender", gender);
			employeeDetailList.add(map);
		}
		String employeeListJson = null;
		String empdetailsjson = "";
		String empjson = "";
		if (employeeDetailList != null) {
			ObjectMapper om = new ObjectMapper();
			try {
				empjson = om.writeValueAsString(employeeDetailList);
				empdetailsjson = empjson.replaceAll(":null", ":\"-\"").replaceAll(":\"\"", ":\"-\"");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			employeeListJson = "{ \"empdetailsjson\":" + empdetailsjson + "}";
		} else {
			employeeListJson = "Details not Available";
		}
		offerDetailsMap.clear();
		termsAndCondMap.clear(); 
		assignmentMap.clear();
		appraisalMap.clear();
		workpermitMap.clear();
		
		return employeeListJson;
	}

	@Override
	public List<Object> getAllNewEmployee(String dateSelected) throws Throwable {
		Session session = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		java.util.Date selectedDate = sdf.parse(dateSelected);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedDate);
		int month = calendar.get(Calendar.MONTH)+1;
		int year = calendar.get(Calendar.YEAR);
		java.util.List personalList = null;
		java.util.List offerList = null;
		java.util.List termsAndCondList = null;
		java.util.List assignmentList = null;
		java.util.List appraisalList = null;
		java.util.List workpermitist = null;
		java.util.List salaryList= null;
		java.util.List timesheetEmailList= null;
		ArrayList<Object> employeeDetailList = new ArrayList<Object>();
		try {
			session = sessionFactory.openSession();
			String personalQuery = "SELECT * FROM Employee_Personal_Details WHERE MONTH(actual_date_of_joining)= :month && YEAR(actual_date_of_joining)= :year";
			personalList = session.createSQLQuery(personalQuery).addEntity(Employee_Personal_Details.class).setParameter("month",month).setParameter("year",year).list();
			String offerQuery = "select * from Employee_Offer_Details";
			offerList = session.createSQLQuery(offerQuery).addEntity(Employee_Offer_Details.class).list();
			String termsCondQuery = "select * from Employee_Terms_And_Conditions";
			termsAndCondList = session.createSQLQuery(termsCondQuery).addEntity(Employee_Terms_And_Conditions.class).list();
			String assignmentQuery = "select * from Employee_Assignment_Details";
			assignmentList = session.createSQLQuery(assignmentQuery).addEntity(Employee_Assignment_Details.class).list();
			String appraisalQuery = "select * from Employee_Appraisal_Details";
			appraisalList = session.createSQLQuery(appraisalQuery).addEntity(Employee_Appraisal_Details.class).list();
			String workpermitQuery = "select * from Employee_Work_Permit_Details";
			workpermitist = session.createSQLQuery(workpermitQuery).addEntity(Employee_Work_Permit_Details.class).list();
			String salaryQuery = "select * from Employee_Salary_Details";
			salaryList = session.createSQLQuery(salaryQuery).addEntity(Employee_Salary_Details.class).list();
			String timesheetEmailQuery = "select * from Timesheet_Email";
			timesheetEmailList = session.createSQLQuery(timesheetEmailQuery).addEntity(Timesheet_Email.class).list();
		Employee_Personal_Details personalDetails = null;
		Employee_Offer_Details offer = null;
		Employee_Terms_And_Conditions termsAndCond = null;
		Employee_Assignment_Details assignment = null;
		Employee_Appraisal_Details appraisal = null;
		Employee_Work_Permit_Details workpermit = null;
		Timesheet_Email timesheetEmail = null;
		Employee_Salary_Details salary = null;
		HashMap<String, Object> offerDetailsMap = new HashMap<String, Object>();
		HashMap<String, Object> termsAndCondMap = new HashMap<String, Object>();
		HashMap<String, Object> assignmentMap = new HashMap<String, Object>();
		HashMap<String, Object> appraisalMap = new HashMap<String, Object>();
		HashMap<String, Object> workpermitMap = new HashMap<String, Object>();
		HashMap<String, Object> salaryDetailsMap = new HashMap<String, Object>();
		HashMap<String, Object> timesheetEmailMap = new HashMap<String, Object>();
		for (Object timesheetEmaildetails : timesheetEmailList) {
			timesheetEmail = (Timesheet_Email) timesheetEmaildetails;
			timesheetEmailMap.put(timesheetEmail.getEmployeeId(), timesheetEmail);
		}
		for (Object salarydetails : salaryList) {
			salary = (Employee_Salary_Details) salarydetails;
			salaryDetailsMap.put(salary.getEmployee_id(), salary);
		}
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
		for (Object emp : personalList) {
			String name = "";
			String nationality = "";
			String heliusrecruiter = "";
			String client = "";
			Timestamp actualdateofjoining = null;
			String payroll = "";
			String group = "";
			String sowassigned = "";
			String ctc_month = "";
			String createdby = "";
			String work_country = "";
			String accountmanager = "";
			String visaStatus = "";
			String hiringmanager = "";
			String commitment_period = "";
			String pes_checks = "";
			String skills = "";
			String on_notice_period = "";
			String timesheetEmailId = "";
			Timestamp expectedJoining = null;
			Timestamp sowExpiryDate = null;
			String renewal_sow_confirmed = "";
			Timestamp workpassExpiryDate = null;
			Timestamp lastModifiedDate = null;
			Timestamp appraisalDueOn = null;
			Timestamp relievingDate = null;
			Timestamp resignationDate = null;
			Timestamp contractualWorkingDate = null;			
			personalDetails = (Employee_Personal_Details) emp;
			String empid = personalDetails.getEmployee_id();
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (personalDetails.getActual_date_of_joining() != null) {
				actualdateofjoining = personalDetails.getActual_date_of_joining();
			}
			if (personalDetails.getEmployee_name() != null) {
				name = personalDetails.getEmployee_name();
			}
			if (personalDetails.getSkills() != null) {
				skills = personalDetails.getSkills();
			}
			if (personalDetails.getRelieving_date() != null) {
				relievingDate = personalDetails.getRelieving_date();
			}if (personalDetails.getResignation_date() != null) {
				resignationDate = personalDetails.getResignation_date();
			}
			if (personalDetails.getContractual_working_date() != null) {
				contractualWorkingDate = personalDetails.getContractual_working_date();
			}
			if (personalDetails.getOn_notice_period() != null) {
				on_notice_period = personalDetails.getOn_notice_period();
			}
			
			if (personalDetails.getCreated_by() != null) {
				createdby = personalDetails.getCreated_by();
			}
			if (personalDetails.getLast_modified_date() != null) {
				lastModifiedDate = personalDetails.getLast_modified_date();
			}
			if (termsAndCondMap.containsKey(empid)) {
				Employee_Terms_And_Conditions employee_Terms_And_Conditions = (Employee_Terms_And_Conditions) termsAndCondMap
						.get(empid);
				if (employee_Terms_And_Conditions.getCommitment_period() != null) {
					commitment_period = employee_Terms_And_Conditions.getCommitment_period();
				}
			}
			if (workpermitMap.containsKey(empid)) {
				Employee_Work_Permit_Details employee_Work_Permit_Details = (Employee_Work_Permit_Details) workpermitMap
			.get(empid);
				if (employee_Work_Permit_Details.getWork_permit_name() != null) {
					visaStatus = employee_Work_Permit_Details.getWork_permit_name();
				}
				if (employee_Work_Permit_Details.getWork_permit_name_expiry_date() != null) {
					workpassExpiryDate = employee_Work_Permit_Details.getWork_permit_name_expiry_date();
				}
			}
			if (offerDetailsMap.containsKey(empid)) {
				Employee_Offer_Details offer_details = (Employee_Offer_Details) offerDetailsMap.get(empid);
				if (offer_details.getExpected_date_of_joining() != null) {
					expectedJoining = offer_details.getExpected_date_of_joining();
				}
				if (offer_details.getNationality() != null) {
					nationality = offer_details.getNationality();
				}
				if (offer_details.getHelius_recruiter() != null) {
					heliusrecruiter = offer_details.getHelius_recruiter();
				}
				if (offer_details.getWork_country() != null) {
					work_country = offer_details.getWork_country();
				}
			}
			if (assignmentMap.containsKey(empid)) {
				Employee_Assignment_Details employee_Assignment_Details = (Employee_Assignment_Details) assignmentMap
						.get(empid);
				if (employee_Assignment_Details.getAccount_manager() != null) {
					accountmanager = employee_Assignment_Details.getAccount_manager();
				}
				if (employee_Assignment_Details.getPes_checks() != null) {
					pes_checks = employee_Assignment_Details.getPes_checks();
				}
				if (employee_Assignment_Details.getSow_expiry_date() != null) {
					sowExpiryDate = employee_Assignment_Details.getSow_expiry_date();
				}
				if (employee_Assignment_Details.getClient_hiring_manager() != null) {
					hiringmanager = employee_Assignment_Details.getClient_hiring_manager();
				}
				if (employee_Assignment_Details.getClient() != null) {
					client = employee_Assignment_Details.getClient();
				}
				if (employee_Assignment_Details.getRenewal_sow_confirmed() != null) {
					renewal_sow_confirmed = employee_Assignment_Details.getRenewal_sow_confirmed();
				}
				if (employee_Assignment_Details.getClient_group() != null) {
					group = employee_Assignment_Details.getClient_group();
				}
				if (employee_Assignment_Details.getAssign_employee_to_sow() != null) {
					sowassigned = employee_Assignment_Details.getAssign_employee_to_sow();
				}
			}
			if (appraisalMap.containsKey(empid)) {
				Employee_Appraisal_Details employee_Appraisal_Details = (Employee_Appraisal_Details) appraisalMap
						.get(empid);
				if (employee_Appraisal_Details.getAppraisal_due_on() != null) {
					appraisalDueOn = employee_Appraisal_Details.getAppraisal_due_on();
				}
				if (employee_Appraisal_Details.getNew_monthly_basic() != null) {
					ctc_month = employee_Appraisal_Details.getNew_monthly_basic();
				}
			}
			if (salaryDetailsMap.containsKey(empid)) {
				Employee_Salary_Details employee_Salary_Details = (Employee_Salary_Details) salaryDetailsMap.get(empid);
				if (employee_Salary_Details.getPayroll_entity() != null) {
					payroll = employee_Salary_Details.getPayroll_entity();
				}
			}
			if (timesheetEmailMap.containsKey(empid)) {
				Timesheet_Email employee_Timesheet_Email = (Timesheet_Email) timesheetEmailMap.get(empid);
				if (employee_Timesheet_Email.getTimesheetEmailId() != null) {
					timesheetEmailId = employee_Timesheet_Email.getTimesheetEmailId();
				}
			}
			
			map.put("employee_id", empid);
			map.put("employee_name", name);
			map.put("nationality", nationality);
			map.put("helius_recruiter", heliusrecruiter);
			map.put("client", client);
			map.put("client_group", group);
			map.put("actual_date_of_joining", actualdateofjoining);
			map.put("assign_employee_to_sow", sowassigned);
			map.put("work_country", work_country);
			map.put("created_by", createdby);
			map.put("last_modified_date", lastModifiedDate);
			map.put("account_manager", accountmanager);
			map.put("work_permit_name_expiry_date", workpassExpiryDate);
			map.put("sow_expiry_date", sowExpiryDate);
			map.put("visa_status", visaStatus);
			map.put("appraisal_due_on", appraisalDueOn);
			map.put("client_hiring_manager", hiringmanager);
			map.put("expected_date_of_joining", expectedJoining);
			map.put("commitment_period", commitment_period);
			map.put("pes_checks", pes_checks);
			map.put("skills", skills);
			map.put("on_notice_period", on_notice_period);
			map.put("renewal_sow_confirmed", renewal_sow_confirmed);
			map.put("ctc_month", ctc_month);
			map.put("relieving_date", relievingDate);
			map.put("resignation_date", resignationDate);
			map.put("contractual_working_date", contractualWorkingDate);
			map.put("timesheetEmailId", timesheetEmailId);
			map.put("payroll", payroll);
			map.put("timesheetUploadPath","");
			employeeDetailList.add(map);
		}
		offerDetailsMap.clear();
		termsAndCondMap.clear(); 
		assignmentMap.clear();
		appraisalMap.clear();
		workpermitMap.clear();
		}catch (Exception e) {
				e.printStackTrace();
				throw new Throwable("Failed to fetch New Joinee Details");
			} finally {
				session.close();
			}
		return employeeDetailList;
	}
	
	@Override
	public List<Object> getNewJoineeDashboard(String dateSelected,String fromdate,String thrudate) throws Throwable {
		Session session = null;
		Query emplist = null;
		List<Object> results = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");		
		//java.util.List offerList = null;
		//java.util.List salaryList = null;
		try {
			session = sessionFactory.openSession();
			if (!"".equalsIgnoreCase(dateSelected)) {
				java.util.Date selectedDate = sdf.parse(dateSelected);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(selectedDate);
				int month = calendar.get(Calendar.MONTH) + 1;
				int year = calendar.get(Calendar.YEAR);
				String newjoinee_query = "SELECT a.employee_id,a.employee_name,a.date_of_birth,a.actual_date_of_joining,a.designation,a.marital_status,b.work_permit_number,b.work_permit_name,b.work_permit_name_issued_date,c.personal_email_id,c.work_country,c.nationality,c.account_manager,c.helius_recruiter,c.gender,c.client,d.helius_email_id,f.ctc_per_month,f.payroll_entity FROM Employee_Personal_Details a LEFT JOIN Employee_Work_Permit_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Offer_Details c ON a.employee_id=c.employee_id LEFT JOIN Employee_Assignment_Details d ON a.employee_id=d.employee_id LEFT JOIN Employee_Salary_Details f ON a.employee_id=f.employee_id where a.employee_status ='Active' AND MONTH(a.actual_date_of_joining)= :month AND YEAR(a.actual_date_of_joining)= :year";
				emplist = session.createSQLQuery(newjoinee_query)
						.setResultTransformer(Transformers.aliasToBean(NewJoineeDashboard.class))
						.setParameter("month", month).setParameter("year", year);
				results = emplist.list();
/*				String offerQuery = "select * from Employee_Offer_Details where offer_status = 'rolled_out' AND MONTH(expected_date_of_joining)= :month AND YEAR(expected_date_of_joining)= :year";
				offerList = session.createSQLQuery(offerQuery).addEntity(Employee_Offer_Details.class)
						.setParameter("month", month).setParameter("year", year).list();*/
			} else {
				String newjoinee_query = "SELECT a.employee_id,a.employee_name,a.date_of_birth,a.actual_date_of_joining,a.designation,a.marital_status,b.work_permit_number,b.work_permit_name,b.work_permit_name_issued_date,c.personal_email_id,c.work_country,c.nationality,c.account_manager,c.helius_recruiter,c.gender,c.client,d.helius_email_id,f.ctc_per_month,f.payroll_entity FROM Employee_Personal_Details a LEFT JOIN Employee_Work_Permit_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Offer_Details c ON a.employee_id=c.employee_id LEFT JOIN Employee_Assignment_Details d ON a.employee_id=d.employee_id LEFT JOIN Employee_Salary_Details f ON a.employee_id=f.employee_id WHERE a.employee_status ='Active' AND DATE(a.actual_date_of_joining) BETWEEN str_to_date(:fromdate,'%Y-%m-%d') AND :thrudate";
				emplist = session.createSQLQuery(newjoinee_query)
						.setResultTransformer(Transformers.aliasToBean(NewJoineeDashboard.class))
						.setParameter("fromdate", fromdate).setParameter("thrudate", thrudate);
				results = emplist.list();
				/*String offerQuery = "select * from Employee_Offer_Details where offer_status = 'rolled_out' AND DATE(expected_date_of_joining) BETWEEN str_to_date(:fromdate,'%Y-%m-%d') AND :thrudate";
				offerList = session.createSQLQuery(offerQuery).addEntity(Employee_Offer_Details.class)
						.setParameter("fromdate", fromdate).setParameter("thrudate", thrudate).list();*/
			}
			/*String salaryQuery = "select * from Employee_Salary_Details";
			salaryList = session.createSQLQuery(salaryQuery).addEntity(Employee_Salary_Details.class).list();
			HashMap<Integer, Object> salaryDetailsMap = new HashMap<Integer, Object>();
			Employee_Salary_Details salarydet = null;
			for (Object salarydetails : salaryList) {
				salarydet = (Employee_Salary_Details) salarydetails;
				salaryDetailsMap.put(salarydet.getOffer_id(), salarydet);
			}
			Employee_Offer_Details Offer_Details = null;
			for (Object offer : offerList) {
				 Offer_Details = (Employee_Offer_Details) offer;
				int offerId = Offer_Details.getOffer_id();
				NewJoineeDashboard newJoineeDashboard = new NewJoineeDashboard();
				newJoineeDashboard.setEmployee_name(Offer_Details.getEmployee_name());
				newJoineeDashboard.setGender(Offer_Details.getGender());
				newJoineeDashboard.setClient(Offer_Details.getClient());
				newJoineeDashboard.setNationality(Offer_Details.getNationality());
				newJoineeDashboard.setWork_country(Offer_Details.getWork_country());
				newJoineeDashboard.setWork_permit_name(Offer_Details.getVisa_status());
				newJoineeDashboard.setAccount_manager(Offer_Details.getAccount_manager());
				newJoineeDashboard.setHelius_recruiter(Offer_Details.getHelius_recruiter());
				newJoineeDashboard.setDesignation(Offer_Details.getDesignation());
				newJoineeDashboard.setPersonal_email_id(Offer_Details.getPersonal_email_id());
				if(Offer_Details.getNationality().equalsIgnoreCase("Singapore")){
					if(Offer_Details.getNational_id() != null){
				newJoineeDashboard.setWork_permit_number(Offer_Details.getNational_id());
					}
				}
				newJoineeDashboard.setActual_date_of_joining(Offer_Details.getExpected_date_of_joining());
				if (salaryDetailsMap.containsKey(offerId)) {
					Employee_Salary_Details employee_Salary_Details = (Employee_Salary_Details) salaryDetailsMap.get(offerId);
					if (employee_Salary_Details.getPayroll_entity() != null) {
						newJoineeDashboard.setPayroll_entity(employee_Salary_Details.getPayroll_entity());
					}
					if (employee_Salary_Details.getCtc_per_month() != null) {
						newJoineeDashboard.setCtc_per_month(employee_Salary_Details.getCtc_per_month());
					}
				}
				results.add(newJoineeDashboard);
			}*/
		} catch (Exception e) {
				e.printStackTrace();
				throw new Throwable("Failed to fetch  New Joinee Employee Details " );
			}finally {
			session.close();
		}
		return results;
	}
	
	
	/**
	 * returns all exit employee whose employee status is exit and active with resign status as yes.
	 * whenever any field is added in this method query then same needs to be added in getAllResignEmployee method
	 * 
	 * **/
	@Override
	public List<Object> getAllExitEmployee() throws Throwable {
		Session session = null;
		Query emplist = null;
		List<Object> results = null;
		try {
			session = sessionFactory.openSession();
			String expEmp_query = "SELECT a.employee_id,a.employee_name,a.employee_status,a.actual_date_of_joining,a.skills,a.resignation_date,a.relieving_date,"
					+ "a.contractual_working_date,a.created_by,a.last_modified_date,b.work_permit_number,b.work_permit_name,"
					+ "b.work_permit_name_expiry_date,b.work_permit_name_issued_date,c.nationality,c.visa_status,c.work_country,"
					+ "c.helius_recruiter,d.account_manager,d.client,d.client_hiring_manager,d.client_group,e.new_monthly_basic,f.payroll_entity FROM Employee_Personal_Details a LEFT JOIN Employee_Work_Permit_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Offer_Details c ON a.employee_id=c.employee_id LEFT JOIN Employee_Assignment_Details d ON a.employee_id=d.employee_id LEFT JOIN Employee_Appraisal_Details e ON a.employee_id=e.employee_id LEFT JOIN Employee_Salary_Details f ON a.employee_id=f.employee_id"
					+ " where (employee_status = 'Exited') OR (employee_status = 'Active' AND on_notice_period = 'YES') ";
			emplist = session.createSQLQuery(expEmp_query).setResultTransformer(Transformers.aliasToBean(ExitEmployeeDashboard.class));
			results = emplist.list();
		} catch (Exception e) {
				e.printStackTrace();
				throw new Throwable("Failed to fetch  Exit Employee Details " );
			}finally {
			session.close();
		}
		return results;
	}
	
	
	@Override
	public List<Object> getAllResignEmployee(String dateSelected,String fromdate,String thrudate) throws Throwable {
		Session session = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		java.util.List personalList = null;
		java.util.List offerList = null;
		java.util.List termsAndCondList = null;
		java.util.List assignmentList = null;
		java.util.List appraisalList = null;
		java.util.List workpermitist = null;
		java.util.List salaryList= null;
		java.util.List timesheetEmailList= null;
		ArrayList<Object> employeeDetailList = new ArrayList<Object>();
		try {
			session = sessionFactory.openSession();
			if(!"".equalsIgnoreCase(dateSelected)){
				java.util.Date selectedDate = sdf.parse(dateSelected);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(selectedDate);
				int month = calendar.get(Calendar.MONTH)+1;
				int year = calendar.get(Calendar.YEAR);
			String personalQuery = "SELECT * FROM Employee_Personal_Details WHERE (employee_status ='Exited' AND MONTH(relieving_date)= :month AND YEAR(relieving_date)= :year) OR (employee_status ='Active' AND on_notice_period = 'YES' AND MONTH(relieving_date)= :month AND YEAR(relieving_date)= :year)";
			personalList = session.createSQLQuery(personalQuery).addEntity(Employee_Personal_Details.class).setParameter("month",month).setParameter("year",year).list();
			}else{
				String personalQuery = "SELECT * FROM Employee_Personal_Details WHERE (employee_status ='Exited' AND DATE(relieving_date) BETWEEN str_to_date(:fromdate,'%Y-%m-%d') AND :thrudate) OR (employee_status ='Active' AND on_notice_period = 'YES' AND DATE(relieving_date) BETWEEN str_to_date(:fromdate,'%Y-%m-%d') AND :thrudate)";
				personalList = session.createSQLQuery(personalQuery).addEntity(Employee_Personal_Details.class).setParameter("fromdate",fromdate).setParameter("thrudate",thrudate).list();	
			}
			String offerQuery = "select * from Employee_Offer_Details";
			offerList = session.createSQLQuery(offerQuery).addEntity(Employee_Offer_Details.class).list();
			String termsCondQuery = "select * from Employee_Terms_And_Conditions";
			termsAndCondList = session.createSQLQuery(termsCondQuery).addEntity(Employee_Terms_And_Conditions.class).list();
			String assignmentQuery = "select * from Employee_Assignment_Details";
			assignmentList = session.createSQLQuery(assignmentQuery).addEntity(Employee_Assignment_Details.class).list();
			String appraisalQuery = "select * from Employee_Appraisal_Details";
			appraisalList = session.createSQLQuery(appraisalQuery).addEntity(Employee_Appraisal_Details.class).list();
			String workpermitQuery = "select * from Employee_Work_Permit_Details";
			workpermitist = session.createSQLQuery(workpermitQuery).addEntity(Employee_Work_Permit_Details.class).list();
			String salaryQuery = "select * from Employee_Salary_Details";
			salaryList = session.createSQLQuery(salaryQuery).addEntity(Employee_Salary_Details.class).list();
			String timesheetEmailQuery = "select * from Timesheet_Email";
			timesheetEmailList = session.createSQLQuery(timesheetEmailQuery).addEntity(Timesheet_Email.class).list();
		Employee_Personal_Details personalDetails = null;
		Employee_Offer_Details offer = null;
		Employee_Terms_And_Conditions termsAndCond = null;
		Employee_Assignment_Details assignment = null;
		Employee_Appraisal_Details appraisal = null;
		Employee_Work_Permit_Details workpermit = null;
		Timesheet_Email timesheetEmail = null;
		Employee_Salary_Details salary = null;
		HashMap<String, Object> offerDetailsMap = new HashMap<String, Object>();
		HashMap<String, Object> termsAndCondMap = new HashMap<String, Object>();
		HashMap<String, Object> assignmentMap = new HashMap<String, Object>();
		HashMap<String, Object> appraisalMap = new HashMap<String, Object>();
		HashMap<String, Object> workpermitMap = new HashMap<String, Object>();
		HashMap<String, Object> salaryDetailsMap = new HashMap<String, Object>();
		HashMap<String, Object> timesheetEmailMap = new HashMap<String, Object>();
		for (Object timesheetEmaildetails : timesheetEmailList) {
			timesheetEmail = (Timesheet_Email) timesheetEmaildetails;
			timesheetEmailMap.put(timesheetEmail.getEmployeeId(), timesheetEmail);
		}
		for (Object salarydetails : salaryList) {
			salary = (Employee_Salary_Details) salarydetails;
			salaryDetailsMap.put(salary.getEmployee_id(), salary);
		}
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
		for (Object emp : personalList) {
			String name = "";
			String employee_status = "";
			String nationality = "";
			String heliusrecruiter = "";
			String client = "";
			Timestamp actualdateofjoining = null;
			String payroll = "";
			String group = "";
			String sowassigned = "";
			String ctc_month = "";
			String createdby = "";
			String work_country = "";
			String accountmanager = "";
			String visaStatus = "";
			String workpermitNumber = null;
			String hiringmanager = "";
			String commitment_period = "";
			String pes_checks = "";
			String skills = "";
			String on_notice_period = "";
			String timesheetEmailId = "";
			String nric = "";
			Timestamp workpermitIssueDate = null;
			Timestamp expectedJoining = null;
			Timestamp sowExpiryDate = null;
			String renewal_sow_confirmed = "";
			Timestamp workpassExpiryDate = null;
			Timestamp lastModifiedDate = null;
			Timestamp appraisalDueOn = null;
			Timestamp relievingDate = null;
			Timestamp resignationDate = null;
			Timestamp contractualWorkingDate = null;
			String gender = "";
			personalDetails = (Employee_Personal_Details) emp;
			String empid = personalDetails.getEmployee_id();
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (personalDetails.getActual_date_of_joining() != null) {
				actualdateofjoining = personalDetails.getActual_date_of_joining();
			}
			if (personalDetails.getEmployee_name() != null) {
				name = personalDetails.getEmployee_name();
			}
			if (personalDetails.getEmployee_status() != null) {
				employee_status = personalDetails.getEmployee_status();
			}
			if (personalDetails.getSkills() != null) {
				skills = personalDetails.getSkills();
			}
			if (personalDetails.getRelieving_date() != null) {
				relievingDate = personalDetails.getRelieving_date();
			}if (personalDetails.getResignation_date() != null) {
				resignationDate = personalDetails.getResignation_date();
			}
			if (personalDetails.getContractual_working_date() != null) {
				contractualWorkingDate = personalDetails.getContractual_working_date();
			}
			if (personalDetails.getOn_notice_period() != null) {
				on_notice_period = personalDetails.getOn_notice_period();
			}
			
			if (personalDetails.getCreated_by() != null) {
				createdby = personalDetails.getCreated_by();
			}
			if (personalDetails.getLast_modified_date() != null) {
				lastModifiedDate = personalDetails.getLast_modified_date();
			}
			if (termsAndCondMap.containsKey(empid)) {
				Employee_Terms_And_Conditions employee_Terms_And_Conditions = (Employee_Terms_And_Conditions) termsAndCondMap
						.get(empid);
				if (employee_Terms_And_Conditions.getCommitment_period() != null) {
					commitment_period = employee_Terms_And_Conditions.getCommitment_period();
				}
			}
			if (workpermitMap.containsKey(empid)) {
				Employee_Work_Permit_Details employee_Work_Permit_Details = (Employee_Work_Permit_Details) workpermitMap
			.get(empid);
				if (employee_Work_Permit_Details.getWork_permit_name() != null) {
					visaStatus = employee_Work_Permit_Details.getWork_permit_name();
				}
				if (employee_Work_Permit_Details.getWork_permit_name_expiry_date() != null) {
					workpassExpiryDate = employee_Work_Permit_Details.getWork_permit_name_expiry_date();
				}
				if(employee_Work_Permit_Details.getWork_permit_name_issued_date() != null){
					workpermitIssueDate = employee_Work_Permit_Details.getWork_permit_name_issued_date();
				}
				if(employee_Work_Permit_Details.getWork_permit_number() != null){
					String numWP = employee_Work_Permit_Details.getWork_permit_number().split(",")[0];
					workpermitNumber = numWP;
				}
			}
			if (offerDetailsMap.containsKey(empid)) {
				Employee_Offer_Details offer_details = (Employee_Offer_Details) offerDetailsMap.get(empid);
				if (offer_details.getExpected_date_of_joining() != null) {
					expectedJoining = offer_details.getExpected_date_of_joining();
				}
				if (offer_details.getNationality() != null) {
					nationality = offer_details.getNationality();
				}
				if (offer_details.getHelius_recruiter() != null) {
					heliusrecruiter = offer_details.getHelius_recruiter();
				}
				if (offer_details.getWork_country() != null) {
					work_country = offer_details.getWork_country();
				}
				if (offer_details.getVisa_status() != null) {
					nric = offer_details.getVisa_status();
				}
				if (offer_details.getGender() != null) {
					gender = offer_details.getGender();
				}
			}
			if (assignmentMap.containsKey(empid)) {
				Employee_Assignment_Details employee_Assignment_Details = (Employee_Assignment_Details) assignmentMap
						.get(empid);
				if (employee_Assignment_Details.getAccount_manager() != null) {
					accountmanager = employee_Assignment_Details.getAccount_manager();
				}
				if (employee_Assignment_Details.getPes_checks() != null) {
					pes_checks = employee_Assignment_Details.getPes_checks();
				}
				if (employee_Assignment_Details.getSow_expiry_date() != null) {
					sowExpiryDate = employee_Assignment_Details.getSow_expiry_date();
				}
				if (employee_Assignment_Details.getClient_hiring_manager() != null) {
					hiringmanager = employee_Assignment_Details.getClient_hiring_manager();
				}
				if (employee_Assignment_Details.getClient() != null) {
					client = employee_Assignment_Details.getClient();
				}
				if (employee_Assignment_Details.getRenewal_sow_confirmed() != null) {
					renewal_sow_confirmed = employee_Assignment_Details.getRenewal_sow_confirmed();
				}
				if (employee_Assignment_Details.getClient_group() != null) {
					group = employee_Assignment_Details.getClient_group();
				}
				if (employee_Assignment_Details.getAssign_employee_to_sow() != null) {
					sowassigned = employee_Assignment_Details.getAssign_employee_to_sow();
				}
			}
			if (appraisalMap.containsKey(empid)) {
				Employee_Appraisal_Details employee_Appraisal_Details = (Employee_Appraisal_Details) appraisalMap
						.get(empid);
				if (employee_Appraisal_Details.getAppraisal_due_on() != null) {
					appraisalDueOn = employee_Appraisal_Details.getAppraisal_due_on();
				}
				if (employee_Appraisal_Details.getNew_monthly_basic() != null) {
					ctc_month = employee_Appraisal_Details.getNew_monthly_basic();
				}
			}
			if (salaryDetailsMap.containsKey(empid)) {
				Employee_Salary_Details employee_Salary_Details = (Employee_Salary_Details) salaryDetailsMap.get(empid);
				if (employee_Salary_Details.getPayroll_entity() != null) {
					payroll = employee_Salary_Details.getPayroll_entity();
				}
			}
			if (timesheetEmailMap.containsKey(empid)) {
				Timesheet_Email employee_Timesheet_Email = (Timesheet_Email) timesheetEmailMap.get(empid);
				if (employee_Timesheet_Email.getTimesheetEmailId() != null) {
					timesheetEmailId = employee_Timesheet_Email.getTimesheetEmailId();
				}
			}		
			map.put("employee_id", empid);
			map.put("employee_name", name);
			map.put("gender", gender);
			map.put("employee_status", employee_status);
			map.put("nationality", nationality);
			map.put("helius_recruiter", heliusrecruiter);
			map.put("client", client);
			map.put("client_group", group);
			map.put("actual_date_of_joining", actualdateofjoining);
			map.put("assign_employee_to_sow", sowassigned);
			map.put("work_country", work_country);
			map.put("created_by", createdby);
			map.put("last_modified_date", lastModifiedDate);
			map.put("account_manager", accountmanager);
			map.put("work_permit_name_expiry_date", workpassExpiryDate);
			map.put("sow_expiry_date", sowExpiryDate);
			map.put("visa_status", visaStatus);
			map.put("nric", nric);
			map.put("work_permit_number", workpermitNumber);
			map.put("work_permit_name_issued_date", workpermitIssueDate);
			map.put("appraisal_due_on", appraisalDueOn);
			map.put("client_hiring_manager", hiringmanager);
			map.put("expected_date_of_joining", expectedJoining);
			map.put("commitment_period", commitment_period);
			map.put("pes_checks", pes_checks);
			map.put("skills", skills);
			map.put("on_notice_period", on_notice_period);
			map.put("renewal_sow_confirmed", renewal_sow_confirmed);
			map.put("ctc_month", ctc_month);
			map.put("relieving_date", relievingDate);
			map.put("resignation_date", resignationDate);
			map.put("contractual_working_date", contractualWorkingDate);
			map.put("timesheetEmailId", timesheetEmailId);
			map.put("payroll", payroll);
			map.put("timesheetUploadPath", "");
			employeeDetailList.add(map);
		}
		offerDetailsMap.clear();
		termsAndCondMap.clear(); 
		assignmentMap.clear();
		appraisalMap.clear();
		workpermitMap.clear();
		}catch (Exception e) {
				e.printStackTrace();
				throw new Throwable("Failed to fetch Resigned Employee Details");
			} finally {
				session.close();
			}
		return employeeDetailList;
	}
	@Override
	public String getOpenOffers() throws Throwable {
		Session session = null;
		java.util.List offerlist = null;
		try {
			session = sessionFactory.openSession();
			String offer_query = "select * from Employee_Offer_Details where offer_status = 'rolled_out'";
			offerlist = session.createSQLQuery(offer_query).addEntity(Employee_Offer_Details.class).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			throw new Throwable("unable to get open offers");
		}
		String offerdetailsByStatus = null;
		String offerdetailsjson = "";
		String offerjson = "";
		if (offerlist != null) {
			ObjectMapper om = new ObjectMapper();
			try {
				offerjson = om.writeValueAsString(offerlist);
				offerdetailsjson = offerjson.replaceAll(":null", ":\"-\"").replaceAll(":\"\"", ":\"-\"");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			offerdetailsByStatus = "{ \"offerdetailsByStatus\":" + offerjson + "}";
		} else {
			offerdetailsByStatus = "No Open Offer Letters Availaable";
		}
		return offerdetailsByStatus;
	}
	
	@Override
	public String getAllOffers() {
		Session session = null;
		java.util.List offerlist = null;
		try {
			session = sessionFactory.openSession();
			String offer_query = "select * from Employee_Offer_Details";
			offerlist = session.createSQLQuery(offer_query).addEntity(Employee_Offer_Details.class).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}
		String offerdetailsByStatus = null;
	/*	JSONObject jb = new JSONObject();
		if (offerlist != null) {
			jb.put("offerdetailsByStatus", offerlist);
			jb.replace("null", "-");
		} else {
			jb.put("offerdetailsByStatus", "No Offer Letters Availaable");
		}*/
		String offerdetailsjson = "";
		String offerjson = "";
		if (offerlist != null) {
			ObjectMapper om = new ObjectMapper();
			try {
				// om.setSerializationInclusion(Include.NON_EMPTY);
				// om.setSerializationInclusion(Include.NON_NULL);
				offerjson = om.writeValueAsString(offerlist);
				offerdetailsjson = offerjson.replaceAll(":null", ":\"-\"").replaceAll(":\"\"", ":\"-\"");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			offerdetailsByStatus = "{ \"offerdetailsByStatus\":" + offerdetailsjson + "}";
		} else {
			offerdetailsByStatus = "No Offer Letters Availaable";
		}
		return offerdetailsByStatus;
	}
	
	@Override
	public String getOfferDashboard(String month) {
		Session session = null;
		java.util.List<OffersDashboard> offerlist = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");		
		try {
			session = sessionFactory.openSession();
			Date selectedMonth = sdf.parse(month);
			Timestamp selected = new Timestamp(selectedMonth.getTime());
			int offerMonth = selected.toLocalDateTime().getMonthValue();
			int year = selected.toLocalDateTime().getYear();
			String offer_query = "select a.*,b.payroll_entity,b.ctc_per_month,b.ctc_per_year from Employee_Offer_Details a left join Employee_Salary_Details b on a.offer_id = b.offer_id where offer_status = 'rolled_out' AND MONTH(a.expected_date_of_joining)= :month AND YEAR(a.expected_date_of_joining) = :year";
			offerlist = session.createSQLQuery(offer_query)
					.setResultTransformer(Transformers.aliasToBean(OffersDashboard.class))
					.setParameter("month", offerMonth).setParameter("year", year).list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
		}
		String offerdetailsByStatus = null;
		String offerdetailsjson = "";
		String offerjson = "";
		if (offerlist != null) {
			ObjectMapper om = new ObjectMapper();
			try {
				offerjson = om.writeValueAsString(offerlist);
				offerdetailsjson = offerjson.replaceAll(":null", ":\"-\"").replaceAll(":\"\"", ":\"-\"");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			offerdetailsByStatus = "{ \"offerdetailsByStatus\":" + offerdetailsjson + "}";
		} else {
			offerdetailsByStatus = "No Offer Letters Available";
		}
		return offerdetailsByStatus;
	}

	@Override
	public void saveJobOffer(Employee employee, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		Object offerId = null;
		int offer_id = 0;
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> fileFolder = new HashMap<String, String>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (employee.getEmployeeOfferDetails() != null) {
				if(employee.getEmployeeOfferDetails().getName_in_chinese_characters()!=null ){
				String convertedValue = new String(employee.getEmployeeOfferDetails().getName_in_chinese_characters().getBytes("ISO8859_1"), "UTF8");	
				employee.getEmployeeOfferDetails().setName_in_chinese_characters(convertedValue);	
			}
				if("Singapore".equalsIgnoreCase(employee.getEmployeeOfferDetails().getWork_country()) && "Singapore".equalsIgnoreCase(employee.getEmployeeOfferDetails().getNationality())){
					employee.getEmployeeOfferDetails().setVisa_status("Citizen");
				}
				offerId = session.save(employee.getEmployeeOfferDetails());
				offer_id = (int) offerId;
				if(employee.getEmployeeOfferDetails().getOffer_path()!=null && !employee.getEmployeeOfferDetails().getOffer_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getOffer_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getOffer_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getOffer_path(), "offer");
				}
				if(employee.getEmployeeOfferDetails().getJob_desc_path()!=null && !employee.getEmployeeOfferDetails().getJob_desc_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getJob_desc_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getJob_desc_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getJob_desc_path(), "jobdescription");
				}
				if(employee.getEmployeeOfferDetails().getEp_path()!=null && !employee.getEmployeeOfferDetails().getEp_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getEp_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getEp_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getEp_path(), "ep");
				}
				if(employee.getEmployeeOfferDetails().getBgv_report_path()!=null && !employee.getEmployeeOfferDetails().getBgv_report_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getBgv_report_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getBgv_report_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getBgv_report_path(), "bgv");
				}
			}
			if (employee.getEmployeeSalaryDetails() != null) {
				employee.getEmployeeSalaryDetails().setOffer_id(offer_id);
				session.save(employee.getEmployeeSalaryDetails());
			}
			if (employee.getSowCtcBreakup() != null) {
				Iterator<Sow_Ctc_Breakup> itr = employee.getSowCtcBreakup().iterator();
				while (itr.hasNext()) {
					Sow_Ctc_Breakup sowCtcBreakup = itr.next();
					sowCtcBreakup.setOfferId(offer_id);
					sowCtcBreakup.setGroupId(1);
					sowCtcBreakup.setStatus("active");
					session.save(sowCtcBreakup);
				}
			}
			
			if (offerId != null) {
				Map<String, MultipartFile> files = null;
				files = request.getFileMap();
				if (files.size() > 0) {			
				FilecopyStatus status = Utils.copyFiles(request, templateFilenames, fileFolder);
				copied_with_success = status.getCopied_with_success();
				}
			}
			transaction.commit();
			try{
				SimpleMailMessage Email = new SimpleMailMessage();
				String[] cc = null;
				String recruiterName = employee.getEmployeeOfferDetails().getHelius_recruiter();
				String accountManagerName = employee.getEmployeeOfferDetails().getAccount_manager();
				String client = employee.getEmployeeOfferDetails().getClient();
				String payroll = employee.getEmployeeSalaryDetails().getPayroll_entity();
				String[] picklistNames = {recruiterName,accountManagerName};
				String ccSingapore = "";		
				if("Singapore".equalsIgnoreCase(employee.getEmployeeOfferDetails().getWork_country())){
					HashMap<String,String> emailids = Utils.getEmailIdFromPickllistNameAndEmployeeNameAssoc(picklistNames, session);
					Email.setTo(Utils.getHapProperty("notifyOnboardingForSingaporeCreatedOffer-TO"));
					String getCC = Utils.getHapProperty("notifyOnboardingForSingaporeCreatedOffer-CC");				
						if(emailids!=null && emailids.get(recruiterName) != null && !emailids.get(recruiterName).isEmpty()){
							getCC = getCC+","+emailids.get(recruiterName);
						}
						if(emailids!=null && emailids.get(accountManagerName) != null && !emailids.get(accountManagerName).isEmpty()){
							getCC = getCC+","+emailids.get(accountManagerName);
						}				
						if("DBS".equalsIgnoreCase(client) && "Helius".equalsIgnoreCase(payroll)){
							getCC = getCC + ","+Utils.getHapProperty("notifyOnboardingForSingaporeCC-DBS-Helius");	
						}
						if("DBS".equalsIgnoreCase(client) && !"Helius".equalsIgnoreCase(payroll)){
							getCC = getCC + ","+Utils.getHapProperty("notifyOnboardingForSingaporeCC-DBS-nonHelius");	
						}
						if(!"DBS".equalsIgnoreCase(client)){
							getCC = getCC + ","+Utils.getHapProperty("notifyOnboardingForSingaporeCC-nonDBS");	
						}
					if (getCC != null && !getCC.isEmpty()) {	
						cc = getCC.split(",");
					}
				}
				if("India".equalsIgnoreCase(employee.getEmployeeOfferDetails().getWork_country())){
					Email.setTo(Utils.getHapProperty("notifyOnboardingForIndiaCreatedOffer-TO"));
					String getCC = Utils.getHapProperty("notifyOnboardingForIndiaCreatedOffer-CC");
					/*java.util.List emailids =  Utils.getEmailIdFromPickllistNameAndEmployeeNameAssoc(recruiterName,session);
					for (Object emailid : emailids) {
						if(emailid.toString() != null && !emailid.toString().isEmpty()){
						getCC = getCC+","+emailid.toString();
						}
					}*/
					HashMap<String,String> emailids = Utils.getEmailIdFromPickllistNameAndEmployeeNameAssoc(picklistNames, session);
					if("DAH2".equalsIgnoreCase(client) && "Helius".equalsIgnoreCase(payroll)){
						getCC = getCC + ","+Utils.getHapProperty("notifyOnboardingForIndiaCC-DAH2-Helius");	
						if(emailids!=null && emailids.get(recruiterName) != null && !emailids.get(recruiterName).isEmpty()){
							getCC = getCC+","+emailids.get(recruiterName);
						}
						if(emailids!=null && emailids.get(accountManagerName) != null && !emailids.get(accountManagerName).isEmpty()){
							getCC = getCC+","+emailids.get(accountManagerName);
						}
					}
					if("DAH2".equalsIgnoreCase(client) && !"Helius".equalsIgnoreCase(payroll)){
						getCC = getCC + ","+Utils.getHapProperty("notifyOnboardingForIndiaCC-DAH2-nonHelius");	
					}
					if(!"DAH2".equalsIgnoreCase(client)){
						getCC = getCC + ","+Utils.getHapProperty("notifyOnboardingForIndiaCC-nonDAH2");	
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
				if("Thailand".equalsIgnoreCase(employee.getEmployeeOfferDetails().getWork_country())){
					Email.setTo(Utils.getHapProperty("notifyOnboardingForThailandCreatedOffer-TO"));
					String getCC = Utils.getHapProperty("notifyOnboardingForThailandCreatedOffer-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
				}
				Email.setCc(cc);
				Email.setSubject("New Offer Generated for candidate - "+employee.getEmployeeOfferDetails().getEmployee_name());
				Email.setText("Hi,"
						+ "\n\n" + "New Offer is generated for "+employee.getEmployeeOfferDetails().getEmployee_name()+" which is : " +employee.getEmployeeOfferDetails().getOffer_id()+" ." + "\n\n"
						+ "Client - " +employee.getEmployeeOfferDetails().getClient()+ "\n"
						+ "Location - ," +employee.getEmployeeOfferDetails().getWork_country()+ "\n"
						+ "CTC per year - "+employee.getEmployeeSalaryDetails().getCtc_per_year() +"\n"
						+ "CTC per month - "+employee.getEmployeeSalaryDetails().getCtc_per_month() +"\n"
						+ "Designation - "+employee.getEmployeeOfferDetails().getDesignation() +"\n"
						+ "Recruiter - "+employee.getEmployeeOfferDetails().getHelius_recruiter() +"\n"
						+ "Account Manager - "+employee.getEmployeeOfferDetails().getAccount_manager() +"\n"
						+ "Payroll - "+employee.getEmployeeSalaryDetails().getPayroll_entity() +"\n"
						+ "Offer Date - "+Utils.convertTimestampToStringDate(employee.getEmployeeOfferDetails().getOffer_made_on())+"\n"
						+ "Expected DOJ - "+Utils.convertTimestampToStringDate(employee.getEmployeeOfferDetails().getExpected_date_of_joining())+"\n"
						+ "Email-Id - "+employee.getEmployeeOfferDetails().getPersonal_email_id() +"\n\n"
						+ "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
						emailService.sendEmail(Email);
			}catch (Exception e) {
					e.printStackTrace();
				}
		} catch (HibernateException e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable(e.getCause().getMessage());	
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable(e.getMessage());
		} finally {
			session.close();
		}
	}
	
	@Override
	public void offUpdate(Employee employee, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		int offerId = 0;
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> fileFolder = new HashMap<String, String>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (employee.getEmployeeOfferDetails() != null) {
				offerId = employee.getEmployeeOfferDetails().getOffer_id();	
				if(employee.getEmployeeOfferDetails().getName_in_chinese_characters()!=null ){
					String convertedValue = new String(employee.getEmployeeOfferDetails().getName_in_chinese_characters().getBytes("ISO8859_1"), "UTF8");	
					employee.getEmployeeOfferDetails().setName_in_chinese_characters(convertedValue);	
				}
			//	session.evict(employee.getEmployeeOfferDetails());
				session.update(employee.getEmployeeOfferDetails());
				if(employee.getEmployeeOfferDetails().getOffer_path()!=null && !employee.getEmployeeOfferDetails().getOffer_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getOffer_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getOffer_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getOffer_path(), "offer");
				}
				if(employee.getEmployeeOfferDetails().getJob_desc_path()!=null && !employee.getEmployeeOfferDetails().getJob_desc_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getJob_desc_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getJob_desc_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getJob_desc_path(), "jobdescription");
				}
				if(employee.getEmployeeOfferDetails().getEp_path()!=null && !employee.getEmployeeOfferDetails().getEp_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getEp_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getEp_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getEp_path(), "ep");
				}
				if(employee.getEmployeeOfferDetails().getBgv_report_path()!=null && !employee.getEmployeeOfferDetails().getBgv_report_path().isEmpty()){
					String modifiedFileName =  offerId+"_"+employee.getEmployeeOfferDetails().getBgv_report_path();
				    templateFilenames.put(employee.getEmployeeOfferDetails().getBgv_report_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeOfferDetails().getBgv_report_path(), "bgv");
				}
				}
			if (employee.getEmployeeSalaryDetails()!= null) {
				employee.getEmployeeSalaryDetails().setOffer_id(offerId);
				session.evict(employee.getEmployeeSalaryDetails());
				session.merge(employee.getEmployeeSalaryDetails());
				}
			if (employee.getSowCtcBreakup() != null) {
				Iterator<Sow_Ctc_Breakup> itr = employee.getSowCtcBreakup().iterator();
				while (itr.hasNext()) {
					Sow_Ctc_Breakup sowCtcBreakup = itr.next();
					sowCtcBreakup.setOfferId(offerId);
					sowCtcBreakup.setGroupId(1);
					sowCtcBreakup.setStatus("active");
					session.evict(sowCtcBreakup);
					session.merge(sowCtcBreakup);
				}
			}
			if(employee.getDeletedsowCtcBreakup()!=null){
				Iterator<Sow_Ctc_Breakup> itr = employee.getDeletedsowCtcBreakup().iterator();
				while (itr.hasNext()) {
					Sow_Ctc_Breakup sow_Ctc_Breakup = itr.next();
					session.delete(sow_Ctc_Breakup);
				}
			}
			Map<String, MultipartFile> files = null;
			files = request.getFileMap();
			if (files.size() > 0) {			
				FilecopyStatus status = Utils.copyFiles(request, templateFilenames, fileFolder);
			copied_with_success = status.getCopied_with_success();
			}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable(e.getCause().getMessage());
		
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable(e.getMessage());
		} finally {
			session.close();
		}
	}
	
	@Override
	public ResponseEntity<byte[]> getEmployeeFiles(String employee_id, String filetype) {		
		Employee employee = get(employee_id);
		String url = null;
		if (filetype.equalsIgnoreCase("photo")) {
			url=employee.getEmployeePersonalDetails().getPhoto();
			if (url != null && !"".equalsIgnoreCase(url)) {
			url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator + employee_id+"_"+url;
		}
		}
		if (filetype.equalsIgnoreCase("cancelledcheque")) {
			url=employee.getEmployeeBankDetails().getCancelled_cheque_path();
			if (url != null && !"".equalsIgnoreCase(url)) {
			url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator + employee_id+"_"+url;
		}
		}
		byte[] files = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(url);
			files = IOUtils.toByteArray(fi);
			fi.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
		return responseEntity;
	}

	@Override
	public ResponseEntity<byte[]> getOfferFiles(String offerid, String filetype) {
		Employee employee = getOfferDetails(offerid);
		String url = null;
		if (filetype.equalsIgnoreCase("offer")) {
			url = employee.getEmployeeOfferDetails().getOffer_path();
			if (url != null && !"".equalsIgnoreCase(url)) {
				url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator + offerid+"_"+url;
			}
		}
		if (filetype.equalsIgnoreCase("jobdescription")) {
			url = employee.getEmployeeOfferDetails().getJob_desc_path();
			if (url != null && !"".equalsIgnoreCase(url)) {
				url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator + offerid+"_"+url;
			}
		}
		if (filetype.equalsIgnoreCase("ep")) {
			url = employee.getEmployeeOfferDetails().getEp_path();
			if (url != null && !"".equalsIgnoreCase(url)) {
				url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator + offerid+"_"+url;
			}
		}
		if (filetype.equalsIgnoreCase("bgv")) {
			url = employee.getEmployeeOfferDetails().getBgv_report_path();
			if (url != null && !"".equalsIgnoreCase(url)) {
				url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator + offerid+"_"+url;
			}
		}
		byte[] files = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(url);
			files = IOUtils.toByteArray(fi);
			fi.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
		return responseEntity;
	}

	@Override
	public void save(Employee employee, MultipartHttpServletRequest request, String offerId,String appUrl) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> fileFolder = new HashMap<String, String>();
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		Timestamp date1 = new Timestamp(System.currentTimeMillis());
		Object employeeid = null;
		String employee_id = null;
		try {
			Employee offer = getOfferDetails(offerId);
			Employee_Personal_Details employee_personal_details = employee.getEmployeePersonalDetails();
			employee_id = employee.getEmployeePersonalDetails().getEmployee_id();
			employee.getEmployeePersonalDetails().setEmployee_status("Active");
			if(employee_personal_details.getName_in_chinese_characters()!=null ){
				String convertedValue = new String(employee_personal_details.getName_in_chinese_characters().getBytes("ISO8859_1"), "UTF8");	
				employee_personal_details.setName_in_chinese_characters(convertedValue);	
			}
			if(offer.getEmployeeOfferDetails().getDesignation()!=null){
				employee_personal_details.setDesignation(offer.getEmployeeOfferDetails().getDesignation());
			}
			if(offer.getEmployeeOfferDetails().getPrimary_skills()!=null){
				employee_personal_details.setPrimary_skills(offer.getEmployeeOfferDetails().getPrimary_skills());
			}
			if(offer.getEmployeeOfferDetails().getSecondary_skills()!=null){
				employee_personal_details.setSecondary_skills(offer.getEmployeeOfferDetails().getSecondary_skills());
			}
			if(offer.getEmployeeOfferDetails().getResource_type()!=null){
				employee_personal_details.setResource_type(offer.getEmployeeOfferDetails().getResource_type());
			}
			if(offer.getEmployeeOfferDetails().getDate_of_birth()!=null){
				employee_personal_details.setDate_of_birth(offer.getEmployeeOfferDetails().getDate_of_birth());
			}
			if(offer.getEmployeeOfferDetails().getEmp_race()!=null){
				employee_personal_details.setEmp_race(offer.getEmployeeOfferDetails().getEmp_race());
			}if(offer.getEmployeeOfferDetails().getReligion()!=null){
				employee_personal_details.setReligion(offer.getEmployeeOfferDetails().getReligion());
			}
			employeeid = session.save(employee_personal_details);
			if (employee.getEmployeePersonalDetails().getPhoto() != null) {
				String modifiedFileName = employeeid + "_" + employee_personal_details.getPhoto();
				templateFilenames.put(employee.getEmployeePersonalDetails().getPhoto(), modifiedFileName);
				fileFolder.put(employee.getEmployeePersonalDetails().getPhoto(), "photo");
			}
			if (employee.getEmployeeAssignmentDetails() != null) {
				employee.getEmployeeAssignmentDetails().setEmployee_id(employee_id);
				session.save(employee.getEmployeeAssignmentDetails());
			}
			if (employee.getIndianEmployeesInsuranceDetails() != null) {
				employee.getIndianEmployeesInsuranceDetails().setEmployee_id(employee_id);
				session.save(employee.getIndianEmployeesInsuranceDetails());
				Indian_Employees_Insurance_Details indianInsurance = employee.getIndianEmployeesInsuranceDetails();
				List<Indian_Employee_Family_Member> members = indianInsurance.getIndianEmployeeFamilyMember();
				if (members != null) {
					Iterator<Indian_Employee_Family_Member> itr = indianInsurance.getIndianEmployeeFamilyMember()
							.iterator();
					while (itr.hasNext()) {
						Indian_Employee_Family_Member indianFamilyMembers = itr.next();
						indianFamilyMembers.setEmployee_id(employee_id);
						session.save(indianFamilyMembers);
					}
				}
			}
			if (employee.getSingaporeEmployeeInsuranceDetails() != null) {
				employee.getSingaporeEmployeeInsuranceDetails().setEmployee_id(employee_id);
				session.save(employee.getSingaporeEmployeeInsuranceDetails());
				Singapore_Employee_Insurance_Details singaporeInsurance = employee
						.getSingaporeEmployeeInsuranceDetails();
				List<Singapore_Employee_Family_Member> members = singaporeInsurance.getSingaporeEmployeeFamilyMember();
				if (members != null) {
					Iterator<Singapore_Employee_Family_Member> itr = singaporeInsurance
							.getSingaporeEmployeeFamilyMember().iterator();
					while (itr.hasNext()) {
						Singapore_Employee_Family_Member singaporeFamilyMembers = itr.next();
						singaporeFamilyMembers.setEmployee_id(employee_id);
						session.save(singaporeFamilyMembers);
					}
				}
			}
			if (employee.getEmployeeTermsAndConditions() != null) {
				employee.getEmployeeTermsAndConditions().setEmployee_id(employee_id);
				session.save(employee.getEmployeeTermsAndConditions());
			}
			if (employee.getEmployeeAppraisalDetails() != null) {
				employee.getEmployeeAppraisalDetails().setEmployee_id(employee_id);
				if(offer.getEmployeeSalaryDetails().getCtc_per_month()!=null){
				String basic = offer.getEmployeeSalaryDetails().getCtc_per_month();
				employee.getEmployeeAppraisalDetails().setCurrent_monthly_basic(basic);
				employee.getEmployeeAppraisalDetails().setNew_monthly_basic(basic);
				}
				if(offer.getEmployeeOfferDetails().getOnetime_bonus_amount()!=null){
					employee.getEmployeeAppraisalDetails().setBonus_amount(offer.getEmployeeOfferDetails().getOnetime_bonus_amount());
				}
				if(offer.getEmployeeOfferDetails().getBonus_payable()!=null){
					employee.getEmployeeAppraisalDetails().setBonus_frequency(offer.getEmployeeOfferDetails().getBonus_payable());
				}
				session.save(employee.getEmployeeAppraisalDetails());
			}
			if (employee.getEmployeeBankDetails() != null) {
				employee.getEmployeeBankDetails().setEmployee_id(employee_id);
				session.save(employee.getEmployeeBankDetails());
				if (employee.getEmployeeBankDetails().getCancelled_cheque_path() != null) {
					String modifiedFileName = employeeid + "_"
							+ employee.getEmployeeBankDetails().getCancelled_cheque_path();
					templateFilenames.put(employee.getEmployeeBankDetails().getCancelled_cheque_path(),
							modifiedFileName);
					fileFolder.put(employee.getEmployeeBankDetails().getCancelled_cheque_path(), "cancelledcheque");
				}
			}
			if (employee.getLeavesEligibility() != null && !employee.getLeavesEligibility().isEmpty()) {
				Iterator<Leave_Eligibility_Details> itr = employee.getLeavesEligibility().iterator();
				while (itr.hasNext()) {
					Leave_Eligibility_Details leaveEligibility = itr.next();
					leaveEligibility.setEmployee_id(employee_id);
					session.save(leaveEligibility);
				}
				Timestamp adoj = employee.getEmployeePersonalDetails().getActual_date_of_joining();
				List<Leave_Usage_Details> accruedLeaveusage = leaveserv.newEmployeeLeaveUsage(adoj,employee.getEmployeeWorkPermitDetails().getWork_country(),employee.getLeavesEligibility());
				for(Leave_Usage_Details lvusage : accruedLeaveusage){
					lvusage.setEmployee_id(employee_id);
					session.save(lvusage);
				}
			}
			if (employee.getEmployeeContactAddressDetails() != null) {
				Iterator<Contact_Address_Details> itr = employee.getEmployeeContactAddressDetails().iterator();
				while (itr.hasNext()) {
					Contact_Address_Details employeeContactAddress = itr.next();
					employeeContactAddress.setEmployee_id(employee_id);
					session.save(employeeContactAddress);
				}
			}
			if (employee.getEmergencyContactDetails() != null) {
				employee.getEmergencyContactDetails().setEmployee_id(employee_id);
				session.save(employee.getEmergencyContactDetails());
			}
			if (employee.getEmployeeIdentificationDetails() != null) {
				Iterator<Employee_Identification_Details> itr = employee.getEmployeeIdentificationDetails().iterator();
				while (itr.hasNext()) {
					Employee_Identification_Details employeeIdentificationDetails = itr.next();
					employeeIdentificationDetails.setEmployee_id(employee_id);
					session.save(employeeIdentificationDetails);
				}
			}
			if (employee.getEmployeeWorkPermitDetails() != null) {
				employee.getEmployeeWorkPermitDetails().setEmployee_id(employee_id);
				session.save(employee.getEmployeeWorkPermitDetails());
			}
			/*if (employee.getSowEmployeeAssoc() != null) {
				Iterator<Sow_Employee_Association> itr = employee.getSowEmployeeAssoc().iterator();
				while (itr.hasNext()) {
					Sow_Employee_Association sowEmpAssoc = itr.next();
					sowEmpAssoc.setEmployeeId(employee_id);
					session.save(sowEmpAssoc);
				}
			}*/
			if (employeeid != null) {
				if (offer.getEmployeeOfferDetails() != null) {
					offer.getEmployeeOfferDetails().setEmployee_id(employee_id);
					offer.getEmployeeOfferDetails().setOffer_status("joined");
					session.update(offer.getEmployeeOfferDetails());
				}
				if (offer.getEmployeeSalaryDetails() != null) {
					offer.getEmployeeSalaryDetails().setEmployee_id(employee_id);
					session.update(offer.getEmployeeSalaryDetails());
				}
				if (offer.getSowCtcBreakup() != null) {
					Iterator<Sow_Ctc_Breakup> itr = offer.getSowCtcBreakup().iterator();
					while (itr.hasNext()) {
						Sow_Ctc_Breakup sowCtcBreakup = itr.next();
						sowCtcBreakup.setEmployeeId(employee_id);
						sowCtcBreakup.setOfferId(0);
						sowCtcBreakup.setSowCtcBreakupId(0);
						session.save(sowCtcBreakup);
					}
				}
				 String sowofferAssoc_query = "select offAssoc.* from Sow_Employee_Association offAssoc where offer_id = :offer_id"; 
				  java.util.List offAssocList = session.createSQLQuery(sowofferAssoc_query).addEntity(Sow_Employee_Association.class).setParameter("offer_id", offerId).list();
				  Sow_Employee_Association sowAssoc = null; 
					List<Sow_Employee_Association> sow_OffAssoscList = new ArrayList<Sow_Employee_Association>();
					if(!offAssocList.isEmpty() && offAssocList != null) {	
					  Iterator<Sow_Employee_Association> itr = offAssocList.iterator();
						while (itr.hasNext()) {
							Sow_Employee_Association sowAssociation = itr.next();
							sowAssociation.setEmployeeId(employee_id);
							sowAssociation.setOffer_id(0);
							session.update(sowAssociation);
						}
					  }	
			}
			// create employee selfcare userlogin-id 
			String token = null;
			if("India".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())
					|| "Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
			String password = employee_id+"hap@heliustech";
			String encodedpassword = Base64.getEncoder().encodeToString(password.getBytes());
			SecureRandom random = new SecureRandom();
		    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
			byte[] buffer = new byte[20];
	        random.nextBytes(buffer);
	        token = encoder.encodeToString(buffer)+Instant.now().toEpochMilli();
		    Employee_Selfcare_Users user = new Employee_Selfcare_Users();
		    user.setEmployee_id(employee_id);
		    user.setEmployee_name(employee.getEmployeePersonalDetails().getEmployee_name());
		    user.setPassword(encodedpassword);
		    user.setToken(token);
		    user.setActive("Yes");
		    user.setUser_login_attempts(0);
		    user.setCreated_by(employee.getEmployeePersonalDetails().getCreated_by());
		    session.save(user);
			}
		Map<String, MultipartFile> files = null;
		files = request.getFileMap();
		if (files.size() > 0) {
			FilecopyStatus status = Utils.copyFiles(request, templateFilenames, fileFolder);
			copied_with_success = status.getCopied_with_success();
		}
			transaction.commit();
			/*
			 * send Activation link for employee selfcare loginId
			 * */
			if("India".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country()) ||
					"Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
			try{
				int index  = appUrl.lastIndexOf("/");
				String urlindex = appUrl.substring(0, index);
				String loginLink = urlindex+"/login.html";
			    String To = offer.getEmployeeOfferDetails().getPersonal_email_id();				
				String subject= "Account Activation";
				String text = "Hello " + employee.getEmployeePersonalDetails().getEmployee_name() + ","
						+ "\n\n" + "Please first click below link to activate your login-id and change your password "
						+ "\n\n" + appUrl + "?token="+employeeid+"-fgtN"+ token+"\n\n" 
						+ "Use below link to login further"
						+"\n\n" + loginLink +"\n\n"
						+ "With Regards," + "\n"
						+ "Helius Technologies.";
				emailService.sendEmail(To, null, null, subject, text);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		try{
				SimpleMailMessage Email = new SimpleMailMessage();
				String[] cc = null;
				String recruiterName = offer.getEmployeeOfferDetails().getHelius_recruiter();
				String accountManagerName = employee.getEmployeeAssignmentDetails().getAccount_manager();
				String client = employee.getEmployeeAssignmentDetails().getClient();
				String payroll = offer.getEmployeeSalaryDetails().getPayroll_entity();
				String[] picklistNames = {recruiterName,accountManagerName};				
				if("Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
					/*String picklist_name = employee.getEmployeeAssignmentDetails().getAccount_manager();
					String Query = "SELECT helius_email_id from pickllistNameAndEmployeeNameAssoc where picklist_name = :picklist_name ";
					java.util.List emailids = session.createSQLQuery(Query).setParameter("picklist_name", picklist_name).list();
					String toSingapore = "";
					for (Object emailid : emailids) {
						toSingapore = emailid.toString();
					}
					Email.setTo(toSingapore);
					String getCC = Utils.getHapProperty("notifyEmployeeIdGeneratedToSingaporeCCHr-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}*/
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
				if("India".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
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
				if("Thailand".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
					Email.setTo(Utils.getHapProperty("notifyEmployeeIdGeneratedToThailandPayrollUser-TO"));
					String getCC = Utils.getHapProperty("notifyEmployeeIdGeneratedToThailandCCHr-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
				}
				Email.setCc(cc);
				Email.setSubject("New EmployeeId Generated for "+employee.getEmployeePersonalDetails().getEmployee_name());
				Email.setText("Hi,"
						+ "\n\n" + "New Employee-Id is generated for "+employee.getEmployeePersonalDetails().getEmployee_name()+" which is : " +employee.getEmployeePersonalDetails().getEmployee_id()+" ." + "\n"
						+ "Location - "+employee.getEmployeeWorkPermitDetails().getWork_country() +"\n"
						+ "Client - "+employee.getEmployeeAssignmentDetails().getClient() +"\n"
						+ "Account Manager - "+employee.getEmployeeAssignmentDetails().getAccount_manager() +"\n"
						+ "Recruiter - "+offer.getEmployeeOfferDetails().getHelius_recruiter() +"\n"
						+ "Payroll - "+employee.getEmployeeSalaryDetails().getPayroll_entity() +"\n"
						+ "Offer Date - "+Utils.convertTimestampToStringDate(offer.getEmployeeOfferDetails().getOffer_made_on())+"\n"
						+ "Expected DOJ - "+ Utils.convertTimestampToStringDate(offer.getEmployeeOfferDetails().getExpected_date_of_joining())+"\n"
						+ "Actual DOJ - "+Utils.convertTimestampToStringDate(employee.getEmployeePersonalDetails().getActual_date_of_joining()) +"\n\n"
						+ "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
				emailService.sendEmail(Email);
			}catch (Exception e) {
					e.printStackTrace();
				}
		}catch (HibernateException e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to save the Employee Details " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to save Employee Details " + e.getMessage());
		} 
		finally {
			session.close();
		}
	}
	/*public void createSelfcareUser(String json) 
			  throws Throwable {
		try{
			    CloseableHttpClient client = HttpClients.createDefault();
			    HttpPost httpPost = new HttpPost("http://localhost:8080/Employee_Selfcare_backend/user/createuser");
			    //StringEntity entity = new StringEntity(json);
			   // httpPost.setEntity(entity);
			    httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "application/json");
			    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			    pairs.add(new BasicNameValuePair("user",json));
			    httpPost.setEntity(new UrlEncodedFormEntity(pairs));
			    UsernamePasswordCredentials creds
			      = new UsernamePasswordCredentials("004", "thiru@123");
			    httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
			    CloseableHttpResponse response = client.execute(httpPost);
			    String result = EntityUtils.toString(response.getEntity());
                System.out.println("====result=="+result);
			    //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
			    client.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new Throwable("Failed to save Employee Details " + e.getMessage());
		}
			}*/
	@Override
	public void empUpdate(Employee employee, MultipartHttpServletRequest request) throws Throwable {
	String	offerId = null;
		Session session = null;
		Transaction transaction = null;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> fileFolder = new HashMap<String, String>();
		long millis = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(millis);
		Timestamp date1 = new Timestamp(System.currentTimeMillis());
		Object employeeid = null;
		String employee_id = null;
		try {		
			if(employee.getEmployeePersonalDetails()!=null){
			Employee_Personal_Details employee_personal_details = employee.getEmployeePersonalDetails();
			employee_id = employee.getEmployeePersonalDetails().getEmployee_id();
			if(employee_personal_details.getName_in_chinese_characters()!=null ){
				String convertedValue = new String(employee_personal_details.getName_in_chinese_characters().getBytes("ISO8859_1"), "UTF8");	
				employee_personal_details.setName_in_chinese_characters(convertedValue);	
			}
			session.evict(employee_personal_details);
			session.merge(employee_personal_details);
			if(employee.getEmployeePersonalDetails().getPhoto()!=null && !employee.getEmployeePersonalDetails().getPhoto().isEmpty()){
				String modifiedFileName =  employee_id+"_"+employee_personal_details.getPhoto();
			    templateFilenames.put(employee.getEmployeePersonalDetails().getPhoto(), modifiedFileName);
			    fileFolder.put(employee.getEmployeePersonalDetails().getPhoto(), "photo");
			}
			}
			if (employee.getEmployeeAssignmentDetails() != null) {
				employee.getEmployeeAssignmentDetails().setEmployee_id(employee_id);
				session.evict(employee.getEmployeeAssignmentDetails());
				session.merge(employee.getEmployeeAssignmentDetails());	
				}
			if (employee.getIndianEmployeesInsuranceDetails() != null) {
				employee.getIndianEmployeesInsuranceDetails().setEmployee_id(employee_id);
				session.evict(employee.getIndianEmployeesInsuranceDetails());
				session.merge(employee.getIndianEmployeesInsuranceDetails());
				Indian_Employees_Insurance_Details indianInsurance = employee.getIndianEmployeesInsuranceDetails();
				List<Indian_Employee_Family_Member> members = indianInsurance.getIndianEmployeeFamilyMember();
				if (members != null) {
					Iterator<Indian_Employee_Family_Member> itr = indianInsurance.getIndianEmployeeFamilyMember()
							.iterator();
					while (itr.hasNext()) {
						Indian_Employee_Family_Member indianFamilyMembers = itr.next();
						indianFamilyMembers.setEmployee_id(employee_id);
						session.evict(indianFamilyMembers);
						session.merge(indianFamilyMembers);
					}
				}
			}
			if (employee.getSingaporeEmployeeInsuranceDetails() != null) {
				employee.getSingaporeEmployeeInsuranceDetails().setEmployee_id(employee_id);
				session.evict(employee.getSingaporeEmployeeInsuranceDetails());
				session.merge(employee.getSingaporeEmployeeInsuranceDetails());
				Singapore_Employee_Insurance_Details singaporeInsurance = employee
						.getSingaporeEmployeeInsuranceDetails();
				List<Singapore_Employee_Family_Member> members = singaporeInsurance.getSingaporeEmployeeFamilyMember();
				if (members != null) {
					Iterator<Singapore_Employee_Family_Member> itr = singaporeInsurance
							.getSingaporeEmployeeFamilyMember().iterator();
					while (itr.hasNext()) {
						Singapore_Employee_Family_Member singaporeFamilyMembers = itr.next();
						singaporeFamilyMembers.setEmployee_id(employee_id);
						session.evict(singaporeFamilyMembers);
						session.merge(singaporeFamilyMembers);
					}
				}
			}
			if (employee.getEmployeeTermsAndConditions() != null) {
				employee.getEmployeeTermsAndConditions().setEmployee_id(employee_id);
				session.evict(employee.getEmployeeTermsAndConditions());
				session.merge(employee.getEmployeeTermsAndConditions());
				}
			if (employee.getEmployeeBankDetails() != null) {
				employee.getEmployeeBankDetails().setEmployee_id(employee_id);
				session.evict(employee.getEmployeeBankDetails());
				session.merge(employee.getEmployeeBankDetails());
				if(employee.getEmployeeBankDetails().getCancelled_cheque_path()!=null && !employee.getEmployeeBankDetails().getCancelled_cheque_path().isEmpty() ){
					String modifiedFileName =  employee_id+"_"+employee.getEmployeeBankDetails().getCancelled_cheque_path();
				    templateFilenames.put(employee.getEmployeeBankDetails().getCancelled_cheque_path(), modifiedFileName);
				    fileFolder.put(employee.getEmployeeBankDetails().getCancelled_cheque_path(), "cancelledcheque");
				}
				}
			if (employee.getLeavesEligibility() != null && !employee.getLeavesEligibility().isEmpty()) {
				Iterator<Leave_Eligibility_Details> itr = employee.getLeavesEligibility().iterator();
				//Employee_Leave_Data leavedata = leaveserv.getEmployeeLeaveData(employee_id);
				while (itr.hasNext()) {
					Leave_Eligibility_Details leaveEligibility = itr.next();
					leaveEligibility.setEmployee_id(employee_id);
					session.evict(leaveEligibility);
					session.merge(leaveEligibility);		
					}
				String leaveUsageDetails = "select * from  Leave_Usage_Details where employee_id = :employee_id";
				java.util.List leaveUsageDetailsList = session.createSQLQuery(leaveUsageDetails)
						.addEntity(Leave_Usage_Details.class).setParameter("employee_id", employee_id).list();			
				if(leaveUsageDetailsList == null || leaveUsageDetailsList.isEmpty()) {
				Timestamp adoj = employee.getEmployeePersonalDetails().getActual_date_of_joining();
				List<Leave_Usage_Details> accruedLeaveusage = leaveserv.newEmployeeLeaveUsage(adoj,employee.getEmployeeWorkPermitDetails().getWork_country(),employee.getLeavesEligibility());
				for(Leave_Usage_Details lvusage : accruedLeaveusage){
					lvusage.setEmployee_id(employee_id);
					session.save(lvusage);
				}
				}
			}
			  if (employee.getEmployeeAppraisalDetails() != null) {
			  employee.getEmployeeAppraisalDetails().setEmployee_id(employee_id ); 
			 /* Timestamp t1 = employee.getEmployeePersonalDetails().getActual_date_of_joining(); 
					  Calendar cal = Calendar.getInstance();
					  cal.setTime(t1);
					  cal.add(Calendar.MONTH, 10);
					  t1.setTime(cal.getTime().getTime()); */
			  session.evict(employee.getEmployeeAppraisalDetails());
				session.merge(employee.getEmployeeAppraisalDetails());
				}
			 
			if (employee.getEmployeeContactAddressDetails() != null) {
				Iterator<Contact_Address_Details> itr = employee.getEmployeeContactAddressDetails().iterator();
				while (itr.hasNext()) {
					Contact_Address_Details employeeContactAddress = itr.next();
					employeeContactAddress.setEmployee_id(employee_id);
					session.evict(employeeContactAddress);
					session.merge(employeeContactAddress);
				}
			}
			if (employee.getEmergencyContactDetails() != null) {
				employee.getEmergencyContactDetails().setEmployee_id(employee_id);
				session.evict(employee.getEmergencyContactDetails());
				session.merge(employee.getEmergencyContactDetails());
				}
			if (employee.getEmployeeIdentificationDetails() != null) {
				Iterator<Employee_Identification_Details> itr = employee.getEmployeeIdentificationDetails().iterator();
				while (itr.hasNext()) {
					Employee_Identification_Details employeeIdentificationDetails = itr.next();
					employeeIdentificationDetails.setEmployee_id(employee_id);
					session.evict(employeeIdentificationDetails);
					session.merge(employeeIdentificationDetails);
					}
			}
			if(employee.getDeleteSingaporeEmployeeFamilyMember()!=null){
					Iterator<DeleteSingaporeEmployeeFamilyMember> itr = employee.getDeleteSingaporeEmployeeFamilyMember().iterator();
					Singapore_Employee_Family_Member singmember = new Singapore_Employee_Family_Member();
					while (itr.hasNext()) {
						DeleteSingaporeEmployeeFamilyMember deleteSingaporeEmployeeFamilyMember = itr.next();
						int memberid = deleteSingaporeEmployeeFamilyMember.getSingapore_employee_family_member_id();
						singmember.setSingapore_employee_family_member_id(memberid);
						session.delete(singmember);
					}
					
				}
			if(employee.getDeleteIndianEmployeeFamilyMember()!=null){
				Iterator<DeleteIndianEmployeeFamilyMember> itr = employee.getDeleteIndianEmployeeFamilyMember().iterator();
				Indian_Employee_Family_Member indmember = new Indian_Employee_Family_Member();
				while (itr.hasNext()) {
					DeleteIndianEmployeeFamilyMember deleteIndianEmployeeFamilyMember = itr.next();
					int memberid = deleteIndianEmployeeFamilyMember.getIndian_employee_family_member_id();
					indmember.setIndian_employee_family_member_id(memberid);
					session.delete(indmember);
				}
				
			}
			if (employee.getEmployeeWorkPermitDetails() != null) {
				employee.getEmployeeWorkPermitDetails().setEmployee_id(employee_id);
				session.evict(employee.getEmployeeWorkPermitDetails());
				session.merge(employee.getEmployeeWorkPermitDetails());
				}
			if (employee.getSowEmployeeAssoc() != null) {
				Iterator<Sow_Employee_Association> itr = employee.getSowEmployeeAssoc().iterator();
				while (itr.hasNext()) {
					Sow_Employee_Association sowEmpAssoc = itr.next();
					sowEmpAssoc.setEmployeeId(employee_id);
					session.evict(sowEmpAssoc);
					session.merge(sowEmpAssoc);
				}
			}
			if (employee.getSowCtcBreakup() != null) {
				Iterator<Sow_Ctc_Breakup> itr = employee.getSowCtcBreakup().iterator();
				int count=0;
				int groupId = 0;
				int renewGroupId = 0;
				while (itr.hasNext()) {
					Sow_Ctc_Breakup sowCtcBreakup = itr.next();
					if(groupId == 0){
					if(sowCtcBreakup.getGroupId() != 0){
					groupId = sowCtcBreakup.getGroupId();
					}else{
						for(Sow_Ctc_Breakup brkp : employee.getSowCtcBreakup()){
							if(brkp.getGroupId() != 0){
								groupId = brkp.getGroupId();
								break;
							}
						}
					}
					}
					if(count==0){
					if("renewal".equalsIgnoreCase(sowCtcBreakup.getStatus())){
						Employee emp = get(employee_id);
						if (emp.getSowCtcBreakup() != null) {
							Iterator<Sow_Ctc_Breakup> itr1 = emp.getSowCtcBreakup().iterator();
							while (itr1.hasNext()) {
								Sow_Ctc_Breakup sowCtcBreakupPrev = itr1.next();
								sowCtcBreakupPrev.setStatus("inactive");
								session.update(sowCtcBreakupPrev);
								renewGroupId = sowCtcBreakupPrev.getGroupId();
							}
						}	
						employee.setDeletedsowCtcBreakup(null);
					}
					count++;
					}
					if(renewGroupId==0){
					sowCtcBreakup.setGroupId(groupId);
					sowCtcBreakup.setStatus("active");
					sowCtcBreakup.setEmployeeId(employee_id);
				//	sowCtcBreakup.setOfferId(employee.getEmployeeOfferDetails().getOffer_id());
					session.evict(sowCtcBreakup);
					session.merge(sowCtcBreakup);
					}else{
					int	renGroupId = renewGroupId;
						sowCtcBreakup.setSowCtcBreakupId(0);
						sowCtcBreakup.setGroupId(renGroupId+1);
						sowCtcBreakup.setStatus("active");
						sowCtcBreakup.setEmployeeId(employee_id);
						sowCtcBreakup.setOfferId(0);
						session.save(sowCtcBreakup);
					}
				}
			}
			if(employee.getDeletedsowCtcBreakup()!=null ){
				Iterator<Sow_Ctc_Breakup> itr = employee.getDeletedsowCtcBreakup().iterator();
				while (itr.hasNext()) {
					Sow_Ctc_Breakup sow_Ctc_Breakup = itr.next();
					session.delete(sow_Ctc_Breakup);
				}
			}
			Map<String, MultipartFile> files = null;
			files = request.getFileMap();
			if (files.size() > 0) {			
				FilecopyStatus status = Utils.copyFiles(request, templateFilenames, fileFolder);
			copied_with_success = status.getCopied_with_success();
			}
			transaction.commit();
			try{
				if(employee.getEmployeeExitCheck().isEmployeeexit_changed() || employee.getEmployeeExitCheck().isEmployeerelievingdate_changed()){
				SimpleMailMessage Email = new SimpleMailMessage();
				String[] cc = null;				
				/*if("Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
					String picklist_name = employee.getEmployeeAssignmentDetails().getAccount_manager();
					String Query = "SELECT helius_email_id from pickllistNameAndEmployeeNameAssoc where picklist_name = :picklist_name ";
					java.util.List emailids = session.createSQLQuery(Query).setParameter("picklist_name", picklist_name).list();
					String toSingapore = "";
					for (Object emailid : emailids) {
						toSingapore = emailid.toString();
					}
					Email.setTo(toSingapore);
					String getCC = Utils.getHapProperty("notifyExitDetailsForSingaporeEmployee-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
				}*/
				String recruiterName = employee.getEmployeeOfferDetails().getHelius_recruiter();
				String accountManagerName = employee.getEmployeeAssignmentDetails().getAccount_manager();
				String client = employee.getEmployeeAssignmentDetails().getClient();
				String payroll = employee.getEmployeeSalaryDetails().getPayroll_entity();
				String[] picklistNames = {recruiterName,accountManagerName};				
				if("Singapore".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
					HashMap<String,String> emailids = Utils.getEmailIdFromPickllistNameAndEmployeeNameAssoc(picklistNames, session);
					Email.setTo(Utils.getHapProperty("notifyExitDetailsForSingaporeEmployee-TO"));
					String getCC = Utils.getHapProperty("notifyExitDetailsForSingaporeEmployee-CC");				
						if(emailids!=null && emailids.get(recruiterName) != null && !emailids.get(recruiterName).isEmpty()){
						getCC = getCC+","+emailids.get(recruiterName);
						}
						if(emailids!=null && emailids.get(accountManagerName) != null && !emailids.get(accountManagerName).isEmpty()){
						getCC = getCC+","+emailids.get(accountManagerName);
						}			
						if("DBS".equalsIgnoreCase(client) && "Helius".equalsIgnoreCase(payroll)){
							getCC = getCC + ","+Utils.getHapProperty("notifyExitDetailsForSingaporeCC-DBS-Helius");	
						}
						if("DBS".equalsIgnoreCase(client) && !"Helius".equalsIgnoreCase(payroll)){
							getCC = getCC + ","+Utils.getHapProperty("notifyExitDetailsForSingaporeCC-DBS-nonHelius");	
						}
						if(!"DBS".equalsIgnoreCase(client)){
							getCC = getCC + ","+Utils.getHapProperty("notifyExitDetailsForSingaporeCC-nonDBS");	
						}
					if (getCC != null && !getCC.isEmpty()) {	
						cc = getCC.split(",");
					}
				}
				if("India".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
					Email.setTo(Utils.getHapProperty("notifyExitDetailsForIndiaEmployee-TO"));
					String getCC = Utils.getHapProperty("notifyExitDetailsForIndiaEmployee-CC");
					HashMap<String,String> emailids = Utils.getEmailIdFromPickllistNameAndEmployeeNameAssoc(picklistNames, session);
					if("DAH2".equalsIgnoreCase(client) && "Helius".equalsIgnoreCase(payroll)){
						getCC = getCC + ","+Utils.getHapProperty("notifyExitDetailsForIndiaCC-DAH2-Helius");	
						if(emailids!=null && emailids.get(recruiterName) != null && !emailids.get(recruiterName).isEmpty()){
							getCC = getCC+","+emailids.get(recruiterName);
						}
						if(emailids!=null && emailids.get(accountManagerName) != null && !emailids.get(accountManagerName).isEmpty()){
							getCC = getCC+","+emailids.get(accountManagerName);
						}
					}
					if("DAH2".equalsIgnoreCase(client) && !"Helius".equalsIgnoreCase(payroll)){
						getCC = getCC + ","+Utils.getHapProperty("notifyExitDetailsForIndiaCC-DAH2-nonHelius");	
					}
					if(!"DAH2".equalsIgnoreCase(client)){
						getCC = getCC + ","+Utils.getHapProperty("notifyExitDetailsForIndiaCC-nonDAH2");	
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
				if("Thailand".equalsIgnoreCase(employee.getEmployeeWorkPermitDetails().getWork_country())){
					Email.setTo(Utils.getHapProperty("notifyExitDetailsForThailandEmployee-TO"));
					String getCC = Utils.getHapProperty("notifyExitDetailsForThailandEmployee-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
				}
				Email.setCc(cc);
				Email.setSubject("Employee exit details updated for "+employee.getEmployeePersonalDetails().getEmployee_name());
				StringBuffer message = new StringBuffer();
				message.append("Hi, "+ "\n\n");
				if(employee.getEmployeeExitCheck().isEmployeeexit_changed()){
					message.append("Employee Status is updated for "+employee.getEmployeePersonalDetails().getEmployee_name()+" TO "+employee.getEmployeePersonalDetails().getEmployee_status()+ "\n" );
				}
				if(employee.getEmployeeExitCheck().isEmployeerelievingdate_changed()){
					message.append("Employee Exit Date is updated for "+employee.getEmployeePersonalDetails().getEmployee_name()+" TO "+employee.getEmployeePersonalDetails().getRelieving_date()+ "\n" );
				}
				message.append("Location - "+employee.getEmployeeWorkPermitDetails().getWork_country() +"\n");
				message.append("Client - "+employee.getEmployeeAssignmentDetails().getClient() +"\n");
				message.append("Account Manager - "+employee.getEmployeeAssignmentDetails().getAccount_manager() +"\n");
				message.append("Recruiter - "+employee.getEmployeeOfferDetails().getHelius_recruiter() +"\n");
				message.append("Payroll - "+employee.getEmployeeSalaryDetails().getPayroll_entity() +"\n");
				message.append("Designation - "+employee.getEmployeePersonalDetails().getDesignation()+"\n");
				message.append("Actual DOJ - "+Utils.convertTimestampToStringDate(employee.getEmployeePersonalDetails().getActual_date_of_joining()) +"\n");
				message.append("Reason For Exit - "+employee.getEmployeePersonalDetails().getResignation_reason() +"\n");
				message.append("Date of resignation - "+Utils.convertTimestampToStringDate(employee.getEmployeePersonalDetails().getResignation_date()) +"\n");
				message.append("Contractual LWD - "+Utils.convertTimestampToStringDate(employee.getEmployeePersonalDetails().getContractual_working_date()) +"\n");
				message.append("Exit Date - "+Utils.convertTimestampToStringDate(employee.getEmployeePersonalDetails().getRelieving_date()) +"\n\n");
				message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
				Email.setText(message.toString());
				emailService.sendEmail(Email);
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
		} catch (HibernateException e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to update the Employee Details" + e.getCause().getMessage());
		}
		 catch (Exception e) {
				e.printStackTrace();
				Utils.deleteFiles(copied_with_success);
				throw new Throwable("Failed to update Employee Details" + e.getMessage());
			}
			finally {
				session.close();
			}
		}
	
	@Override
	public ArrayList<Object> getAllEmpIdName() throws Throwable {
		Session session = null;
		java.util.List empList =null;
		String emppicklist = "";
		ArrayList<Object> emplists = new ArrayList<Object>();
		try {
			session = sessionFactory.openSession();
			String empQuery = "SELECT * FROM Employee_Personal_Details where employee_status ='Active' ORDER BY employee_name ASC;";
			 empList = session.createSQLQuery(empQuery).addEntity(Employee_Personal_Details.class).list();	
		Employee_Personal_Details empPersonDetails = null;
	/*	Collections.sort(empList,new Comparator<Employee_Personal_Details>() {
			@Override
			public int compare(Employee_Personal_Details o1, Employee_Personal_Details o2) {
				// TODO Auto-generated method stub
				return o1.getEmployee_name().compareTo(o2.getEmployee_name());  
			}
		});  */
		for(Object obj : empList){
			empPersonDetails = (Employee_Personal_Details) obj;
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("employee_id", empPersonDetails.getEmployee_id());
			map.put("employee_name", empPersonDetails.getEmployee_name());
			emplists.add(map);
		}
			ObjectMapper om = new ObjectMapper();
			try {
				emppicklist = om.writeValueAsString(emplists);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				session.close();
			}
			session.close();
		}catch(Exception e){
			e.printStackTrace();
			session.close();
		}
		//return  "{\"empIdNamePicklist\" : " + emppicklist + "}";
		return emplists;
	}
	
	@Override
	public void sendEmailNotification(String jsonData) throws Throwable {
		ObjectMapper obm = new ObjectMapper();
		try {
		EmailScreen emailJson = obm.readValue(jsonData, EmailScreen.class);
		String st = emailJson.getCc();
		String[] cc = null;
		if (st != null && !st.isEmpty()) {
			cc = st.split(";");
		}
		String to = emailJson.getTo();
		String subject = emailJson.getSubject();
		String text = emailJson.getText();
		List<String> urlList = new ArrayList<String>();
		emailService.sendMessageWithAttachment(to, cc, subject, text, urlList);		
		}  catch (MessagingException e) {
			e.printStackTrace();
			throw new Throwable("Unable to Send Email notification " + e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable to Send Email notification " + e.getMessage());
		}
	}
	
	@Override
	public void saveOrUpdateHelpVideo(String helpdata, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		Map<String, String> templateFilenames = new HashMap<String, String>();
		ObjectMapper om = new ObjectMapper();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Help_Videos	help = om.readValue(helpdata, Help_Videos.class);
			if (help.getHelp_videos_id()==0) {
				session.save(help);
			}else{
				session.evict(help);
				session.merge(help);
			}
				Map<String, MultipartFile> files = null;
				files = request.getFileMap();
				if (files.size() > 0) {		
				templateFilenames.put(help.getVideos_url(), help.getVideos_url());
				FilecopyStatus status = Utils.copyFiles(request, templateFilenames, "hapHelpVideo");
				copied_with_success = status.getCopied_with_success();
				}
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable(e.getCause().getMessage());	
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable(e.getMessage());
		} finally {
			session.close();
		}
	}
	
	public void deleteHelpVideo(String help_videos_id)throws Throwable{
		Session session = null;
		Transaction transaction = null;
		List<Help_Videos> help_Videos = null;
		try {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		help_Videos = getAllHelpVideos();
		Help_Videos videoObj = null;
		for(Help_Videos help : help_Videos){
			int videos_id = Integer.parseInt(help_videos_id);
			if(videos_id == help.getHelp_videos_id()){
				videoObj = help;
				 break;
			}
		}
		String url = null;
			if (videoObj.getVideos_url() != null && !"".equalsIgnoreCase(videoObj.getVideos_url())) {
				url = Utils.getProperty("fileLocation") + File.separator + "hapHelpVideo" + File.separator + videoObj.getVideos_url();
			}
			if(videoObj != null){
			session.delete(videoObj);
			transaction.commit();
			}
			File file = new File(url);
			if(file.exists()){
			file.delete();
			}
	} catch (HibernateException e) {
		e.printStackTrace();
		Utils.deleteFiles(copied_with_success);
		throw new Throwable(e.getCause().getMessage());	
	} finally {
		session.close();
	}
	}

	
	@Override
	public List<Help_Videos> getAllHelpVideos() throws Throwable {
		Session session = null;
		List<Help_Videos> help_Videos = new ArrayList<Help_Videos>();
		try {
			session = sessionFactory.openSession();
			String query = "select * from Help_Videos";
			help_Videos = session.createSQLQuery(query).addEntity(Help_Videos.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch help videos");
		} finally {
			session.close();
		}
		return help_Videos;
	}
	
	@Override
	public ResponseEntity<byte[]> getHelpVideoFIle(int help_videos_id) {
		List<Help_Videos> help_Videos = null;
		try {
			help_Videos = getAllHelpVideos();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String videoURL = null;
		for(Help_Videos help : help_Videos){
			if(help_videos_id == help.getHelp_videos_id()){
				 videoURL = help.getVideos_url();
				 break;
			}
		}
		String url = null;
			if (videoURL != null && !"".equalsIgnoreCase(videoURL)) {
				url = Utils.getProperty("fileLocation") + File.separator + "hapHelpVideo" + File.separator + videoURL;
			}
		byte[] files = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(url);
			files = IOUtils.toByteArray(fi);
			fi.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
		return responseEntity;
	}
	
	/* public List audit(){
		Session session = null;
		String className ="Employee_Personal_Details";
    Class classTemp = null;
	List empchecklist = new ArrayList();

	List ecklist=null;
		try {
		 session = sessionFactory.openSession();
		AuditReader reader = AuditReaderFactory.get(session);
		AuditQuery query = reader.createQuery()
				  .forRevisionsOfEntity(Employee_Personal_Details.class, true, true);
		 ecklist =  query.getResultList();
				}catch (Exception e) {
			e.printStackTrace();		
			}finally {
				session.close();
			}	
		return ecklist;
	}*/
	/*public BaseEntity getPreviousVersion(BaseEntity entity, int current_rev) {

	    AuditReader reader = AuditReaderFactory.get(entityManager);

	    Number prior_revision = (Number) reader.createQuery()
	            .forRevisionsOfEntity(entity.getClass(), false, true)
	            .addProjection(AuditEntity.revisionNumber().max())
	            .add(AuditEntity.id().eq(entity.getId()))
	            .add(AuditEntity.revisionNumber().lt(current_rev))
	            .getSingleResult();

	    if (prior_revision != null)
	        return reader.find(entity.getClass(), entity.getId(), prior_revision);
	    else
	        return null;
	}
	*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.dao.IEmployeeDAO#delete(java.lang.String)
	 */
	@Override
	public void delete(String employeeid) {
		Session session = null;
		Transaction transaction = null;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		Query q1 = session.createQuery(hql1).setParameter("employee_id", employeeid);
		q1.executeUpdate();
		Query q2 = session.createQuery(hql2).setParameter("employee_id", employeeid);
		q2.executeUpdate();
		Query q3 = session.createQuery(hql3).setParameter("employee_id", employeeid);
		q3.executeUpdate();
		Query q4 = session.createQuery(hql4).setParameter("employee_id", employeeid);
		q4.executeUpdate();
		Query q5 = session.createQuery(hql5).setParameter("employee_id", employeeid);
		q5.executeUpdate();
		transaction.commit();
		session.close();
	}

}
