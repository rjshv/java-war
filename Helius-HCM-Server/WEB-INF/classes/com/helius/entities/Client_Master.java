package com.helius.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Client_Master")
public class Client_Master {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int client_master_id;
	@Column
	private String client_name;
	@Column
	private String group_team;
	@Column
	private String sub_group;
	
	
	public String getGroup_team() {
		return group_team;
	}
	public void setGroup_team(String group_team) {
		this.group_team = group_team;
	}
	public int getClient_master_id() {
		return client_master_id;
	}
	public void setClient_master_id(int client_master_id) {
		this.client_master_id = client_master_id;
	}
	public String getClient_name() {
		return client_name;
	}
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	
	public String getSub_group() {
		return sub_group;
	}
	public void setSub_group(String sub_group) {
		this.sub_group = sub_group;
	}
}
