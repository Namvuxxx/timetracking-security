package ominext.timetracking.service;

import ominext.timetracking.model.baseenum.StatusTimeOffOtEnum;
import ominext.timetracking.model.baseenum.TypeTimeOffOtEnum;
import ominext.timetracking.model.dto.TimeOffOtDTO;

import java.util.List;

public interface ITimeOffOtService {
    TimeOffOtDTO getById(long id, String token);

    List<TimeOffOtDTO> getAllMonth(long id, TypeTimeOffOtEnum type, int month, int year, int page, int size, String token);

    List<TimeOffOtDTO> getAllYear(long id, TypeTimeOffOtEnum type, int year, int page, int size, String token);

    TimeOffOtDTO create(TimeOffOtDTO dto, String token);

    TimeOffOtDTO update(TimeOffOtDTO dto, String token);

    TimeOffOtDTO approve(long idOffOT, StatusTimeOffOtEnum statusTimeOffOtEnum, String token);


}
