package com.safetynet.alerts.api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class DataAlreadyExistsExceptionTest {
    @Test
    void testConstructor() {
        assertEquals(HttpStatus.CONFLICT, (new DataAlreadyExistsException("Data Name")).getHttpStatus());
    }

    @Test
    void testConstructor2() {
        DataAlreadyExistsException actualDataAlreadyExistsException = new DataAlreadyExistsException("Data Name");
        assertNull(actualDataAlreadyExistsException.getCause());
        assertEquals("com.safetynet.alerts.api.exception.DataAlreadyExistsException: Data 'Data Name' already exists !",
                actualDataAlreadyExistsException.toString());
        assertEquals(0, actualDataAlreadyExistsException.getSuppressed().length);
        assertEquals("Data 'Data Name' already exists !", actualDataAlreadyExistsException.getMessage());
        assertEquals("Data 'Data Name' already exists !", actualDataAlreadyExistsException.getLocalizedMessage());
        assertEquals(HttpStatus.CONFLICT, actualDataAlreadyExistsException.getHttpStatus());
    }
}

