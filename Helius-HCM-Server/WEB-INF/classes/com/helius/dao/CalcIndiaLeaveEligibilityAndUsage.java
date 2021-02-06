package com.helius.dao;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.helius.entities.Client_Leave_Policy_New;
import com.helius.entities.Employee;
import com.helius.entities.Leave_Eligibility_Details;
import com.helius.entities.Leave_Usage_Details;
import com.helius.utils.ClientLeavePolicy;

public class CalcIndiaLeaveEligibilityAndUsage {
	
	/**
	 *Author - vinay
	 *if employee probation period is completing in current year den it will check for its actual date of joining if its in current year 
	 *then it will 3 annual leaves during probation period 
	 *if actual date of joining is in previous year den it will calculate for annual day from start of current year to probation end date 
	 *plus remaining annual leaves from client policy till end of the year 
	 * **/
	public List<Leave_Eligibility_Details> leaveEligForOnProbationCompletionInCurrentYear(LocalDate adoj, List<ClientLeavePolicy> clientPolicy) throws Throwable {
		List<Leave_Eligibility_Details> leaveEligibleList = new ArrayList<Leave_Eligibility_Details>();
		try{
		Leave_Eligibility_Details leave_Eligibility_Detail = null;
		LocalDate now = LocalDate.now();
		LocalDate yearEndDate = now.with(lastDayOfYear());
		LocalDate yearStartDate = now.with(firstDayOfYear());
		float noOfDaysBetween = ChronoUnit.DAYS.between(adoj.plusMonths(3), yearEndDate);
		float daysDiffFromProbEndToYearEnd = noOfDaysBetween / yearEndDate.getDayOfYear();
		//LeaveServiceImpl leaveService = new LeaveServiceImpl();
		for (ClientLeavePolicy clientleavepolicy : clientPolicy) {
			//float eligibleValue = clientleavepolicy.getNumber_days();
			float eligibleValue = 0;
			if (clientleavepolicy.getTypeofleave() != null
					&& LeaveTypeConstants.india_Annual.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
				float value = daysDiffFromProbEndToYearEnd * clientleavepolicy.getNumber_days();
				float eligibleValues = 0;
				if(adoj.getYear() == yearEndDate.getYear()){
				 eligibleValues = value + 3;
				}else{
					LocalDate probationDate = adoj.plusMonths(3);
						int i = 1;
						int addProbationLeave = 0;
						for (i = 1; i <= 3; i++) {
							if (probationDate.minusMonths(i).isAfter(yearStartDate)
									|| probationDate.minusMonths(i).equals(yearStartDate)) {
								addProbationLeave = addProbationLeave + 1;
							} else {
								break;
							}
						}
						float probationvalue = ChronoUnit.DAYS.between(yearStartDate,
								probationDate.minusMonths(addProbationLeave));
						float res = (probationvalue + 1)/ yearStartDate.MAX.getDayOfMonth();
					res = res + (float) addProbationLeave;
					eligibleValues = value + res;
				}
				eligibleValue = eligibleValues;
				//eligibleValue = leaveService.roundLeaveBalnc(eligibleValues);
			}
			/*if (clientleavepolicy.getTypeofleave() != null
					&& LeaveTypeConstants.india_Sick.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
				float value = daysDiffFromProbEndToYearEnd * clientleavepolicy.getNumber_days();
				eligibleValue = value;
				//eligibleValue = leaveService.roundLeaveBalnc(value);
			}*/
			leave_Eligibility_Detail = new Leave_Eligibility_Details();
			leave_Eligibility_Detail.setClient_id(clientleavepolicy.getClientId());
			leave_Eligibility_Detail.setType_of_leave(clientleavepolicy.getTypeofleave());
			leave_Eligibility_Detail.setNumber_of_days(eligibleValue);
			leave_Eligibility_Detail.setYear(now.getYear());
			leaveEligibleList.add(leave_Eligibility_Detail);
		}
	} catch (Throwable e) {
		e.printStackTrace();
		throw new Throwable("Failed To Populate Employee Leaves" + e.getMessage());
	}
		return leaveEligibleList;
	}
	
