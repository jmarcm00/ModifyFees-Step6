package es.unileon.ulebank.assets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import es.unileon.ulebank.account.Account;
import es.unileon.ulebank.assets.exceptions.LoanException;
import es.unileon.ulebank.assets.financialproducts.InterestRate;
import es.unileon.ulebank.assets.handler.Handler;
import es.unileon.ulebank.assets.history.LoanHistory;
import es.unileon.ulebank.assets.iterator.LoanIterator;
import es.unileon.ulebank.assets.iterator.LoanIteratorDates;
import es.unileon.ulebank.assets.strategy.loan.FrenchMethod;
import es.unileon.ulebank.assets.strategy.loan.ScheduledPayment;
import es.unileon.ulebank.assets.strategy.loan.StrategyLoan;
import es.unileon.ulebank.assets.support.PaymentPeriod;
import es.unileon.ulebank.exceptions.TransactionException;
import es.unileon.ulebank.fees.FeeStrategy;
import es.unileon.ulebank.fees.InvalidFeeException;
import es.unileon.ulebank.fees.LoanCommission;
import es.unileon.ulebank.history.GenericTransaction;
import es.unileon.ulebank.history.Transaction;
import es.unileon.ulebank.taskList.TaskList;
import es.unileon.ulebank.time.Time;

// TODO PREGUNTAR A CAMINO COMO ACTUALIZAR DEBT CUANDO PASIVOS REALIZA EL PAGO DE LA CUOTA

public class Loan implements FinancialProduct {
	/**
	 * Type of time period used for the effective interest
	 */
	private PaymentPeriod paymentPeriod;

	/**
	 * Interest applicated to the loan
	 */
	private double interest;

	/**
	 * Number of fees to resolve the loan
	 */
	private int amortizationTime;

	/**
	 * Amount of money required for the user
	 */
	private double initialCapital;

	/**
	 * Amount of money that the user have not payed yet
	 */
	private double debt;

	/**
	 * Commission applicated in the case of the client do not pay the fee in the
	 * correct time
	 */
	private double delayedPaymentInterest;

	/**
	 * Unique identificator for the loan
	 */
	private Handler idLoan;

	/**
	 * Strategy used for calculate the payments
	 */
	private StrategyLoan strategy;

	/**
	 * Money that you have already payed
	 */
	private double amortized;

	/**
	 * Arraylist where you store the fees with all data
	 */
	private ArrayList<ScheduledPayment> payments;

	/**
	 * Commisions that you have in the contract
	 */
	/**
	 * Commission that you applied if the owner cancel the loan
	 */
	private FeeStrategy cancelCommission;
	/**
	 * Commission applied when the bank studied the account and other things
	 */
	private FeeStrategy studyCommission;
	/**
	 * Commission applied for open a loan
	 */
	private FeeStrategy openningCommission;
	/**
	 * Commission applied if the owner decides modify the loan contract during
	 * the loan
	 */
	private FeeStrategy modifyCommission;
	/**
	 * Commission applied if the owner decides to amortize some part of the loan
	 */
	private FeeStrategy amortizedCommission;

	/**
	 * Account where we must charge the different payments of the loan
	 */
	private Account account;

	/**
	 * List where we store the payments for every loans
	 */

	private LoanHistory loanHistory;

	/*
	 * internal index used to have the possibility to change the arraylist of
	 * the payments
	 */
	private int arrayListIndex;

	/*
	 * Fixed fee that you have to pay every month
	 */
	private double periodFee;
	
	/**
	 * this is the task lisk for put the commands
	 */
	private TaskList taskList;
	/**
	 * This is the date that the loan is created
	 */
	private Date creatinngDate;
	/**
	 * This is the interest of the bank
	 */

	private double interestOfBank;
	
	/**
	 * This is the loan description for the client
	 */
	private String description;
	
