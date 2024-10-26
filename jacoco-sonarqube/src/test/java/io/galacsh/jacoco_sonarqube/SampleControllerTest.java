package io.galacsh.jacoco_sonarqube;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SampleControllerTest {

    SampleService sampleService;
    SampleController sampleController;

    @BeforeEach
    void setUp() {
        sampleService = mock(SampleService.class);
        when(sampleService.printsDifferentMessage(true)).thenReturn("Hello, World!");
        when(sampleService.printsDifferentMessage(false)).thenReturn("Goodbye, World!");
        sampleController = new SampleController(sampleService);
    }

    @Test
    void When_FlagIsTrue_Expect_HelloWorld() {
        // given
        boolean flag = true;

        // when
        String result = sampleController.printsMessage(flag);

        // then
        assertEquals("Hello, World!", result);
    }

    @Test
    void When_FlagIsFalse_Expect_GoodbyeWorld() {
        // given
        boolean flag = false;

        // when
        String result = sampleController.printsMessage(flag);

        // then
        assertEquals("Goodbye, World!", result);
    }
}