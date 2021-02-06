package com.helius.service;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

import com.helius.dao.Mail;

public interface EmailService {
	public void sendEmail(SimpleMailMessage email) throws  MailException;
	public void sendEmail(String to,String[] cc,String[] bcc, String subject, String text )throws MessagingException;
	public void sendBulkEmail(String to,String[] cc,String[] bcc, String subject, String text) throws MessagingException;
	public void sendMessageWithAttachment(String to,String[] cc, String subject, String message, List<String> urlList) throws MessagingException;
	public void sendMessageWithAttachmentUsingTimesheetEmail(String to, String[] cc, String subject, String text, List<File> al) throws MessagingException;
	public void sendEmailAlert(Mail mail)throws Exception;
}