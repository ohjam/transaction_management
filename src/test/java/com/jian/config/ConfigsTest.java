package com.jian.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigsTest {

    private Configs configsUnderTest;

    @BeforeEach
    void setUp() {
        configsUnderTest = new Configs();
    }

    @Test
    void testLogFilter() {
        // Setup
        // Run the test
        var result = configsUnderTest.logFilter();

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testCacheManager() {
        var result = configsUnderTest.cacheManager();
        assertNotNull(result);
    }
}
