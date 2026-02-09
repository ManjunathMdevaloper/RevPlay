package com.revplay.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class BasicTest {

    @Test
    void simpleTest() {
        int result = 2 + 3;
        assertEquals(5, result);
    }
}
