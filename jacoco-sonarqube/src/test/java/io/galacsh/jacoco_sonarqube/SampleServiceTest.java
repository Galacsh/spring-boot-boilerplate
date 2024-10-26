package io.galacsh.jacoco_sonarqube;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SampleServiceTest {

    SampleService sampleService = new SampleService();

    @Test
    void When_FlagIsTrue_Expect_HelloWorld() {
        // given
        boolean flag = true;

        // when
        String result = sampleService.printsDifferentMessage(flag);

        // then
        assertEquals("Hello, World!", result);
    }

    @Test
    void When_FlagIsFalse_Expect_GoodbyeWorld() {
        // given
        boolean flag = false;

        // when
        String result = sampleService.printsDifferentMessage(flag);

        // then
        assertEquals("Goodbye, World!", result);
    }
}