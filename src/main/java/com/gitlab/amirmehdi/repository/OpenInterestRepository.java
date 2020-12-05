package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.AssetCompositeKey;
import com.gitlab.amirmehdi.domain.OpenInterest;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the OpenInterest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OpenInterestRepository extends JpaRepository<OpenInterest, AssetCompositeKey> {
}
