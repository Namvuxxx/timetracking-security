package timetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ominext.timetracking.controller.EmployeeController;
import ominext.timetracking.model.baseenum.RoleEmployeeEnum;
import ominext.timetracking.model.dto.EmployeeDTO;
import ominext.timetracking.service.EmployeeServiceImpl;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmployeeController.class})
@WebMvcTest(value = EmployeeController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})

public class EmployeeControllerUnitTest {

    @Autowired
    WebApplicationContext context;
    @MockBean
    private EmployeeServiceImpl service;
    @Autowired
    private MockMvc mvc;


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

    public static String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void whenGetAll_ThenReturnList() throws Exception {

        List<EmployeeDTO> list = new ArrayList<>();
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "namvsvs", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);

        list.add(dto);
        Mockito.when(service.getAll(0, 20, "token")).thenReturn(list);

        //with parameter
        mvc.perform(MockMvcRequestBuilders.get("/api/employee/all")
                .param("page", "0")
                .param("size", "20")
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").isNotEmpty());

        //without parameter
        mvc.perform(MockMvcRequestBuilders.get("/api/employee/all")
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").isNotEmpty());
    }

    @Test
    public void whenGetAll_ThenReturn403() throws Exception {

        Mockito.when(service.getAll(0, 20, "token")).thenReturn(null);

        //with parameter
        mvc.perform(MockMvcRequestBuilders.get("/api/employee/all")
                .param("page", "0")
                .param("size", "20")
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));

        //without parameter
        mvc.perform(MockMvcRequestBuilders.get("/api/employee/all")
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenGetByUsername_ThenReturnEmployee() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.USER, "vsn", "namvsvs", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);
        Mockito.when(service.getByUsername("vsn", "token")).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.get("/api/employee/username/{username}", "vsn")
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("vsn"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("namvsvs"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("1993-03-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("huntingdogshero@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("0971425031"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manager").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeOff").value(480))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value("08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startBreaktime").value("11:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endBreaktime").value("13:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value("18:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(1));


    }

    @Test
    public void whenGetByUsername_ThenReturn403() throws Exception {

        Mockito.when(service.getByUsername("username", "token")).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/api/employee/username/{username}", "username")
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenGetById_ThenReturnEmployee() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "namvsvs", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);
        Mockito.when(service.getById(1, "token")).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.get("/api/employee/id/{id}", 1)
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ADMIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("vsn"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("namvsvs"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("1993-03-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("huntingdogshero@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("0971425031"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.manager").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeOff").value(480))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value("08:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startBreaktime").value("11:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endBreaktime").value("13:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value("18:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(1));
    }

    @Test
    public void whenGetById_ThenReturn403() throws Exception {
        Mockito.when(service.getById(1, "token")).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/api/employee/id/{id}", 1)
                .header(AUTHORIZATION, "token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenCreate_ThenReturnEmployeeDTO() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "namvsvs", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);


        Mockito.when(service.create(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.post("/api/employee/create")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201));

    }

    @Test
    public void whenCreateWithNameLengthOutOfRange_ThenReturn400() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "nam", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);


        Mockito.when(service.create(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.post("/api/employee/create")
                .header(AUTHORIZATION, "token")
                .content(toJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void whenCreateWithEmailInCorrect_ThenReturn400() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "vu", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);


        Mockito.when(service.create(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.post("/api/employee/create")
                .header(AUTHORIZATION, "token")
                .content(toJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void whenCreateWithPhoneNumberInCorrect_ThenReturn400() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "vu son nam", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "A971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);
        BindingResult errors = new DataBinder(dto).getBindingResult();
        System.out.println(errors.hasErrors());
        Mockito.when(service.create(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.post("/api/employee/create")
                .header(AUTHORIZATION, "token")
                .content(toJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void whenCreate_ThenReturn403() throws Exception {

        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "namvsvs", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);

        Mockito.when(service.create(any(EmployeeDTO.class), anyString())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/api/employee/create")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenUpdate_ThenReturnEmployeeDTO() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "namvsvs", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);

        Mockito.when(service.update(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.put("/api/employee/update")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateWithNameLengthOutOfRange_ThenReturn400() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "nam", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);


        Mockito.when(service.update(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.put("/api/employee/update")
                .header(AUTHORIZATION, "token")
                .content(toJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void whenUpdateWithEmailInCorrect_ThenReturn400() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "vu son nam", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);


        Mockito.when(service.update(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.put("/api/employee/update")
                .header(AUTHORIZATION, "token")
                .content(toJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void whenUpdateWithPhoneNumberInCorrect_ThenReturn400() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "vu son nam", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "A971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);


        Mockito.when(service.update(any(EmployeeDTO.class), anyString())).thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.put("/api/employee/update")
                .header(AUTHORIZATION, "token")
                .content(toJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void whenUpdate_ThenReturn403() throws Exception {
        EmployeeDTO dto = new EmployeeDTO(1, RoleEmployeeEnum.ADMIN, "vsn", "namvsvs", "male",
                LocalDate.of(1993, 3, 1), "huntingdogshero@gmail.com",
                "0971425031", 7, 480L,
                LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0),
                LocalTime.of(11, 0, 0), LocalTime.of(13, 0, 0), 1, 1);

        Mockito.when(service.update(any(EmployeeDTO.class), anyString())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.put("/api/employee/update")
                .header(AUTHORIZATION, "token")
                .content(asJsonString(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    public void whenDelete_ThenReturnOK() throws Exception {

        Mockito.when(service.isDeleted(1, "token")).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/api/employee/delete/{id}", 1)
                .header(AUTHORIZATION, "token"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenDelete_ThenReturn403() throws Exception {
        Mockito.when(service.isDeleted(1, "token")).thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.delete("/api/employee/delete/{id}", 1)
                .header(AUTHORIZATION, "token"))
                .andDo(print())
                .andExpect(status().is(403));
    }


}
