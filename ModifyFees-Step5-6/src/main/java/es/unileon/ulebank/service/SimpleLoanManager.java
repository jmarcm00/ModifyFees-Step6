package es.unileon.ulebank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.unileon.ulebank.assets.Loan;
import es.unileon.ulebank.domain.Product;
import es.unileon.ulebank.repository.LoanDao;

/**
 * Simple Fee Manager Class
 * @brief Class which manages the loan
 */
@Component
public class SimpleLoanManager implements LoanManager {

    private static final long serialVersionUID = 1L;

    @Autowired
    private LoanDao loanDao;

    public void setProductDao(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    public List<Product> getProducts() {
        return loanDao.getProductList();
    }

    public void setNumberOfFees(int numFees) {
        List<Product> products = loanDao.getProductList();
        if (products != null) {
            for (Product product : products) {
                product.setNumFees(numFees);
                loanDao.saveProduct(product);
            }
        }
    }

	@Override
	public Loan getLoan() {
		return loanDao.getLoan();
	}
}