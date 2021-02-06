package com.helius.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Employee;
import com.helius.entities.User;
import com.helius.entities.Users;
import com.helius.managers.EmployeeManager;
import com.helius.managers.UserManager;
import com.helius.service.UserService;
import com.helius.service.UserServiceImpl;
import com.helius.utils.Logindetails;
import com.helius.utils.Status;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	private UserServiceImpl userServiceImp;
	@Autowired
	private UserManager userManager;
	@Autowired
	ApplicationContext context;
	@Autowired
	private EmployeeManager employeemanager;
	
	@CrossOrigin
	@RequestMapping(value = "/verifyEmailAddress", method = RequestMethod.GET)
	public ResponseEntity<String> verifyEmailAdress(@RequestParam String employeeid,String appUrl) throws Throwable {
		Status status = null;
		status = userManager.verifyEmailadress(employeeid,appUrl);
	return new ResponseEntity<String>("{\"response\":\"" + status.getMessage() + "\"}",HttpStatus.OK);
	}
	

	@CrossOrigin
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> resetPwd(@RequestHeader("Authorization") String Authorization,
			String token, String fgt) {
		Status status = null;
		try {
			final String authorization = Authorization;
			String base64Credentials = authorization.substring("Basic".length()).trim();
				status = userManager.resetPassword(base64Credentials, token, fgt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>("{\"response\":\"" + status.getMessage() + "\"}", HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "user/createuser", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public @ResponseBody String createUser(@RequestParam("user") String userjson, String appUrl) {
		System.out.println("userjson : " + userjson);
		ObjectMapper obm = new ObjectMapper();
		Status status = null;
		com.helius.utils.User user;
		try {
			user = obm.readValue(userjson, com.helius.utils.User.class);
			UserManager userManager = (UserManager) context.getBean("userManager");
			status = userManager.createUser(user, appUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "{\"response\":\"" + status.getMessage() + "\"}";
	}

	@CrossOrigin
	@RequestMapping(value = "user/updateuser", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public @ResponseBody String updateUser(@RequestParam("user") String userjson) {
		System.out.println("updateuser userjson : " + userjson);
		ObjectMapper obm = new ObjectMapper();
		Status status = null;
		com.helius.utils.User user;
		try {
			user = obm.readValue(userjson, com.helius.utils.User.class);
			UserManager userManager = (UserManager) context.getBean("userManager");
			status = userManager.updateUser(user);
			/*
			 * com.helius.utils.User existing_user =
			 * userManager.getUser(user.getUserid());
			 * if(existing_user.getUserid() == user.getUserid()) { status =
			 * userManager.updateUser(user); } else { status =
			 * userManager.createUser(user); }
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "{\"response\":\"" + status.getMessage() + "\"}";
	}

	@CrossOrigin
	@RequestMapping(value = "user/deleteuser", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public @ResponseBody String deleteUser(@RequestParam("user") String userjson) {
		System.out.println("delete user userjson : " + userjson);
		ObjectMapper obm = new ObjectMapper();
		Status status = null;
		com.helius.utils.User user;
		try {
			user = obm.readValue(userjson, com.helius.utils.User.class);
			UserManager userManager = (UserManager) context.getBean("userManager");

			status = userManager.updateUser(user);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return "{\"response\":\"" + status.getMessage() + "\"}";
	}

	@CrossOrigin
	@RequestMapping(value = "user/getAllUsers", method = RequestMethod.GET, produces = { "multipart/form-data" })
	public @ResponseBody String getAllUsers() {
		UserManager userManager = (UserManager) context.getBean("userManager");
		ObjectMapper obm = new ObjectMapper();
		String response = null;
		try {
			List<com.helius.utils.User> users = userManager.getAllUsers();
			response = obm.writeValueAsString(users);
		} catch (Throwable e) {
			response = e.getMessage();

		}
		return response;
	}

	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String login(@RequestHeader("Authorization") String Authorization) {
		final String authorization = Authorization;
		System.out.println("===authorization==" + authorization);
		// final String authorization = httpRequest.getHeader("Authorization");

		String base64Credentials = authorization.substring("Basic".length()).trim();
		String credentials = new String(Base64.getDecoder().decode(base64Credentials));
		final String[] values = credentials.split(":", 2);
		String usrId = values[0];
		String password = values[1];

		Logindetails logindetails;
		String response = "";
		try {
			logindetails = userManager.validateUser(usrId, password);
			ObjectMapper obm = new ObjectMapper();
			response = obm.writeValueAsString(logindetails);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	@CrossOrigin
	@RequestMapping(value = "user/changepassword", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public @ResponseBody String changePassword(@RequestParam("user") String userjson) {
		System.out.println("change password userjson : " + userjson);
		ObjectMapper obm = new ObjectMapper();
		Status status = null;
		com.helius.utils.User user;
		try {
			user = obm.readValue(userjson, com.helius.utils.User.class);
			UserManager userManager = (UserManager) context.getBean("userManager");

			status = userManager.updateUser(user);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (status.isOk()) {
			return "{\"response\":\"" + "change password is successful" + "\"}";
		}
		return "{\"response\":\"" + status.getMessage() + "\"}";
	}

}
