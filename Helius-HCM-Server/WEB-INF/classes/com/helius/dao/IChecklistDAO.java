package com.helius.dao;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.helius.entities.Checklist;
import com.helius.entities.EmployeeChecklistItemAndMandatoryitems;
import com.helius.entities.Employee_CheckList;
import com.helius.entities.Employee_Checklist_Items;
import com.helius.entities.Employee_Checklist_Master;

public interface IChecklistDAO {

	public List<Employee_Checklist_Master> getAllChecklist() throws Throwable;
	public Employee_Checklist_Master getChecklist(String masterId) throws Throwable;

public void saveChecklist(Checklist masterChecklist,MultipartHttpServletRequest request) throws Throwable;

public ResponseEntity<byte[]> getCheckListFiles(String employeeChecklistMasterId) throws Throwable;

public List<EmployeeChecklistItemAndMandatoryitems> getEmployeeChecklistItems(String offerId,String checkListType) throws Throwable;

public void saveEmployeeChecklistItem(Employee_CheckList employeeChecklist, MultipartHttpServletRequest request) throws Throwable;
public void updateEmployeeChecklistItem(Employee_CheckList employeeChecklist, MultipartHttpServletRequest request) throws Throwable;

public List<Employee_Checklist_Items> getemployeeItems(String offerId,String checkListType) throws Throwable;
public ResponseEntity<byte[]> getItemFiles(String itemId) throws Throwable;
//public void sendChecklistReminder(String offerId, String checklistType) throws Throwable;
public void sendChecklistEmailNotification(String jsonData, String offerId, String checklistType) throws Throwable;


	}
