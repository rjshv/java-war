package com.helius.security;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.helius.entities.*;



public interface UserService {
	
	User findById(long id);
	
	User findByName(String name);
	
	void saveUser(User user_inmemory);
	
	void updateUser(User user_inmemory);
	
	void deleteUserById(long id);

	List<User> findAllUsers(); 
	
	void deleteAllUsers();
	
	public boolean isUserExist(User user_inmemory);
	public void populateDummyUsers(DataSource datasource);
	

	
	
}
