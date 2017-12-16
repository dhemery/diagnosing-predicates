package com.dhemery.predicates;

import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateAssert {
    /**
     * Asserts that the given value is true
     *
     * @param errorMessage the error message for the {@code AssertionError}
     * @param value        the value to test
     * @throws AssertionError if the value is false
     */
    public static void assertThat(String errorMessage, boolean value) {
        if (value) return;
        throw new AssertionError(errorMessage);
    }

    /**
     * Asserts that the value matches the predicate
     *
     * @param errorMessage the error message for the {@code AssertionError}
     * @param value        the value to test
     * @param predicate    tests the value
     * @param <T>          the type of value to test
     * @throws AssertionError if the value does not match the predicate
     */
    public static <T> void assertThat(String errorMessage, T value, Predicate<? super T> predicate) {
        if (predicate.test(value)) return;
        throw new AssertionError(errorMessage);
    }

    /**
     * Asserts that the value matches the predicate.
     *
     * @param value         the value to test
     * @param predicate     tests the value
     * @param errorReporter applied to the value to create the error message
     * @param <T>           the type of value to test
     * @throws AssertionError if the value does not match the predicate
     */
    public static <T> void assertThat(T value, Predicate<? super T> predicate, Function<? super T, String> errorReporter) {
        if (predicate.test(value)) return;
        throw new AssertionError(errorReporter.apply(value));
    }
}
