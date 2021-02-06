/**
 * 
 */
package com.helius.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.ClientDetail;
import com.helius.utils.*;
import com.helius.entities.ClientLeavePolicy;
import com.helius.entities.ClientReimbursementCategory;
import com.helius.entities.ClientReimbursementPolicy;
import com.helius.entities.Client_Leave_Policy_New;
import com.helius.entities.Employee;
import com.helius.entities.Employee_Leaves_Eligibility;
import com.helius.utils.Utils;
import com.lowagie.text.pdf.AcroFields.Item;

/**
 * @author Tirumala 25-Jul-2018
 */
public class ClientDAOImpl implements IClientDAO {
	private org.hibernate.internal.SessionFactoryImpl sessionFactory;
	private Map<String, String> modifiedFilenames = new HashMap<String, String>();
	private List<String> copied_with_success = new ArrayList<String>();
	/**
	 * 
	 */
	public ClientDAOImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the sessionFactory
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.dao.IClientDAO#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public com.helius.utils.ClientDetail get(String clientName) {
		com.helius.entities.ClientDetail clientdetail_entity = new ClientDetail();
		List<com.helius.entities.ClientLeavePolicy> clientLeavePolicy = new ArrayList<com.helius.entities.ClientLeavePolicy>();
		com.helius.entities.ClientReimbursementPolicy clientReimbursementPolicy = new com.helius.entities.ClientReimbursementPolicy();
		List<com.helius.entities.ClientReimbursementCategory> clientReimbureseCategories = new ArrayList<com.helius.entities.ClientReimbursementCategory>();
		com.helius.utils.ClientDetail clientdetail = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			 Query query = session.createSQLQuery("select * from client_details c where c.client_name=:client_name")
					 .addEntity(com.helius.entities.ClientDetail.class).setParameter("client_name", clientName);        
	        List clients =query.list();
			if(!clients.isEmpty()) {
				Iterator iter = clients.iterator();
				while(iter.hasNext()) {
					clientdetail_entity =(com.helius.entities.ClientDetail) iter.next();
					break;
				}
				
			}
			Query query1 = session.createSQLQuery("select * from Client_Leave_Policy_New1 c where c.clientId=:clientId")
					 .addEntity(com.helius.entities.ClientLeavePolicy.class).setParameter("clientId", clientdetail_entity.getClientId());
	         
	        
	        List clientleavePolicy =query1.list();
	       
			if(!clientleavePolicy.isEmpty()) {
				Iterator iter = clientleavePolicy.iterator();
				while(iter.hasNext()) {
					com.helius.entities.ClientLeavePolicy item = null;
					item =(com.helius.entities.ClientLeavePolicy) iter.next();
					clientLeavePolicy.add(item);
					
				}
				
			}	
			Query query2 = session.createSQLQuery("select * from client_reimbursement_categories c where c.client_id=:client_id")
					 .addEntity(com.helius.entities.ClientReimbursementCategory.class).setParameter("client_id", clientdetail_entity.getClientId());
	         
	        
	        List clientreimbursements =query2.list();
			if(!clientreimbursements.isEmpty()) {
				
				
				Iterator iter = clientreimbursements.iterator();
				while(iter.hasNext()) {
					com.helius.entities.ClientReimbursementCategory temp = null;
					temp = (com.helius.entities.ClientReimbursementCategory) iter.next();
					clientReimbureseCategories.add(temp);
				}
				
			}	
			Query query3 = session.createSQLQuery("select * from client_reimbursement_policy c where c.client_id=:client_id")
					 .addEntity(com.helius.entities.ClientReimbursementPolicy.class).setParameter("client_id", clientdetail_entity.getClientId());
	         
	        
	        List clientreimbursementpolicy =query3.list();
			if(!clientreimbursementpolicy.isEmpty()) {
			
				Iterator iter1 = clientreimbursementpolicy.iterator();
				while(iter1.hasNext()) {					
					clientReimbursementPolicy = (com.helius.entities.ClientReimbursementPolicy) iter1.next();
					break;
				}
			}

			DozerBeanMapper dbm = new DozerBeanMapper();
			clientdetail = dbm.map(clientdetail_entity, com.helius.utils.ClientDetail.class);
			List<com.helius.utils.ClientLeavePolicy> clientLeavePolicy1 = new ArrayList<com.helius.utils.ClientLeavePolicy>();
			for(com.helius.entities.ClientLeavePolicy item : clientLeavePolicy) {
				com.helius.utils.ClientLeavePolicy item1	= dbm.map(item, com.helius.utils.ClientLeavePolicy.class);
				clientLeavePolicy1.add(item1);
			}
			 
			com.helius.utils.ClientReimbursementPolicy clientreimbursementpolicy1 = dbm.map(clientReimbursementPolicy, com.helius.utils.ClientReimbursementPolicy.class);
			List<com.helius.utils.ClientReimbursementCategory> clientReimbursementCategories_utils = new ArrayList<com.helius.utils.ClientReimbursementCategory>();
			for(com.helius.entities.ClientReimbursementCategory crc : clientReimbureseCategories) {
				com.helius.utils.ClientReimbursementCategory crc_util = dbm.map(crc, com.helius.utils.ClientReimbursementCategory.class);
				clientReimbursementCategories_utils.add(crc_util);
			}
			if(clientLeavePolicy1 != null) {
				clientdetail.setClientLeavePolicy(clientLeavePolicy1);
			} 
			
			if(clientreimbursementpolicy1 != null) {
				clientdetail.setClientReimbursementPolicy(clientreimbursementpolicy1);
			} 
			
			clientdetail.setClientReimbursementCategories(clientReimbursementCategories_utils);
			
