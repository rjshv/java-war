package com.helius.managers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.dao.IChecklistDAO;
import com.helius.entities.Checklist;
import com.helius.entities.EmployeeChecklistItemAndMandatoryitems;
import com.helius.entities.Employee_CheckList;
import com.helius.entities.Employee_Checklist_Items;
import com.helius.entities.Employee_Checklist_Master;
import com.helius.utils.Status;

public class ChecklistManager {
	private IChecklistDAO checklistDAO = null;
	
	
	public IChecklistDAO getChecklistDAO() {
		return checklistDAO;
	}


	public void setChecklistDAO(IChecklistDAO checklistDAO) {
		this.checklistDAO = checklistDAO;
	}


	public List<Employee_Checklist_Master> getAllChecklist() throws Throwable {
	/*	String checkLists = null;
		try {
			checkLists = checklistDAO.getAllChecklist();
		} catch (Throwable e) {
			return checkLists;
		}
		return checkLists;*/
		return checklistDAO.getAllChecklist();

	}
	
	public Employee_Checklist_Master getChecklist(String masterId) throws Throwable {
		/*	String checkLists = null;
			try {
				checkLists = checklistDAO.getAllChecklist();
			} catch (Throwable e) {
				return checkLists;
			}
			return checkLists;*/
			return checklistDAO.getChecklist(masterId);

		}
	
	public Status saveChecklist(Checklist masterChecklist,MultipartHttpServletRequest request) throws Throwable {
		try{
		checklistDAO.saveChecklist(masterChecklist,request);
	} catch (Throwable e) {
		return new Status(false," Checklist Details Not Updated " + e.getMessage());
	}
	return new Status(true, "Checklist Details Updated Successfully.!");	
	}
	
	
	
	public ResponseEntity<byte[]> getCheckListFiles(String employeeChecklistMasterId) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	checklistDAO.getCheckListFiles(employeeChecklistMasterId);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	public ResponseEntity<byte[]> getItemFiles(String itemId) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	checklistDAO.getItemFiles(itemId);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	public List<EmployeeChecklistItemAndMandatoryitems> getEmployeeChecklistItems(String offerId,String checkListType) throws Throwable {
		List<EmployeeChecklistItemAndMandatoryitems> checkLists = null;
			try {
				checkLists = checklistDAO.getEmployeeChecklistItems(offerId,checkListType);
			} catch (Throwable e) {
				return checkLists;
			}
			return checkLists;
		}
	
	public Status saveEmployeeChecklistItem(Employee_CheckList employeeChecklist,MultipartHttpServletRequest request) throws Throwable {
		try{
		checklistDAO.saveEmployeeChecklistItem(employeeChecklist,request);
	} catch (Throwable e) {
		return new Status(false," Employee Checklist Details Not Updated " + e.getMessage());
	}
	return new Status(true, "Employee Checklist Details Updated Successfully.!");	
	}
	
	public Status updateEmployeeChecklistItem(Employee_CheckList employeeChecklist,MultipartHttpServletRequest request) throws Throwable {
		try{
		checklistDAO.updateEmployeeChecklistItem(employeeChecklist,request);
	} catch (Throwable e) {
		return new Status(false," Employee Checklist Details Not Updated " + e.getMessage());
	}
	return new Status(true, "Employee Checklist Details Updated Successfully.!");	
	}

	
	public List<Employee_Checklist_Items> getEmployeeItems(String offerId,String checkListType) throws Throwable {
		List<Employee_Checklist_Items> checkLists = null;
			try {
				checkLists = checklistDAO.getemployeeItems(offerId,checkListType);
			} catch (Throwable e) {
				return checkLists;
			}
			return checkLists;
		}
	
	public Status sendChecklistEmailNotification(String jsondata, String offerId, String checkListType)
			throws Throwable {
		try {
			checklistDAO.sendChecklistEmailNotification(jsondata, offerId, checkListType);
		} catch (Throwable e) {
			return new Status(false, " Unable to send email ");
		}
		return new Status(true, "Email Sent Successfully.!");
	}
	
	/*public Status sendChecklistReminder(String offerId,String checkListType) throws Throwable {
		try{
		checklistDAO.sendChecklistReminder(offerId,checkListType);
	} catch (Throwable e) {
		return new Status(false," Unable To Send Reminder Email ");
	}
	return new Status(true, "Reminder Email Sent Successfully.!");	
	}*/
}
