package com.dhemery.predicates;

import java.util.function.Predicate;

public interface SelfDescribingPredicate<T> extends Predicate<T> {
    String description();

    static <T> SelfDescribingPredicate<T> of(Predicate<T> underlyingPredicate, String description) {
        return new SelfDescribingPredicate<>() {
            @Override
            public String description() {
                return description;
            }

            @Override
            public boolean test(T t) {
                return underlyingPredicate.test(t);
            }
        };
    }
}
