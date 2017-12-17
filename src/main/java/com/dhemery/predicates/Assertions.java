package com.dhemery.predicates;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * Methods for asserting conditions.
 */
public class Assertions {
    private static final String LINE_PREFIX_FORMAT = "%n%8s: ";
    public static final String EXPECTED = format(LINE_PREFIX_FORMAT, "Expected");
    public static final String BUT_WAS = format(LINE_PREFIX_FORMAT, "But was");
    public static final String BUT = format(LINE_PREFIX_FORMAT, "But");
    public static final String WAS = format(LINE_PREFIX_FORMAT, "Was");

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
     * If the assertion fails, this method constructs an error message
     * that describes the context and the subject.
     *
     * @param context   the context of the assertion
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T>
    void assertThat(String context, T subject, Predicate<? super T> predicate) {
        if (predicate.test(subject)) return;
        throw new AssertionError(errorMessage(context, WAS, subject));
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails, this method constructs an error message
     * by applying the formatter
     * to the subject.
     *
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject
     * @param formatter the function to apply to describe mismatches
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T>
    void assertThat(T subject, Predicate<? super T> predicate, Function<? super T, String> formatter) {
        if (predicate.test(subject)) return;
        throw new AssertionError(formatter.apply(subject));
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails, this method constructs an error message
     * by applying the formatter
     * to the predicate and the subject.
     *
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject
     * @param formatter the function to apply to describe mismatches
     * @param <T>       the type of the subject
     * @param <P>       the type of the predicate
     * @throws AssertionError if the assertion fails
     */
    public static <T, P extends Predicate<? super T>>
    void assertThat(T subject, P predicate, BiFunction<? super P, ? super T, String> formatter) {
        if (predicate.test(subject)) return;
        throw new AssertionError(formatter.apply(predicate, subject));
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails, this method constructs an error message
     * that describes
     * the predicate and the subject.
     *
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T>
    void assertThat(T subject, SelfDescribingPredicate<? super T> predicate) {
        if (predicate.test(subject)) return;
        throw new AssertionError(errorMessage(EXPECTED, predicate.description(), BUT_WAS, subject));
    }

    /**
     * Asserts that the subject matches the predicate.
     * If the assertion fails, this method constructs an error message
     * that describes
     * the predicate and the predicates's diagnosis of the subject.
     *
     * @param subject   the value to test
     * @param predicate the predicate to apply to test the subject and to describe mismatches
     * @param <T>       the type of the subject
     * @throws AssertionError if the assertion fails
     */
    public static <T>
    void assertThat(T subject, DiagnosingPredicate<? super T> predicate) {
        if (predicate.test(subject)) return;
        throw new AssertionError(errorMessage(EXPECTED, predicate.description(), BUT, predicate.diagnosisOf(subject)));
    }

    private static String errorMessage(Object... parts) {
        return Stream.of(parts).map(Object::toString).collect(joining());
    }
}
