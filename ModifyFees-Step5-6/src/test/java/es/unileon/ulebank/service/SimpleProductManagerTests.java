package es.unileon.ulebank.service;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import es.unileon.ulebank.assets.Loan;
import es.unileon.ulebank.domain.Product;
import es.unileon.ulebank.repository.InMemoryProductDao;
import es.unileon.ulebank.repository.LoanDao;


public class SimpleProductManagerTests {

	 private SimpleLoanManager productManager;

	    private Loan product;

	    @Before
	    public void setUp() throws Exception {
	        productManager = new SimpleLoanManager();

	        product = new Loan();
	        product.setAmortizationTime(12);
	        product.setAmountOfMoney(3000);
	        product.setInterest(0.05);

	        LoanDao productDao = new InMemoryProductDao(product);
	        productManager.setProductDao(productDao);

	    }

	    @Test
	    public void testGetProductsWithNoProducts() {
	        productManager = new SimpleLoanManager();
	        productManager.setProductDao(new InMemoryProductDao(new ArrayList<Product>()));
	        assertNull(productManager.getLoan());
	    }

	    @Test
	    public void testChangeBuyLimits() {
	        productManager.setNumberOfFees(12);
	        assertEquals(12, product.getAmortizationTime(), 0);
	    }

}