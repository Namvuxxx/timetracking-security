package ominext.timetracking.repository;

import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;
import ominext.timetracking.model.entity.TimeOffOt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITimeOffOtRepository extends JpaRepository<TimeOffOt, Long> {


    @Query("select t from TimeOffOt t inner join t.employee e where e.id = ?1 and t.type = ?2 and " +
            " MONTH(t.startTime) = ?3 and YEAR(t.startTime) =?4")
    List<TimeOffOt> findAllMonth(long id, TypeTimeOffOtEnum type, int month, int year, Pageable pageable);

    @Query("select t from TimeOffOt t inner join t.employee e where e.id = ?1 and t.type =?2 and  YEAR(t.startTime) = ?3")
    List<TimeOffOt> findAllYear(long id, TypeTimeOffOtEnum type, int year, Pageable pageable);

    @Query("select t from TimeOffOt t inner join t.employee e where e.id = ?1 and t.type = ?2 and" +
            " t.status = ?3 and MONTH(t.startTime) = ?4 and YEAR(t.startTime) =?5")
    List<TimeOffOt> findToDoData(long id, TypeTimeOffOtEnum type, StatusTimeOffOtEnum status, int month, int year);

    @Query("select t from TimeOffOt t inner join t.employee e where e.id = ?1 and t.type = ?2 and" +
            " t.status = ?3 and MONTH(t.endTime) = ?4 and YEAR(t.endTime) =?5")
    List<TimeOffOt> findToDoDataByEndTime(long id, TypeTimeOffOtEnum type, StatusTimeOffOtEnum status, int month, int year);
}

