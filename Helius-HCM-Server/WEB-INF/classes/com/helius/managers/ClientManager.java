/**
 * 
 */
package com.helius.managers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.dao.IClientDAO;
import com.helius.entities.ClientDetail;
import com.helius.utils.AllClientDetails;
import com.helius.utils.Client_Group_HiringManagerDetails;
import com.helius.utils.Status;

/**
 * @author Tirumala
 * 25-Jul-2018
 */
public class ClientManager {

	private IClientDAO clientDAO = null;
	/**
	 * @return the clientDAO
	 */
	public IClientDAO getClientDAO() {
		return clientDAO;
	}
	/**
	 * @param clientDAO the clientDAO to set
	 */
	public void setClientDAO(IClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}
	/**
	 * 
	 */
	public ClientManager() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Status addClient(ClientDetail clientdetail, MultipartHttpServletRequest request) {
		/*try {
			if( clientdetail.getClientLeavePolicy() != null ) {
				clientdetail.getClientLeavePolicy().setClientDetail(clientdetail);
			}
			if(clientdetail.getClientReimbursementPolicy() != null) {
				clientdetail.getClientReimbursementPolicy().setClientDetail(clientdetail);				
			}
			if(clientdetail.getClientReimbursementCategories() != null && !clientdetail.getClientReimbursementCategories().isEmpty()) {
				for(ClientReimbursementCategory crc : clientdetail.getClientReimbursementCategories()) {
					crc.setClientDetail(clientdetail);
				}
			}
			if(clientdetail.getClientGroupDetails() != null && !clientdetail.getClientGroupDetails().isEmpty()) {
				List<ClientGroupDetail> clientgrooupdetails = clientdetail.getClientGroupDetails();
				for(ClientGroupDetail cgd : clientgrooupdetails) {
					cgd.setClientDetail(clientdetail);
					if( cgd.getClientGroupLeavePolicy() != null ) {
						cgd.getClientGroupLeavePolicy().setClientGroupDetail(cgd);
					}
					if(cgd.getClientGroupReimbursementPolicy() != null) {
						cgd.getClientGroupReimbursementPolicy().setClientGroupDetail(cgd);
					}
					if(cgd.getClientGroupReimbursementCategories() != null && !cgd.getClientGroupReimbursementCategories().isEmpty()) {
						for(ClientGroupReimbursementCategory cgrc : cgd.getClientGroupReimbursementCategories()) {
							cgrc.setClientGroupDetail(cgd);
						}
					}
				}
			}
					
			clientDAO.save(clientdetail,request);
		} catch (Throwable e) {
			return new Status(false, e.getMessage());
		}*/
		return new Status(true, "Client Details Saved Successfully.!");
	}

	public Status addClient(String  clientjson, MultipartHttpServletRequest request) {
		try {
			clientDAO.save(clientjson,request);
		} catch (Throwable e) {
			return new Status(false, e.getMessage());
		}
		return new Status(true, "Client Details Saved Successfully.!");
	}
	
	
	public Status updateClient(String  clientjson, MultipartHttpServletRequest request) {
		try {
			clientDAO.update(clientjson,request);
		} catch (Throwable e) {
			return new Status(false, e.getMessage());
		}
		return new Status(true, "Client Details Saved Successfully.!");
	}
	public com.helius.utils.ClientDetail  getClient(String clientName) throws Throwable{
		com.helius.utils.ClientDetail clientdetail = null;
		try {
			clientdetail = clientDAO.get(clientName);
			
		} catch (Throwable e) {
			throw new Exception(e.getMessage());
		}
		return clientdetail;
	}

	
	public List<com.helius.utils.ClientDetail>  getHeliusData() throws Throwable{
		List<com.helius.utils.ClientDetail> clientdetails = null;
		try {
			clientdetails = clientDAO.getHeliusData();
			
		} catch (Throwable e) {
			throw new Exception(e.getMessage());
		}
		return clientdetails;
	}
	public List<String> getAllClientNames() throws Throwable {
		
		
		return clientDAO.getAllClientNames();
		
	}
	
	public ResponseEntity<byte[]> getFile(String filename) {
		ResponseEntity<byte[]> response = null;
		try {
			response =	clientDAO.getFile(filename);
		} catch (Throwable e) {
				return response;
		}
		return response;
	}
	
	public AllClientDetails getClient_Group_HiringManagerDetails() throws Throwable {
		return clientDAO.getClient_Group_HiringManagerDetails();
	}
}
