package io.galacsh.rest_docs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, world!";
    }
}
