package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.EmbeddedOption;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmbeddedOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmbeddedOptionRepository extends JpaRepository<EmbeddedOption, Long>, JpaSpecificationExecutor<EmbeddedOption> {
}
