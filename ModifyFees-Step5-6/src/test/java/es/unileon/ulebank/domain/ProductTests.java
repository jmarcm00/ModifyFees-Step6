package es.unileon.ulebank.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import es.unileon.ulebank.domain.Product;

public class ProductTests {

    private Product product;

    @Before
    public void setUp() throws Exception {
        product = new Product();
    }

	@Test
    public void testSetAndGetMoney() {
        int cash = 5;
        product.setMoney(cash);
        assertEquals(cash, product.getMoney(),0.0);
    }

    @Test
    public void testSetAndGetInterest() {
        double testPrice = 100.00;
        assertEquals(0, 0, 0);    
        product.setInterest(testPrice);
        assertEquals(testPrice, product.getInterest(), 0);
    }
    
    @Test
    public void testSetAndGetFees() {
        int testPrice = 50;
        assertEquals(0, 0, 0);    
        product.setNumFees(testPrice);
        assertEquals(testPrice, product.getNumFees(), 0);
    }

}