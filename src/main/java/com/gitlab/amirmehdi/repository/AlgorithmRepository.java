package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Algorithm;
import com.gitlab.amirmehdi.domain.enumeration.AlgorithmState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Algorithm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlgorithmRepository extends JpaRepository<Algorithm, Long>, JpaSpecificationExecutor<Algorithm> {
    List<Algorithm> findAllByTypeAndState(String type, AlgorithmState algorithmState);
}
