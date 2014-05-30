package es.unileon.ulebank.repository;

import java.util.ArrayList;
import java.util.List;

import es.unileon.ulebank.assets.Loan;
import es.unileon.ulebank.domain.Product;
import es.unileon.ulebank.repository.LoanDao;


public class InMemoryProductDao implements LoanDao {

    private ArrayList<Product> productList;
    private Loan loan;

    public InMemoryProductDao(ArrayList<Product> arrayList) {
        this.productList = arrayList;
    }

	public InMemoryProductDao(Loan product) {
		this.loan = product;
	}

	@Override
	public List<Product> getProductList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Loan getLoan() {
		return loan;
	}

	@Override
	public void saveProduct(Product prod) {
		// TODO Auto-generated method stub
		
	}



}