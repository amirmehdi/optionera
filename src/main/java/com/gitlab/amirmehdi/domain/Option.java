package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@ToString
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "call_isin", nullable = false)
    private String callIsin;

    @NotNull
    @Column(name = "put_isin", nullable = false)
    private String putIsin;

    @Column(name = "call_tse_id")
    private String callTseId;

    @Column(name = "put_tse_id")
    private String putTseId;

    @NotNull
    @Column(name = "exp_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expDate;

    @NotNull
    @Column(name = "strike_price", nullable = false)
    private Integer strikePrice;

    @Column(name = "contract_size")
    private Integer contractSize;

    @NotNull
    @Column(name = "call_in_the_money", nullable = false)
    private boolean callInTheMoney;

    @NotNull
    @Column(name = "call_break_even", nullable = false)
    private float callBreakEven;

    @NotNull
    @Column(name = "put_break_even", nullable = false)
    private float putBreakEven;

    @NotNull
    @Column(name = "call_ask_to_bs", nullable = false)
    private float callAskToBS;

    @NotNull
    @Column(name = "put_ask_to_bs", nullable = false)
    private float putAskToBS;

    @NotNull
    @Column(name = "call_leverage", nullable = false)
    private float callLeverage;

    @NotNull
    @Column(name = "put_leverage", nullable = false)
    private float putLeverage;

    @NotNull
    @Column(name = "call_margin")
    private int callMargin;

    @NotNull
    @Column(name = "put_margin")
    private int putMargin;

    @NotNull
    @Column(name = "call_hedge", nullable = false)
    private float callHedge;

    @NotNull
    @Column(name = "call_indifference", nullable = false)
    private float callIndifference;

    @NotNull
    @Column(name = "call_gain", nullable = false)
    private float callGain;

    @NotNull
    @Column(name = "call_gain_monthly", nullable = false)
    private float callGainMonthly;

    @Column(name = "call_trade_volume")
    private long callTradeVolume;

    @Column(name = "put_trade_volume")
    private long putTradeVolume;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instrument_id")
    @NotNull
    @JsonIgnoreProperties("options")
    private Instrument instrument;

    public Option name(String name) {
        this.name = name;
        return this;
    }

    public Option callIsin(String callIsin) {
        this.callIsin = callIsin;
        return this;
    }

    public Option putIsin(String putIsin) {
        this.putIsin = putIsin;
        return this;
    }

    public Option callTseId(String callTseId) {
        this.callTseId = callTseId;
        return this;
    }

    public Option putTseId(String putTseId) {
        this.putTseId = putTseId;
        return this;
    }

    public Option expDate(LocalDate expDate) {
        this.expDate = expDate;
        return this;
    }

    public Option strikePrice(Integer strikePrice) {
        this.strikePrice = strikePrice;
        return this;
    }

    public Option contractSize(Integer contractSize) {
        this.contractSize = contractSize;
        return this;
    }

    public Option callInTheMoney(boolean callInTheMoney) {
        this.callInTheMoney = callInTheMoney;
        return this;
    }

    public Option callBreakEven(float callBreakEven) {
        this.callBreakEven = callBreakEven;
        return this;
    }

    public Option putBreakEven(float putBreakEven) {
        this.putBreakEven = putBreakEven;
        return this;
    }

    public Option callAskToBS(float callAskToBS) {
        this.callAskToBS = callAskToBS;
        return this;
    }

    public Option putAskToBS(float putAskToBS) {
        this.putAskToBS = putAskToBS;
        return this;
    }

    public Option callLeverage(float callLeverage) {
        this.callLeverage = callLeverage;
        return this;
    }

    public Option putLeverage(float putLeverage) {
        this.putLeverage = putLeverage;
        return this;
    }

    public Option callMargin(int callMargin) {
        this.callMargin = callMargin;
        return this;
    }

    public Option putMargin(int putMargin) {
        this.putMargin = putMargin;
        return this;
    }

    public Option callHedge(float callHedge) {
        this.callHedge = callHedge;
        return this;
    }

    public Option callIndifference(float callIndifference) {
        this.callIndifference = callIndifference;
        return this;
    }

    public Option callGain(float callGain) {
        this.callGain = callGain;
        return this;
    }

    public Option callGainMonthly(float callGainMonthly) {
        this.callGainMonthly = callGainMonthly;
        return this;
    }

    public Option callTradeVolume(long callTradeVolume) {
        this.callTradeVolume = callTradeVolume;
        return this;
    }

    public Option putTradeVolume(long putTradeVolume) {
        this.putTradeVolume = putTradeVolume;
        return this;
    }

    public Option instrument(Instrument instrument) {
        this.instrument = instrument;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return id != null && id.equals(((Option) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
