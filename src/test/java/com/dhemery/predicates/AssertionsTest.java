package com.dhemery.predicates;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.dhemery.predicates.Assertions.assertThat;
import static java.lang.String.format;
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
        throwsWithContextIfValueIsFalse() {
            String context = "context";

            Executable failingAssertion = () -> assertThat(context, false);

            AssertionError thrown = assertThrows(AssertionError.class, failingAssertion);
            assertEquals(context, thrown.getMessage());
        }
    }

    @Nested
    public class AssertPredicateWithContext {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            assertThat("context", 1, t -> true);
        }

        // TODO: Error message includes context and actual
        @Test
        public void
        throwsWithContextIfSubjectDoesNotMatchPredicate() {
            String context = "context";

            Executable assertion = () -> assertThat(context, 1, t -> false);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(context, thrown.getMessage());
        }
    }

    @Nested
    public class AssertPredicateWithDiagnoser {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            assertThat(1, t -> true, THROWING_DIAGNOSER);
        }

        @Test
        public void
        throwsWithDiagnosisIfSubjectDoesNotMatchPredicate() {
            int subject = 1;
            Function<Integer, String> diagnoser = actual -> format("was %d", actual);

            Executable assertion = () -> assertThat(subject, t -> false, diagnoser);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(diagnoser.apply(subject), thrown.getMessage());
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
        throwsWithExpectationAndDiagnosisIfSubjectDoesNotMatchPredicate() {
            int subject = 1;
            SelfDescribingPredicate<Integer> mismatchingPredicate = SelfDescribingPredicate.of(t -> false, "mismatches");
            BiFunction<String, Integer, String> diagnoser = (expectation, actual) -> format("expected %s but was %d ", expectation, actual);

            Executable assertion = () -> assertThat(subject, mismatchingPredicate, diagnoser);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(diagnoser.apply(mismatchingPredicate.description(), subject), thrown.getMessage());
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
        throwsWithExpectationAndDiagnosisIfSubjectDoesNotMatchPredicate() {
            int subject = 1;
            DiagnosingPredicate<Integer> mismatchingPredicate = DiagnosingPredicate.of(t -> false, "mismatches", actual -> format("was %d", actual));

            Executable assertion = () -> assertThat(subject, mismatchingPredicate);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertContains(thrown.getMessage(), mismatchingPredicate.description(), "description");
            assertContains(thrown.getMessage(), mismatchingPredicate.diagnosisOf(subject), "diagnosis");
        }
    }

    private static void assertContains(String string, String substring, String context) {
        if (string.contains(substring)) return;
        String message = new StringBuilder()
                .append(context).append(System.lineSeparator())
                .append("Expected a string containing \"").append(substring).append('"').append(System.lineSeparator())
                .append("But was \"").append(string).append('"').toString();
        throw new AssertionError(message);
    }
}
