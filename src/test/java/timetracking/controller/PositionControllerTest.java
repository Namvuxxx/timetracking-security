package timetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ominext.timetracking.controller.PositionController;
import ominext.timetracking.model.dto.PositionDTO;
import ominext.timetracking.service.PositionServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PositionController.class})
@WebMvcTest(value = PositionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class PositionControllerTest {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private PositionServiceImpl service;


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void whenGetAll_ThenReturnList() throws Exception {
        PositionDTO dto1 = new PositionDTO(1, "BU1");
        PositionDTO dto2 = new PositionDTO(2, "BU2");
        List<PositionDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);

        // Khong truyen parameter page va size
        Mockito.when(service.getAll(0, 20, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/position/all")
                .header(AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());


        // truyen tham so parameter page va size
        Mockito.when(service.getAll(0, 10, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/position/all")
                .param("page", "0")
                .param("size", "10")
                .header(AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());
    }

    @Test
    public void whenGetAll_ThenReturn403() throws Exception {

        // Khong truyen parameter page va size
        Mockito.when(service.getAll(0, 20, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/position/all")
                .header(AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));


        // truyen tham so parameter page va size
        Mockito.when(service.getAll(0, 10, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/position/all")
                .param("page", "0")
                .param("size", "10")
                .header(AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));

    }

    @Test
    public void whenGetById_ThenReturnPositionDTO() throws Exception {
        PositionDTO dto = new PositionDTO(1, "BU1");

        Mockito.when(service.getById(1, "token")).thenReturn(dto);
        mvc.perform(MockMvcRequestBuilders.get("/api/position/{id}", 1)
                .header(AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("BU1"));

    }

    @Test
    public void whenGeById_ThenReturn403() throws Exception {
        PositionDTO dto = new PositionDTO(1, "BU1");

        Mockito.when(service.getById(1, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/position/{id}", 1)
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));

    }

    @Test
    public void whenCreate_ThenReturnPositionDTO() throws Exception {
        PositionDTO dto = new PositionDTO(1, "BU1");

        Mockito.when(service.create(any(PositionDTO.class), anyString())).thenReturn(dto);
        mvc.perform(MockMvcRequestBuilders.post("/api/position/create")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("BU1"));
    }

    @Test
    public void whenCreate_ThenReturn403() throws Exception {
        PositionDTO dto = new PositionDTO(1, "BU1");

        Mockito.when(service.create(dto, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/api/position/create")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));

    }

    @Test
    public void whenUpdate_ThenReturnPositionDTO() throws Exception {
        PositionDTO dto = new PositionDTO(1, "BU1");

        Mockito.when(service.update(any(PositionDTO.class), anyString())).thenReturn(dto);
        mvc.perform(MockMvcRequestBuilders.put("/api/position/update")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("BU1"));
    }

    @Test
    public void whenUpdate_ThenReturn403() throws Exception {
        PositionDTO dto = new PositionDTO(1, "BU1");

        Mockito.when(service.update(dto, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.put("/api/position/update")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

}
