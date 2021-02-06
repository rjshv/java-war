package com.helius.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.entities.InvoiceAnnexure;

public interface InvoiceService {
	
	public String generateAnnexureForDah2(String month, String action,MultipartHttpServletRequest request) throws Throwable;

	public List<InvoiceAnnexure> getDah2AnnexureDashboard(String month) throws Throwable;

}
