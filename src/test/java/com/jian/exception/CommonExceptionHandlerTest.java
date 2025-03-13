package com.jian.exception;

import com.jian.common.http.response.JsonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommonExceptionHandlerTest {

    private CommonExceptionHandler commonExceptionHandlerUnderTest;

    @BeforeEach
    void setUp() {
        commonExceptionHandlerUnderTest = new CommonExceptionHandler();
    }

    @Test
    void testHandleBusinessException() {
        // Setup
        final BusinessException ex = new BusinessException("message");

        // Run the test
        final JsonResult<Object> result = commonExceptionHandlerUnderTest.handleBusinessException(ex);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "field", "not null"));

        MethodArgumentNotValidException exceptionMock = Mockito.mock(MethodArgumentNotValidException.class);
        Mockito.when(exceptionMock.getBindingResult()).thenReturn(bindingResult);
        // Run the test
        final JsonResult<Object> result = commonExceptionHandlerUnderTest.handleMethodArgumentNotValidException(exceptionMock);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    void testHandleException() {
        // Setup
        // Run the test
        final JsonResult<Object> result = commonExceptionHandlerUnderTest.handleException(new Exception("message"));

        // Verify the results
        assertNotNull(result);
    }
}
