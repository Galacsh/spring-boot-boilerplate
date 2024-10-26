package io.galacsh.jacoco_sonarqube;

import org.springframework.stereotype.Controller;

@Controller
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    public String printsMessage(boolean flag) {
        return sampleService.printsDifferentMessage(flag);
    }
}