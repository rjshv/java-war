/**
 * 
 */
package com.helius.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Employee_Personal_Details;
import com.helius.entities.Sow_Billing_Schedule;
import com.helius.entities.Sow_Details;
import com.helius.entities.Sow_Details_History;
import com.helius.entities.Sow_Employee_Association;
import com.helius.utils.FilecopyStatus;
import com.helius.utils.Utils;
public class SowDAOImpl implements ISowDAO {

	// private static final Logger logger =
	// LogManager.getLogger(SowDAOImpl.class);
	private org.hibernate.internal.SessionFactoryImpl sessionFactory;

	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SowDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	private List<String> copied_with_success = new ArrayList<String>();

	@Override
	public String getSowPicklist() {
		Session session = null;
		java.util.List sowList = null;
		java.util.List empList = null;
		String sowpicklist = "";
		String emppicklist = "";
		java.util.List sowAlertlist = null;
		String sowalertpicklist = "";

		try {
			session = sessionFactory.openSession();
			String sowQuery = "select * from Sow_Details";
			sowList = session.createSQLQuery(sowQuery).addEntity(Sow_Details.class).list();
			String empQuery = "SELECT * FROM Employee_Personal_Details a LEFT JOIN Employee_Assignment_Details b ON a.employee_id=b.employee_id WHERE a.employee_status='Active' and  b.client NOT LIKE '%Helius%' OR b.client IS NULL ORDER BY employee_name ASC";
			empList = session.createSQLQuery(empQuery).addEntity(Employee_Personal_Details.class).list();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date2 = sdf.parse(sdf.format(new Date()));
			Timestamp date = new Timestamp(date2.getTime());
			String sowAlertQuery = "SELECT * FROM Sow_Details where DATE(sow_start_date) <= :sow_start_date AND sow_status = :sow_status";
			sowAlertlist = session.createSQLQuery(sowAlertQuery).addEntity(Sow_Details.class)
					.setParameter("sow_start_date", date).setParameter("sow_status", "renewal").list();
			Sow_Details sowDetails = null;
			ArrayList<Object> sowlists = new ArrayList<Object>();
			for (Object obj : sowList) {
				sowDetails = (Sow_Details) obj;
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("sowDetailsId", sowDetails.getSowDetailsId());
				map.put("sowClientReferenceNumber", sowDetails.getSowClientReferenceNumber());
				map.put("sowstatus", sowDetails.getSowStatus());
				map.put("heliusReferenceNumber", sowDetails.getHeliusReferenceNumber());
				sowlists.add(map);
			}
			ArrayList<Object> sowAlertlists = new ArrayList<Object>();
			for (Object obj : sowAlertlist) {
				sowDetails = (Sow_Details) obj;
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("sowDetailsId", sowDetails.getSowDetailsId());
				map.put("heliusReferenceNumber", sowDetails.getHeliusReferenceNumber());
				map.put("sow_start_date", sowDetails.getSowStartDate());
				map.put("sow_expiry_date", sowDetails.getSowExpiryDate());
				sowAlertlists.add(map);
			}
			
			Employee_Personal_Details empPersonDetails = null;
			ArrayList<Object> emplists = new ArrayList<Object>();
			/*
			 * Collections.sort(empList,new
			 * Comparator<Employee_Personal_Details>() {
			 * 
			 * @Override public int compare(Employee_Personal_Details o1,
			 * Employee_Personal_Details o2) { // TODO Auto-generated method
			 * stub return
			 * o1.getEmployee_name().compareTo(o2.getEmployee_name()); } });
			 */
			for (Object obj : empList) {
				empPersonDetails = (Employee_Personal_Details) obj;
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("employee_id", empPersonDetails.getEmployee_id());
				map.put("employee_name", empPersonDetails.getEmployee_name());
				emplists.add(map);
			}
			ObjectMapper om = new ObjectMapper();
			try {
				sowpicklist = om.writeValueAsString(sowlists);
				emppicklist = om.writeValueAsString(emplists);
				sowalertpicklist = om.writeValueAsString(sowAlertlists);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.close();
		}
		return "{\"sowpicklist\" : " + sowpicklist + ",\"emppicklist\" : " + emppicklist + ",\"sowalertpicklist\" : " + sowalertpicklist + "}";
	}

	@Override
	public List<Sowdashboard> getAllSow() throws Throwable {
		Session session = null;
		Query sowlist = null;
		SQLQuery sowBillinglist = null;
		String sowjson = "";
		List<Sowdashboard> results = null;
		try {
			session = sessionFactory.openSession();
			//String sowAss_query = "SELECT a.*,b.margin,c.employee_id FROM Sow_Details a LEFT JOIN (SELECT SUM(margin) AS margin,helius_reference_number FROM  Sow_Billing_Schedule GROUP BY helius_reference_number) b ON a.helius_reference_number=b.helius_reference_number LEFT JOIN Sow_Employee_Association c ON a.helius_reference_number=c.helius_reference_number";
			
			/*String sowAss_query = "SELECT a.*,b.margin,e.employee_id,e.offer_id,e.employee_status,e.relieving_date FROM Sow_Details a LEFT JOIN (SELECT SUM(margin) AS margin,helius_reference_number FROM  Sow_Billing_Schedule GROUP BY helius_reference_number) b ON a.helius_reference_number=b.helius_reference_number LEFT JOIN"
					+  "(SELECT c.employee_id,c.offer_id,c.helius_reference_number,d.employee_status,d.relieving_date FROM Sow_Employee_Association c LEFT JOIN Employee_Personal_Details d ON c.employee_id=d.employee_id) e ON a.helius_reference_number=e.helius_reference_number";
			*/
			String sowAss_query = "SELECT * from Sow_Billing_Assoc_Dashboard";
			sowlist = session.createSQLQuery(sowAss_query)
					.setResultTransformer(Transformers.aliasToBean(Sowdashboard.class));
			results = sowlist.list();
			/*
			 * String sowBilli_query =
			 * "SELECT a.*,SUM(b.margin) FROM Sow_Details a LEFT JOIN   Sow_Billing_Schedule b ON a.helius_reference_number=b.helius_reference_number  GROUP BY a.helius_reference_number"
			 * ; sowBillinglist =
			 * session.createSQLQuery(sowBilli_query).addEntity(Sow_Details.
			 * class).addJoin("b", "a.sow_details_id"); List<Object[]> rows =
			 * sowBillinglist.list(); for (Object[] row : rows) { Sow_Details e
			 * = (Sow_Details) row[0];
			 * System.out.println("a Info::"+e.getHeliusReferenceNumber());
			 * Sow_Billing_Schedule a = (Sow_Billing_Schedule) row[1];
			 * System.out.println("b Info::"+a.getMargin()); }
			 */
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			throw new Throwable("Failed to fetch  SOW Details ");
		}
		return results;
	}

	// Retrieve the sow details and assigned employees to sow
	/*
	 * @Override public Sow_Details getSowDetails(String sowDetailsId) throws
	 * Throwable { Sow_Details sowDetails = null; Session session = null;
	 * session = sessionFactory.openSession(); try { String sow_details_query =
	 * "select * from Sow_Details where sow_details_id = :sow_details_id";
	 * java.util.List sowlist =
	 * session.createSQLQuery(sow_details_query).addEntity(Sow_Details.class)
	 * .setParameter("sow_details_id", sowDetailsId).list(); if
	 * (!sowlist.isEmpty()) { sowDetails = (Sow_Details)
	 * sowlist.iterator().next(); } if (sowDetails != null) {
	 * List<Sow_Details_History> historySow =
	 * sowDetails.getSowDetailsHistories(); if (historySow != null) {
	 * Iterator<Sow_Details_History> itr =
	 * sowDetails.getSowDetailsHistories().iterator(); List<Sow_Details_History>
	 * SowDetailsHistoryList = new ArrayList<Sow_Details_History>(); while
	 * (itr.hasNext()) { Sow_Details_History sowHist = itr.next();
	 * List<Sow_Employee_Association> sowHistoryAssoc =
	 * sowDetails.getSowEmpAssoc(); List<Sow_Employee_Association>
	 * Sow_HisAssocList = new ArrayList<Sow_Employee_Association>(); for
	 * (Sow_Employee_Association sow_hisAssoc : sowHistoryAssoc) {
	 * if(sow_hisAssoc.getHeliusReferenceNumber().equalsIgnoreCase(sowHist.
	 * getHeliusReferenceNumber())){ Sow_HisAssocList.add(sow_hisAssoc); } }
	 * sowHist.setSowHistoryEmpAssoc(Sow_HisAssocList);
	 * SowDetailsHistoryList.add(sowHist); }
	 * sowDetails.setSowDetailsHistories(SowDetailsHistoryList); } } //
	 * Sow_Employee_Association sowAssoc=null;
	 * Iterator<Sow_Employee_Association> itr =
	 * sowDetails.getSowEmpAssoc().iterator(); while (itr.hasNext()) {
	 * Sow_Employee_Association sowAssoc = itr.next();
	 * if(sowAssoc.getSowClientReferenceNumber() != null &&
	 * !sowAssoc.getSowClientReferenceNumber().equalsIgnoreCase(sowDetails.
	 * getSowClientReferenceNumber())){ itr.remove(); }
	 * if(sowAssoc.getHeliusReferenceNumber() != null &&
	 * !sowAssoc.getHeliusReferenceNumber().equalsIgnoreCase(sowDetails.
	 * getHeliusReferenceNumber())){ itr.remove(); } }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); throw new Throwable("Failed to fetch  SOW Details");
	 * }finally { session.close(); } return sowDetails; }
	 */
	
	public List<Sow_Details> getListOfSowDetails(String sowList) throws Throwable {
		ObjectMapper om = new ObjectMapper();
		ArrayList<Sow_Details> listOfSows = new ArrayList<Sow_Details>();
		Sow_Details sowDetails = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			ArrayList<Integer> sows = new ArrayList<Integer>();
			sows = om.readValue(sowList,ArrayList.class);
			for(int sowDetailsId : sows){
				sowDetails = getSowDetails(String.valueOf(sowDetailsId));
				listOfSows.add(sowDetails);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Throwable("Failed to fetch SOW Details");
		} finally {
			session.close();
		}
		return listOfSows;
	}
	
	@Override
	public Sow_Details getSowDetails(String sowDetailsId) throws Throwable {
		Sow_Details sowDetails = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			String sow_details_query = "select * from Sow_Details where sow_details_id = :sow_details_id";
			java.util.List sowlist = session.createSQLQuery(sow_details_query).addEntity(Sow_Details.class)
					.setParameter("sow_details_id", sowDetailsId).list();
			if (!sowlist.isEmpty()) {
				sowDetails = (Sow_Details) sowlist.iterator().next();
			}
			if (sowDetails != null) {
				List<Sow_Details_History> historySow = sowDetails.getSowDetailsHistories();
				if (historySow != null) {
					Iterator<Sow_Details_History> itr = sowDetails.getSowDetailsHistories().iterator();
					List<Sow_Details_History> SowDetailsHistoryList = new ArrayList<Sow_Details_History>();
					while (itr.hasNext()) {
						Sow_Details_History sowHist = itr.next();
						List<Sow_Employee_Association> sowHistoryAssoc = sowDetails.getSowEmpAssoc();
						ArrayList<Sow_Employee_Association> Sow_HisAssocList = new ArrayList<Sow_Employee_Association>();
						for (Sow_Employee_Association sow_hisAssoc : sowHistoryAssoc) {
							if (sow_hisAssoc.getHeliusReferenceNumber() != null && sow_hisAssoc
									.getHeliusReferenceNumber().equalsIgnoreCase(sowHist.getHeliusReferenceNumber())) {
								Sow_HisAssocList.add(sow_hisAssoc);
							}
						}
						sowHist.setSowHistoryEmpAssoc(Sow_HisAssocList);
						List<Sow_Billing_Schedule> sowhisbillSch = sowDetails.getSowBillingSchedule();
						List<Sow_Billing_Schedule> sow_hisbillSchList = new ArrayList<Sow_Billing_Schedule>();
						for (Sow_Billing_Schedule sow_hisBilling : sowhisbillSch) {
							if (sow_hisBilling.getHeliusReferenceNumber() != null && sow_hisBilling
									.getHeliusReferenceNumber().equalsIgnoreCase(sowHist.getHeliusReferenceNumber())) {
								sow_hisbillSchList.add(sow_hisBilling);
							}
						}
						sowHist.setSowHistoryBillingSchedule(sow_hisbillSchList);
						SowDetailsHistoryList.add(sowHist);
					}
					sowDetails.setSowDetailsHistories(SowDetailsHistoryList);
				}
				if (sowDetails.getSowEmpAssoc() != null) {
					Iterator<Sow_Employee_Association> itr = sowDetails.getSowEmpAssoc().iterator();
					while (itr.hasNext()) {
						Sow_Employee_Association sowAssoc = itr.next();
						/*
						 * if(sowAssoc.getSowClientReferenceNumber() != null &&
						 * !sowAssoc.getSowClientReferenceNumber().
						 * equalsIgnoreCase(sowDetails.
						 * getSowClientReferenceNumber())){ itr.remove(); }
						 */
						if (sowAssoc.getHeliusReferenceNumber() != null && !sowAssoc.getHeliusReferenceNumber()
								.equalsIgnoreCase(sowDetails.getHeliusReferenceNumber())) {
							itr.remove();
						}
					}
				}
				if (sowDetails.getSowBillingSchedule() != null) {
					Iterator<Sow_Billing_Schedule> itrBilling = sowDetails.getSowBillingSchedule().iterator();
					while (itrBilling.hasNext()) {
						Sow_Billing_Schedule sowBillingSch = itrBilling.next();
						if (sowBillingSch.getHeliusReferenceNumber() != null && !sowBillingSch
								.getHeliusReferenceNumber().equalsIgnoreCase(sowDetails.getHeliusReferenceNumber())) {
							itrBilling.remove();
						}
					}
				}
			}
			// Sow_Employee_Association sowAssoc=null;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Throwable("Failed to fetch  SOW Details");
		} finally {
			session.close();
		}
		return sowDetails;
	}
	@Override
	public void updateSOWResolveStatus(JSONObject Json) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String SowId = null;
			if(Json.get("sow_details_id")!=null){
				SowId = Json.get("sow_details_id").toString();
			}
			String types_of_conflict = null;
			if(Json.get("types_of_conflict")!=null){
				types_of_conflict = Json.get("types_of_conflict").toString();
			}
			String resolved_status = null;
			if(Json.get("resolved_status")!=null){
				resolved_status = Json.get("resolved_status").toString();
			}
			String remarks = null;
			if(Json.get("remarks")!=null){
				remarks = Json.get("remarks").toString();
			}
		Sow_Details sow = getSowDetails(SowId);
		sow.setTypesOfConflict(types_of_conflict);
		sow.setRemarks(remarks);
		sow.setResolvedStatus(resolved_status);
		session.update(sow);
		transaction.commit();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to Update Resolve status" + e.getCause().getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void saveorUpdateSOW(Sow sows, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> fileFolder = new HashMap<String, String>();
		Object sowId = null;
		try {
			Sow_Details sow = sows.getSowdetails();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (sow != null) {
				if (sow.getSowDetailsId() == 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date1 = sdf.parse(sdf.format(new Date()));
					Timestamp date = null;
					date = new Timestamp(date1.getTime());
					if("addendum".equalsIgnoreCase(sow.getSowStatus())){
						Sow_Details setSowfutureField = getSowDetails(sow.getPrevious_sownumber());
						String futureoraddendumid =  setSowfutureField.getFutureOrAdendumSowId();
						if(futureoraddendumid==null || futureoraddendumid.isEmpty()){
						setSowfutureField.setFutureOrAdendumSowId(sow.getHeliusReferenceNumber());
						}else{
							setSowfutureField.setFutureOrAdendumSowId(futureoraddendumid+"#"+sow.getHeliusReferenceNumber());
						}
						session.update(setSowfutureField);
						if(sow.getSowStartDate().equals(date) || sow.getSowStartDate().before(date)){
							sow.setSowStatus("active");
						}
					}					
					sowId = session.save(sow);			
					if (sow.getSowPath() != null && !sow.getSowPath().isEmpty()) {
						String modifiedFileName = sow.getHeliusReferenceNumber() + "_" + sow.getSowPath();
						templateFilenames.put(sow.getSowPath(), modifiedFileName);
						fileFolder.put(sow.getSowPath(), "sow");
					}
					if (sow.getPoPath() != null && !sow.getPoPath().isEmpty()) {
						String modifiedFileName = sow.getHeliusReferenceNumber() + "_" + sow.getPoPath();
						templateFilenames.put(sow.getPoPath(), modifiedFileName);
						fileFolder.put(sow.getPoPath(), "po");
					}
					List<Sow_Employee_Association> assoc = sow.getSowEmpAssoc();
					if (assoc != null) {
						Iterator<Sow_Employee_Association> itr = assoc.iterator();
						while (itr.hasNext()) {
							Sow_Employee_Association sowAssoc = itr.next();
							sowAssoc.setSowDetailsId(sow.getSowDetailsId());
							session.save(sowAssoc);
						}
					}
					List<Sow_Billing_Schedule> billingSch = sow.getSowBillingSchedule();
					if (billingSch != null) {
						Iterator<Sow_Billing_Schedule> itr = billingSch.iterator();
						while (itr.hasNext()) {
							Sow_Billing_Schedule sowBillSchedule = itr.next();
							sowBillSchedule.setSowDetailsId(sow.getSowDetailsId());
							session.save(sowBillSchedule);
						}
					}
					Map<String, MultipartFile> files = null;
					files = request.getFileMap();
					if (files.size() > 0) {
						FilecopyStatus status = Utils.copySowFiles(request, templateFilenames, fileFolder);
						copied_with_success = status.getCopied_with_success();
					}
					transaction.commit();
					if("renewal".equalsIgnoreCase(sow.getSowStatus())){
						if(sow.getSowStartDate().equals(date) || sow.getSowStartDate().before(date)){
							if(!"no".equalsIgnoreCase(sow.getSow_override())){
							convertFutureSowToActive(Integer.toString(sow.getSowDetailsId()),sow.getPrevious_sownumber());
							}
						}
					}
				} else {
					// session.evict(sow);
					/*if ("addendum".equalsIgnoreCase(sow.getSowStatus())) {
						String sowdetailId = Integer.toString(sow.getSowDetailsId());
						sowRenewal(sowdetailId, sow.getSowStatus(), session);
						sow.setSowStatus("active");
					}*/
					session.update(sow);
					if (sow.getSowPath() != null && !sow.getSowPath().isEmpty()) {
						String modifiedFileName = sow.getHeliusReferenceNumber() + "_" + sow.getSowPath();
						templateFilenames.put(sow.getSowPath(), modifiedFileName);
						fileFolder.put(sow.getSowPath(), "sow");
					}
					if (sow.getPoPath() != null && !sow.getPoPath().isEmpty()) {
						String modifiedFileName = sow.getHeliusReferenceNumber() + "_" + sow.getPoPath();
						templateFilenames.put(sow.getPoPath(), modifiedFileName);
						fileFolder.put(sow.getPoPath(), "po");
					}
					List<Sow_Employee_Association> checks = sow.getSowEmpAssoc();
					if (checks != null && !checks.isEmpty()) {
						Iterator<Sow_Employee_Association> itr = sow.getSowEmpAssoc().iterator();
						while (itr.hasNext()) {
							Sow_Employee_Association sowAssoc = itr.next();
							sowAssoc.setSowDetailsId(sow.getSowDetailsId());
							if ("expired".equalsIgnoreCase(sow.getSowStatus())) {
								sowAssoc.setStatus("inactive");
							}
							if ("early_termination".equalsIgnoreCase(sow.getSowStatus())) {
								sowAssoc.setStatus("inactive");
							}
							session.evict(sowAssoc);
							session.merge(sowAssoc);
						}
					}
					List<Sow_Billing_Schedule> billingSchupdate = sow.getSowBillingSchedule();
					if (billingSchupdate != null && !billingSchupdate.isEmpty()) {
						Iterator<Sow_Billing_Schedule> itr = billingSchupdate.iterator();
						while (itr.hasNext()) {
							Sow_Billing_Schedule sowBillUpd = itr.next();
							sowBillUpd.setSowDetailsId(sow.getSowDetailsId());
							session.evict(sowBillUpd);
							session.merge(sowBillUpd);
						}
					}
					List<Sow_Billing_Schedule> deleteBillingSchupdate = sows.getDeleteSowBillingSchedule();
					if (deleteBillingSchupdate != null && !deleteBillingSchupdate.isEmpty()) {
						Iterator<Sow_Billing_Schedule> itr = deleteBillingSchupdate.iterator();
						while (itr.hasNext()) {
							Sow_Billing_Schedule deleteBilling = itr.next();
							session.delete(deleteBilling);
						}
					}
					Map<String, MultipartFile> files = null;
					files = request.getFileMap();
					if (files.size() > 0) {
						FilecopyStatus status = Utils.copySowFiles(request, templateFilenames, fileFolder);
						copied_with_success = status.getCopied_with_success();
					}
					transaction.commit();
				}
				/*Map<String, MultipartFile> files = null;
				files = request.getFileMap();
				if (files.size() > 0) {
					FilecopyStatus status = Utils.copyFiles(request, templateFilenames, fileFolder);
					copied_with_success = status.getCopied_with_success();
				}
				transaction.commit();*/
			} else {
				throw new Throwable("Failed to Save the SOW Details");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to Save the SOW Details" + e.getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to Save the SOW Details" + e.getCause().getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void convertFutureSowToActive(String futureSowDetailsId, String activeDetailsId) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		Session session2 = null;
		Transaction transaction2 = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			sowRenewal(activeDetailsId, "expired", session);
			Sow_Details futureDetails = getSowDetails(futureSowDetailsId);
			List<Sow_Billing_Schedule> billingSchupdate = futureDetails.getSowBillingSchedule();
			if (billingSchupdate != null && !billingSchupdate.isEmpty()) {
				Iterator<Sow_Billing_Schedule> itr = billingSchupdate.iterator();
				while (itr.hasNext()) {
					Sow_Billing_Schedule sowBillUpd = itr.next();
					sowBillUpd.setSowDetailsId(Integer.parseInt(activeDetailsId));
					session.evict(sowBillUpd);
					session.merge(sowBillUpd);
				}
			}
			List<Sow_Employee_Association> checks = futureDetails.getSowEmpAssoc();
			if (checks != null && !checks.isEmpty()) {
				Iterator<Sow_Employee_Association> itr = checks.iterator();
				while (itr.hasNext()) {
					Sow_Employee_Association sowAssoc = itr.next();
					sowAssoc.setSowDetailsId(Integer.parseInt(activeDetailsId));
					session.evict(sowAssoc);
					session.merge(sowAssoc);
				}
			}
			int deleteSowId = futureDetails.getSowDetailsId();
			futureDetails.setSowDetailsId(Integer.parseInt(activeDetailsId));
			futureDetails.setSowStatus("active");
			futureDetails.setPrevious_sownumber(null);
			session.update(futureDetails);
			Sow_Details delSow = getSowDetails(Integer.toString(deleteSowId));
			delSow.setSowStatus("readyToDelete");
			session.update(delSow);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		}
		try {
			session2 = sessionFactory.openSession();
			transaction2 = session2.beginTransaction();
			String sowExpire_query = "SELECT * FROM Sow_Details where sow_status = :sow_status";
			List sowDeletelist = session2.createSQLQuery(sowExpire_query).addEntity(Sow_Details.class)
					.setParameter("sow_status", "readyToDelete").list();
			for (Object obj : sowDeletelist) {
				Sow_Details sow = (Sow_Details) obj;
				session2.delete(sow);
			}
			transaction2.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Sow Activated Successfully !! Failed to delete the backend Temporary Record !! ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Sow Activated Successfully !! Failed to delete the backend Temporary Record !!");
		} finally {
			session.close();
			session2.close();
		}
	}
	
