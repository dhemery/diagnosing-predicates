package com.dhemery.predicates;

import org.junit.jupiter.api.Test;

import static com.dhemery.predicates.PredicateAssert.assertThat;
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
}
