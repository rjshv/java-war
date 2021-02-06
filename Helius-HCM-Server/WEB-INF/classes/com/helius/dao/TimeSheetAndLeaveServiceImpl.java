package com.helius.dao;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.mapping.Array;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.EmailScreen;
import com.helius.entities.Employee;
import com.helius.entities.Employee_Assignment_Details;
import com.helius.entities.Employee_Bank_Details;
import com.helius.entities.Employee_Beeline_Timesheet;
import com.helius.entities.Employee_Personal_Details;
import com.helius.entities.Employee_Salary_Details;
import com.helius.entities.Employee_Timesheet_Status;
import com.helius.entities.Timesheet_Email;
import com.helius.service.EmailService;
import com.helius.utils.FilecopyStatus;
import com.helius.utils.Utils;
import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import ooo.connector.BootstrapSocketConnector;


public class TimeSheetAndLeaveServiceImpl implements TimesheetService {
	
	@Autowired
	EmployeeDAOImpl employeeDAO;
	@Autowired
	private EmailService emailService;
	@Autowired
	BeelineTimesheetService beelineService;
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
	private List<String> copied_with_success = new ArrayList<String>();

//	private static int leaveHeaderIndex = 0;
//	private static int clockingDateStartIndex = 0;
//	private static Row leaveHeaderRow = null;
	//private static FileInputStream file = null;
	//private static Sheet sheet = null; 
	//private static HashMap<String, ArrayList<Row>> uniqueJobtypHrs = null;
	//private static HashMap<String, String> nameID = null;
	//private static List<String> issueinprocessingOneBnankId = new ArrayList<String>();
	@Override
	public void saveorupdateTimesheetEmailId(String jsondata) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ObjectMapper om = new ObjectMapper();
			Timesheet_Email timesheetEmail = null;
			timesheetEmail = om.readValue(jsondata, Timesheet_Email.class);
			if (timesheetEmail != null) {
				String tsquery = "select * from Timesheet_Email  where employee_id = :employee_id ";
				java.util.List emailList = session.createSQLQuery(tsquery).addEntity(Timesheet_Email.class)
						.setParameter("employee_id", timesheetEmail.getEmployeeId()).list();
				if (emailList.isEmpty()) {
					session.save(timesheetEmail);
				} else {
					Timesheet_Email timesheetEmails = (Timesheet_Email) emailList.iterator().next();
					timesheetEmails.setTimesheetEmailId(timesheetEmail.getTimesheetEmailId());
					session.update(timesheetEmails);
				}
				transaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to save timesheet Email-Id" + e.getMessage());
		} finally {
			session.close();
		}
	}

	public void updateSalaryProcessing(String jsondata) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ObjectMapper om = new ObjectMapper();
			ArrayList timesheetSalary = null;
			timesheetSalary = om.readValue(jsondata,ArrayList.class);
			for(Object obj :timesheetSalary ){
				HashMap<String, Object> mp = (HashMap<String, Object>) obj;
				String timesheetId = null;
				boolean salaryProcessingStatus = false;
				Timestamp dt = null;
				if(mp.get("employeeTimesheetStatusId") != null){
				 timesheetId = mp.get("employeeTimesheetStatusId").toString();
				}
				if(mp.get("salaryProcessingStatus") != null){
				 salaryProcessingStatus = Boolean.valueOf(mp.get("salaryProcessingStatus").toString());
				}
				if(mp.get("salaryProcessedDate") != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String ss = mp.get("salaryProcessedDate").toString();
				Date processedtime = sdf.parse(ss);
				String formattedDate = outputFormat.format(processedtime);
				Date processedtimestamp = outputFormat.parse(formattedDate);
				dt = new Timestamp(processedtimestamp.getTime());
				}
				Employee_Timesheet_Status emptimesteet = getTimesheetById(timesheetId);
				emptimesteet.setSalaryProcessingStatus(salaryProcessingStatus);
				emptimesteet.setSalaryProcessedDate(dt);
				session.update(emptimesteet);
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to update Salary Processing Status" + e.getMessage());
		} finally {
			session.close();
		}
	}
	
	public void convertFiles(File sourceFile,File convertToFile,XComponentLoader xcomponentLoader) throws Throwable{
		try{
			//for linux path
			String scr = "file:///" + sourceFile.getAbsolutePath();
			//for windows path
		//	String scr = "file:///" + sourceFile.getAbsolutePath().replace( '\\', '/' );
			if(!sourceFile.canRead()) {
				  throw new RuntimeException("Unable to read the File :");
				 }
				 PropertyValue[] propertyValues = new PropertyValue[0];
				 propertyValues = new PropertyValue[1];
				 propertyValues[0] = new PropertyValue();
				 propertyValues[0].Name = "Hidden";
				 propertyValues[0].Value = new Boolean(true);
				 //	 XComponent xComp = xcomponentLoader.loadComponentFromURL(scr, "_blank", 0, propertyValues);
				 Object oDocToStore =xcomponentLoader.loadComponentFromURL(scr, "_blank", 0, propertyValues);
				 // save as a PDF
				 XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, oDocToStore);
				 propertyValues = new PropertyValue[2];
				 propertyValues[0] = new PropertyValue();
				 propertyValues[0].Name = "Overwrite";
				 propertyValues[0].Value = new Boolean(true);
				 propertyValues[1] = new PropertyValue();
				 propertyValues[1].Name = "FilterName";
				 propertyValues[1].Value = "writer_pdf_Export";			
				 String scr2 = "file:///" + convertToFile.getAbsolutePath();
				 xStorable.storeToURL(scr2, propertyValues);
				 System.out.println("Saved " + scr2);
   				 //xComp.dispose();
				// Closing the converted document. Use XCloseable.close if the
                 // interface is supported, otherwise use XComponent.dispose
                 com.sun.star.util.XCloseable xCloseable =
                     UnoRuntime.queryInterface(
                     com.sun.star.util.XCloseable.class, xStorable);

                 if ( xCloseable != null ) {
                     xCloseable.close(false);
                 } else {
                     com.sun.star.lang.XComponent xComp =
                         UnoRuntime.queryInterface(
                         com.sun.star.lang.XComponent.class, xStorable);

                     xComp.dispose();
                 }
		}catch(Exception e){
			e.printStackTrace();
			throw new Throwable("Failed to Convert File +!! "+sourceFile.toString()); 
		}
	}
	
	/**
	 * command to start openoffice in case of aws server is restarted or any issue while opening libreoffice.
	 * cmd to be given under opt/libreoffice/program/ i.e.,
	 * ./soffice -headless -accept="socket,host=localhost,port=8100;urp;" -nofirststartwizard
	 * 
	 * incase any issue regarding continuous loading while converting then its suggested to kill the 
	 * running libreoffice and then start libreoffice using above command.
	 * 
	 * @author vinay
	 * **/
	@Override
	public void saveTimeSheetStatus(String jsondata, MultipartHttpServletRequest request)
			throws Throwable {
		Session session = null;
		Transaction transaction = null;
		String pdfFilePath = null;
		String tempfilelocation = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ObjectMapper om = new ObjectMapper();
			Employee_Timesheet_Status empTimesheetStatus = null;
			empTimesheetStatus = om.readValue(jsondata, Employee_Timesheet_Status.class);
			if (empTimesheetStatus != null) {
				Timestamp timesheetMonth = empTimesheetStatus.getTimesheetMonth();
				timesheetMonth.setMinutes(0);
				timesheetMonth.setHours(0);
				timesheetMonth.setSeconds(0);
				timesheetMonth.setDate(1);
				empTimesheetStatus.setTimesheetMonth(timesheetMonth);
				java.sql.Date date = new java.sql.Date(empTimesheetStatus.getTimesheetMonth().getTime());
				LocalDate month = date.toLocalDate();
				Map<String, List<MultipartFile>> allfiles = request.getMultiFileMap();
				if (allfiles.size() > 0 || (empTimesheetStatus.getManagerApprovalDocumentPath() != null
						&& !"".equalsIgnoreCase(empTimesheetStatus.getManagerApprovalDocumentPath()))) {
					// Map stores final converted files path which are ready to merge as single pdf
					HashMap<String, ArrayList<String>> conFileLists = new HashMap<String, ArrayList<String>>();
					tempfilelocation = Utils.getProperty("fileLocation") + File.separator + "tempTimesheetPDFFolder";
					File fileDir = new File(tempfilelocation);
					if (!fileDir.exists()) {
						boolean iscreated = fileDir.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory");
						}
					}
					String oooExeFolder = Utils.getProperty("libreOfficeLoc");
					XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder);
				//	XComponentContext xContext = Bootstrap.bootstrap();
					XMultiComponentFactory xMCF = xContext.getServiceManager(); 
					Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);								 
					XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, oDesktop);
					XComponentLoader xCompLoader = (XComponentLoader) UnoRuntime.queryInterface(com.sun.star.frame.XComponentLoader.class, xDesktop);
					for (String key : allfiles.keySet()) {
						List<MultipartFile> ff = allfiles.get(key);
						// list holds file path for individual timesheet file type
						ArrayList<String> al = new ArrayList<String>();
						for (MultipartFile files1 : ff) {
							String extension = FilenameUtils.getExtension(files1.getOriginalFilename());
							File outputFile = File.createTempFile(empTimesheetStatus.getEmployeeName()+"_"+files1.getName(),FilenameUtils.getBaseName(files1.getOriginalFilename())+".pdf", fileDir);
							try {
								if ("pdf".equalsIgnoreCase(extension)) {
									File filePath = File.createTempFile(empTimesheetStatus.getEmployeeName()+"_"+files1.getName(),files1.getOriginalFilename(), fileDir);
									files1.transferTo(filePath);
									al.add(filePath.getPath());
								} else if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsm")) {
									File tempxlpath = null;
									if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xlsm")) {
										XSSFWorkbook workbookinput = new XSSFWorkbook(files1.getInputStream());
										XSSFWorkbook workbookoutput = workbookinput;
										int countSheet = workbookoutput.getNumberOfSheets();
										int i = 0;
										for (i = 0; i < countSheet; i++) {
											XSSFSheet sh = workbookoutput.getSheetAt(i);
											sh.setFitToPage(true);
											//sh.setAutobreaks(true);
											PrintSetup ps = sh.getPrintSetup();
											ps.setFitWidth( (short) 1);
											ps.setFitHeight( (short) 0);
										}
										tempxlpath = File.createTempFile(empTimesheetStatus.getEmployeeName() + "_" + files1.getName()+"_",
												 files1.getOriginalFilename(),fileDir);
										FileOutputStream out = new FileOutputStream(tempxlpath);
										workbookoutput.write(out);
										workbookinput.close();
										out.close();
									}
									if (extension.equalsIgnoreCase("xls")) {
										HSSFWorkbook workbookinput = new HSSFWorkbook(files1.getInputStream());
										HSSFWorkbook workbookoutput = workbookinput;
										int countSheet = workbookoutput.getNumberOfSheets();
										int i = 0;
										for (i = 0; i < countSheet; i++) {
											HSSFSheet sh = workbookoutput.getSheetAt(i);
											sh.setFitToPage(true);
											sh.setAutobreaks(true);
											PrintSetup ps = sh.getPrintSetup();
											ps.setFitWidth( (short)1);
											ps.setFitHeight( (short)0);	
										}
										tempxlpath = File.createTempFile(empTimesheetStatus.getEmployeeName() + "_" + files1.getName(),
												 files1.getOriginalFilename(),fileDir);
										FileOutputStream out = new FileOutputStream(tempxlpath);
										workbookoutput.write(out);
										workbookinput.close();
										out.close();
									}
									convertFiles(tempxlpath, outputFile, xCompLoader);
									al.add(outputFile.toString());
								} else {
									File tmpfilePath = File.createTempFile(
											empTimesheetStatus.getEmployeeName() + "_" + files1.getName(),
											files1.getOriginalFilename(), fileDir);	
									files1.transferTo(tmpfilePath);
									boolean x = false;
									if(tmpfilePath.exists()){
										x=true;
									}
									convertFiles(tmpfilePath, outputFile, xCompLoader);
									al.add(outputFile.toString());
								}
							} catch (Exception e) {
								String type = "";
								e.printStackTrace();
								if ("timesheet".equalsIgnoreCase(key)) {
									type = "Timesheet";
								} else if ("supportingdoc".equalsIgnoreCase(key)) {
									type = "Supporting Document";
								}
								throw new Throwable(
										"Failed to save timesheet  - issue in saving " + type +" File "+files1.getOriginalFilename()+ " !!");
							}						
						}
						conFileLists.put(key, al);
					}
					if (empTimesheetStatus.getManagerApprovalDocumentPath() != null
							&& !"".equalsIgnoreCase(empTimesheetStatus.getManagerApprovalDocumentPath())) {
						try {
							 String strDoc = "private:factory/swriter";
							 PropertyValue[] propertyValues = new PropertyValue[0];
							 propertyValues = new PropertyValue[1];
							 propertyValues[0] = new PropertyValue();
							 propertyValues[0].Name = "Hidden";
							 propertyValues[0].Value = new Boolean(true);
							XComponent	xComp = xCompLoader.loadComponentFromURL(strDoc, "_blank", 0, propertyValues);
							XTextDocument  xDoc = UnoRuntime.queryInterface(com.sun.star.text.XTextDocument.class,xComp);
							 //getting the text object
					        com.sun.star.text.XText xText = xDoc.getText();
					        //create a cursor object
					        com.sun.star.text.XTextCursor xTCursor = xText.createTextCursor();
					        //inserting some Text
					        String msg = new String(empTimesheetStatus.getManagerApprovalDocumentPath().getBytes("ISO8859_1"), "UTF8");
					        xText.insertString( xTCursor, msg, false );
					        //inserting a second line
					       // xText.insertString( xTCursor, "Now we're in the second line\n", false );
					        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, xComp);
							 propertyValues = new PropertyValue[2];
							 propertyValues[0] = new PropertyValue();
							 propertyValues[0].Name = "Overwrite";
							 propertyValues[0].Value = new Boolean(true);
							 propertyValues[1] = new PropertyValue();
							 propertyValues[1].Name = "FilterName";
							 propertyValues[1].Value = "writer_pdf_Export";
							 File res = File.createTempFile(empTimesheetStatus.getEmployeeName(),"ManagerApproval.pdf",fileDir);
							 String output = "file:///" + res.getAbsolutePath();
							 xStorable.storeToURL(output, propertyValues);
							// xComp.dispose();
							 com.sun.star.util.XCloseable xCloseable =
				                     UnoRuntime.queryInterface(
				                     com.sun.star.util.XCloseable.class, xStorable);

				                 if ( xCloseable != null ) {
				                     xCloseable.close(false);
				                 } else {
				                     com.sun.star.lang.XComponent xComp2 =
				                         UnoRuntime.queryInterface(
				                         com.sun.star.lang.XComponent.class, xStorable);

				                     xComp2.dispose();
				                 }
							 ArrayList<String> mngrApprovTempFile = new ArrayList<String>();
							 mngrApprovTempFile.add(res.toString());
							 conFileLists.put("mgrapp", mngrApprovTempFile);
						} catch (Exception e) {
							e.printStackTrace();
							throw new Throwable(
									"Failed to save timesheet  - issue in saving manager approval !!");
						}
					}
				//	xDesktop.terminate();	
					PDFMergerUtility PDFmerger = new PDFMergerUtility();
					File pdffileDir = new File(Utils.getProperty("fileLocation") + File.separator + "timesheetpdf");
					if (!pdffileDir.exists()) {
						boolean iscreated = pdffileDir.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory ");
						}
					}
					File timesheetPDFLoc = new File(
							pdffileDir + File.separator + month.getMonth() + " " + month.getYear());
					if (!timesheetPDFLoc.exists()) {
						boolean iscreated = timesheetPDFLoc.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory");
						}
					}
					pdfFilePath = timesheetPDFLoc + File.separator + empTimesheetStatus.getEmployeeName()
							+ "_Time Sheet_" + month.getMonth() + " " + month.getYear() + ".pdf";
					PDFmerger.setDestinationFileName(pdfFilePath);
					if (conFileLists.containsKey("timesheet")) {
						ArrayList<String> timesheetUrl = conFileLists.get("timesheet");
						for (String url : timesheetUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					if (conFileLists.containsKey("mgrapp")) {
						ArrayList<String> mgrApprovUrl = conFileLists.get("mgrapp");
						for (String url : mgrApprovUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					if (conFileLists.containsKey("supportingdoc")) {
						ArrayList<String> supportingDocUrl = conFileLists.get("supportingdoc");
						for (String url : supportingDocUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					// setting null as argument in pdf merger to use default
					// memorysetting
					PDFmerger.mergeDocuments(null);
					if (tempfilelocation != null && !"".equalsIgnoreCase(tempfilelocation)) {
						File tempPdf = new File(tempfilelocation);
						if (tempPdf.exists()) {
							FileUtils.deleteQuietly(tempPdf);
							//FileUtils.deleteDirectory(tempPdf);
						}
					}
					empTimesheetStatus.setTimesheetUploadPath(pdfFilePath);
				}
				empTimesheetStatus.setManagerApprovalDocumentPath(null);
				session.save(empTimesheetStatus);
				transaction.commit();
				try{
				if("none".equalsIgnoreCase(empTimesheetStatus.getTimesheetError()) && allfiles.size() > 0){
				String subject = "Timesheet Status";
				String to = empTimesheetStatus.getTimesheetEmail();
				String[] cc = null;
				String[] bcc = null;
				String message = "Dear " + empTimesheetStatus.getEmployeeName()+ ","
						+ "\n\n" + "Thanks for submitting your timesheet and it is error free. We will process your timesheet and get back to you, if necessary." + "\n\n"
						+"\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd";
				//emailService.sendSimpleMessage(to, subject, message);
				emailService.sendBulkEmail(to,cc,bcc,subject,message.toString());
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tempfilelocation != null && !"".equalsIgnoreCase(tempfilelocation)) {
				File tempPdf = new File(tempfilelocation);
				if (tempPdf.exists()) {
					FileUtils.deleteDirectory(tempPdf);
				}
			}
			if (pdfFilePath != null && !"".equalsIgnoreCase(pdfFilePath)) {
				File file = new File(pdfFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
			throw new Throwable("Failed to save timesheet  !!");
		} finally {
			session.close();
		}
	}


	@Override
	public void updateTimeSheetStatus(String jsondata, MultipartHttpServletRequest request,String email)
			throws Throwable {
		Session session = null;
		Transaction transaction = null;
		String pdfFilePath = null;
		String tempfilelocation = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ObjectMapper om = new ObjectMapper();
			Employee_Timesheet_Status empTimesheetStatus = null;
			empTimesheetStatus = om.readValue(jsondata, Employee_Timesheet_Status.class);
			if (empTimesheetStatus != null) {
				Timestamp timesheetMonth = empTimesheetStatus.getTimesheetMonth();
				timesheetMonth.setMinutes(0);
				timesheetMonth.setHours(0);
				timesheetMonth.setSeconds(0);
				timesheetMonth.setDate(1);
				empTimesheetStatus.setTimesheetMonth(timesheetMonth);
				java.sql.Date date = new java.sql.Date(empTimesheetStatus.getTimesheetMonth().getTime());
				LocalDate month = date.toLocalDate();
				Map<String, List<MultipartFile>> allfiles = request.getMultiFileMap();
				if (allfiles.size() > 0 || (empTimesheetStatus.getManagerApprovalDocumentPath() != null
						&& !"".equalsIgnoreCase(empTimesheetStatus.getManagerApprovalDocumentPath()))) {
					// Map stores final converted files path which are ready to merge as single pdf
					HashMap<String, ArrayList<String>> conFileLists = new HashMap<String, ArrayList<String>>();
					tempfilelocation = Utils.getProperty("fileLocation") + File.separator + "tempTimesheetPDFFolder";
					File fileDir = new File(tempfilelocation);
					if (!fileDir.exists()) {
						boolean iscreated = fileDir.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory");
						}
					}
					String ss = Utils.getProperty("libreOfficeLoc");
					String oooExeFolder = Utils.getProperty("libreOfficeLoc");
					XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder);
					XMultiComponentFactory xMCF = xContext.getServiceManager(); 
					Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);								 
					XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, oDesktop);
					XComponentLoader xCompLoader = (XComponentLoader) UnoRuntime.queryInterface(com.sun.star.frame.XComponentLoader.class, xDesktop);
					for (String key : allfiles.keySet()) {
						List<MultipartFile> ff = allfiles.get(key);
						// list holds file path for individual timesheet file type
						ArrayList<String> al = new ArrayList<String>();
						for (MultipartFile files1 : ff) {
							String extension = FilenameUtils.getExtension(files1.getOriginalFilename());
							File outputFile = File.createTempFile(empTimesheetStatus.getEmployeeName()+"_"+files1.getName(),FilenameUtils.getBaseName(files1.getOriginalFilename())+".pdf", fileDir);
							try {
								if ("pdf".equalsIgnoreCase(extension)) {
									File filePath = File.createTempFile(empTimesheetStatus.getEmployeeName()+"_"+files1.getName(),files1.getOriginalFilename(), fileDir);
									files1.transferTo(filePath);
									al.add(filePath.getPath());
								} else if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsm")) {
									File tempxlpath = null;
									if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xlsm")) {
										XSSFWorkbook workbookinput = new XSSFWorkbook(files1.getInputStream());
										XSSFWorkbook workbookoutput = workbookinput;
										int countSheet = workbookoutput.getNumberOfSheets();
										int i = 0;
										for (i = 0; i < countSheet; i++) {
											XSSFSheet sh = workbookoutput.getSheetAt(i);
											sh.setFitToPage(true);
											//sh.setAutobreaks(true);
											PrintSetup ps = sh.getPrintSetup();
											ps.setFitWidth( (short) 1);
											ps.setFitHeight( (short) 0);
										}
										tempxlpath = File.createTempFile(empTimesheetStatus.getEmployeeName() + "_" + files1.getName()+"_",
												 files1.getOriginalFilename(),fileDir);
										FileOutputStream out = new FileOutputStream(tempxlpath);
										workbookoutput.write(out);
										workbookinput.close();
										out.close();
									}
									if (extension.equalsIgnoreCase("xls")) {
										HSSFWorkbook workbookinput = new HSSFWorkbook(files1.getInputStream());
										HSSFWorkbook workbookoutput = workbookinput;
										int countSheet = workbookoutput.getNumberOfSheets();
										int i = 0;
										for (i = 0; i < countSheet; i++) {
											HSSFSheet sh = workbookoutput.getSheetAt(i);
											sh.setFitToPage(true);
											sh.setAutobreaks(true);
											PrintSetup ps = sh.getPrintSetup();
											ps.setFitWidth( (short)1);
											ps.setFitHeight( (short)0);	
										}
										tempxlpath = File.createTempFile(empTimesheetStatus.getEmployeeName() + "_" + files1.getName(),
												 files1.getOriginalFilename(),fileDir);
										FileOutputStream out = new FileOutputStream(tempxlpath);
										workbookoutput.write(out);
										workbookinput.close();
										out.close();
									}
									convertFiles(tempxlpath, outputFile, xCompLoader);
									al.add(outputFile.toString());
								} else {
									File tmpfilePath = File.createTempFile(
											empTimesheetStatus.getEmployeeName() + "_" + files1.getName(),
											files1.getOriginalFilename(), fileDir);	
									files1.transferTo(tmpfilePath);
									convertFiles(tmpfilePath, outputFile, xCompLoader);
									al.add(outputFile.toString());
								}
							} catch (Exception e) {
								String type = "";
								e.printStackTrace();
								if ("timesheet".equalsIgnoreCase(key)) {
									type = "Timesheet";
								} else if ("supportingdoc".equalsIgnoreCase(key)) {
									type = "Supporting Document";
								}
								throw new Throwable(
										"Failed to update timesheet  - issue in saving " + type +" File "+files1.getOriginalFilename()+ " !!");
							}						
						}
						conFileLists.put(key, al);
					}
					if (empTimesheetStatus.getManagerApprovalDocumentPath() != null
							&& !"".equalsIgnoreCase(empTimesheetStatus.getManagerApprovalDocumentPath())) {
						try {
							 String strDoc = "private:factory/swriter";
							 PropertyValue[] propertyValues = new PropertyValue[0];
							 propertyValues = new PropertyValue[1];
							 propertyValues[0] = new PropertyValue();
							 propertyValues[0].Name = "Hidden";
							 propertyValues[0].Value = new Boolean(true);
							XComponent	xComp = xCompLoader.loadComponentFromURL(strDoc, "_blank", 0, propertyValues);
							XTextDocument  xDoc = UnoRuntime.queryInterface(com.sun.star.text.XTextDocument.class,xComp);
							 //getting the text object
					        com.sun.star.text.XText xText = xDoc.getText();
					        //create a cursor object
					        com.sun.star.text.XTextCursor xTCursor = xText.createTextCursor();
					        //inserting some Text
					        String msg = new String(empTimesheetStatus.getManagerApprovalDocumentPath().getBytes("ISO8859_1"), "UTF8");
					        xText.insertString( xTCursor, msg, false );
					       XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, xComp);
							 propertyValues = new PropertyValue[2];
							 propertyValues[0] = new PropertyValue();
							 propertyValues[0].Name = "Overwrite";
							 propertyValues[0].Value = new Boolean(true);
							 propertyValues[1] = new PropertyValue();
							 propertyValues[1].Name = "FilterName";
							 propertyValues[1].Value = "writer_pdf_Export";
							 File res = File.createTempFile(empTimesheetStatus.getEmployeeName(),"ManagerApproval.pdf",fileDir);
							 String output = "file:///" + res.getAbsolutePath();
							 xStorable.storeToURL(output, propertyValues);
						//	 xComp.dispose();
							 com.sun.star.util.XCloseable xCloseable =
				                     UnoRuntime.queryInterface(
				                     com.sun.star.util.XCloseable.class, xStorable);

				                 if ( xCloseable != null ) {
				                     xCloseable.close(false);
				                 } else {
				                     com.sun.star.lang.XComponent xComp2 =
				                         UnoRuntime.queryInterface(
				                         com.sun.star.lang.XComponent.class, xStorable);

				                     xComp2.dispose();
				                 }
							 ArrayList<String> mngrApprovTempFile = new ArrayList<String>();
							 mngrApprovTempFile.add(res.toString());
							 conFileLists.put("mgrapp", mngrApprovTempFile);
						} catch (Exception e) {
							e.printStackTrace();
							throw new Throwable(
									"Failed to update timesheet - issue in saving manager approval !!");
						}
					}
				//	 xDesktop.terminate();	
					PDFMergerUtility PDFmerger = new PDFMergerUtility();
					File pdffileDir = new File(Utils.getProperty("fileLocation") + File.separator + "timesheetpdf");
					if (!pdffileDir.exists()) {
						boolean iscreated = pdffileDir.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory ");
						}
					}
					File timesheetPDFLoc = new File(
							pdffileDir + File.separator + month.getMonth() + " " + month.getYear());
					if (!timesheetPDFLoc.exists()) {
						boolean iscreated = timesheetPDFLoc.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory");
						}
					}
					pdfFilePath = timesheetPDFLoc + File.separator + empTimesheetStatus.getEmployeeName()
							+ "_Time Sheet_" + month.getMonth() + " " + month.getYear() + ".pdf";
					PDFmerger.setDestinationFileName(pdfFilePath);
					if (conFileLists.containsKey("timesheet")) {
						ArrayList<String> timesheetUrl = conFileLists.get("timesheet");
						for (String url : timesheetUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					if (conFileLists.containsKey("mgrapp")) {
						ArrayList<String> mgrApprovUrl = conFileLists.get("mgrapp");
						for (String url : mgrApprovUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					if (conFileLists.containsKey("supportingdoc")) {
						ArrayList<String> supportingDocUrl = conFileLists.get("supportingdoc");
						for (String url : supportingDocUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					// setting null as argument in pdf merger to use default
					// memorysetting
					PDFmerger.mergeDocuments(null);
					if (tempfilelocation != null && !"".equalsIgnoreCase(tempfilelocation)) {
						File tempPdf = new File(tempfilelocation);
						if (tempPdf.exists()) {
							FileUtils.deleteQuietly(tempPdf);
						}
					}
					empTimesheetStatus.setTimesheetUploadPath(pdfFilePath);
				}
				empTimesheetStatus.setManagerApprovalDocumentPath(null);
				session.update(empTimesheetStatus);
				transaction.commit();
				try{
				if("yes".equalsIgnoreCase(email)){
					String subject = "Timesheet Status";
					String to = empTimesheetStatus.getTimesheetEmail();
					String[] cc = null;
					String[] bcc = null;
					String message = "Dear " + empTimesheetStatus.getEmployeeName()+ ","
							+ "\n\n" + "Thanks for submitting your timesheet and it is error free. We will process your timesheet and get back to you, if necessary." + "\n\n"
							+"\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd";
					//emailService.sendSimpleMessage(to, subject, message);
					emailService.sendBulkEmail(to,cc,bcc,subject,message.toString());
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tempfilelocation != null && !"".equalsIgnoreCase(tempfilelocation)) {
				File tempPdf = new File(tempfilelocation);
				if (tempPdf.exists()) {
					FileUtils.deleteDirectory(tempPdf);
				}
			}
			if (pdfFilePath != null && !"".equalsIgnoreCase(pdfFilePath)) {
				File file = new File(pdfFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
			throw new Throwable("Failed to update timesheet !!");
		} finally {
			session.close();
		}
	}

	public Employee_Timesheet_Status getTimesheetById(String timesheetId) {
		Employee_Timesheet_Status empTimesheetStatus = null;
		Session session = null;
		session = sessionFactory.openSession();
		try {
			String query = "select * from Employee_Timesheet_Status where employee_timesheet_status_id = :employee_timesheet_status_id";
			List<Employee_Timesheet_Status> tslist = session.createSQLQuery(query)
					.addEntity(Employee_Timesheet_Status.class)
					.setParameter("employee_timesheet_status_id", timesheetId).list();
			if (!tslist.isEmpty() && tslist != null) {
				empTimesheetStatus = (Employee_Timesheet_Status) tslist.iterator().next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return empTimesheetStatus;
	}

	@Override
	public ResponseEntity<byte[]> getTimesheetFile(String url) {
		byte[] files = null;
		FileInputStream fi = null;
		try {
			File file = new File(url);
			if (file.exists()) {
				fi = new FileInputStream(url);
				files = IOUtils.toByteArray(fi);
				fi.close();
			} else {
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
		return responseEntity;
	}

	@Override
	public ResponseEntity<byte[]> getAllTimesheetFiles(JSONObject json) {
		byte[] files = null;
		FileInputStream fi = null;
		String zipPath = null;
		String failFiles = "";
		ResponseEntity<byte[]> responseEntity = null;
		Session session = null;
		Timestamp timesheetday = null;
		Timestamp timesheetMonths = null;
		try {
			session = sessionFactory.openSession();
			List<Employee_Timesheet_Status> allTimesheet = new ArrayList<Employee_Timesheet_Status>();
			String query = "";
			String where = "";
			if (json.get("timesheetmonth") != null) {
				// allTimesheet = getAllTimesheetStatusByDate(timesheetdate);
				SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
				java.util.Date selectedMonth = sdfMonth.parse(json.get("timesheetmonth").toString());
				timesheetMonths = new Timestamp(selectedMonth.getTime());
				if(json.get("client") != null || json.get("account_manager") != null || json.get("timesheetType") != null){
					query = "LEFT JOIN Employee_Assignment_Details b ON a.employee_id=b.employee_id ";
					if(json.get("client") != null){
					where = where +" AND b.client = '"+json.get("client").toString()+"'";
					}
					if(json.get("account_manager") != null){
					where = where +" AND b.account_manager = '"+json.get("account_manager").toString()+"'";	
					}
					if(json.get("timesheetType") != null){
						where = where +" AND b.timesheet_type = '"+json.get("timesheetType").toString()+"'";	
					}
					}
					if(json.get("payroll") != null){
						query =query + " LEFT JOIN Employee_Salary_Details c ON a.employee_id=c.employee_id ";
						where = where +" AND c.payroll_entity = '"+json.get("payroll").toString()+"'";
					}
					if(json.get("bank_ifsc_code") != null){
						query =query + " LEFT JOIN Employee_Bank_Details d ON a.employee_id=d.employee_id ";
						if("DBS".equalsIgnoreCase(json.get("bank_ifsc_code").toString())){
						where = where +" AND d.bank_ifsc_code = '7171'";
						}
						if("Non DBS".equalsIgnoreCase(json.get("bank_ifsc_code").toString())){
							where = where +" AND (d.bank_ifsc_code != '7171' AND d.bank_ifsc_code IS NOT NULL AND d.bank_ifsc_code != '')";
						}
						if("None".equalsIgnoreCase(json.get("bank_ifsc_code").toString())){
							where = where +" AND (d.bank_ifsc_code = '' OR d.bank_ifsc_code IS NULL)";
						}
					}
					if(json.get("lopType") != null){
						where = where +" AND a.lop_type = '"+json.get("lopType").toString()+"'";
					}					
				if(json.get("timesheetdate")!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date selectedDate = sdf.parse(json.get("timesheetdate").toString());
					timesheetday = new Timestamp(selectedDate.getTime());
					where =where + " AND DATE(a.date_of_final_submission) = '"+timesheetday+"'";
				}
				if(json.get("sets")!=null){
					where =where + " AND a.sets = '"+json.get("sets").toString()+"'";
				}
					query= "select * from Employee_Timesheet_Status a " + query + "WHERE a.timesheet_month = '"
						+ timesheetMonths + "'" + where;
			}
			List<Employee_Timesheet_Status> tslist = session.createSQLQuery(query)
					.addEntity(Employee_Timesheet_Status.class).list();
			for (Employee_Timesheet_Status ets : tslist) {
				allTimesheet.add(ets);
			}
			if (allTimesheet != null) {
				if (!allTimesheet.isEmpty()) {
					zipPath = Utils.getProperty("fileLocation") + File.separator + "empTimesheetPdf.zip";
					ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath));
					for (Employee_Timesheet_Status empTimesheet : allTimesheet) {
						String timesheetUrl = empTimesheet.getTimesheetUploadPath();
						if ("None".equalsIgnoreCase(empTimesheet.getTimesheetError()) && timesheetUrl != null
								&& !"".equalsIgnoreCase(timesheetUrl)) {
							File fileToZip = new File(timesheetUrl);
							if (fileToZip.exists()) {
								try {
									zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
									Files.copy(fileToZip.toPath(), zipOut);
									zipOut.closeEntry();
								} catch (Exception e) {
									e.printStackTrace();
									failFiles = failFiles + empTimesheet.getEmployeeId() + ",";
								}
							} else {
								failFiles = failFiles + empTimesheet.getEmployeeId() + ",";
							}
						}
					}
					zipOut.close();
					fi = new FileInputStream(zipPath);
					files = IOUtils.toByteArray(fi);
					fi.close();
					File fil = new File(zipPath);
					if (fil.exists()) {
						fil.delete();
					}
					if ("".equalsIgnoreCase(failFiles)) {
						if (failFiles.endsWith(",")) {
							failFiles = failFiles.substring(0, failFiles.length() - 1);
						}
					}
					HttpHeaders headers = new HttpHeaders();
					headers.add("empFilesFailedtoCopy", failFiles);
					List<String> ls = new ArrayList<String>();
					ls.add("empFilesFailedtoCopy");
					headers.setAccessControlExposeHeaders(ls);
					responseEntity = new ResponseEntity<byte[]>(files, headers, HttpStatus.OK);
				} else {
					responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.NO_CONTENT);
				}
			} else {
				return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			File fil = new File(zipPath);
			if (fil.exists()) {
				fil.delete();
			}
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			session.close();
		}
		return responseEntity;
	}
	
	/*public void getAllTimesheetFiles2(String timesheetmonth, HttpServletResponse response) throws Exception {
		try {
			String failFiles = "";
			List<Employee_Timesheet_Status> allTimesheet = getAllTimesheetStatus(timesheetmonth);
			response.addHeader("empFilesFailedtoCopy", "");
			response.addHeader("Content-Disposition", "attachment; filename=\"timesheet.zip\"");
			ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
			// List<String> failFiles = new ArrayList<String>();
			for (Employee_Timesheet_Status empTimesheet : allTimesheet) {
				String timesheetUrl = empTimesheet.getTimesheetUploadPath();
				if ("None".equalsIgnoreCase(empTimesheet.getTimesheetError()) && timesheetUrl != null
						&& !"".equalsIgnoreCase(timesheetUrl)) {
					File fileToZip = new File(timesheetUrl);
					if (fileToZip.exists()) {
						try {
							zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
							Files.copy(fileToZip.toPath(), zipOut);
							zipOut.closeEntry();
						} catch (Exception e) {
							failFiles = failFiles + empTimesheet.getEmployeeId() + ",";
						}
					} else {
						failFiles = failFiles + empTimesheet.getEmployeeId() + ",";
					}

				}
			}
			if (failFiles.endsWith(",")) {
				failFiles = failFiles.substring(0, failFiles.length() - 1);
			}
			response.setHeader("empFilesFailedtoCopy", failFiles);
			response.addHeader("empFilesFailedtoCopy", failFiles);
			response.setStatus(HttpServletResponse.SC_OK);
			zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}*/

	@Override
	public Employee_Timesheet_Status getTimesheetStatus(String employeeId, String timesheetMonth) throws Throwable {
		Employee_Timesheet_Status empTimesheetStatus = null;
		Session session = null;
		session = sessionFactory.openSession();
		ResponseEntity<String> timesheet = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		java.util.Date selectedDate = sdf.parse(timesheetMonth);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		try {
			// empTimesheetStatus =(Employee_Timesheet_Status)
			// session.get(Employee_Timesheet_Status.class,
			// Integer.parseInt(employeeTimesheetStatusId));
			String query = "select * from Employee_Timesheet_Status where timesheet_month = :timesheet_month AND employee_id = :employee_id";
			List<Employee_Timesheet_Status> tslist = session.createSQLQuery(query)
					.addEntity(Employee_Timesheet_Status.class).setParameter("timesheet_month", timesheetMonths)
					.setParameter("employee_id", employeeId).list();
			if (!tslist.isEmpty() && tslist != null) {
				empTimesheetStatus = (Employee_Timesheet_Status) tslist.iterator().next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch  Timesheet Status Details");
		} finally {
			session.close();
		}
		return empTimesheetStatus;
	}

	public List<Employee_Timesheet_Status> getAllTimesheetStatusByDate(String timesheetdate) throws Throwable {
		ArrayList<Employee_Timesheet_Status> empTimesheetStatus = null;
		Session session = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp timesheetday = null;
		java.util.Date selectedDate = sdf.parse(timesheetdate);
		timesheetday = new Timestamp(selectedDate.getTime());
		try {
			session = sessionFactory.openSession();
			String query = "select * from Employee_Timesheet_Status where DATE(date_of_final_submission) = :date_of_final_submission ";
			List<Employee_Timesheet_Status> tslist = session.createSQLQuery(query)
					.addEntity(Employee_Timesheet_Status.class).setParameter("date_of_final_submission", timesheetday)
					.list();
			if (tslist != null) {
				empTimesheetStatus = new ArrayList<Employee_Timesheet_Status>();
				for (Employee_Timesheet_Status ts1 : tslist) {
					empTimesheetStatus.add(ts1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch Timesheet Status Details");
		} finally {
			session.close();
		}
		return empTimesheetStatus;
	}

	@Override
	public List<Employee_Timesheet_Status> getAllTimesheetStatus(String timesheetMonth) throws Throwable {
		ArrayList<Employee_Timesheet_Status> empTimesheetStatus = null;
		Session session = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		java.util.Date selectedDate = sdf.parse(timesheetMonth);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		try {
			session = sessionFactory.openSession();
			String query = "select * from Employee_Timesheet_Status where timesheet_month = :timesheet_month ";
			List<Employee_Timesheet_Status> tslist = session.createSQLQuery(query)
					.addEntity(Employee_Timesheet_Status.class).setParameter("timesheet_month", timesheetMonths).list();
			if (tslist != null) {
				empTimesheetStatus = new ArrayList<Employee_Timesheet_Status>();
				for (Employee_Timesheet_Status ts1 : tslist) {
					empTimesheetStatus.add(ts1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch Timesheet Status Details");
		} finally {
			session.close();
		}
		return empTimesheetStatus;
	}

	@Override
	public List<Object> getTimesheetDashboardDetails(String timesheetMonth) throws Throwable {
		ArrayList<Employee_Timesheet_Status> empTimesheetStatus = null;
		Session session = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		java.util.Date selectedDate = sdf.parse(timesheetMonth);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedDate);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		java.util.List personalList = null;
		java.util.List timesheetlist = null;
		java.util.List assignmentList = null;
		java.util.List salaryList = null;
		java.util.List bankDetailList = null;
		ArrayList<Object> employeeTimesheetDetailList = new ArrayList<Object>();
		try {
			session = sessionFactory.openSession();
			//String personalQuery = "select * from Employee_Personal_Details a LEFT JOIN Employee_Assignment_Details b ON a.employee_id=b.employee_id WHERE  b.client NOT LIKE '%Helius%'";
			String personalQuery = "select * from Employee_Personal_Details a LEFT JOIN Employee_Assignment_Details b ON a.employee_id=b.employee_id";
			personalList = session.createSQLQuery(personalQuery).addEntity(Employee_Personal_Details.class).list();
			String query = "select * from Employee_Timesheet_Status where timesheet_month = :timesheet_month ";
			timesheetlist = session.createSQLQuery(query).addEntity(Employee_Timesheet_Status.class)
					.setParameter("timesheet_month", timesheetMonths).list();
			String assignmentQuery = "select * from Employee_Assignment_Details";
			assignmentList = session.createSQLQuery(assignmentQuery).addEntity(Employee_Assignment_Details.class)
					.list();
			String salaryQuery = "select * from Employee_Salary_Details";
			salaryList = session.createSQLQuery(salaryQuery).addEntity(Employee_Salary_Details.class).list();
			String bankDetailQuery = "select * from Employee_Bank_Details";
			bankDetailList = session.createSQLQuery(bankDetailQuery).addEntity(Employee_Bank_Details.class).list();
			Employee_Timesheet_Status timesheet = null;
			Employee_Assignment_Details assignment = null;
			Employee_Bank_Details bankdet = null;
			Employee_Salary_Details salary = null;
			HashMap<String, Object> salaryDetailsMap = new HashMap<String, Object>();
			HashMap<String, Object> assignmentMap = new HashMap<String, Object>();
			HashMap<String, Object> bankDetailMap = new HashMap<String, Object>();
			HashMap<String, Object> timesheetMap = new HashMap<String, Object>();
			for (Object salarydetails : salaryList) {
				salary = (Employee_Salary_Details) salarydetails;
				salaryDetailsMap.put(salary.getEmployee_id(), salary);
			}
			for (Object ts : timesheetlist) {
				timesheet = (Employee_Timesheet_Status) ts;
				timesheetMap.put(timesheet.getEmployeeId(), timesheet);
			}
			for (Object assignmentdetails : assignmentList) {
				assignment = (Employee_Assignment_Details) assignmentdetails;
				assignmentMap.put(assignment.getEmployee_id(), assignment);
			}
			for (Object bankdetails : bankDetailList) {
				bankdet = (Employee_Bank_Details) bankdetails;
				bankDetailMap.put(bankdet.getEmployee_id(), bankdet);
			}
			Employee_Personal_Details personalDetails = null;
			for (Object emp : personalList) {
				String empname = "";
				String payroll = "";
				String group = "";
				String accountManager = "";
				String client = "";
				String timesheetType = "";
				String timesheetEmailId = "";
				String timesheetStatus = "";
				String lopType = "";
				String sets = "";
				String timesheetUploadPath = "";
				String bank_ifsc_code = "";
				Timestamp receivedDate = null;
				float lopDays = 0;
				int employeeTimesheetStatusId = 0;
				boolean salaryProcessingStatus = false;
				Timestamp finalSubmissionDate = null;
				Timestamp salaryProcessedDate = null;
				personalDetails = (Employee_Personal_Details) emp;
				if ("Exited".equalsIgnoreCase(personalDetails.getEmployee_status())) {
					if (personalDetails.getRelieving_date() != null) {
						java.sql.Date date = new java.sql.Date(personalDetails.getRelieving_date().getTime());
						LocalDate month1 = date.toLocalDate();
						int empExitMonth = month1.getMonthValue();
						int empExitYear = month1.getYear();
						if (year > empExitYear) {
							continue;
						}
						if (year == empExitYear && month > empExitMonth) {
							continue;
						}

					} else {
						continue;
					}
				}
				String empid = personalDetails.getEmployee_id();
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (personalDetails.getEmployee_name() != null) {
					empname = personalDetails.getEmployee_name();
				}
				if (timesheetMap.containsKey(empid)) {
					Employee_Timesheet_Status employeeTimesheetStatus = (Employee_Timesheet_Status) timesheetMap
							.get(empid);
					if (employeeTimesheetStatus.getTimesheetEmail() != null) {
						timesheetEmailId = employeeTimesheetStatus.getTimesheetEmail();
					}
					if (employeeTimesheetStatus.getTimesheetError() != null) {
						timesheetStatus = employeeTimesheetStatus.getTimesheetError();
					}
					if (employeeTimesheetStatus.getDateOfReceipt() != null) {
						receivedDate = employeeTimesheetStatus.getDateOfReceipt();
					}
					if (employeeTimesheetStatus.getDateOfFinalSubmission() != null) {
						finalSubmissionDate = employeeTimesheetStatus.getDateOfFinalSubmission();
					}
					if (employeeTimesheetStatus.getSalaryProcessedDate() != null) {
						salaryProcessedDate = employeeTimesheetStatus.getSalaryProcessedDate();
					}
					if (employeeTimesheetStatus.getTimesheetUploadPath() != null) {
						timesheetUploadPath = employeeTimesheetStatus.getTimesheetUploadPath();
					}
					if (employeeTimesheetStatus.getSets() != null) {
						sets = employeeTimesheetStatus.getSets();
					}
					if (employeeTimesheetStatus.getLopType() != null) {
						lopType = employeeTimesheetStatus.getLopType();
					}
					lopDays = employeeTimesheetStatus.getLopDays();
					salaryProcessingStatus = employeeTimesheetStatus.isSalaryProcessingStatus();
					employeeTimesheetStatusId = employeeTimesheetStatus.getEmployeeTimesheetStatusId();
				}
				if (assignmentMap.containsKey(empid)) {
					Employee_Assignment_Details employee_Assignment_Details = (Employee_Assignment_Details) assignmentMap
							.get(empid);
					if (employee_Assignment_Details.getClient_group() != null) {
						group = employee_Assignment_Details.getClient_group();
					}
					if (employee_Assignment_Details.getAccount_manager() != null) {
						accountManager = employee_Assignment_Details.getAccount_manager();
					}
					if (employee_Assignment_Details.getClient() != null) {
						client = employee_Assignment_Details.getClient();
					}
					if (employee_Assignment_Details.getTimesheet_type() != null) {
						timesheetType = employee_Assignment_Details.getTimesheet_type();
					}
				}
				if (salaryDetailsMap.containsKey(empid)) {
					Employee_Salary_Details employee_Salary_Details = (Employee_Salary_Details) salaryDetailsMap
							.get(empid);
					if (employee_Salary_Details.getPayroll_entity() != null) {
						payroll = employee_Salary_Details.getPayroll_entity();
					}
				}
				if (bankDetailMap.containsKey(empid)) {
					Employee_Bank_Details employee_Bank_Details = (Employee_Bank_Details)bankDetailMap
							.get(empid);
					if (employee_Bank_Details.getBank_ifsc_code() != null) {
						bank_ifsc_code = employee_Bank_Details.getBank_ifsc_code();
					}
				}
				map.put("payroll", payroll);
				map.put("timesheetType", timesheetType);
				map.put("client", client);
				map.put("account_manager", accountManager);
				map.put("client_group", group);
				map.put("finalSubmissionDate", finalSubmissionDate);
				map.put("receivedDate", receivedDate);
				map.put("timesheetStatus", timesheetStatus);
				map.put("timesheetEmailId", timesheetEmailId);
				map.put("employee_name", empname);
				map.put("employee_id", empid);
				map.put("timesheetUploadPath", timesheetUploadPath);
				map.put("lopType", lopType);
				map.put("lopDays", lopDays);
				map.put("sets", sets);
				map.put("bank_ifsc_code", bank_ifsc_code);
				map.put("employeeTimesheetStatusId",employeeTimesheetStatusId);
				map.put("salaryProcessingStatus", salaryProcessingStatus);
				map.put("salaryProcessedDate",salaryProcessedDate);
				employeeTimesheetDetailList.add(map);
			}
			salaryDetailsMap.clear();
			assignmentMap.clear();
			timesheetMap.clear();
			bankDetailMap.clear();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch Timesheet Status Details");
		} finally {
			session.close();
		}
		return employeeTimesheetDetailList;
	}

	@Override
	public String TimesheetPicklist(String timesheetMonth) throws Throwable {
		Session session = null;
		java.util.List empList = null;
		String emppicklist = "";
		java.util.List timesheetlist = null;
		String errorLists = null;
		ArrayList<Object> emplists = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		java.util.Date selectedDate = sdf.parse(timesheetMonth);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		ObjectMapper om = new ObjectMapper();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedDate);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		try {
			session = sessionFactory.openSession();
			String empQuery = "SELECT * FROM Employee_Personal_Details ORDER BY employee_name ASC";
			empList = session.createSQLQuery(empQuery).addEntity(Employee_Personal_Details.class).list();
			String query = "select * from Timesheet_Email";
			timesheetlist = session.createSQLQuery(query).addEntity(Timesheet_Email.class).list();
			Employee_Personal_Details empPersonDetails = null;
			Timesheet_Email timesheet = null;
			HashMap<String, String> timesheetMap = new HashMap<String, String>();
			for (Object ts : timesheetlist) {
				timesheet = (Timesheet_Email) ts;
				timesheetMap.put(timesheet.getEmployeeId(), timesheet.getTimesheetEmailId());
			}
			for (Object obj : empList) {
				empPersonDetails = (Employee_Personal_Details) obj;
				HashMap<String, Object> map = new HashMap<String, Object>();
				if ("Exited".equalsIgnoreCase(empPersonDetails.getEmployee_status())) {
					if (empPersonDetails.getRelieving_date() != null) {						
						java.sql.Date date = new java.sql.Date(empPersonDetails.getRelieving_date().getTime());
						LocalDate month1 = date.toLocalDate();
						int empExitMonth = month1.getMonthValue();
						int empExitYear = month1.getYear();
						if (year > empExitYear) {
							continue;
						}
						if (year == empExitYear && month > empExitMonth) {
							continue;
						}
					} else {
						continue;
					}
				}
				String timesheetEmailId = "";
				if (timesheetMap.containsKey(empPersonDetails.getEmployee_id())) {
					String email_id = timesheetMap.get(empPersonDetails.getEmployee_id());
					if (email_id != null) {
						timesheetEmailId = email_id;
					}
				}
				map.put("employee_id", empPersonDetails.getEmployee_id());
				map.put("employee_name", empPersonDetails.getEmployee_name());
				map.put("timesheetEmailId", timesheetEmailId);
				map.put("actual_date_of_joining", empPersonDetails.getActual_date_of_joining());
				map.put("relieving_date", empPersonDetails.getRelieving_date());
				emplists.add(map);
			}
			timesheetMap.clear();
			emppicklist = om.writeValueAsString(emplists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String query = "SELECT value from Timesheet_Master where type = 'timesheet_error'";
			java.util.List errorList = session.createSQLQuery(query).list();
			if (!errorList.isEmpty() && errorList != null) {
				errorLists = om.writeValueAsString(errorList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "{\"empIdNameList\" : " + emppicklist + ",\"errorList\" : " + errorLists + "}";
	}
	@Override
	public void sendEmailNotification(String jsonData) throws Throwable {
		try {
			employeeDAO.sendEmailNotification(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendEmailNotification(String jsonData, MultipartHttpServletRequest request) throws Throwable {
		try {
			Map<String, List<MultipartFile>> files = request.getMultiFileMap();
			List<File> al = new ArrayList<File>();
			if (files.size() > 0) {
				for (String key : files.keySet()) {
					List<MultipartFile> ff = files.get(key);
					for (MultipartFile multifile : ff) {
						File convFile = new File(multifile.getOriginalFilename());
						FileOutputStream fos = new FileOutputStream(convFile);
						fos.write(multifile.getBytes());
						al.add(convFile);
						fos.close();
					}
				}
			}
			ObjectMapper obm = new ObjectMapper();
			EmailScreen emailJson = obm.readValue(jsonData, EmailScreen.class);
			String st = emailJson.getCc();
			String[] cc = null;
			if (st != null && !st.isEmpty()) {
				cc = st.split(";");
			}
			String to = emailJson.getTo();
			String subject = emailJson.getSubject();
			String text = emailJson.getText();
			emailService.sendMessageWithAttachmentUsingTimesheetEmail(to, cc, subject, text, al);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateTimeSheetPDF(String timesheetMonth) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		List<Employee_Timesheet_Status> empTimesheetStatus = null;
		java.util.Date selectedDate = sdf.parse(timesheetMonth);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		try {
			String query = "select * from Employee_Timesheet_Status where timesheet_month = :timesheet_month AND timesheet_error = 'None'";
			List<Employee_Timesheet_Status> tslist = session.createSQLQuery(query)
					.addEntity(Employee_Timesheet_Status.class).setParameter("timesheet_month", timesheetMonths).list();
			if (!tslist.isEmpty() && tslist != null) {
				empTimesheetStatus = new ArrayList<Employee_Timesheet_Status>();
				for (Employee_Timesheet_Status ts : tslist) {
					empTimesheetStatus.add(ts);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch timesheet details" + e.getMessage());
		} finally {
			session.close();
		}
	}
	
	public String validateRTSTimesheet(String json, MultipartHttpServletRequest RTSFile)throws Throwable{
			InputStream xlsxContentStream = null;
			OPCPackage	pkg = null;
			String result = null;
			HashMap<String, ArrayList<Row>> uniqueJobtypHrs = null;
			HashMap<String, String> nameID = null;
			List<String> issueinprocessingOneBnankId  = null;
			int grandTotalIndex = 0;
			try{
			JSONObject jsonData = (JSONObject) JSONValue.parse(json);
			SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
			java.util.Date selectedMonth = sdfMonth.parse(jsonData.get("timesheetmonth").toString());
			Timestamp timesheetMonth = new Timestamp(selectedMonth.getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(selectedMonth);
			int month = calendar.get(Calendar.MONTH)+1;
			int year = calendar.get(Calendar.YEAR);
			ObjectMapper om = new ObjectMapper();
			try {
				Map<String, List<MultipartFile>> allfiles = RTSFile.getMultiFileMap();
				for (List<MultipartFile> files : allfiles.values()) {
					for (MultipartFile file : files) {
						String filename = file.getOriginalFilename();
						xlsxContentStream = file.getInputStream();
						pkg = OPCPackage.open(xlsxContentStream);
					}
				}
			} catch (InvalidFormatException e) {
				e.printStackTrace();
				return "unable to open RTS File";
			}
			if (pkg != null) {
				try {
					HashMap<String, Object> timesheetMapData =	readRTSTimesheetAndLeaveHEeader(pkg);
					uniqueJobtypHrs =  (HashMap<String, ArrayList<Row>>) timesheetMapData.get("uniqueJobtypHrs");
					nameID =  (HashMap<String, String>) timesheetMapData.get("nameID");
					issueinprocessingOneBnankId =  (List<String>) timesheetMapData.get("issueinprocessingOneBnankId");
					grandTotalIndex = (int) timesheetMapData.get("grandTotalIndex");
				} catch (IOException e) {
					e.printStackTrace();
					throw new Throwable("Failed to Process timesheet " + e.getMessage());
				}
			} else {
				return "unable to open RTS File";
			}
			float validhrs = Float.parseFloat(jsonData.get("totalhours").toString());
			float sum = 0;
			Integer i = 0;
			String hoursList = null;
			ArrayList<HashMap<String, Object>> correctClocking = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> inCorrectClocking = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> newClocking = new ArrayList<HashMap<String, Object>>();
			HashMap<String,OnebankEmployeeDetailsAssoc> onebankEmpAssocDetailsMap = getOnebankEmpAssocDetails(month,year);
			HashMap<String,String> rtsEmpList =getRTSEmpList(month,year);
			Set<String> noClocking = new HashSet<String>(rtsEmpList.values());
			if(noClocking.contains(null)){
			noClocking.remove(null);
			}
			for (Map.Entry<String, ArrayList<Row>> entry : uniqueJobtypHrs.entrySet()) {
				String key = entry.getKey();
				String empid = null;
				OnebankEmployeeDetailsAssoc onebankEmployeeDetailsAssoc = onebankEmpAssocDetailsMap.get(key);
				if (onebankEmployeeDetailsAssoc != null) {
					if (onebankEmployeeDetailsAssoc.getEmployee_id() != null
							&& !"".equalsIgnoreCase(onebankEmployeeDetailsAssoc.getEmployee_id())) {
						empid = onebankEmployeeDetailsAssoc.getEmployee_id();
					}
				}
				ArrayList<Row> rows = entry.getValue();
				Iterator<Row> iter = rows.iterator();
				while (iter.hasNext()) {
					Row row = iter.next();
					hoursList = row.getCell(grandTotalIndex).toString();
					float hrs = Float.parseFloat(hoursList);
					sum = sum + hrs;
				}
				HashMap<String, Object> ClockersMAP = new HashMap<String, Object>();				
				if (sum == validhrs) {
					if (nameID.containsKey(key)) {
						ClockersMAP.put("empName", nameID.get(key).toString());
					}
					if (empid != null) {
						ClockersMAP.put("empId", empid);
					}else{
						ClockersMAP.put("empId", "");
					}
						ClockersMAP.put("onebankid", key);
						ClockersMAP.put("RTS_hours", sum);
						correctClocking.add(ClockersMAP);
				}
				if (sum < validhrs || sum > validhrs) {
					ClockersMAP.put("onebankid", key);
					if (empid != null) {
						ClockersMAP.put("empId", empid);
					}else{
						ClockersMAP.put("empId", "");
					}
					if (nameID.containsKey(key)) {
						ClockersMAP.put("empName", nameID.get(key).toString());
					}
					ClockersMAP.put("RTS_hours", sum);
					inCorrectClocking.add(ClockersMAP);
				}
				sum = 0;
				if(rtsEmpList != null && !rtsEmpList.isEmpty()){
					if(rtsEmpList.containsValue(key)){
						noClocking.remove(key);
					}
					if(!rtsEmpList.containsValue(key)){
						newClocking.add(ClockersMAP);
					}
				}
			}		
			ArrayList<HashMap<String,String>> noClockingEmpIdList = new ArrayList<HashMap<String,String>>();
			ArrayList<String> noClockingBCCList = new ArrayList<String>();
			for (String obj : noClocking) {
				if (onebankEmpAssocDetailsMap.containsKey(obj)) {
					OnebankEmployeeDetailsAssoc onebankAssocObj = onebankEmpAssocDetailsMap.get(obj);
					HashMap<String,String> noclockingMap = new HashMap<String,String>();
					noclockingMap.put("empId", onebankAssocObj.getEmployee_id());
					noclockingMap.put("empName", onebankAssocObj.getEmployee_name());
					noclockingMap.put("onebankid", onebankAssocObj.getOnebankId());
					noClockingEmpIdList.add(noclockingMap);
					String to = null;
					if(onebankAssocObj.getClient_email_id() !=null && !"".equalsIgnoreCase(onebankAssocObj.getClient_email_id())){
					if (onebankAssocObj.getClient_email_id().contains("@dbs.com")) {
						to = onebankAssocObj.getClient_email_id();
					} else {
						to = onebankAssocObj.getPersonal_email_id();
					}
					noClockingBCCList.add(to);
					}
				}
			}
			HashMap<String,String> noCliEmailID = fetchEmptyClientEmailId();
			ArrayList<HashMap<String,String>> empNocliemails = new ArrayList<HashMap<String,String>>();
			for(Map.Entry<String,String> entry : noCliEmailID.entrySet()){
				HashMap<String,String> nocliemailMap = new HashMap<String,String>();
				nocliemailMap.put("empId", entry.getKey());
				nocliemailMap.put("empName", entry.getValue());
				empNocliemails.add(nocliemailMap);
			}
			HashMap<String,Object> resultobj = new HashMap<String,Object>();
			resultobj.put("correctClocking",correctClocking);
			resultobj.put("inCorrectClocking",inCorrectClocking);
			resultobj.put("noClocking",noClockingEmpIdList);
			resultobj.put("newClocking",newClocking);
			resultobj.put("noCliEmailID",empNocliemails);		
			resultobj.put("unprocessedOneBankId",issueinprocessingOneBnankId);	
			result=om.writeValueAsString(resultobj);
			if(jsonData.get("sendEmail").toString() != null && "yes".equalsIgnoreCase(jsonData.get("sendEmail").toString())){
				for(HashMap<String, Object> obj : inCorrectClocking){					
					if(onebankEmpAssocDetailsMap.containsKey(obj.get("onebankid"))){
					OnebankEmployeeDetailsAssoc onebankAssocObj =  onebankEmpAssocDetailsMap.get(obj.get("onebankid"));
					String to = null;
					String bcc[] = null;
					String[] cc = null;
					String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
					if(onebankAssocObj.getClient_email_id().contains("@dbs.com")){
						to = onebankAssocObj.getClient_email_id();
					}else{
						to = onebankAssocObj.getPersonal_email_id();
					}
					String subject = "Timesheet Missed Clocking";
					StringBuffer message = new StringBuffer();
					message.append("Dear "+onebankAssocObj.getEmployee_name()+","+ "\n\n");			
					message.append("Your RTS timesheet for the month of "+timesheetMonth+" does not have the required number of hours. "+"\n\n");
					message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
					try{
					emailService.sendBulkEmail(to, cc, bcc, subject, message.toString());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				}		
				if(noClockingBCCList.size()>0){
					String noclockedTo = Utils.getHapProperty("RTSTimeSheetNotification-TO");;
					String[] noclockedcc = null;
					String[] noclockedbcc = null;
					String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
					if (getCC != null && !getCC.isEmpty()) {
						noclockedcc = getCC.split(",");
					}
					noclockedbcc = noClockingBCCList.toArray(new String[noClockingBCCList.size()]);
					String subject =" Timesheet Reminder ";
					StringBuffer message = new StringBuffer();
					message.append("Dear,"+ "\n\n");			
					message.append("You have not yet submitted the RTS timesheet for the month of "+timesheetMonth+". Please do so at the earliest."+"\n\n");
					message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
					try{
					emailService.sendBulkEmail(noclockedTo,noclockedcc,noclockedbcc,subject,message.toString());
					}catch(Exception e){
						e.printStackTrace();	
					}
				}
			}
			/*FileWriter file = null;
			try {
	            // Constructs a FileWriter given a file name, using the platform's default charset
	            file = new FileWriter("C:/Users/HELIUS/Documents/RTSRESULT.txt");
	            file.write(result);
	        } catch (IOException e) {
	            e.printStackTrace();
	 
	        }*/
			
			
			/*Logger logger = Logger.getLogger(TimeSheetAndLeaveServiceImpl.class.getName());
	        FileHandler fileHandler = new FileHandler("timesheet.log", true);        
	        logger.addHandler(fileHandler);
	        if (logger.isLoggable(Level.INFO)) {
	            logger.info("Information message");
	        }
	        if (logger.isLoggable(Level.WARNING)) {
	            logger.warning("Warning message");
	        }*/
			
			}catch(Exception e){
	            e.printStackTrace();
				throw new Throwable("Failed to Process timesheet " + e.getMessage());
			}
			return result;		
		}
	
	public HashMap<String, Object> readRTSTimesheetAndLeaveHEeader(OPCPackage pkg) throws Throwable {
		int headerIndex = 0;
		int inscopeNameIndex = 0;
		int grandTotalIndex = 0;
		int inscope1bankIDIndex = 0;
		int inscopeJobTypeIndex = 0;
		Sheet sheet = null;
		HashMap<String, ArrayList<Row>> uniqueJobtypHrs = null;
		HashMap<String, String> nameID = null;
		List<String> issueinprocessingOneBnankId = new ArrayList<String>();
		HashMap<String, Object> timesheetMapData = new HashMap<String, Object>();
		try{
		Workbook workbook = new XSSFWorkbook(pkg);
		sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			XSSFCell cell;
			Iterator cells = row.cellIterator();
			while (cells.hasNext()) {
				cell = (XSSFCell) cells.next();
				if ("Inscope 1bankID".equalsIgnoreCase(cell.toString())
						|| "Inscope1bankID".equalsIgnoreCase(cell.toString())
						|| "Inscope 1bankID".trim().equalsIgnoreCase(cell.toString().trim())
						|| "Inscope1bankID".trim().equalsIgnoreCase(cell.toString().trim())) {
					headerIndex = cell.getRowIndex();
					inscope1bankIDIndex = cell.getColumnIndex();
				}
				if ("Inscope Job Type".equalsIgnoreCase(cell.toString()) || "Inscope Clocking Type".equalsIgnoreCase(cell.toString())) {
					inscopeJobTypeIndex = cell.getColumnIndex();
				}
				if ("Inscope Name".equalsIgnoreCase(cell.toString()) || "Name".equalsIgnoreCase(cell.toString()) ) {
					headerIndex = cell.getRowIndex();
					inscopeNameIndex = cell.getColumnIndex();
				}
				if ("Grand Total".equalsIgnoreCase(cell.toString())) {
					grandTotalIndex = cell.getColumnIndex();
				}
			}
			if (headerIndex != 0) {
				break;
			}
		}
	//	readExcel(inscope1bankIDIndex, inscopeJobTypeIndex, grandTotalIndex);
		uniqueJobtypHrs = new HashMap<String, ArrayList<Row>>();
		nameID = new HashMap<String, String>();
		int i = 0;
		String uniqueId = null;
		String prevUniqueId = null;
		try{
		for (Row row : sheet) {
			if (i >= headerIndex) {
				if (row.getCell(inscope1bankIDIndex) == null || row.getCell(inscope1bankIDIndex).toString().isEmpty()) {
					try{
					uniqueId = prevUniqueId;
					if (uniqueJobtypHrs.containsKey(uniqueId) && (row.getCell(inscopeJobTypeIndex) != null
							&& !row.getCell(inscopeJobTypeIndex).toString().isEmpty())) {
						ArrayList<Row> rowdata = uniqueJobtypHrs.get(uniqueId);
						rowdata.add(row);
						uniqueJobtypHrs.put(uniqueId, rowdata);
					}
					}catch (Exception e) {
						e.printStackTrace();
						issueinprocessingOneBnankId.add(uniqueId);
					}
				} else {
					try{
					uniqueId = row.getCell(inscope1bankIDIndex).toString();
					if (row.getCell(inscopeJobTypeIndex) == null
							|| row.getCell(inscopeJobTypeIndex).toString().isEmpty()) {
						continue;
					}
					prevUniqueId = uniqueId;
					ArrayList<Row> rowdata = new ArrayList<Row>();
					rowdata.add(row);
					nameID.put(uniqueId, row.getCell(inscopeNameIndex).toString());
					uniqueJobtypHrs.put(uniqueId, rowdata);
				}catch (Exception e) {
					e.printStackTrace();
					issueinprocessingOneBnankId.add(uniqueId);
				}
				}
			}
			i++;
		}
		for(String onebankid : issueinprocessingOneBnankId){
			if(uniqueJobtypHrs.containsKey(onebankid)){
				uniqueJobtypHrs.remove(onebankid);
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to process timesheet details " + e.getMessage());
		}
		if (workbook != null) {
			workbook.close();
		}
		}catch (Exception e) {
			throw new Throwable("Failed to read timesheet header" + e.getMessage());
		}
		timesheetMapData.put("uniqueJobtypHrs", uniqueJobtypHrs);
		timesheetMapData.put("nameID", nameID);
		timesheetMapData.put("issueinprocessingOneBnankId", issueinprocessingOneBnankId);
		timesheetMapData.put("grandTotalIndex", grandTotalIndex);
		return timesheetMapData;
	}

	/*public  void readExcel(int inscope1bankIDIndex, int inscopeJobTypeIndex, int grandTotalIndex)
			throws Throwable {
		uniqueJobtypHrs = new HashMap<String, ArrayList<Row>>();
		HashMap<String, String> jobwtypeHours = new HashMap<String, String>();
		nameID = new HashMap<String, String>();
		int i = 0;
		String uniqueId = null;
		String prevUniqueId = null;
		try{
		for (Row row : sheet) {
			if (i >= headerIndex) {
				if (row.getCell(inscope1bankIDIndex) == null || row.getCell(inscope1bankIDIndex).toString().isEmpty()) {
					try{
					uniqueId = prevUniqueId;
					if (uniqueJobtypHrs.containsKey(uniqueId) && (row.getCell(inscopeJobTypeIndex) != null
							&& !row.getCell(inscopeJobTypeIndex).toString().isEmpty())) {
						ArrayList<Row> rowdata = uniqueJobtypHrs.get(uniqueId);
						rowdata.add(row);
						uniqueJobtypHrs.put(uniqueId, rowdata);
					}
					}catch (Exception e) {
						e.printStackTrace();
						issueinprocessingOneBnankId.add(uniqueId);
					}
				} else {
					try{
					uniqueId = row.getCell(inscope1bankIDIndex).toString();
					
					 * if (uniqueId.equals("Grand Total")) { continue; }
					 
					if (row.getCell(inscopeJobTypeIndex) == null
							|| row.getCell(inscopeJobTypeIndex).toString().isEmpty()) {
						continue;
					}
					prevUniqueId = uniqueId;
					ArrayList<Row> rowdata = new ArrayList<Row>();
					rowdata.add(row);
					nameID.put(uniqueId, row.getCell(inscopeNameIndex).toString());
					uniqueJobtypHrs.put(uniqueId, rowdata);
				}catch (Exception e) {
					e.printStackTrace();
					issueinprocessingOneBnankId.add(uniqueId);
				}
				}
			}
			i++;
		}
		for(String onebankid : issueinprocessingOneBnankId){
			if(uniqueJobtypHrs.containsKey(onebankid)){
				uniqueJobtypHrs.remove(onebankid);
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to read timesheet details " + e.getMessage());
		}
		
		 * int y=0; Row leaveHeaderresult = null; for (Row leaveHeader : sheet)
		 * { if (y == leaveHeaderIndex) {
		 * System.out.println("leave num -======== "
		 * +leaveHeader.getFirstCellNum()+"==="+leaveHeader.getCell(leaveHeader.
		 * getFirstCellNum())); leaveHeaderresult=leaveHeader; } y++; } 
	}
*/
	public HashMap<String,String> fetchEmptyClientEmailId() throws Throwable {
		Session session = null;
		List<Object[]> rtsEmpList = null;
		HashMap<String,String> map = new HashMap<String,String>();
		try {
			session = sessionFactory.openSession();
			String query = "SELECT a.employee_id,a.employee_name FROM Employee_Personal_Details a LEFT JOIN  Employee_Assignment_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Work_Permit_Details c ON a.employee_id = c.employee_id WHERE b.client='DBS' AND b.timesheet_type='automated' AND (b.client_email_id IS NULL OR b.client_email_id = '') AND c.work_country = 'Singapore' AND a.employee_status = 'Active'";
			rtsEmpList = session.createSQLQuery(query).list();
			session.close();
			for(Object[] obj :rtsEmpList){
				String name = null;
				if(obj[1] != null){
					name=obj[1].toString();
				}
				map.put(obj[0].toString(), name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch no client emial id details " + e.getMessage());
		}
		return map;
	}
	
	public HashMap<String,OnebankEmployeeDetailsAssoc> getOnebankEmpAssocDetails(int month,int year) throws Throwable {
		Session session = null;
		List<OnebankEmployeeDetailsAssoc> onebankEmpAssocList = null;
		HashMap<String,OnebankEmployeeDetailsAssoc> onebankEmpList = new HashMap<String,OnebankEmployeeDetailsAssoc>();
		try {
			session = sessionFactory.openSession();
			String query = "SELECT a.employee_id,SUBSTRING(a.client_email_id, 1, LOCATE('@', a.client_email_id) - 1) AS onebankId,a.client_email_id,d.personal_email_id,b.employee_name FROM Employee_Assignment_Details a LEFT JOIN  Employee_Personal_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Work_Permit_Details c ON a.employee_id = c.employee_id LEFT JOIN Employee_Offer_Details d ON a.employee_id= d.employee_id WHERE a.client='DBS' AND a.timesheet_type='automated' AND a.client_email_id IS NOT NULL AND a.client_email_id != '' AND c.work_country = 'Singapore' AND  (b.employee_status = 'Active' OR (b.employee_status ='Exited' AND MONTH(b.relieving_date)>= :month AND YEAR(b.relieving_date)= :year))";
			Query onebankEmpAssocQuery = session.createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(OnebankEmployeeDetailsAssoc.class)).setParameter("month",month).setParameter("year",year);
			onebankEmpAssocList = onebankEmpAssocQuery.list();
			session.close();
			for(OnebankEmployeeDetailsAssoc obj :onebankEmpAssocList){
				onebankEmpList.put(obj.getOnebankId(),obj);
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			throw new Throwable("Failed to fetch OnebankEmployeeDetailsAssoc details " + e.getMessage());
		}
		return onebankEmpList;
	}
	/**
	 * fetch total no: rts employee list
	 * **/
	public HashMap<String,String> getRTSEmpList(int month,int year) throws Throwable{
		Session session = null;
		List<Object[]> rtsEmpList = null;
		HashMap<String,String> RTSEmpList = new HashMap<String,String>();
		try {
			session = sessionFactory.openSession();
			//String query = "SELECT a.employee_id,SUBSTRING(b.client_email_id, 1, LOCATE('@', b.client_email_id) - 1) AS clientId FROM Employee_Personal_Details a LEFT JOIN  Employee_Assignment_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Work_Permit_Details c ON a.employee_id = c.employee_id WHERE b.client='DBS' AND b.timesheet_type='automated' AND c.work_country = 'Singapore' AND a.employee_status = 'Active'";
			
			String query = "SELECT a.employee_id,SUBSTRING(b.client_email_id, 1, LOCATE('@', b.client_email_id) - 1) AS clientId FROM Employee_Personal_Details a LEFT JOIN  Employee_Assignment_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Work_Permit_Details c ON a.employee_id = c.employee_id WHERE b.client='DBS' AND b.timesheet_type='automated' AND c.work_country = 'Singapore' AND (a.employee_status = 'Active' OR (a.employee_status ='Exited' AND MONTH(a.relieving_date)>= :month AND YEAR(a.relieving_date)= :year))";
			rtsEmpList = session.createSQLQuery(query).setParameter("month",month).setParameter("year",year).list();
			session.close();
			for(Object[] obj :rtsEmpList){
				String onbankemail = null;
				if(obj[1] != null){
					onbankemail=obj[1].toString();
				}
				RTSEmpList.put(obj[0].toString(), onbankemail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch RTS Employee List ");
		}
		System.out.println("RTSEmpList =====size====="+RTSEmpList.size());
		return RTSEmpList;
	}
	/*public List<String> getRTSEmpList() {
		Session session = null;
		List rtsEmpList = null;
		try {
			session = sessionFactory.openSession();
			String query = "SELECT a.employee_id,SUBSTRING(b.client_email_id, 1, LOCATE('@', b.client_email_id) - 1) AS clientId FROM Employee_Personal_Details a LEFT JOIN  Employee_Assignment_Details b ON a.employee_id=b.employee_id LEFT JOIN Employee_Work_Permit_Details c ON a.employee_id = c.employee_id WHERE b.client='DBS' AND b.timesheet_type='automated' AND c.work_country = 'Singapore' AND a.employee_status = 'Active'";
			rtsEmpList = session.createSQLQuery(query).list();
			session.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return rtsEmpList;
	}*/

	public Employee_Timesheet_Status getTimesheetDetails(String employeeId) {
		Employee_Timesheet_Status check_Employee_Timesheets = null;
		String status = "";

		Session session = null;
		try {
			session = sessionFactory.openSession();
			String query = "select * from Employee_Timesheet_Status where employee_id = :employee_id ";
			java.util.List empTSList = session.createSQLQuery(query).addEntity(Employee_Timesheet_Status.class)
					.setParameter("employee_id", employeeId).list();
			if (!empTSList.isEmpty()) {
				check_Employee_Timesheets = (Employee_Timesheet_Status) empTSList.iterator().next();
			}
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return check_Employee_Timesheets;
	}

	public String getEmpIdforOnebankId(String onebankid) {
		String empid = null;
		Session session = null;
		try {
			session = sessionFactory.openSession();
			SQLQuery querydropdown = session.createSQLQuery(
					"SELECT employee_id from onebankid_employeeid_assoc where onebank_id =:onebank_id");
			querydropdown.setParameter("onebank_id", onebankid);
			List<String> empidsss = querydropdown.list();
			for (String emp : empidsss) {
				if (emp != null) {
					empid = emp.toString();
				}
			}
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empid;
	}
	
	public String saveRTSTimesheet(String json, MultipartHttpServletRequest RTSFile) throws Throwable {
		String result = null;
		Session session = null;
		Transaction transaction = null;
		String status = null;		
		OPCPackage	pkg = null;
		MultipartFile fileRts = null;
		JSONObject jsonData = (JSONObject) JSONValue.parse(json);
		ObjectMapper om = new ObjectMapper();
		SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
		java.util.Date selectedMonth = sdfMonth.parse(jsonData.get("timesheetmonth").toString());
		Timestamp timesheetMonth = new Timestamp(selectedMonth.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedMonth);
		int month = calendar.get(Calendar.MONTH)+1;
		int year = calendar.get(Calendar.YEAR);
		String	filename = null;
		HashMap<String, ArrayList<Row>> uniqueJobtypHrs = null;
		HashMap<String, String> nameID = null;
		List<String> issueinprocessingOneBnankId  = null;
		int grandTotalIndex = 0;
		try {
			Map<String, List<MultipartFile>> allfiles = RTSFile.getMultiFileMap();
			for (List<MultipartFile> files : allfiles.values()) {
				for (MultipartFile file : files) {
					fileRts = file;
					filename = file.getOriginalFilename();
					InputStream xlsxContentStream = file.getInputStream();
					pkg = OPCPackage.open(xlsxContentStream);
				}
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return "unable to open RTS File";
		}
		if (pkg != null) {
			try {
				HashMap<String, Object> timesheetMapData =	readRTSTimesheetAndLeaveHEeader(pkg);
				uniqueJobtypHrs =  (HashMap<String, ArrayList<Row>>) timesheetMapData.get("uniqueJobtypHrs");
				nameID =  (HashMap<String, String>) timesheetMapData.get("nameID");
				issueinprocessingOneBnankId =  (List<String>) timesheetMapData.get("issueinprocessingOneBnankId");
				grandTotalIndex = (int) timesheetMapData.get("grandTotalIndex");
			} catch (IOException e) {
				e.printStackTrace();
				throw new Throwable("Failed to Process timesheet " + e.getMessage());
			}
		} else {
			return "unable to open RTS File";
		}
		float validhrs = Float.parseFloat(jsonData.get("totalhours").toString());
		float sum = 0;
		int i = 0;
		String hoursList = null;
		try {
			List<Employee_Timesheet_Status> checkExistingTimesheetList = null;
			HashMap<String, Employee_Timesheet_Status> checkExistingTimesheetMap = new HashMap<String, Employee_Timesheet_Status>();
			try {
				checkExistingTimesheetList = getAllTimesheetStatus(jsonData.get("timesheetmonth").toString());
				for(Employee_Timesheet_Status employee_Timesheet_Status : checkExistingTimesheetList){
					checkExistingTimesheetMap.put(employee_Timesheet_Status.getEmployeeId(), employee_Timesheet_Status);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Throwable("Failed to fetch Existing Timesheet Status Details");
			}    
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ArrayList<HashMap<String, Object>> correctClocking = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> inCorrectClocking = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> newClocking = new ArrayList<HashMap<String, Object>>();
			HashMap<String,OnebankEmployeeDetailsAssoc> onebankEmpAssocDetailsMap = getOnebankEmpAssocDetails(month,year);		
			HashMap<String,String> noCliEmailID = fetchEmptyClientEmailId();	
			HashMap<String,String> rtsEmpList =getRTSEmpList(month,year);
			Set<String> noClocking = new HashSet<String>(rtsEmpList.values());
			if(noClocking.contains(null)){
			noClocking.remove(null);
			}
			String fileURL = null;
			String	tempfilelocation = null;
			try{
			tempfilelocation = Utils.getProperty("fileLocation") + File.separator + "rtsTimesheets";
			fileURL = LocalDateTime.now().toString().replaceAll(":","_")+"_"+filename;
			}catch(Exception e){
				e.printStackTrace();
			}
			for (Map.Entry<String, ArrayList<Row>> entry : uniqueJobtypHrs.entrySet()) {
				String key = null;
				try{
				 key = entry.getKey();
				String empid = null;
				OnebankEmployeeDetailsAssoc onebankEmployeeDetailsAssoc = onebankEmpAssocDetailsMap.get(key);
				if (onebankEmployeeDetailsAssoc != null) {
					if (onebankEmployeeDetailsAssoc.getEmployee_id() != null
							&& !"".equalsIgnoreCase(onebankEmployeeDetailsAssoc.getEmployee_id())) {
						empid = onebankEmployeeDetailsAssoc.getEmployee_id();
					}
				}
				ArrayList<Row> rows = entry.getValue();
				Iterator<Row> iter = rows.iterator();
				HashMap<String, Object> ClockersMAP = new HashMap<String, Object>();				
				while (iter.hasNext()) {
					Row row = iter.next();
					hoursList = row.getCell(grandTotalIndex).toString();
					float hrs = Float.parseFloat(hoursList);
					sum = sum + hrs;
				}
				if (sum < validhrs || sum > validhrs) {
					status = "Missed Clocking";
					ClockersMAP.put("onebankid", key);
					if (nameID.containsKey(key)) {
						ClockersMAP.put("empName", nameID.get(key).toString());
					}
					if (empid != null) {
						ClockersMAP.put("empId", empid);
					}else{
						ClockersMAP.put("empId", "");
					}
					ClockersMAP.put("RTS_hours", sum);
					inCorrectClocking.add(ClockersMAP);
				}
				if (sum == validhrs) {
					status = "None";
					if (nameID.containsKey(key)) {
						ClockersMAP.put("empName", nameID.get(key).toString());
					}
					if (empid != null) {
						ClockersMAP.put("empId", empid);
					}else{
						ClockersMAP.put("empId", "");
					}	
						ClockersMAP.put("onebankid", key);
						ClockersMAP.put("RTS_hours", sum);
						correctClocking.add(ClockersMAP);
				}
				if(rtsEmpList != null && !rtsEmpList.isEmpty()){
					if(rtsEmpList.containsValue(key)){
						noClocking.remove(key);
					}
					if(!rtsEmpList.containsValue(key)){
						newClocking.add(ClockersMAP);
					}
				}
				if(empid != null){
					Employee_Timesheet_Status employee_Timesheets = new Employee_Timesheet_Status();
					employee_Timesheets.setTimesheetError(status);
					employee_Timesheets.setEmployeeId(empid);
					employee_Timesheets.setCreated_by("HAP");
					employee_Timesheets.setTimesheetMonth(timesheetMonth);
					employee_Timesheets.setTimesheetUploadPath(tempfilelocation+File.separator+fileURL);
					employee_Timesheets.setEmployeeName(onebankEmployeeDetailsAssoc.getEmployee_name());
					if (!checkExistingTimesheetMap.containsKey(empid)) {
						session.save(employee_Timesheets);
					} else {
						Employee_Timesheet_Status Timesheet_Status = checkExistingTimesheetMap.get(empid);
						employee_Timesheets.setEmployeeTimesheetStatusId(Timesheet_Status.getEmployeeTimesheetStatusId());
						session.update(employee_Timesheets);
					}
					  if (i%50 == 0) {
						  session.flush();
						  session.clear(); 
					 }
					  i++;
				}
				sum = 0;
				}catch(Exception e){
					e.printStackTrace();
					issueinprocessingOneBnankId.add(key);
				}
			}
			int j=0;
			for (String noclock : noClocking) {
				try{
				if (onebankEmpAssocDetailsMap.containsKey(noclock)) {
					OnebankEmployeeDetailsAssoc onebankAssocObj = onebankEmpAssocDetailsMap.get(noclock);
					String noclkEmpId = null;
					if (onebankAssocObj != null) {
						if (onebankAssocObj.getEmployee_id() != null
								&& !"".equalsIgnoreCase(onebankAssocObj.getEmployee_id())) {
							noclkEmpId = onebankAssocObj.getEmployee_id();
						}
					}
					if(noclkEmpId != null){
						Employee_Timesheet_Status noclkemployee_Timesheets = new Employee_Timesheet_Status();
						noclkemployee_Timesheets.setEmployeeId(noclkEmpId);
						noclkemployee_Timesheets.setCreated_by("HAP");
						noclkemployee_Timesheets.setTimesheetMonth(timesheetMonth);
						noclkemployee_Timesheets.setTimesheetError("No Clocking");
						noclkemployee_Timesheets.setEmployeeName(onebankAssocObj.getEmployee_name());
								if (!checkExistingTimesheetMap.containsKey(noclkEmpId)) {
									session.save(noclkemployee_Timesheets);
								} else {
									Employee_Timesheet_Status noclkTimesheet_Status = checkExistingTimesheetMap.get(noclkEmpId);
									noclkemployee_Timesheets.setEmployeeTimesheetStatusId(noclkTimesheet_Status.getEmployeeTimesheetStatusId());
									session.update(noclkemployee_Timesheets);
								}
								  if (j%50 == 0) {
									  session.flush();
									  session.clear(); 
								 }
								  j++;	
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				issueinprocessingOneBnankId.add(noclock);
			}
			}
			int z = 0;
			for(Map.Entry<String, String> empNocliemaild : noCliEmailID.entrySet()){		
				try{
					String empid = empNocliemaild.getKey();
					Employee_Timesheet_Status noemailidemployee_Timesheets = new Employee_Timesheet_Status();
					noemailidemployee_Timesheets.setEmployeeId(empid);
					noemailidemployee_Timesheets.setCreated_by("HAP");
					noemailidemployee_Timesheets.setTimesheetMonth(timesheetMonth);
					noemailidemployee_Timesheets.setTimesheetError("No OnebankId");
					noemailidemployee_Timesheets.setEmployeeName(empNocliemaild.getValue());
							if (!checkExistingTimesheetMap.containsKey(empid)) {
								session.save(noemailidemployee_Timesheets);
							} else {
								Employee_Timesheet_Status nocliemailTimesheet_Status = checkExistingTimesheetMap.get(empid);
								noemailidemployee_Timesheets.setEmployeeTimesheetStatusId(nocliemailTimesheet_Status.getEmployeeTimesheetStatusId());
								session.update(noemailidemployee_Timesheets);
							}
							  if (z%50 == 0) {
								  session.flush();
								  session.clear(); 
							 }
							  z++;	
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			ArrayList<HashMap<String,String>> noClockingEmpIdList = new ArrayList<HashMap<String,String>>();
			ArrayList<String> noClockingBCCList = new ArrayList<String>();
			for (String obj : noClocking) {
				if (onebankEmpAssocDetailsMap.containsKey(obj)) {
					OnebankEmployeeDetailsAssoc onebankAssocObj = onebankEmpAssocDetailsMap.get(obj);
					HashMap<String,String> noclockingMap = new HashMap<String,String>();
					noclockingMap.put("empId", onebankAssocObj.getEmployee_id());
					noclockingMap.put("empName", onebankAssocObj.getEmployee_name());
					noclockingMap.put("onebankid", onebankAssocObj.getOnebankId());
					noClockingEmpIdList.add(noclockingMap);
					String to = null;
					if(onebankAssocObj.getClient_email_id() !=null && !"".equalsIgnoreCase(onebankAssocObj.getClient_email_id())){
					if (onebankAssocObj.getClient_email_id().contains("@dbs.com")) {
						to = onebankAssocObj.getClient_email_id();
					} else {
						to = onebankAssocObj.getPersonal_email_id();
					}
					noClockingBCCList.add(to);
					}
				}
			}
			ArrayList<HashMap<String,String>> empNocliemails = new ArrayList<HashMap<String,String>>();
			for(Map.Entry<String,String> entry : noCliEmailID.entrySet()){
				HashMap<String,String> nocliemailMap = new HashMap<String,String>();
				nocliemailMap.put("empId", entry.getKey());
				nocliemailMap.put("empName", entry.getValue());
				empNocliemails.add(nocliemailMap);
			}
			HashMap<String,Object> resultobj = new HashMap<String,Object>();
			resultobj.put("correctClocking",correctClocking);
			resultobj.put("inCorrectClocking",inCorrectClocking);
			resultobj.put("noClocking",noClockingEmpIdList);
			resultobj.put("newClocking",newClocking);
			resultobj.put("noCliEmailID",empNocliemails);		
			resultobj.put("unprocessedOneBankId",issueinprocessingOneBnankId);	
			result=om.writeValueAsString(resultobj);
			transaction.commit();
			try{
				if(fileURL != null){
					File fileDir = new File(tempfilelocation);
					if (!fileDir.exists()) {
						fileDir.mkdirs();
					}
					fileRts.transferTo(new File(new File(tempfilelocation), fileURL));
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			if(jsonData.get("sendEmail").toString() != null && "yes".equalsIgnoreCase(jsonData.get("sendEmail").toString())){		
				for(HashMap<String, Object> obj : inCorrectClocking){
					if(onebankEmpAssocDetailsMap.containsKey(obj.get("onebankid"))){
					OnebankEmployeeDetailsAssoc onebankAssocObj =  onebankEmpAssocDetailsMap.get(obj.get("onebankid"));
					String to = null;
					String[] cc = null;
					String bcc[] = null;
					String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
					if(onebankAssocObj.getClient_email_id().contains("@dbs.com")){
						to = onebankAssocObj.getClient_email_id();
					}else{
						to = onebankAssocObj.getPersonal_email_id();
					}
					String subject =" Timesheet Missed Clocking ";
					StringBuffer message = new StringBuffer();
					message.append("Dear "+onebankAssocObj.getEmployee_name()+","+ "\n\n");			
					message.append("Your RTS timesheet for the month of "+timesheetMonth+" does not have the required number of hours. "+"\n\n");
					message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
					try{
					emailService.sendBulkEmail(to,cc,bcc,subject,message.toString());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				}
				ArrayList<String> correctClockingBCCList = new ArrayList<String>();
				for (HashMap<String, Object> obj : correctClocking) {
					if (onebankEmpAssocDetailsMap.containsKey(obj.get("onebankid"))) {
						OnebankEmployeeDetailsAssoc onebankAssocObj = onebankEmpAssocDetailsMap.get(obj.get("onebankid"));
						String to = null;
						if(onebankAssocObj.getClient_email_id() !=null && !"".equalsIgnoreCase(onebankAssocObj.getClient_email_id())){
						if (onebankAssocObj.getClient_email_id().contains("@dbs.com")) {
							to = onebankAssocObj.getClient_email_id();
						} else {
							to = onebankAssocObj.getPersonal_email_id();
						}
						correctClockingBCCList.add(to);
						}
					}
				}
				if(correctClockingBCCList.size()>0){
					String correctclockedTo = Utils.getHapProperty("RTSTimeSheetNotification-TO");;
					String[] correctclockedcc = null;
					String[] correctclockedbcc = null;
					String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
					if (getCC != null && !getCC.isEmpty()) {
						correctclockedcc = getCC.split(",");
					}
					correctclockedbcc = correctClockingBCCList.toArray(new String[correctClockingBCCList.size()]);
					String subject =" Timesheet Submitted ";
					StringBuffer message = new StringBuffer();
					message.append("Hello,"+ "\n\n");			
					message.append("Thanks for submitting the RTS timesheet. "+"\n\n");
					message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
					try{
					emailService.sendBulkEmail(correctclockedTo,correctclockedcc,correctclockedbcc,subject,message.toString());
					}catch(Exception e){
						e.printStackTrace();	
					}
				}
				if(noClockingBCCList.size()>0){
					String noclockedTo = Utils.getHapProperty("RTSTimeSheetNotification-TO");;
					String[] noclockedcc = null;
					String[] noclockedbcc = null;
					String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
					if (getCC != null && !getCC.isEmpty()) {
						noclockedcc = getCC.split(",");
					}
					noclockedbcc = noClockingBCCList.toArray(new String[noClockingBCCList.size()]);
					String subject =" Timesheet Reminder ";
					StringBuffer message = new StringBuffer();
					message.append("Dear,"+ "\n\n");			
					message.append("You have not yet submitted the RTS timesheet for the month of "+timesheetMonth+". Please do so at the earliest."+"\n\n");
					message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
					try{
					emailService.sendBulkEmail(noclockedTo,noclockedcc,noclockedbcc,subject,message.toString());
					}catch(Exception e){
						e.printStackTrace();	
					}
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to process RTS Timesheet " + e.getMessage());
		}finally{
			session.close();
		}
		return result;
	}
	
	
	/*public void saveTimeSheetStatusold(@RequestParam("model") String jsondata, MultipartHttpServletRequest request)
			throws Throwable {
		Session session = null;
		Transaction transaction = null;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String pdfFilePath = null;
		String tempfilelocation = null;
		try {
			ObjectMapper om = new ObjectMapper();
			Employee_Timesheet_Status empTimesheetStatus = null;
			empTimesheetStatus = om.readValue(jsondata, Employee_Timesheet_Status.class);
			if (empTimesheetStatus != null) {
				Timestamp timesheetMonth = empTimesheetStatus.getTimesheetMonth();
				timesheetMonth.setMinutes(0);
				timesheetMonth.setHours(0);
				timesheetMonth.setSeconds(0);
				timesheetMonth.setDate(1);
				empTimesheetStatus.setTimesheetMonth(timesheetMonth);
				java.sql.Date date = new java.sql.Date(empTimesheetStatus.getTimesheetMonth().getTime());
				LocalDate month = date.toLocalDate();
				Map<String, List<MultipartFile>> allfiles = request.getMultiFileMap();
				if (allfiles.size() > 0) {
					HashMap<String, ArrayList<String>> conFileLists = new HashMap<String, ArrayList<String>>();
					tempfilelocation = Utils.getProperty("fileLocation") + File.separator + "tempTimesheetPDFFolder";
					File fileDir = new File(tempfilelocation);
					if (!fileDir.exists()) {
						boolean iscreated = fileDir.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory");
						}
					}
					for (String key : allfiles.keySet()) {
						List<MultipartFile> ff = allfiles.get(key);
						ArrayList<String> al = new ArrayList<String>();
						for (MultipartFile files1 : ff) {
							String extension = FilenameUtils.getExtension(files1.getOriginalFilename());
							if (extension.equalsIgnoreCase("docx")) {
								try {
									XWPFDocument document = new XWPFDocument(files1.getInputStream());
									PdfOptions options = PdfOptions.create();
									File filePath = File.createTempFile(
											empTimesheetStatus.getEmployeeName() + "_" + files1.getName(),
											FilenameUtils.getBaseName(files1.getOriginalFilename()) + ".pdf", fileDir);
									OutputStream out = new FileOutputStream(filePath);
									PdfConverter.getInstance().convert(document, out, options);
									al.add(filePath.getAbsolutePath());
									out.close();
									document.close();
								} catch (Exception e) {
									String type = "";
									e.printStackTrace();
									if ("timesheet".equalsIgnoreCase(key)) {
										type = "Timesheet";
									} else if ("supportingdoc".equalsIgnoreCase(key)) {
										type = "Supporting Document";
									}
									throw new Throwable(
											"Failed to save timesheet status - issue in saving " + type + " !!");
								}
							}
							else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")
									|| extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("bmp")) {
								try {
									File fi = File.createTempFile("tempimage", files1.getOriginalFilename(), fileDir);
									files1.transferTo(fi);
									PDDocument document = new PDDocument();
									String imagePath = fi.getPath();
									BufferedImage bimg = ImageIO.read(fi);
									float width = bimg.getWidth();
									float height = bimg.getHeight();
									PDPage page = new PDPage(new PDRectangle(width, height));
									document.addPage(page);
									PDImageXObject img = PDImageXObject.createFromFile(imagePath, document);
									PDPageContentStream contentStream = new PDPageContentStream(document, page);
									contentStream.drawImage(img, 0, 0);
									contentStream.close();
									File filePath = File.createTempFile(
											empTimesheetStatus.getEmployeeName() + "_" + files1.getName(),
											FilenameUtils.getBaseName(files1.getOriginalFilename()) + ".pdf", fileDir);
									document.save(filePath);
									al.add(filePath.getAbsolutePath());
									document.close();
								} catch (Exception e) {
									String type = "";
									e.printStackTrace();
									if ("timesheet".equalsIgnoreCase(key)) {
										type = "Timesheet";
									} else if ("supportingdoc".equalsIgnoreCase(key)) {
										type = "Supporting Document";
									}
									throw new Throwable(
											"Failed to save timesheet status - issue in saving " + type + " !!");
								}
							} else if (extension.equalsIgnoreCase("pdf")) {
								try {
									File filePath = File.createTempFile(
											empTimesheetStatus.getEmployeeName() + "_" + files1.getName(),
											FilenameUtils.getBaseName(files1.getOriginalFilename()) + ".pdf", fileDir);
									files1.transferTo(filePath);
									al.add(filePath.getPath());
								} catch (Exception e) {
									String type = "";
									e.printStackTrace();
									if ("timesheet".equalsIgnoreCase(key)) {
										type = "Timesheet";
									} else if ("supportingdoc".equalsIgnoreCase(key)) {
										type = "Supporting Document";
									}
									throw new Throwable(
											"Failed to save timesheet status - issue in saving " + type + " !!");
								}
							} else {
								throw new Throwable(
										"Failed to save timesheet status please check file format one of the file is not supported !!");
							}
						}
						conFileLists.put(key, al);
					}
					if (empTimesheetStatus.getManagerApprovalDocumentPath() != null
							&& !"".equalsIgnoreCase(empTimesheetStatus.getManagerApprovalDocumentPath())) {
						PDDocument doc = new PDDocument();
						try {
							PDPage page = new PDPage();
							doc.addPage(page);
							PDPageContentStream contentStream = new PDPageContentStream(doc, page);
							// Begin the Content stream
							contentStream.beginText();
							// Setting the font to the Content stream
							contentStream.setFont(PDType1Font.TIMES_ROMAN, 11);
							// Setting the position for the line
							contentStream.setLeading(14.5f);
							contentStream.newLineAtOffset(25, 725);
							String text = new String(
									empTimesheetStatus.getManagerApprovalDocumentPath().getBytes("ISO8859_1"), "UTF8");
							// Adding text in the form of string
							StringBuilder b = new StringBuilder();
							for (int i = 0; i < text.length(); i++) {
								if (WinAnsiEncoding.INSTANCE.contains(text.charAt(i))) {
									if (b.length() == 128) {
										contentStream.showText(b.toString());
										contentStream.newLine();
										b.setLength(0);
									}
									b.append(text.charAt(i));
								} else {
									contentStream.showText(b.toString());
									b.setLength(0);
									char t = text.charAt(i);
									String character = String.valueOf(t);
									if (character.equalsIgnoreCase("\n")) {
										contentStream.newLine();
									} else if (character.equalsIgnoreCase("\t")) {
										b.append(" ");
										contentStream.showText(b.toString());
									} else {
										b.setLength(0);
									}
									b.setLength(0);
								}
							}
							String text1 = b.toString();
							contentStream.showText(text1);
							// Ending the content stream
							contentStream.endText();
							// Closing the content stream
							contentStream.close();
							// Saving the document
							File filePath = File.createTempFile(empTimesheetStatus.getEmployeeName(),
									"manager_approval" + ".pdf", fileDir);
							doc.save(filePath);
							// Closing the document
							doc.close();
							ArrayList<String> mngrApprovTempFile = new ArrayList<String>();
							mngrApprovTempFile.add(filePath.getPath());
							conFileLists.put("mgrapp", mngrApprovTempFile);	 
						} catch (Exception e) {
							e.printStackTrace();
							doc.close();
							throw new Throwable(
									"Failed to save timesheet status - issue in saving manager approval !!");
						}
					}
					PDFMergerUtility PDFmerger = new PDFMergerUtility();
					File pdffileDir = new File(Utils.getProperty("fileLocation") + File.separator + "timesheetpdf");
					if (!pdffileDir.exists()) {
						boolean iscreated = pdffileDir.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory ");
						}
					}
					File timesheetPDFLoc = new File(
							pdffileDir + File.separator + month.getMonth() + " " + month.getYear());
					if (!timesheetPDFLoc.exists()) {
						boolean iscreated = timesheetPDFLoc.mkdirs();
						if (!iscreated) {
							throw new Exception("Failed to create Directory");
						}
					}
					pdfFilePath = timesheetPDFLoc + File.separator + empTimesheetStatus.getEmployeeName()
							+ "_Time Sheet_" + month.getMonth() + " " + month.getYear() + ".pdf";
					PDFmerger.setDestinationFileName(pdfFilePath);
					if (conFileLists.containsKey("timesheet")) {
						ArrayList<String> timesheetUrl = conFileLists.get("timesheet");
						for (String url : timesheetUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					if (conFileLists.containsKey("mgrapp")) {
						ArrayList<String> mgrApprovUrl = conFileLists.get("mgrapp");
						for (String url : mgrApprovUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					if (conFileLists.containsKey("supportingdoc")) {
						ArrayList<String> supportingDocUrl = conFileLists.get("supportingdoc");
						for (String url : supportingDocUrl) {
							File conFile = new File(url);
							PDFmerger.addSource(conFile);
						}
					}
					// setting null as argument in pdf merger to use default
					// memorysetting
					PDFmerger.mergeDocuments(null);
					if (tempfilelocation != null && !"".equalsIgnoreCase(tempfilelocation)) {
						File tempPdf = new File(tempfilelocation);
						if (tempPdf.exists()) {
							FileUtils.deleteDirectory(tempPdf);
						}
					}
					empTimesheetStatus.setTimesheetUploadPath(pdfFilePath);
				}
				empTimesheetStatus.setManagerApprovalDocumentPath(null);
				session.save(empTimesheetStatus);
				transaction.commit();
				try{
					if("none".equalsIgnoreCase(empTimesheetStatus.getTimesheetError())){
					String subject = "Timesheet Status";
					String to = empTimesheetStatus.getTimesheetEmail();
					String message = "Dear " + empTimesheetStatus.getEmployeeName()+ ","
							+ "\n\n" + "Thanks for submitting your timesheet and it is error free. We will process your timesheet and get back to you, if necessary." + "\n\n"
							+"\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd";
					emailService.sendSimpleMessage(to, subject, message);
					}
					}catch(Exception e){
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (tempfilelocation != null && !"".equalsIgnoreCase(tempfilelocation)) {
				File tempPdf = new File(tempfilelocation);
				if (tempPdf.exists()) {
					FileUtils.deleteDirectory(tempPdf);
				}
			}
			if (pdfFilePath != null && !"".equalsIgnoreCase(pdfFilePath)) {
				File file = new File(pdfFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
			throw new Throwable("Failed to save timesheet status !!");
		} finally {
			session.close();
		}
	}
	*/
	public String saveBeelineTimesheetSummaryFile(String json,MultipartHttpServletRequest request) throws Throwable{
		String result = null;
		try{
		 result = beelineService.saveBeelineTimesheetSummaryFile(json,request);
		}catch(Exception e){
			e.printStackTrace();
		throw new Throwable("Failed to run beeline summary service " + e.getMessage());
		}
		return result;
	}
	
	public String saveBeelineTimesheetDetailsFiles(String json,MultipartHttpServletRequest request) throws Throwable{
		String result = null;
		try{
		 result = beelineService.saveBeelineTimesheetDetailsFiles(json,request);
		}catch(Exception e){
			e.printStackTrace();
		throw new Throwable("Failed to run beeline summary service " + e.getMessage());
		}
		return result;
	}
	@Override
	public void saveBeelineTimeSheet(String jsondata, MultipartHttpServletRequest request) throws Throwable {
		Session session = null;
		Transaction transaction = null;
		String tempfilelocation = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> fileFolder = new HashMap<String, String>();
		Employee_Beeline_Timesheet beeline = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ObjectMapper om = new ObjectMapper();
			beeline = om.readValue(jsondata, Employee_Beeline_Timesheet.class);
			if (beeline != null) {
				java.util.Date date = sdfMonth.parse(beeline.getTimesheet_month().toString());
				Timestamp tsMonth = new Timestamp(date.getTime());
				LocalDateTime month = tsMonth.toLocalDateTime();
				if("Approved".equalsIgnoreCase(beeline.getTimesheet_status())){
					beeline.setApproved_email_send(true);
				}
				if (beeline.getTimesheet_document_path() != null && !beeline.getTimesheet_document_path().isEmpty()) {
					String extension = FilenameUtils.getExtension(beeline.getTimesheet_document_path());
					String modifiedFileName = beeline.getEmployee_name()+"_TimeSheet_"+month.getMonth()+" " + month.getYear() +"."+extension;
					templateFilenames.put(beeline.getTimesheet_document_path(), modifiedFileName);
					fileFolder.put(beeline.getTimesheet_document_path(), "beeline_files"+File.separator+"timesheet"+File.separator+month.getMonth()+" "+month.getYear());
					beeline.setTimesheet_document_path(modifiedFileName);
				}
				if (beeline.getSupporting_document_path() != null && !beeline.getSupporting_document_path().isEmpty()) {
					String extension = FilenameUtils.getExtension(beeline.getSupporting_document_path());
					String modifiedFileName = beeline.getEmployee_name()+"_supporting_"+month.getMonth()+" " + month.getYear() +"."+extension;
					templateFilenames.put(beeline.getSupporting_document_path(), modifiedFileName);
					fileFolder.put(beeline.getSupporting_document_path(), "beeline_files"+File.separator+"supporting"+File.separator+month.getMonth()+" "+month.getYear());
					beeline.setSupporting_document_path(modifiedFileName);
				}
				beeline.setTimesheet_month(tsMonth);
				session.save(beeline);
			}
			Map<String, MultipartFile> files = null;
			files = request.getFileMap();
			if (files.size() > 0) {
				FilecopyStatus status = Utils.copyFiles(request, templateFilenames, fileFolder);
				copied_with_success = status.getCopied_with_success();
			}
			transaction.commit();
			try{
				if("Approved".equalsIgnoreCase(beeline.getTimesheet_status())){
				String subject = "Timesheet Status – Approved";
				Employee employee = employeeDAO.get(beeline.getEmployee_id());
				String to = employee.getEmployeeAssignmentDetails().getClient_email_id();
				String[] cc = null;
				String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC")+","+
						Utils.getHapProperty("RTSTimeSheetNotification-TO")+""+employee.getEmployeeOfferDetails().getPersonal_email_id();
				if (getCC != null && !getCC.isEmpty()) {
					cc = getCC.split(",");
				}
				String[] bcc = null;
				String message = "Dear " + beeline.getEmployee_name()+ ","
						+ "Thanks for submitting your "+beeline.getTimesheet_month().toLocalDateTime().getMonth() +" "+beeline.getTimesheet_month().toLocalDateTime().getYear()+" timesheet on Beeline and it is approved. "
						+ "We will process your timesheet and get back to you, if necessary."
						+"\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd";
				emailService.sendEmail(to, cc, bcc, subject, message.toString());
				}
				}catch(Exception e){
					e.printStackTrace();
				}
		}catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to save beeline timesheet" + e.getMessage());
		}finally{
			session.close();
		}
	}

	@Override
	public void updateBeelineTimeSheet(String jsondata, MultipartHttpServletRequest request,String sendEmail)
			throws Throwable {
		Session session = null;
		Transaction transaction = null;
		String tempfilelocation = null;
		SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
		Map<String, String> templateFilenames = new HashMap<String, String>();
		Map<String, String> fileFolder = new HashMap<String, String>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			ObjectMapper om = new ObjectMapper();
			Employee_Beeline_Timesheet beeline = null;
			beeline = om.readValue(jsondata, Employee_Beeline_Timesheet.class);
			if (beeline != null) {
				if("yes".equalsIgnoreCase(sendEmail) && "Approved".equalsIgnoreCase(beeline.getTimesheet_status())){
					beeline.setApproved_email_send(true);
				}
				java.util.Date date = sdfMonth.parse(beeline.getTimesheet_month().toString());
				Timestamp tsMonth = new Timestamp(date.getTime());
				LocalDateTime month = tsMonth.toLocalDateTime();
				if (beeline.getTimesheet_document_path() != null && !beeline.getTimesheet_document_path().isEmpty()) {
					String extension = FilenameUtils.getExtension(beeline.getTimesheet_document_path());
					String modifiedFileName = beeline.getEmployee_name()+"_TimeSheet_"+month.getMonth()+" " + month.getYear() +"."+extension;
					templateFilenames.put(beeline.getTimesheet_document_path(), modifiedFileName);
					fileFolder.put(beeline.getTimesheet_document_path(), "beeline_files"+File.separator+"timesheet"+File.separator+month.getMonth()+" "+month.getYear());
					beeline.setTimesheet_document_path(modifiedFileName);
				}
				if (beeline.getSupporting_document_path() != null && !beeline.getSupporting_document_path().isEmpty()) {
					String extension = FilenameUtils.getExtension(beeline.getSupporting_document_path());
					String modifiedFileName = beeline.getEmployee_name()+"_supporting_"+month.getMonth()+" " + month.getYear() +"."+extension;
					templateFilenames.put(beeline.getSupporting_document_path(), modifiedFileName);
					fileFolder.put(beeline.getSupporting_document_path(), "beeline_files"+File.separator+"supporting"+File.separator+month.getMonth()+" "+month.getYear());
					beeline.setSupporting_document_path(modifiedFileName);
				}
				beeline.setTimesheet_month(tsMonth);
				session.update(beeline);
			}
			Map<String, MultipartFile> files = null;
			files = request.getFileMap();
			if (files.size() > 0) {
				FilecopyStatus status = Utils.copyFiles(request, templateFilenames, fileFolder);
				copied_with_success = status.getCopied_with_success();
			}
			transaction.commit();
				try{
					if("yes".equalsIgnoreCase(sendEmail) && "Approved".equalsIgnoreCase(beeline.getTimesheet_status())){
					String subject = "Timesheet Status – Approved";
					Employee employee = employeeDAO.get(beeline.getEmployee_id());
					String to = employee.getEmployeeAssignmentDetails().getClient_email_id();
					String[] cc = null;
					String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC")+","+
							Utils.getHapProperty("RTSTimeSheetNotification-TO")+","+employee.getEmployeeOfferDetails().getPersonal_email_id();
					if (getCC != null && !getCC.isEmpty()) {
						cc = getCC.split(",");
					}
					String[] bcc = null;
					String message = "Dear " + beeline.getEmployee_name()+ ","
							+ "Thanks for submitting your "+beeline.getTimesheet_month().toLocalDateTime().getMonth() +" "+beeline.getTimesheet_month().toLocalDateTime().getYear()+" timesheet on Beeline and it is approved. "
							+ "We will process your timesheet and get back to you, if necessary."
							+"\n\n" + "Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd";
					emailService.sendEmail(to, cc, bcc, subject, message.toString());
					}
					}catch(Exception e){
						e.printStackTrace();
					}
		}catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to Update beeline timesheet" + e.getMessage());
		}finally{
			session.close();
		}		
	}

	@Override
	public Employee_Beeline_Timesheet getBeelineTimesheet(String employeeId, String timesheetMonth) throws Throwable {
		Employee_Beeline_Timesheet empTimesheetStatus = null;
			Session session = null;
			ResponseEntity<String> timesheet = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Timestamp timesheetMonths = null;
			java.util.Date selectedDate = sdf.parse(timesheetMonth);
			timesheetMonths = new Timestamp(selectedDate.getTime());
			try {
				session = sessionFactory.openSession();
				String query = "select * from Employee_Beeline_Timesheet where timesheet_month = :timesheet_month AND employee_id = :employee_id";
				List<Employee_Beeline_Timesheet> tslist = session.createSQLQuery(query)
						.addEntity(Employee_Beeline_Timesheet.class).setParameter("timesheet_month", timesheetMonths)
						.setParameter("employee_id", employeeId).list();
				if (!tslist.isEmpty() && tslist != null) {
					empTimesheetStatus = (Employee_Beeline_Timesheet) tslist.iterator().next();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Throwable("Failed to fetch Beeline Timesheet Details");
			} finally {
				session.close();
			}
			return empTimesheetStatus;
	}

	@Override
	public List<Employee_Beeline_Timesheet> getAllBeelineTimesheet(String timesheetMonth) throws Throwable {
		List<Employee_Beeline_Timesheet> empTimesheetStatus = null;
		Session session = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		java.util.Date selectedDate = sdf.parse(timesheetMonth);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		try {
			session = sessionFactory.openSession();
			String query = "select * from Employee_Beeline_Timesheet where timesheet_month = :timesheet_month ";
			empTimesheetStatus = session.createSQLQuery(query)
					.addEntity(Employee_Beeline_Timesheet.class).setParameter("timesheet_month", timesheetMonths).list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to fetch Beeline Timesheet Details");
		} finally {
			session.close();
		}
		return empTimesheetStatus;	
	}

	@Override
	public List<BeelineTimesheetDashboard> getBeelineDashboard(String month) throws Throwable {
		Session session = null;
		List<BeelineTimesheetDashboard> results = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp timesheetMonths = null;
		java.util.Date selectedDate = sdf.parse(month);
		timesheetMonths = new Timestamp(selectedDate.getTime());
		try {
			session = sessionFactory.openSession();
			String query = "SELECT * from Employee_Beeline_Timesheet_Dashboard where timesheet_month = :timesheet_month";
			Query listquery = session.createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(BeelineTimesheetDashboard.class)).setParameter("timesheet_month",timesheetMonths);
			results = listquery.list();
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			throw new Throwable("Failed to fetch  Beeline dashboard Details ");
		}
		return results;
	}
	
	public Employee_Beeline_Timesheet getBeelineTimesheetById(String timesheetId) {
		Employee_Beeline_Timesheet empTimesheetStatus = null;
		Session session = null;
		session = sessionFactory.openSession();
		try {
			String query = "select * from Employee_Beeline_Timesheet where employee_beeline_timesheet_id = :employee_beeline_timesheet_id";
			List<Employee_Beeline_Timesheet> tslist = session.createSQLQuery(query)
					.addEntity(Employee_Beeline_Timesheet.class)
					.setParameter("employee_beeline_timesheet_id", timesheetId).list();
			if (!tslist.isEmpty() && tslist != null) {
				empTimesheetStatus = (Employee_Beeline_Timesheet) tslist.iterator().next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return empTimesheetStatus;
	}

		@Override
		public ResponseEntity<byte[]> getBeelineTimesheetFile(String timesheetId,String fileType) {
			byte[] files = null;
			FileInputStream fi = null;
			try {
				Employee_Beeline_Timesheet timesheet = getBeelineTimesheetById(timesheetId);
				LocalDateTime month = timesheet.getTimesheet_month().toLocalDateTime();
				String path = null;
				if("timesheet".equalsIgnoreCase(fileType)){
					path = timesheet.getTimesheet_document_path();
				}else{
					path = timesheet.getSupporting_document_path();
				}
				String fileUrl = Utils.getProperty("fileLocation")+ File.separator +"beeline_files"+File.separator
						+fileType+File.separator+month.getMonth()+" "+month.getYear()+File.separator+path;
				File file = new File(fileUrl);
				if (file.exists()) {
					fi = new FileInputStream(fileUrl);
					files = IOUtils.toByteArray(fi);
					fi.close();
				} else {
					return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(files, HttpStatus.OK);
			return responseEntity;
		}
	@Override
	public String beelineBulkEmailService(String month,String statusType) throws Throwable{
		String result = null;
		try{
		result =  beelineService.beelineBulkEmailService(month,statusType);
		}catch(Exception e){
			e.printStackTrace();
			throw new Throwable("Failed to run  Beeline email service ");
		}
		return result;
	}

	//------------------------------------------------------------
		//--------------------------------------------------------
	
	
	
	public static  String[] beelinenameIndex(Sheet sheet){
		int rowindex = 0;
		int cellindex = 0;
    	for(Row row : sheet){
    		for(Cell cell : row){
    			if(cell.toString().startsWith("Contractor: ")){
    				rowindex= cell.getRowIndex();
    				cellindex = cell.getColumnIndex();
    			}
    		}
    	} 
    	String[] str = {String.valueOf(rowindex),String.valueOf(cellindex)};
    	return str;
	}
	/*public static void main(String[] args) throws IOException {
        FileInputStream file  = null;
        try {
            file = new FileInputStream("C:/Users/HELIUS/Documents/Timesheet_Details.xlsx");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            org.apache.commons.io.IOUtils.copy(file, baos);
            byte[] bytes = baos.toByteArray();
            XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(bytes));
           
            FileOutputStream out = null;
           
            XSSFWorkbook[] workbooks = new XSSFWorkbook[wb.getNumberOfSheets()];
            //file.close();
           Sheet sh = wb.getSheetAt(0);
          String[] beelinenameindex = beelinenameIndex(sh);
          int rowIndexName = 0;
          if(beelinenameindex[0] != null && !"".equalsIgnoreCase(beelinenameindex[0])){
        	  rowIndexName = Integer.parseInt(beelinenameindex[0]);
          }
          int cellNameIndex = 0;
          if(beelinenameindex[1] != null && !"".equalsIgnoreCase(beelinenameindex[1])){
        	  cellNameIndex = Integer.parseInt(beelinenameindex[1]);
          }
            for(int i=0 ; i < wb.getNumberOfSheets(); i++){
                //FileInputStream file1 = new FileInputStream("C:/Users/HELIUS/Documents/SOW/beeline/Book2.xlsx");
                XSSFWorkbook wb1 = new XSSFWorkbook(new ByteArrayInputStream(bytes));
            	workbooks[i] = wb1;
                int k = i + 1; 
                String beeline_name = null;
                if(wb1.getSheetAt(i).getRow(rowIndexName).getCell(cellNameIndex) != null){
                beeline_name =  wb1.getSheetAt(i).getRow(rowIndexName).getCell(cellNameIndex).getStringCellValue();
                }
               String workboonName = "";
               if(beeline_name != null && beeline_name.startsWith("Contractor: ")){
           	   	int collonindex = beeline_name.indexOf(":");
            	   	workboonName = beeline_name.substring(collonindex+1).replace(","," ").trim();  
            	   	workboonName = workboonName+"_TimeSheet_";
       				}else{
       				String[] str =	beelinenameIndex(wb1.getSheetAt(i));
       				beeline_name = wb1.getSheetAt(i).getRow(Integer.parseInt(str[0])).getCell(Integer.parseInt(str[1])).toString();
            	   	int collonindex = beeline_name.indexOf(":");
       				workboonName = beeline_name.substring(collonindex+1).replace(","," ").trim();  
            	   	workboonName = workboonName+"_TimeSheet_";
       				}
                String sheetName = wb1.getSheetName(i);
                for(int j=workbooks[i].getNumberOfSheets()-1; j>=0; j--){
                    XSSFSheet tmpSheet =workbooks[i].getSheetAt(j);
                    if(!tmpSheet.getSheetName().equals(sheetName)){
                        workbooks[i].removeSheetAt(j);
                    }
                }
                String outputfilename = "C:/Users/HELIUS/Documents/SOW/beeline/" + workboonName + ".xlsx";
                out = new FileOutputStream(outputfilename);
              //  workbooks[i].write(out);
                workbooks[i].close();
                out.close();
                //file1.close();
                System.out.println("created " + sheetName);
            }
            //    System.out.println("========size======="+workbookinput.getNumberOfSheets());
              //  System.out.println("========size====2==="+workbookinput.getNumberOfSheets());
               
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            file.close();
        }
    }*/
	public static void main(String[] args) throws Exception {
		String appUrl = "https://hap.heliusapp.com/Employee_selfcare/changepwd.html";
			/*file = new FileInputStream("C:/Users/HELIUS/Documents/SOW/beeline/Book1.xlsx");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(file, baos);
			byte[] bytes = baos.toByteArray();
			XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(bytes));
			
			FileOutputStream out = null;
			
			XSSFWorkbook[] workbooks = new XSSFWorkbook[wb.getNumberOfSheets()];
			//file.close();
			
			for(int i=0 ; i < wb.getNumberOfSheets(); i++){
				//FileInputStream file1 = new FileInputStream("C:/Users/HELIUS/Documents/SOW/beeline/Book2.xlsx");
				XSSFWorkbook wb1 = new XSSFWorkbook(new ByteArrayInputStream(bytes));
				workbooks[i] = wb1;
				int k = i + 1;
				String sheetName = "Sheet" + k;
				for(int j=workbooks[i].getNumberOfSheets()-1; j>=0; j--){
					XSSFSheet tmpSheet =workbooks[i].getSheetAt(j);
	                if(!tmpSheet.getSheetName().equals(sheetName)){
	                	workbooks[i].removeSheetAt(j);
	                }
				}
				String outputfilename = "C:/Users/HELIUS/Documents/SOW/beeline/" + sheetName + ".xlsx";
				out = new FileOutputStream(outputfilename);
				workbooks[i].write(out);
				workbooks[i].close();
				out.close();
				//file1.close();
				System.out.println("created " + sheetName);
	        } */
		/*finally {
			file.close();
		}*/
	}
	public void process(String fileName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(bis);
        XSSFWorkbook myWorkBook = new XSSFWorkbook();
        XSSFSheet sheet = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFSheet mySheet = null;
        XSSFRow myRow = null;
        XSSFCell myCell = null;
        XSSFCellStyle dd= null;
        int sheets = workbook.getNumberOfSheets();
        int fCell = 0;
        int lCell = 0;
        int fRow = 0;
        int lRow = 0;
        for (int iSheet = 0; iSheet < sheets; iSheet++) {
            sheet = workbook.getSheetAt(iSheet);
          
            
            if (sheet != null) {
                mySheet = myWorkBook.createSheet(sheet.getSheetName());
                mySheet.setFitToPage(true);
                PrintSetup ps = mySheet.getPrintSetup();
                ps.setFitWidth( (short) 1);
				ps.setFitHeight( (short) 0);
                ListIterator<CellRangeAddress>  src_MergedRegions_iter = sheet.getMergedRegions().listIterator();
             
                while(src_MergedRegions_iter.hasNext()) {
                    CellRangeAddress cr = src_MergedRegions_iter.next();
                    mySheet.addMergedRegion(cr);
                    
                }
                fRow = sheet.getFirstRowNum();
                lRow = sheet.getLastRowNum();
                for (int iRow = fRow; iRow <= lRow; iRow++) {
                    row = sheet.getRow(iRow);
                    myRow = mySheet.createRow(iRow);
                    
                    if (row != null) {
                        fCell = row.getFirstCellNum();
                        lCell = row.getLastCellNum();
                        for (int iCell = fCell; iCell < lCell; iCell++) {
                            cell = row.getCell(iCell);
                            myCell = myRow.createCell(iCell);
                            if (cell != null) {
                                myCell.setCellType(cell.getCellType());
                               
                                switch (cell.getCellType()) {
                                case XSSFCell.CELL_TYPE_BLANK:
                                    myCell.setCellValue("");
                                    break;

 

                                case XSSFCell.CELL_TYPE_BOOLEAN:
                                    myCell.setCellValue(cell.getBooleanCellValue());
                                    break;

 

                                case XSSFCell.CELL_TYPE_ERROR:
                                    myCell.setCellErrorValue(cell.getErrorCellValue());
                                    break;

 

                                case XSSFCell.CELL_TYPE_FORMULA:
                                    myCell.setCellFormula(cell.getCellFormula());
                                    break;

 

                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    myCell.setCellValue(cell.getNumericCellValue());
                                    break;

 

                                case XSSFCell.CELL_TYPE_STRING:
                                    myCell.setCellValue(cell.getStringCellValue());
                                    break;
                                default:
                                    myCell.setCellFormula(cell.getCellFormula());
                                }
                            }
                        }
                    }
                }
                
               
            }
                
                 
                 Drawing draw = sheet.createDrawingPatriarch();
                 //List<Picture> pics = new ArrayList<Picture>();
                 XSSFPicture picture = null;
                 XSSFDrawing xdraw = (XSSFDrawing)draw;
                 for (XSSFShape xs : xdraw.getShapes()) {
                     if (xs instanceof Picture) {
                         
                           picture = (XSSFPicture)xs;
                           break;
                     }
                 }
             
                 PictureData pd = picture.getPictureData();
                 byte[] saveme = pd.getData();

 

                 /* Read the input image into InputStream */
                // InputStream my_banner_image = new FileInputStream("C:/Users/HELIUS/Documents/SOW/beeline/dbs.png");
                /* Convert Image to byte array */
               // byte[] bytes = IOUtils.toByteArray(my_banner_image);
                
                /* Add Picture to workbook and get a index for the picture */
                int my_picture_id = myWorkBook.addPicture(saveme, Workbook.PICTURE_TYPE_PNG);
                /* Close Input Stream */
                //my_banner_image.close();                
                //Returns an object that handles instantiating concrete classes
                CreationHelper helper = myWorkBook.getCreationHelper();
                //Creates the top-level drawing patriarch.
                Drawing drawing = mySheet.createDrawingPatriarch();
                
                //Create an anchor that is attached to the worksheet
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(1);
                anchor.setRow1(2);
                anchor.setCol2(4);
                anchor.setRow2(3);     
                //Creates a picture
                Picture pict = drawing.createPicture(anchor, my_picture_id);
                //Reset the image to the original size
                 pict.resize();
              // Resize all columns to fit the content size
                 /*for (int iRow = fRow; iRow <= lRow; iRow++) {
                     row = sheet.getRow(iRow);
                     if (row != null) {
                         fCell = row.getFirstCellNum();
                         lCell = row.getLastCellNum();
                         for (int i = 0; i < lCell;  i++) {
                             mySheet.autoSizeColumn(i);
                         }
                     }
                 }*/
        bis.close();
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream("C:/Users/HELIUS/Documents/SOW/beeline/123_copy1.xlsx"));
        myWorkBook.write(bos);
        bos.close();
        }
    }
	/*public static void main1(String[] args) throws IOException {
       // TimeSheetAndLeaveServiceImpl excel = new TimeSheetAndLeaveServiceImpl();
       // excel.process("C:/Users/HELIUS/Documents/Timesheet_Details.xlsx");
		//--------
		
		FileInputStream file = new FileInputStream("C:/Users/HELIUS/Documents/Timesheet_Details.xlsx");
		XSSFWorkbook wb1 = new XSSFWorkbook(file);
		XSSFSheet sheet1 = wb1.getSheetAt(0);
		XSSFWorkbook wb2 = new XSSFWorkbook();
		XSSFSheet sheet2 = wb2.createSheet("vinay");
		Util utl = new Util();
		utl.copySheets(sheet2, sheet1);
		FileOutputStream out = new FileOutputStream("C:/Users/HELIUS/Documents/beeline/sheetersd.xlsx");
		wb2.write(out);
		wb2.close();
		out.close();
		
		System.out.print("----------");
	}*/
	
	/*public void process(String fileName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
        XSSFWorkbook workbook = new XSSFWorkbook(bis);
        XSSFWorkbook myWorkBook = new XSSFWorkbook();
        XSSFSheet sheet = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFSheet mySheet = null;
        XSSFRow myRow = null;
        XSSFCell myCell = null;
        XSSFCellStyle dd= null;
        int sheets = workbook.getNumberOfSheets();
        int fCell = 0;
        int lCell = 0;
        int fRow = 0;
        int lRow = 0;
        for (int iSheet = 0; iSheet < sheets; iSheet++) {
            sheet = workbook.getSheetAt(iSheet);
            
            if (sheet != null) {
                mySheet = myWorkBook.createSheet(sheet.getSheetName());
                
                fRow = sheet.getFirstRowNum();
                lRow = sheet.getLastRowNum();
                for (int iRow = fRow; iRow <= lRow; iRow++) {
                    row = sheet.getRow(iRow);
                    myRow = mySheet.createRow(iRow);
                    
                    if (row != null) {
                        fCell = row.getFirstCellNum();
                        lCell = row.getLastCellNum();
                        for (int iCell = fCell; iCell < lCell; iCell++) {
                            cell = row.getCell(iCell);
                            myCell = myRow.createCell(iCell);
                            if (cell != null) {
                                myCell.setCellType(cell.getCellType());
                                switch (cell.getCellType()) {
                                case XSSFCell.CELL_TYPE_BLANK:
                                    myCell.setCellValue("");
                                    break;

                                case XSSFCell.CELL_TYPE_BOOLEAN:
                                    myCell.setCellValue(cell.getBooleanCellValue());
                                    break;

                                case XSSFCell.CELL_TYPE_ERROR:
                                    myCell.setCellErrorValue(cell.getErrorCellValue());
                                    break;

                                case XSSFCell.CELL_TYPE_FORMULA:
                                    myCell.setCellFormula(cell.getCellFormula());
                                    break;

                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    myCell.setCellValue(cell.getNumericCellValue());
                                    break;

                                case XSSFCell.CELL_TYPE_STRING:
                                    myCell.setCellValue(cell.getStringCellValue());
                                    break;
                                default:
                                    myCell.setCellFormula(cell.getCellFormula());
                                }
                            }
                        }
                    }
                }
            }
        }
        bis.close();
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream("C:/Users/HELIUS/Documents/beeline/sheeter.xlsx"));
        myWorkBook.write(bos);
        bos.close();
    }*/
}
