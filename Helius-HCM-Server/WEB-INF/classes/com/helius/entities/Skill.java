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
@Table(name="Skill")
public class Skill {

	@Column
	private String skillname;
	@Id
	@Column
	private int skillid;
	
	/**
	 * 
	 */
	public Skill() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the skillname
	 */
	public String getSkillname() {
		return skillname;
	}

	/**
	 * @param skillname the skillname to set
	 */
	public void setSkillname(String skillname) {
		this.skillname = skillname;
	}

	/**
	 * @return the skillid
	 */
	public int getSkillid() {
		return skillid;
	}

	/**
	 * @param skillid the skillid to set
	 */
	public void setSkillid(int skillid) {
		this.skillid = skillid;
	}

}
