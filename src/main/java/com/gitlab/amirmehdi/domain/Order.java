package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * A Order.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "validity", nullable = false)
    private Validity validity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false)
    private Side side;

    @Column(name = "oms_id")
    private String omsId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OrderState state;

    @Column(name = "executed")
    private Integer executed;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private Signal signal;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("orders")
    private BourseCode bourseCode;

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

    public Order isin(String isin) {
        this.isin = isin;
        return this;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public Integer getPrice() {
        return price;
    }

    public Order price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Order quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Validity getValidity() {
        return validity;
    }

    public Order validity(Validity validity) {
        this.validity = validity;
        return this;
    }

    public void setValidity(Validity validity) {
        this.validity = validity;
    }

    public Side getSide() {
        return side;
    }

    public Order side(Side side) {
        this.side = side;
        return this;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public String getOmsId() {
        return omsId;
    }

    public Order omsId(String omsId) {
        this.omsId = omsId;
        return this;
    }

    public void setOmsId(String omsId) {
        this.omsId = omsId;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public OrderState getState() {
        return state;
    }

    public Order state(OrderState state) {
        this.state = state;
        return this;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public Integer getExecuted() {
        return executed;
    }

    public Order executed(Integer executed) {
        this.executed = executed;
        return this;
    }

    public void setExecuted(Integer executed) {
        this.executed = executed;
    }

    public String getDescription() {
        return description;
    }

    public Order description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Signal getSignal() {
        return signal;
    }

    public Order signal(Signal signal) {
        this.signal = signal;
        return this;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    public BourseCode getBourseCode() {
        return bourseCode;
    }

    public Order bourseCode(BourseCode bourseCode) {
        this.bourseCode = bourseCode;
        return this;
    }

    public void setBourseCode(BourseCode bourseCode) {
        this.bourseCode = bourseCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", isin='" + getIsin() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", validity='" + getValidity() + "'" +
            ", side='" + getSide() + "'" +
            ", omsId='" + getOmsId() + "'" +
            ", state='" + getState() + "'" +
            ", executed=" + getExecuted() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
