package com.gitlab.amirmehdi.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitlab.amirmehdi.domain.Board} entity. This class is used
 * in {@link com.gitlab.amirmehdi.web.rest.BoardResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /boards?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BoardCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter isin;

    private IntegerFilter last;

    private IntegerFilter close;

    private IntegerFilter first;

    private IntegerFilter low;

    private IntegerFilter high;

    private IntegerFilter min;

    private IntegerFilter max;

    private IntegerFilter tradeCount;

    private LongFilter tradeVolume;

    private LongFilter tradeValue;

    private IntegerFilter askPrice;

    private IntegerFilter askVolume;

    private IntegerFilter bidPrice;

    private IntegerFilter bidVolume;

    private LongFilter individualBuyVolume;

    private LongFilter individualSellVolume;

    private LongFilter legalBuyVolume;

    private LongFilter legalSellVolume;

    private IntegerFilter referencePrice;

    public BoardCriteria() {
    }

    public BoardCriteria(BoardCriteria other) {
        this.isin = other.isin == null ? null : other.isin.copy();
        this.last = other.last == null ? null : other.last.copy();
        this.close = other.close == null ? null : other.close.copy();
        this.first = other.first == null ? null : other.first.copy();
        this.low = other.low == null ? null : other.low.copy();
        this.high = other.high == null ? null : other.high.copy();
        this.min = other.min == null ? null : other.min.copy();
        this.max = other.max == null ? null : other.max.copy();
        this.tradeCount = other.tradeCount == null ? null : other.tradeCount.copy();
        this.tradeVolume = other.tradeVolume == null ? null : other.tradeVolume.copy();
        this.tradeValue = other.tradeValue == null ? null : other.tradeValue.copy();
        this.askPrice = other.askPrice == null ? null : other.askPrice.copy();
        this.askVolume = other.askVolume == null ? null : other.askVolume.copy();
        this.bidPrice = other.bidPrice == null ? null : other.bidPrice.copy();
        this.bidVolume = other.bidVolume == null ? null : other.bidVolume.copy();
        this.individualBuyVolume = other.individualBuyVolume == null ? null : other.individualBuyVolume.copy();
        this.individualSellVolume = other.individualSellVolume == null ? null : other.individualSellVolume.copy();
        this.legalBuyVolume = other.legalBuyVolume == null ? null : other.legalBuyVolume.copy();
        this.legalSellVolume = other.legalSellVolume == null ? null : other.legalSellVolume.copy();
        this.referencePrice = other.referencePrice == null ? null : other.referencePrice.copy();
    }

    @Override
    public BoardCriteria copy() {
        return new BoardCriteria(this);
    }

    public StringFilter getIsin() {
        return isin;
    }

    public void setIsin(StringFilter isin) {
        this.isin = isin;
    }

    public IntegerFilter getLast() {
        return last;
    }

    public void setLast(IntegerFilter last) {
        this.last = last;
    }

    public IntegerFilter getClose() {
        return close;
    }

    public void setClose(IntegerFilter close) {
        this.close = close;
    }

    public IntegerFilter getFirst() {
        return first;
    }

    public void setFirst(IntegerFilter first) {
        this.first = first;
    }

    public IntegerFilter getLow() {
        return low;
    }

    public void setLow(IntegerFilter low) {
        this.low = low;
    }

    public IntegerFilter getHigh() {
        return high;
    }

    public void setHigh(IntegerFilter high) {
        this.high = high;
    }
    public IntegerFilter getMin() {
        return min;
    }

    public void setMin(IntegerFilter min) {
        this.min = min;
    }

    public IntegerFilter getMax() {
        return max;
    }

    public void setMax(IntegerFilter max) {
        this.max = max;
    }

    public IntegerFilter getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(IntegerFilter tradeCount) {
        this.tradeCount = tradeCount;
    }

    public LongFilter getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(LongFilter tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public LongFilter getTradeValue() {
        return tradeValue;
    }

    public void setTradeValue(LongFilter tradeValue) {
        this.tradeValue = tradeValue;
    }

    public IntegerFilter getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(IntegerFilter askPrice) {
        this.askPrice = askPrice;
    }

    public IntegerFilter getAskVolume() {
        return askVolume;
    }

    public void setAskVolume(IntegerFilter askVolume) {
        this.askVolume = askVolume;
    }

    public IntegerFilter getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(IntegerFilter bidPrice) {
        this.bidPrice = bidPrice;
    }

    public IntegerFilter getBidVolume() {
        return bidVolume;
    }

    public void setBidVolume(IntegerFilter bidVolume) {
        this.bidVolume = bidVolume;
    }

    public LongFilter getIndividualBuyVolume() {
        return individualBuyVolume;
    }

    public void setIndividualBuyVolume(LongFilter individualBuyVolume) {
        this.individualBuyVolume = individualBuyVolume;
    }

    public LongFilter getIndividualSellVolume() {
        return individualSellVolume;
    }

    public void setIndividualSellVolume(LongFilter individualSellVolume) {
        this.individualSellVolume = individualSellVolume;
    }

    public LongFilter getLegalBuyVolume() {
        return legalBuyVolume;
    }

    public void setLegalBuyVolume(LongFilter legalBuyVolume) {
        this.legalBuyVolume = legalBuyVolume;
    }

    public LongFilter getLegalSellVolume() {
        return legalSellVolume;
    }

    public void setLegalSellVolume(LongFilter legalSellVolume) {
        this.legalSellVolume = legalSellVolume;
    }

    public IntegerFilter getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(IntegerFilter referencePrice) {
        this.referencePrice = referencePrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BoardCriteria that = (BoardCriteria) o;
        return
            Objects.equals(isin, that.isin) &&
            Objects.equals(last, that.last) &&
            Objects.equals(close, that.close) &&
            Objects.equals(first, that.first) &&
            Objects.equals(low, that.low) &&
            Objects.equals(high, that.high) &&
            Objects.equals(tradeCount, that.tradeCount) &&
            Objects.equals(tradeVolume, that.tradeVolume) &&
            Objects.equals(tradeValue, that.tradeValue) &&
            Objects.equals(askPrice, that.askPrice) &&
            Objects.equals(askVolume, that.askVolume) &&
            Objects.equals(bidPrice, that.bidPrice) &&
            Objects.equals(bidVolume, that.bidVolume) &&
            Objects.equals(individualBuyVolume, that.individualBuyVolume) &&
            Objects.equals(individualSellVolume, that.individualSellVolume) &&
            Objects.equals(legalBuyVolume, that.legalBuyVolume) &&
            Objects.equals(legalSellVolume, that.legalSellVolume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        isin,
        last,
        close,
        first,
        low,
        high,
        tradeCount,
        tradeVolume,
        tradeValue,
        askPrice,
        askVolume,
        bidPrice,
        bidVolume,
        individualBuyVolume,
        individualSellVolume,
        legalBuyVolume,
        legalSellVolume
        );
    }

    @Override
    public String toString() {
        return "BoardCriteria{" +
                (isin != null ? "isin=" + isin + ", " : "") +
                (last != null ? "last=" + last + ", " : "") +
                (close != null ? "close=" + close + ", " : "") +
                (first != null ? "first=" + first + ", " : "") +
                (low != null ? "low=" + low + ", " : "") +
                (high != null ? "high=" + high + ", " : "") +
                (tradeCount != null ? "tradeCount=" + tradeCount + ", " : "") +
                (tradeVolume != null ? "tradeVolume=" + tradeVolume + ", " : "") +
                (tradeValue != null ? "tradeValue=" + tradeValue + ", " : "") +
                (askPrice != null ? "askPrice=" + askPrice + ", " : "") +
                (askVolume != null ? "askVolume=" + askVolume + ", " : "") +
                (bidPrice != null ? "bidPrice=" + bidPrice + ", " : "") +
                (bidVolume != null ? "bidVolume=" + bidVolume + ", " : "") +
                (individualBuyVolume != null ? "individualBuyVolume=" + individualBuyVolume + ", " : "") +
                (individualSellVolume != null ? "individualSellVolume=" + individualSellVolume + ", " : "") +
                (legalBuyVolume != null ? "legalBuyVolume=" + legalBuyVolume + ", " : "") +
                (legalSellVolume != null ? "legalSellVolume=" + legalSellVolume + ", " : "") +
            "}";
    }

}
