package com.helius.managers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.dao.BeelineTimesheetDashboard;
import com.helius.dao.InvoiceService;
import com.helius.entities.InvoiceAnnexure;

public class InvoiceManager {
	
	
	InvoiceService invoiceService;
	

	public InvoiceService getInvoiceService() {
		return invoiceService;
	}


	public void setInvoiceService(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}


	public String generateAnnexureForDah2(String json, String action, MultipartHttpServletRequest mandayFile) {
		String result = null;
		try {
			result = invoiceService.generateAnnexureForDah2(json, action,mandayFile);
		} catch (Throwable e) {
			return " Failed to process Invoice annexure for Dah2" + e.getMessage();
		}
		return result;
	}
	
	public List<InvoiceAnnexure> getDah2AnnexureDashboard(String Month) throws Throwable {
		List<InvoiceAnnexure> annexure = null;
		try {
			annexure = invoiceService.getDah2AnnexureDashboard(Month);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch annexure for dah2 dashboard Details");
		}
		return annexure;
	}
}
