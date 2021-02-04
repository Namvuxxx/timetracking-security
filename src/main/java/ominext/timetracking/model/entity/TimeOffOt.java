package ominext.timetracking.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "timeoff_ot")
public class TimeOffOt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeTimeOffOtEnum type;

    @Enumerated(EnumType.STRING)
    private StatusTimeOffOtEnum status;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    private String reason;

    @ManyToOne
    private Employee employee;
}
