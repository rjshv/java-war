/**
 * 
 */
package com.helius.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Tirumala
 * 19-Mar-2018
 */
@Controller
public class HomeController {

	
	@Autowired
    ApplicationContext context;
	/**
	 * 
	 */
	public HomeController() {
		// Sample change here to test github
	}

	@CrossOrigin
	@RequestMapping(value="/", method=RequestMethod.GET )
	public @ResponseBody String home() {
		
		return "index";
	}
	
}