	public Loan(double money, double interest, PaymentPeriod paymentPeriod, int numFees){
		this.debt = money;
		this.interest = interest;
		this.paymentPeriod = paymentPeriod;
		this.amortizationTime = numFees;
		
		this.loanHistory = new LoanHistory();
		try {
			this.cancelCommission = new LoanCommission(0, false);
			this.studyCommission = new LoanCommission(0, false);
			this.cancelCommission = new LoanCommission(0, false);
			this.modifyCommission = new LoanCommission(0, false);
			this.openningCommission = new LoanCommission(0, false);
			this.amortizedCommission = new LoanCommission(0, false);
		} catch (InvalidFeeException e) {
			
		}
		
		this.payments = new ArrayList<ScheduledPayment>();
		this.initialCapital = this.debt;
		this.strategy = new FrenchMethod(this);
		this.account = account;
		this.payments = this.strategy.doCalculationOfPayments();
		this.loanHistory.addAllPayments(this.payments);
		this.arrayListIndex = 0;
		
		this.creatinngDate=Calendar.getInstance().getTime();
		this.debt = this.openningCommission.getFee(this.debt);
		this.debt = this.studyCommission.getFee(this.debt);
		this.taskList=new TaskList();
	}
	
	public Loan(){
		
	}

	/**
	 * Constructor of the class
	 * 
	 * @param idLoan
	 * @param initialCapital
	 * @param interest
	 * @param paymentPeriod
	 * @param amortizationTime
	 * @param account
	 * @throws LoanException
	 */
	public Loan(Handler idLoan, double initialCapital, double interest,
			PaymentPeriod paymentPeriod, int amortizationTime, Account account, String description)
			throws LoanException {
		StringBuffer exceptionMessage = new StringBuffer();

		this.loanHistory = new LoanHistory();
		try {
			this.cancelCommission = new LoanCommission(0, false);
			this.studyCommission = new LoanCommission(0, false);
			this.cancelCommission = new LoanCommission(0, false);
			this.modifyCommission = new LoanCommission(0, false);
			this.openningCommission = new LoanCommission(0, false);
			this.amortizedCommission = new LoanCommission(0, false);
		} catch (InvalidFeeException e) {
			exceptionMessage.append("Commission is marformed.");
		}

		this.idLoan = idLoan;


		if (initialCapital < 100000000) {
			this.debt = initialCapital;
		}else{
			exceptionMessage.append("The bank can not lend this amount of money");
		}
		

		if (interest >= 0 && interest <= 1) {
			this.interest = interest;
			this.setInterestOfBank(interest);
		} else {
			exceptionMessage
					.append("The interest value must be a value between 0 and 1\n");
		}
		

		this.paymentPeriod = paymentPeriod;
		this.amortizationTime = amortizationTime;
		this.payments = new ArrayList<ScheduledPayment>();
		this.initialCapital = this.debt;
		this.strategy = new FrenchMethod(this);
		this.account = account;
		this.description = description;
		this.payments = this.strategy.doCalculationOfPayments();
		this.loanHistory.addAllPayments(this.payments);
		this.arrayListIndex = 0;

		if (exceptionMessage.length() > 1)
			throw new LoanException(exceptionMessage.toString());

		
		Calendar.getInstance().setTimeInMillis(Time.getInstance().getTime());
		this.creatinngDate=Calendar.getInstance().getTime();
		this.debt = this.openningCommission.getFee(this.debt);
		this.debt = this.studyCommission.getFee(this.debt);
		this.taskList=new TaskList();
		
	}

	/**
	 * 
	 * @param idLoan
	 * @param initialCapital
	 * @param interestRate
	 * @param paymentPeriod
	 * @param amortizationTime
	 * @param account
	 * @throws LoanException
	 */
	public Loan(Handler idLoan, double initialCapital,
			InterestRate interestRate, PaymentPeriod paymentPeriod,
			int amortizationTime, Account account,String description) throws LoanException {
		this(idLoan, initialCapital, interestRate.getInterestRate(),
				paymentPeriod, amortizationTime, account,description);

	}

