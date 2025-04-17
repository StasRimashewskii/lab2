package com.example.inventions.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LoggingAspectTest {

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(joinPoint.getTarget()).thenReturn(new Object() {
            @Override
            public String toString() {
                return "TestController";
            }
        });
    }

    @Test
    void testLogControllerMethods_Success() throws Throwable {
        Object[] args = {"arg1", 123};
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn("Success");

        Object result = loggingAspect.logControllerMethods(joinPoint);

        assertEquals("Success", result);
    }

    @Test
    void testLogControllerMethods_Exception() throws Throwable {
        Object[] args = {"arg1", 123};
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Test exception"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            loggingAspect.logControllerMethods(joinPoint);
        });

        assertEquals("Test exception", ex.getMessage());
    }

    @Test
    void testLogControllerMethods_EmptyArgs() throws Throwable {
        when(joinPoint.getArgs()).thenReturn(new Object[]{});
        when(joinPoint.proceed()).thenReturn("Result with no args");

        Object result = loggingAspect.logControllerMethods(joinPoint);

        assertEquals("Result with no args", result);
    }
}
