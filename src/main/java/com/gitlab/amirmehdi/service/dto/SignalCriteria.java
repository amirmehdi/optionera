package com.gitlab.amirmehdi.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitlab.amirmehdi.domain.Signal} entity. This class is used
 * in {@link com.gitlab.amirmehdi.web.rest.SignalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /signals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SignalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter isin;

    private IntegerFilter last;

    private LongFilter tradeVolume;

    private LongFilter bidVolume;

    private IntegerFilter bidPrice;

    private IntegerFilter askPrice;

    private LongFilter askVolume;

    private IntegerFilter baseInstrumentLast;

    private InstantFilter createdAt;

    private LongFilter orderId;

    public SignalCriteria() {
    }

    public SignalCriteria(SignalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.isin = other.isin == null ? null : other.isin.copy();
        this.last = other.last == null ? null : other.last.copy();
        this.tradeVolume = other.tradeVolume == null ? null : other.tradeVolume.copy();
        this.bidVolume = other.bidVolume == null ? null : other.bidVolume.copy();
        this.bidPrice = other.bidPrice == null ? null : other.bidPrice.copy();
        this.askPrice = other.askPrice == null ? null : other.askPrice.copy();
        this.askVolume = other.askVolume == null ? null : other.askVolume.copy();
        this.baseInstrumentLast = other.baseInstrumentLast == null ? null : other.baseInstrumentLast.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
    }

    @Override
    public SignalCriteria copy() {
        return new SignalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
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

    public LongFilter getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(LongFilter tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public LongFilter getBidVolume() {
        return bidVolume;
    }

    public void setBidVolume(LongFilter bidVolume) {
        this.bidVolume = bidVolume;
    }

    public IntegerFilter getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(IntegerFilter bidPrice) {
        this.bidPrice = bidPrice;
    }

    public IntegerFilter getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(IntegerFilter askPrice) {
        this.askPrice = askPrice;
    }

    public LongFilter getAskVolume() {
        return askVolume;
    }

    public void setAskVolume(LongFilter askVolume) {
        this.askVolume = askVolume;
    }

    public IntegerFilter getBaseInstrumentLast() {
        return baseInstrumentLast;
    }

    public void setBaseInstrumentLast(IntegerFilter baseInstrumentLast) {
        this.baseInstrumentLast = baseInstrumentLast;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SignalCriteria that = (SignalCriteria) o;
        return
            Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(isin, that.isin) &&
                Objects.equals(last, that.last) &&
                Objects.equals(tradeVolume, that.tradeVolume) &&
                Objects.equals(bidVolume, that.bidVolume) &&
                Objects.equals(bidPrice, that.bidPrice) &&
                Objects.equals(askPrice, that.askPrice) &&
                Objects.equals(askVolume, that.askVolume) &&
                Objects.equals(baseInstrumentLast, that.baseInstrumentLast) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            type,
            isin,
            last,
            tradeVolume,
            bidVolume,
            bidPrice,
            askPrice,
            askVolume,
            baseInstrumentLast,
            createdAt,
            orderId
        );
    }

    @Override
    public String toString() {
        return "SignalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (isin != null ? "isin=" + isin + ", " : "") +
            (last != null ? "last=" + last + ", " : "") +
            (tradeVolume != null ? "tradeVolume=" + tradeVolume + ", " : "") +
            (bidVolume != null ? "bidVolume=" + bidVolume + ", " : "") +
            (bidPrice != null ? "bidPrice=" + bidPrice + ", " : "") +
            (askPrice != null ? "askPrice=" + askPrice + ", " : "") +
            (askVolume != null ? "askVolume=" + askVolume + ", " : "") +
            (baseInstrumentLast != null ? "baseInstrumentLast=" + baseInstrumentLast + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            "}";
    }

}
