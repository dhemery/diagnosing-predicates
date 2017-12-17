package com.dhemery.predicates;

import java.util.function.Function;
import java.util.function.Predicate;

// TODO: Is it meaningful to compose DiagnosingPredicates (via and(), or(), and negate())?
/**
 * A self-describing predicate that can diagnose mismatching inputs.
s *
 * @param <T> the type of the input to the predicate
 */
public interface DiagnosingPredicate<T> extends SelfDescribingPredicate<T> {
    /**
     * Returns this predicate's justification for rejecting the input.
     * This method may assume, without testing, that the input does not match the predicate.
     *
     * @param t the input to diagnose
     * @return the justification for rejecting the input
     */
    String diagnosisOf(T t);

    /**
     * Decorates a predicate to make it self-describing and to add the ability to diagnose mismatching inputs.
     *
     * @param predicate   the underlying predicate
     * @param description the description of the underlying predicate
     * @param diagnoser   the function to apply to diagnose mismatching inputs
     * @param <T>         the type of the input to the predicate
     */
    static <T> DiagnosingPredicate<T> of(Predicate<T> predicate, String description, Function<? super T, String> diagnoser) {
        return of(SelfDescribingPredicate.of(predicate, description), diagnoser);
    }

    /**
     * Decorates a self-describing predicate to add the ability to diagnose mismatching inputs.
     *
     * @param predicate the underlying predicate
     * @param diagnoser the function to apply to diagnose mismatching inputs
     * @param <T>       the type of the input to the predicate
     */
    static <T> DiagnosingPredicate<T> of(SelfDescribingPredicate<T> predicate, Function<? super T, String> diagnoser) {
        return new DiagnosingPredicate<T>() {
            @Override
            public String description() {
                return predicate.description();
            }

            @Override
            public boolean test(T t) {
                return predicate.test(t);
            }

            @Override
            public String diagnosisOf(T t) {
                return diagnoser.apply(t);
            }
        };
    }
}
