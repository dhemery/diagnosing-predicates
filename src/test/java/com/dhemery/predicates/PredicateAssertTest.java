package com.dhemery.predicates;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dhemery.predicates.PredicateAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PredicateAssertTest {
    @Test
    public void
    testsBooleanDirectly() {
        assertThat("success reason message", true);

        String errorMessage = "failing reason message";
        Executable failingAssertion = () -> assertThat(errorMessage, false);

        shouldThrow(failingAssertion, errorMessage::equals);
    }

    @Test
    public void
    assertsThatValueMatchesPredicate() {
        assertThat("success reason message", "non-null", Objects::nonNull);

        String errorMessage = "failing reason message";
        Executable assertion = () -> assertThat(errorMessage, "non-null", Objects::isNull);

        shouldThrow(assertion, errorMessage::equals);
    }

    @Test
    public void
    appliesErrorReporterToTestedValueToObtainErrorMessage() {
        String expected = "expected";
        String actual = "actual";
        Function<String, String> errorReporter = a -> "<<<" + a + ">>>";

        Executable assertion = () -> assertThat(actual, expected::equals, errorReporter);

        String errorMessage = errorReporter.apply(actual);
        shouldThrow(assertion, errorMessage::equals);
    }

    @Disabled("Awaiting DiagnosingPredicate")
    @Test
    public void
    includesDescriptionOfTestedValueInErrorMessage() {
        String expected = "expected";
        String actual = "actual";

        String expectedMessage = "identifier\nExpected: \"expected\"\n     but: was \"actual\"";

        Executable assertion = () -> assertThat("identifier", actual, expected::equals);

        shouldThrow(assertion, expectedMessage::startsWith);
    }

    @Disabled("Awaiting SelfDescribingPredicate")
    @Test
    public void
    descriptionCanBeElided() {
        String expected = "expected";
        String actual = "actual";

        String expectedMessage = "\nExpected: \"expected\"\n     but: was \"actual\"";

//        shouldThrow(() -> assertThat(actual, expected::equals), expectedMessage::startsWith);
    }

    @Disabled("Awaiting DiagnosingPredicate")
    @Test
    public void
    includesMismatchDescription() {
//        Matcher<String> matcherWithCustomMismatchDescription = new BaseMatcher<String>() {
//            @Override
//            public boolean matches(Object item) {
//                return false;
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Something cool");
//            }
//
//            @Override
//            public void describeMismatch(Object item, Description mismatchDescription) {
//                mismatchDescription.appendText("Not cool");
//            }
//        };
//
//        String expectedMessage = "\nExpected: Something cool\n     but: Not cool";
//
//        try {
//            assertThat("Value", matcherWithCustomMismatchDescription);
//            fail("should have failed");
//        } catch (AssertionError e) {
//            assertEquals(expectedMessage, e.getMessage());
//        }
    }

    @Test
    public void
    canAssertSubtypes() {
        Number number = 1;
        Integer numberAsInteger = number.intValue();
        assertThat("", numberAsInteger, number::equals);
    }

    private static void shouldThrow(Executable failingAssertion, Predicate<String> messageTest) {
        AssertionError thrown = assertThrows(AssertionError.class, failingAssertion);
        assertTrue(messageTest.test(thrown.getMessage()), "error message");
    }
}