	/**
	 *Author - vinay
	 *if employee probation period is completing next year den it will calculate for only till current year 
	 * **/
	public List<Leave_Eligibility_Details> leaveEligForOnProbationCompletionNotInCurrentYear(LocalDate adoj, List<ClientLeavePolicy> clientPolicy) throws Throwable {
		List<Leave_Eligibility_Details> leaveEligibleList = new ArrayList<Leave_Eligibility_Details>();
		try{
		Leave_Eligibility_Details leave_Eligibility_Detail = null;
		LocalDate now = LocalDate.now();
		LocalDate yearEndDate = now.with(lastDayOfYear());
		int i = 1;
		int addProbationLeave = 0;
		for (i = 1; i <= 3; i++) {
			if (adoj.plusMonths(i).isBefore(yearEndDate)
					|| adoj.plusMonths(i).equals(yearEndDate)) {
				addProbationLeave = addProbationLeave + 1;
			} else {
				break;
			}
		}
		float probationvalue = ChronoUnit.DAYS.between(adoj.plusMonths(addProbationLeave), yearEndDate);
		float res = (probationvalue + 1)/ yearEndDate.getDayOfMonth();
		res = res + (float) addProbationLeave;
		//LeaveServiceImpl leaveService = new LeaveServiceImpl();
		for (ClientLeavePolicy clientleavepolicy : clientPolicy) {
			//float eligibleValue = clientleavepolicy.getNumber_days();
			float eligibleValue = 0;
			if (clientleavepolicy.getTypeofleave() != null
					&& LeaveTypeConstants.india_Annual.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
				eligibleValue = res;
				//eligibleValue = leaveService.roundLeaveBalnc(res);
			}
			/*if (clientleavepolicy.getTypeofleave() != null
					&& LeaveTypeConstants.india_Sick.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
				eligibleValue = 0;
			}*/
			leave_Eligibility_Detail = new Leave_Eligibility_Details();
			leave_Eligibility_Detail.setClient_id(clientleavepolicy.getClientId());
			leave_Eligibility_Detail.setType_of_leave(clientleavepolicy.getTypeofleave());
			leave_Eligibility_Detail.setNumber_of_days(eligibleValue);
			leave_Eligibility_Detail.setYear(now.getYear());
			leaveEligibleList.add(leave_Eligibility_Detail);
		}
	} catch (Throwable e) {
		e.printStackTrace();
		throw new Throwable("Failed To Populate Employee Leaves" + e.getMessage());
	}
		return leaveEligibleList;
	}
	
