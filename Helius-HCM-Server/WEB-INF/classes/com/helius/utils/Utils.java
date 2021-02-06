package com.helius.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.taglibs.standard.extra.spath.AbsolutePath;
import org.hibernate.Session;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helius.entities.Employee;
import com.helius.entities.Workpermit_Worklocation;
import com.helius.entities.workpermit;
import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor.Uri;

public class Utils {
	public static Properties instance = null;
	public static Properties happrop = null;

	static{
		String path = System.getProperty("jboss.server.home.dir")+"/conf/helius_hcm.properties";
		//String path1 = "C:"+File.separator+"Users"+File.separator+"HELIUS"+File.separator+"git"+File.separator+"Helius-HCM-Server"+File.separator+"WebContent"+File.separator+"WEB-INF"+File.separator+"helius_hcm.properties";
		//String path =  System.getProperty("helius_hcm.properties");
		InputStream inStream;
		try {
			inStream = new FileInputStream(path);
			instance = new Properties();
			instance.load(inStream);		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream is = null;
        try {
            happrop = new Properties();
             is = Utils.class.getResourceAsStream("/com/helius/utils/hap.properties");
            happrop.load(is);
            String check =	happrop.getProperty("hcm_testing");
        	if("yes".equalsIgnoreCase(check)){
                System.out.println("====check===="+check);
        		String fileLoc = instance.getProperty("fileLocation")+File.separator+"hapTesting";
        		instance.setProperty("fileLocation", fileLoc);
        	}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }	
	}
	
	public Utils(){
	
	}
	
	public static String getProperty(String key) {
		return instance.getProperty(key);
	}
	
	public static String getHapProperty(String key) {
		return happrop.getProperty(key);
	}

	private  org.hibernate.internal.SessionFactoryImpl sessionFactory;

	/**
	 * @return the sessionFactory
	 */
	public org.hibernate.internal.SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(org.hibernate.internal.SessionFactoryImpl sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public final String imagedir = "C:" + File.separator + "Users" + File.separator + "HELIUS" + File.separator
			+ "Documents" + File.separator + "employee_photo";


	/*public final static String fileLocation = "C:" + File.separator + "Users" + File.separator + "HELIUS" + File.separator
			+ "Documents";
	public static final String fileLocation = File.separator + "home" + File.separator + "ec2-user" + File.separator + "documents";
	*/
	public final static String fileLocation = "C:" + File.separator + "Users" + File.separator + "HELIUS" + File.separator
			+ "Documents";


	public static String jsonPicklist(Set<String> data) {
		StringBuffer Sb = new StringBuffer();
		List<String> sortedList = new ArrayList(data);
		Collections.sort(sortedList,String.CASE_INSENSITIVE_ORDER);
		int num = 0;
		for (String s : sortedList) {
			if (num < sortedList.size() - 1) {
				Sb.append("\"" + s + "\",");
			} else {
				Sb.append("\"" + s + "\"");
			}
			num++;
		}
		String jsonResult = "[" + Sb.toString() + "" + "]";
		return jsonResult;
	}
	
	public static String jsonPicklistClientGroups44(List<Object[]> ClientGrouplists) {
		// TODO Auto-generated method stub	
		List<String> sub_groups1 = new ArrayList<String>();
		HashMap<String, HashMap<String,List<String>>> client_groups_subgroups = new HashMap<String, HashMap<String,List<String>>>();
		HashMap<String, List<String>> group_subgroups = new HashMap<String, List<String>>();
		String grp=null;
		String cli=null;
		String subgrp=null;
		for (Object[] row : ClientGrouplists) {
			 grp = row[1].toString();
			 cli = row[0].toString();
			 subgrp = row[2].toString();
	/*		if(!(sub_groups1.contains(subgrp))){
		sub_groups1.add(subgrp);
			}
if(!(group_subgroups.containsValue(subgrp))){
		group_subgroups.put(grp, sub_groups1);
		System.out.println("=============group_subgroupssas====="+group_subgroups);

}*/
			 if(group_subgroups.containsKey(grp)){
				    // if the key has already been used,
				    // we'll just grab the array list and add the value to it
				 sub_groups1 =  group_subgroups.get(grp);
				 sub_groups1.add(subgrp);
				} else {
				    // if the key hasn't been used yet,
				    // we'll create a new ArrayList<String> object, add the value
				    // and put it in the array list with the new key
					sub_groups1 = new ArrayList<String>();
					sub_groups1.add(subgrp);
				    group_subgroups.put(grp, sub_groups1);
					System.out.println("======group_subgroups2323====="+group_subgroups);

				}
			//	client_groups_subgroups.put(cli,group_subgroups);

	/*	List<String> sub_groups2 = new ArrayList<String>();
		sub_groups2.add("RIB");
		sub_groups2.add("Support");
		group_subgroups.put("Digital", sub_groups2);*/
		
		//if(!(client_groups_subgroups.containsValue(group_subgroups.containsKey(grp)))){
			 
		 if(client_groups_subgroups.containsKey(cli)){
			    // if the key has already been used,
			    // we'll just grab the array list and add the value to it
			 group_subgroups =  client_groups_subgroups.get(cli);
			 group_subgroups.put(grp, sub_groups1);
			} else {
			    // if the key hasn't been used yet,
			    // we'll create a new ArrayList<String> object, add the value
			    // and put it in the array list with the new key
				group_subgroups = new HashMap<String, List<String>>();

				group_subgroups.put(grp, sub_groups1);
				client_groups_subgroups.put(cli,group_subgroups);

				System.out.println("======client_groups_subgroups23789====="+client_groups_subgroups);

			}
		//}
		}
	/*	String clientmasterdetails=null;
		for (Object cligrplst : ClientGrouplists) {
			clientmasterdetails = (String) cligrplst;
			System.out.println("\n\n===clientmasterdetailsxxxxx===" + clientmasterdetails);
			sub_groups1.add(clientmasterdetails);
		}
		*/
		
	
		
		ObjectMapper objmapper = new ObjectMapper();
		String mapToJson=null;
		try {
			 mapToJson = objmapper.writeValueAsString(client_groups_subgroups);
			System.out.println(mapToJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapToJson;
	}
	
	
	public static String jsonPicklistClientGroups(List<Object[]> ClientGrouplists) {
		// TODO Auto-generated method stub
		List<String> sub_groups1 = new ArrayList<String>();
		HashMap<String, HashMap<String, List<String>>> client_groups_subgroups = new HashMap<String, HashMap<String, List<String>>>();
		HashMap<String, List<String>> group_subgroups = new HashMap<String, List<String>>();
		String grp = null;
		String cli = null;
		String subgrp = null;
		for (Object[] row : ClientGrouplists) {
			grp = row[1].toString();
			cli = row[0].toString();
			subgrp = row[2].toString();
			if (group_subgroups.containsKey(grp)) {
				sub_groups1 = group_subgroups.get(grp);
				sub_groups1.add(subgrp);
			} else {
				sub_groups1 = new ArrayList<String>();
				sub_groups1.add(subgrp);
				group_subgroups.put(grp, sub_groups1);
			}
			HashMap<String, List<String>> group_subgroupses;
			if (client_groups_subgroups.containsKey(cli)) {
				group_subgroupses = client_groups_subgroups.get(cli);
				group_subgroupses.put(grp, sub_groups1);
			} else {
				group_subgroupses = new HashMap<String, List<String>>();
				group_subgroupses.put(grp, sub_groups1);
				client_groups_subgroups.put(cli, group_subgroupses);
			}
		}
		ObjectMapper objmapper = new ObjectMapper();
		String mapToJson = null;
		try {
			mapToJson = objmapper.writeValueAsString(client_groups_subgroups);
			System.out.println(mapToJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapToJson;
	}
	
	/** to check whether given string is date timestamp format **/
	public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
       dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
	
	public static String jsonPicklistClientMembers(List<Object[]> ClientMember) {
		
		// TODO Auto-generated method stub	
		List<String> members = new ArrayList<String>();
		HashMap<String, List<String>> client_members = new HashMap<String, List<String>>();
		for (Object[] row : ClientMember) {
			String member = row[1].toString();
			String cli = row[0].toString();
			
			if (client_members.containsKey(cli)) {
				members = client_members.get(cli);
				members.add(member);
			} else {
				members = new ArrayList<String>();
				
				members.add(member);
				client_members.put(cli, members);
				System.out.println("======clientmemberssdsja23=====" + client_members);
			}	
		}
		ObjectMapper objmapper = new ObjectMapper();
		String mapToJson=null;
		try {
			 mapToJson = objmapper.writeValueAsString(client_members);
			System.out.println(mapToJson);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapToJson;
	}
		public static String jsonPicklist1(List<Object[]> rows,int index) {
			StringBuffer Sb = new StringBuffer();
			Set<String> data= new HashSet<String>();
			for (Object[] row : rows) {
				if (row[index] != null) {
					String rowList = row[index].toString().trim();
					if (rowList.length() > 0 ) {
						data.add(rowList);
					}
				}
			}
			List<String> sortedList = new ArrayList(data);
			Collections.sort(sortedList,String.CASE_INSENSITIVE_ORDER);
			int num = 0;
			for (String s : sortedList) {
				if (num < sortedList.size() - 1) {
					Sb.append("\"" + s + "\",");
				} else {
					Sb.append("\"" + s + "\"");
				}
				num++;
			}
			String jsonResult = "[" + Sb.toString() + "" + "]";
			return jsonResult;
		}
	
	public static String jsonWorkPermitPicklist(List<Object[]> work_Permit_Mstr) {
		StringBuffer Sb = new StringBuffer();
		HashMap<String, Workpermit_Worklocation> workpermit_nationality = new HashMap<String, Workpermit_Worklocation>();
		/*
		  for (Object[] row : work_Permit_Mstr) {
		   String nationality = row[1].toString(); if(workPermitMaster.containsKey(nationality)) {
		  List values = (List)workPermitMaster.get(nationality); String wrkLoc  = row[2].toString(); if (!(values.contains(wrkLoc))) {
		  values.add(row[2].toString()); values.add("{"+row[6].toString()+"}");
		  }
		   values.add(row[4].toString()); values.add(row[5].toString()); }
		  else
		   {
		    List values = new ArrayList(); values.add(row[2].toString());
		  values.add(row[4].toString()); values.add(row[5].toString());
		  values.add("{"+row[6].toString()+"}");
		  workPermitMaster.put(nationality, values); } }
		 */
		try{
			String nationality=null;
		String worklocation = null;
		String WPname = null;
		String[] dspFields=null;
		workpermit wp=null;
		for (Object[] row : work_Permit_Mstr) {
			 nationality = row[1].toString(); 
			 	if(row[2]!=null){
					 worklocation = row[2].toString();
			 	}else{				 		
			 		worklocation=null;
			 	}			
			String key = nationality + "," + worklocation;		
			if(row[4] != null && row[5] != null) {
				String displayFields = row[4].toString();
				 dspFields = displayFields.split(",");
				 wp = new workpermit(row[5].toString(), dspFields);

			}else{
				
				wp = null;
			}
			//workpermit wp = new workpermit(row[5].toString(), dspFields);
			String im = (row[6].toString());
			Workpermit_Worklocation ww = workpermit_nationality.get(key);
			if (ww == null) {
				ww = new Workpermit_Worklocation(nationality, worklocation);
			}
			String[] identications = im.split(",");
			for (String id : identications) {
				ww.addid(id);
			}
			ww.addworkpermit(wp);
			workpermit_nationality.put(key, ww);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper om = new ObjectMapper();
		// om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		StringBuffer json = new StringBuffer();
		try {
			int i = 0;
			for (Entry<String, Workpermit_Worklocation> entry : workpermit_nationality.entrySet()) {
				json.append(om.writeValueAsString(entry.getValue()));
				// json.append(",");
				if (i < (workpermit_nationality.size() - 1)) {
					json.append(",");
					i++;
				}
			}
			System.out.println("json" + json);
			// employeejson = employeejson1.replaceAll("null",
			// "\"-\"").replaceAll("\"\"", "\"-\"");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jsonResult = "[" + json.toString() + "" + "]";
		return jsonResult;
	}	

	public String mapUiLabel(Employee employee) {
		Map<String, Object> paramMap = new HashMap<>();
		ObjectMapper om = new ObjectMapper();
		String jsonResponse = null;
		try {
			if (employee.getEmployeePersonalDetails().getEmployee_id() != null) {
				paramMap.put("Employee ID", employee.getEmployeePersonalDetails().getEmployee_id());
			}
			jsonResponse = om.writeValueAsString(paramMap);
		} catch (JsonProcessingException e) {

		}
		return jsonResponse;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static HashMap<String,String> getEmailIdFromPickllistNameAndEmployeeNameAssoc(String[] picklist_name, Session session)
			throws Exception {
		List<Object[]> emailids = null;
		String Query = "SELECT picklist_name,helius_email_id from pickllistNameAndEmployeeNameAssoc where picklist_name IN (:picklist_name) ";
		emailids = session.createSQLQuery(Query).setParameterList("picklist_name", picklist_name).list();
		HashMap<String, String> map = new HashMap<String, String>();
		for (Object[] obj : emailids) {
			String email = "";
			if (obj[1] != null) {
				email = obj[1].toString();
			}
			map.put(obj[0].toString(), email);
		}
		return map;
	}
	
	public static List getEmailIdFromPickllistNameAndEmployeeNameAssoc(String picklist_name,Session session) throws Exception {
		String Query = "SELECT helius_email_id from pickllistNameAndEmployeeNameAssoc where picklist_name = :picklist_name ";
		java.util.List emailids = session.createSQLQuery(Query).setParameter("picklist_name", picklist_name).list();
		return emailids;
		}
	
	
	public static FilecopyStatus copyFiles(MultipartHttpServletRequest request, Map<String,String> modifiedFilenames, String filefolder) throws Exception {
		List<String> copied_with_success = new ArrayList<String>();
		FilecopyStatus success = new FilecopyStatus();
		success.setOk(true);
		String clientfilelocation = Utils.getProperty("fileLocation") + File.separator + filefolder;
		File fileDir = new File(clientfilelocation);
		if (!fileDir.exists()) {
			boolean iscreated = fileDir.mkdirs();
			if (!iscreated) {
				throw new Exception("Failed to copy files Directory not available");
			}
		}
		Iterator<String> fileNames = request.getFileNames();
		//String filename = "";
		while(fileNames.hasNext()) {		
			String filename = fileNames.next();
			MultipartFile file = request.getFile(filename);
			//filename = file.getOriginalFilename();
			String modifiedfilename = modifiedFilenames.get(filename);
			// filename = id + "_" + file.getOriginalFilename();
			String fileUrl = clientfilelocation;
			// fileUrl = fileUrl.replaceAll("\\\\", "\\\\\\\\");
			try {
				file.transferTo(new File(new File(fileUrl), modifiedfilename));
				copied_with_success.add(fileUrl+File.separator + modifiedfilename);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				//success = false;
				success.setOk(false);
				deleteFiles(copied_with_success);
				throw new Exception("Failed to save the files")	;					
			}
		}
		success.setCopied_with_success(copied_with_success);
		return success;
	}
	
	public static FilecopyStatus copyFiles(MultipartHttpServletRequest request, Map<String,String> modifiedFilenames, Map<String,String> filefolder) throws Exception {
		
		List<String> copied_with_success = new ArrayList<String>();
		FilecopyStatus success = new FilecopyStatus();
		success.setOk(true);
		String clientfilelocation = Utils.getProperty("fileLocation");
		Iterator<String> fileNames = request.getFileNames();
		//String filename = "";
		while(fileNames.hasNext()) {
			String filename = fileNames.next();
			MultipartFile file = request.getFile(filename);
			//filename = file.getOriginalFilename();
			String modifiedfilename = modifiedFilenames.get(filename);
			// filename = id + "_" + file.getOriginalFilename();
			String folder = filefolder.get(filename);
			String fileUrl = clientfilelocation + File.separator + folder;
			File fileDir = new File(fileUrl);
			if (!fileDir.exists()) {
				boolean iscreated = fileDir.mkdirs();
				if (!iscreated) {
					throw new Exception("Failed to copy files Directory not available");
				}
			}
			try {
				file.transferTo(new File(new File(fileUrl), modifiedfilename));
				copied_with_success.add(fileUrl+File.separator + modifiedfilename);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//success = false;
				success.setOk(false);
				deleteFiles(copied_with_success);
				throw new Exception("Failed to save the files")	;					
			}
		}
		success.setCopied_with_success(copied_with_success);
		return success;
	}
	
public static FilecopyStatus copySowFiles(MultipartHttpServletRequest request, Map<String,String> modifiedFilenames, Map<String,String> filefolder) throws Exception {		
		List<String> copied_with_success = new ArrayList<String>();
		FilecopyStatus success = new FilecopyStatus();
		success.setOk(true);
		String clientfilelocation = Utils.getProperty("fileLocation");
		Iterator<String> fileNames = request.getFileNames();
		//String filename = "";
		while(fileNames.hasNext()) {
			String filename1 = fileNames.next();
			MultipartFile file = request.getFile(filename1);
			String filename = new String(filename1.getBytes("ISO8859_1"), "UTF8");
			//filename = file.getOriginalFilename();
			String modifiedfilename = modifiedFilenames.get(filename);
			// filename = id + "_" + file.getOriginalFilename();
			String folder = filefolder.get(filename);
			String fileUrl = clientfilelocation + File.separator + folder;
			File fileDir = new File(fileUrl);
			if (!fileDir.exists()) {
				boolean iscreated = fileDir.mkdirs();
				if (!iscreated) {
					throw new Exception("Failed to copy files Directory not available");
				}
			}
			try {
				file.transferTo(new File(new File(fileUrl), modifiedfilename));
				copied_with_success.add(fileUrl+File.separator + modifiedfilename);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//success = false;
				success.setOk(false);
				deleteFiles(copied_with_success);
				throw new Exception("Failed to save the files")	;					
			}
		}
		success.setCopied_with_success(copied_with_success);
		return success;
	}
	
	public static void deleteFiles(List<String> copied_with_success) {
		
		for(String filename : copied_with_success) {
			File file = new File(filename);
			file.delete();
		}
	}
	
	public static String convertTimestampToStringDate(Timestamp date){
		LocalDate localdate = date.toLocalDateTime().toLocalDate();
		return localdate.getDayOfMonth()+"-"+localdate.getMonthValue()+"-"+localdate.getYear(); 
	}
}
