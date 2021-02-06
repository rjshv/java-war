package com.helius.service;

import java.util.List;

import org.hibernate.Session;

import com.helius.entities.User;
import com.helius.entities.Users;

public interface UserService {
	
	public com.helius.utils.User getUser(String userid)throws Throwable;
	public String createUser(User user,String appUrl) throws Throwable;
	public String updateUser(User user) throws Throwable;
	public String deleteUser(User user) throws Throwable;
	public List<com.helius.utils.User> getAllUsers() throws Throwable;
	public com.helius.utils.Logindetails validateUser(String username, String password) throws Throwable;
	public String resetpswd(String base64Credentials, String token, String fg) throws Throwable;
	public String verifyForgotEmailAddress(String employeeid, String appUrl) throws Throwable;


}