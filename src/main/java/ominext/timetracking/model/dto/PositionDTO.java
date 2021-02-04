package ominext.timetracking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionDTO implements Serializable {
    private static final Long serialVersionUID = 1L;
    private long id;
    private String name;

}
