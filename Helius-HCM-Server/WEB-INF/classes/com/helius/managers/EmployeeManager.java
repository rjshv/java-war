/**
 * 
 */
package com.helius.managers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.helius.dao.IEmployeeDAO;
import com.helius.entities.Employee;
import com.helius.entities.Employee_Offer_Details;
import com.helius.entities.Employee_Timesheet_Status;
import com.helius.entities.Help_Videos;
import com.helius.utils.Status;

/**
 * @author Tirumala
 * 23-Feb-2018
 */
public class EmployeeManager {

	IEmployeeDAO employeeDAO = null;
	
	/**
	 * @return the employeeDAO
	 */
	public IEmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}
	/**
	 * @param employeeDAO the employeeDAO to set
	 */
	public void setEmployeeDAO(IEmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
	/**
	 * 
	 */
	public EmployeeManager() {
		// TODO Auto-generated constructor stub
	}
	public Employee getEmployee(String employeeid) {
		
		
		//test employee
		/*Employee_Personal_Details employeePersonalDetails = new Employee_Personal_Details();
		employeePersonalDetails.setFirstname("padala");
		employeePersonalDetails.setLastname("tirumala");
		employeePersonalDetails.setContact_number("9000340993");
		employeePersonalDetails.setContact_address("Helius Pvt limited");
		employeePersonalDetails.setAdhar_number("12345678");
		employeePersonalDetails.setEmployee_id(employeeid);
		employeePersonalDetails.setPancard_number("abcde");
		employeePersonalDetails.setSex("M");
		employeePersonalDetails.setMarital_status("Married");
		employeePersonalDetails.setEmail("padalar@yahoo.com");
		java.sql.Date dob = Date.valueOf("1990-09-09");
		employeePersonalDetails.setDob(dob);
		employeePersonalDetails.setPhoto(null);
		employeePersonalDetails.setFin(null);
		employeePersonalDetails.setPassport_number(null);
		employeePersonalDetails.setNric(null);
		*/
		
		//IEmployeeDAO employeeDAO = (EmployeeDAOImpl) ctx.getBean("employeeDAO");
		Employee emp = null;
		try {
			emp = employeeDAO.get(employeeid);
		} catch (Throwable e) {
			return emp;
		}
		
		return emp;
	}
	
	public Status addEmployee(Employee employee,MultipartHttpServletRequest request,String offerId,String appUrl) {
		try {
			employeeDAO.save(employee,request,offerId,appUrl);
		} catch (Throwable e) {
			return new Status(false,e.getMessage());
		}
		return new Status(true, "Employee Details Saved Successfully.!");
	}
	
	public Status saveJobOffer(Employee employee,MultipartHttpServletRequest request) {
		try {
			employeeDAO.saveJobOffer(employee,request);
		} catch (Throwable e) {
			return new Status(false, "Offer Details Not Saved ..! " + e.getMessage());
		}
		return new Status(true, "Offer Details Saved Successfully !");
	}
	
	public ResponseEntity<byte[]> getEmployeeFiles(String employee_Id, String filetype) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	employeeDAO.getEmployeeFiles(employee_Id,filetype);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	
	public ResponseEntity<byte[]> getOfferFiles(String offerid, String filetype) {
		ResponseEntity<byte[]> response = null;
		try {
			response =	employeeDAO.getOfferFiles(offerid,filetype);
		} catch (Throwable e) {
				return response;
		}
		return response;
	}
	
	public Status empUpdate(Employee emp, MultipartHttpServletRequest request) {
		try {
			employeeDAO.empUpdate(emp, request);
		} catch (Throwable e) {
			return new Status(false,"Employee Details Not Updated " + e.getMessage());
		}
		return new Status(true, "Employee Details Updated Successfully.!");
	}
	
	public Status offUpdate(Employee emp, MultipartHttpServletRequest request) {
		try {
			employeeDAO.offUpdate(emp, request);
		} catch (Throwable e) {
			return new Status(false,"Offer Details Not Updated ");
		}
		return new Status(true, "Offer Details Updated Successfully.!");
	}
	
	public String workMasterpicklist() {
		String emp = null;
		try {
			emp = employeeDAO.WorkLocationPicklist();
		} catch (Throwable e) {
			return emp;
		}
		return emp;
	}
	
	public String generateEmployeeId(String offerId) {
		String result = null;
		try {
			result = employeeDAO.generateEmployeeId(offerId);
		} catch (Throwable e) {
			return result;
		}
		return result;
	}
	public String getAllOffers() {
		String offerdetailsByStatus = null;
		try {
			offerdetailsByStatus = employeeDAO.getAllOffers();
		} catch (Throwable e) {
			return offerdetailsByStatus;
		}
		return offerdetailsByStatus;
	}
	public String getOfferDashboard(String month) {
		String offerdetailsByStatus = null;
		try {
			offerdetailsByStatus = employeeDAO.getOfferDashboard(month);
		} catch (Throwable e) {
			return offerdetailsByStatus;
		}
		return offerdetailsByStatus;
	}
	public String getAllEmployees() {
		String empdetailsByStatus = null;
		try {
			empdetailsByStatus = employeeDAO.getAllEmployees();
		} catch (Throwable e) {
			return empdetailsByStatus;
		}
		return empdetailsByStatus;
	}
	public List<Object> getAllNewEmployees(String dateSelected) throws Throwable {
		List<Object> newjoinee = null;
		try {
			newjoinee = employeeDAO.getAllNewEmployee(dateSelected);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch New joinee Details");
		}
		return newjoinee;
	}
	public List<Object> getNewJoineeDashboard(String dateSelected,String fromdate, String thrudate) throws Throwable {
		List<Object> newjoinee = null;
		try {
			newjoinee = employeeDAO.getNewJoineeDashboard(dateSelected,fromdate,thrudate);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch New Joinee Employee Details");
		}
		return newjoinee;
	}
	public List<Object> getAllResignEmployees(String dateSelected,String fromdate,String thrudate) throws Throwable {
		List<Object> resignEmp = null;
		try {
			resignEmp = employeeDAO.getAllResignEmployee(dateSelected,fromdate,thrudate);
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch Resigned Employee Details");
		}
		return resignEmp;
	}
	public List<Object> getAllExitEmployee() throws Throwable {
		List<Object> exitEmp = null;
		try {
			exitEmp = employeeDAO.getAllExitEmployee();
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch Exited Employee Details");
		}
		return exitEmp;
	}
	public String getOpenOffers() {
		String openoffer = null;
		try {
			openoffer = employeeDAO.getOpenOffers();
		} catch (Throwable e) {
			return openoffer;
		}
		return openoffer;
	}
	
	public Employee getOfferbyID(String offerid) {
		Employee offerdetailsById = null;
		try {
			offerdetailsById = employeeDAO.getOfferDetails(offerid);
		} catch (Throwable e) {
			return offerdetailsById;
		}
		return offerdetailsById;
	}
	
	public String masterpicklist() {
		String emp = null;
		try {
			emp = employeeDAO.masterpicklist();
		} catch (Throwable e) {
			return emp;
		}
		return emp;
	}

	public List<Help_Videos> getAllHelpVideos() throws Throwable {
		List<Help_Videos> help_Videos = null;
		try {
			help_Videos = employeeDAO.getAllHelpVideos();
		} catch (Throwable e) {
			throw new Throwable("Failed to fetch  Help video details");
		}
		return help_Videos;
	}

	public List<Employee_Offer_Details> getOfferbyName(String offerName,String type) {
		List<Employee_Offer_Details> offerdetailsByName = null;
		try {
			offerdetailsByName = employeeDAO.getOfferDetailsByName(offerName,type);
		} catch (Throwable e) {
			return offerdetailsByName;
		}
		return offerdetailsByName;
	}
	public ResponseEntity<byte[]> getHelpFile(int help_videos_id) {
		ResponseEntity<byte[]> res = null;
		try {
			 res =	employeeDAO.getHelpVideoFIle(help_videos_id);
		} catch (Throwable e) {
				return res;
		}
		return res;
	}
	
	public Status saveOrUpdateHelpVideo(String help, MultipartHttpServletRequest request) {
			try {
				employeeDAO.saveOrUpdateHelpVideo(help, request);
			} catch (Throwable e) {
				return new Status(false,"Failed to save video file");
			}
			return new Status(true, "video saved successfully");
		}
	public Status deleteHelpVideo(String help_videos_id) {
		try {
			employeeDAO.deleteHelpVideo(help_videos_id);
		} catch (Throwable e) {
			return new Status(false,"Failed to delete video file");
		}
		return new Status(true, "video deleted successfully");	
	}
	
	/*public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");	    
		IEmployeeDAO employeeDAO = (EmployeeDAOImpl) ctx.getBean("employeeDAO");
		Employee_Personal_Details employeePersonalDetails = new Employee_Personal_Details();
		employeePersonalDetails.setFirstname("padala");
		employeePersonalDetails.setLastname("tirumala");
		employeePersonalDetails.setContact_number("9000340993");
		employeePersonalDetails.setContact_address("Helius Pvt limited");
		employeePersonalDetails.setAdhar_number("12345678");
		employeePersonalDetails.setEmployee_id("1235");
		employeePersonalDetails.setPancard_number("abcde");
		employeePersonalDetails.setSex("M");
		employeePersonalDetails.setMarital_status("Married");
		employeePersonalDetails.setDate_of_relieving(new Date(2017,01,01));
		employeePersonalDetails.setDate_of_joining(new Date(2016,01,01));
		Date dob = new Date(0, 0, 0);
		employeePersonalDetails.setDob(dob);
		employeePersonalDetails.setEmail("padalar1@yahoo.com");
		employeePersonalDetails.setFin("fin");
		employeePersonalDetails.setNric("nric");
		Employee employee = new Employee();
		employee.setEmployeePersonalDetails(employeePersonalDetails);
		Employee_Professional_Details employee_professional_details = new Employee_Professional_Details();
		employee_professional_details.setEmployee_id("1235");
		employee_professional_details.setQualification("M.tech");
		employee_professional_details.setPrevious_company("Portware");
		employee_professional_details.setCurrent_experience(1.5f);
		employee_professional_details.setPrevious_experience(16.5f);
		employee_professional_details.setTotal_experience(18.0f);
		//employee_professional_details.setEmployee_professional_details_id(1);
		employee.setEmployeeProfessionalDetails(employee_professional_details);
		Employee_Salary_Details employee_Salary_Details = new Employee_Salary_Details();
		employee_Salary_Details.setEmployee_id("1235");
		employee_Salary_Details.setBasic(10000);
		employee_Salary_Details.setCurrency("SGD");
		employee_Salary_Details.setBooks_periodicals(100);
		employee_Salary_Details.setChildren_education_allowance(100);
		employee_Salary_Details.setGratuity(100);
		employee_Salary_Details.setHra(0.4*10000);
		employee_Salary_Details.setLta(100);
		employee_Salary_Details.setMedical_insurance(1000);
		employee_Salary_Details.setPositional_allowance(1000);
		employee_Salary_Details.setTelephone_allowance(100);
		employee_Salary_Details.setTotal_cost_per_month(12000);
		employee_Salary_Details.setTotal_cost_per_anual(144000);
		employee.setEmployeeSalaryDetails(employee_Salary_Details);
		Employee_Assignments employee_Assignments = new Employee_Assignments();
		employee_Assignments.setEmployee_id("1235");
		employee_Assignments.setClient_name("DBS");
		employee_Assignments.setClient_manager("Raj");
		employee_Assignments.setCorporate_email("abc@dbs.com");
		employee_Assignments.setDesignation("Software engineer");
		employee_Assignments.setDesk_number("12345");
		employee_Assignments.setRecruiter("Pawan");
		
		employee_Assignments.setLine_Of_business("CST");
		employee_Assignments.setTeam("CST");
		employee.setEmployeeAssignment(employee_Assignments);
		
		try {
			employeeDAO.save(employee);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}*/
}
