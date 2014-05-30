/* Application developed for AW subject, belonging to passive operations
 group.*/
package es.unileon.ulebank.account;

import es.unileon.ulebank.handler.Handler;
import es.unileon.ulebank.history.Transaction;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author runix
 */
public interface LiquidationStrategy {

    /**
     * Perform liquitadation based on account history.
     *
     * @param transactions ( The transactions for doing the liquidation )
     * @param min
     * @param max
     *
     * @return (The amount of money to pay )
     */
    public Transaction doLiquidation(Iterator<Transaction> transactions, Date min, Date max);

    /**
     * The liquidation strategy id.
     *
     * @return (the id)
     */
    public Handler getID();
}
