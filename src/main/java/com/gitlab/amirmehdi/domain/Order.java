package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitlab.amirmehdi.domain.enumeration.OrderState;
import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Getter
@Setter
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
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

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
    public Order isin(String isin) {
        this.isin = isin;
        return this;
    }

    public Order price(Integer price) {
        this.price = price;
        return this;
    }

    public Order quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Order validity(Validity validity) {
        this.validity = validity;
        return this;
    }

    public Order side(Side side) {
        this.side = side;
        return this;
    }

    public Order omsId(String omsId) {
        this.omsId = omsId;
        return this;
    }

    public Order state(OrderState state) {
        this.state = state;
        return this;
    }

    public Order executed(Integer executed) {
        this.executed = executed;
        return this;
    }

    public Order description(String description) {
        this.description = description;
        return this;
    }

    public Order signal(Signal signal) {
        this.signal = signal;
        return this;
    }

    public Order bourseCode(BourseCode bourseCode) {
        this.bourseCode = bourseCode;
        return this;
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
