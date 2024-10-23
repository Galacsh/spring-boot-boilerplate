package io.galacsh.validation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class UserController implements UserControllerSpec {

    @Override
    public String onMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String createUser(User user) {
        return user.toString();
    }

    @Override
    public String createAdultUser(User user) {
        return user.toString();
    }
}
