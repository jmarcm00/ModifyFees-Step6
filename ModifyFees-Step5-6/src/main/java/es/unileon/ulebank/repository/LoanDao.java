package es.unileon.ulebank.repository;

import java.util.List;

import es.unileon.ulebank.assets.Loan;
import es.unileon.ulebank.domain.Product;

public interface LoanDao {

    public List<Product> getProductList();
    
    public Loan getLoan();

    public void saveProduct(Product prod);

}