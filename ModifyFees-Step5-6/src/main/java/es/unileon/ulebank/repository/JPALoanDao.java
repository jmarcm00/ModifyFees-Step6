package es.unileon.ulebank.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.unileon.ulebank.assets.Loan;
import es.unileon.ulebank.assets.support.PaymentPeriod;
import es.unileon.ulebank.domain.Product;

@Repository(value = "productDao")
public class JPALoanDao implements LoanDao {

    private EntityManager em = null;

    /*
     * Sets the entity manager.
     */
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Product> getProductList() {
        return em.createQuery("select p from Product p order by p.id").getResultList();
    }

    @Transactional(readOnly = false)
    public void saveProduct(Product prod) {
        em.merge(prod);
    }

	@Override
	public Loan getLoan() {
		Product p = (Product) em.createQuery("select p from Product p order by p.id").getSingleResult();
		
		return new Loan(p.getMoney(), p.getInterest(), PaymentPeriod.MONTHLY, p.getNumFees());
	}

}
