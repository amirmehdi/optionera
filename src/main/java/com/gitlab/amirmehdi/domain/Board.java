package com.gitlab.amirmehdi.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * A Board.
 */
@Entity
@Table(name = "board")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Board {

    @Id
    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

    @NotNull
    @Column(name = "date", nullable = false)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "last")
    private Integer last;

    @Column(name = "close")
    private Integer close;

    @Column(name = "reference_price")
    private Integer referencePrice;

    @Column(name = "first")
    private Integer first;

    @Column(name = "low")
    private Integer low;

    @Column(name = "high")
    private Integer high;

    @Column(name = "trade_count")
    private Integer tradeCount;

    @Column(name = "trade_volume")
    private Long tradeVolume;

    @Column(name = "trade_value")
    private Long tradeValue;

    @Column(name = "ask_price")
    private Integer askPrice;

    @Column(name = "ask_volume")
    private Integer askVolume;

    @Column(name = "bid_price")
    private Integer bidPrice;

    @Column(name = "bid_volume")
    private Integer bidVolume;

    @Column(name = "individual_buy_volume")
    private Integer individualBuyVolume;

    @Column(name = "individual_sell_volume")
    private Integer individualSellVolume;

    @Column(name = "legal_buy_volume")
    private Integer legalBuyVolume;

    @Column(name = "legal_sell_volume")
    private Integer legalSellVolume;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getIsin() {
        return isin;
    }

    public Board isin(String isin) {
        this.isin = isin;
        return this;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public Date getDate() {
        return date;
    }

    public Board date(Date date) {
        this.date = date;
        return this;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getLast() {
        return last;
    }

    public Board last(Integer last) {
        this.last = last;
        return this;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public Integer getClose() {
        return close;
    }

    public Board close(Integer close) {
        this.close = close;
        return this;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public Integer getReferencePrice() {
        return referencePrice;
    }

    public Board referencePrice(Integer referencePrice) {
        this.referencePrice = referencePrice;
        return this;
    }

    public void setReferencePrice(Integer referencePrice) {
        this.referencePrice = referencePrice;
    }

    public Integer getFirst() {
        return first;
    }

    public Board first(Integer first) {
        this.first = first;
        return this;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getLow() {
        return low;
    }

    public Board low(Integer low) {
        this.low = low;
        return this;
    }

    public void setLow(Integer low) {
        this.low = low;
    }

    public Integer getHigh() {
        return high;
    }

    public Board high(Integer high) {
        this.high = high;
        return this;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public Board tradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
        return this;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public Long getTradeVolume() {
        return tradeVolume;
    }

    public Board tradeVolume(Long tradeVolume) {
        this.tradeVolume = tradeVolume;
        return this;
    }

    public void setTradeVolume(Long tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public Long getTradeValue() {
        return tradeValue;
    }

    public Board tradeValue(Long tradeValue) {
        this.tradeValue = tradeValue;
        return this;
    }

    public void setTradeValue(Long tradeValue) {
        this.tradeValue = tradeValue;
    }

    public Integer getAskPrice() {
        return askPrice;
    }

    public Board askPrice(Integer askPrice) {
        this.askPrice = askPrice;
        return this;
    }

    public void setAskPrice(Integer askPrice) {
        this.askPrice = askPrice;
    }

    public Integer getAskVolume() {
        return askVolume;
    }

    public Board askVolume(Integer askVolume) {
        this.askVolume = askVolume;
        return this;
    }

    public void setAskVolume(Integer askVolume) {
        this.askVolume = askVolume;
    }

    public Integer getBidPrice() {
        return bidPrice;
    }

    public Board bidPrice(Integer bidPrice) {
        this.bidPrice = bidPrice;
        return this;
    }

    public void setBidPrice(Integer bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Integer getBidVolume() {
        return bidVolume;
    }

    public Board bidVolume(Integer bidVolume) {
        this.bidVolume = bidVolume;
        return this;
    }

    public void setBidVolume(Integer bidVolume) {
        this.bidVolume = bidVolume;
    }

    public Integer getIndividualBuyVolume() {
        return individualBuyVolume;
    }

    public Board individualBuyVolume(Integer individualBuyVolume) {
        this.individualBuyVolume = individualBuyVolume;
        return this;
    }

    public void setIndividualBuyVolume(Integer individualBuyVolume) {
        this.individualBuyVolume = individualBuyVolume;
    }

    public Integer getIndividualSellVolume() {
        return individualSellVolume;
    }

    public Board individualSellVolume(Integer individualSellVolume) {
        this.individualSellVolume = individualSellVolume;
        return this;
    }

    public void setIndividualSellVolume(Integer individualSellVolume) {
        this.individualSellVolume = individualSellVolume;
    }

    public Integer getLegalBuyVolume() {
        return legalBuyVolume;
    }

    public Board legalBuyVolume(Integer legalBuyVolume) {
        this.legalBuyVolume = legalBuyVolume;
        return this;
    }

    public void setLegalBuyVolume(Integer legalBuyVolume) {
        this.legalBuyVolume = legalBuyVolume;
    }

    public Integer getLegalSellVolume() {
        return legalSellVolume;
    }

    public Board legalSellVolume(Integer legalSellVolume) {
        this.legalSellVolume = legalSellVolume;
        return this;
    }

    public void setLegalSellVolume(Integer legalSellVolume) {
        this.legalSellVolume = legalSellVolume;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board)) {
            return false;
        }
        return isin != null && isin.equals(((Board) o).isin);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Board{" +
            ", isin='" + getIsin() + "'" +
            ", date='" + getDate() + "'" +
            ", last=" + getLast() +
            ", close=" + getClose() +
            ", first=" + getFirst() +
            ", low=" + getLow() +
            ", high=" + getHigh() +
            ", tradeCount=" + getTradeCount() +
            ", tradeVolume=" + getTradeVolume() +
            ", tradeValue=" + getTradeValue() +
            ", askPrice=" + getAskPrice() +
            ", askVolume=" + getAskVolume() +
            ", bidPrice=" + getBidPrice() +
            ", bidVolume=" + getBidVolume() +
            ", individualBuyVolume=" + getIndividualBuyVolume() +
            ", individualSellVolume=" + getIndividualSellVolume() +
            ", legalBuyVolume=" + getLegalBuyVolume() +
            ", legalSellVolume=" + getLegalSellVolume() +
            "}";
    }
}
