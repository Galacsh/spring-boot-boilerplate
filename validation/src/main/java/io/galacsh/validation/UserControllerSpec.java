package io.galacsh.validation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserControllerSpec {

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    String onMethodArgumentNotValid(MethodArgumentNotValidException e);

    @PostMapping
    String createUser(@Validated @RequestBody User user);

    @PostMapping("/adult")
    String createAdultUser(@Validated(Adult.class) @RequestBody User user);
}
