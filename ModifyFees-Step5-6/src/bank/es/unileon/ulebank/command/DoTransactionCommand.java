/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.unileon.ulebank.command;

import es.unileon.ulebank.account.DetailedInformation;
import es.unileon.ulebank.handler.Handler;
import es.unileon.ulebank.history.GenericTransaction;
import es.unileon.ulebank.history.Transaction;
import es.unileon.ulebank.transacionManager.TransactionManager;
import java.util.Date;

/**
 *
 * @author Paula
 */
public class DoTransactionCommand implements Command {

    private final Handler commandID;
    private final double amount;
    private final Date date;
    private Date effectiveDate;
    private final String subject;
    private final DetailedInformation extraInformation;
    private Transaction transaction;
    private TransactionManager trans;

    /**
     *
     * @param amount
     * @param date
     * @param subject
     * @param info
     * @param commandId
     */

    public DoTransactionCommand(double amount, Date date, String subject, DetailedInformation info, Handler commandId) {
        this.amount = amount;
        this.date = date;
        this.subject = subject;
        this.extraInformation = info;
        this.commandID = commandId;

    }

    /**
     *
     */
    @Override
    public void execute() {
        this.transaction = new GenericTransaction(this.amount, this.date, this.subject, this.extraInformation);
        // this.trans.doTransaction(transaction, );
    }

    /**
     *
     * @return
     */
    @Override
    public Date getEffectiveDate() {
        return this.effectiveDate;
    }

    /**
     *
     * @return
     */
    @Override
    public Handler getID() {
        return this.commandID;
    }

    /**
     *
     */
    @Override
    public void undo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     */
    @Override
    public void redo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
