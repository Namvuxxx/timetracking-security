package timetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ominext.timetracking.controller.TimeTrackingController;
import ominext.timetracking.model.dto.TimeTrackingDTO;
import ominext.timetracking.service.TimeTrackingServiceImpl;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TimeTrackingController.class})
@WebMvcTest(value = TimeTrackingController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class TimeTrackingControllerTests {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private TimeTrackingServiceImpl service;


    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void whenGetAllDay_ThenReturnList() throws Exception {
        TimeTrackingDTO dto1 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(7, 45, 0));
        TimeTrackingDTO dto2 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(8, 45, 0));
        TimeTrackingDTO dto3 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(9, 45, 0));
        TimeTrackingDTO dto4 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(10, 45, 0));

        List<TimeTrackingDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);


        //Khong truyen parameter page va size
        Mockito.when(service.getAllDay(5, LocalDate.of(2020, 12, 12), 0, 20, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/tracking/day")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("date", "2020-12-12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());


        //Truyen tham so page va size
        Mockito.when(service.getAllDay(5,
                LocalDate.of(2020, 12, 12), 0, 10, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/tracking/day")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("date", "2020-12-12")
                .param("page", "0")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());

    }

    @Test
    public void whenGetAllDay_ThenReturn403() throws Exception {
        Mockito.when(service.getAllDay(5,
                LocalDate.of(2020, 12, 12), 0, 20, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/tracking/day")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("date", "2020-12-12"))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenGetAllMonth_ThenReturnList() throws Exception {
        TimeTrackingDTO dto1 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(7, 45, 0));
        TimeTrackingDTO dto2 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(8, 45, 0));
        TimeTrackingDTO dto3 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(9, 45, 0));
        TimeTrackingDTO dto4 = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(10, 45, 0));

        List<TimeTrackingDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);


        //Khong truyen parameter page va size
        Mockito.when(service.getAllMonth(5, 12, 2020, 0, 20, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/tracking/month")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("month", "12")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());


        //Truyen tham so page va size
        Mockito.when(service.getAllMonth(5, 12, 2020, 0, 10, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/tracking/month")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("month", "12")
                .param("year", "2020")
                .param("page", "0")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());
    }

    @Test
    public void whenGetAllMonth_ThenReturn403() throws Exception {
        Mockito.when(service.getAllMonth(5, 12, 2020, 0, 20, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/tracking/month")
                .header(AUTHORIZATION, "token")
                .param("id", "5")
                .param("month", "12")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenCreate_ThenReturnTimeTrackingDTO() throws Exception {
        TimeTrackingDTO dto = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(7, 45, 0));

        Mockito.when(service.create(anyLong(), any(TimeTrackingDTO.class), anyString())).thenReturn(dto);
        mvc.perform(MockMvcRequestBuilders.post("/api/tracking/create/{id}", 1)
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2020-12-12"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeChecking").value("07:45:00"));

    }

    @Test
    public void whenCreate_ThenReturn403() throws Exception {

        TimeTrackingDTO dto = new TimeTrackingDTO(1, LocalDate.of(2020, 12, 12)
                , LocalTime.of(7, 45, 0));

        Mockito.when(service.create(anyLong(), any(TimeTrackingDTO.class), anyString())).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post("/api/tracking/create/{id}", 1)
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));

    }
}
