package com.gitlab.amirmehdi.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Criteria class for the {@link com.gitlab.amirmehdi.domain.Option} entity. This class is used
 * in {@link com.gitlab.amirmehdi.web.rest.OptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@EqualsAndHashCode
@Getter
@Setter
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

    private LongFilter callTradeVolume;

    private LongFilter putTradeVolume;

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
        this.callTradeVolume = other.callTradeVolume == null ? null : other.callTradeVolume.copy();
        this.putTradeVolume = other.putTradeVolume == null ? null : other.putTradeVolume.copy();
        this.instrumentId = other.instrumentId == null ? null : other.instrumentId.copy();
    }

    @Override
    public OptionCriteria copy() {
        return new OptionCriteria(this);
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
                (callTradeVolume != null ? "callTradeVolume=" + callTradeVolume + ", " : "") +
                (putTradeVolume != null ? "putTradeVolume=" + putTradeVolume + ", " : "") +
                (instrumentId != null ? "instrumentId=" + instrumentId + ", " : "") +
            "}";
    }

}
