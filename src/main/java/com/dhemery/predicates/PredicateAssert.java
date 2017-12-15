package com.dhemery.predicates;

public class PredicateAssert {
    public static void assertThat(String errorMessage, boolean assertion) {
        if(assertion) return;
        throw new AssertionError(errorMessage);
    }
}
