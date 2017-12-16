package com.dhemery.predicates;

import java.util.function.Function;
import java.util.function.Predicate;

public class Assertions {
    /**
     * Asserts that the value is {@code true}.
     *
     * @param errorMessage the error message for the {@code AssertionError}
     * @param value        the value to test
     * @throws AssertionError if the assertion fails
     */
    public static void assertThat(String errorMessage, boolean value) {
        if (value) return;
        throw new AssertionError(errorMessage);
    }

    /**
     * Asserts that the subject matches the predicate.
     *
     * @param context   the error message
     * @param subject   the value to test
     * @param predicate tests the subject
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(String context, T subject, Predicate<? super T> predicate) {
        if (predicate.test(subject)) return;
        throw new AssertionError(context);
    }

    /**
     * Asserts that the subject matches the predicate.
     *
     * @param value         the value to test
     * @param predicate     tests the subject
     * @param errorReporter constructs the error message if the assertion fails
     * @param <T>           the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(T value, Predicate<? super T> predicate, Function<? super T, String> errorReporter) {
        if (predicate.test(value)) return;
        throw new AssertionError(errorReporter.apply(value));
    }
}