	public Sow_Details_History setSowHistoryFields(Sow_Details prevDetails,String status){
		Sow_Details_History history = new Sow_Details_History();
		history.setAssignSowToEmployee(prevDetails.getAssignSowToEmployee());
		history.setCurrency(prevDetails.getCurrency());
		history.setEmployeesLeavesBillable(prevDetails.getEmployeesLeavesBillable());
		history.setHeliusReferenceNumber(prevDetails.getHeliusReferenceNumber());
		history.setPoEndDate(prevDetails.getPoEndDate());
		history.setPoStartDate(prevDetails.getPoStartDate());
		history.setRealizedSowValue(prevDetails.getRealizedSowValue());
		history.setRenewalStatusDescription(prevDetails.getRenewalStatusDescription());
		history.setSowClientReferenceNumber(prevDetails.getSowClientReferenceNumber());
		history.setSowDetailsId(prevDetails.getSowDetailsId());
		history.setSowExpiryDate(prevDetails.getSowExpiryDate());
		history.setSowQuantity(prevDetails.getSowQuantity());
		history.setSowStatus(status);
		history.setHeliusAccountManager(prevDetails.getHeliusAccountManager());
		history.setSowResources(prevDetails.getSowResources());
		history.setSowClient(prevDetails.getSowClient());
		history.setSowClientGroup(prevDetails.getSowClientGroup());
		history.setPrevious_sownumber(prevDetails.getPrevious_sownumber());
		history.setClientContact(prevDetails.getClientContact());
		history.setPoPath(prevDetails.getPoPath());
		history.setClientContactType(prevDetails.getClientContactType());
		history.setBonusFrequency(prevDetails.getBonusFrequency());
		history.setBonusReimbursibleAmount(prevDetails.getBonusReimbursibleAmount());
		history.setBonusReimbursible(prevDetails.getBonusReimbursible());
		history.setSowPath(prevDetails.getSowPath());
		history.setSowInitialCost(prevDetails.getSowInitialCost());
		history.setSowInitialCostAmount(prevDetails.getSowInitialCostAmount());
		history.setSowRateForUnit(prevDetails.getSowRateForUnit());
		history.setSowRenewalStatus(prevDetails.getSowRenewalStatus());
		history.setSowStartDate(prevDetails.getSowStartDate());
		history.setSowTotalValue(prevDetails.getSowTotalValue());
		history.setSowType(prevDetails.getSowType());
		history.setUnrealizedSowValue(prevDetails.getUnrealizedSowValue());
		history.setPoNumber(prevDetails.getPoNumber());
		history.setCreated_by(prevDetails.getCreated_by());
		history.setCreate_date(prevDetails.getCreate_date());
		history.setLast_modified_by(prevDetails.getLast_modified_by());
		history.setLast_modified_date(prevDetails.getLast_modified_date());
		history.setForce_closure_date(prevDetails.getForce_closure_date());
		history.setForce_closure_reason(prevDetails.getForce_closure_reason());
		history.setTypesOfConflict(prevDetails.getTypesOfConflict());
		history.setResolvedStatus(prevDetails.getResolvedStatus());
		history.setRemarks(prevDetails.getRemarks());
		history.setFutureOrAdendumSowId(prevDetails.getFutureOrAdendumSowId());
		return history;
	}
	
