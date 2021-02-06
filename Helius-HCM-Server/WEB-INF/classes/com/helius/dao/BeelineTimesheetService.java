package com.helius.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Employee_Beeline_Details;
import com.helius.entities.Employee_Beeline_Timesheet;
import com.helius.entities.Employee_Timesheet_Status;
import com.helius.service.EmailService;
import com.helius.utils.Utils;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class BeelineTimesheetService {
	
	private org.hibernate.internal.SessionFactoryImpl sessionFactory;

	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Autowired
	TimeSheetAndLeaveServiceImpl timesheetService;
	@Autowired
	EmailService emailService;
	
	public List<Employee_Beeline_Details> getBeelineHapNameAssosc() throws Throwable {
		Session session = null;
		List<Employee_Beeline_Details> beelineEmpAssocList = null;
		HashMap<String,Employee_Beeline_Details> beelineEmpList = new HashMap<String,Employee_Beeline_Details>();
		try {
			session = sessionFactory.openSession();
			String query = "SELECT * from Employee_Beeline_Details";
			Query EmpAssocQuery = session.createSQLQuery(query).addEntity(Employee_Beeline_Details.class);
			beelineEmpAssocList = EmpAssocQuery.list();
			session.close();
		} catch (Exception e) {
			e.printStackTrace(); 
			throw new Throwable("Failed to fetch beeline employee Association details " + e.getMessage());
		}
		return beelineEmpAssocList;
	}
	
	public HashMap<String,BeelineTimesheetDashboard> getEmpBlinDetails(Timestamp month) throws Throwable {
		Session session = null;
		List<BeelineTimesheetDashboard> empEmails = null;
		HashMap<String,BeelineTimesheetDashboard> beelineEmpList = new HashMap<String,BeelineTimesheetDashboard>();
		try {
			session = sessionFactory.openSession();
			String query = "SELECT * from Employee_Beeline_Timesheet_Dashboard where timesheet_month = :timesheet_month";
			Query empEmailsQuery = session.createSQLQuery(query).
					setResultTransformer(Transformers.aliasToBean(BeelineTimesheetDashboard.class)).setParameter("timesheet_month", month);
			empEmails = empEmailsQuery.list();
			session.close();
			for(BeelineTimesheetDashboard obj :empEmails){
				beelineEmpList.put(obj.getEmployee_id(),obj);
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			throw new Throwable("Failed to fetch Beeline dashboard details " + e.getMessage());
		}
		return beelineEmpList;
	}

	public Timestamp convertExcelDateTOTimestamp(String date) throws Exception{
		SimpleDateFormat sdfs = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parseDate = sdfs.parse(date);
		String formattedDate = outputFormat.format(parseDate);
		Date convdate = outputFormat.parse(formattedDate);
		Timestamp timestamp = new Timestamp(convdate.getTime());
		return timestamp;
	}
	
	public String[] beelinenameIndex(Sheet sheet) {
		int rowindex = 0;
		int cellindex = 0;
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.toString().startsWith("Contractor: ")) {
					rowindex = cell.getRowIndex();
					cellindex = cell.getColumnIndex();
					break;
				}
			}
		}
		String[] str = { String.valueOf(rowindex), String.valueOf(cellindex) };
		return str;
	}

	public void process(Sheet sheet,String out) throws Exception {
        XSSFWorkbook myWorkBook = new XSSFWorkbook();
        XSSFRow row = null;
        XSSFCell cell = null;
        XSSFSheet mySheet = null;
        XSSFRow myRow = null;
        XSSFCell myCell = null;
        XSSFCellStyle dd= null;
        int fCell = 0;
        int lCell = 0;
        int fRow = 0;
        int lRow = 0;
        mySheet = myWorkBook.createSheet(sheet.getSheetName());
        ListIterator<CellRangeAddress>  src_MergedRegions_iter = sheet.getMergedRegions().listIterator();
        while(src_MergedRegions_iter.hasNext()) {
            CellRangeAddress cr = src_MergedRegions_iter.next();
            mySheet.addMergedRegion(cr);
            
        }
        fRow = sheet.getFirstRowNum();
        lRow = sheet.getLastRowNum();
        for (int iRow = fRow; iRow <= lRow; iRow++) {
            row = (XSSFRow) sheet.getRow(iRow);
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
BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream(out));
mySheet.setFitToPage(true);
myWorkBook.write(bos);
bos.close();
}

	
	public String saveBeelineTimesheetDetailsFiles(String timesheetMonth, MultipartHttpServletRequest request)
			throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp tsMonth = null;
		Timestamp submittedDate = null;
		Session session = null;
		Transaction transaction = null;
		String result = null;
		List<String> copied_with_success = new ArrayList<String>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Set<String> issueinprocessingBeelineId = new HashSet<String>();
			Set<String> processedBeelineIdSuccess = new HashSet<String>();
			java.util.Date selectedDate = sdf.parse(timesheetMonth);
			tsMonth = new Timestamp(selectedDate.getTime());
			Date startdat1 = sdf.parse(sdf.format(new Date()));
			submittedDate = new Timestamp(startdat1.getTime());
			String fileUrl = Utils.getProperty("fileLocation") + File.separator + "beeline_files" + File.separator
					+ "timesheet" + File.separator + tsMonth.toLocalDateTime().getMonth() + " "
					+ tsMonth.toLocalDateTime().getYear();
			File fileDir = new File(fileUrl);
			if (!fileDir.exists()) {
				boolean iscreated = fileDir.mkdirs();
				if (!iscreated) {
					throw new Exception("Failed to copy files beeline Directory not available");
				}
			}
			List<Employee_Beeline_Timesheet> Checkalltimesheet = timesheetService
					.getAllBeelineTimesheet(timesheetMonth);
			HashMap<String, Employee_Beeline_Timesheet> checkExistingTimesheetMap = new HashMap<String, Employee_Beeline_Timesheet>();
			for (Employee_Beeline_Timesheet beelinTS : Checkalltimesheet) {
				if(beelinTS.getTimesheet_document_path() == null || beelinTS.getTimesheet_document_path().isEmpty()){
					checkExistingTimesheetMap.put(beelinTS.getEmployee_id(), beelinTS);
				}else{
					continue;
				}
			}
			List<Employee_Beeline_Details> empAssocList = getBeelineHapNameAssosc();
			HashMap<String, Employee_Beeline_Details> empIDBeelinNameAssoc = new HashMap<String, Employee_Beeline_Details>();
			for(Employee_Beeline_Details obj :empAssocList){
				empIDBeelinNameAssoc.put(obj.getEmployee_id(),obj);
			}
			MultipartFile beelineFile = request.getFile("beeline");
			XSSFWorkbook wb = new XSSFWorkbook(beelineFile.getInputStream());
			Sheet sh = wb.getSheetAt(0);
			String[] beelinenameindex = beelinenameIndex(sh);
			int rowIndexName = 0;
			if (beelinenameindex[0] != null && !"".equalsIgnoreCase(beelinenameindex[0])) {
				rowIndexName = Integer.parseInt(beelinenameindex[0]);
			}
			int cellNameIndex = 0;
			if (beelinenameindex[1] != null && !"".equalsIgnoreCase(beelinenameindex[1])) {
				cellNameIndex = Integer.parseInt(beelinenameindex[1]);
			}
			HashMap<String, Sheet> sheetMap = new HashMap<String,Sheet>();
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				try{
				String beeline_name = null;
				if(wb.getSheetAt(i).getRow(rowIndexName).getCell(cellNameIndex) != null){
					beeline_name = wb.getSheetAt(i).getRow(rowIndexName).getCell(cellNameIndex).toString();
				}else{
					String[] beelinenameindexs = beelinenameIndex(wb.getSheetAt(i));
					beeline_name = wb.getSheetAt(i).getRow(Integer.parseInt(beelinenameindexs[0])).getCell(Integer.parseInt(beelinenameindex[1])).toString();
				}
				if (beeline_name != null && beeline_name.startsWith("Contractor: ")) {
					int collonindex = beeline_name.indexOf(":");
					String blnName = beeline_name.substring(collonindex + 1).trim();
					sheetMap.put(blnName, wb.getSheetAt(i));
				}else{
					issueinprocessingBeelineId.add(wb.getSheetName(i));
				}
				}catch(Exception e){
					e.printStackTrace();
					issueinprocessingBeelineId.add(wb.getSheetName(i));
				}
			}
			int j=0;
			for(String empid : checkExistingTimesheetMap.keySet()){
				String outputfilename = null;
				try{
				String blnName = null;
				String employee_Name = null;
				if(empIDBeelinNameAssoc.containsKey(empid)){
					Employee_Beeline_Details  empBlnDtls = empIDBeelinNameAssoc.get(empid);
					blnName = empBlnDtls.getBeeline_name();
					employee_Name = empBlnDtls.getEmployee_name();
				}
				if(blnName != null && sheetMap.containsKey(blnName)){
				String workboonName = (employee_Name + "_TimeSheet_" + tsMonth.toLocalDateTime().getMonth()
							+ " " + tsMonth.toLocalDateTime().getYear()).trim();
					outputfilename = fileDir + File.separator + workboonName +".xlsx";
					process(sheetMap.get(blnName),outputfilename);
					Employee_Beeline_Timesheet timesheet = checkExistingTimesheetMap.get(empid);
					timesheet.setTimesheet_document_path(workboonName+".xlsx");
					LocalDateTime ldc = LocalDateTime.now();
					timesheet.setFinal_submission_date(new Timestamp(new Date().getTime()));
					timesheet.setTimesheet_status("Approved");
					session.update(timesheet);
					copied_with_success.add(outputfilename);
					processedBeelineIdSuccess.add(blnName);
					System.out.println("created " + blnName);
					if(j%50 == 0) {
						  session.flush();
						  session.clear(); 
					 }
					j++;
				}
				} catch (Exception e) {
					e.printStackTrace();
					File tempFile = new File(outputfilename);
					if (tempFile.exists()) {
						try {
							tempFile.delete();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			HashMap<String, Object> resultobj = new HashMap<String, Object>();
			resultobj.put("processes", processedBeelineIdSuccess);
			resultobj.put("issueInProcessing", issueinprocessingBeelineId);
			ObjectMapper om = new ObjectMapper();
			result = om.writeValueAsString(resultobj);
			wb.close();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to process beeline Timesheet " + e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}
	
	
	/*public String saveBeelineTimesheetDetailsFiles2(String timesheetMonth, MultipartHttpServletRequest request)
			throws Throwable {
		InputStream file = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp tsMonth = null;
		Timestamp submittedDate = null;
		Session session = null;
		Transaction transaction = null;
		String result = null;
		List<String> copied_with_success = new ArrayList<String>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			List<String> issueinprocessingBeelineId = new ArrayList<String>();
			List<String> processedBeelineIdSuccess = new ArrayList<String>();
			java.util.Date selectedDate = sdf.parse(timesheetMonth);
			tsMonth = new Timestamp(selectedDate.getTime());
			Date startdat1 = sdf.parse(sdf.format(new Date()));
			submittedDate = new Timestamp(startdat1.getTime());
			String fileUrl = Utils.getProperty("fileLocation") + File.separator + "beeline_files" + File.separator
					+ "timesheet" + File.separator + tsMonth.toLocalDateTime().getMonth() + " "
					+ tsMonth.toLocalDateTime().getYear();
			File fileDir = new File(fileUrl);
			if (!fileDir.exists()) {
				boolean iscreated = fileDir.mkdirs();
				if (!iscreated) {
					throw new Exception("Failed to copy files beeline Directory not available");
				}
			}
			List<Employee_Beeline_Timesheet> Checkalltimesheet = timesheetService
					.getAllBeelineTimesheet(timesheetMonth);
			HashMap<String, Employee_Beeline_Timesheet> checkExistingTimesheetMap = new HashMap<String, Employee_Beeline_Timesheet>();
			for (Employee_Beeline_Timesheet beelinTS : Checkalltimesheet) {
				checkExistingTimesheetMap.put(beelinTS.getEmployee_id(), beelinTS);
			}
			List<Employee_Beeline_Details> empAssocList = getBeelineHapNameAssosc();
			HashMap<String, Employee_Beeline_Details> empAssoc = new HashMap<String, Employee_Beeline_Details>();
			for(Employee_Beeline_Details obj :empAssocList){
				empAssoc.put(obj.getBeeline_name(),obj);
			}
			MultipartFile beelineFile = request.getFile("beeline");
			file = beelineFile.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(file, baos);
			byte[] bytes = baos.toByteArray();
			XSSFWorkbook wb = new XSSFWorkbook(new ByteArrayInputStream(bytes));
			FileOutputStream out = null;
			XSSFWorkbook[] workbooks = new XSSFWorkbook[wb.getNumberOfSheets()];
			// file.close();
			Sheet sh = wb.getSheetAt(0);
			String[] beelinenameindex = beelinenameIndex(sh);
			int rowIndexName = 0;
			if (beelinenameindex[0] != null && !"".equalsIgnoreCase(beelinenameindex[0])) {
				rowIndexName = Integer.parseInt(beelinenameindex[0]);
			}
			int cellNameIndex = 0;
			if (beelinenameindex[1] != null && !"".equalsIgnoreCase(beelinenameindex[1])) {
				cellNameIndex = Integer.parseInt(beelinenameindex[1]);
			}
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				String empName = "";
				try {
					XSSFWorkbook wb1 = new XSSFWorkbook(new ByteArrayInputStream(bytes));
					workbooks[i] = wb1;
					int k = i + 1;
					String beeline_name = wb1.getSheetAt(i).getRow(rowIndexName).getCell(cellNameIndex).toString();
					String workboonName = "";
					if (beeline_name != null && beeline_name.startsWith("Contractor: ")) {
						int collonindex = beeline_name.indexOf(":");
						empName = beeline_name.substring(collonindex + 1).trim();
						workboonName = empName.replace(",", " ") + "_TimeSheet_" + tsMonth.toLocalDateTime().getMonth()
								+ " " + tsMonth.toLocalDateTime().getYear();
						workboonName.trim();
					} else {
						String[] str = beelinenameIndex(wb1.getSheetAt(i));
						beeline_name = wb1.getSheetAt(i).getRow(Integer.parseInt(str[0]))
								.getCell(Integer.parseInt(str[1])).toString();
						int collonindex = beeline_name.indexOf(":");
						empName = beeline_name.substring(collonindex + 1).trim();
						workboonName = empName.replace(",", " ") + "_TimeSheet_" + tsMonth.toLocalDateTime().getMonth()
								+ " " + tsMonth.toLocalDateTime().getYear();
						workboonName.trim();
					}
					String outputfilename = fileDir + File.separator + workboonName + ".xlsx";
					if (empAssoc.containsKey(empName)) {
						Employee_Beeline_Details ebd = empAssoc.get(empName);
						String empid = ebd.getEmployee_id();
						if (checkExistingTimesheetMap.containsKey(empid)) {
							try {
								Employee_Beeline_Timesheet timesheet = checkExistingTimesheetMap.get(empid);
								if ("Approved".equalsIgnoreCase(timesheet.getTimesheet_status())
										&& timesheet.getTimesheet_document_path() != null
										&& !"".equalsIgnoreCase(timesheet.getTimesheet_document_path())) {
									processedBeelineIdSuccess.add(empName);
									continue;
								}
								String sheetName = wb1.getSheetName(i);
								for (int j = workbooks[i].getNumberOfSheets() - 1; j >= 0; j--) {
									XSSFSheet tmpSheet = workbooks[i].getSheetAt(j);
									if (!tmpSheet.getSheetName().equals(sheetName)) {
										workbooks[i].removeSheetAt(j);
									}
								}
								out = new FileOutputStream(outputfilename);
								workbooks[i].write(out);
								workbooks[i].close();
								out.close();
								timesheet.setTimesheet_document_path(workboonName);
								timesheet.setFinal_submission_date(submittedDate);
								session.update(timesheet);
								copied_with_success.add(outputfilename);
								processedBeelineIdSuccess.add(empName);
								System.out.println("created " + sheetName);
							} catch (Exception e) {
								e.printStackTrace();
								File tempFile = new File(outputfilename);
								if (tempFile.exists()) {
									try {
										tempFile.delete();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						} else {
							issueinprocessingBeelineId.add(empName);
						}
					} else {
						issueinprocessingBeelineId.add(empName);
					}
				} catch (Exception e) {
					e.printStackTrace();
					issueinprocessingBeelineId.add(empName);
				}
			}
			transaction.commit();
			HashMap<String, Object> resultobj = new HashMap<String, Object>();
			resultobj.put("processes", processedBeelineIdSuccess);
			resultobj.put("issueInProcessing", issueinprocessingBeelineId);
			ObjectMapper om = new ObjectMapper();
			result = om.writeValueAsString(resultobj);
			wb.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
			Utils.deleteFiles(copied_with_success);
			throw new Throwable("Failed to process beeline Timesheet " + e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}*/
	
	public String saveBeelineTimesheetSummaryFile(String timesheetMonth, MultipartHttpServletRequest request) throws Throwable {
		HashMap<String, Row> beelineMap = new HashMap<String, Row>();
		int headerIndex = 0;
		int statusIndex = 0;
		int assignmentIdIndex = 0;
		int beelinenameIndex = 0;
		int dbsManagerIndex = 0;
		int approvalDateIndex = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Timestamp tsMonth = null;
		Session session = null;
		Transaction transaction = null;
		String result = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			java.util.Date selectedDate = sdf.parse(timesheetMonth);
			tsMonth = new Timestamp(selectedDate.getTime());
			Set<String> issueinprocessingBeelineId = new HashSet<String>();
			Set<String> processedBeelineIdSuccess = new HashSet<String>();
			MultipartFile beelineFile = request.getFile("beeline");
			InputStream xlsxContentStream = beelineFile.getInputStream();
			OPCPackage pkg = OPCPackage.open(xlsxContentStream);
			Workbook workbook = new XSSFWorkbook(pkg);
			// Iterator<Sheet> sh = workbook.iterator();
			// while(sh.hasNext()){
			// Sheet sheet = sh.next();
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					/*
					 * if(cell.toString().startsWith("Contractor:")){ nameIndex
					 * = cell.getRowIndex(); }
					 */
					if ("Timesheet Status".equalsIgnoreCase(cell.toString())) {
						headerIndex = cell.getRowIndex();
						statusIndex = cell.getColumnIndex();
					}
					if ("Assignment ID".equalsIgnoreCase(cell.toString())) {
						headerIndex = cell.getRowIndex();
						assignmentIdIndex = cell.getColumnIndex();
					}
					if ("Contractor".equalsIgnoreCase(cell.toString())) {
						headerIndex = cell.getRowIndex();
						beelinenameIndex = cell.getColumnIndex();
					}
					if ("DBS Manager".equalsIgnoreCase(cell.toString())) {
						dbsManagerIndex = cell.getColumnIndex();
					}	
					if ("Approved Date".equalsIgnoreCase(cell.toString())) {
						approvalDateIndex = cell.getColumnIndex();
					}
				} // end of cell iterator
				if (headerIndex != 0) {
					break;
				}
			}
			int i = 0;
			for (Row row : sheet) {
				if (i > headerIndex) {
					if (row.getCell(assignmentIdIndex) != null
							&& !row.getCell(assignmentIdIndex).toString().isEmpty()) {
						String beelineNmae = row.getCell(beelinenameIndex).toString();
						try{
						if (beelineMap.containsKey(beelineNmae)) {
							Row prevrowdata = beelineMap.get(beelineNmae);
							int prevAssignmentId = (int) prevrowdata.getCell(assignmentIdIndex).getNumericCellValue();
							int currAssignmentId = (int)row.getCell(assignmentIdIndex).getNumericCellValue();
							if (currAssignmentId > prevAssignmentId) {
								beelineMap.put(beelineNmae, row);
							}
							if (currAssignmentId < prevAssignmentId) {
								beelineMap.put(beelineNmae, prevrowdata);
							}
						} else {
							beelineMap.put(beelineNmae, row);
						}
						}catch (Exception e) {
							e.printStackTrace();
							issueinprocessingBeelineId.add(row.getCell(beelinenameIndex).toString());
						}
					}
				}
				i++;
			}
			List<Employee_Beeline_Timesheet> Checkalltimesheet = timesheetService
					.getAllBeelineTimesheet(timesheetMonth);
			HashMap<String, Employee_Beeline_Timesheet> checkExistingTimesheetMap = new HashMap<String, Employee_Beeline_Timesheet>();
			for (Employee_Beeline_Timesheet beelinTS : Checkalltimesheet) {
				checkExistingTimesheetMap.put(beelinTS.getEmployee_id(), beelinTS);
			}
			List<Employee_Beeline_Details> empAssocList = getBeelineHapNameAssosc();
			HashMap<String, Employee_Beeline_Details> empAssoc = new HashMap<String, Employee_Beeline_Details>();
			for(Employee_Beeline_Details obj :empAssocList){
				empAssoc.put(obj.getBeeline_name(),obj);
			}
			int j = 0;
			for (Map.Entry<String, Row> entry : beelineMap.entrySet()) {
				try{
				Row record = entry.getValue();
				String empid = null;
				String empName = null;
				if (empAssoc.containsKey(entry.getKey())) {
					Employee_Beeline_Details ebd = empAssoc.get(entry.getKey());
					empid = ebd.getEmployee_id();
					empName = ebd.getEmployee_name();
				}
				if (empid != null && !empid.isEmpty()) {
					Employee_Beeline_Timesheet timesheet = new Employee_Beeline_Timesheet();
					timesheet.setAssignment_id(String.valueOf((int)record.getCell(assignmentIdIndex).getNumericCellValue()));
					timesheet.setEmployee_id(empid);
					timesheet.setEmployee_name(empName);
					timesheet.setTimesheet_status(record.getCell(statusIndex).toString());
					timesheet.setDbs_manager(record.getCell(dbsManagerIndex).toString());
					timesheet.setTimesheet_month(tsMonth);
					Timestamp approvedDate = null;
					if(!"".equalsIgnoreCase(record.getCell(approvalDateIndex).toString())){
					approvedDate = convertExcelDateTOTimestamp(record.getCell(approvalDateIndex).toString());
					timesheet.setReceived_date(approvedDate);
					}
					if (!checkExistingTimesheetMap.containsKey(empid)) {
						session.save(timesheet);
						processedBeelineIdSuccess.add(entry.getKey());
					} else {
						Employee_Beeline_Timesheet Timesheet_Status = checkExistingTimesheetMap.get(empid);
						if("Approved".equalsIgnoreCase(Timesheet_Status.getTimesheet_status())){
							continue;
						}
						Timesheet_Status.setTimesheet_status(timesheet.getTimesheet_status());
						Timesheet_Status.setAssignment_id(timesheet.getAssignment_id());
						Timesheet_Status.setReceived_date(timesheet.getReceived_date());
						Timesheet_Status.setDbs_manager(timesheet.getDbs_manager());
						Timesheet_Status.setEmployee_name(timesheet.getEmployee_name());
						session.update(Timesheet_Status);
						processedBeelineIdSuccess.add(entry.getKey());
					}
					if (j % 50 == 0) {
						session.flush();
						session.clear();
					}
					j++;
				}else{
					issueinprocessingBeelineId.add(entry.getKey());
				}
			}catch (Exception e) {
				e.printStackTrace();
				issueinprocessingBeelineId.add(entry.getKey());
			}
			}
			HashMap<String,Object> resultobj = new HashMap<String,Object>();
			resultobj.put("processes", processedBeelineIdSuccess);
			resultobj.put("issueInProcessing", issueinprocessingBeelineId);
			ObjectMapper om = new ObjectMapper();
			result = om.writeValueAsString(resultobj);
			workbook.close();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Throwable("Failed to process beeline Timesheet " + e.getMessage());
		}finally{
			session.close();
		}
		return result;
	}
	
	public String beelineBulkEmailService(String month,String statusType) throws Throwable{
		Session session = null;
		Transaction transaction = null;
		ArrayList<String> failedTOEmailList = new ArrayList<>();
		String result = null;
		try{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		java.util.Date selectedDate = sdf.parse(month);
		Timestamp tsMonth = new Timestamp(selectedDate.getTime());
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		List<Employee_Beeline_Timesheet> allTimesheets = timesheetService.getAllBeelineTimesheet(month);
		HashMap<String, BeelineTimesheetDashboard> empEmailIds = getEmpBlinDetails(tsMonth);
		ArrayList<String> approvedList = new ArrayList<>();
		ArrayList<String> submittedList = new ArrayList<>();
		ArrayList<String> notSubmittedList = new ArrayList<>();
		ArrayList<String> missingList = new ArrayList<>();
		int j = 0;
		for(Employee_Beeline_Timesheet timesheet : allTimesheets){
			try{
			String status = timesheet.getTimesheet_status();
			if("ALL".equalsIgnoreCase(statusType) || status.equalsIgnoreCase(statusType)){
			if("Approved".equalsIgnoreCase(status)){
				if(timesheet.isApproved_email_send() == false){
					timesheet.setApproved_email_send(true);
					session.update(timesheet);
					if(j%50 == 0) {
						  session.flush();
						  session.clear(); 
					 }
					j++;
					boolean id = false;
					if (empEmailIds.containsKey(timesheet.getEmployee_id())) {
						 BeelineTimesheetDashboard blnData = empEmailIds.get(timesheet.getEmployee_id());
						if(blnData.getClient_email_id() !=null && !"".equalsIgnoreCase(blnData.getClient_email_id())){
							approvedList.add(blnData.getClient_email_id());
							id = true;
						}
						if(blnData.getPersonal_email_id() != null && !"".equalsIgnoreCase(blnData.getPersonal_email_id())){
							approvedList.add(blnData.getPersonal_email_id());
							id = true;
						}
						}
					if(id == false){
					failedTOEmailList.add(timesheet.getEmployee_id());
					}
				}
			}
			if("Submitted".equalsIgnoreCase(status)){
				boolean id = false;
				if (empEmailIds.containsKey(timesheet.getEmployee_id())) {
					 BeelineTimesheetDashboard blnData = empEmailIds.get(timesheet.getEmployee_id());
					if(blnData.getClient_email_id() !=null && !"".equalsIgnoreCase(blnData.getClient_email_id())){
						submittedList.add(blnData.getClient_email_id());
						id = true;
					}
					if(blnData.getPersonal_email_id() != null && !"".equalsIgnoreCase(blnData.getPersonal_email_id())){
						submittedList.add(blnData.getPersonal_email_id());
						id = true;
					}
					}
				if(id == false){
				failedTOEmailList.add(timesheet.getEmployee_id());
				}
				}
			if("Not Submitted".equalsIgnoreCase(status)){
				boolean id = false;
				if (empEmailIds.containsKey(timesheet.getEmployee_id())) {
					 BeelineTimesheetDashboard blnData = empEmailIds.get(timesheet.getEmployee_id());
					if(blnData.getClient_email_id() !=null && !"".equalsIgnoreCase(blnData.getClient_email_id())){
						notSubmittedList.add(blnData.getClient_email_id());
						id = true;
					}
					if(blnData.getPersonal_email_id() != null && !"".equalsIgnoreCase(blnData.getPersonal_email_id())){
						notSubmittedList.add(blnData.getPersonal_email_id());
						id = true;
					}
					}
				if(id == false){
				failedTOEmailList.add(timesheet.getEmployee_id());
				}
			}
			if("Missing".equalsIgnoreCase(status)){
				boolean id = false;
				if (empEmailIds.containsKey(timesheet.getEmployee_id())) {
					 BeelineTimesheetDashboard blnData = empEmailIds.get(timesheet.getEmployee_id());
					if(blnData.getClient_email_id() !=null && !"".equalsIgnoreCase(blnData.getClient_email_id())){
						missingList.add(blnData.getClient_email_id());
						id = true;
					}
					if(blnData.getPersonal_email_id() != null && !"".equalsIgnoreCase(blnData.getPersonal_email_id())){
						missingList.add(blnData.getPersonal_email_id());
						id = true;
					}
					}
				if(id == false){
				failedTOEmailList.add(timesheet.getEmployee_id());
				}
			}
			}
			}catch(Exception e){
				e.printStackTrace();
				failedTOEmailList.add(timesheet.getEmployee_id());
			}
		}
		transaction.commit();
		String timesheetMonth = tsMonth.toLocalDateTime().getMonth()+" "+tsMonth.toLocalDateTime().getYear();
		if(approvedList.size() > 0){
			try{
			String to = Utils.getHapProperty("RTSTimeSheetNotification-TO");
			String[] cc = null;
			String[] bcc = null;
			String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
			if (getCC != null && !getCC.isEmpty()) {
				cc = getCC.split(",");
			}
			bcc = approvedList.toArray(new String[approvedList.size()]);
			String subject =" Timesheet Status – Approved  ";
			StringBuffer message = new StringBuffer();
			message.append("Dear Employee,"+ "\n\n");			
			message.append("Thanks for submitting your "+timesheetMonth+" timesheet on Beeline and it is approved. "
					+ "We will process your timesheet and get back to you, if necessary.");
			message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendBulkEmail(to,cc,bcc,subject,message.toString());
			}catch(Exception e){
				e.printStackTrace();
				failedTOEmailList.add("failed to send - APPROVED - Emails");
			}
		}
		if(submittedList.size() > 0){
			try{
			String to = Utils.getHapProperty("RTSTimeSheetNotification-TO");
			String[] cc = null;
			String[] bcc = null;
			String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
			if (getCC != null && !getCC.isEmpty()) {
				cc = getCC.split(",");
			}
			bcc = submittedList.toArray(new String[submittedList.size()]);
			String subject =" Timesheet Status – SUBMITTED – yet pending for approval  ";
			StringBuffer message = new StringBuffer();
			message.append("Dear Employee,"+ "\n\n");			
			message.append("Your timesheet submission on Beeline is yet to be approved by your manager for the month of "+timesheetMonth
					+ ". Please reach out and remind your manager to approve your timesheet at the earliest. Please write to us if you find any difficulty in completing the same. You can reach us at timesheet@helius-tech.com "
					+ "and in case of any technical difficulty, please reach askhr@dbs.com. ");
			message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendBulkEmail(to,cc,bcc,subject,message.toString());
			}catch(Exception e){
				e.printStackTrace();	
				failedTOEmailList.add("failed to send - SUBMITTED - Emails");
			}
		}
		if(notSubmittedList.size() > 0){
			try{
			String to = Utils.getHapProperty("RTSTimeSheetNotification-TO");
			String[] cc = null;
			String[] bcc = null;
			String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
			if (getCC != null && !getCC.isEmpty()) {
				cc = getCC.split(",");
			}
			bcc = notSubmittedList.toArray(new String[notSubmittedList.size()]);
			String subject =" Subject Line: Timesheet Status – NOT SUBMITTED   ";
			StringBuffer message = new StringBuffer();
			message.append("Dear Employee,"+ "\n\n");			
			message.append("You have not yet submitted your timesheet on Beeline for the month of "+timesheetMonth
					+ ". Please do so at the earliest. Please write to us if you find any difficulty in completing the same. You can reach us at timesheet@helius-tech.com "
					+ "and in case of any technical difficulty, please reach askhr@dbs.com.  ");
			message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendBulkEmail(to,cc,bcc,subject,message.toString());
			}catch(Exception e){
				e.printStackTrace();	
				failedTOEmailList.add("failed to send - NOT SUBMITTED - Emails");
			}
		}
		
		if(missingList.size() > 0){
			try{
			String to = Utils.getHapProperty("RTSTimeSheetNotification-TO");
			String[] cc = null;
			String[] bcc = null;
			String getCC = Utils.getHapProperty("RTSTimeSheetNotification-CC");
			if (getCC != null && !getCC.isEmpty()) {
				cc = getCC.split(",");
			}
			bcc = missingList.toArray(new String[missingList.size()]);
			String subject =" Timesheet Status - MISSING   ";
			StringBuffer message = new StringBuffer();
			message.append("Dear Employee,"+ "\n\n");			
			message.append("Your timesheet submission on Beeline for the month of "+timesheetMonth+" is MISSING. "
					+ "Please complete submission of your timesheet and get your manager approval at the earliest."
					+ " Please write to us if you find any difficulty in completing the same. You can reach us at timesheet@helius-tech.com and"
					+ " in case of any technical difficulty, please reach askhr@dbs.com. ");
			message.append("Thanks," + "\n\n" + "HR Team," + "\n" + "Helius Technologies Pte.Ltd");
			emailService.sendBulkEmail(to,cc,bcc,subject,message.toString());
			}catch(Exception e){
				e.printStackTrace();	
				failedTOEmailList.add("failed to send - MISSING - Emails");
			}
		}
		ObjectMapper om = new ObjectMapper();
		result = om.writeValueAsString(failedTOEmailList);
		}catch(Exception e){
			e.printStackTrace();
			throw new Throwable("Failed to send beeline emails " + e.getMessage());
		}finally{
			session.close();
		}
		return result;
	}
}
