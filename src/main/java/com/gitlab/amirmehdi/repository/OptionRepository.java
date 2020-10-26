package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option> {
    Optional<Option> findByInstrumentIsinAndStrikePriceAndExpDate(String isin, Integer strikePrice, LocalDate expDate);

    List<Option> findAllByInstrumentIsinIsIn(List<String> isins);

    List<Option> findAllByInstrumentIsin(String isin);

    @Query(value = "select o from Option o where" +
        " o.callIsin=?1 or " +
        " o.putIsin=?1 ")
    Optional<Option> findByCallIsinOrPutIsin(String isin);

    Optional<Option> findByCallIsin(String isin);

    Optional<Option> findByCallIsinAndPutIsin(String callIsin, String putIsin);

    List<Option> findAllByExpDateGreaterThanEqual(LocalDate date);

    Page<Option> findAllByExpDateGreaterThanEqual(LocalDate date, Pageable pageable);

    Page<Option> findAllByCallTseIdIsNullOrPutTseIdIsNull(Pageable pageable);

    List<Option> findAllByCallBreakEvenIsLessThanEqual(float maxThreshold);

    List<Option> findAllByExpDateAndCallInTheMoney(LocalDate localDate, boolean callIntThemMoney);

    List<Option> findAllByCallAskToBSLessThanEqualAndExpDateGreaterThanEqual(float maxThreshold, LocalDate localDate);

    @Transactional
    @Modifying
    @Query(value = "update Option  o set o.name = ?2," +
        " o.callTseId = ?3 " +
        " where o.callIsin =?1")
    void updateCallTseId(String isin, String name, String tseId);


    @Transactional
    @Modifying
    @Query(value = "update Option  o set o.name = ?2," +
        " o.putTseId = ?3 " +
        " where o.putIsin =?1")
    void updatePutTseId(String isin, String name, String tseId);
    /*
    have a bug
    @Transactional
    @Modifying
    @Query(value = "update Option o " +
        "set o.callAskToBS= :#{#option.callAskToBS}, " +
        "o.putAskToBS= :#{#option.putAskToBS}, " +
        "o.callBreakEven= :#{#option.callBreakEven}, " +
        "o.putBreakEven= :#{#option.putBreakEven}, " +
        "o.callLeverage= :#{#option.callLeverage}, " +
        "o.putLeverage= :#{#option.putLeverage}, " +
        "o.callInTheMoney= :#{#option.callInTheMoney} " +
        "where o.id= :#{#option.id}")
    void updateParam(Option option);*/

    void deleteAllByExpDateBefore(LocalDate localDate);

    List<Option> findAllOptionsByExpDate(LocalDate now);
}