	public void sowRenewal(String sowId, String status, Session session) throws Throwable {
		Sow_Details_History history = new Sow_Details_History();
		try {
			if (sowId != null) {
				Sow_Details prevDetails = getSowDetails(sowId);
				if (prevDetails != null) {
					history = setSowHistoryFields(prevDetails,status);
					session.save(history);
					List<Sow_Employee_Association> checks = prevDetails.getSowEmpAssoc();
					if (checks != null) {
						Iterator<Sow_Employee_Association> itr = prevDetails.getSowEmpAssoc().iterator();
						while (itr.hasNext()) {
							Sow_Employee_Association sowAssoc = itr.next();
							if (prevDetails.getHeliusReferenceNumber() != null
									&& sowAssoc.getHeliusReferenceNumber() != null) {
								if (prevDetails.getHeliusReferenceNumber()
										.equalsIgnoreCase(sowAssoc.getHeliusReferenceNumber())) {
									sowAssoc.setStatus("inactive");
									session.evict(sowAssoc);
									session.merge(sowAssoc);
								}
							}
						}
					}
					if(prevDetails.getFutureOrAdendumSowId()!=null && !prevDetails.getFutureOrAdendumSowId().isEmpty()){
						String futureoradendumid = prevDetails.getFutureOrAdendumSowId();
						String[] futureoradendumids = futureoradendumid.split("#");
						for(String futureoraddendumid : futureoradendumids){
							Sow_Details sw = getSowByHeliusReferenceNum(futureoraddendumid);
							Sow_Details_History	addenhistory = setSowHistoryFields(sw,status);
							addenhistory.setSowDetailsId(prevDetails.getSowDetailsId());
							session.save(addenhistory);
							sw.setSowStatus("readyToDelete");
							session.update(sw);
							List<Sow_Billing_Schedule> futoraddenbillingSchupdate = sw.getSowBillingSchedule();
							if (futoraddenbillingSchupdate != null && !futoraddenbillingSchupdate.isEmpty()) {
								Iterator<Sow_Billing_Schedule> itr2 = futoraddenbillingSchupdate.iterator();
								while (itr2.hasNext()) {
									Sow_Billing_Schedule sowBillUpd = itr2.next();
									sowBillUpd.setSowDetailsId(prevDetails.getSowDetailsId());
									session.evict(sowBillUpd);
									session.merge(sowBillUpd);
								}
							}
							List<Sow_Employee_Association> addenAssocChecks = sw.getSowEmpAssoc();
							if (addenAssocChecks != null) {
								Iterator<Sow_Employee_Association> itr = addenAssocChecks.iterator();
								while (itr.hasNext()) {
									Sow_Employee_Association sowAssoc = itr.next();
									if (sw.getHeliusReferenceNumber() != null
											&& sowAssoc.getHeliusReferenceNumber() != null) {
										if (sw.getHeliusReferenceNumber()
												.equalsIgnoreCase(sowAssoc.getHeliusReferenceNumber())) {
											sowAssoc.setStatus("inactive");
											session.evict(sowAssoc);
											session.merge(sowAssoc);
										}
									}
								}
							}
						}
					}
					
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		}
	}

	
	/*@Override
	public void convertFutureSowToActive(String futureSowDetailsId, String activeDetailsId) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		Session session2 = null;
		Transaction transaction2 = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			sowRenewal(activeDetailsId, "expired", session);
			Sow_Details futureDetails = getSowDetails(futureSowDetailsId);
			List<Sow_Billing_Schedule> billingSchupdate = futureDetails.getSowBillingSchedule();
			if (billingSchupdate != null && !billingSchupdate.isEmpty()) {
				Iterator<Sow_Billing_Schedule> itr = billingSchupdate.iterator();
				while (itr.hasNext()) {
					Sow_Billing_Schedule sowBillUpd = itr.next();
					sowBillUpd.setSowDetailsId(Integer.parseInt(activeDetailsId));
					session.evict(sowBillUpd);
					session.merge(sowBillUpd);
				}
			}
			List<Sow_Employee_Association> checks = futureDetails.getSowEmpAssoc();
			if (checks != null && !checks.isEmpty()) {
				Iterator<Sow_Employee_Association> itr = checks.iterator();
				while (itr.hasNext()) {
					Sow_Employee_Association sowAssoc = itr.next();
					sowAssoc.setSowDetailsId(Integer.parseInt(activeDetailsId));
					session.evict(sowAssoc);
					session.merge(sowAssoc);
				}
			}
			int deleteSowId = futureDetails.getSowDetailsId();
			futureDetails.setSowDetailsId(Integer.parseInt(activeDetailsId));
			futureDetails.setSowStatus("active");
			futureDetails.setPrevious_sownumber(null);
			session.update(futureDetails);
			Sow_Details delSow = getSowDetails(Integer.toString(deleteSowId));
			delSow.setSowStatus("readyToDelete");
			session.update(delSow);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		}
		try {
			session2 = sessionFactory.openSession();
			transaction2 = session2.beginTransaction();
			String sowExpire_query = "SELECT * FROM Sow_Details where sow_status = :sow_status";
			List sowDeletelist = session2.createSQLQuery(sowExpire_query).addEntity(Sow_Details.class)
					.setParameter("sow_status", "readyToDelete").list();
			for (Object obj : sowDeletelist) {
				Sow_Details sow = (Sow_Details) obj;
				session2.delete(sow);
			}
			transaction2.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Sow Activated Successfully !! Failed to delete the backend Temporary Record !! ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Sow Activated Successfully !! Failed to delete the backend Temporary Record !!");
		} finally {
			session.close();
			session2.close();
		}
	}*/

	/*public void sowRenewal(String sowId, String status, Session session) throws Throwable {
		Sow_Details_History history = new Sow_Details_History();
		try {
			if (sowId != null) {
				Sow_Details prevDetails = getSowDetails(sowId);
				if (prevDetails != null) {
					history.setAssignSowToEmployee(prevDetails.getAssignSowToEmployee());
					history.setCurrency(prevDetails.getCurrency());
					history.setEmployeesLeavesBillable(prevDetails.getEmployeesLeavesBillable());
					history.setHeliusReferenceNumber(prevDetails.getHeliusReferenceNumber());
					history.setPoEndDate(prevDetails.getPoEndDate());
					history.setPoStartDate(prevDetails.getPoStartDate());
					history.setRealizedSowValue(prevDetails.getRealizedSowValue());
					history.setRenewalStatusDescription(prevDetails.getRenewalStatusDescription());
					history.setSowClientReferenceNumber(prevDetails.getSowClientReferenceNumber());
					history.setSowDetailsId(prevDetails.getSowDetailsId());
					history.setSowExpiryDate(prevDetails.getSowExpiryDate());
					history.setSowQuantity(prevDetails.getSowQuantity());
					history.setSowStatus(status);
					history.setHeliusAccountManager(prevDetails.getHeliusAccountManager());
					history.setSowResources(prevDetails.getSowResources());
					history.setSowClient(prevDetails.getSowClient());
					history.setSowClientGroup(prevDetails.getSowClientGroup());
					history.setPrevious_sownumber(prevDetails.getPrevious_sownumber());
					history.setClientContact(prevDetails.getClientContact());
					history.setPoPath(prevDetails.getPoPath());
					history.setClientContactType(prevDetails.getClientContactType());
					history.setBonusFrequency(prevDetails.getBonusFrequency());
					history.setBonusReimbursibleAmount(prevDetails.getBonusReimbursibleAmount());
					history.setBonusReimbursible(prevDetails.getBonusReimbursible());
					history.setSowPath(prevDetails.getSowPath());
					history.setSowInitialCost(prevDetails.getSowInitialCost());
					history.setSowInitialCostAmount(prevDetails.getSowInitialCostAmount());
					history.setSowRateForUnit(prevDetails.getSowRateForUnit());
					history.setSowRenewalStatus(prevDetails.getSowRenewalStatus());
					history.setSowStartDate(prevDetails.getSowStartDate());
					history.setSowTotalValue(prevDetails.getSowTotalValue());
					history.setSowType(prevDetails.getSowType());
					history.setUnrealizedSowValue(prevDetails.getUnrealizedSowValue());
					history.setPoNumber(prevDetails.getPoNumber());
					history.setCreated_by(prevDetails.getCreated_by());
					history.setCreate_date(prevDetails.getCreate_date());
					history.setLast_modified_by(prevDetails.getLast_modified_by());
					history.setLast_modified_date(prevDetails.getLast_modified_date());
					history.setForce_closure_date(prevDetails.getForce_closure_date());
					history.setForce_closure_reason(prevDetails.getForce_closure_reason());
					session.save(history);
					List<Sow_Employee_Association> checks = prevDetails.getSowEmpAssoc();
					if (checks != null) {
						Iterator<Sow_Employee_Association> itr = prevDetails.getSowEmpAssoc().iterator();
						while (itr.hasNext()) {
							Sow_Employee_Association sowAssoc = itr.next();
							if (prevDetails.getHeliusReferenceNumber() != null
									&& sowAssoc.getHeliusReferenceNumber() != null) {
								if (prevDetails.getHeliusReferenceNumber()
										.equalsIgnoreCase(sowAssoc.getHeliusReferenceNumber())) {
									sowAssoc.setStatus("inactive");
									session.evict(sowAssoc);
									session.merge(sowAssoc);
								}
							}
						}
					}
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW Status from Renewal To Active ");
		}
	}
*/
	public void convertRenewalSowToActive(String futureDetailsId, String activeDetailsId) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		System.out.println("====11111=2222=====");

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			System.out.println("====11111==3333====");

			sowRenewal(activeDetailsId, "expired", session);
			Sow_Details futureDetails = getSowDetails(futureDetailsId);
			List<Sow_Billing_Schedule> billingSchupdate = futureDetails.getSowBillingSchedule();
			if (billingSchupdate != null && !billingSchupdate.isEmpty()) {
				Iterator<Sow_Billing_Schedule> itr = billingSchupdate.iterator();
				while (itr.hasNext()) {
					Sow_Billing_Schedule sowBillUpd = itr.next();
					sowBillUpd.setSowDetailsId(Integer.parseInt(activeDetailsId));
					session.evict(sowBillUpd);
					session.merge(sowBillUpd);
				}
			}
			List<Sow_Employee_Association> checks = futureDetails.getSowEmpAssoc();
			if (checks != null && !checks.isEmpty()) {
				Iterator<Sow_Employee_Association> itr = checks.iterator();
				while (itr.hasNext()) {
					Sow_Employee_Association sowAssoc = itr.next();
					sowAssoc.setSowDetailsId(Integer.parseInt(activeDetailsId));
					session.evict(sowAssoc);
					session.merge(sowAssoc);
				}
			}
			int deleteSowId = futureDetails.getSowDetailsId();
			futureDetails.setSowDetailsId(Integer.parseInt(futureDetails.getPrevious_sownumber()));
			futureDetails.setSowStatus("active");
			futureDetails.setPrevious_sownumber(null);
			session.update(futureDetails);
			Sow_Details delSow = getSowDetails(Integer.toString(deleteSowId));
			delSow.setSowStatus("readyToDelete");
			session.update(delSow);
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW ");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to convert the SOW ");
		} finally {
			session.close();
		}
	}

	public Sow_Details getSowByHeliusReferenceNum(String heliusReferenceNum) throws Throwable {
		Sow_Details sowDetails = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			String sow_details_query = "select * from Sow_Details where helius_reference_number = :helius_reference_number";
			java.util.List sowlist = session.createSQLQuery(sow_details_query).addEntity(Sow_Details.class)
					.setParameter("helius_reference_number", heliusReferenceNum).list();
			if (!sowlist.isEmpty()) {
				sowDetails = (Sow_Details) sowlist.iterator().next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Throwable("Failed to fetch  SOW Details");
		} finally {
			session.close();
		}
		return sowDetails;
	}
	
	public Sow_Employee_Association getSowAssocByHeliusReferenceNum(String heliusReferenceNum) throws Exception {
		Sow_Employee_Association sowDetails = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			String sow_details_query = "select * from Sow_Employee_Association where helius_reference_number = :helius_reference_number";
			java.util.List sowlist = session.createSQLQuery(sow_details_query).addEntity(Sow_Employee_Association.class)
					.setParameter("helius_reference_number", heliusReferenceNum).list();
			if (!sowlist.isEmpty()) {
				sowDetails = (Sow_Employee_Association) sowlist.iterator().next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Failed to fetch  SOW Details");
		} finally {
			session.close();
		}
		return sowDetails;
	}
	
	@Scheduled(cron = "0 0 6 * * ?")
	public void runservice() throws Throwable {
		System.out.println("Running Sow Service job");
		Session session = null;
		Session session2 = null;
		Transaction transaction = null;
		List sowlist = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = sdf.parse(sdf.format(new Date()));
		Timestamp date = null;
		date = new Timestamp(date2.getTime());
		int expiryday = date.getDate() - 1;
		Timestamp expirationDate = new Timestamp(date.getTime());
		expirationDate.setDate(expiryday);
		HashMap<String, String> overrideProcessed = new HashMap<String, String>();
		HashMap<String, String> overrideNOtProcessed = new HashMap<String, String>();
		try {
			session = sessionFactory.openSession();
			String sow_query = "SELECT * FROM Sow_Details where DATE(sow_start_date) <= :sow_start_date AND sow_status = :sow_status";
			sowlist = session.createSQLQuery(sow_query).addEntity(Sow_Details.class)
					.setParameter("sow_start_date", date).setParameter("sow_status", "renewal").list();
			for (Object obj : sowlist) {
				Sow_Details sow = (Sow_Details) obj;
				if ("yes".equalsIgnoreCase(sow.getSow_override()) || sow.getSow_override() == null || "".equalsIgnoreCase(sow.getSow_override())) {
					if (sow.getPrevious_sownumber() != null && !sow.getPrevious_sownumber().isEmpty()) {
						try {
							convertRenewalSowToActive(Integer.toString(sow.getSowDetailsId()),
									sow.getPrevious_sownumber());
							overrideProcessed.put(Integer.toString(sow.getSowDetailsId()), sow.getPrevious_sownumber());
							System.out.println("===Override Yes==active sow" + sow.getPrevious_sownumber()
									+ "==with future sow==" + sow.getSowDetailsId());
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("===Issue in processing Override Yes==active sow" + sow.getPrevious_sownumber()
							+ "==with future sow==" + sow.getSowDetailsId());	
						} catch (Throwable e) {
							e.printStackTrace();
							System.out.println("===Issue in processing Override Yes==active sow" + sow.getPrevious_sownumber()
							+ "==with future sow==" + sow.getSowDetailsId());
						}
					}
				} else {
					if (sow.getPrevious_sownumber() != null && !"".equalsIgnoreCase(sow.getPrevious_sownumber())) {
						overrideNOtProcessed.put(sow.getPrevious_sownumber(), Integer.toString(sow.getSowDetailsId()));
						System.out.println("===Override No==active sow" + sow.getPrevious_sownumber()
								+ "==with future sow==" + sow.getSowDetailsId());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			session2 = sessionFactory.openSession();
			transaction = session2.beginTransaction();
			try {
				String sowAtcive_query = "SELECT * FROM Sow_Details where DATE(sow_start_date) <= :sow_start_date AND sow_status = :sow_status";
				List<Sow_Details> adendumlist = session2.createSQLQuery(sowAtcive_query).addEntity(Sow_Details.class)
						.setParameter("sow_start_date", date).setParameter("sow_status", "addendum").list();
				for (Sow_Details sow : adendumlist) {
					try {
						sow.setSowStatus("active");
						session2.update(sow);
						System.out.println("==SOW Activated==" + sow.getSowDetailsId());
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("==failed to active adendum ==" + sow.getSowDetailsId());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("==failed to activate adendum sows==");
			}
			
			try{
			String sowExpire_query = "SELECT * FROM Sow_Details where DATE(sow_expiry_date) <= :sow_expiry_date AND sow_status = :sow_status";
			List<Sow_Details> sowExplist = session2.createSQLQuery(sowExpire_query).addEntity(Sow_Details.class)
					.setParameter("sow_expiry_date", expirationDate).setParameter("sow_status", "active").list();
			for (Sow_Details sow : sowExplist) {
				try{
				if (overrideNOtProcessed.containsKey(sow.getSowDetailsId())) {
					continue;
				} else {
					sow.setSowStatus("expired");
					session2.update(sow);
					Sow_Employee_Association assosc = getSowAssocByHeliusReferenceNum(sow.getHeliusReferenceNumber());
					assosc.setStatus("inactive");
					session2.update(assosc);
				}
				System.out.println("==SOW Expired==" + sow.getSowDetailsId());
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("==failed to expire sow=="+sow.getSowDetailsId());				}
			}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("==failed to expire sows==");
			}
			
			String sowDel_query = "SELECT * FROM Sow_Details where sow_status = :sow_status";
			List sowDeletelist = session2.createSQLQuery(sowDel_query).addEntity(Sow_Details.class)
					.setParameter("sow_status", "readyToDelete").list();
			for (Object obj : sowDeletelist) {
				Sow_Details sow = (Sow_Details) obj;
				session2.delete(sow);
				System.out.println("==deleted sow whose status is readytodelete==" + sow.getSowDetailsId());
			}

			String sowForceClosure_query = "SELECT c.* FROM Employee_Personal_Details a LEFT JOIN Sow_Employee_Association b "
					+ "ON a.employee_id = b.employee_id LEFT JOIN Sow_Details c ON b.helius_reference_number = c.helius_reference_number "
					+ "WHERE a.employee_status = 'Exited' AND DATE(a.relieving_date) < DATE(c.po_end_date) AND (c.sow_status = 'active' OR c.sow_status = 'renewal')";
			List sowForceClosurelist = session2.createSQLQuery(sowForceClosure_query).addEntity(Sow_Details.class).list();
			for (Object obj : sowForceClosurelist) {
				Sow_Details sow = (Sow_Details) obj;
				sow.setSowStatus("force_closure");
				session2.update(sow);
				System.out.println("==sow force closed whose status is force_closure==" + sow.getSowDetailsId());
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
			if (session2.isOpen()) {
				session2.close();
			}
		}
	}

	@Override
	public ResponseEntity<byte[]> getSowFiles(String sowDetailsId, String filetype) {
		Sow_Details sow = null;
		HttpHeaders headers = new HttpHeaders();
		String url = null;
		ResponseEntity<byte[]> errorResponse = null;
		try {
			sow = getSowDetails(sowDetailsId);
			if (filetype.equalsIgnoreCase("sow")) {
				url = sow.getSowPath();
				if (url != null && !"".equalsIgnoreCase(url)) {
					url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator
							+ sow.getHeliusReferenceNumber() + "_" + url;
				}
			}
			if (filetype.equalsIgnoreCase("po")) {
				url = sow.getPoPath();
				if (url != null && !"".equalsIgnoreCase(url)) {
					url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator
							+ sow.getHeliusReferenceNumber() + "_" + url;
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			headers.add("fileExist", "no");
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
		byte[] files = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(url);
			files = IOUtils.toByteArray(fi);
			fi.close();
		} catch (Throwable e) {
			e.printStackTrace();
			headers.add("fileExist", "no");
			return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
		}
		headers.add("fileExist", "yes");
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, headers, HttpStatus.OK);
		return responseEntity;
	}

@Override
public ResponseEntity<byte[]> deleteSowPoFile(String sowDetailsId, String filetype) {
	Sow_Details sow = null;
	String url = null;
	ResponseEntity<byte[]> Response = null;
	Session session = null;
	Transaction transaction = null;
	try {
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		sow = getSowDetails(sowDetailsId);
		if (filetype.equalsIgnoreCase("sow")) {
			url = sow.getSowPath();
			if (url != null && !"".equalsIgnoreCase(url)) {
				url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator
						+ sow.getHeliusReferenceNumber() + "_" + url;
				sow.setSowPath(null);
				session.update(sow);
			}
		}
		if (filetype.equalsIgnoreCase("po")) {
			url = sow.getPoPath();
			if (url != null && !"".equalsIgnoreCase(url)) {
				url = Utils.getProperty("fileLocation") + File.separator + filetype + File.separator
						+ sow.getHeliusReferenceNumber() + "_" + url;
				sow.setPoPath(null);
				session.update(sow);
			}
		}
		if (url != null && !"".equalsIgnoreCase(url)) {
			File file = new File(url);
			if(file.exists()){
				file.delete();
			}
		}
		transaction.commit();
		Response = new ResponseEntity<byte[]>(HttpStatus.OK);
	} catch (Throwable e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
	}finally{
		session.close();
	}
	return Response;
}
}