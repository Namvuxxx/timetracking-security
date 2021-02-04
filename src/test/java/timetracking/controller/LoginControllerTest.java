package timetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ominext.timetracking.controller.LoginController;
import ominext.timetracking.model.dto.LoginDTO;
import ominext.timetracking.service.LoginServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {LoginController.class})
@WebMvcTest(value = LoginController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private LoginServiceImpl service;


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void loginSuccessful() throws Exception {

        Mockito.when(service.isCorrect("username1", "password1")).thenReturn(true);
        Mockito.when(service.token("username1", "password1")).thenReturn("token");
        mvc.perform(MockMvcRequestBuilders.post("/login")
                .content(asJsonString(new LoginDTO("username1", "password1")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Login successful"))
                .andExpect(MockMvcResultMatchers.header().exists(AUTHORIZATION))
                .andExpect(MockMvcResultMatchers.header().string(AUTHORIZATION, "Bearer token"));
    }

    @Test
    public void loginUnSuccessful() throws Exception {
        Mockito.when(service.isCorrect("username1", "password1")).thenReturn(false);
        Mockito.when(service.token("username1", "password1")).thenReturn("token");
        mvc.perform(MockMvcRequestBuilders.post("/login")
                .content(asJsonString(new LoginDTO("username1", "password1")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(401))
                .andExpect(MockMvcResultMatchers.content().string("Login unsuccessful"))
                .andExpect(MockMvcResultMatchers.header().doesNotExist(AUTHORIZATION));
    }
}
