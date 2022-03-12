package com.safetynet.alerts.api.datasource;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class AlertsDataSourceTest {
    @Test
    void testConstructor() {
        assertNull((new AlertsDataSource()).getData());
    }
}