	/**
	 * this service will run once only every year to reset the leave eligibility and will be triggered manually from hap front end.
	 * Also this service is used to calculate yearly eligibility at the time of new employee creation
	 * creates leave eligibility for Annual leaves based on calculation as :
	 * a) on probation initial 3 months 1 leave each month is given and 2 leaves for remaining months
	 * for non probation employee complete leaves given as client policy
	 * b) other leave type will be zero by default.
	**/
	public List<Leave_Eligibility_Details> getLeaveEligibility(LocalDate startday,List<ClientLeavePolicy> Client_Leave_Policy) throws Throwable {
		List<Leave_Eligibility_Details> leaveEligibleList = new ArrayList<Leave_Eligibility_Details>();
		try {
			LocalDate now = LocalDate.now();
			//LocalDate startday = date.toLocalDate();
			LocalDate yearEndDate = now.with(lastDayOfYear());
			LocalDate yearStartDate = now.with(firstDayOfYear());
			float noOfDaysBetween = ChronoUnit.DAYS.between(startday.plusMonths(3), yearEndDate);
			float daysInDiff = noOfDaysBetween / yearEndDate.getDayOfYear();
			// float eligibleValue = 0;
			Leave_Eligibility_Details leave_Eligibility_Detail = null;
			LeaveServiceImpl leaveService = new LeaveServiceImpl();
			boolean isOnProbation = leaveService.checkProbationPeriod(startday);
			// check for employee status where he is on probation or not
			if (isOnProbation) {
				if (startday.plusMonths(3).isBefore(yearEndDate) || startday.plusMonths(3).equals(yearEndDate)) {
					leaveEligibleList = leaveEligForOnProbationCompletionInCurrentYear(startday,
							Client_Leave_Policy);
				} 
				if(startday.plusMonths(3).isAfter(yearEndDate)) {
					leaveEligibleList = leaveEligForOnProbationCompletionNotInCurrentYear(startday,
							Client_Leave_Policy);
				}
			} else {
				LocalDate probationDate = startday.plusMonths(3);
				if (probationDate.getYear() != now.getYear()) {
					for (ClientLeavePolicy clientleavepolicy : Client_Leave_Policy) {
						float eligibleLeave = 0;
						leave_Eligibility_Detail = new Leave_Eligibility_Details();
						leave_Eligibility_Detail.setClient_id(clientleavepolicy.getClientId());
						leave_Eligibility_Detail.setType_of_leave(clientleavepolicy.getTypeofleave());
						if(LeaveTypeConstants.india_Annual.equalsIgnoreCase(clientleavepolicy.getTypeofleave())){
							eligibleLeave = clientleavepolicy.getNumber_days();
						}
						leave_Eligibility_Detail.setNumber_of_days(eligibleLeave);
						leave_Eligibility_Detail.setYear(now.getYear());
						leaveEligibleList.add(leave_Eligibility_Detail);
					}
				}
				if (probationDate.getYear() == now.getYear()) {
					float res = 0;
					if (startday.getYear() == now.getYear()) {
						res = 3;
					} else {
						int i = 1;
						int addProbationLeave = 0;
						for (i = 1; i <= 3; i++) {
							if (probationDate.minusMonths(i).isAfter(yearStartDate)
									|| probationDate.minusMonths(i).equals(yearStartDate)) {
								addProbationLeave = addProbationLeave + 1;
							} else {
								break;
							}
						}
						float probationvalue = ChronoUnit.DAYS.between(yearStartDate,
								probationDate.minusMonths(addProbationLeave));
						res = (probationvalue + 1)/ yearStartDate.MAX.getDayOfMonth();
						res = res + (float) addProbationLeave;
					}
					for (ClientLeavePolicy clientleavepolicy : Client_Leave_Policy) {
						//float eligibleValue = clientleavepolicy.getNumber_days();
						float eligibleValue = 0;
						if (clientleavepolicy.getTypeofleave() != null
								&& LeaveTypeConstants.india_Annual.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
							float value = daysInDiff * clientleavepolicy.getNumber_days();
							value = value + res;
							eligibleValue =value;
							//eligibleValue = leaveService.roundLeaveBalnc(value);
						}
						/*if (clientleavepolicy.getTypeofleave() != null
								&& LeaveTypeConstants.india_Sick.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
							float value = daysInDiff * clientleavepolicy.getNumber_days();
							eligibleValue =value;
							//eligibleValue = leaveService.roundLeaveBalnc(value);
						}*/
						leave_Eligibility_Detail = new Leave_Eligibility_Details();
						leave_Eligibility_Detail.setClient_id(clientleavepolicy.getClientId());
						leave_Eligibility_Detail.setType_of_leave(clientleavepolicy.getTypeofleave());
						leave_Eligibility_Detail.setNumber_of_days(eligibleValue);
						leave_Eligibility_Detail.setYear(now.getYear());
						leaveEligibleList.add(leave_Eligibility_Detail);
					}
				} 
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Throwable("Failed To Populate Employee Leaves" + e.getMessage());
		}
		return leaveEligibleList;
	}
	
	/**
	 * 	@author vinay 
	 * this service is used at the time of employee creation and 1 leave is given as employee will be in probation
	 * 
	 * NOTE: -  incase in employee update populate for old migrated data the leave usage for accrued will be generated as per date of joining
		but usage record will be created for current month so user  must update the accrued value maually if its incorrect
		and for previous months usage should be added from backend
		**/

	public List<Leave_Usage_Details> newEmployeeLeaveUsage(Timestamp adoj,List<Leave_Eligibility_Details> eligibility) throws Throwable{
		float accruedLeave = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date startdat1 = sdf.parse(sdf.format(new Date()));
	    Timestamp usagemonth = new Timestamp(startdat1.getTime());
		List<Leave_Usage_Details> usageDetails = new ArrayList<Leave_Usage_Details>();
		try {
			java.sql.Date date = new java.sql.Date(adoj.getTime());
			LocalDate month = date.toLocalDate();
			Leave_Usage_Details leave_Usage_Details = null;
				float startDay = month.getDayOfMonth();
				float lastDay = month.MAX.getDayOfMonth();	
				float res = (lastDay-startDay +1)/lastDay;
				//LeaveServiceImpl leaveService = new LeaveServiceImpl();
				//accruedLeave = leaveService.roundLeaveBalnc(res);
				accruedLeave = res;
				Leave_Eligibility_Details elig = (Leave_Eligibility_Details)eligibility.iterator().next();
				leave_Usage_Details = new Leave_Usage_Details();
				leave_Usage_Details.setClient_id(elig.getClient_id());
				leave_Usage_Details.setCreated_by(elig.getCreated_by());
				leave_Usage_Details.setEmployee_id(elig.getEmployee_id());
				leave_Usage_Details.setLeaves_accrued(accruedLeave);
				leave_Usage_Details.setUsageMonth(usagemonth);
				leave_Usage_Details.setType_of_leave(LeaveTypeConstants.india_Annual);
				usageDetails.add(leave_Usage_Details);
		}catch(Exception e){
			e.printStackTrace();
		}
		return usageDetails;
	}
		
	/**
	 * 	@author vinay 
	 * this service runs automatically first of every month.
	 * 
	 * a) on probation initial 3 months 1 leave each month is given and 2 leaves for remaining months
	 * b) for non probation employee 2 leaves will be added every  month
	 * NOTE: -  for old migrated data the leave usage for accrued will be generated as per date of joining
		but usage record will be created for current month so user  must update the accrued value maually if its incorrect
		and for previous months usage should be added from backend
		**/
	public List<Leave_Usage_Details> calcLeaveUsageService(Timestamp adoj,List<Leave_Eligibility_Details> leaveEligibility,List<Leave_Usage_Details> usageList) throws Throwable {
		java.sql.Date date = new java.sql.Date(adoj.getTime());
		LocalDate startday = date.toLocalDate();
		Timestamp currentTimestamp = Timestamp.from(Instant.now());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		java.util.Date currDate = sdf.parse(currentTimestamp.toString());	
		LeaveServiceImpl leaveService = new LeaveServiceImpl();
		boolean isOnProbation = leaveService.checkProbationInSameMonthOrAfter(startday);
		LocalDate prevDate = LocalDate.now().minusMonths(1);
		LocalDate prevMonth = prevDate.with(firstDayOfMonth());
		float prevAnnuaLeaveBalance = 0;
		//float prevSickLeaveBalance = 0;
		float annualEligible = 0;
		float cfLeave = 0;
		//float sickEligible = 0;
		float accruedAnnual = 0;
		//float accruedSick = 0;
		for(Leave_Eligibility_Details eligible :leaveEligibility){
			if(LeaveTypeConstants.india_Annual.equalsIgnoreCase(eligible.getType_of_leave())){
				annualEligible = eligible.getNumber_of_days();
			}
			if(LeaveTypeConstants.india_CF.equalsIgnoreCase(eligible.getType_of_leave())){
				cfLeave = eligible.getNumber_of_days();
			}
			/*if(LeaveTypeConstants.india_Sick.equalsIgnoreCase(eligible.getType_of_leave())){
				sickEligible = eligible.getNumber_of_days();
			}*/
		}
		if(usageList != null){
		for(Leave_Usage_Details usageHistory : usageList ){	
			LocalDateTime dd = usageHistory.getUsageMonth().toLocalDateTime();
					if(prevMonth.equals(dd.toLocalDate()) && prevMonth.getYear() == dd.getYear()){
						if(LeaveTypeConstants.india_Annual.equalsIgnoreCase(usageHistory.getType_of_leave())){
						float prevAccruedleave = usageHistory.getLeaves_accrued();
						float prevAccrUsedLeave = usageHistory.getLeaves_used();
						prevAnnuaLeaveBalance = prevAccruedleave - prevAccrUsedLeave;
						}
						/*if(LeaveTypeConstants.india_Sick.equalsIgnoreCase(usageHistory.getType_of_leave())){
							float prevSickLeave = usageHistory.getLeaves_accrued();
							float prevSickleaveUsed = usageHistory.getLeaves_used();
							prevSickLeaveBalance = prevSickLeave - prevSickleaveUsed;
						}*/
					}
		}}
		LocalDate now = LocalDate.now();
		if (isOnProbation) {
			if(now.getMonthValue() != startday.plusMonths(3).getMonthValue()){
				 accruedAnnual = prevAnnuaLeaveBalance + 1;
			}		
			if(now.getMonthValue() == startday.plusMonths(3).getMonthValue()){
				float noOfDaysBetween = ChronoUnit.DAYS.between(now.with(firstDayOfMonth()), startday.plusMonths(3));
				float daysbeforeProbtion = (noOfDaysBetween + 1)/ now.MAX.getDayOfMonth();
				float noOfdaysafterProb = ChronoUnit.DAYS.between(startday.plusMonths(3),now.with(lastDayOfMonth()));
				float daysAfterProb = (noOfdaysafterProb/now.MAX.getDayOfMonth());
				accruedAnnual = (daysAfterProb * (annualEligible/12)) + daysbeforeProbtion + prevAnnuaLeaveBalance;
				//accruedSick = (daysAfterProb * (sickEligible/12));
			}	
		}else{	
				accruedAnnual = annualEligible/12;
				if(prevAnnuaLeaveBalance != 0){
					accruedAnnual = accruedAnnual + prevAnnuaLeaveBalance;
				}
				/*accruedSick = sickEligible/12;
				if(prevSickLeaveBalance != 0){
					accruedSick = accruedSick + prevSickLeaveBalance;
				}*/
		}
		if(LocalDate.now().getMonthValue() == 1){
			accruedAnnual = accruedAnnual + cfLeave;
		}
		List<Leave_Usage_Details> employeeMonthlyUsage = new ArrayList<Leave_Usage_Details>();
		Leave_Usage_Details annual_usage = new Leave_Usage_Details();
		annual_usage.setCreated_by("HAP");
		//annual_usage.setLeaves_accrued(leaveService.roundLeaveBalnc(accruedAnnual));
		annual_usage.setLeaves_accrued(accruedAnnual);
		annual_usage.setType_of_leave(LeaveTypeConstants.india_Annual);
		annual_usage.setUsageMonth(new Timestamp(currDate.getTime()));
		employeeMonthlyUsage.add(annual_usage);
		
	/*	Leave_Usage_Details sick_usage = new Leave_Usage_Details();
		sick_usage.setCreated_by("HAP");
		//sick_usage.setLeaves_accrued(leaveService.roundLeaveBalnc(accruedSick));
		sick_usage.setLeaves_accrued(accruedSick);
		sick_usage.setType_of_leave(LeaveTypeConstants.india_Sick);
		sick_usage.setMonth(new Timestamp(currDate.getTime()));
		employeeMonthlyUsage.add(sick_usage);*/
		
		return employeeMonthlyUsage;
	}
		

}
