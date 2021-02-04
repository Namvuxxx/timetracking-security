package ominext.timetracking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeOffOtDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private TypeTimeOffOtEnum type;
    private StatusTimeOffOtEnum status;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
