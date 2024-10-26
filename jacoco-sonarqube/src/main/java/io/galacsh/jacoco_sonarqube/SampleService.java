package io.galacsh.jacoco_sonarqube;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public String printsDifferentMessage(boolean flag) {
        if (flag) {
            return "Hello, World!";
        } else {
            return "Goodbye, World!";
        }
    }
}
