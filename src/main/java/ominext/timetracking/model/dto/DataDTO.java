package ominext.timetracking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    @Min(1)
    @Max(12)
    private Integer month;
    @Min(2010)
    private Integer year;
    private long timeWorking;
    private long realTime; /*Thời gian làm sau khi tính toán*/
    private long timeLate;
    @NotNull
    private long timeOff;
    private long timeOffLeft; /*Thời gian phép tồn trong 1 tháng*/
    private long timeOT;

}
