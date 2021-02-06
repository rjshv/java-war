/**
 * 
 */
package com.helius.dao;

import com.helius.entities.Employee_Personal_Details;

/**
 * @author Tirumala
 * 22-Feb-2018
 */
public interface IEmployeePersonalDetailsDAO {
	
	public Employee_Personal_Details get(String employeeid);
	public void save(Employee_Personal_Details employeePersonalDetails);
	public void update(Employee_Personal_Details employeePersonalDetails);
	public void delete(String employeeid);

}
