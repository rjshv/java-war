/**
 * 
 */
package com.helius.security;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MessageWebApplicationInitializer extends
AbstractAnnotationConfigDispatcherServletInitializer {

@Override
protected Class<?>[] getRootConfigClasses() {
return new Class[] { RootConfiguration.class };
}

/* (non-Javadoc)
 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getServletConfigClasses()
 */
@Override
protected Class<?>[] getServletConfigClasses() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#getServletMappings()
 */
@Override
protected String[] getServletMappings() {
	// TODO Auto-generated method stub
	return null;
}

// ... other overrides ...
}
