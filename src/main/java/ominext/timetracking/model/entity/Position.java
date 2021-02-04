package ominext.timetracking.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Position extends BaseEntity {
    @Id
    private long id;
    private String name;

    public Position(long id) {
        this.id = id;
    }
//    @OneToMany
//    private List<Employee> employees;
}
