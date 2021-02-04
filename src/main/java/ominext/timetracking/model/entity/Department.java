package ominext.timetracking.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter

@NoArgsConstructor
@Entity
@Table
public class Department extends BaseEntity {
    @Id
    private long id;
    private String name;

    public Department(long id) {
        this.id = id;
    }


}
