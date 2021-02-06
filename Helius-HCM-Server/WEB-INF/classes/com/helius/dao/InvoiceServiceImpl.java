package com.helius.dao;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.InvoiceAnnexure;
import com.helius.entities.Sow_Details;
import com.helius.entities.Sow_Details_History;
import com.helius.service.EmailService;

public class InvoiceServiceImpl implements InvoiceService{

	private org.hibernate.internal.SessionFactoryImpl sessionFactory;

	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Autowired
	private EmailService emailService;
	
	final float maximumManDaysPerMonth = 21;
	public HashMap<String, EmpOnebankSowDetails> getEmpSowDetails(LocalDate invoicedate) throws Exception {
		Session session = null;
		List<EmpOnebankSowDetails> empSowList = null;
		List<EmpOnebankSowDetails> empSowHisList = null;
		HashMap<String, EmpOnebankSowDetails> onebankEmpAssocSowList = new HashMap<String, EmpOnebankSowDetails>();
		try {
			session = sessionFactory.openSession();
			//fetching employee sow and appraisal and assignment details who is from dah2
			String query = "SELECT a.employee_id,SUBSTRING(a.client_email_id, 1, LOCATE('@', a.client_email_id) - 1) AS onebankId,a.account_manager,b.actual_date_of_joining,b.relieving_date,d.sow_type,d.po_number,d.po_start_date,d.po_end_date,d.sow_rate_for_unit,e.helius_recruiter,f.current_monthly_basic,e.pc_code,f.new_monthly_basic "
					+ "FROM Employee_Assignment_Details a LEFT JOIN  Employee_Personal_Details b ON a.employee_id=b.employee_id INNER JOIN Sow_Employee_Association c ON a.employee_id = c.employee_id "
					+ "INNER JOIN Sow_Details d ON c.helius_reference_number=d.helius_reference_number LEFT JOIN Employee_Offer_Details e ON a.employee_id= e.employee_id  LEFT JOIN Employee_Appraisal_Details f ON a.employee_id = f.employee_id "
					+ "WHERE a.client_email_id IS NOT NULL AND a.client_email_id != '' AND  (b.employee_status = 'Active' OR (b.employee_status ='Exited' AND MONTH(b.relieving_date)>= :month AND YEAR(b.relieving_date)= :year)) "
					+ "AND a.client='DAH2'";
			Query empSowListQuery = session.createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(EmpOnebankSowDetails.class))
					.setParameter("month", invoicedate.getMonthValue()).setParameter("year", invoicedate.getYear());
			empSowList = empSowListQuery.list();
			for (EmpOnebankSowDetails obj : empSowList) {
				if (obj.getPo_start_date() != null && obj.getPo_end_date() != null) {
					LocalDate poStartDate = obj.getPo_start_date().toLocalDateTime().toLocalDate();
					LocalDate poEndDate = obj.getPo_end_date().toLocalDateTime().toLocalDate();
					// checking invoice month is exists between po start and end
					// date
					if ((invoicedate.with(lastDayOfMonth()).isAfter(poStartDate)
							|| invoicedate.with(lastDayOfMonth()).isEqual(poStartDate))
							&& (invoicedate.isBefore(poEndDate) || invoicedate.isEqual(poEndDate))) {
						if (!onebankEmpAssocSowList.containsKey(obj.getEmployee_id())) {
							onebankEmpAssocSowList.put(obj.getEmployee_id(), obj);
						}
					}
				}
			}

			String queryHistory = "SELECT a.employee_id,SUBSTRING(a.client_email_id, 1, LOCATE('@', a.client_email_id) - 1) AS onebankId,a.account_manager,d.po_number,d.po_start_date,d.po_end_date,d.sow_rate_for_unit,e.helius_recruiter,f.current_monthly_basic,f.new_monthly_basic "
					+ "FROM Employee_Assignment_Details a LEFT JOIN  Employee_Personal_Details b ON a.employee_id=b.employee_id LEFT JOIN Sow_Employee_Association c ON a.employee_id = c.employee_id "
					+ "LEFT JOIN Sow_Details_History d ON c.helius_reference_number=d.helius_reference_number LEFT JOIN Employee_Offer_Details e ON a.employee_id= e.employee_id  LEFT JOIN Employee_Appraisal_Details f ON a.employee_id = f.employee_id "
					+ "WHERE a.client_email_id IS NOT NULL AND a.client_email_id != '' AND  (b.employee_status = 'Active' OR (b.employee_status ='Exited' AND MONTH(b.relieving_date)>= :month AND YEAR(b.relieving_date)= :year)) "
					+ "AND a.client='DAH2'";
			Query empSowHisListQuery = session.createSQLQuery(queryHistory)
					.setResultTransformer(Transformers.aliasToBean(EmpOnebankSowDetails.class))
					.setParameter("month", invoicedate.getMonthValue()).setParameter("year", invoicedate.getYear());
			empSowHisList = empSowHisListQuery.list();

			for (EmpOnebankSowDetails obj : empSowHisList) {
				if (obj.getPo_start_date() != null && obj.getPo_end_date() != null) {
					LocalDate poStartDate = obj.getPo_start_date().toLocalDateTime().toLocalDate();
					LocalDate poEndDate = obj.getPo_end_date().toLocalDateTime().toLocalDate();
					// checking invoice month is exists between po start and end
					// date
					if (/*(invoicedate.with(lastDayOfMonth()).isAfter(poStartDate)
							|| invoicedate.with(lastDayOfMonth()).isEqual(poStartDate))
							&& */(invoicedate.isBefore(poEndDate) || invoicedate.isEqual(poEndDate))) {
						if (!onebankEmpAssocSowList.containsKey(obj.getEmployee_id())) {
							onebankEmpAssocSowList.put(obj.getEmployee_id(), obj);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Failed to fetch  employee sow details " + e.getMessage());
		} finally {
			session.close();
		}
		return onebankEmpAssocSowList;
	}
	
	public HashMap<String,String> getOnebankEmpAssoc(LocalDate invoicedate) throws Exception {
		Session session = null;
		List<Object[]> onebankEmpAssocList = null;
		HashMap<String,String> onebankEmpList = new HashMap<String,String>();
		try {
			session = sessionFactory.openSession();
			String query = "SELECT SUBSTRING(a.client_email_id, 1, LOCATE('@', a.client_email_id) - 1) AS onebankId,a.employee_id FROM Employee_Assignment_Details a LEFT JOIN  Employee_Personal_Details b ON a.employee_id=b.employee_id WHERE a.client_email_id IS NOT NULL AND a.client_email_id != '' AND  (b.employee_status = 'Active' OR (b.employee_status ='Exited' AND MONTH(b.relieving_date)>= :month AND YEAR(b.relieving_date)= :year)) AND (a.client='DBS' OR a.client='DAH2');";
			Query onebankEmpAssocQuery = session.createSQLQuery(query).setParameter("month",invoicedate.getMonthValue()).setParameter("year",invoicedate.getYear());
			onebankEmpAssocList = onebankEmpAssocQuery.list();
			session.close();
			for(Object[] obj :onebankEmpAssocList){
				onebankEmpList.put(obj[0].toString(),obj[1].toString());
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			throw new Exception("Failed to fetch onebank employee assosciation details " + e.getMessage());
		}
		return onebankEmpList;
	}
	
	public Sow_Details_History getSowHistoryByPO(String PO,String employee_id) throws Exception {
		Sow_Details_History sowDetails = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			String sow_details_query = "SELECT a.* FROM Sow_Details_History a INNER JOIN Sow_Employee_Association b ON a.helius_reference_number = b.helius_reference_number WHERE a.po_number= :po_number AND b.employee_id=:employee_id";
			java.util.List sowlist = session.createSQLQuery(sow_details_query).addEntity(Sow_Details_History.class)
					.setParameter("po_number", PO).setParameter("employee_id", employee_id).list();
			if (sowlist != null && !sowlist.isEmpty()) {
				sowDetails = (Sow_Details_History) sowlist.iterator().next();
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
	
	public Sow_Details getSowByPO(String PO,String employee_id) throws Exception {
		Sow_Details sowDetails = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			String sow_details_query = "SELECT a.* FROM Sow_Details a INNER JOIN Sow_Employee_Association b ON a.helius_reference_number = b.helius_reference_number WHERE a.po_number= :po_number AND b.employee_id=:employee_id";
			java.util.List sowlist = session.createSQLQuery(sow_details_query).addEntity(Sow_Details.class)
					.setParameter("po_number", PO).setParameter("employee_id", employee_id).list();
			if (sowlist != null && !sowlist.isEmpty()) {
				sowDetails = (Sow_Details) sowlist.iterator().next();
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
	
	@Override
	public String generateAnnexureForDah2(String month, String action, MultipartHttpServletRequest request)
			throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp invoiceMonth = null;
		Session session = null;
		Transaction transaction = null;
		String Response = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			java.util.Date selectedDate = sdf.parse(month);
			invoiceMonth = new Timestamp(selectedDate.getTime());
			LocalDate invoicedate = invoiceMonth.toLocalDateTime().toLocalDate();
			HashMap<String, String> empidmap = getOnebankEmpAssoc(invoicedate);
			HashMap<String, EmpOnebankSowDetails> empSowDetails = getEmpSowDetails(invoicedate);
			Set<String> issueInProcessing = new HashSet<String>();
			ArrayList<InvoiceAnnexure> processed = new ArrayList<InvoiceAnnexure>();
			MultipartFile mandayFile = request.getFile("mandayFile");
			InputStream xlsxContentStream = mandayFile.getInputStream();
			OPCPackage pkg = OPCPackage.open(xlsxContentStream);
			Workbook workbook = new XSSFWorkbook(pkg);
			Sheet sheet = workbook.getSheetAt(0);
			Row rowHeader = sheet.getRow(0);
			String noOfDaysColumn = null;
			String oneBankIDColumn = null;
			String poColumn = null;
			String nameColumn = null;

			int noOfDaysIndex = 0;
			int oneBankIDIndex = 0;
			int poIndex = 0;
			int nameIndex = 0;
			Iterator<Cell> cellIterator = rowHeader.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if ("1BankID".equalsIgnoreCase(cell.toString().trim())) {
					oneBankIDColumn = String.valueOf(cell.getColumnIndex());
					oneBankIDIndex = cell.getColumnIndex();
				}
				if ("Name".equalsIgnoreCase(cell.toString().trim())) {
					nameColumn = String.valueOf(cell.getColumnIndex());
					nameIndex = cell.getColumnIndex();
				}
				if ("No. of Days".equalsIgnoreCase(cell.toString().trim())) {
					noOfDaysColumn = String.valueOf(cell.getColumnIndex());
					noOfDaysIndex = cell.getColumnIndex();
				}
				if ("PO #".equalsIgnoreCase(cell.toString().trim())) {
					poColumn = String.valueOf(cell.getColumnIndex());
					poIndex = cell.getColumnIndex();
				}
			} // end of cell iterator
			if (oneBankIDColumn == null || nameColumn == null || noOfDaysColumn == null || poColumn == null) {
				throw new Throwable("Failed to process invoice annexure one or more  header column is not matching ");
			}
			HashMap<String, InvoiceAnnexure> multiplePO = new HashMap<String, InvoiceAnnexure>();
			float totalMandays = 0;
			float totalPo = 0;
			for (Row row : sheet) {
				if (row.getRowNum() > 0) {
					if (row.getCell(oneBankIDIndex) != null && !row.getCell(oneBankIDIndex).toString().isEmpty()) {
						String onebankId = row.getCell(oneBankIDIndex).toString().trim();
						if (empidmap.containsKey(onebankId)) {
							try {
								String name = row.getCell(nameIndex).toString();
								String days = row.getCell(noOfDaysIndex).toString().replaceAll("[^0-9.]", "");
								float noOfManDays = Float.parseFloat(days.trim());
								String employee_id = null;
								if (row.getCell(poIndex) == null || row.getCell(poIndex).toString().isEmpty()) {
									employee_id = empidmap.get(onebankId);
									InvoiceAnnexure invoiceAnnexureDAH2 = new InvoiceAnnexure();
									EmpOnebankSowDetails empSowDetail = empSowDetails.get(employee_id);
									if (empSowDetail != null) {
										invoiceAnnexureDAH2.setOnebankId(onebankId);
										invoiceAnnexureDAH2.setEmployee_id(empSowDetail.getEmployee_id());
										invoiceAnnexureDAH2.setEmployee_name(name);
										invoiceAnnexureDAH2.setDate_of_joining(empSowDetail.getActual_date_of_joining());
										invoiceAnnexureDAH2.setPo_number(empSowDetail.getPo_number());
										invoiceAnnexureDAH2.setPo_start_date(empSowDetail.getPo_start_date());
										invoiceAnnexureDAH2.setPc_code(empSowDetail.getPc_code());
										invoiceAnnexureDAH2.setPo_end_date(empSowDetail.getPo_end_date());
										invoiceAnnexureDAH2.setMonth(invoiceMonth);
										invoiceAnnexureDAH2.setSow_rate_for_unit(
												Float.parseFloat(empSowDetail.getSow_rate_for_unit().replace(",", "")));
										invoiceAnnexureDAH2.setAccount_manager(empSowDetail.getAccount_manager());
										invoiceAnnexureDAH2.setHelius_recruiter(empSowDetail.getHelius_recruiter());
										String ctc = null;
										if (empSowDetail.getNew_monthly_basic() != null
												&& !empSowDetail.getNew_monthly_basic().isEmpty()) {
											ctc = empSowDetail.getNew_monthly_basic();
										} else if (empSowDetail.getCurrent_monthly_basic() != null
												&& !empSowDetail.getCurrent_monthly_basic().isEmpty()) {
											ctc = empSowDetail.getCurrent_monthly_basic();
										} else {
											ctc = "0";
										}
										
										float monthlyCTC = Float.parseFloat(ctc.replace(",", ""));
										float atcualctc = monthlyCTC;
										if(hasJoinedthisMonth(empSowDetail, invoicedate)) {
											atcualctc = calculateCTC_joined(empSowDetail, monthlyCTC);
										}
										if(hasExitedthisMonth(empSowDetail, invoicedate)) {
											atcualctc = calculateCTC_exited(empSowDetail, monthlyCTC);
										}
										if(empSowDetail.getActual_date_of_joining().toLocalDateTime().toLocalDate().isAfter(invoicedate.with(lastDayOfMonth()))) {
											atcualctc = 0;
										}
										float totalRate = 0;
										if ("Mandays".equalsIgnoreCase(empSowDetail.getSow_type())
												|| "TM-Mandays".equalsIgnoreCase(empSowDetail.getSow_type())) {
											totalRate = invoiceAnnexureDAH2.getSow_rate_for_unit() * noOfManDays;
										}
										if ("Manmonth".equalsIgnoreCase(empSowDetail.getSow_type())
												|| "TM-Manmonths".equalsIgnoreCase(empSowDetail.getSow_type())) {
											totalRate = invoiceAnnexureDAH2.getSow_rate_for_unit()
													* (noOfManDays / maximumManDaysPerMonth);
										}
										invoiceAnnexureDAH2.setMonthly_ctc(atcualctc);
										invoiceAnnexureDAH2.setNoOfMandays(noOfManDays);
										invoiceAnnexureDAH2.setRateForTotalWorkedDays(totalRate);
										if (monthlyCTC == 0) {
											invoiceAnnexureDAH2.setMargin(0);
										} else {
											invoiceAnnexureDAH2.setMargin(totalRate - atcualctc);
										}
										totalMandays = totalMandays + invoiceAnnexureDAH2.getNoOfMandays();
										totalPo = totalPo + invoiceAnnexureDAH2.getRateForTotalWorkedDays();
										processed.add(invoiceAnnexureDAH2);
									} else {
										issueInProcessing.add(onebankId);
									}
								}
								// check for multiple po
								if (row.getCell(poIndex) != null && !row.getCell(poIndex).toString().isEmpty()) {
									employee_id = empidmap.get(onebankId);
									EmpOnebankSowDetails empSowDetail = empSowDetails.get(employee_id);
									InvoiceAnnexure invoiceAnnexureDAH2 = new InvoiceAnnexure();
									if (empSowDetail != null) {
										Sow_Details sowdetails = getSowByPO(row.getCell(poIndex).toString(),employee_id);
										if (sowdetails != null) {
											invoiceAnnexureDAH2.setPo_number(sowdetails.getPoNumber());
											invoiceAnnexureDAH2.setPo_start_date(sowdetails.getPoStartDate());
											invoiceAnnexureDAH2.setPo_end_date(sowdetails.getPoEndDate());
											invoiceAnnexureDAH2.setSow_rate_for_unit(
													Float.parseFloat(sowdetails.getSowRateForUnit().replace(",", "")));
										}
										if (sowdetails == null) {
											Sow_Details_History history = getSowHistoryByPO(
													row.getCell(poIndex).toString(),employee_id);
											if (history != null) {
												invoiceAnnexureDAH2.setPo_number(history.getPoNumber());
												invoiceAnnexureDAH2.setPo_start_date(history.getPoStartDate());
												invoiceAnnexureDAH2.setPo_end_date(history.getPoEndDate());
												invoiceAnnexureDAH2.setSow_rate_for_unit(
														Float.parseFloat(history.getSowRateForUnit().replace(",", "")));
											} else {
												issueInProcessing.add(onebankId);
												continue;
											}
										}
										invoiceAnnexureDAH2.setOnebankId(onebankId);
										invoiceAnnexureDAH2.setEmployee_id(empSowDetail.getEmployee_id());
										invoiceAnnexureDAH2.setEmployee_name(name);
										invoiceAnnexureDAH2.setPc_code(empSowDetail.getPc_code());
										invoiceAnnexureDAH2.setDate_of_joining(empSowDetail.getActual_date_of_joining());
										invoiceAnnexureDAH2.setMonth(invoiceMonth);
										invoiceAnnexureDAH2.setAccount_manager(empSowDetail.getAccount_manager());
										invoiceAnnexureDAH2.setHelius_recruiter(empSowDetail.getHelius_recruiter());
										String ctc = null;
										if (empSowDetail.getNew_monthly_basic() != null
												&& !empSowDetail.getNew_monthly_basic().isEmpty()) {
											ctc = empSowDetail.getNew_monthly_basic();
										} else if (empSowDetail.getCurrent_monthly_basic() != null
												&& !empSowDetail.getCurrent_monthly_basic().isEmpty()) {
											ctc = empSowDetail.getCurrent_monthly_basic();
										} else {
											ctc = "0";
										}
										float monthlyCTC = Float.parseFloat(ctc.replace(",", ""));
										
										// float totalRate =
										// invoiceAnnexureDAH2.getSow_rate_for_unit()
										// *
										// (noOfManDays/invoicedate.lengthOfMonth());
										float totalRate = 0;
										if ("Mandays".equalsIgnoreCase(empSowDetail.getSow_type())
												|| "TM-Mandays".equalsIgnoreCase(empSowDetail.getSow_type())) {
											totalRate = invoiceAnnexureDAH2.getSow_rate_for_unit() * noOfManDays;
										}
										if ("Manmonth".equalsIgnoreCase(empSowDetail.getSow_type())
												|| "TM-Manmonths".equalsIgnoreCase(empSowDetail.getSow_type())) {
											totalRate = invoiceAnnexureDAH2.getSow_rate_for_unit()
													* (noOfManDays / maximumManDaysPerMonth);
										}
										invoiceAnnexureDAH2.setMonthly_ctc(monthlyCTC);
										invoiceAnnexureDAH2.setNoOfMandays(noOfManDays);
										invoiceAnnexureDAH2.setRateForTotalWorkedDays(totalRate);
										invoiceAnnexureDAH2.setMargin(totalRate - monthlyCTC);
										totalMandays = totalMandays + invoiceAnnexureDAH2.getNoOfMandays();
										totalPo = totalPo + invoiceAnnexureDAH2.getRateForTotalWorkedDays();
										processed.add(invoiceAnnexureDAH2);
										/*
										 * if(!multiplePO.containsKey(
										 * employee_id)){
										 * multiplePO.put(employee_id,
										 * invoiceAnnexureDAH2); }else{
										 * InvoiceAnnexure prevInvoiceAnnexure =
										 * multiplePO.get(employee_id);
										 * invoiceAnnexureDAH2.
										 * setRateForTotalWorkedDays(
										 * invoiceAnnexureDAH2.
										 * getRateForTotalWorkedDays()+
										 * prevInvoiceAnnexure.
										 * getRateForTotalWorkedDays());
										 * invoiceAnnexureDAH2.setMargin(
										 * invoiceAnnexureDAH2.
										 * getRateForTotalWorkedDays()-
										 * monthlyCTC); totalMandays =
										 * totalMandays +
										 * invoiceAnnexureDAH2.getNoOfMandays();
										 * totalPo = totalPo +
										 * invoiceAnnexureDAH2.
										 * getRateForTotalWorkedDays();
										 * processed.add(invoiceAnnexureDAH2); }
										 */
									} else {
										issueInProcessing.add(onebankId);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								issueInProcessing.add(onebankId);
							}
						} else {
							issueInProcessing.add(onebankId);
						}
					}
				}
			}
			if ("final".equalsIgnoreCase(action)) {
				int i = 0;
				for (InvoiceAnnexure obj : processed) {
					session.save(obj);
					if (i % 50 == 0) {
						session.flush();
						session.clear();
					}
					i++;
				}
			}
			transaction.commit();
			HashMap<String, Object> result = new HashMap<String, Object>();
			result.put("success", processed);
			result.put("failed", issueInProcessing);
			result.put("totalPO's", totalPo);
			result.put("totalManDays's", totalMandays);
			ObjectMapper om = new ObjectMapper();
			Response = om.writeValueAsString(result);
			if (issueInProcessing != null && !issueInProcessing.isEmpty()) {
				try {
					String to = "hap@helius-tech.com";
					String subject = "Invoice Annexure for DAH2";
					StringBuffer message = new StringBuffer();
					message.append("Hi," + "\n\n" + "Issue in calculating " + invoicedate.getMonth() + " "
							+ invoicedate.getYear() + " Invoice Annexure for below DAH@ onebankid : " + "\n\n");
					for (String onebankid : issueInProcessing) {
						message.append(onebankid + " ," + "\n");
					}
					message.append("\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
					emailService.sendEmail(to, null, null, subject, message.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to process invoice annexure == " + e.getMessage());
		} finally {
			session.close();
		}
		return Response;
	}
	
	@Override
	public List<InvoiceAnnexure> getDah2AnnexureDashboard(String month) throws Throwable {
		Session session = null;
		List<InvoiceAnnexure> results = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp invoiceMonth = null;
		java.util.Date selectedDate = sdf.parse(month);
		invoiceMonth = new Timestamp(selectedDate.getTime());
		try {
			session = sessionFactory.openSession();
			String query = "SELECT * from InvoiceAnnexure where month = :invoiceMonth";
			Query listquery = session.createSQLQuery(query).addEntity(InvoiceAnnexure.class).setParameter("invoiceMonth",invoiceMonth);
			results = listquery.list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			throw new Throwable("Failed to fetch  InvoiceAnnexure dashboard Details ");
		}
		return results;
	}
	
	private boolean hasJoinedthisMonth(EmpOnebankSowDetails empSowDetail, LocalDate invoicedate) {
		
		LocalDateTime adj = empSowDetail.getActual_date_of_joining().toLocalDateTime();
		return (adj.getYear() == invoicedate.getYear() && adj.getMonthValue()== invoicedate.getMonthValue());	
	}

	private boolean hasExitedthisMonth(EmpOnebankSowDetails empSowDetail, LocalDate invoicedate) {
		LocalDateTime adj = null;
		if(empSowDetail.getRelieving_date() != null){
		adj = empSowDetail.getRelieving_date().toLocalDateTime();
		}
		return (adj != null  && adj.getYear() == invoicedate.getYear() && adj.getMonthValue() == invoicedate.getMonthValue());	
	}
	
	private float calculateCTC_joined(EmpOnebankSowDetails empSowDetail,float monthlyCTC) {
		float ctc = 0;
		LocalDateTime adj = empSowDetail.getActual_date_of_joining().toLocalDateTime();
		YearMonth yearMonthObject = YearMonth.of(adj.getYear(), adj.getMonth());
		float daysInMonth = yearMonthObject.lengthOfMonth(); //28 
		float daysworked = daysInMonth - adj.getDayOfMonth() + 1;
		ctc = (daysworked/daysInMonth)*monthlyCTC;
		return ctc;
	}
	private float calculateCTC_exited(EmpOnebankSowDetails empSowDetail,float monthlyCTC) {
		float ctc = 0;
		LocalDateTime exitdate = empSowDetail.getRelieving_date().toLocalDateTime();
		YearMonth yearMonthObject = YearMonth.of(exitdate.getYear(), exitdate.getMonth());
		float daysInMonth = yearMonthObject.lengthOfMonth(); //28 
		float daysworked =exitdate.getDayOfMonth();
		ctc = (daysworked/daysInMonth)*monthlyCTC;
		return ctc;
	}
	
	
	private void test_hasJoinedthisMonth() {
		
		EmpOnebankSowDetails empSowDetail = new EmpOnebankSowDetails();
		
		Timestamp adoj = new Timestamp(2020, 10, 15, 0, 0, 0, 0);
		Timestamp invoicedate = new Timestamp(2020, 10, 1, 0, 0, 0, 0);
		empSowDetail.setActual_date_of_joining(adoj);
		empSowDetail.setNew_monthly_basic("12,00,000");
		boolean joined = hasJoinedthisMonth(empSowDetail,invoicedate.toLocalDateTime().toLocalDate());
		if(joined){
			float actualctc = calculateCTC_joined(empSowDetail,1200000);
			System.out.println("actualctc"+ actualctc);
		}
	}
	
	public static void main(String[] args) {
		InvoiceServiceImpl invoiceimpl = new InvoiceServiceImpl();
		invoiceimpl.test_hasJoinedthisMonth();
	}
}
