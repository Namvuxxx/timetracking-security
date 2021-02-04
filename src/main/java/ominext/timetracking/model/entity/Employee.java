package ominext.timetracking.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ominext.timetracking.model.baseenum.RoleEmployeeEnum;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Employee extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    private long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEmployeeEnum role;
    @Size(min = 5, max = 50)
    private String name;
    private String sex;
    private LocalDate birthdate;
    private String email;
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private long manager;

    private boolean isLogin;

    private long timeOff; // thoi gian nghi phep quy dinh
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    @Column(name = "start_breaktime")
    private LocalTime startBreaktime;
    @Column(name = "end_breaktime")
    private LocalTime endBreaktime;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private List<TimeTracking> timeTrackinglist;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private List<Data> datalist;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private List<TimeOffOt> off_otlist;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;


}
