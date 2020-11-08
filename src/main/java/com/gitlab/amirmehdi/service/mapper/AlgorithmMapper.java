package com.gitlab.amirmehdi.service.mapper;


import com.gitlab.amirmehdi.domain.Algorithm;
import com.gitlab.amirmehdi.service.dto.AlgorithmDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Algorithm} and its DTO {@link AlgorithmDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlgorithmMapper extends EntityMapper<AlgorithmDTO, Algorithm> {


    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "removeOrder", ignore = true)
    Algorithm toEntity(AlgorithmDTO algorithmDTO);

    default Algorithm fromId(Long id) {
        if (id == null) {
            return null;
        }
        Algorithm algorithm = new Algorithm();
        algorithm.setId(id);
        return algorithm;
    }
}
