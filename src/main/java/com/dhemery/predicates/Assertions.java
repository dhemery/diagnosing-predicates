package com.dhemery.predicates;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Methods for asserting conditions in tests.
 */
public class Assertions {
    /**
     * Asserts that the value is {@code true}.
     * If the assertion fails,
     * this method throws an {@code AssertionError}
     * with the given error message.
     *
     * @param errorMessage the error message to include in the {@code AssertionError}
     * @param value        the value to test
     * @throws AssertionError if the assertion fails
     */
    public static void assertThat(String errorMessage, boolean value) {
        if (value) return;
        throw new AssertionError(errorMessage);
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails,
     * this method throws an {@code AssertionError}
     * that describes the context and the subject.
     *
     * @param context   the context of the assertion
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(String context, T subject, Predicate<? super T> predicate) {
        if (predicate.test(subject)) return;
        throw new AssertionError(context);
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails,
     * this method throws an {@code AssertionError}
     * with the diagnoser's description
     * of the result.
     *
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject
     * @param diagnoser the function to apply to diagnose the subject if it mismatches the predicate
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(T subject, Predicate<? super T> predicate, Function<? super T, String> diagnoser) {
        if (predicate.test(subject)) return;
        throw new AssertionError(diagnoser.apply(subject));
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails,
     * this method throws an {@code AssertionError}
     * with the diagnoser's description
     * of the expectation and the result.
     *
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject
     * @param diagnoser the function to apply to diagnose the subject if it mismatches the predicate
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(T subject, SelfDescribingPredicate<? super T> predicate, BiFunction<String, ? super T, String> diagnoser) {
        if (predicate.test(subject)) return;
        throw new AssertionError(diagnoser.apply(predicate.description(), subject));
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails,
     * this method throws an {@code AssertionError}
     * with the predicate's description
     * of the expectation and the result.
     *
     * @param subject   the value to test
     * @param predicate tests the subject and diagnoses failures
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(T subject, DiagnosingPredicate<? super T> predicate) {
        if (predicate.test(subject)) return;
        String message = new StringBuffer()
                .append("Expected: ").append(predicate.description()).append(System.lineSeparator())
                .append("     But: ").append(predicate.diagnosisOf(subject))
                .toString();
        throw new AssertionError(message);
    }
}
