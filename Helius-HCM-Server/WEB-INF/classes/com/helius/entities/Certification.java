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
@Table(name="Certification")
public class Certification implements IEntity{

	@Column
	private String certificationname;
	@Id
	@Column
	private int certificationid;
	
	/**
	 * 
	 */
	public Certification() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the certificationname
	 */
	public String getCertificationname() {
		return certificationname;
	}

	/**
	 * @param certificationname the certificationname to set
	 */
	public void setCertificationname(String certificationname) {
		this.certificationname = certificationname;
	}

	/**
	 * @return the certificationid
	 */
	public int getCertificationid() {
		return certificationid;
	}

	/**
	 * @param certificationid the certificationid to set
	 */
	public void setCertificationid(int certificationid) {
		this.certificationid = certificationid;
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
