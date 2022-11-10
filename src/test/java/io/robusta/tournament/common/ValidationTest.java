package io.robusta.tournament.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DummyControllerForTestingValidation.class)
public class ValidationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenInputIsInvalid_thenReturnsStatus422() throws Exception {
        DummyInputForTestingValidation invalidInput = new DummyInputForTestingValidation("A");
        String body = objectMapper.writeValueAsString(invalidInput);

        mvc.perform(post("/dummy-validation")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isUnprocessableEntity()).andExpect(content().json(
                "[{\"code\":\"invalid.argument\",\"message\":\"field -- size must be between 2 and 10\"}]"
        ));
    }

    @Test
    void whenInputIsValid_thenReturnsStatus201() throws Exception {
        DummyInputForTestingValidation invalidInput = new DummyInputForTestingValidation("ABC");
        String body = objectMapper.writeValueAsString(invalidInput);

        mvc.perform(post("/dummy-validation")
                .contentType("application/json")
                .content(body))
                .andExpect(status().is2xxSuccessful());
    }
}

@RestController
class DummyControllerForTestingValidation {

    @PostMapping("/dummy-validation")
    public void testCreate(@Valid @RequestBody DummyInputForTestingValidation body) {
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class DummyInputForTestingValidation {
    @NotBlank
    @Size(min = 2, max = 10)
    private String field;
}
