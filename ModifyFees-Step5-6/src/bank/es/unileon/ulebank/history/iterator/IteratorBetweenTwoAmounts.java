/* Application developed for AW subject, belonging to passive operations
 group.*/
package es.unileon.ulebank.history.iterator;

import es.unileon.ulebank.history.Transaction;
import es.unileon.ulebank.history.conditions.ConditionTransactionBetweenTwoAmounts;
import es.unileon.ulebank.history.conditions.WrongArgsException;
import es.unileon.ulebank.iterator.ConditionalIterator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author runix
 * @param <T>
 */
public class IteratorBetweenTwoAmounts<T extends Transaction> extends ConditionalIterator<T> {

    public IteratorBetweenTwoAmounts(List<T> elements, double min, double max) throws WrongArgsException {
        super(new ConditionTransactionBetweenTwoAmounts<T>(min, max), elements);
    }

    public IteratorBetweenTwoAmounts(Iterator<T> iterator, double min, double max
    ) throws WrongArgsException {
        super(new ConditionTransactionBetweenTwoAmounts<T>(min, max), iterator);
    }
}
