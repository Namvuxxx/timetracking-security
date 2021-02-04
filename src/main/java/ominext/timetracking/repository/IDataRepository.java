package ominext.timetracking.repository;

import ominext.timetracking.model.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDataRepository extends JpaRepository<Data, Long> {
    @Query("select d from Data d inner  join d.employee e where e.id =?1 and d.month = ?2 and  d.year =?3")
    Optional<Data> findDataByEmployeeId(long id, int month, int year);

    @Query("select d from Data d inner  join d.employee e where e.id =?1 and d.year = ?2")
    List<Data> findAllYear(long id, int year);
}
