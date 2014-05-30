package es.unileon.ulebank.assets;

import java.util.Calendar;
import java.util.Date;

import es.unileon.ulebank.account.Account;
import es.unileon.ulebank.assets.command.UpdateInterestCommand;
import es.unileon.ulebank.assets.exceptions.LoanException;
import es.unileon.ulebank.assets.financialproducts.InterestRate;
import es.unileon.ulebank.assets.handler.Handler;
import es.unileon.ulebank.assets.support.PaymentPeriod;
import es.unileon.ulebank.command.Command;
import es.unileon.ulebank.handler.GenericHandler;
import es.unileon.ulebank.taskList.Task;
import es.unileon.ulebank.time.Time;

public class VariableLoan extends Loan{
	
	/**
	 * This is the interest rate of credit
	 */
	private InterestRate interestOfRate;
	/**
	 * This is the period of the interst rate is calculate
	 */
	private PaymentPeriod recalcOfInterest;
	/**
	 * this is the day of last racalculate date
	 */
	private Date lastRecalculateInterest;
	
	public VariableLoan(Handler idLoan, double initialCapital, double interest,
			PaymentPeriod paymentPeriod, int amortizationTime, Account account,String description,
			InterestRate interestRate, PaymentPeriod recalcOfInterset)
			throws LoanException {
		super(idLoan, initialCapital, interest, paymentPeriod, amortizationTime,
				account,description);
		
		this.interestOfRate=interestRate;
		this.recalcOfInterest=recalcOfInterset;
		this.setInterest(this.getInterestOfBank()+interestRate.getInterestRate());
		this.lastRecalculateInterest=this.getCreatinngDate();
		this.recalcInterestRate();
	}
	
	public void recalcInterestRate(){
		this.setInterest(this.getInterestOfBank()+this.interestOfRate.getInterestRate());
		UpdateInterestCommand updateinterestCommand=new UpdateInterestCommand(this,(Handler) new GenericHandler("a"));
		Date commandDate=this.forwardDate(this.lastRecalculateInterest, this.recalcOfInterest);
		Task task=new Task(commandDate, (Command) updateinterestCommand);
		this.getTaskList().addTask(task);
	}

}
