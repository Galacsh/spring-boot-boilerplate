package io.galacsh.springdoc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/hello")
public interface SampleControllerSpec {

    @GetMapping({ "/{name}", "/", "" })
    @Operation(summary = "Greets you", description = "Greets you with your name.")
    String index(@Parameter(description = "Name to greet")
                 @PathVariable(required = false) String name);
}
