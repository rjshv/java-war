package com.helius.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.helius.dao.MyRevisionListener;

@Entity
@RevisionEntity(MyRevisionListener.class)
@Table(name="MyRevision")
public class MyRevision extends DefaultRevisionEntity {
//public class MyRevision{
	
	@Column(name="userName")
	private String userName;
	
	private Timestamp modifiedDate;

	
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
/*@Id
@GeneratedValue
@RevisionNumber
private int id;

@RevisionTimestamp
private long timestamp;

private String userName;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public long getTimestamp() {
	return timestamp;
}

public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
}

public String getUserName() {
	return userName;
}

public void setUserName(String userName) {
	this.userName = userName;
}
*/

}
