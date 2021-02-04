package ominext.timetracking.repository;

import ominext.timetracking.model.entity.TimeTracking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITimeTrackingRepository extends JpaRepository<TimeTracking, Long> {
    @Query("select t from TimeTracking t inner join t.employee e where e.id = ?1 and MONTH(t.date) =?2 and YEAR(t.date) = ?3")
    List<TimeTracking> getAllMonth(long id, int month, int year, Pageable pageable);

    @Query("select t from  TimeTracking t inner join  t.employee e where e.id =?1 and  t.date = ?2")
    List<TimeTracking> getAllDate(long id, LocalDate date, Pageable pageable);

    @Query("select t from TimeTracking  t inner  join  t.employee e where e.id =?1 and DAY(t.date) =?2 and MONTH(t.date)=?3 and YEAR(t.date)=?4")
    List<TimeTracking> getAllDay(long id, int day, int month, int year);
}
