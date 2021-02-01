package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
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
        "               and b.tradeValue>100000000")
    List<Board> findAllForLegalSupplyStrategy();

    @Query(nativeQuery = true,
    value = "delete " +
        "from board b using option o where (o.call_isin = b.isin or o.put_isin=b.isin) and o.exp_date<now()")
    @Modifying
    void deleteAllByExpDateBefore();

    @Query(nativeQuery = true,
    value = "delete " +
        "from board b using embedded_option o where (o.isin = b.isin) and o.exp_date<now()")
    @Modifying
    void deleteAllEmbeddedOptionsByExpDateBefore();
}
