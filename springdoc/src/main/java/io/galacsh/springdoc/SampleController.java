package io.galacsh.springdoc;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController implements SampleControllerSpec {

    @Override
    public String index(String name) {
        if (name == null) {
            return "Hello!";
        }

        return "Hello, " + name + "!";
    }
}