			List<com.helius.utils.ClientGroupDetail> clientGroupDetails = getClientGroupDetails(session, clientdetail_entity.getClientId());
			clientdetail.setClientGroupDetails(clientGroupDetails);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		//ClassB classB = new DozerBeanMapper().map(classA, ClassB.class);
		//getAllLeavePolicies();
		return clientdetail;
	}
		
	private List<com.helius.utils.ClientGroupDetail> getClientGroupDetails(Session session, int clientid) {
		List<com.helius.utils.ClientGroupDetail> clientGroupDetails = new ArrayList<com.helius.utils.ClientGroupDetail>();
		DozerBeanMapper dbm = new DozerBeanMapper();
		try {

			Query query = session.createSQLQuery("select * from client_group_details c where c.client_id=:client_id")
					.addEntity(com.helius.entities.ClientGroupDetail.class).setParameter("client_id", clientid);
			List clients = query.list();
			if (!clients.isEmpty()) {
				Iterator iter = clients.iterator();
				while (iter.hasNext()) {
					com.helius.entities.ClientGroupDetail clientgroupdetail_entity = (com.helius.entities.ClientGroupDetail) iter
							.next();
					int client_group_id = clientgroupdetail_entity.getClientGroupDetailsId();
					com.helius.utils.ClientGroupDetail clientgroupdetail_util = dbm.map(clientgroupdetail_entity,
							com.helius.utils.ClientGroupDetail.class);

					Query query1 = session
							.createSQLQuery(
									"select * from client_group_leave_policy_new c where c.client_group_details_id=:client_group_id")
							.addEntity(com.helius.entities.ClientGroupLeavePolicy.class)
							.setParameter("client_group_id", client_group_id);

					List<com.helius.utils.ClientGroupReimbursementCategory> clientGroupReimbureseCategories = new ArrayList<com.helius.utils.ClientGroupReimbursementCategory>();

					List clientgroupleave = query1.list();
					List<com.helius.utils.ClientGroupLeavePolicy> clientGroupLeavePolicy_util_list = new ArrayList<com.helius.utils.ClientGroupLeavePolicy>();
					if (!clientgroupleave.isEmpty()) {
						Iterator iter1 = clientgroupleave.iterator();
						while (iter1.hasNext()) {
							com.helius.entities.ClientGroupLeavePolicy clientGroupLeavePolicy = (com.helius.entities.ClientGroupLeavePolicy) iter1
									.next();
							com.helius.utils.ClientGroupLeavePolicy clientGroupLeavePolicy_util = dbm
									.map(clientGroupLeavePolicy, com.helius.utils.ClientGroupLeavePolicy.class);
							clientGroupLeavePolicy_util_list.add(clientGroupLeavePolicy_util);
						}
						clientgroupdetail_util.setClientGroupLeavePolicy(clientGroupLeavePolicy_util_list);
					}

					Query query2 = session
							.createSQLQuery(
									"select * from client_group_reimbursement_categories c where c.client_group_details_id=:client_group_id")
							.addEntity(com.helius.entities.ClientGroupReimbursementCategory.class)
							.setParameter("client_group_id", client_group_id);

					List clientreimbursements = query2.list();
					if (!clientreimbursements.isEmpty()) {

						Iterator iter1 = clientreimbursements.iterator();
						while (iter1.hasNext()) {
							com.helius.entities.ClientGroupReimbursementCategory temp = null;
							temp = (com.helius.entities.ClientGroupReimbursementCategory) iter1.next();
							com.helius.utils.ClientGroupReimbursementCategory temp_util = dbm.map(temp,
									com.helius.utils.ClientGroupReimbursementCategory.class);
							clientGroupReimbureseCategories.add(temp_util);
						}
						clientgroupdetail_util.setClientGroupReimbursementCategories(clientGroupReimbureseCategories);
					}
					Query query3 = session
							.createSQLQuery(
									"select * from client_group_reimbursement_policy c where c.client_group_details_id=:client_group_id")
							.addEntity(com.helius.entities.ClientGroupReimbursementPolicy.class)
							.setParameter("client_group_id", client_group_id);
					
					List clientreimbursementpolicies = query3.list();
					if (!clientreimbursementpolicies.isEmpty()) {

						Iterator iter1 = clientreimbursementpolicies.iterator();
						while (iter1.hasNext()) {
							com.helius.entities.ClientGroupReimbursementPolicy clientGroupReimbursementPolicy = (com.helius.entities.ClientGroupReimbursementPolicy) iter1
									.next();
							com.helius.utils.ClientGroupReimbursementPolicy clientGroupReimbursementPolicy_util = dbm
									.map(clientGroupReimbursementPolicy,
											com.helius.utils.ClientGroupReimbursementPolicy.class);
							clientgroupdetail_util.setClientGroupReimbursementPolicy(clientGroupReimbursementPolicy_util);
							break;
						}
					}

					clientGroupDetails.add(clientgroupdetail_util);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientGroupDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.dao.IClientDAO#update(com.helius.entities.ClientDetail)
	 */
	@Override
	public void update(ClientDetail clientDetail) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.dao.IClientDAO#save(com.helius.entities.ClientDetail)
	 */
	@Override
	public void save(ClientDetail clientDetail, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			session.save(clientDetail);
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} catch (Exception e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} finally {
			session.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.dao.IClientDAO#save(java.lang.String,
	 * org.springframework.web.multipart.MultipartHttpServletRequest)
	 */
	@Override
	public void save(String clientjson, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		// JSONObject clientJsonObj = (JSONObject) JSONValue.parse(clientjson);
		
		
		ObjectMapper obm = new ObjectMapper();
		try {
			com.helius.utils.ClientDetail clientdetail = obm.readValue(clientjson, com.helius.utils.ClientDetail.class);

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ClientDetail clientDetail = getClientDetail(clientdetail);
			
			Object id = session.save(clientDetail);
			int clientid = (int) id;
			List<ClientLeavePolicy> clientLeavePolicyList = getClientLeavePolicy(clientdetail);
			for(ClientLeavePolicy clientLeavePolicy : clientLeavePolicyList ) {
				if (clientLeavePolicy != null) {
					clientLeavePolicy.setClientId(clientid);
					session.save(clientLeavePolicy);
				}
			}
			

			ClientReimbursementPolicy clientReimbursementPolicy = getClientReimbursementPolicy(clientdetail);
			if (clientReimbursementPolicy != null) {
				clientReimbursementPolicy.setClientId(clientid);
				session.save(clientReimbursementPolicy);
			}

			List<ClientReimbursementCategory> clientReimbursementCategories = getClientReimbursementCategories(
					clientdetail);
			if (clientReimbursementCategories != null) {
				for (ClientReimbursementCategory crc : clientReimbursementCategories) {
					crc.setClientId(clientid);
					session.save(crc);
				}

			}

			Map<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> clienGroups = getClientGroupDetail(
					clientdetail);
			if (clienGroups != null) {

				for (Entry<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> entry : clienGroups
						.entrySet()) {
					com.helius.entities.ClientGroupDetail cgde = entry.getKey();
					cgde.setClientId(clientid);
					int clientgroupid = (int) session.save(cgde);
					com.helius.utils.ClientGroupDetail cgd = entry.getValue();
					List<com.helius.entities.ClientGroupLeavePolicy> cglp = getClientGroupLeavePolicy(cgd);

					if (cglp != null) {
						for(com.helius.entities.ClientGroupLeavePolicy item : cglp) {
							item.setClient_group_details_id(clientgroupid);
							session.save(item);
						}
					}

					List<com.helius.entities.ClientGroupReimbursementCategory> clientGroupReimbursementCategories_entities = getClientGroupReimbursementCategories(
							cgd);
					if (clientGroupReimbursementCategories_entities != null
							&& !clientGroupReimbursementCategories_entities.isEmpty()) {
						for (com.helius.entities.ClientGroupReimbursementCategory crc : clientGroupReimbursementCategories_entities) {
							crc.setClientGroupGetailsId(clientgroupid);
							session.save(crc);
						}
					}

				}
			}
			copyfiles(request);
			transaction.commit();
			//throw new HibernateException("Dummy exception");
		} catch (HibernateException e) {
			
			deleteFiles(copied_with_success);
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} catch (Exception e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} finally {
			session.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.dao.IClientDAO#save(com.helius.entities.ClientDetail)
	 */
	@Override
	public void save(ClientDetail clientDetail) throws Throwable {
		// TODO Auto-generated method stub

	}

	private com.helius.entities.ClientDetail getClientDetail(com.helius.utils.ClientDetail clientdetail) {

		com.helius.entities.ClientDetail clientdetail_entity = new com.helius.entities.ClientDetail();
		
		clientdetail_entity.setApplicableSowType(clientdetail.getApplicableSowType());
		clientdetail_entity.setClientCountry(clientdetail.getClientCountry());
		clientdetail_entity.setClientMasterAgreementExpiresOn(clientdetail.getClientMasterAgreementExpiresOn());
		clientdetail_entity.setClientMasterAgreementReference(clientdetail.getClientMasterAgreementReference());
		clientdetail_entity.setClientName(clientdetail.getClientName());
		clientdetail_entity.setClientShortName(clientdetail.getClientShortName());
		clientdetail_entity.setClientTimesheet(clientdetail.getClientTimesheet());
		clientdetail_entity.setTimesheetApprover(clientdetail.getTimesheetApprover());
		clientdetail_entity.setTimesheetApproversAll(clientdetail.getTimesheetApproversAll());
		clientdetail_entity.setIsheliusclient(clientdetail.getIsheliusclient());
		clientdetail_entity.setBudgetowner(clientdetail.getBudgetowner());
		clientdetail_entity.setBudgetownerList(clientdetail.getBudgetownerList());
		
		String clientname_wth_hyphen = clientdetail.getClientName() + "_";
		if(clientdetail.getUploadMasterAgreementPath() != null &&  clientdetail.getUploadMasterAgreementPath() != "") {
			String modifiled_UploadMasterAgreementPath = "";
			if(clientdetail.getUploadMasterAgreementPath().indexOf(";") != -1) {			
				String[] filenames = clientdetail.getUploadMasterAgreementPath().split(";");
				
				for(int i =0; i < filenames.length; i++){
					String filename1 = filenames[i];
					if(!filenames[i].startsWith(clientname_wth_hyphen)) {
						filename1 =  clientname_wth_hyphen + filenames[i];
					}
					
					modifiedFilenames.put(filenames[i], filename1);
					if(i > 0){
						modifiled_UploadMasterAgreementPath = modifiled_UploadMasterAgreementPath + ";" + filename1;
					} else {
						modifiled_UploadMasterAgreementPath =  filename1;
					}
					
				}
				
			} else {
				String filename1 = clientdetail.getUploadMasterAgreementPath();
				if(!filename1.startsWith(clientname_wth_hyphen)) {
					filename1 = clientname_wth_hyphen + clientdetail.getUploadMasterAgreementPath();
				}
				modifiedFilenames.put(clientdetail.getUploadMasterAgreementPath(), filename1);
				modifiled_UploadMasterAgreementPath = filename1;				
			}
			clientdetail_entity.setUploadMasterAgreementPath(modifiled_UploadMasterAgreementPath);
			
		} else {
			clientdetail_entity.setUploadMasterAgreementPath(clientdetail.getUploadMasterAgreementPath());
		}
		
		if(clientdetail.getUploadClientLogoPath() != null && !clientdetail.getUploadClientLogoPath().startsWith(clientname_wth_hyphen)) {
			String filename = clientdetail.getClientName() + "_" +clientdetail.getUploadClientLogoPath();
			clientdetail_entity.setUploadClientLogoPath(filename);
			modifiedFilenames.put(clientdetail.getUploadClientLogoPath(), filename);
		} else {
			clientdetail_entity.setUploadClientLogoPath(clientdetail.getUploadClientLogoPath());
		}
		
		return clientdetail_entity;
	}

	private Map<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> getClientGroupDetail(
			com.helius.utils.ClientDetail clientdetail) {
		Map<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> clientGroupDetail_entities = new HashMap<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail>();
		List<com.helius.utils.ClientGroupDetail> clientGroupDetails = clientdetail.getClientGroupDetails();
		for (com.helius.utils.ClientGroupDetail cgd : clientGroupDetails) {
			com.helius.entities.ClientGroupDetail cgde = new com.helius.entities.ClientGroupDetail();
			cgde.setClientGroup(cgd.getClientGroup());
			cgde.setClientGroupAgreementExpiresOn(cgd.getClientGroupAgreementExpiresOn());
			cgde.setClientGroupAgreementReference(cgd.getClientGroupAgreementReference());
			cgde.setClientGroupTimesheet(cgd.getClientGroupTimesheet());
			cgde.setTimesheetApprover(cgd.getTimesheetApprover());
			cgde.setClientGroupDetailsId(cgd.getClientGroupDetailsId());
			cgde.setLobList(cgd.getLobList());
			cgde.setLob(cgd.getLob());
			String clientname_wth_hyphen = clientdetail.getClientName() + "_";
			if(cgd.getUploadGroupAgreement() != null  && !cgd.getUploadGroupAgreement().startsWith(clientname_wth_hyphen)) {
				String filename = cgd.getClientGroup() + "_" +cgd.getUploadGroupAgreement();
				cgde.setUploadGroupAgreement(filename);
				modifiedFilenames.put(cgd.getUploadGroupAgreement(), filename);
			} else {
				cgde.setUploadGroupAgreement(cgd.getUploadGroupAgreement());
			}
			//cgde.setUploadGroupAgreement(cgd.getUploadGroupAgreement() != null ? (cgd.getClientGroup() + cgd.getUploadGroupAgreement()) : "");
			
			
			clientGroupDetail_entities.put(cgde, cgd);
		}

		return clientGroupDetail_entities;
	}

	private Map<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> getdeleteGroupDetail(
			com.helius.utils.ClientDetail clientdetail) {
		Map<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> clientGroupDetail_entities = new HashMap<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail>();
		List<com.helius.utils.ClientGroupDetail> clientGroupDetails = clientdetail.getDelete_groups();
		if (clientGroupDetails != null) {
			for (com.helius.utils.ClientGroupDetail cgd : clientGroupDetails) {
				com.helius.entities.ClientGroupDetail cgde = new com.helius.entities.ClientGroupDetail();
				cgde.setClientGroup(cgd.getClientGroup());
				cgde.setClientGroupAgreementExpiresOn(cgd.getClientGroupAgreementExpiresOn());
				cgde.setClientGroupAgreementReference(cgd.getClientGroupAgreementReference());
				cgde.setClientGroupTimesheet(cgd.getClientGroupTimesheet());
				cgde.setTimesheetApprover(cgd.getTimesheetApprover());
				cgde.setClientGroupDetailsId(cgd.getClientGroupDetailsId());
				if (cgd.getUploadGroupAgreement() != null) {
					String filename = cgd.getClientGroup() + "_" + cgd.getUploadGroupAgreement();
					cgde.setUploadGroupAgreement(filename);
					modifiedFilenames.put(cgd.getUploadGroupAgreement(), filename);
				}
				// cgde.setUploadGroupAgreement(cgd.getUploadGroupAgreement() !=
				// null ? (cgd.getClientGroup() + cgd.getUploadGroupAgreement())
				// : "");

				clientGroupDetail_entities.put(cgde, cgd);
			}
		}
		return clientGroupDetail_entities;
	}
	
	public List<com.helius.entities.ClientLeavePolicy> getClientLeavePolicy(com.helius.utils.ClientDetail clientdetail) {
		List<com.helius.entities.ClientLeavePolicy> clientLeavePolicy_entityList = new ArrayList<com.helius.entities.ClientLeavePolicy>();
		List<com.helius.utils.ClientLeavePolicy> clientLeavePolicy = clientdetail.getClientLeavePolicy();
		
		for(com.helius.utils.ClientLeavePolicy item : clientLeavePolicy) {
			com.helius.entities.ClientLeavePolicy clientLeavePolicy_entity = new com.helius.entities.ClientLeavePolicy();
			clientLeavePolicy_entity.setTypeofleave(item.getTypeofleave());
			clientLeavePolicy_entity.setNumber_days(item.getNumber_days());
			clientLeavePolicy_entity.setCompensatory_off_leave_allowed(item.getCompensatory_off_leave_allowed());
			clientLeavePolicy_entity.setLeave_encashments(item.getLeave_encashments());
			
		
			clientLeavePolicy_entity.setSame_as_helius_policy(item.getSame_as_helius_policy());
			clientLeavePolicy_entity.setCustomerSOW_payforemployeeleaves(item.getCustomerSOW_payforemployeeleaves());
			if(item.getClientLeavePolicyId() != 0) {
				clientLeavePolicy_entity.setClientLeavePolicyId(item.getClientLeavePolicyId());
			}
			clientLeavePolicy_entityList.add(clientLeavePolicy_entity);
		}
		
		return clientLeavePolicy_entityList;

	}

	public List<com.helius.entities.ClientGroupLeavePolicy> getClientGroupLeavePolicy(
			com.helius.utils.ClientGroupDetail clientgroupdetail) {
		
		
		List<com.helius.entities.ClientGroupLeavePolicy> clientGroupLeavePolicy_entityList = new ArrayList<com.helius.entities.ClientGroupLeavePolicy>();
		List<com.helius.utils.ClientGroupLeavePolicy> cliengrouptLeavePolicy_list = clientgroupdetail.getClientGroupLeavePolicy();
		
		for(com.helius.utils.ClientGroupLeavePolicy item : cliengrouptLeavePolicy_list) {
			com.helius.entities.ClientGroupLeavePolicy clientGroupLeavePolicy_entity = new com.helius.entities.ClientGroupLeavePolicy();
			clientGroupLeavePolicy_entity.setTypeofleave(item.getTypeofleave());
			clientGroupLeavePolicy_entity.setNumber_days(item.getNumber_days());
			clientGroupLeavePolicy_entity.setCompensatory_off_leave_allowed(item.getCompensatory_off_leave_allowed());
			clientGroupLeavePolicy_entity.setLeave_encashments(item.getLeave_encashments());
			
		
			clientGroupLeavePolicy_entity.setSameas_client_policy(item.getSameas_client_policy());
			clientGroupLeavePolicy_entity.setCustomerSOW_payforemployeeleaves(item.getCustomerSOW_payforemployeeleaves());
			if(item.getCLient_gorup_leave_policyid()!= 0) {
				clientGroupLeavePolicy_entity.setCLient_gorup_leave_policyid(item.getCLient_gorup_leave_policyid());
				clientGroupLeavePolicy_entity.setClient_group_details_id(item.getClient_group_details_id());
				
			}
			clientGroupLeavePolicy_entityList.add(clientGroupLeavePolicy_entity);
		}
		
		return clientGroupLeavePolicy_entityList;

		

	}

	public com.helius.entities.ClientReimbursementPolicy getClientReimbursementPolicy(
			com.helius.utils.ClientDetail clientdetail) {
		com.helius.entities.ClientReimbursementPolicy clientReimbursementPolicy_entity = null;
		com.helius.utils.ClientReimbursementPolicy clientReimbursementPolicy = clientdetail
				.getClientReimbursementPolicy();

		if (clientReimbursementPolicy != null && clientReimbursementPolicy.getSameAsHeliusPolicy() != null) {
			clientReimbursementPolicy_entity = new com.helius.entities.ClientReimbursementPolicy();
			clientReimbursementPolicy_entity.setSameAsHeliusPolicy(clientReimbursementPolicy.getSameAsHeliusPolicy());
			if (clientReimbursementPolicy.getClientReimbursementPolicyid() != 0) {
				clientReimbursementPolicy_entity
						.setClientReimbursementPolicyid(clientReimbursementPolicy.getClientReimbursementPolicyid());
			}
		}

		return clientReimbursementPolicy_entity;

	}

	public com.helius.entities.ClientGroupReimbursementPolicy getClientGroupReimbursementPolicy(
			com.helius.utils.ClientGroupDetail clientgroupdetail) {
		com.helius.entities.ClientGroupReimbursementPolicy clientgroupReimbursementPolicy_entity = null;
		com.helius.utils.ClientGroupReimbursementPolicy clientgroupReimbursementPolicy = clientgroupdetail
				.getClientGroupReimbursementPolicy();

		if (clientgroupReimbursementPolicy != null && clientgroupReimbursementPolicy.getSameasClientPolicy() != null) {
			clientgroupReimbursementPolicy_entity = new com.helius.entities.ClientGroupReimbursementPolicy();
			clientgroupReimbursementPolicy_entity
					.setSameasClientPolicy(clientgroupReimbursementPolicy.getSameasClientPolicy());
			if (clientgroupReimbursementPolicy.getClientGroupReimbursementPolicyid() != 0) {
				clientgroupReimbursementPolicy_entity.setClientGroupReimbursementPolicyid(
						clientgroupReimbursementPolicy.getClientGroupReimbursementPolicyid());
			}
		}
		clientgroupReimbursementPolicy_entity
				.setSameasClientPolicy(clientgroupReimbursementPolicy.getSameasClientPolicy());
		return clientgroupReimbursementPolicy_entity;

	}

	private List<com.helius.entities.ClientReimbursementCategory> getClientReimbursementCategories(
			com.helius.utils.ClientDetail clientdetail) {
		List<com.helius.entities.ClientReimbursementCategory> clientReimbursementCategories_entities = new ArrayList<com.helius.entities.ClientReimbursementCategory>();
		List<com.helius.utils.ClientReimbursementCategory> clientReimbursementCategories = clientdetail
				.getClientReimbursementCategories();
		

		if (clientReimbursementCategories != null) {
			for (com.helius.utils.ClientReimbursementCategory crc : clientReimbursementCategories) {
				com.helius.entities.ClientReimbursementCategory crc_entity = new com.helius.entities.ClientReimbursementCategory();
				crc_entity.setAllowed(crc.getAllowed());
				crc_entity.setLimitAmount(crc.getLimitAmount());
				crc_entity.setRequiresSupportingProofs(crc.getRequiresSupportingProofs());
				crc_entity.setType(crc.getType());
				if(crc.getClientReimbursementCategoryId()  > 0) {
					crc_entity.setClientReimbursementCategoryId(crc.getClientReimbursementCategoryId());
				}
				clientReimbursementCategories_entities.add(crc_entity);
			}
		}

		return clientReimbursementCategories_entities;

	}

	private List<com.helius.entities.ClientGroupReimbursementCategory> getClientGroupReimbursementCategories(
			com.helius.utils.ClientGroupDetail clientgroupdetail) {
		List<com.helius.entities.ClientGroupReimbursementCategory> clientGroupReimbursementCategories_entities = new ArrayList<com.helius.entities.ClientGroupReimbursementCategory>();
		List<com.helius.utils.ClientGroupReimbursementCategory> clientGroupReimbursementCategories = clientgroupdetail
				.getClientGroupReimbursementCategories();
		

		if (clientGroupReimbursementCategories != null) {
			for (com.helius.utils.ClientGroupReimbursementCategory crc : clientGroupReimbursementCategories) {
				com.helius.entities.ClientGroupReimbursementCategory crc_entity = new com.helius.entities.ClientGroupReimbursementCategory();
				crc_entity.setAllowed(crc.getAllowed());
				crc_entity.setLimitAmount(crc.getLimitAmount());
				crc_entity.setRequiresSupportingProofs(crc.getRequiresSupportingProofs());
				crc_entity.setType(crc.getType());
				if(crc.getClientGroupReimbursementCategoryId()  > 0) {
					crc_entity.setClientGroupReimbursementCategoryId(crc.getClientGroupReimbursementCategoryId());
				}
				clientGroupReimbursementCategories_entities.add(crc_entity);
			}
		}

		return clientGroupReimbursementCategories_entities;

	}

	private boolean copyfiles(MultipartHttpServletRequest request) throws Throwable{
		/*boolean success = true;
		String clientfilelocation = Utils.getProperty("fileLocation") + File.separator + "client";
		Iterator<String> fileNames = request.getFileNames();
		//String filename = "";
		while(fileNames.hasNext()) {
			
			String filename = fileNames.next();
			MultipartFile file = request.getFile(filename);
			//filename = file.getOriginalFilename();
			String modifiedfilename = modifiedFilenames.get(filename);
			// filename = id + "_" + file.getOriginalFilename();
			String fileUrl = clientfilelocation + File.separator;

			// fileUrl = fileUrl.replaceAll("\\\\", "\\\\\\\\");

			try {
				file.transferTo(new File(new File(fileUrl), modifiedfilename));
				copied_with_success.add(fileUrl+File.separator + modifiedfilename);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				success = false;
				deleteFiles(copied_with_success);
				throw new Exception("Failed to save the files")	;					
			}

		}

		return success;*/
		FilecopyStatus status = Utils.copyFiles(request, modifiedFilenames, "client");
		copied_with_success = status.getCopied_with_success();
		return status.isOk();

	}
	
	private void deleteFiles(List<String> copied_with_success) {
		
		for(String filename : copied_with_success) {
			File file = new File(filename);
			file.delete();
		}
	}



	/* (non-Javadoc)
	 * @see com.helius.dao.IClientDAO#getAllClientNames()
	 */
	@Override
	public List<String> getAllClientNames() throws Throwable {
		Session session  = null;
		List clients = null;
		try {
			
			session = sessionFactory.openSession();
			Query query = session.createSQLQuery("select client_name from client_details");
			clients = query.list();
			
		} catch (HibernateException e) {

			
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} catch (Exception e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} finally {
			session.close();
		}

		
		return clients;

	}

	/* (non-Javadoc)
	 * @see com.helius.dao.IClientDAO#update(java.lang.String, org.springframework.web.multipart.MultipartHttpServletRequest)
	 */
	@Override
	public void update(String clientjson, MultipartHttpServletRequest request) throws Throwable {

		Session session = null;
		Transaction transaction = null;
		// JSONObject clientJsonObj = (JSONObject) JSONValue.parse(clientjson);

		ObjectMapper obm = new ObjectMapper();
		try {
			com.helius.utils.ClientDetail clientdetail = obm.readValue(clientjson, com.helius.utils.ClientDetail.class);

			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ClientDetail clientDetail = getClientDetail(clientdetail);
			
			int clientid = clientdetail.getClientId();
			clientDetail.setClientId(clientid);
			session.evict(clientDetail);
			session.merge(clientDetail);
		
			List<ClientLeavePolicy> clientLeavePolicyList = getClientLeavePolicy(clientdetail);
			for(ClientLeavePolicy clientLeavePolicy : clientLeavePolicyList ) {
				if (clientLeavePolicy != null) {
					clientLeavePolicy.setClientId(clientid);
					session.evict(clientLeavePolicy);
					session.merge(clientLeavePolicy);
				}
			}
			ClientReimbursementPolicy clientReimbursementPolicy = getClientReimbursementPolicy(clientdetail);
			if (clientReimbursementPolicy != null) {
				clientReimbursementPolicy.setClientId(clientid);
				if(clientReimbursementPolicy.getClientReimbursementPolicyid() > 0) {
					session.evict(clientReimbursementPolicy);
					session.merge(clientReimbursementPolicy);
				} else {
					session.save(clientReimbursementPolicy);
				}
				
			}

			List<ClientReimbursementCategory> clientReimbursementCategories = getClientReimbursementCategories(
					clientdetail);
			if (clientReimbursementCategories != null) {

				List<ClientReimbursementCategory> clientReimbursementCategories_fromDB = getClientReimbursementCategories_fromDB(
						clientid);

				for (ClientReimbursementCategory crc : clientReimbursementCategories) {
					crc.setClientId(clientid);
					if (crc.getClientReimbursementCategoryId() > 0) {
						session.evict(crc);
						session.merge(crc);
						/*for (ClientReimbursementCategory crc_db : clientReimbursementCategories_fromDB) {
							if (crc_db.getType().equalsIgnoreCase(crc.getType())) {
								session.evict(crc);
								session.merge(crc);
								break;

							}
						}*/
					} else {
						for (ClientReimbursementCategory crc_db : clientReimbursementCategories_fromDB) {
							if (crc_db.getType().equalsIgnoreCase(crc.getType())) {
								crc.setClientReimbursementCategoryId(crc_db.getClientReimbursementCategoryId());
								session.evict(crc_db);
								session.merge(crc);
								break;

							}
						}
					}
				}
			}

			Map<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> clienGroups = getClientGroupDetail(
					clientdetail);
			if (clienGroups != null) {
				
				for (Entry<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> entry : clienGroups
						.entrySet()) {
					com.helius.entities.ClientGroupDetail cgde = entry.getKey();
					cgde.setClientId(clientid);
					
					if(cgde.getClientGroupDetailsId() != 0) {
						session.evict(cgde);
						session.merge(cgde);
					} else {
						saveClienGroup(session, entry,  clientid);
						continue;
					}
					
					
					com.helius.utils.ClientGroupDetail cgd = entry.getValue();
					List<com.helius.entities.ClientGroupLeavePolicy> cglp = getClientGroupLeavePolicy(cgd);
					if (cglp != null) {
						for(com.helius.entities.ClientGroupLeavePolicy item : cglp) {
							
							if(item.getCLient_gorup_leave_policyid() != 0) {
								session.evict(item);
								session.merge(item);
							} else {
								item.setClient_group_details_id(cgd.getClientGroupDetailsId());
								session.save(item);
							}
							
						}
						
					}
					

					List<com.helius.entities.ClientGroupReimbursementCategory> clientGroupReimbursementCategories_entities = getClientGroupReimbursementCategories(
							cgd);
					if (clientGroupReimbursementCategories_entities != null
							&& !clientGroupReimbursementCategories_entities.isEmpty()) {
						for (com.helius.entities.ClientGroupReimbursementCategory crc : clientGroupReimbursementCategories_entities) {
							crc.setClientGroupGetailsId(cgd.getClientGroupDetailsId());
							
							session.evict(crc);
							session.merge(crc);
						}
					}

				}
				
				// throw new HibernateException("Dummy exception");
			}
			
			Map<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> deleteGroups = getdeleteGroupDetail(clientdetail);
			
			if (deleteGroups != null) {
				for (Entry<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> entry : deleteGroups
							.entrySet()) {
						com.helius.entities.ClientGroupDetail cgde = entry.getKey();
						cgde.setClientId(-1*clientid);
	
						/*com.helius.utils.ClientGroupDetail cgd = entry.getValue();
						cgde.setClientGroupDetailsId(cgd.getClientGroupDetailsId());
						cgde.setClientGroup(cgd.getClientGroup());
						List<com.helius.entities.ClientGroupLeavePolicy> cglp = getClientGroupLeavePolicy(cgd);
						if (cglp != null) {
							for (com.helius.entities.ClientGroupLeavePolicy item : cglp) {
								item.setClient_group_details_id(cgd.getClientGroupDetailsId());
								session.delete(item);
							}
								
						}
						if (cglp != null) {
	
							List<com.helius.entities.ClientGroupReimbursementCategory> clientGroupReimbursementCategories_entities = getClientGroupReimbursementCategories(
									cgd);
							if (clientGroupReimbursementCategories_entities != null
									&& !clientGroupReimbursementCategories_entities.isEmpty()) {
								for (com.helius.entities.ClientGroupReimbursementCategory crc : clientGroupReimbursementCategories_entities) {
									crc.setClientGroupGetailsId(cgd.getClientGroupDetailsId());
	
									session.delete(crc);
								}
							}
	
							
						}*/
						if (cgde.getClientGroupDetailsId() != 0) {
							
							session.evict(cgde);
							session.merge(cgde);

					}
				}
			}
			copyfiles(request);
			transaction.commit();
		
		} catch (HibernateException e) {

			deleteFiles(copied_with_success);
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} catch (Exception e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Failed to Save the Client Details" + e.getCause().getMessage());
		} finally {
			session.close();
		}
	}
		
	private void saveClienGroup(Session session, Entry<com.helius.entities.ClientGroupDetail, com.helius.utils.ClientGroupDetail> entry, int clientid) {
		com.helius.entities.ClientGroupDetail cgde = entry.getKey();
		cgde.setClientId(clientid);
		int clientgorupdetailsid = (int)session.save(cgde);
		com.helius.utils.ClientGroupDetail cgd = entry.getValue();
		List<com.helius.entities.ClientGroupLeavePolicy> cglp = getClientGroupLeavePolicy(cgd);
		if (cglp != null) {
			for(com.helius.entities.ClientGroupLeavePolicy item : cglp) {
				item.setClient_group_details_id(clientgorupdetailsid);
				session.save(item);
			}
			
		}
		

		List<com.helius.entities.ClientGroupReimbursementCategory> clientGroupReimbursementCategories_entities = getClientGroupReimbursementCategories(
				cgd);
		if (clientGroupReimbursementCategories_entities != null
				&& !clientGroupReimbursementCategories_entities.isEmpty()) {
			for (com.helius.entities.ClientGroupReimbursementCategory crc : clientGroupReimbursementCategories_entities) {
				crc.setClientGroupGetailsId(clientgorupdetailsid);
				session.save(crc);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.helius.dao.IClientDAO#getFile(java.lang.String)
	 */
	@Override
	public ResponseEntity<byte[]> getFile(String filename) throws Throwable {
		String clientfilelocation = Utils.getProperty("fileLocation") + File.separator + "client" + File.separator + filename;
		
		byte[] files = null;		
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(clientfilelocation);
			files = IOUtils.toByteArray(fi);
			fi.close();
			//int lastIndexOf = url.lastIndexOf(".");
			//extname = url.substring(lastIndexOf);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}	
		catch (IOException e) {
			e.printStackTrace();
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files,HttpStatus.OK);
		return responseEntity;	
		
	}
	
	private List<com.helius.entities.ClientReimbursementCategory> getClientReimbursementCategories_fromDB(int client_id) {

		List<com.helius.entities.ClientReimbursementCategory> clientReimbureseCategories = new ArrayList<com.helius.entities.ClientReimbursementCategory>();

		Session session = null;
		try {
			session = sessionFactory.openSession();

			Query query2 = session
					.createSQLQuery("select * from client_reimbursement_categories c where c.client_id=:client_id")
					.addEntity(com.helius.entities.ClientReimbursementCategory.class)
					.setParameter("client_id", client_id);

			List clientreimbursements = query2.list();
			if (!clientreimbursements.isEmpty()) {

				Iterator iter = clientreimbursements.iterator();
				while (iter.hasNext()) {
					com.helius.entities.ClientReimbursementCategory temp = null;
					temp = (com.helius.entities.ClientReimbursementCategory) iter.next();
					clientReimbureseCategories.add(temp);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return clientReimbureseCategories;
	}
	
	private List<com.helius.entities.ClientGroupReimbursementCategory> getClientGroupReimbursementCategories__fromDB(int client_group_id) {

		List<com.helius.entities.ClientGroupReimbursementCategory> clientGroupReimbureseCategories = new ArrayList<com.helius.entities.ClientGroupReimbursementCategory>();

		Session session = null;
		try {
			session = sessionFactory.openSession();

			Query query2 = session
					.createSQLQuery(
							"select * from client_group_reimbursement_categories c where c.client_group_details_id=:client_group_id")
					.addEntity(com.helius.entities.ClientGroupReimbursementCategory.class)
					.setParameter("client_group_id", client_group_id);

			List clientreimbursements = query2.list();
			if (!clientreimbursements.isEmpty()) {

				Iterator iter1 = clientreimbursements.iterator();
				while (iter1.hasNext()) {
					com.helius.entities.ClientGroupReimbursementCategory temp = null;
					temp = (com.helius.entities.ClientGroupReimbursementCategory) iter1.next();
					
					clientGroupReimbureseCategories.add(temp);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return clientGroupReimbureseCategories;
	}

	/* (non-Javadoc)
	 * @see com.helius.dao.IClientDAO#getHeliusData(java.lang.String)
	 */
	@Override
	public List<com.helius.utils.ClientDetail> getHeliusData() {
		Session session = null;
		List<com.helius.utils.ClientDetail> clientDetails = new ArrayList<com.helius.utils.ClientDetail> ();
		try {
			session = sessionFactory.openSession();
			
			
			//Query query = session.createSQLQuery("select client_name from client_details c where c.client_name like '%Helius%'");
			Query query = session.createSQLQuery("select client_name from client_details c where c.isheliusclient='true'");		       
	        List clients =query.list();
			if(!clients.isEmpty()) {
				Iterator<String> iter = clients.iterator();
				while(iter.hasNext()) {
					String clientname  =  iter.next();
					com.helius.utils.ClientDetail clientDetail = get(clientname);
					clientDetails.add(clientDetail);
					
				}
				
			}
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		return clientDetails;
	}

	
	public AllClientDetails getClient_Group_HiringManagerDetails() throws Throwable {	
		Session session = null;
		AllClientDetails allClientDetails = new AllClientDetails();
		List<Client_Group_HiringManagerDetails> client_Group_HiringManagerDetails_List = new ArrayList<Client_Group_HiringManagerDetails>();
		List<String> clientNames = new ArrayList<String>();
		session = sessionFactory.openSession();
 		try {	
			 Query query = session.createSQLQuery("select * from client_details c ")
					 .addEntity(com.helius.entities.ClientDetail.class);       
	        List clients =query.list();
	        Query query1 = session.createSQLQuery("select cg.* from client_details c , client_group_details cg where c.client_id=cg.client_id")
					 .addEntity(com.helius.entities.ClientGroupDetail.class); 
			List clientgroups = query1.list();			
			if(!clients.isEmpty()) {
				Iterator iter = clients.iterator();
				while(iter.hasNext()) {
					 Client_Group_HiringManagerDetails client_Group_HiringManagerDetails = new Client_Group_HiringManagerDetails();
					 List<String> hiringmanagers_list = new ArrayList<String>();
					 List<String> groupnames = new ArrayList<String>();
					 List<String> budgetowners_list = new ArrayList<String>();
					 Map<String,List<String>> lobList = new HashMap<String, List<String>>();
					 com.helius.entities.ClientDetail clientdetail_entity =(com.helius.entities.ClientDetail) iter.next();
					 if(clientdetail_entity.getTimesheetApproversAll() != null) {
						 if(clientdetail_entity.getTimesheetApproversAll() != null) {
							 String[] hiringmanagers = clientdetail_entity.getTimesheetApproversAll().split(",");
							 for(int i = 0; i < hiringmanagers.length ; i++){
								 hiringmanagers_list.add(hiringmanagers[i]);
							 }
						 }
						 if(clientdetail_entity.getBudgetownerList() != null) {
							 String[] budgetowners = clientdetail_entity.getBudgetownerList().split(",");
							 for(int i = 0; i < budgetowners.length ; i++){
								 budgetowners_list.add(budgetowners[i]);
							 }	 
						 }	 
					 }		 
					 if(!clientgroups.isEmpty()) {
						Iterator<com.helius.entities.ClientGroupDetail> iter1 = clientgroups.iterator();					
						while(iter1.hasNext()) {
							com.helius.entities.ClientGroupDetail cg = iter1.next();
							if(cg.getClientId() == clientdetail_entity.getClientId() ) {
								groupnames.add(cg.getClientGroup());
								List<String> lobLists = new ArrayList<String>();
								if(cg.getLobList() != null) {
									 String[] lob = cg.getLobList().split(",");
									 for(int i = 0; i < lob.length ; i++){
										 lobLists.add(lob[i]);
									 }
								 }
								lobList.put(cg.getClientGroup(), lobLists);
							}
						}
					}
					 clientNames.add(clientdetail_entity.getClientName());
					 client_Group_HiringManagerDetails.setClientname(clientdetail_entity.getClientName());
					 client_Group_HiringManagerDetails.setClientgroups(groupnames);
					 client_Group_HiringManagerDetails.setHiringmanagers(hiringmanagers_list);
					 client_Group_HiringManagerDetails.setBudgetowners(budgetowners_list);
					 client_Group_HiringManagerDetails.setLobList(lobList);
					 client_Group_HiringManagerDetails_List.add(client_Group_HiringManagerDetails);
				}
				
				
				allClientDetails.setAllClients(clientNames);
				allClientDetails.setClient_Group_HiringManagerDetails_List(client_Group_HiringManagerDetails_List);
			}
			
			
		} catch(HibernateException he) {
			he.printStackTrace();
			throw new Exception("Could not retrieve the client and group details");
		}
 		finally{
 			session.close();
 		}
		return allClientDetails;
	}
	
	public Map<Integer, List<com.helius.utils.ClientLeavePolicy>> getAllLeavePolicies() {
		Session session = null;
		Map<Integer, List<com.helius.utils.ClientLeavePolicy>> allPolicies = new HashMap<Integer, List<com.helius.utils.ClientLeavePolicy>>();
		try {
			session = sessionFactory.openSession();			
			Query query1 = session.createSQLQuery("select * from Client_Leave_Policy_New1 c")
					.addEntity(com.helius.entities.ClientLeavePolicy.class);
			List clientleavePolicy = query1.list();
			DozerBeanMapper dbm = new DozerBeanMapper();
			if (!clientleavePolicy.isEmpty()) {
				Iterator<com.helius.entities.ClientLeavePolicy> iter = clientleavePolicy.iterator();
				while (iter.hasNext()) {
					com.helius.entities.ClientLeavePolicy item = null;
					item = iter.next();
					com.helius.utils.ClientLeavePolicy item1 = dbm.map(item, com.helius.utils.ClientLeavePolicy.class);
					if (allPolicies.containsKey(new Integer(item1.getClientId()))) {
						List<com.helius.utils.ClientLeavePolicy> itemlist1 = allPolicies
								.get(new Integer(item1.getClientId()));
						itemlist1.add(item1);
						allPolicies.put(new Integer(item1.getClientId()), itemlist1);
					} else {
						List<com.helius.utils.ClientLeavePolicy> itemlist = new ArrayList<com.helius.utils.ClientLeavePolicy>();
						itemlist.add(item1);
						allPolicies.put(new Integer(item1.getClientId()), itemlist);
					}

				}

			}
		} catch (HibernateException he) {
			he.printStackTrace();
			
		} finally {
			session.close();
		}

		return allPolicies;
	}
	
	public static void main(String args[]) {
		
	}
}
