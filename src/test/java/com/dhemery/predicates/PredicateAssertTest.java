package com.dhemery.predicates;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PredicateAssertTest {
    @Test
    public void  testsBooleanDirectly() {
        assertThat("success reason message", true);

        try {
            assertThat("failing reason message", false);
        }
        catch (AssertionError e) {
            assertEquals("failing reason message", e.getMessage());
            return;
        }

        fail("should have failed");
    }

    private void assertThat(String errorMessage, boolean assertion) {
        if(assertion) return;
        throw new AssertionFailedError(errorMessage);
    }
}
