package com.gitlab.amirmehdi.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitlab.amirmehdi.domain.Option} entity. This class is used
 * in {@link com.gitlab.amirmehdi.web.rest.OptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter callIsin;

    private StringFilter putIsin;

    private LocalDateFilter expDate;

    private IntegerFilter strikePrice;

    private IntegerFilter contractSize;

    private BooleanFilter callInTheMoney;

    private FloatFilter callBreakEven;

    private FloatFilter putBreakEven;

    private FloatFilter callAskToBS;

    private FloatFilter putAskToBS;

    private FloatFilter callLeverage;

    private FloatFilter putLeverage;

    private FloatFilter callHedge;

    private FloatFilter callIndifference;

    private FloatFilter callGain;

    private FloatFilter callGainMonthly;

    private IntegerFilter callMargin;

    private IntegerFilter putMargin;

    private StringFilter instrumentId;

    public OptionCriteria() {
    }

    public OptionCriteria(OptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.callIsin = other.callIsin == null ? null : other.callIsin.copy();
        this.putIsin = other.putIsin == null ? null : other.putIsin.copy();
        this.expDate = other.expDate == null ? null : other.expDate.copy();
        this.strikePrice = other.strikePrice == null ? null : other.strikePrice.copy();
        this.contractSize = other.contractSize == null ? null : other.contractSize.copy();
        this.callInTheMoney = other.callInTheMoney == null ? null : other.callInTheMoney.copy();
        this.callBreakEven = other.callBreakEven == null ? null : other.callBreakEven.copy();
        this.putBreakEven = other.putBreakEven == null ? null : other.putBreakEven.copy();
        this.callAskToBS = other.callAskToBS == null ? null : other.callAskToBS.copy();
        this.putAskToBS = other.putAskToBS == null ? null : other.putAskToBS.copy();
        this.callLeverage = other.callLeverage == null ? null : other.callLeverage.copy();
        this.putLeverage = other.putLeverage == null ? null : other.putLeverage.copy();
        this.callHedge = other.callHedge == null ? null : other.callHedge.copy();
        this.callIndifference = other.callIndifference == null ? null : other.callIndifference.copy();
        this.callGain = other.callGain == null ? null : other.callGain.copy();
        this.callGainMonthly = other.callGainMonthly == null ? null : other.callGainMonthly.copy();
        this.callMargin = other.callMargin == null ? null : other.callMargin.copy();
        this.putMargin = other.putMargin == null ? null : other.putMargin.copy();
        this.instrumentId = other.instrumentId == null ? null : other.instrumentId.copy();
    }

    @Override
    public OptionCriteria copy() {
        return new OptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCallIsin() {
        return callIsin;
    }

    public void setCallIsin(StringFilter callIsin) {
        this.callIsin = callIsin;
    }

    public StringFilter getPutIsin() {
        return putIsin;
    }

    public void setPutIsin(StringFilter putIsin) {
        this.putIsin = putIsin;
    }

    public LocalDateFilter getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDateFilter expDate) {
        this.expDate = expDate;
    }

    public IntegerFilter getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(IntegerFilter strikePrice) {
        this.strikePrice = strikePrice;
    }

    public IntegerFilter getContractSize() {
        return contractSize;
    }

    public void setContractSize(IntegerFilter contractSize) {
        this.contractSize = contractSize;
    }

    public BooleanFilter getCallInTheMoney() {
        return callInTheMoney;
    }

    public void setCallInTheMoney(BooleanFilter callInTheMoney) {
        this.callInTheMoney = callInTheMoney;
    }

    public FloatFilter getCallBreakEven() {
        return callBreakEven;
    }

    public void setCallBreakEven(FloatFilter callBreakEven) {
        this.callBreakEven = callBreakEven;
    }

    public FloatFilter getPutBreakEven() {
        return putBreakEven;
    }

    public void setPutBreakEven(FloatFilter putBreakEven) {
        this.putBreakEven = putBreakEven;
    }

    public FloatFilter getCallAskToBS() {
        return callAskToBS;
    }

    public void setCallAskToBS(FloatFilter callAskToBS) {
        this.callAskToBS = callAskToBS;
    }

    public FloatFilter getPutAskToBS() {
        return putAskToBS;
    }

    public void setPutAskToBS(FloatFilter putAskToBS) {
        this.putAskToBS = putAskToBS;
    }

    public FloatFilter getCallLeverage() {
        return callLeverage;
    }

    public void setCallLeverage(FloatFilter callLeverage) {
        this.callLeverage = callLeverage;
    }

    public FloatFilter getPutLeverage() {
        return putLeverage;
    }

    public void setPutLeverage(FloatFilter putLeverage) {
        this.putLeverage = putLeverage;
    }

    public FloatFilter getCallHedge() {
        return callHedge;
    }

    public void setCallHedge(FloatFilter callHedge) {
        this.callHedge = callHedge;
    }

    public FloatFilter getCallIndifference() {
        return callIndifference;
    }

    public void setCallIndifference(FloatFilter callIndifference) {
        this.callIndifference = callIndifference;
    }

    public FloatFilter getCallGain() {
        return callGain;
    }

    public void setCallGain(FloatFilter callGain) {
        this.callGain = callGain;
    }

    public FloatFilter getCallGainMonthly() {
        return callGainMonthly;
    }

    public void setCallGainMonthly(FloatFilter callGainMonthly) {
        this.callGainMonthly = callGainMonthly;
    }

    public IntegerFilter getCallMargin() {
        return callMargin;
    }

    public void setCallMargin(IntegerFilter callMargin) {
        this.callMargin = callMargin;
    }

    public IntegerFilter getPutMargin() {
        return putMargin;
    }

    public void setPutMargin(IntegerFilter putMargin) {
        this.putMargin = putMargin;
    }

    public StringFilter getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(StringFilter instrumentId) {
        this.instrumentId = instrumentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OptionCriteria that = (OptionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(callIsin, that.callIsin) &&
            Objects.equals(putIsin, that.putIsin) &&
            Objects.equals(expDate, that.expDate) &&
            Objects.equals(strikePrice, that.strikePrice) &&
            Objects.equals(contractSize, that.contractSize) &&
            Objects.equals(callInTheMoney, that.callInTheMoney) &&
            Objects.equals(callBreakEven, that.callBreakEven) &&
            Objects.equals(putBreakEven, that.putBreakEven) &&
            Objects.equals(callAskToBS, that.callAskToBS) &&
            Objects.equals(putAskToBS, that.putAskToBS) &&
            Objects.equals(callLeverage, that.callLeverage) &&
            Objects.equals(putLeverage, that.putLeverage) &&
            Objects.equals(callHedge, that.callHedge) &&
            Objects.equals(callIndifference, that.callIndifference) &&
            Objects.equals(callGain, that.callGain) &&
            Objects.equals(callGainMonthly, that.callGainMonthly) &&
            Objects.equals(callMargin, that.callMargin) &&
            Objects.equals(putMargin, that.putMargin) &&
            Objects.equals(instrumentId, that.instrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        callIsin,
        putIsin,
        expDate,
        strikePrice,
        contractSize,
        callInTheMoney,
        callBreakEven,
        putBreakEven,
        callAskToBS,
        putAskToBS,
        callLeverage,
        putLeverage,
        callHedge,
        callIndifference,
        callGain,
        callGainMonthly,
        callMargin,
        putMargin,
        instrumentId
        );
    }

    @Override
    public String toString() {
        return "OptionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (callIsin != null ? "callIsin=" + callIsin + ", " : "") +
                (putIsin != null ? "putIsin=" + putIsin + ", " : "") +
                (expDate != null ? "expDate=" + expDate + ", " : "") +
                (strikePrice != null ? "strikePrice=" + strikePrice + ", " : "") +
                (contractSize != null ? "contractSize=" + contractSize + ", " : "") +
                (callInTheMoney != null ? "callInTheMoney=" + callInTheMoney + ", " : "") +
                (callBreakEven != null ? "callBreakEven=" + callBreakEven + ", " : "") +
                (putBreakEven != null ? "putBreakEven=" + putBreakEven + ", " : "") +
                (callAskToBS != null ? "callAskToBS=" + callAskToBS + ", " : "") +
                (putAskToBS != null ? "putAskToBS=" + putAskToBS + ", " : "") +
                (callLeverage != null ? "callLeverage=" + callLeverage + ", " : "") +
                (putLeverage != null ? "putLeverage=" + putLeverage + ", " : "") +
                (callHedge != null ? "callHedge=" + callHedge + ", " : "") +
                (callIndifference != null ? "callIndifference=" + callIndifference + ", " : "") +
                (callGain != null ? "callGain=" + callGain + ", " : "") +
                (callGainMonthly != null ? "callGainMonthly=" + callGainMonthly + ", " : "") +
                (callMargin != null ? "callMargin=" + callMargin + ", " : "") +
                (putMargin != null ? "putMargin=" + putMargin + ", " : "") +
                (instrumentId != null ? "instrumentId=" + instrumentId + ", " : "") +
            "}";
    }

}
