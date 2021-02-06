package com.helius.dao;

import java.util.HashMap;

public class LeaveTypeConstants {
	
	private LeaveTypeConstants(){
		
	}
	// leave tyypes
	 static final String india_Annual = "Annual Leave";
	 static final String india_Sick = "Sick Leave";
	 static final String india_CF = "CF Leave";
	 static final String singapore_Annual = "Annual Leave";
	 static final String singapore_Sick = "Sick Leave";
	 static final String singapore_CF = "CF Leave";
	 
	 // Singapore Sick Leave allowed for the duration of months
	 static final float After3MOnths = 5;
	 static final float After4MOnths = 8;
	 static final float After5MOnths = 11;
	 static final float After6MOnths = 14;

	 
	 //leave codes for singapore location
	 static final String singaporePresentLeaveCode = "PRESENT";
	 static final String singaporeAnnualLeaveCode = "ANNU";
	 static final String singaporeSICKLeaveCode = "SICK";
	 static final String singaporeChildCareLeaveCode = "CHILDLVE";
	 static final String singaporeEnhancedChildCareLeaveCode = "ECHILD";
	 static final String singaporeHospitalisationLeaveCode = "HOSP";
	 static final String singaporePublicHolidayLeaveCode = "PH";
	 static final String singaporeOffInLieuLeaveCode = "LIEU";
	 static final String singaporeMaternityLeaveCode = "MATE";
	 static final String singaporeNoPayLeaveLeaveCode = "NPL";
	 static final String singaporeNationalServiceLeaveCode = "NSL";
	 static final String singaporePaternityLeaveCode = "PATE";
	 static final String singaporeMarriageLeaveCode = "MARR";
	 static final String singaporeInfantCareLeaveCode = "INFANT";
	 static final String singaporeExtendedMaternityLeaveCode = "EMATE";
	 static final String singaporeCompassionateLeaveCode = "COMP";
	 static final String singaporeWeekOffLeaveCode = "OFF";

	 
	public static HashMap<String,String> getSingaporeLeaveCode(){
		HashMap<String,String> leavecodes = new HashMap<String,String>();
		leavecodes.put("Annual Leave", singaporeAnnualLeaveCode);
		leavecodes.put("Sick Leave", singaporeSICKLeaveCode);
		leavecodes.put("Childcare Leave", singaporeChildCareLeaveCode);
		leavecodes.put("Enhanced Childcare Leave", singaporeEnhancedChildCareLeaveCode);
		leavecodes.put("Maternity Leave", singaporeMaternityLeaveCode);
		leavecodes.put("No Pay Leave", singaporeNoPayLeaveLeaveCode);
		leavecodes.put("National Service Leave", singaporeNationalServiceLeaveCode);
		leavecodes.put("Paternity Leave", singaporePaternityLeaveCode);
		leavecodes.put("Infant Care Leave", singaporeInfantCareLeaveCode);
		leavecodes.put("Extended Maternity Leave", singaporeExtendedMaternityLeaveCode);
		leavecodes.put("Compassionate Leave", singaporeCompassionateLeaveCode);
		leavecodes.put("Marriage Leave", singaporeMarriageLeaveCode);
		return leavecodes;		
	}

}
