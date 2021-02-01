package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.EmbeddedOption;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the EmbeddedOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmbeddedOptionRepository extends JpaRepository<EmbeddedOption, Long>, JpaSpecificationExecutor<EmbeddedOption> {

    @Query(nativeQuery = true, value = "delete " +
        "from embedded_option o where o.exp_date<now()")
    @Modifying
    void deleteAllByExpDateBefore();

    Optional<EmbeddedOption> findByIsin(String isin);


}
