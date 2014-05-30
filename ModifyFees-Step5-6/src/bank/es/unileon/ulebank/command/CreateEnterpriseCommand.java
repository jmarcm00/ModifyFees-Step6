/* Application developed for AW subject, belonging to passive operations
 group.*/

package es.unileon.ulebank.command;

import es.unileon.ulebank.client.Address;
import es.unileon.ulebank.client.Client;
import es.unileon.ulebank.client.Enterprise;
import es.unileon.ulebank.client.EnterpriseHandler;
import es.unileon.ulebank.client.Person;
import es.unileon.ulebank.handler.Handler;
import es.unileon.ulebank.handler.MalformedHandlerException;
import es.unileon.ulebank.office.Office;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paula
 */
public class CreateEnterpriseCommand implements Command{
    private final String enterpriseName;
    private final Address address;
    private int cifNumber;
    private char cifLetter;
    private char cifControl;
    private EnterpriseHandler enterpriseCode;
    private final Office office;
    private Client client;
    private final Handler commandID;
    private final Date effectiveDate;

    /**
     *
     * @param office
     * @param enterpriseName
     * @param address
     * @param cifNumber
     * @param cifLetter
     * @param effectiveDate
     * @param commandId
     */
    public CreateEnterpriseCommand(Office office, String enterpriseName, Address address, int cifNumber, char cifLetter, char cifControl, Date effectiveDate, Handler commandId){
       
        this.enterpriseName = enterpriseName;
        this.address=address;
        this.office = office;
        this.effectiveDate = effectiveDate;
        this.commandID = commandId;
      
    }
    /**
     *
     */
    @Override
    public void execute() {
        try {
            this.client = new Enterprise(this.cifLetter, this.cifNumber, this.cifControl, this.enterpriseName, this.address);
            this.office.addClient(client);
        } catch (MalformedHandlerException ex) {
            Logger.getLogger(CreateEnterpriseCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        if (this.client != null) {
            this.office.deleteClient(this.client.getId());
        }
    }

    /**
     *
     */
    @Override
    public void redo() {
        if (this.client != null) {
            this.office.addClient(this.client);
        }
    }
    
}
