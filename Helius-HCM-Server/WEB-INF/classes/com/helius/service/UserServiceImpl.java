package com.helius.service;


import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import com.helius.entities.Employee;
import com.helius.entities.User;
import com.helius.entities.Users;
import com.helius.managers.EmployeeManager;
import com.helius.managers.UserManager;
import com.helius.utils.Logindetails;

@Service
public class UserServiceImpl implements com.helius.service.UserService {
	@Autowired
	private EmailService emailService;
	@Autowired
	private EmployeeManager employeemanager;
	@Autowired
	ApplicationContext context;
	@Autowired 
	private InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	private org.hibernate.internal.SessionFactoryImpl sessionFactory;
	
	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public User get(String userid) throws Throwable{
		com.helius.entities.User User = null;
		String status = "";
		Session session = null;
		try {
			session = sessionFactory.openSession();
			String user_query = "select * from user where userid = :userid ";
			java.util.List userlist = session.createSQLQuery(user_query).addEntity(com.helius.entities.User.class)
					.setParameter("userid", userid).list();
			if (userlist != null && !userlist.isEmpty()) {
				User = (User) userlist.iterator().next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		return User;
	}

	// to compare password with encrypt pswd
	private Boolean checkPass(String plainPassword, String hashedPassword) {
		Boolean status = null;
		if (BCrypt.checkpw(plainPassword, hashedPassword))
			status = true;
		else
			status = false;
		return status;
	}
	
	/**
	 * forgot password first verify the emailaddress and send email to employee
	 * to set new password
	 **/
	@Override
	public String verifyForgotEmailAddress(String employeeid,String appUrl) throws Throwable{
		String status = "";
		Session session = null;
		Transaction transaction = null;
		String To = "";
		Employee employee = null;
		User user = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			user = get(employeeid);
			employee = employeemanager.getEmployee(employeeid);
			if(employee == null){
				employee = employeemanager.getEmployee(user.getEmpid());
			}
			if (employee != null && user !=null) {
				int User_login_attempts = Integer.parseInt(user.getUserLoginAttempts());
				if(User_login_attempts>0){
			    SecureRandom random = new SecureRandom();
			    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
				byte[] buffer = new byte[20];
		        random.nextBytes(buffer);
		        String token = encoder.encodeToString(buffer)+Instant.now().toEpochMilli();
				user.setToken(token);
				session.update(user);
				To = employee.getEmployeeOfferDetails().getPersonal_email_id();
			//	String appUrl = "http://localhost:8080/helius/changepassword.html#!/";
				String subject= "Forgot Password";
				String text = "Hello " + employee.getEmployeePersonalDetails().getEmployee_name() + ","
						+ "\n\n" + "Please click below and change your password "
						+ "\n\n" + appUrl + "?token="+employeeid+"-fgtY"+ token+"\n\n" + "With Regards," + "\n"
						+ "Helius Technologies.";
				transaction.commit();
				//logger.info("new token for forgot password is updated for user -"+employeeid+"--token--"+token);
				emailService.sendEmail(To, null, null, subject, text);
			//	logger.info("forgot password link is sent to user successfully "+To);
				status = "Forgot Password link has been send to your registered Email Address";
				}else{
				status = "Your Account is not activated. Please activate your account using activation link shared earlier or Contact - HR ";
				}
			} else {
				throw new Throwable("Please check your User-Id or contact HR !");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//logger.error("issue in sending forgot password link to user "+employeeid,e);
			e.printStackTrace();
			throw new Throwable("Please check your User-Id or contact HR !");
		} finally {
			session.close();
		}
		return status;
	}
	
	/**
	 * Service used to activate account for the first time login and
	 * also to change password incase of forgot
	 **/
	@Override
	public String resetpswd(String base64Credentials,String token,String fg) throws Throwable{
		Session session = null;
		Transaction transaction = null;
		String password = null;
		String userid = null;
		String status = "";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials));
			final String[] values = credentials.split(":", 2);
			password = values[1];
			userid = values[0];
			//String pwd = (BCrypt.hashpw(password, BCrypt.gensalt()));
			User user = get(userid);
			if("Y".equalsIgnoreCase(fg)){
			if(token.equals(user.getToken())){
			user.setPassword(password);
			session.update(user);
			updateuser_to_memory(user);
			transaction.commit();
			status = "Password saved succesfully Please Login !";
			}else{
				//logger.error("failed to authenticate user email link token is not matching with db token for user -"+userid+ "given email token is "+token+" db token is "+user.getToken());
				throw new Throwable("Failed to change Password Please Contact HR !");
			}
		}
			if("N".equalsIgnoreCase(fg)){				
				int User_login_attempts = Integer.parseInt(user.getUserLoginAttempts());
				if(token.equals(user.getToken())){
				if (User_login_attempts == 0) {
					user.setUserLoginAttempts(String.valueOf(User_login_attempts + 1));
					user.setPassword(password);
					session.update(user);
					adduser_to_memory(user);
					transaction.commit();
					status = "Password saved succesfully Please Login !";
				} else {
					status = "Account is already activated please Login";
				}
			}else{
			//	logger.error("failed to authenticate user email link token is not matching with db token given email token is "+token+"db token is "+user.getToken());
				throw new Throwable("Failed to change Password Please Contact HR !");
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//logger.error("Failed to update password for user - "+userid+" internal error check stacktrace",e);
			e.printStackTrace();
			throw new Throwable("Failed to change Password Please Contact HR !");
		} finally {
			session.close();
		}
		return status;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.service.UserService#createUser(com.helius.entities.User)
	 */
	@Override
	public String createUser(User user,String appUrl) throws Throwable {
		Session session  = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			// user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			SecureRandom random = new SecureRandom();
		    Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
			byte[] buffer = new byte[20];
	        random.nextBytes(buffer);
	        String token = encoder.encodeToString(buffer)+Instant.now().toEpochMilli();
	        user.setToken(token);
			session.save(user);
			transaction.commit();
			//adduser_to_memory(user);
			Employee employee = employeemanager.getEmployee(user.getUserid());
				try{
				    String To = employee.getEmployeeOfferDetails().getPersonal_email_id();				
					String subject= "Account Activation";
					String text = "Hello " + employee.getEmployeePersonalDetails().getEmployee_name() + ","
							+ "\n\n" + "Please click below and change your password "
							+ "\n\n" + appUrl + "?token="+user.getUserid()+"-fgtN"+ token+"\n\n" + "With Regards," + "\n"
							+ "Helius Technologies.";
					emailService.sendEmail(To, null, null, subject, text);
					}catch(Exception e){
						e.printStackTrace();
					}
		} catch (HibernateException e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Failed to Save User");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to Save User" + e.getMessage());
		}
		
		finally{
			session.close();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.service.UserService#updateUser(com.helius.entities.User)
	 */
	@Override
	public String updateUser(User user) throws Throwable {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			// user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			session.evict(user);
			session.merge(user);
			transaction.commit();
			//sc.updateuser(user);
			updateuser_to_memory(user);
		} catch (HibernateException e) {
			// transaction.rollback();
			e.printStackTrace();
			throw new Throwable("Failed to update User");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to Save User" + e.getMessage());
		}
		finally{
			session.close();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.helius.service.UserService#deleteUser(com.helius.entities.User)
	 */
	@Override
	public String deleteUser(User user) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public List<com.helius.utils.User> getAllUsers() throws Throwable {
		List<com.helius.utils.User> allusers = new ArrayList<com.helius.utils.User>();
		
		Session session = null;
		try {

			session = sessionFactory.openSession();

			Query query = session.createSQLQuery("select * from user u where u.active='Yes'")
					.addEntity(com.helius.entities.User.class);
			List users = query.list();
			if (!users.isEmpty()) {
				Iterator iter = users.iterator();
				while (iter.hasNext()) {
					com.helius.entities.User user_entity = (com.helius.entities.User) iter.next();
					com.helius.utils.User user_util = new com.helius.utils.User();
					user_util.setActive(user_entity.getActive());
					user_util.setId(user_entity.getId());
					user_util.setUserid(user_entity.getUserid());
					user_util.setPassword(user_entity.getPassword());
					user_util.setUsername(user_entity.getUsername());
					user_util.setEdit(user_entity.getEdit());
					user_util.setView(user_entity.getView());
					user_util.setActive(user_entity.getActive());
					user_util.setUserLoginAttempts(user_entity.getUserLoginAttempts());
					user_util.setToken(user_entity.getToken());
					String[] rr = user_entity.getRole().split(",");
					List<String> role = new ArrayList<String>();
					for (int i = 0; i < rr.length; i++) {
						role.add(rr[i]);
					}
					user_util.setRole(role);
					List<String> country = new ArrayList<String>();
					String[] countries = user_entity.getCountry().split(",");
					for (int i = 0; i < countries.length; i++) {
						country.add(countries[i]);
					}
					user_util.setCountry(country);

					allusers.add(user_util);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Could not retrive users");
		}
		finally{
			session.close();
		}
		return allusers;
	}

	/* (non-Javadoc)
	 * @see com.helius.service.UserService#validateUser()
	 */
	@Override
	public Logindetails validateUser(String userid, String password) throws Throwable {
		Logindetails validauser = new Logindetails();
		String error = "";
		if (userid != null && !userid.isEmpty()) {
			Session session = null;
			try {
				String encodedpassword = Base64.getEncoder().encodeToString(password.getBytes());
				session = sessionFactory.openSession();
				String querystr = "select * from user u where u.active='Yes' and u.userid='"
						+ userid + "' and" + " u.password='" + encodedpassword + "'";
				Query query = session.createSQLQuery(querystr).addEntity(com.helius.entities.User.class);
				Object userobj = query.uniqueResult();
				if (userobj != null) {
					com.helius.entities.User user_entity = (com.helius.entities.User) userobj;

					com.helius.utils.User user_util = new com.helius.utils.User();
					user_util.setActive(user_entity.getActive());
					user_util.setId(user_entity.getId());
					user_util.setUserid(user_entity.getUserid());
					user_util.setPassword(user_entity.getPassword());
					user_util.setUsername(user_entity.getUsername());
					user_util.setEdit(user_entity.getEdit());
					user_util.setView(user_entity.getView());
					user_util.setActive(user_entity.getActive());
					user_util.setUserLoginAttempts(user_entity.getUserLoginAttempts());
					String[] rr = user_entity.getRole().split(",");
					List<String> role = new ArrayList<String>();
					for (int i = 0; i < rr.length; i++) {
						role.add(rr[i]);
					}
					user_util.setRole(role);
					List<String> country = new ArrayList<String>();
					String[] countries = user_entity.getCountry().split(",");
					for (int i = 0; i < countries.length; i++) {
						country.add(countries[i]);
					}
					user_util.setCountry(country);
					validauser.setResult("Login success");
					validauser.setUser(user_util);

				} else {
					validauser.setResult("User not found in the system");
					validauser.setUser(null);
				}

			} catch (Exception e) {
				e.printStackTrace();
				validauser.setResult("Login failure");
				validauser.setUser(null);

			} finally {
				session.close();
			}

		}
		return validauser;
	}

	private void adduser_to_memory(User user) {
		String decodedpassword = new String(Base64.getDecoder().decode(user.getPassword()));
		String userid = user.getUserid();
		String[] rr = user.getRole().split(",");
		List<GrantedAuthority> role = new ArrayList<GrantedAuthority>();
		for (int i = 0; i < rr.length; i++) {
			GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_"+rr[i]);
			role.add(ga);
		}			
		org.springframework.security.core.userdetails.User user_sec =
				new org.springframework.security.core.userdetails.User(user.getUserid(),decodedpassword,true,true,true,true,role);
		inMemoryUserDetailsManager.createUser(user_sec);
	}
	private void updateuser_to_memory(User user) {
		String decodedpassword = new String(Base64.getDecoder().decode(user.getPassword()));
		String userid = user.getUserid();
		String[] rr = user.getRole().split(",");
		List<GrantedAuthority> role = new ArrayList<GrantedAuthority>();
		for (int i = 0; i < rr.length; i++) {
			GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_"+rr[i]);
			role.add(ga);
		}			
		org.springframework.security.core.userdetails.User user_sec =
				new org.springframework.security.core.userdetails.User(user.getUserid(),decodedpassword,true,true,true,true,role);
		inMemoryUserDetailsManager.updateUser(user_sec);
	}

	/* (non-Javadoc)
	 * @see com.helius.service.UserService#getUser(java.lang.String)
	 */
	@Override
	public com.helius.utils.User getUser(String userid) throws Exception{
		com.helius.utils.User user = null;
		
		Session session = null;
		try {

			session = sessionFactory.openSession();

			Query query = session.createSQLQuery("select * from user u where u.userid=" + userid + "")
					.addEntity(com.helius.entities.User.class);
			List users = query.list();
			if (!users.isEmpty()) {
				Iterator iter = users.iterator();
				while (iter.hasNext()) {
					com.helius.entities.User user_entity = (com.helius.entities.User) iter.next();
					com.helius.utils.User user_util = new com.helius.utils.User();
					user_util.setActive(user_entity.getActive());
					user_util.setId(user_entity.getId());
					user_util.setUserid(user_entity.getUserid());
					user_util.setPassword(user_entity.getPassword());
					user_util.setUsername(user_entity.getUsername());
					user_util.setEdit(user_entity.getEdit());
					user_util.setView(user_entity.getView());
					user_util.setActive(user_entity.getActive());
					user_util.setUserLoginAttempts(user_entity.getUserLoginAttempts());
					user_util.setToken(user_entity.getToken());
					String[] rr = user_entity.getRole().split(",");
					List<String> role = new ArrayList<String>();
					for (int i = 0; i < rr.length; i++) {
						role.add(rr[i]);
					}
					user_util.setRole(role);
					List<String> country = new ArrayList<String>();
					String[] countries = user_entity.getCountry().split(",");
					for (int i = 0; i < countries.length; i++) {
						country.add(countries[i]);
					}
					user_util.setCountry(country);

					user =user_util;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Could not retrive users");
		}
		finally{
			session.close();
		}
		return user;
	}
}
