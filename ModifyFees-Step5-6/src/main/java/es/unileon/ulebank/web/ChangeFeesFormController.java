package es.unileon.ulebank.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.unileon.ulebank.service.FeeChanges;
import es.unileon.ulebank.service.LoanManager;

/**
 * Class Controller of the page feeLimits.jsp
 * 
 * @brief Concrete controller of feeLimits.jsp which change the number of fees
 *        in the loan.
 */
@Controller
@RequestMapping(value = "/feeLimits.htm")
public class ChangeFeesFormController {

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	/** Manager de loan */
	@Autowired
	private LoanManager loanManager;

	/**
	 * Method that obtains the data of the form in feeLimits.jsp and save the
	 * changes in the loan
	 * 
	 * @param changes
	 * @param result
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@Valid FeeChanges changes, BindingResult result) {
		if (result.hasErrors()) {
			return "feeLimits";
		}

		int numFees = changes.getNumberOfFees();
		logger.info("Number of fees set to " + numFees);

		loanManager.setNumberOfFees(numFees);

		return "redirect:/changeNumFees.htm";
	}

	/**
	 * Method that sends the number of fees in the loan to the form in
	 * feeLimits.jsp
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping(method = RequestMethod.GET)
	protected FeeChanges formBackingObject(HttpServletRequest request)
			throws ServletException {
		FeeChanges fees = new FeeChanges();
		fees.setNumberOfFees(loanManager.getLoan().getAmortizationTime());
		return fees;
	}

	/**
	 * Setter of the manager
	 * 
	 * @param feeManager
	 */
	public void setLoanManager(LoanManager loanManager) {
		this.loanManager = loanManager;
	}

	/**
	 * Getter of the manager
	 * 
	 * @return
	 */
	public LoanManager getLoanManager() {
		return loanManager;
	}

}