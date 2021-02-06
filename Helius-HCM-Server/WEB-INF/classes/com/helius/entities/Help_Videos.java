package com.helius.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Table(name="Help_Videos")
public class Help_Videos {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int help_videos_id;
	@Column
	private String help_name;
	@Column
	private String videos_url;
	public int getHelp_videos_id() {
		return help_videos_id;
	}
	public void setHelp_videos_id(int help_videos_id) {
		this.help_videos_id = help_videos_id;
	}
	public String getHelp_name() {
		return help_name;
	}
	public void setHelp_name(String help_name) {
		this.help_name = help_name;
	}
	public String getVideos_url() {
		return videos_url;
	}
	public void setVideos_url(String videos_url) {
		this.videos_url = videos_url;
	}
	
	
}
