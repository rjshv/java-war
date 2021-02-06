package com.helius.service;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.helius.dao.Mail;
import com.helius.utils.Utils;

import freemarker.template.Template;


@Service("emailService")
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	/*@Autowired
	private JavaMailSender timesheetMailSender;*/
	
	// @Autowired
	// private SimpleMailMessage preConfiguredMessage;

/* public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
*/
	/*	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("vinaynaiduer@gmail.com");
		mailSender.setPassword("bgoqfjdhgtzcvnwn");
	//	mailSender.setPassword("Rvrcamv@789");
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		return mailSender;
	}
*/
/*	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.office365.com");
		mailSender.setPort(587);
	//	mailSender.setPort(26);
		mailSender.setUsername("hap@helius-tech.com");
		mailSender.setPassword("Helius@123");
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "false");
		props.put("mail.smtp.ssl.enable", "false");
	//	props.put("mail.smtp.host", "mail.helius-tech.com");        
	//	props.put("mail.smtp.port", "465");
		return mailSender;
	} */
	/*@Bean
	public JavaMailSender timesheetMail() {
		JavaMailSenderImpl timesheetMailSender = new JavaMailSenderImpl();
		timesheetMailSender.setHost(Utils.getProperty("emailHost"));
		timesheetMailSender.setPort(Integer.parseInt(Utils.getProperty("emailPort")));
		timesheetMailSender.setUsername(Utils.getHapProperty("timesheetUserName"));
		timesheetMailSender.setPassword(Utils.getHapProperty("timesheetPassword"));
		Properties props = timesheetMailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", Utils.getProperty("mail.transport.protocol"));
		props.put("mail.smtp.auth", Utils.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", Utils.getProperty("mail.smtp.starttls.enable"));
		props.put("mail.debug", Utils.getProperty("mail.debug"));
		props.put("mail.smtp.ssl.enable", Utils.getProperty("mail.smtp.ssl.enable"));
	//	props.put("mail.smtp.host", "mail.helius-tech.com");        
	//	props.put("mail.smtp.port", "465");
		return timesheetMailSender;
	}*/
	
	
	@Bean
	//@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(Utils.getProperty("emailHost"));
		mailSender.setPort(Integer.parseInt(Utils.getProperty("emailPort")));
		mailSender.setUsername(Utils.getProperty("emailUserName"));
		mailSender.setPassword(Utils.getProperty("emailPassword"));
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", Utils.getProperty("mail.transport.protocol"));
		props.put("mail.smtp.auth", Utils.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.starttls.enable", Utils.getProperty("mail.smtp.starttls.enable"));
		props.put("mail.debug", Utils.getProperty("mail.debug"));
		props.put("mail.smtp.ssl.enable", Utils.getProperty("mail.smtp.ssl.enable"));
	//	props.put("mail.smtp.host", "mail.helius-tech.com");        
	//	props.put("mail.smtp.port", "465");
		return mailSender;
	}
	
	/*public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean freemarkerConfig = new FreeMarkerConfigurationFactoryBean();
        freemarkerConfig.setTemplateLoaderPath("/WEB-INF");  
        return freemarkerConfig;
    }*/
	
	@Bean 
	public FreeMarkerConfigurer freemarkerConfig() { 
	    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer(); 
	    freeMarkerConfigurer.setTemplateLoaderPath("/AlertTemplate/");
	    return freeMarkerConfigurer; 
	}
	
	@Async
	public void sendEmailAlert(Mail mail) throws Exception {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    Template t =  freeMarkerConfigurer.getConfiguration().getTemplate("/WorkpermitExpiryTemplate.ftl");
    String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());
	String from = (((JavaMailSenderImpl) mailSender).getUsername());
    helper.setFrom(new InternetAddress(from));
    String check =	Utils.getHapProperty("hcm_testing");
    if("yes".equalsIgnoreCase(check)){
    	 mail.setMailTo("hap-testing@helius-tech.com"); 
	}
    helper.setTo(mail.getMailTo());
    helper.setText(text, true);
    helper.setSubject(mail.getMailSubject());
    mailSender.send(message);
}

	@Async
	public void sendEmail(SimpleMailMessage email)throws MailException {
		email.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
		String check =	Utils.getHapProperty("hcm_testing");
		if("yes".equalsIgnoreCase(check)){
    		email.setTo("hap-testing@helius-tech.com"); 
    		email.setCc("hap-testing@helius-tech.com");
    		email.setBcc("hap-testing@helius-tech.com");
    	}
		mailSender.send(email);	
	}
	
	@Async
	public void sendEmail(String to,String[] cc,String[] bcc, String subject, String text )throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		String check =	Utils.getHapProperty("hcm_testing");
		String from = (((JavaMailSenderImpl) mailSender).getUsername());
		helper.setSubject(subject);
		helper.setText(text.toString());
		helper.setFrom(new InternetAddress(from));
		if("yes".equalsIgnoreCase(check)){
    		to = "hap-testing@helius-tech.com";
    		if(bcc != null && bcc.length > 0){
    		 String[] testBCC = new String [] {"hap-testing@helius-tech.com"};
    		 bcc = testBCC;
    		}
    		if(cc != null && cc.length > 0){
          		 String[] testCC = new String [] {"hap-testing@helius-tech.com"};
          		 cc = testCC;
          		}
    		}
		helper.setTo(to);
		if(cc!=null){
		helper.setCc(cc);
		}
		if(bcc!=null){
		helper.setBcc(bcc);
		}
		mailSender.send(message);	
	}
	
	public void sendBulkEmail(String to,String[] cc,String[] bcc, String subject, String text)throws MessagingException {
    	MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		String check =	Utils.getHapProperty("hcm_testing");
		String from = (((JavaMailSenderImpl) mailSender).getUsername());
		helper.setSubject(subject);
		helper.setText(text.toString());
		helper.setFrom(new InternetAddress(from));
		if("yes".equalsIgnoreCase(check)){
    		to = "hap-testing@helius-tech.com";
    		if(bcc != null && bcc.length > 0){
    		 String[] testBCC = new String [] {"hap-testing@helius-tech.com"};
    		 bcc = testBCC;
    		}
    		if(cc != null && cc.length > 0){
       		 String[] testCC = new String [] {"hap-testing@helius-tech.com"};
       		 cc = testCC;
       		}
    		}
		helper.setTo(to);
		if(cc!=null){
		helper.setCc(cc);
		}
		if(bcc!=null){
		helper.setBcc(bcc);
		}
		mailSender.send(message);	
	}

	public void syncSentItems(MimeMessage message)throws MessagingException{
		Session session=Session.getInstance(((JavaMailSenderImpl) mailSender).getJavaMailProperties());
		Store store = session.getStore("imaps");
		store.connect(((JavaMailSenderImpl) mailSender).getHost(),((JavaMailSenderImpl) mailSender).getUsername(),((JavaMailSenderImpl) mailSender).getPassword());	
		Folder folder = store.getFolder("Inbox.Sent");
		((JavaMailSenderImpl) mailSender).getHost();
		folder.open(Folder.READ_WRITE);  
		//message.setFlag(Flag.SEEN, true);  
		folder.appendMessages(new Message[] {message});  
		store.close();
	}
	
	@Async
	public void sendMessageWithAttachment(String to,String[] cc, String subject, String text,
			List<String> pathToAttachment) throws MessagingException {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			String from = (((JavaMailSenderImpl) mailSender).getUsername());
			helper.setSubject(subject);
			helper.setText(text.toString());
			helper.setFrom(new InternetAddress(from));
			String check =	Utils.getHapProperty("hcm_testing");
        	if("yes".equalsIgnoreCase(check)){
        		to = "hap-testing@helius-tech.com";
        	}
			helper.setTo(to);
			if(cc!=null){
			helper.setCc(cc);
			}
			if(!pathToAttachment.isEmpty() || pathToAttachment.size()!=0){
			for (String file : pathToAttachment) {
				FileSystemResource fr = new FileSystemResource(file);
				helper.addAttachment(fr.getFilename(), fr); 
			}	
			}
			mailSender.send(message);
		//	syncSentItems(message);		    
	}
	
	@Async
	public void sendMessageWithAttachmentUsingTimesheetEmail(String to,String[] cc, String subject, String text,
			List<File> files) throws MessagingException {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			((JavaMailSenderImpl) mailSender).setUsername(Utils.getHapProperty("timesheetUserName"));
			((JavaMailSenderImpl) mailSender).setPassword(Utils.getHapProperty("timesheetPassword"));
			String from = (((JavaMailSenderImpl) mailSender).getUsername());
			helper.setSubject(subject);
			helper.setText(text.toString());
			helper.setFrom(new InternetAddress(from));
			String check =	Utils.getHapProperty("hcm_testing");
        	if("yes".equalsIgnoreCase(check)){
        		to = "hap-testing@helius-tech.com";
        	}
    		helper.setTo(to);
			if(cc!=null){
			helper.setCc(cc);
			}
			if(!files.isEmpty() || files.size()!=0){
			for (File file : files) { 
			helper.addAttachment(file.getName(),file);
			}	
			}
			mailSender.send(message);
	}
}
