package io.galacsh.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class UserControllerSpecTest {

    @Autowired
    MockMvc mock;

    @Autowired
    ObjectMapper mapper;

    final String NAME_EXCEPTION_MESSAGE = "Name must start with a capital letter";
    final String NICKNAME_EXCEPTION_MESSAGE = "Nickname must start with a capital letter";
    final String ADULT_EXCEPTION_MESSAGE = "Only adult is allowed";

    @Test
    void When_ValidUser_Expect_CreateUser() throws Exception {
        User user = new User("John Doe", 5, "Doe");
        String userJson = mapper.writeValueAsString(user);

        var request = post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("John Doe (5)"));
    }

    @Test
    void When_NameNotCapitalized_Expect_NameException() throws Exception {
        User user = new User("john Doe", 5, "Doe");
        String userJson = mapper.writeValueAsString(user);

        var request = post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(NAME_EXCEPTION_MESSAGE))
                .andDo(printContent);
    }

    @Test
    void When_NicknameNotCapitalized_Expect_NicknameException() throws Exception {
        User user = new User("John Doe", 5, "doe");
        String userJson = mapper.writeValueAsString(user);

        var request = post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(NICKNAME_EXCEPTION_MESSAGE))
                .andDo(printContent);
    }

    @Test
    void When_BothNameNotCapitalized_Expect_BothException() throws Exception {
        User user = new User("john doe", 5, "doe");
        String userJson = mapper.writeValueAsString(user);

        var request = post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString(NAME_EXCEPTION_MESSAGE),
                        containsString(NICKNAME_EXCEPTION_MESSAGE)
                )))
                .andDo(printContent);
    }

    @Test
    void When_ValidAdult_Expect_CreateAdult() throws Exception {
        User user = new User("John Doe", 20, "Doe");
        String userJson = mapper.writeValueAsString(user);

        var request = post("/user/adult")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("John Doe (20)"))
                .andDo(printContent);
    }

    @Test
    void When_Teenager_Expect_AdultException() throws Exception {
        User user = new User("John Doe", 17, "Doe");
        String userJson = mapper.writeValueAsString(user);

        var request = post("/user/adult")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(ADULT_EXCEPTION_MESSAGE))
                .andDo(printContent);
    }

    @Test
    void When_AdultNicknameNotCapitalized_Expect_NicknameException() throws Exception {
        User user = new User("John Doe", 18, "doe");
        String userJson = mapper.writeValueAsString(user);

        var request = post("/user/adult")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(NICKNAME_EXCEPTION_MESSAGE))
                .andDo(printContent);
    }

    ResultHandler printContent = (MvcResult result) -> {
        try {
            System.out.printf("%n" + """
                    Response content:
                        %s
                    %n""".trim(), result.getResponse().getContentAsString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    };
}