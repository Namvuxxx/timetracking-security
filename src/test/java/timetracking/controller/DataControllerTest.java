package timetracking.controller;

import ominext.timetracking.controller.DataController;
import ominext.timetracking.model.dto.DataDTO;
import ominext.timetracking.service.DataServiceImpl;
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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataController.class})
@WebMvcTest(value = DataController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class DataControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private DataServiceImpl service;


    @Test
    public void whenGetDataMonth_thenReturnDataDTO() throws Exception {
        DataDTO dto = new DataDTO(1, 12, 2020, 2000,
                1800, 90, 480, 120, 480);

        Mockito.when(service.getDataMonth(5, 12, 2020, "token")).thenReturn(dto);
        mvc.perform(MockMvcRequestBuilders.get("/api/data/month")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("month", "12")
                .param("year", "2020")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.month").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2020))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workingTime").value(2000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.realTime").value(1800))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeLate").value(90))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeOff").value(480))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeOT").value(480));


    }

    @Test
    public void whenGetDataMonth_ThenReturn403() throws Exception {
        Mockito.when(service.getDataMonth(5, 12, 2020, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/data/month")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("month", "12")
                .param("year", "2020")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenGetDataYear_thenReturnList() throws Exception {
        DataDTO dto1 = new DataDTO(1, 12, 2020, 2000,
                1800, 90, 480, 120, 480);
        DataDTO dto2 = new DataDTO(2, 11, 2020, 2000,
                1800, 90, 480, 120, 480);

        List<DataDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);

        Mockito.when(service.getDataYear(5, 2020, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/data/year")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("year", "2020")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());
    }

    @Test
    public void whenGetDataYear_ThenReturn403() throws Exception {
        Mockito.when(service.getDataYear(5, 2020, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/data/year")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("year", "2020")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenDoData_thenReturnDataDTO() throws Exception {
        DataDTO dto = new DataDTO(1, 12, 2020, 2000,
                1800, 90, 480, 120, 480);

        Mockito.when(service.doData(5, 12, 2020, "token")).thenReturn(dto);
        mvc.perform(MockMvcRequestBuilders.post("/api/data/doData")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("month", "12")
                .param("year", "2020")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.month").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(2020))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workingTime").value(2000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.realTime").value(1800))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeLate").value(90))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeOff").value(480))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeOT").value(480));
    }

    @Test
    public void whenDoData_ThenReturn403() throws Exception {
        Mockito.when(service.doData(anyLong(), anyInt(), anyInt(), anyString())).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/api/data/doData")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("month", "12")
                .param("year", "2020")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

}
