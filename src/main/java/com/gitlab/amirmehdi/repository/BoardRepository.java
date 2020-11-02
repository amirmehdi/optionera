package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Board entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardRepository extends JpaRepository<Board, String>, JpaSpecificationExecutor<Board> {

    @Query(value = "select b " +
        " from Board b where b.isin like 'IRO9%' " +
        "               and 2*b.legalSellVolume>b.tradeVolume " +
        "               and b.tradeValue>10000000")
    List<Board> findAllForLegalSupplyStrategy();
}
