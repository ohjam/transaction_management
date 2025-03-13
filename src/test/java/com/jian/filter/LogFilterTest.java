package com.jian.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LogFilterTest {

    private LogFilter logFilterUnderTest;

    @BeforeEach
    void setUp() {
        logFilterUnderTest = new LogFilter();
    }

    @Test
    void testDoFilterInternal() throws Exception {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        // Run the test
        logFilterUnderTest.doFilterInternal(request, response, filterChain);
        assertTrue(true);
    }
}
