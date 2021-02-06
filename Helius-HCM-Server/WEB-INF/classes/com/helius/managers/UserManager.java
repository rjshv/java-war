package com.helius.managers;

import java.util.List;

import org.hibernate.Session;

import com.helius.utils.User;
import com.helius.service.UserService;

import com.helius.utils.Logindetails;

import com.helius.utils.Status;


public class UserManager {

	UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public Status verifyEmailadress(String employeeid,String appUrl) {
		String status = "";
		try {
			status = userService.verifyForgotEmailAddress(employeeid,appUrl);
		} catch (Throwable e) {
			e.printStackTrace();
			return new Status(false,"Failed to send verify email link Please Contact HR !");
		}
		return new Status(true, status);
	}
	
	public Status resetPassword(String base64Credentials,String token,String fg) {
		String status = "";
		try {
			status = userService.resetpswd(base64Credentials,token,fg);
		} catch (Throwable e) {
			e.printStackTrace();
			return new Status(false,"Failed to update Password Please Contact HR !");
		}
		return new Status(true, status);
	}
	
	public Status createUser(User user,String appUrl) {
		com.helius.entities.User user_entity = new com.helius.entities.User();
		
		user_entity.setActive("Yes");
		user_entity.setId(user.getId());
		user_entity.setUserid(user.getUserid()); 
		user_entity.setPassword(user.getPassword());
		user_entity.setUsername(user.getUsername());
		user_entity.setEdit(user.getEdit());
		user_entity.setView(user.getView());
		user_entity.setUserLoginAttempts("0");
		String roles = "";
		for(String role : user.getRole()){
			roles = role + "," + roles;
		}
		roles = roles.substring(0, roles.lastIndexOf(","));
		user_entity.setRole(roles);
		
		String countries = "";
		for(String country : user.getCountry()){
			countries = country + "," + countries;
		}
		countries = countries.substring(0, countries.lastIndexOf(","));
		user_entity.setCountry(countries);
		try {
			userService.createUser(user_entity,appUrl);
		} catch (Throwable e) {
			
			return new Status(false, e.getMessage());
		}
		return new Status(true, "created user successfully");
	}
	
	public Status updateUser(User user) {
		com.helius.entities.User user_entity = new com.helius.entities.User();
		
		user_entity.setId(user.getId());
		user_entity.setUserid(user.getUserid()); 
		user_entity.setPassword(user.getPassword());
		user_entity.setUsername(user.getUsername());
		user_entity.setEdit(user.getEdit());
		user_entity.setView(user.getView());
		user_entity.setActive(user.getActive());
		user_entity.setUserLoginAttempts(user.getUserLoginAttempts());
		String roles = "";
		for(String role : user.getRole()){
			roles = role + "," + roles;
		}
		roles = roles.substring(0, roles.lastIndexOf(","));
		user_entity.setRole(roles);
		
		String countries = "";
		for(String country : user.getCountry()){
			countries = country + "," + countries;
		}
		countries = countries.substring(0, countries.lastIndexOf(","));
		user_entity.setCountry(countries);
		try {
			userService.updateUser(user_entity);
		} catch (Throwable e) {
			
			return new Status(false, e.getMessage());
		}
		return new Status(true, "updated user successfully");
	}
	
	public List<com.helius.utils.User> getAllUsers() throws Throwable {
		return userService.getAllUsers();
		
		
	}

	
	public Logindetails validateUser(String username, String password) throws Throwable {
		return userService.validateUser(username, password);
	}
	public com.helius.utils.User getUser(String userid) throws Throwable{
		return userService.getUser(userid);
	}
}
