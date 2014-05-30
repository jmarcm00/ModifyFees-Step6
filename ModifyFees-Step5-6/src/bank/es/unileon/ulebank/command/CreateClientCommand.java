/* Application developed for AW subject, belonging to passive operations
 group.*/
package es.unileon.ulebank.command;

import es.unileon.ulebank.client.Address;
import es.unileon.ulebank.client.Client;
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
public class CreateClientCommand implements Command {

    private final String name;
    private final String surnames;
    private final String civilState;
    private final Address address;
    private final String profession;
    private char foreingLetter;
    private int dniNumber;
    private char dniLetter;
    private final Date birthDate;
    private final int phoneNumber1, phoneNumber2;
    private final Office office;
    private Client client;
    private final Handler commandID;
    private final Date effectiveDate;

    /**
     *
     * @param office
     * @param name
     * @param surnames
     * @param address
     * @param civilState
     * @param phoneNumber1
     * @param phoneNumber2
     * @param profession
     * @param birthDate
     * @param foreingLetter
     * @param dniNumber
     * @param dniLetter
     * @param effectiveDate
     * @param commandId
     */
    public CreateClientCommand(Office office, String name, String surnames, Address address, String civilState, int phoneNumber1, int phoneNumber2, String profession, Date birthDate, char foreingLetter, int dniNumber, char dniLetter, Date effectiveDate, Handler commandId) {
        this.name = name;
        this.surnames = surnames;
        this.address = address;
        this.civilState = civilState;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.profession = profession;
        this.birthDate = birthDate;
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
            this.client = new Person(this.name, this.surnames, this.address, this.civilState, this.phoneNumber1, this.phoneNumber2, this.profession, this.birthDate, this.foreingLetter, this.dniNumber, this.dniLetter);
            this.office.addClient(client);
        } catch (MalformedHandlerException ex) {
            Logger.getLogger(CreateClientCommand.class.getName()).log(Level.SEVERE, null, ex);
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
