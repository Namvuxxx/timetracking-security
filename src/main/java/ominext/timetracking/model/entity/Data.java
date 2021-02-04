package ominext.timetracking.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Data extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Integer month;
    private Integer year;
    private long realTime;      /*Thời gian làm thực sau khi tính toán*/
    private long timeWorking;   /*Thời gian làm dựa theo bảng timetracking*/
    private long timeOff;       /*Thời gian nghỉ*/
    private long timeOffLeft;   /*Thời gian phép tồn trong 1 tháng*/
    private long timeOT;        /*Thời gian OT trong tháng*/


    @ManyToOne
    private Employee employee;

}
