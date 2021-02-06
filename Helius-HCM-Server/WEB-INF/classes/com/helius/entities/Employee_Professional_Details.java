/**
 * 
 */
package com.helius.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tirumala
 * 22-Feb-2018
 */

@Entity
@Table(name="Employee_Professional_Details")
public class Employee_Professional_Details extends HeliusEntity{

	@Id
	@Column
	private int employee_professional_details_id;
	/**
	 * @return the employee_professional_details_id
	 */
	public int getEmployee_professional_details_id() {
		return employee_professional_details_id;
	}

	/**
	 * @param employee_professional_details_id the employee_professional_details_id to set
	 */
	public void setEmployee_professional_details_id(int employee_professional_details_id) {
		this.employee_professional_details_id = employee_professional_details_id;
	}

	@Column
	private String employee_id;
/*	@Column
	private String qualification;
	@Column
	private float previous_experience;
	@Column
	private String previous_company;
	@Column
	private float current_experience;
	@Column
	private float total_experience;
	*/
	@Column
	private String category;
	@Column
	private String sub_category;
	@Column
	private String skill_name;
	@Column
	private String work_experience;
	@Column
	private String work_experience_level;
	
	/**
	 * 
	 */
	public Employee_Professional_Details() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the employee_id
	 */
	public String getEmployee_id() {
		return employee_id;
	}
	/**
	 * @param employee_id the employee_id to set
	 */
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSub_category() {
		return sub_category;
	}

	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}

	public String getSkill_name() {
		return skill_name;
	}

	public void setSkill_name(String skill_name) {
		this.skill_name = skill_name;
	}

	public String getWork_experience() {
		return work_experience;
	}

	public void setWork_experience(String work_experience) {
		this.work_experience = work_experience;
	}

	public String getWork_experience_level() {
		return work_experience_level;
	}

	public void setWork_experience_level(String work_experience_level) {
		this.work_experience_level = work_experience_level;
	}

	/* (non-Javadoc)
	 * @see com.helius.entities.IEntity#toJSON()
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
