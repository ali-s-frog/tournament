package io.robusta.tournament.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.robusta.tournament.common.exception.DomainException;
import io.robusta.tournament.common.exception.ResourceNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DummyControllerForTestingDomainException.class)
public class DomainExceptionHandlingTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenDomainExceptionThrown_thenReturnsStatus422() throws Exception {
        DummyInputForTestingDomainExceptionHandling input = new DummyInputForTestingDomainExceptionHandling("test");
        String body = objectMapper.writeValueAsString(input);

        mvc.perform(post("/dummy-domain-exception")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isUnprocessableEntity()).andExpect(content().json(
                "{\"code\":\"dummy.domain.exception\",\"message\":\"This message is being used for testing purposes. Params param1, param2\"}"
        ));
    }

    @Test
    void whenResourceNotFound_thenReturnsStatus404() throws Exception {
        DummyInputForTestingDomainExceptionHandling input = new DummyInputForTestingDomainExceptionHandling("test");
        String body = objectMapper.writeValueAsString(input);

        mvc.perform(get("/dummy-domain-exception/12345")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        "{\"code\":\"resource.not.found\",\"message\":\"Resource: DummyInputForTestingDomainExceptionHandling - identifier: 12345\"}"
                ));
    }
}

@RestController
class DummyControllerForTestingDomainException {

    @PostMapping("/dummy-domain-exception")
    public void create(@RequestBody DummyInputForTestingDomainExceptionHandling body) {
        throw new DummyExceptionForTestingDomainExceptionHandling("param1", "param2");
    }

    @GetMapping("/dummy-domain-exception/{id}")
    public void get(@PathVariable Long id) {
        throw new ResourceNotFoundException(DummyInputForTestingDomainExceptionHandling.class, id);
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class DummyInputForTestingDomainExceptionHandling {
    private String name;
}

class DummyExceptionForTestingDomainExceptionHandling extends DomainException {

    public DummyExceptionForTestingDomainExceptionHandling(String param1, String param2) {
        super("dummy.domain.exception", param1, param2);
    }
}
