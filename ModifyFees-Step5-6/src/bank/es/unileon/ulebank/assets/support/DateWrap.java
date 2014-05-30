package es.unileon.ulebank.assets.support;

import java.util.Calendar;
import java.util.Date;

public class DateWrap {
	private Calendar calendar;
	private Date date;
	private int time;
	
	public DateWrap(Date startDate, PaymentPeriod periodOfTime) {
		this.calendar = Calendar.getInstance();
		this.calendar.setTime(startDate);
		this.time = periodOfTime.getTime();
		this.date = startDate;
	}
	
	public DateWrap(int year, int month, int day, PaymentPeriod periodOfTime) {
		this.calendar = Calendar.getInstance();
		this.calendar.set(year, month, day);
		this.time = periodOfTime.getTime();
		this.date = this.calendar.getTime();
	}
	
	public Date updateDate(){
		this.calendar.add(Calendar.MONTH, this.time);
		this.date = this.calendar.getTime();
		return this.date;
		
	}
	
	public Date getDate() {
		return this.date;
	}
	
	@Override
	public String toString() {
		return this.calendar.getTime().toString();
	}
	
	public void setPeriodOfTime(PaymentPeriod periodOfTime) {
		this.time = periodOfTime.getTime();
	}
	
}
