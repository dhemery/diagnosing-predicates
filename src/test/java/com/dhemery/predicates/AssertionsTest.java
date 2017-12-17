package com.dhemery.predicates;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.dhemery.predicates.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssertionsTest {
    private static final Function<Integer, String> THROWING_DIAGNOSER = subject -> {
        throw new AssertionError("Diagnoser unexpectedly applied to subject " + subject);
    };

    @Nested
    public class AssertBoolean {
        @Test
        public void
        returnsWithoutThrowingIfValueIsTrue() {
            assertThat("should not be thrown", true);
        }

        @Test
        public void
        throwsWithMessageIfValueIsFalse() {
            String errorMessage = "error message";

            Executable failingAssertion = () -> assertThat(errorMessage, false);

            AssertionError thrown = assertThrows(AssertionError.class, failingAssertion);
            assertEquals(errorMessage, thrown.getMessage());
        }
    }

    @Nested
    public class AssertPredicateWithContext {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            assertThat("context", 1, t -> true);
        }

        @Test
        public void
        throwsWithContextAndSubjectIfSubjectDoesNotMatchPredicate() {
            String context = "context";
            int subject = 1;

            Executable assertion = () -> assertThat(context, subject, t -> false);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertStartsWith(context, thrown.getMessage());
            assertEndsWith("\n     Was: " + subject, thrown.getMessage());
        }
    }

    @Nested
    public class AssertPredicateWithFormatter {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            assertThat(1, t -> true, THROWING_DIAGNOSER);
        }

        @Test
        public void
        throwsWithFormattedSubjectIfSubjectDoesNotMatchPredicate() {
            int subject = 1;
            Function<Integer, String> mismatchFormatter = s -> "formatted subject";

            Executable assertion = () -> assertThat(subject, t -> false, mismatchFormatter);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(mismatchFormatter.apply(subject), thrown.getMessage());
        }
    }

    @Nested
    public class AssertSelfDescribingPredicate {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            SelfDescribingPredicate<Integer> matchingPredicate = SelfDescribingPredicate.of(t -> true, "matches");

            assertThat(1, matchingPredicate);
        }

        @Test
        public void
        throwsWithExpectationAndSubjectIfSubjectDoesNotMatchPredicate() {
            int subject = 1;
            SelfDescribingPredicate<Integer> mismatchingPredicate = SelfDescribingPredicate.of(t -> false, "mismatches");

            Executable assertion = () -> assertThat(subject, mismatchingPredicate);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertStartsWith("\nExpected: " + mismatchingPredicate.description(), thrown.getMessage());
            assertEndsWith(" But was: " + subject, thrown.getMessage());
        }
    }

    @Nested
    public class AssertSelfDescribingPredicateWithDiagnoser {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            SelfDescribingPredicate<Integer> matchingPredicate = SelfDescribingPredicate.of(t -> true, "matches");

            assertThat(1, matchingPredicate, THROWING_DIAGNOSER);
        }

        @Test
        public void
        throwsWithFormattedExpectationAndSubjectIfSubjectDoesNotMatchPredicate() {
            int subject = 1;
            SelfDescribingPredicate<Integer> mismatchingPredicate = SelfDescribingPredicate.of(t -> false, "mismatches");
            BiFunction<String, Integer, String> formatter = (e, s) -> "formatted expectation and subject";

            Executable assertion = () -> assertThat(subject, mismatchingPredicate, formatter);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(formatter.apply(mismatchingPredicate.description(), subject), thrown.getMessage());
        }
    }

    @Nested
    public class AssertDiagnosingPredicate {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            DiagnosingPredicate<Integer> matchingPredicate = DiagnosingPredicate.of(t -> true, "matches", THROWING_DIAGNOSER);

            assertThat(1, matchingPredicate);
        }

        @Test
        public void
        throwsWithExpectationAndFormattedSubjectIfSubjectDoesNotMatchPredicate() {
            int subject = 1;
            DiagnosingPredicate<Integer> mismatchingPredicate = DiagnosingPredicate.of(t -> false, "mismatches", s -> "formatted subject");

            Executable assertion = () -> assertThat(subject, mismatchingPredicate);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertStartsWith("\nExpected: " + mismatchingPredicate.description(), thrown.getMessage());
            assertEndsWith("     But: " + mismatchingPredicate.diagnosisOf(subject), thrown.getMessage());
        }
    }

    private void assertStartsWith(String prefix, String string) {
        if (string.startsWith(prefix)) return;
        String message = new StringBuilder()
                .append("Expected prefix: \"").append(prefix).append('"').append(System.lineSeparator())
                .append("But was: \"").append(string).append('"').toString();
        throw new AssertionError(message);
    }

    private void assertEndsWith(String suffix, String string) {
        if (string.endsWith(suffix)) return;
        String message = new StringBuilder()
                .append("Expected suffix: \"").append(suffix).append('"').append(System.lineSeparator())
                .append("But was: \"").append(string).append('"').toString();
        throw new AssertionError(message);
    }
}
