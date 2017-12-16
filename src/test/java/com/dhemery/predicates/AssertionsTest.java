package com.dhemery.predicates;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Objects;
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
            assertThat("non-null", Objects::nonNull, Function.identity());
        }

        @Test
        public void
        throwsWithErrorReportIfSubjectDoesNotMatchPredicate() {
            String actual = "non-null";
            Function<String, String> diagnoser = a -> "<<<" + a + ">>>";

            Executable assertion = () -> assertThat(actual, Objects::isNull, diagnoser);

            AssertionError thrown = assertThrows(AssertionError.class, assertion);
            assertEquals(diagnoser.apply(actual), thrown.getMessage());
        }
    }
}
