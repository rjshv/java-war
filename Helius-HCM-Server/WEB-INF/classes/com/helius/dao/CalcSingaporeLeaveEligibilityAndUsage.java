package com.helius.dao;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.helius.entities.Leave_Eligibility_Details;
import com.helius.entities.Leave_Usage_Details;
import com.helius.utils.ClientLeavePolicy;

public class CalcSingaporeLeaveEligibilityAndUsage {

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
			//LeaveServiceImpl leaveService = new LeaveServiceImpl();
			for(Leave_Eligibility_Details elig : eligibility){
				if(LeaveTypeConstants.singapore_Annual.equalsIgnoreCase(elig.getType_of_leave())){
					//float startDay = month.getDayOfMonth();
					//float lastDay = month.MAX.getDayOfMonth();	
					accruedLeave = elig.getNumber_of_days();
					//float res = (lastDay-startDay +1)/lastDay;
					//accruedLeave = res * elig.getNumber_of_days();
					//accruedLeave = leaveService.roundLeaveBalnc(res);
					leave_Usage_Details = new Leave_Usage_Details();
					leave_Usage_Details.setClient_id(elig.getClient_id());
					leave_Usage_Details.setCreated_by(elig.getCreated_by());
					leave_Usage_Details.setEmployee_id(elig.getEmployee_id());
					leave_Usage_Details.setLeaves_accrued(accruedLeave);
					leave_Usage_Details.setUsageMonth(usagemonth);
					leave_Usage_Details.setType_of_leave(elig.getType_of_leave());
					usageDetails.add(leave_Usage_Details);
				}
				if(LeaveTypeConstants.singapore_Sick.equalsIgnoreCase(elig.getType_of_leave())){
					//float startDay = month.getDayOfMonth();
					//float lastDay = month.MAX.getDayOfYear();
					accruedLeave = elig.getNumber_of_days();
					//float res = (lastDay-startDay +1)/lastDay;
					//accruedLeave = res * elig.getNumber_of_days();
					//accruedLeave = leaveService.roundLeaveBalnc(res);
					leave_Usage_Details = new Leave_Usage_Details();
					leave_Usage_Details.setClient_id(elig.getClient_id());
					leave_Usage_Details.setCreated_by(elig.getCreated_by());
					leave_Usage_Details.setEmployee_id(elig.getEmployee_id());
					leave_Usage_Details.setLeaves_accrued(accruedLeave);
					leave_Usage_Details.setUsageMonth(usagemonth);
					leave_Usage_Details.setType_of_leave(elig.getType_of_leave());
					usageDetails.add(leave_Usage_Details);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return usageDetails;
	}
	
	public float calculateSickLeave(LocalDate adoj, float clientSickLeave) throws Throwable {
		LocalDate currentDate = LocalDate.now();
		LocalDate After3MOnths = adoj.plusMonths(3);
		LocalDate After4MOnths = adoj.plusMonths(4);
		LocalDate After5MOnths = adoj.plusMonths(5);
		LocalDate After6MOnths = adoj.plusMonths(6);
		float totalSickLeave = 0;
		if((After3MOnths.isBefore(currentDate) || (After3MOnths.isEqual(currentDate)) && 
				(After3MOnths.isBefore(After4MOnths)))){
			totalSickLeave = LeaveTypeConstants.After3MOnths;
		}
		if((After4MOnths.isBefore(currentDate) || (After4MOnths.isEqual(currentDate)) && 
				(After4MOnths.isBefore(After5MOnths)))){
			totalSickLeave = LeaveTypeConstants.After4MOnths;
		}
		if((After5MOnths.isBefore(currentDate) || (After5MOnths.isEqual(currentDate)) && 
				(After4MOnths.isBefore(After6MOnths)))){
			totalSickLeave = LeaveTypeConstants.After5MOnths;
		}
		if(After6MOnths.isBefore(currentDate) || After6MOnths.isEqual(currentDate)){
			totalSickLeave = LeaveTypeConstants.After6MOnths;
		}
		return totalSickLeave;
	}
	
	/**
	 * @author vinay
	 * 
	 * this service is used to calculate yearly eligibility also used for new employee eligibility calculation
	 * creates leave eligibility for Annual and sick leave based on prorate calculation and other leave type will be zero by default
	a) if employee ADOJ is of previous year then employee eligible for full annual leaves
		and sick leave will be calculated based on calculation as per 3,4,5,6 months
	b) if ADOJ is in same year  then annual leave eligibility will be calculated based on pro rated data and 
		sick leave is calculated as per months calculation.
	 * **/
	public List<Leave_Eligibility_Details> getLeaveEligibility(LocalDate Adoj,List<ClientLeavePolicy> Client_Leave_Policy) throws Throwable {
		List<Leave_Eligibility_Details> leaveEligibleList = new ArrayList<Leave_Eligibility_Details>();
		try {
			LocalDate now = LocalDate.now();
			LocalDate yearEndDate = now.with(lastDayOfYear());
			float noOfDaysBetween = ChronoUnit.DAYS.between(Adoj, yearEndDate);
			float daysInDiff = (noOfDaysBetween + 1)/ yearEndDate.getDayOfYear();
			Leave_Eligibility_Details leave_Eligibility_Detail = null;
			LeaveServiceImpl leaveService = new LeaveServiceImpl();
				if (Adoj.getYear() != now.getYear()) {
					for (ClientLeavePolicy clientleavepolicy : Client_Leave_Policy) {
						float eligibleValue = 0;
						leave_Eligibility_Detail = new Leave_Eligibility_Details();
						leave_Eligibility_Detail.setClient_id(clientleavepolicy.getClientId());
						leave_Eligibility_Detail.setType_of_leave(clientleavepolicy.getTypeofleave());
						if (clientleavepolicy.getTypeofleave() != null
								&& LeaveTypeConstants.singapore_Annual.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
							eligibleValue = clientleavepolicy.getNumber_days();
							eligibleValue = leaveService.roundLeaveBalnc(eligibleValue);
						}
						if(clientleavepolicy.getTypeofleave() != null &&
								LeaveTypeConstants.singapore_Sick.equalsIgnoreCase(clientleavepolicy.getTypeofleave())){
							float eligiblesickLeav = calculateSickLeave(Adoj,clientleavepolicy.getNumber_days());
							/*LocalDate After6MOnths = Adoj.plusMonths(6);
							if(After6MOnths.isAfter(now) || After6MOnths.isEqual(now)){
								eligiblesickLeav = clientleavepolicy.getNumber_days();
							}
							*/
							eligibleValue = leaveService.roundLeaveBalnc(eligiblesickLeav);						}
						leave_Eligibility_Detail.setNumber_of_days(eligibleValue);
						leave_Eligibility_Detail.setYear(now.getYear());
						leaveEligibleList.add(leave_Eligibility_Detail);
					}
				}
				if (Adoj.getYear() == now.getYear()) {									
					for (ClientLeavePolicy clientleavepolicy : Client_Leave_Policy) {
						float eligibleValue = 0;
						float joinedMonthVal = 0;
						if (clientleavepolicy.getTypeofleave() != null
								&& LeaveTypeConstants.singapore_Annual.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
							float value = daysInDiff * clientleavepolicy.getNumber_days();
							eligibleValue = value;
							eligibleValue = leaveService.roundLeaveBalnc(value);
							System.out.println("===sing annual as per old cal======"+eligibleValue);
						}
						if (clientleavepolicy.getTypeofleave() != null
								&& LeaveTypeConstants.singapore_Annual.equalsIgnoreCase(clientleavepolicy.getTypeofleave())) {
							float monthlyVal = clientleavepolicy.getNumber_days()/12;
							int numberOFRemainingMonths = 12 - Adoj.getMonthValue();
							float remainingMonthsVal = monthlyVal * numberOFRemainingMonths;
							if(Adoj.getDayOfMonth()<15){
								joinedMonthVal = monthlyVal;
							}else{
								joinedMonthVal = monthlyVal/2;
							}
							float totalEligibleValue = joinedMonthVal + remainingMonthsVal;
							eligibleValue = leaveService.roundLeaveBalnc(totalEligibleValue);
							System.out.println("===sing annual as per new cal======"+eligibleValue);
						}
						if(clientleavepolicy.getTypeofleave() != null &&
								LeaveTypeConstants.singapore_Sick.equalsIgnoreCase(clientleavepolicy.getTypeofleave())){
							float eligiblesickLeav = calculateSickLeave(Adoj,clientleavepolicy.getNumber_days());
							/*LocalDate After6MOnths = Adoj.plusMonths(6);
							if(After6MOnths.isAfter(now) || After6MOnths.isEqual(now)){
								eligiblesickLeav = clientleavepolicy.getNumber_days();
							}*/
							eligibleValue = leaveService.roundLeaveBalnc(eligiblesickLeav);
						}
						leave_Eligibility_Detail = new Leave_Eligibility_Details();
						leave_Eligibility_Detail.setClient_id(clientleavepolicy.getClientId());
						leave_Eligibility_Detail.setType_of_leave(clientleavepolicy.getTypeofleave());
						leave_Eligibility_Detail.setNumber_of_days(eligibleValue);
						leave_Eligibility_Detail.setYear(now.getYear());
						leaveEligibleList.add(leave_Eligibility_Detail);
					}
				} 
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Throwable("Failed To Populate Employee Leaves" + e.getMessage());
		}
		return leaveEligibleList;
	}
	
	
	public List<Leave_Usage_Details> calcLeaveUsageService(Timestamp adoj,List<Leave_Eligibility_Details> leaveEligibility,List<Leave_Usage_Details> usageList) throws Throwable {
		java.sql.Date date = new java.sql.Date(adoj.getTime());
		Timestamp currentTimestamp = Timestamp.from(Instant.now());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		java.util.Date currDate = sdf.parse(currentTimestamp.toString());
		LocalDate prevDate = LocalDate.now().minusMonths(1);
		LocalDate prevMonth = prevDate.with(firstDayOfMonth());
		float prevAnnuaLeaveBalance = 0;
		float prevSickLeaveBalance = 0;
		float annualEligible = 0;
		float sickEligible = 0;
		float accruedAnnual = 0;
		float accruedSick = 0;
		float cfLeave = 0;
		LeaveServiceImpl leaveService = new LeaveServiceImpl();
		for(Leave_Eligibility_Details eligible :leaveEligibility){
			if(LeaveTypeConstants.singapore_Annual.equalsIgnoreCase(eligible.getType_of_leave())){
				annualEligible = eligible.getNumber_of_days();
			}
			if(LeaveTypeConstants.singapore_Sick.equalsIgnoreCase(eligible.getType_of_leave())){
				sickEligible = eligible.getNumber_of_days();
			}
			if(LeaveTypeConstants.singapore_CF.equalsIgnoreCase(eligible.getType_of_leave())){
				cfLeave = eligible.getNumber_of_days();
			}
		}
		if(usageList != null && !usageList.isEmpty()){
		for(Leave_Usage_Details usageHistory : usageList ){	
			LocalDateTime dd = usageHistory.getUsageMonth().toLocalDateTime();
					if(prevMonth.equals(dd.toLocalDate()) && prevMonth.getYear() == dd.getYear()){
						if(LeaveTypeConstants.singapore_Annual.equalsIgnoreCase(usageHistory.getType_of_leave())){
						float prevAccruedleave = usageHistory.getLeaves_accrued();
						float prevAccrUsedLeave = usageHistory.getLeaves_used();
						prevAnnuaLeaveBalance = prevAccruedleave - prevAccrUsedLeave;
						}
						if(LeaveTypeConstants.singapore_Sick.equalsIgnoreCase(usageHistory.getType_of_leave())){
							float prevSickLeave = usageHistory.getLeaves_accrued();
							float prevSickleaveUsed = usageHistory.getLeaves_used();
							prevSickLeaveBalance = prevSickLeave - prevSickleaveUsed;
						}
					}
		}
		}
		
		if(LocalDate.now().getMonthValue() == 1){
			accruedAnnual = annualEligible + cfLeave;
			accruedSick = sickEligible;
		}
		
		if(LocalDate.now().getMonthValue() != 1){
			accruedAnnual = prevAnnuaLeaveBalance;
			accruedSick = prevSickLeaveBalance;
		}
		List<Leave_Usage_Details> employeeMonthlyUsage = new ArrayList<Leave_Usage_Details>();
		Leave_Usage_Details annual_usage = new Leave_Usage_Details();
		annual_usage.setCreated_by("HAP");
		annual_usage.setLeaves_accrued(leaveService.roundLeaveBalnc(accruedAnnual));
		annual_usage.setLeaves_accrued(accruedAnnual);
		annual_usage.setType_of_leave(LeaveTypeConstants.singapore_Annual);
		annual_usage.setUsageMonth(new Timestamp(currDate.getTime()));
		employeeMonthlyUsage.add(annual_usage);

		Leave_Usage_Details sick_usage = new Leave_Usage_Details();
		sick_usage.setCreated_by("HAP");
		sick_usage.setLeaves_accrued(leaveService.roundLeaveBalnc(accruedSick));
		sick_usage.setLeaves_accrued(accruedSick);
		sick_usage.setType_of_leave(LeaveTypeConstants.singapore_Sick);
		sick_usage.setUsageMonth(new Timestamp(currDate.getTime()));
		employeeMonthlyUsage.add(sick_usage);
		return employeeMonthlyUsage;
	}
		
}
