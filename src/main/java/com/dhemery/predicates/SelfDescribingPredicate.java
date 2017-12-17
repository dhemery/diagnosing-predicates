package com.dhemery.predicates;

import java.util.function.Predicate;

// TODO: Implement default negate().
// TODO: Make and(), or() throw UnsupportedOperationException.
// TODO: Override and() and or() to take SelfDescribingPredicate.

/**
 * A predicate that can describe itself.
 *
 * @param <T> the type the input to the predicate
 */
public interface SelfDescribingPredicate<T> extends Predicate<T> {
    /**
     * Returns a description of the predicate.
     *
     * @return a description of the predicate
     */
    String description();

    /**
     * Decorates a predicate to make it self-describing.
     *
     * @param predicate   the underlying predicate
     * @param description the description of the underlying predicate
     * @param <T>         the type of the input to the predicate
     * @return the self-describing predicate
     */
    static <T> SelfDescribingPredicate<T> of(Predicate<T> predicate, String description) {
        return new SelfDescribingPredicate<>() {
            @Override
            public String description() {
                return description;
            }

            @Override
            public boolean test(T t) {
                return predicate.test(t);
            }
        };
    }
}
