/**
 * 
 */
package com.helius.dao;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.entities.*;
import com.helius.utils.*;
/**
 * @author Tirumala
 * 25-Jul-2018
 */
public interface IClientDAO {
	public com.helius.utils.ClientDetail get(String clientName);
	public List<com.helius.utils.ClientDetail> getHeliusData();
	public void update(com.helius.entities.ClientDetail clientDetail);
	public void save(com.helius.entities.ClientDetail clientDetail) throws Throwable;
	/**
	 * @param clientDetail
	 * @param request
	 * @throws Throwable
	 */
	void save(com.helius.entities.ClientDetail clientDetail, MultipartHttpServletRequest request) throws Throwable;
	void save(String  clientjson, MultipartHttpServletRequest request) throws Throwable;


	void update(String  clientjson, MultipartHttpServletRequest request) throws Throwable;
	public List<String> getAllClientNames() throws Throwable;
	public ResponseEntity<byte[]> getFile(String filename) throws Throwable;
	public AllClientDetails getClient_Group_HiringManagerDetails() throws Throwable; 

}
