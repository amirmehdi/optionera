package com.gitlab.amirmehdi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A InstrumentHistory.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "instrument_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@IdClass(InstrumentHistoryCompositeKey.class)
public class InstrumentHistory implements Serializable {

    @Id
    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

    @Id
    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "last", nullable = false)
    private Integer last;

    @NotNull
    @Column(name = "close", nullable = false)
    private Integer close;

    @NotNull
    @Column(name = "first", nullable = false)
    private Integer first;

    @NotNull
    @Column(name = "reference_price", nullable = false)
    private Integer referencePrice;

    @NotNull
    @Column(name = "low", nullable = false)
    private Integer low;

    @NotNull
    @Column(name = "high", nullable = false)
    private Integer high;

    @NotNull
    @Column(name = "trade_count", nullable = false)
    private Integer tradeCount;

    @NotNull
    @Column(name = "trade_volume", nullable = false)
    private Long tradeVolume;

    @NotNull
    @Column(name = "trade_value", nullable = false)
    private Long tradeValue;

}
