package timetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ominext.timetracking.controller.TimeOffOtController;
import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;
import ominext.timetracking.model.dto.TimeOffOtDTO;
import ominext.timetracking.service.TimeOffOtServiceImpl;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TimeOffOtController.class})
@WebMvcTest(value = TimeOffOtController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class TimeOffOtControllerTest {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private TimeOffOtServiceImpl service;

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
    public void whenGetById_ThenReturnTimeOffOtDTO() throws Exception {
        TimeOffOtDTO dto = new TimeOffOtDTO(1, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi nghi",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        Mockito.when(service.getById(dto.getId(), "token")).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/{id}", 1)
                .header(AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("TIMEOFF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NEW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStart").value("2020-12-08T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeEnd").value("2020-12-10T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("Thich thi nghi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedBy").value(1));

    }

    @Test
    public void whenGetById_ThenReturn403() throws Exception {
        TimeOffOtDTO dto = new TimeOffOtDTO(1, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi nghi",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        Mockito.when(service.getById(dto.getId(), "token")).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/{id}", 1)
                .header(AUTHORIZATION, "token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenGetAllMonth_ThenReturnList() throws Exception {
        TimeOffOtDTO dto1 = new TimeOffOtDTO(1, TypeTimeOffOtEnum.OVERTIME, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi Ot",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);
        TimeOffOtDTO dto2 = new TimeOffOtDTO(2, TypeTimeOffOtEnum.OVERTIME, StatusTimeOffOtEnum.REJECTED,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi OT",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        List<TimeOffOtDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);
//Khong truyen parameter page va size
        Mockito.when(service.getAllMonth(1, TypeTimeOffOtEnum.OVERTIME, 12, 2020,
                0, 20, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/month").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("type", "OVERTIME")
                .param("month", "12")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());

// truyen parameter page va size
        Mockito.when(service.getAllMonth(1, TypeTimeOffOtEnum.OVERTIME, 12, 2020,
                0, 10, "token")).thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/month").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("page", "0")
                .param("size", "10")
                .param("type", "OVERTIME")
                .param("month", "12")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());

    }

    @Test
    public void whenGetAllMonth_ThenReturn403() throws Exception {
        //Khong truyen parameter page va size
        Mockito.when(service.getAllMonth(1, TypeTimeOffOtEnum.OVERTIME, 12, 2020,
                0, 20, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/month").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("type", "OVERTIME")
                .param("month", "12")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").doesNotExist());

        // truyen parameter page va size
        Mockito.when(service.getAllMonth(1, TypeTimeOffOtEnum.OVERTIME, 12, 2020,
                0, 10, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/month").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("page", "0")
                .param("size", "10")
                .param("type", "OVERTIME")
                .param("month", "12")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().is(403));

    }

    @Test
    public void whenGetAllYear_ThenReturnList() throws Exception {
        TimeOffOtDTO dto1 = new TimeOffOtDTO(1, TypeTimeOffOtEnum.OVERTIME, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi Ot",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);
        TimeOffOtDTO dto2 = new TimeOffOtDTO(2, TypeTimeOffOtEnum.OVERTIME, StatusTimeOffOtEnum.REJECTED,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi OT",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        List<TimeOffOtDTO> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);
//Khong truyen parameter page va size
        Mockito.when(service.getAllYear(1, TypeTimeOffOtEnum.OVERTIME, 2020, 0, 20, "token"))
                .thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/year").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("type", "OVERTIME")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());

// truyen parameter page va size
        Mockito.when(service.getAllYear(1, TypeTimeOffOtEnum.OVERTIME, 2020, 0, 10, "token"))
                .thenReturn(list);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/year").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("page", "0")
                .param("size", "10")
                .param("type", "OVERTIME")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists());
    }

    @Test
    public void whenGetAllYear_ThenReturn403() throws Exception {
        //Khong truyen parameter page va size
        Mockito.when(service.getAllYear(1, TypeTimeOffOtEnum.OVERTIME, 2020,
                0, 20, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/year").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("type", "OVERTIME")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").doesNotExist());

        // truyen parameter page va size
        Mockito.when(service.getAllYear(1, TypeTimeOffOtEnum.OVERTIME, 2020,
                0, 10, "token")).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.get("/api/offOt/year").header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("page", "0")
                .param("size", "10")
                .param("type", "OVERTIME")
                .param("year", "2020"))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenCreate_ThenReturnTimeOffOtDTO() throws Exception {
        TimeOffOtDTO dto = new TimeOffOtDTO(1, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi nghi",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        Mockito.when(service.create(any(TimeOffOtDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.post("/api/offOt/create")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("TIMEOFF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NEW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStart").value("2020-12-08T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeEnd").value("2020-12-10T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("Thich thi nghi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedBy").value(1));
    }

    @Test
    public void whenCreate_ThenReturn403() throws Exception {

        TimeOffOtDTO dto = new TimeOffOtDTO(1, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi nghi",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);


        Mockito.when(service.create(any(TimeOffOtDTO.class), anyString())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/api/offOt/create")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenUpdate_ThenReturnTimeOffOtDTO() throws Exception {
        TimeOffOtDTO dto = new TimeOffOtDTO(1, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi nghi",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        Mockito.when(service.update(any(TimeOffOtDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.put("/api/offOt/update")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("TIMEOFF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NEW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStart").value("2020-12-08T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeEnd").value("2020-12-10T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("Thich thi nghi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedBy").value(1));
    }

    @Test
    public void whenUpdate_ThenReturn403() throws Exception {
        TimeOffOtDTO dto = new TimeOffOtDTO(1, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.NEW,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi nghi",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);


        Mockito.when(service.update(any(TimeOffOtDTO.class), anyString())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.put("/api/offOt/update")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenApprove_ThenReturnTimeOffOtDTO() throws Exception {
        TimeOffOtDTO dto = new TimeOffOtDTO(1, TypeTimeOffOtEnum.TIMEOFF, StatusTimeOffOtEnum.APPROVED,
                LocalDateTime.of(2020, 12, 8, 8, 0, 0),
                LocalDateTime.of(2020, 12, 10, 8, 0, 0), "Thich thi nghi",
                LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        Mockito.when(service.approve(1, StatusTimeOffOtEnum.APPROVED, "token")).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.put("/api/offOt/approve")
                .header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("status", "APPROVED")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("TIMEOFF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("APPROVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStart").value("2020-12-08T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeEnd").value("2020-12-10T08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("Thich thi nghi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedBy").value(1));
    }

    @Test
    public void whenApprove_ThenReturn403() throws Exception {
        Mockito.when(service.approve(1, StatusTimeOffOtEnum.APPROVED, "token")).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.put("/api/offOt/approve")
                .header(AUTHORIZATION, "token")
                .param("id", "1")
                .param("status", "APPROVED")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }
}
