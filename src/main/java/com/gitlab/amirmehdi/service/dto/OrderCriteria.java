package com.gitlab.amirmehdi.service.dto;

import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitlab.amirmehdi.domain.Order} entity. This class is used
 * in {@link com.gitlab.amirmehdi.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Validity
     */
    public static class ValidityFilter extends Filter<Validity> {

        public ValidityFilter() {
        }

        public ValidityFilter(ValidityFilter filter) {
            super(filter);
        }

        @Override
        public ValidityFilter copy() {
            return new ValidityFilter(this);
        }

    }
    /**
     * Class for filtering Side
     */
    public static class SideFilter extends Filter<Side> {

        public SideFilter() {
        }

        public SideFilter(SideFilter filter) {
            super(filter);
        }

        @Override
        public SideFilter copy() {
            return new SideFilter(this);
        }

    }
    /**
     * Class for filtering Broker
     */
    public static class BrokerFilter extends Filter<Broker> {

        public BrokerFilter() {
        }

        public BrokerFilter(BrokerFilter filter) {
            super(filter);
        }

        @Override
        public BrokerFilter copy() {
            return new BrokerFilter(this);
        }

    }
    /**
     * Class for filtering OrderState
     */
    public static class OrderStateFilter extends Filter<OrderState> {

        public OrderStateFilter() {
        }

        public OrderStateFilter(OrderStateFilter filter) {
            super(filter);
        }

        @Override
        public OrderStateFilter copy() {
            return new OrderStateFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter isin;

    private IntegerFilter price;

    private IntegerFilter quantity;

    private ValidityFilter validity;

    private SideFilter side;

    private BrokerFilter broker;

    private StringFilter omsId;

    private OrderStateFilter state;

    private IntegerFilter executed;

    private StringFilter description;

    private LongFilter signalId;

    private LongFilter bourseCodeId;

    public OrderCriteria() {
    }

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.isin = other.isin == null ? null : other.isin.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.validity = other.validity == null ? null : other.validity.copy();
        this.side = other.side == null ? null : other.side.copy();
        this.broker = other.broker == null ? null : other.broker.copy();
        this.omsId = other.omsId == null ? null : other.omsId.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.executed = other.executed == null ? null : other.executed.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.signalId = other.signalId == null ? null : other.signalId.copy();
        this.bourseCodeId = other.bourseCodeId == null ? null : other.bourseCodeId.copy();
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIsin() {
        return isin;
    }

    public void setIsin(StringFilter isin) {
        this.isin = isin;
    }

    public IntegerFilter getPrice() {
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public ValidityFilter getValidity() {
        return validity;
    }

    public void setValidity(ValidityFilter validity) {
        this.validity = validity;
    }

    public SideFilter getSide() {
        return side;
    }

    public void setSide(SideFilter side) {
        this.side = side;
    }

    public BrokerFilter getBroker() {
        return broker;
    }

    public void setBroker(BrokerFilter broker) {
        this.broker = broker;
    }

    public StringFilter getOmsId() {
        return omsId;
    }

    public void setOmsId(StringFilter omsId) {
        this.omsId = omsId;
    }

    public OrderStateFilter getState() {
        return state;
    }

    public void setState(OrderStateFilter state) {
        this.state = state;
    }

    public IntegerFilter getExecuted() {
        return executed;
    }

    public void setExecuted(IntegerFilter executed) {
        this.executed = executed;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getSignalId() {
        return signalId;
    }

    public void setSignalId(LongFilter signalId) {
        this.signalId = signalId;
    }

    public LongFilter getBourseCodeId() {
        return bourseCodeId;
    }

    public void setBourseCodeId(LongFilter bourseCodeId) {
        this.bourseCodeId = bourseCodeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderCriteria that = (OrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(isin, that.isin) &&
            Objects.equals(price, that.price) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(validity, that.validity) &&
            Objects.equals(side, that.side) &&
            Objects.equals(broker, that.broker) &&
            Objects.equals(omsId, that.omsId) &&
            Objects.equals(state, that.state) &&
            Objects.equals(executed, that.executed) &&
            Objects.equals(description, that.description) &&
            Objects.equals(signalId, that.signalId) &&
            Objects.equals(bourseCodeId, that.bourseCodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        isin,
        price,
        quantity,
        validity,
        side,
        broker,
        omsId,
        state,
        executed,
        description,
        signalId,
        bourseCodeId
        );
    }

    @Override
    public String toString() {
        return "OrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (isin != null ? "isin=" + isin + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (validity != null ? "validity=" + validity + ", " : "") +
                (side != null ? "side=" + side + ", " : "") +
                (broker != null ? "broker=" + broker + ", " : "") +
                (omsId != null ? "omsId=" + omsId + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (executed != null ? "executed=" + executed + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (signalId != null ? "signalId=" + signalId + ", " : "") +
                (bourseCodeId != null ? "bourseCodeId=" + bourseCodeId + ", " : "") +
            "}";
    }

}
