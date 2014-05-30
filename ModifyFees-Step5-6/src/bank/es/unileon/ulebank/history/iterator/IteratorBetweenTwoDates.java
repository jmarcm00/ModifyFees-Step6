/* Application developed for AW subject, belonging to passive operations
 group.*/
package es.unileon.ulebank.history.iterator;

import es.unileon.ulebank.history.Transaction;
import es.unileon.ulebank.history.conditions.ConditionTransactionBetweenTwoDates;
import es.unileon.ulebank.history.conditions.WrongArgsException;
import es.unileon.ulebank.iterator.Condition;
import es.unileon.ulebank.iterator.ConditionalIterator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author runix
 * @param <T>
 */
public class IteratorBetweenTwoDates<T extends Transaction> extends ConditionalIterator<T> {

    public IteratorBetweenTwoDates(List<T> elements, long minDate, long maxDate) throws WrongArgsException {
        super(new ConditionTransactionBetweenTwoDates<T>(new Date(minDate), new Date(maxDate)), elements);
    }

    public IteratorBetweenTwoDates(Iterator<T> iterator, long minDate, long maxDate) throws WrongArgsException {
        super(new ConditionTransactionBetweenTwoDates<T>(new Date(minDate), new Date(maxDate)), iterator);
    }
}
