package com.helius.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.Address;
import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.descriptor.java.JdbcTimestampTypeDescriptor.TimestampMutabilityPlan;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Checklist;
import com.helius.entities.Contact_Address_Details;
import com.helius.entities.Employee;
import com.helius.entities.EmployeeChecklistItemAndMandatoryitems;
import com.helius.entities.Employee_CheckList;
import com.helius.entities.Employee_Checklist_Items;
import com.helius.entities.Employee_Checklist_Master;
import com.helius.entities.Employee_Identification_Details;
import com.helius.entities.Employee_Offer_Details;
import com.helius.entities.Employee_Personal_Details;
import com.helius.entities.Employee_Salary_Details;
import com.helius.entities.EmailScreen;
import com.helius.managers.EmployeeManager;
import com.helius.service.EmailService;
import com.helius.utils.FilecopyStatus;
import com.helius.utils.Utils;

public class ChecklistDAOImpl implements IChecklistDAO {
	private org.hibernate.internal.SessionFactoryImpl sessionFactory;
	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Autowired
	ApplicationContext context;
	@Autowired
	private EmailService emailService;
	private List<String> copied_with_success = new ArrayList<String>();

	/** returns all master checklist items from checklistmaster entity **/
	@Override
	public List<Employee_Checklist_Master> getAllChecklist() throws Throwable {
		Session session = null;
		java.util.List checklist = null;
		Employee_Checklist_Master employee_Checklist_Master = null;
		List<Employee_Checklist_Master> employee_Checklist_Master_List = new ArrayList<Employee_Checklist_Master>();
		try {
			session = sessionFactory.openSession();
			String checklist_query = "select * from Employee_Checklist_Master WHERE active='YES'";
			checklist = session.createSQLQuery(checklist_query).addEntity(Employee_Checklist_Master.class).list();
			if (!checklist.isEmpty()) {
				for (Object employee_checklist : checklist) {
					employee_Checklist_Master = (Employee_Checklist_Master) employee_checklist;
					employee_Checklist_Master_List.add(employee_Checklist_Master);
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
		return employee_Checklist_Master_List;
	}

	/** Returns  individual master item details **/
	public Employee_Checklist_Master getChecklist(String masterId) throws Throwable {
		Session session = null;
		java.util.List checklist = null;
		Employee_Checklist_Master employee_Checklist_Master = null;
		try {
			session = sessionFactory.openSession();
			String checklist_query = "select * from Employee_Checklist_Master where employee_checklist_master_id = :employee_checklist_master_id ";
			checklist = session.createSQLQuery(checklist_query).addEntity(Employee_Checklist_Master.class)
					.setParameter("employee_checklist_master_id", masterId).list();

			if (!checklist.isEmpty()) {
				employee_Checklist_Master = (Employee_Checklist_Master) checklist.iterator().next();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
		return employee_Checklist_Master;
	}

	/** View/Download checklist template file for master item **/
	@Override
	public ResponseEntity<byte[]> getCheckListFiles(String masterId) throws Throwable {
		Employee_Checklist_Master masterdata = getChecklist(masterId);
		String ChecklistType = masterdata.getChecklistType();
		String filename = masterId + "_" + masterdata.getChecklistName() + "_" + masterdata.getTemplatePath();
		String clientfilelocation = Utils.getProperty("fileLocation") + File.separator + ChecklistType + File.separator
				+ filename;
		byte[] files = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(clientfilelocation);
			files = IOUtils.toByteArray(fi);
			fi.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			throw new Throwable("Unable to get files " + e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Throwable("Unable to get files " + e.getMessage());

		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
		return responseEntity;

	}

	/** Save and update checklist Master item**/
	@Override
	public void saveChecklist(Checklist masterChecklist, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> checklistType = new HashMap<String, String>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Object masterId = null;
			if (masterChecklist.getEmployeeChecklistMaster() != null) {
				Iterator<Employee_Checklist_Master> itr = masterChecklist.getEmployeeChecklistMaster().iterator();
				while (itr.hasNext()) {
					Employee_Checklist_Master employeeChecklistMaster = itr.next();
					String listType = employeeChecklistMaster.getChecklistType();
					String checklistName = employeeChecklistMaster.getChecklistName();
					String Nationality = employeeChecklistMaster.getNationalityApplicable();
					String Region = employeeChecklistMaster.getRegionApplicable();
					if (employeeChecklistMaster.getEmployeeChecklistMasterId() == 0) {
						java.util.List checklist = null;
						String checklist_query = "select * from Employee_Checklist_Master where checklist_type = :checklist_type AND checklist_name = :checklist_name AND nationality_applicable = :nationality_applicable AND region_applicable = :region_applicable";
						checklist = session.createSQLQuery(checklist_query).addEntity(Employee_Checklist_Master.class)
								.setParameter("checklist_type", listType).setParameter("region_applicable", Region)
								.setParameter("nationality_applicable", Nationality)
								.setParameter("checklist_name", checklistName).list();
						if (checklist.isEmpty() || checklist.size() == 0 || checklist == null) {
							session.save(employeeChecklistMaster);
						} else {
							throw new Exception("Duplicate Checklist Item");
						}
					} else {
						session.update(employeeChecklistMaster);
					}
					if(employeeChecklistMaster.getTemplatePath()!=null && !employeeChecklistMaster.getTemplatePath().isEmpty()){
					String modifiedFileName = employeeChecklistMaster.getEmployeeChecklistMasterId() + "_"
							+ employeeChecklistMaster.getChecklistName() + "_"
							+ employeeChecklistMaster.getTemplatePath();
					templateFilenames.put(employeeChecklistMaster.getTemplatePath(), modifiedFileName);
					checklistType.put(employeeChecklistMaster.getTemplatePath(),employeeChecklistMaster.getChecklistType());
					}
				}
				FilecopyStatus status = Utils.copyFiles(request, templateFilenames, checklistType);
				copied_with_success = status.getCopied_with_success();
				transaction.commit();
			}
		} catch (HibernateException e) {
			Utils.deleteFiles(copied_with_success);
			e.printStackTrace();
			throw new Throwable("Unable To Update Checklist Data " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable To Update Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
	}

	/** Returns employee checklist items from master based on condition from client and nationality and region of the employee from offer details **/
	public List<Employee_Checklist_Master> getEmpItemsFromMaster(String offerId, String checkListType)
			throws Throwable {
		Session session = null;
		java.util.List<Employee_Checklist_Master> masterlist = null;
		Employee_Checklist_Master employee_Checklist_Master = null;
		List<Employee_Checklist_Master> employee_Checklist_Master_List = new ArrayList<Employee_Checklist_Master>();
		EmployeeManager employeemanager = (EmployeeManager) context.getBean("employeeManager");
		Employee employee = employeemanager.getOfferbyID(offerId);
		String nationality = employee.getEmployeeOfferDetails().getNationality();
		String client = employee.getEmployeeOfferDetails().getClient();
		String region = employee.getEmployeeOfferDetails().getWork_country();
		String[] clientList = { "ALL", client };
	//	String[] nationalityList = { "ALL", nationality };
		String[] regionList = { "ALL", region };
		try {
			session = sessionFactory.openSession();
			String master_query = "select * from Employee_Checklist_Master where checklist_type = :checklist_type AND client_specific IN (:client_specific) AND region_applicable IN (:region_applicable)  AND active = :active";
			masterlist = session.createSQLQuery(master_query).addEntity(Employee_Checklist_Master.class)
					.setParameter("checklist_type", checkListType).setParameterList("client_specific", clientList)
					.setParameterList("region_applicable", regionList)
					.setParameter("active", "YES").list();
			if (!masterlist.isEmpty()) {
				for (Object employee_checklist : masterlist) {
					employee_Checklist_Master = (Employee_Checklist_Master) employee_checklist;
					employee_Checklist_Master_List.add(employee_Checklist_Master);
				}
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} catch (Exception e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
		return employee_Checklist_Master_List;
	}

	/**
	 * returns employee checklist items from item entity if data exists else
	 * retrieve data from master table
	 **/
	@Override
	public List<EmployeeChecklistItemAndMandatoryitems> getEmployeeChecklistItems(String offerId, String checkListType)
			throws Throwable {
		List<EmployeeChecklistItemAndMandatoryitems> itemsandmandatory = new ArrayList<EmployeeChecklistItemAndMandatoryitems>();
		try {
			List<Employee_Checklist_Items> createdItems = getemployeeItems(offerId, checkListType);
			List<Employee_Checklist_Master> masterdata = getEmpItemsFromMaster(offerId, checkListType);
			if (createdItems.isEmpty()) {
				for (Employee_Checklist_Master mstr : masterdata) {
					EmployeeChecklistItemAndMandatoryitems items = new EmployeeChecklistItemAndMandatoryitems();
					items.setChecklistName(mstr.getChecklistName());
					items.setChecklistType(mstr.getChecklistType());
					items.setMandatory(mstr.getMandatory());
					items.setSubmited("NO");
					items.setOfferId(offerId);
					itemsandmandatory.add(items);
				}
			} else {
				for (Employee_Checklist_Items item : createdItems) {
					EmployeeChecklistItemAndMandatoryitems items = new EmployeeChecklistItemAndMandatoryitems();
					items.setChecklistName(item.getChecklistName());
					items.setChecklistType(item.getChecklistType());
					items.setSubmited(item.getSubmited());
					items.setOfferId(item.getOfferId());
					items.setEmployeeChecklistItemsId(item.getEmployeeChecklistItemsId());
					items.setEmployeeId(item.getEmployeeId());
					items.setLastReminder(item.getLastReminder());
					items.setUploadDocumentPath(item.getUploadDocumentPath());
					items.setCreatedBy(item.getCreatedBy());
					items.setCreateDate(item.getCreateDate());
					items.setLastModifiedBy(item.getLastModifiedBy());
					items.setLastModifiedDate(item.getLastModifiedDate());
					for (Employee_Checklist_Master mstr : masterdata) {
						if (mstr.getChecklistName().equals(item.getChecklistName())) {
							items.setMandatory(mstr.getMandatory());
							break;
						}
					}
					itemsandmandatory.add(items);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		}
		return itemsandmandatory;
	}

	/** save or update employee checklist items **/
	@Override
	public void saveEmployeeChecklistItem(Employee_CheckList employeeChecklist, MultipartHttpServletRequest request)
			throws Throwable {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Map<String, String> templateFilenames = new HashMap<String, String>();
		String checklistType = null;
		try {
			if (employeeChecklist.getEmployeeChecklistItem() != null) {
				Iterator<Employee_Checklist_Items> itr = employeeChecklist.getEmployeeChecklistItem().iterator();
				while (itr.hasNext()) {
					Employee_Checklist_Items employeeChecklistItem = itr.next();
					if(employeeChecklistItem.getUploadDocumentPath()!=null && !employeeChecklistItem.getUploadDocumentPath().isEmpty()){
						employeeChecklistItem.setSubmited("YES");
					}
					if (employeeChecklistItem.getEmployeeChecklistItemsId() == 0) {
						session.save(employeeChecklistItem);
					} else {
						session.update(employeeChecklistItem);
					}
					if(employeeChecklistItem.getUploadDocumentPath()!=null && !employeeChecklistItem.getUploadDocumentPath().isEmpty()){
					String modifiedFileName = employeeChecklistItem.getOfferId() + "_"
							+ employeeChecklistItem.getChecklistName() + "_"
							+ employeeChecklistItem.getUploadDocumentPath();
					templateFilenames.put(employeeChecklistItem.getUploadDocumentPath(), modifiedFileName);
					checklistType = "Employee_" + employeeChecklistItem.getChecklistType();
					}
				}
			
			FilecopyStatus status = Utils.copyFiles(request, templateFilenames, checklistType);
			copied_with_success = status.getCopied_with_success();
			transaction.commit();
			}
		} catch (HibernateException e) {
			Utils.deleteFiles(copied_with_success);
			e.printStackTrace();
			throw new Throwable("Unable To Store Employee Checklist Data " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable To Store Employee Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void updateEmployeeChecklistItem(Employee_CheckList employeeChecklist, MultipartHttpServletRequest request)
			throws Throwable {
		Session session = null;
		Transaction transaction = null;
		Map<String, String> templateFilenames = new HashMap<String, String>();
		String checklistType = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			if (employeeChecklist.getEmployeeChecklistItem() != null) {
				Iterator<Employee_Checklist_Items> itr = employeeChecklist.getEmployeeChecklistItem().iterator();
				while (itr.hasNext()) {
					Employee_Checklist_Items employeeChecklistItem = itr.next();
					session.update(employeeChecklistItem);
					String modifiedFileName = employeeChecklistItem.getOfferId() + "_"
							+ employeeChecklistItem.getChecklistName() + "_"
							+ employeeChecklistItem.getUploadDocumentPath();
					templateFilenames.put(employeeChecklistItem.getUploadDocumentPath(), modifiedFileName);
					checklistType = employeeChecklistItem.getChecklistType();
				}
			}
			FilecopyStatus status = Utils.copyFiles(request, templateFilenames, checklistType);
			copied_with_success = status.getCopied_with_success();
			transaction.commit();
		} catch (HibernateException e) {
			Utils.deleteFiles(copied_with_success);
			e.printStackTrace();
			throw new Throwable("Unable To Store Employee Checklist Data " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable To Store Employee Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
	}

	/** Retrieve all the checklist items from employee item table **/
	@Override
	public List<Employee_Checklist_Items> getemployeeItems(String offerId, String checkListType) throws Throwable {
		Session session = null;
		java.util.List checklist = null;
		Employee_CheckList checklistItem = new Employee_CheckList();

		Employee_Checklist_Items employeeChecklistItem = null;
		List<Employee_Checklist_Items> employeeChecklistItems = new ArrayList<Employee_Checklist_Items>();
		try {
			session = sessionFactory.openSession();
			if (checkListType.equalsIgnoreCase("ALL")) {
				String checklist_query = "select * from Employee_Checklist_Items where offer_id = :offer_id";
				checklist = session.createSQLQuery(checklist_query).addEntity(Employee_Checklist_Items.class)
						.setParameter("offer_id", offerId).list();
			} else {
				String checklist_query = "select * from Employee_Checklist_Items where offer_id = :offer_id AND checklist_type = :checklist_type";
				checklist = session.createSQLQuery(checklist_query).addEntity(Employee_Checklist_Items.class)
						.setParameter("offer_id", offerId).setParameter("checklist_type", checkListType).list();
			}
			if (!checklist.isEmpty()) {
				for (Object employee_checklist : checklist) {
					employeeChecklistItem = (Employee_Checklist_Items) employee_checklist;
					employeeChecklistItems.add(employeeChecklistItem);
				}
			}
			checklistItem.setEmployeeChecklistItem(employeeChecklistItems);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} catch (Exception e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
		return employeeChecklistItems;
	}

	/** gets individual item record **/
	public Employee_Checklist_Items getItem(String itemId) throws Throwable {
		Session session = null;
		java.util.List checklist = null;
		Employee_Checklist_Items checklist_item = null;

		try {
			session = sessionFactory.openSession();
			String checklist_query = "select * from Employee_Checklist_Items where employee_checklist_items_id = :employee_checklist_items_id ";
			checklist = session.createSQLQuery(checklist_query).addEntity(Employee_Checklist_Items.class)
					.setParameter("employee_checklist_items_id", itemId).list();

			if (!checklist.isEmpty()) {
				checklist_item = (Employee_Checklist_Items) checklist.iterator().next();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable to get Checklist Data " + e.getMessage());
		} finally {
			session.close();
		}
		return checklist_item;
	}

	/** view/download employee files **/
	@Override
	public ResponseEntity<byte[]> getItemFiles(String itemId) throws Throwable {
		Employee_Checklist_Items itemdata = getItem(itemId);
		String ChecklistType = itemdata.getChecklistType();
		String filename = itemdata.getOfferId() + "_" + itemdata.getChecklistName() + "_"
				+ itemdata.getUploadDocumentPath();
		String itemfilelocation = Utils.getProperty("fileLocation") + File.separator + "Employee_" + ChecklistType
				+ File.separator + filename;
		byte[] files = null;
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(itemfilelocation);
			files = IOUtils.toByteArray(fi);
			fi.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
		return responseEntity;
	}

	/** send email notification to employee for submitting checklist documents with all the template attachment**/
	@Override
	public void sendChecklistEmailNotification(String jsonData, String offerId, String checklistType) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		ObjectMapper obm = new ObjectMapper();
		EmailScreen emailJson = obm.readValue(jsonData, EmailScreen.class);
		String st = emailJson.getCc();
		String[] cc = null;
		if (st != null && !st.isEmpty()) {
			cc = st.split(";");
		}
		String to = emailJson.getTo();
		List<String> urlList = new ArrayList<String>();
		List<Employee_Checklist_Items> itemList = getemployeeItems(offerId, checklistType);
		List<Employee_Checklist_Master> masterData = getEmpItemsFromMaster(offerId, checklistType);
		EmployeeManager employeemanager = (EmployeeManager) context.getBean("employeeManager");
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String subject = emailJson.getSubject();
		String text = emailJson.getText();
		/*StringBuffer message = new StringBuffer();
		message.append("Dear " + "\n\n" + "Welcome to Helius Family." + "\n\n"
				+ "Please submit your documets for further process \n\n ");*/
		List<String> tmp = new ArrayList<String>();
			if(!itemList.isEmpty() || itemList.size()!=0){
		for (Employee_Checklist_Items remind : itemList) {
			String tmp1 = remind.getSubmited();
			if ("NO".equalsIgnoreCase(tmp1)) {
				for (Employee_Checklist_Master data : masterData) {
					if (data.getChecklistName().equalsIgnoreCase(remind.getChecklistName())) {
						if (data.getTemplatePath() != null && !data.getTemplatePath().isEmpty()) {
							String filename = data.getEmployeeChecklistMasterId() + "_" + data.getChecklistName() + "_"
									+ data.getTemplatePath();
							String filelocation = Utils.getProperty("fileLocation") + File.separator + checklistType
									+ File.separator + filename;
							urlList.add(filelocation);
						}
						break;
					}
				}
				try {
					long millis = System.currentTimeMillis();
					java.sql.Timestamp date = new java.sql.Timestamp(millis);
					remind.setLastReminder(date);
					session.update(remind);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
			}else{
				throw new Throwable("Employee checklist item is not available. Please save checklist items before generating email");
			}
		try {
		emailService.sendMessageWithAttachment(to, cc, subject, text, urlList);		
			transaction.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new Throwable("Unable to update last Email remainder" + e.getMessage());
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new Throwable("Unable to update last Email remainder" + e.getMessage());
		}finally {
			session.close();
		}
	}

	
	/*public void sendChecklistReminder(String offerId, String checklistType) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		List<Employee_Checklist_Items> itemList = getemployeeItems(offerId, checklistType);
		List<Employee_Checklist_Master> mandatoryitem = null;
		List<String> urlList = new ArrayList<String>();
		List<String> remindItems = new ArrayList<String>();
		List<String> tmp = new ArrayList<String>();
		EmployeeManager employeemanager = (EmployeeManager) context.getBean("employeeManager");
		Employee employee = employeemanager.getOfferbyID(offerId);
		List<String> mandatory = new ArrayList<String>();
		for (Employee_Checklist_Master mandatoryTemp : mandatoryitem) {
			String tmp2 = mandatoryTemp.getMandatory();
			if ("YES".equalsIgnoreCase(tmp2)) {
				mandatory.add(mandatoryTemp.getChecklistName());
			}
		}
		for (Employee_Checklist_Items remind : itemList) {
			String tmp1 = remind.getSubmited();
			if ("NO".equalsIgnoreCase(tmp1)) {
				tmp.add(remind.getChecklistName());
			}
		}
		String to = employee.getEmployeeOfferDetails().getPersonal_email_id();
		String subject = "Reminder for " + checklistType + " documents submission";
		StringBuffer message = new StringBuffer();
		message.append("Dear " + employee.getEmployeeOfferDetails().getEmployee_name() + "," + "\n\n"
				+ "Welcome to Helius Family." + "\n\n" + "Please submit your documets for further process \n\n ");
		for (Employee_Checklist_Items checklist_item : itemList) {
			if (tmp.contains(checklist_item.getChecklistName())) {
				if (checklist_item.getUploadDocumentPath() != null
						&& !"".equalsIgnoreCase(checklist_item.getUploadDocumentPath())) {
					String ChecklistType = checklist_item.getChecklistType();
					String filename = checklist_item.getEmployeeChecklistItemsId() + "_"
							+ checklist_item.getChecklistName() + "_" + checklist_item.getUploadDocumentPath();
					String clientfilelocation = Utils.getProperty("fileLocation") + File.separator + ChecklistType
							+ File.separator + filename;
					urlList.add(clientfilelocation);
				}
				try {
					session = sessionFactory.openSession();
					transaction = session.beginTransaction();
					long millis = System.currentTimeMillis();
					java.sql.Timestamp date = new java.sql.Timestamp(millis);
					checklist_item.setLastReminder(date);
					session.update(checklist_item);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (mandatory.contains(checklist_item.getChecklistName())) {
					message.append(checklist_item.getChecklistName() + " *" + "\n");
				} else {
					message.append(checklist_item.getChecklistName() + "\n");
				}
			}
		}
		message.append("\n\n" + "Regards," + "\n" + "Helius Technologies.");
		// emailService.sendMessageWithAttachment(to,subject,message,urlList);
		try {
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Unable to update last Email remainder" + e.getMessage());
		} finally {
			session.close();
		}
	}*/

}