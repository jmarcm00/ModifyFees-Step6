//package es.unileon.ulebank.repository;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import es.unileon.ulebank.domain.Product;
//import es.unileon.ulebank.repository.LoanDao;
//
//
//public class JPAProductDaoTests {
//
//    private ApplicationContext context;
//    private LoanDao productDao;
//
//    @Before
//    public void setUp() throws Exception {
//        context = new ClassPathXmlApplicationContext("classpath:test-context.xml");
//        productDao = (LoanDao) context.getBean("productDao");
//    }
//
//    @Test
//    public void testGetProductList() {
//        List<Product> products = productDao.getProductList();
//        assertEquals(products.size(), 1, 0);	   
//    }
//
//    @Test
//    public void testSaveProduct() {
//        List<Product> products = productDao.getProductList();
//
//        Product p = products.get(0);
//        Double price = p.getMoney();
//        p.setMoney(200.12);
//        productDao.saveProduct(p);
//
//        List<Product> updatedProducts = productDao.getProductList();
//        Product p2 = updatedProducts.get(0);
//        assertEquals(p2.getMoney(), 200.12, 0.0);
//
//        p2.setMoney(price);
//        productDao.saveProduct(p2);
//    }
// }