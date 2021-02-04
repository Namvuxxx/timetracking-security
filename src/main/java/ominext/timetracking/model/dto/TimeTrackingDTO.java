package ominext.timetracking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTrackingDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private LocalDate date;
    private LocalTime timeChecking;

}

