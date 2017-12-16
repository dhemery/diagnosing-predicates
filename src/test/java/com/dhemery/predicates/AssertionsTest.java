package com.dhemery.predicates;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.dhemery.predicates.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssertionsTest {
    @Nested
    public class AssertBoolean {
        @Test
        public void
        returnsWithoutThrowingIfValueIsTrue() {
            assertThat("context", true);
        }

        @Test
        public void
        throwsIfValueIsFalse() {
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
            assertThat("context", "non-null", Objects::nonNull);
        }

        // TODO: Error message describes context and actual
        @Test
        public void
        throwsWithContextIfSubjectDoesNotMatchPredicate() {
            String context = "context";

            Executable assertion = () -> assertThat(context, "non-null", Objects::isNull);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);

            assertEquals(context, thrown.getMessage());
        }
    }

    @Nested
    public class AssertPredicateWithDiagnoser {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            Function<String, String> diagnoser = s -> String.format("Subject <%s>", s);

            assertThat("non-null", Objects::nonNull, diagnoser);
        }

        @Test
        public void
        throwsWithDiagnosisIfSubjectDoesNotMatchPredicate() {
            String nonNullSubject = "non-null";
            Function<String, String> diagnoser = s -> String.format("Subject <%s>", s);

            Executable assertion = () -> assertThat(nonNullSubject, Objects::isNull, diagnoser);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(diagnoser.apply(nonNullSubject), thrown.getMessage());
        }
    }

    @Nested
    public class AssertSelfDescribingPredicateWithDiagnoser {
        @Test
        public void
        returnsWithoutThrowingIfSubjectMatchesPredicate() {
            SelfDescribingPredicate<String> isEmptyString = SelfDescribingPredicate.of(String::isEmpty, "an empty string");
            BiFunction<String, String, String> diagnoser = (e, a) -> String.format("Expected <%s>, Actual <%s> ", e, a);

            assertThat("", isEmptyString, diagnoser);
        }

        @Test
        public void
        throwsWithExpectationAndDiagnosisIfSubjectDoesNotMatchPredicate() {
            String nonEmptySubject = "non-empty";
            SelfDescribingPredicate<String> isEmptyString = SelfDescribingPredicate.of(String::isEmpty, "an empty string");
            BiFunction<String, String, String> diagnoser = (e, a) -> String.format("Expected <%s>, Actual <%s> ", e, a);

            Executable assertion = () -> assertThat(nonEmptySubject, isEmptyString, diagnoser);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(diagnoser.apply(isEmptyString.description(), nonEmptySubject), thrown.getMessage());
        }
    }
}
