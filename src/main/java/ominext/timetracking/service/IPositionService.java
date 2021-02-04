package ominext.timetracking.service;

import ominext.timetracking.model.dto.PositionDTO;

import java.util.List;

public interface IPositionService {

    List<PositionDTO> getAll(int page, int size, String token);

    PositionDTO getById(long id, String token);

    PositionDTO create(PositionDTO positionDTO, String token);

    PositionDTO update(PositionDTO positionDTO, String token);


}
