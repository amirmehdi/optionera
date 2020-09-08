package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Option;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
