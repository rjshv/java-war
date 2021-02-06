package com.helius.dao;

import java.util.List;

import com.helius.entities.Sow_Billing_Schedule;
import com.helius.entities.Sow_Details;

public class Sow {
	private Sow_Details sowdetails;
	private List<Sow_Billing_Schedule> deleteSowBillingSchedule;

	public Sow_Details getSowdetails() {
		return sowdetails;
	}

	public void setSowdetails(Sow_Details sowdetails) {
		this.sowdetails = sowdetails;
	}

	public List<Sow_Billing_Schedule> getDeleteSowBillingSchedule() {
		return deleteSowBillingSchedule;
	}

	public void setDeleteSowBillingSchedule(List<Sow_Billing_Schedule> deleteSowBillingSchedule) {
		this.deleteSowBillingSchedule = deleteSowBillingSchedule;
	}


}
