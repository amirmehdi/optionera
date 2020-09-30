package com.gitlab.amirmehdi.repository;

import com.gitlab.amirmehdi.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data  repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option> {
    Optional<Option> findByInstrumentIsinAndStrikePriceAndExpDate(String isin, Integer strikePrice, LocalDate expDate);

    @Query(value = "select o from Option o where" +
        " o.callIsin=?1 or " +
        " o.putIsin=?1 ")
    Optional<Option> findByCallIsinOrPutIsin(String isin);

    Optional<Option> findByCallIsinAndPutIsin(String callIsin,String putIsin);

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
}
