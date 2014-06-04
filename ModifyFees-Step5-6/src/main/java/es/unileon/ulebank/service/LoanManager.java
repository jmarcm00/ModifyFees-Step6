package es.unileon.ulebank.service;

import java.io.Serializable;
import java.util.List;

import es.unileon.ulebank.assets.Loan;
import es.unileon.ulebank.domain.Product;

/**
 * Fee Manager Interface
 * 
 * @brief Interface for the fee managers
 */
public interface LoanManager extends Serializable {

	/**
	 * Method that changes the number of fees from feeLimits.jsp
	 * 
	 * @param numFees
	 */
	public void setNumberOfFees(int percentage);

	/**
	 * Method that returns a list of loans of the management
	 * 
	 * @return
	 */
	public List<Product> getProducts();

	/**
	 * Method that returns the loan of the management
	 * 
	 * @return
	 */
	public Loan getLoan();

}