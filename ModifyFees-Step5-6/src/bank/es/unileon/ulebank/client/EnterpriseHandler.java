/* Application developed for AW subject, belonging to passive operations
 group.*/

package es.unileon.ulebank.client;

import es.unileon.ulebank.handler.Handler;
import es.unileon.ulebank.handler.MalformedHandlerException;
import es.unileon.ulebank.utils.CifControl;

/**
 * Identifier of enterprises
 * @author Gonzalo Nicolas Barreales
 */

//TODO modify cif with the corresponding data
public class EnterpriseHandler implements Handler{

    /**
     * 
     */
    char entityLetter;
    
    /**
     * Two digits to identify the province
     */
    int provinceCode;
    
    /**
     * Five digits imposed by the Administration in function of the provice
     */
    int registrationCode;
    
    /**
     * The control code is a number if the entity letter is K, Q or S, and is a number if the entetity leter is A, B, E or H
     * With the rest of the entity letters, the control code can be a number or a letter
     */
    char controlCode;
    
    /**
     * 
     * @param entityLetter
     * @param cifNumber
     * @param cifFinalDigit
     */
    public EnterpriseHandler(char entityLetter, int cifNumber, char cifFinalDigit) throws MalformedHandlerException {
        if(Integer.toString(cifNumber).length()<=7 && Integer.toString(cifNumber).length()>=6){
            this.entityLetter=entityLetter;
            provinceCode = (Integer)(cifNumber/100000);
            registrationCode = cifNumber - (provinceCode*100000);
            controlCode=cifFinalDigit;
            if(!CifControl.instance().isCifValid(entityLetter, provinceCode, registrationCode, controlCode)){
                throw new MalformedHandlerException("Invalid control code");
            }
            //Comprobar si es correcto
        }else{
            throw new MalformedHandlerException("Invalid CIF");
        }
    }
    
    @Override
    public int compareTo(Handler another) {
        return toString().compareTo(another.toString());
    }
    
    @Override
    public String toString(){
        return entityLetter + Integer.toString(provinceCode) + Integer.toString(registrationCode) + controlCode;
    }
    
}
