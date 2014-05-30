package es.unileon.ulebank.web;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import es.unileon.ulebank.domain.Product;
import es.unileon.ulebank.repository.InMemoryProductDao;
import es.unileon.ulebank.service.SimpleLoanManager;
import es.unileon.ulebank.web.FeeController;


public class InventoryControllerTests {

    @Test
    public void testHandleRequestView() throws Exception{		
        FeeController controller = new FeeController();
        SimpleLoanManager spm = new SimpleLoanManager();
        spm.setProductDao(new InMemoryProductDao(new ArrayList<Product>()));
        controller.setLoanManager(spm);
        //controller.setProductManager(new SimpleProductManager());
        ModelAndView modelAndView = controller.handleRequest(null, null);		
        assertEquals("changeNumFees", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel());
        Map modelMap = (Map) modelAndView.getModel().get("model");
        String nowValue = (String) modelMap.get("now");
        assertNotNull(nowValue);
    }
}