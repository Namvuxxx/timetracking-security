package ominext.timetracking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ominext.timetracking.model.baseenum.RoleEmployeeEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private RoleEmployeeEnum role;
    private String username;
    @Size(min = 6, max = 30)
    private String name;
    private String sex;
    private LocalDate birthDate;
    @Email
    private String email;
    @Pattern(regexp = "^[0-9]{10,11}$")
    private String phoneNumber;
    private long manager;
    private long timeOff; // Số giờ phép theo quy định doi ra phut
    private LocalTime startTime;
    private LocalTime EndTime;
    private LocalTime startBreaktime;
    private LocalTime endBreaktime;
    private long position;
    private long department;

}
