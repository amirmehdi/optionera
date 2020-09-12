package com.gitlab.amirmehdi.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A InstrumentHistory.
 */
@Entity
@Table(name = "instrument_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InstrumentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

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
    @Column(name = "trade_volume", nullable = false)
    private Long tradeVolume;

    @NotNull
    @Column(name = "trade_count", nullable = false)
    private Integer tradeCount;

    @NotNull
    @Column(name = "trade_value", nullable = false)
    private Long tradeValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsin() {
        return isin;
    }

    public InstrumentHistory isin(String isin) {
        this.isin = isin;
        return this;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public LocalDate getDate() {
        return date;
    }

    public InstrumentHistory date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getLast() {
        return last;
    }

    public InstrumentHistory last(Integer last) {
        this.last = last;
        return this;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public Integer getClose() {
        return close;
    }

    public InstrumentHistory close(Integer close) {
        this.close = close;
        return this;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public Integer getFirst() {
        return first;
    }

    public InstrumentHistory first(Integer first) {
        this.first = first;
        return this;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getReferencePrice() {
        return referencePrice;
    }

    public InstrumentHistory referencePrice(Integer referencePrice) {
        this.referencePrice = referencePrice;
        return this;
    }

    public void setReferencePrice(Integer referencePrice) {
        this.referencePrice = referencePrice;
    }

    public Integer getLow() {
        return low;
    }

    public InstrumentHistory low(Integer low) {
        this.low = low;
        return this;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getHigh() {
        return high;
    }

    public InstrumentHistory high(Integer high) {
        this.high = high;
        return this;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Long getTradeVolume() {
        return tradeVolume;
    }

    public InstrumentHistory tradeVolume(Long tradeVolume) {
        this.tradeVolume = tradeVolume;
        return this;
    }

    public void setTradeVolume(Long tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public InstrumentHistory tradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
        return this;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public Long getTradeValue() {
        return tradeValue;
    }

    public InstrumentHistory tradeValue(Long tradeValue) {
        this.tradeValue = tradeValue;
        return this;
    }

    public void setTradeValue(Long tradeValue) {
        this.tradeValue = tradeValue;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstrumentHistory)) {
            return false;
        }
        return id != null && id.equals(((InstrumentHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InstrumentHistory{" +
            "id=" + getId() +
            ", isin='" + getIsin() + "'" +
            ", date='" + getDate() + "'" +
            ", last=" + getLast() +
            ", close=" + getClose() +
            ", first=" + getFirst() +
            ", referencePrice=" + getReferencePrice() +
            ", low=" + getLow() +
            ", high=" + getHigh() +
            ", tradeVolume=" + getTradeVolume() +
            ", tradeCount=" + getTradeCount() +
            ", tradeValue=" + getTradeValue() +
            "}";
    }
}
