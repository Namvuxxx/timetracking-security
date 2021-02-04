package ominext.timetracking.service;

import ominext.timetracking.model.dto.PositionDTO;
import ominext.timetracking.model.entity.Position;
import ominext.timetracking.repository.IPositionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PositionServiceImpl implements IPositionService {

    @Autowired
    private IPositionRepository repository;

    @Autowired
    private CommonService commonService;

    @Override
    public List<PositionDTO> getAll(int page, int size, String token) {

        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token)) {   /*Neu khong la admin return null*/
            Pageable pageable = PageRequest.of(page, size);
            Type type = new TypeToken<List<PositionDTO>>() {
            }.getType();
            return mapper.map(repository.findAll(pageable), type);
        }
        return null;
    }

    @Override
    public PositionDTO getById(long id, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token) && repository.findById(id).isPresent()) {   /*Neu khong la admin return null*/
            return mapper.map(repository.findById(id).get(), PositionDTO.class);
        }
        return null;

    }

    @Override
    public PositionDTO create(PositionDTO dto, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token)) {   /*Neu khong la admin return null*/
            Position position = mapper.map(dto, Position.class);
            position.setCreatedAt(LocalDateTime.now());
            position.setCreatedBy(commonService.getId(token));
            repository.saveAndFlush(position);
            return dto;
        }
        return null;
    }

    @Override
    public PositionDTO update(PositionDTO dto, String token) {
        ModelMapper mapper = new ModelMapper();
        if (commonService.isAdmin(token)) {            /*Neu khong la admin return null*/
            if (repository.findById(dto.getId()).isPresent()) {
                Position position = mapper.map(dto, Position.class);
                Position trans = repository.findById(dto.getId()).get();
                position.setCreatedAt(trans.getCreatedAt());
                position.setCreatedBy(trans.getCreatedBy());
                position.setUpdatedAt(LocalDateTime.now());
                position.setUpdatedBy(commonService.getId(token));
                repository.saveAndFlush(position);
                return dto;
            }
        }
        return null;
    }


}
