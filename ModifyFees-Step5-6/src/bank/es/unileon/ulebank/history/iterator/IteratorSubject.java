/* Application developed for AW subject, belonging to passive operations
 group.*/
package es.unileon.ulebank.history.iterator;

import es.unileon.ulebank.history.Transaction;
import es.unileon.ulebank.history.conditions.ConditionSubject;
import es.unileon.ulebank.iterator.Condition;
import es.unileon.ulebank.iterator.ConditionalIterator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author runix
 * @param <T>
 */
public class IteratorSubject<T extends Transaction> extends ConditionalIterator<T> {

    public IteratorSubject(Iterator<T> iterator, String subject, boolean includeSubject) {
        super(new ConditionSubject<T>(includeSubject, subject), iterator);
    }

    public IteratorSubject(List<T> elems, String subject, boolean includeSubject) {
        super(new ConditionSubject<T>(includeSubject, subject), elems);
    }

}
