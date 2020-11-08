package com.gitlab.amirmehdi.service.dto;

import com.gitlab.amirmehdi.domain.enumeration.AlgorithmSide;
import com.gitlab.amirmehdi.domain.enumeration.AlgorithmState;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitlab.amirmehdi.domain.Algorithm} entity. This class is used
 * in {@link com.gitlab.amirmehdi.web.rest.AlgorithmResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /algorithms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AlgorithmCriteria implements Serializable, Criteria {
    /**
     * Class for filtering AlgorithmSide
     */
    public static class AlgorithmSideFilter extends Filter<AlgorithmSide> {

        public AlgorithmSideFilter() {
        }

        public AlgorithmSideFilter(AlgorithmSideFilter filter) {
            super(filter);
        }

        @Override
        public AlgorithmSideFilter copy() {
            return new AlgorithmSideFilter(this);
        }

    }
    /**
     * Class for filtering AlgorithmState
     */
    public static class AlgorithmStateFilter extends Filter<AlgorithmState> {

        public AlgorithmStateFilter() {
        }

        public AlgorithmStateFilter(AlgorithmStateFilter filter) {
            super(filter);
        }

        @Override
        public AlgorithmStateFilter copy() {
            return new AlgorithmStateFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private AlgorithmSideFilter side;

    private AlgorithmStateFilter state;

    private StringFilter input;

    private IntegerFilter tradeVolumeLimit;

    private LongFilter tradeValueLimit;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private StringFilter isins;

    private LongFilter orderId;

    public AlgorithmCriteria() {
    }

    public AlgorithmCriteria(AlgorithmCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.side = other.side == null ? null : other.side.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.input = other.input == null ? null : other.input.copy();
        this.tradeVolumeLimit = other.tradeVolumeLimit == null ? null : other.tradeVolumeLimit.copy();
        this.tradeValueLimit = other.tradeValueLimit == null ? null : other.tradeValueLimit.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.isins = other.isins == null ? null : other.isins.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
    }

    @Override
    public AlgorithmCriteria copy() {
        return new AlgorithmCriteria(this);
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

    public AlgorithmSideFilter getSide() {
        return side;
    }

    public void setSide(AlgorithmSideFilter side) {
        this.side = side;
    }

    public AlgorithmStateFilter getState() {
        return state;
    }

    public void setState(AlgorithmStateFilter state) {
        this.state = state;
    }

    public StringFilter getInput() {
        return input;
    }

    public void setInput(StringFilter input) {
        this.input = input;
    }

    public IntegerFilter getTradeVolumeLimit() {
        return tradeVolumeLimit;
    }

    public void setTradeVolumeLimit(IntegerFilter tradeVolumeLimit) {
        this.tradeVolumeLimit = tradeVolumeLimit;
    }

    public LongFilter getTradeValueLimit() {
        return tradeValueLimit;
    }

    public void setTradeValueLimit(LongFilter tradeValueLimit) {
        this.tradeValueLimit = tradeValueLimit;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StringFilter getIsins() {
        return isins;
    }

    public void setIsins(StringFilter isins) {
        this.isins = isins;
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
        final AlgorithmCriteria that = (AlgorithmCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(side, that.side) &&
            Objects.equals(state, that.state) &&
            Objects.equals(input, that.input) &&
            Objects.equals(tradeVolumeLimit, that.tradeVolumeLimit) &&
            Objects.equals(tradeValueLimit, that.tradeValueLimit) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(isins, that.isins) &&
            Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        side,
        state,
        input,
        tradeVolumeLimit,
        tradeValueLimit,
        createdAt,
        updatedAt,
        isins,
        orderId
        );
    }

    @Override
    public String toString() {
        return "AlgorithmCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (side != null ? "side=" + side + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (input != null ? "input=" + input + ", " : "") +
                (tradeVolumeLimit != null ? "tradeVolumeLimit=" + tradeVolumeLimit + ", " : "") +
                (tradeValueLimit != null ? "tradeValueLimit=" + tradeValueLimit + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (isins != null ? "isins=" + isins + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
            "}";
    }

}