	/**
	 * This method can forward the actual date
	 * 
	 * @param date
	 * @param paymentPeriod
	 * @return The new simulated date
	 */
	/* TODO puede ser cambiado a otra clase utils */
	public Date forwardDate(Date date, PaymentPeriod paymentPeriod) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);// reset the parameter

		int month = calendar.get(Calendar.MONTH) + paymentPeriod.getPeriod();
		int year = calendar.get(Calendar.YEAR);
		int day = calendar.get(Calendar.DATE);
		if (month > 12) {
			++year;
		}
		month = month % 12 + 1;

		calendar.set(year, month, day);

		return calendar.getTime();
	}

	/**
	 * This method returns an ArrayList with all fees of the loan
	 * 
	 * @return payments The arraylist with fees
	 */
	public ArrayList<ScheduledPayment> calcPayments() {
		this.payments = this.strategy.doCalculationOfPayments();
		return this.payments;
	}

	/**
	 * This method allows to know what is the price that one person must pay if
	 * he decide cancel the loan. This amount of money is the total amount of
	 * money for cancel the loan
	 * 
	 * @return double with amount of money to pay
	 * @throws LoanException
	 */
	public double cancelLoan() throws LoanException {
		StringBuffer msgException = new StringBuffer();
		double feeCancel = 0;

		feeCancel = this.cancelCommission.getFee(this.debt);

		// We carry out the transaction to discount the money from the account
		// of the customer
		try {
			if (!(this.account.getBalance() < this.debt)) {
				Transaction transactionCharge = new GenericTransaction(
						feeCancel, new Date(Time.getInstance().getTime()),
						"cancel loan");

				transactionCharge.setEffectiveDate(new Date(Time.getInstance()
						.getTime()));
				this.account.doTransaction(transactionCharge);
			} else {
				msgException.append("not enough money");
			}
		} catch (TransactionException transactionException) {
			msgException.append("Transaction error.\n");
			msgException.append(transactionException.getMessage());
		}

		if (msgException.length() > 0) {
			throw new LoanException(msgException.toString());
		}

		// If the exception is not launched the payment is made and will zero
		// debt.
		this.debt = 0;

		return feeCancel;

	}

	/**
	 * Method that applies the delayed interest if some fee has not been payed
	 * in time
	 */
	public void delayedPayment() {
		boolean isPaid = isNotPaid();
		if (isPaid && this.debt > 0)
			this.debt = this.debt + this.debt * delayedPaymentInterest;

	}

	/**
	 * Method that is necesary when the interest change
	 */
	@Override
	public void update() {
		this.payments = this.strategy.doCalculationOfPayments();
		this.loanHistory.addAllPayments(this.payments);
	}

	/**
	 * Method that allows to amortize some money before the loan finish
	 * 
	 * @param quantity
	 *            Amount of money that you want to amortize
	 * @return (double) amount of money that you have to pay for amortize
	 * @throws LoanException
	 */

	public double amortize(double quantity) throws LoanException {
		StringBuffer exceptionMessage = new StringBuffer();
		double comission = 0;

		if (!(quantity <= this.debt)) {
			exceptionMessage
					.append("The money to amortize is more than the debt!");
		}

		if (exceptionMessage.length() > 0) {
			throw new LoanException(exceptionMessage.toString());
		}

		// We carry out the transaction to discount the money from the account
		// of the customer.
		try {
			if (!(this.account.getBalance() < this.debt)) {
				Transaction transactionCharge = new GenericTransaction(
						quantity, new Date(Time.getInstance().getTime()),
						"liquidate a quantity");
				transactionCharge.setEffectiveDate(new Date(Time.getInstance()
						.getTime()));
				this.account.doTransaction(transactionCharge);
			} else {
				exceptionMessage.append("not enough money");
			}
		} catch (TransactionException transactionException) {
			exceptionMessage.append("Transaction error.\n");
			exceptionMessage.append(transactionException.getMessage());
		}

		// If the transaction is unsuccessful we launched the exception.
		if (exceptionMessage.length() > 0) {
			throw new LoanException(exceptionMessage.toString());
		}

		// Si la transaccion se realizo con exito descontamos el dinero de la
		// deuda
		this.debt = this.amortizedCommission.getFee(this.debt);
		this.debt -= quantity;

		setAmortized(this.initialCapital - this.debt);
		update();

		return comission;
	}

	public PaymentPeriod getPaymentPeriod() {
		return paymentPeriod;
	}

	public void setPaymentPeriod(PaymentPeriod paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}

	public double getInterest() {
		return this.interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public int getAmortizationTime() {
		return amortizationTime;
	}

	public void setAmortizationTime(int amortizationTime) {
		this.amortizationTime = amortizationTime;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmountOfMoney() {
		return initialCapital;
	}

	public void setAmountOfMoney(double amountOfMoney) {
		this.initialCapital = amountOfMoney;
	}

	public double getDebt() {
		return this.debt;
	}

	public void setDebt(double debt) {
		this.debt = debt;
		update();
	}

	public double getDelayedPaymentInterest() {
		return this.delayedPaymentInterest;
	}

	public void setDelayedPaymentInterest(double delayedPaymentInterest) {
		this.delayedPaymentInterest = delayedPaymentInterest;
	}

	public StrategyLoan getStrategy() {
		return this.strategy;
	}

	public void setStrategy(StrategyLoan strategy) {
		this.strategy = strategy;
		update();
	}

	@Override
	public Handler getId() {
		return this.idLoan;
	}

	public void setId(Handler idLoan) {
		this.idLoan = idLoan;
	}

	public ArrayList<ScheduledPayment> getPayments() {
		return this.payments;
	}

	/**
	 * Method used to paying the fee if payment is not made
	 * 
	 * @param index
	 *            indicates the number of payments to be amortized
	 */
	@Deprecated
	public void paid(int index) { // Este metodo se borrara asiq no lo useis
		if (index >= 0 && index < payments.size()) {
			ScheduledPayment payment = payments.get(index);
			if (!payment.isPaid()) {
				this.debt -= payment.getAmortization();
				payment.setPaid(true);
			}
		}
	}

	/**
	 * Method used to pay the payment by an id handler
	 * 
	 * @param handlerId
	 *            is the handler of the payment
	 * @throws LoanException
	 */
	public void paid(Handler handlerId) throws LoanException {
		StringBuffer exceptionMessage = new StringBuffer();

		// we look for the payment
		boolean found = false;
		ScheduledPayment payment = null;
		for (int i = 0; i < this.payments.size() && !found; i++) {
			payment = this.payments.get(i);
			if (payment.getId().compareTo(handlerId) == 0) {
				found = true;
			}
		}

		if (payment != null && !payment.isPaid()) {

			// we do the transaction
			try {
				Transaction transaction = new GenericTransaction(
						payment.getImportOfTerm(), new Date(Time.getInstance()
								.getTime()), "payment");

				transaction.setEffectiveDate(new Date(Time.getInstance()
						.getTime()));

				this.account.doTransaction(transaction);

			} catch (TransactionException e) {
				exceptionMessage.append("Transaction error.\n");
			}

			// if the transaction has not errors and was made successfully
			if (exceptionMessage.length() == 0) {
				// we subtract the quantity to amortize of the debt
				this.debt -= payment.getAmortization();
				payment.setPaid(true);
			} else {
				throw new LoanException(
						"The payment has not been made successfully.");
			}
		}
	}

	/**
	 * Method that returns true if any month has not been paid.
	 * 
	 * @return true if it has not paid any month, false if all were paid
	 */
	public boolean isNotPaid() {
		boolean isNotPaid = false;

		for (int i = 0; i < this.payments.size() && !isNotPaid; i++) {
			ScheduledPayment payment = this.payments.get(i);
			if (!payment.isPaid()) {
				isNotPaid = true;
			}
		}

		return isNotPaid;
	}

	public double getAmortized() {
		return amortized;
	}

	public void setAmortized(double amortized) {
		this.amortized = amortized;
	}

	public LoanIteratorDates iterator(Date startDate, Date endDate) {
		return new LoanIteratorDates(this.payments, startDate, endDate);
	}

	public LoanIterator iterator() {
		return new LoanIterator(this.payments);
	}

	// TODO MAKE THE DOC IN ENGLISH OF THIS METHOD PLEASE. Not put your ideas
	public void makeNormalPayment(double amount) {
		// lanzo alguna excepcion o que?
		// pongo la condicion de que el pago se haga entre los meses indicados?
		if (amount == periodFee && payments.size() > 0
				&& arrayListIndex < payments.size()) {
			ScheduledPayment hesGonnaPay = this.payments.get(arrayListIndex);
			hesGonnaPay.setPaid(true);
			this.debt = debt - amount;
			// pongo la fecha de hoy, pero deberia dejar que se le pase por
			// parametro?
			hesGonnaPay.setPaymentDate(new Date());
			arrayListIndex++;
		}

	}

	// TODO MAKE THE DOC IN ENGLISH OF THIS METHOD PLEASE. Not put your ideas

	// metodo de pago de cantidades diferentes a la mensual calculada
	public void makeAbnormalPayment(double amount) {
		// excepciones
		// pongo la condicion de que el pago se haga entre los meses indicados?
		if (amount < this.debt && amount > 0) {
			ScheduledPayment hesGonnaPay = this.payments.get(arrayListIndex);

			double interest = 0;
			double amortized = 0;
			double totalLoan = this.debt;
			double totalCapital = this.debt;

			interest = amount * this.interest;
			amortized = amount - interest;
			if (totalLoan > amount) {
				totalLoan -= amount;
			} else {
				totalLoan = 0;
			}
			totalCapital = round(totalLoan, 100);
			amortized = round(amortized, 100);
			interest = round(interest, 100);
			hesGonnaPay.setAmortization(amortized);
			hesGonnaPay.setInterests(interest);
			hesGonnaPay.setOutstandingCapital(totalCapital);
			// Cambiar
			hesGonnaPay.setPaymentDate(new Date());

			hesGonnaPay.setPaid(true);
			// hesGonnaPay.setOutstandingCapital(outstandingCapital);
			this.debt = debt - amount;
			hesGonnaPay.setImportOfTerm(amount);
		}

		// borro todos los elementos en adelante porque hay que recalcular
		int auxSize = this.payments.size();
		for (int auxInd = arrayListIndex + 1; auxInd < auxSize; auxInd++) {
			this.payments.remove(this.payments.get(this.payments.size() - 1));
		}
		// se recalcula todo
		this.strategy.doCalculationOfPayments();
		// actualizo el indice del arrayList
		++arrayListIndex;

	}

	/**
	 * Calculate the amount of money of one payment
	 * 
	 * @return fee Money to pay in one payment
	 */
	public double calculateMonthlyFee() {
		double fee = 0;
		double interesEf = this.calculateEffectiveInterestRate();
		int numFee = (this.getAmortizationTime() / this.calculatePayment());
		double fracc = ((Math.pow((1 + interesEf), numFee)) * interesEf)
				/ (Math.pow(1 + interesEf, numFee) - 1);
		fee = this.getAmountOfMoney() * fracc;
		return fee;
	}

	/**
	 * Method used to invert the payment period for calculated the total number
	 * of payments for repayment the loan
	 * 
	 * @return Integer with the value of number of payments in one year
	 */
	private int calculatePayment() {
		int num = this.getPaymentPeriod().getPeriod();
		if (num == 12)
			return 1;
		else if (num == 6)
			return 2;
		else if (num == 4)
			return 3;
		else if (num == 2)
			return 6;
		else if (num == 1)
			return 12;
		return 0;
	}

	/**
	 * Method used to calculating the effective interest of do the fees
	 * 
	 * @return Double with the value of this effective interest
	 */
	public double calculateEffectiveInterestRate() {
		return Math.pow(1 + (this.interest / this.paymentPeriod.getPeriod()),
				this.paymentPeriod.getPeriod()) - 1;
	}

	/**
	 * Method used to round some numbers for the payments. This method allow us
	 * to be more exactly in the calcs
	 * 
	 * @param num
	 * @param factor
	 * @return num Number rounded
	 */

	public double round(double num, int factor) {
		num = num * factor;
		num = Math.round(num);
		num = num / factor;
		return num;
	}

	public double getPeriodFee() {
		return periodFee;
	}

	public Account getLinkedAccount() {
		return this.account;
	}

	public void setCancelCommission(FeeStrategy commission) {
		this.cancelCommission = commission;
	}

	public void setStudyCommission(FeeStrategy commission) {
		this.studyCommission = commission;
		this.debt = this.studyCommission.getFee(this.debt);
		update();
	}

	public void setOpenningCommission(FeeStrategy commission) {
		this.openningCommission = commission;
		this.debt = this.openningCommission.getFee(this.debt);
		update();
	}

	public void setAmortizedCommission(FeeStrategy commission) {
		this.amortizedCommission = commission;
	}

	public void setModifyCommission(FeeStrategy commission) {
		this.modifyCommission = commission;
	}

	public double getInterestOfBank() {
		return interestOfBank;
	}

	public void setInterestOfBank(double interestOfBank) {
		this.interestOfBank = interestOfBank;
	}

	public Date getCreatinngDate() {
		return creatinngDate;
	}

	public void setCreatinngDate(Date creatinngDate) {
		this.creatinngDate = creatinngDate;
	}

	public TaskList getTaskList() {
		return taskList;
	}

	public void setTaskList(TaskList taskList) {
		this.taskList = taskList;
	}

}
