package com.gitlab.amirmehdi.domain;

import com.gitlab.amirmehdi.domain.enumeration.AlgorithmSide;
import com.gitlab.amirmehdi.domain.enumeration.AlgorithmState;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Algorithm.
 */
@Entity
@Table(name = "algorithm")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Algorithm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false)
    private AlgorithmSide side;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private AlgorithmState state;

    @Column(name = "input")
    private String input;

    @Column(name = "trade_volume_limit")
    private Integer tradeVolumeLimit;

    @Column(name = "trade_value_limit")
    private Long tradeValueLimit;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @Column(name = "isins", nullable = false)
    private String isins;

    @OneToMany(mappedBy = "algorithm")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Order> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Algorithm type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AlgorithmSide getSide() {
        return side;
    }

    public Algorithm side(AlgorithmSide side) {
        this.side = side;
        return this;
    }

    public void setSide(AlgorithmSide side) {
        this.side = side;
    }

    public AlgorithmState getState() {
        return state;
    }

    public Algorithm state(AlgorithmState state) {
        this.state = state;
        return this;
    }

    public void setState(AlgorithmState state) {
        this.state = state;
    }

    public String getInput() {
        return input;
    }

    public Algorithm input(String input) {
        this.input = input;
        return this;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getTradeVolumeLimit() {
        return tradeVolumeLimit;
    }

    public Algorithm tradeVolumeLimit(Integer tradeVolumeLimit) {
        this.tradeVolumeLimit = tradeVolumeLimit;
        return this;
    }

    public void setTradeVolumeLimit(Integer tradeVolumeLimit) {
        this.tradeVolumeLimit = tradeVolumeLimit;
    }

    public Long getTradeValueLimit() {
        return tradeValueLimit;
    }

    public Algorithm tradeValueLimit(Long tradeValueLimit) {
        this.tradeValueLimit = tradeValueLimit;
        return this;
    }

    public void setTradeValueLimit(Long tradeValueLimit) {
        this.tradeValueLimit = tradeValueLimit;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Algorithm createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Algorithm updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsins() {
        return isins;
    }

    public Algorithm isins(String isins) {
        this.isins = isins;
        return this;
    }

    public void setIsins(String isins) {
        this.isins = isins;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Algorithm orders(Set<Order> orders) {
        this.orders = orders;
        return this;
    }

    public Algorithm addOrder(Order order) {
        this.orders.add(order);
        order.setAlgorithm(this);
        return this;
    }

    public Algorithm removeOrder(Order order) {
        this.orders.remove(order);
        order.setAlgorithm(null);
        return this;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Algorithm)) {
            return false;
        }
        return id != null && id.equals(((Algorithm) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Algorithm{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", side='" + getSide() + "'" +
            ", state='" + getState() + "'" +
            ", input='" + getInput() + "'" +
            ", tradeVolumeLimit=" + getTradeVolumeLimit() +
            ", tradeValueLimit=" + getTradeValueLimit() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isins='" + getIsins() + "'" +
            "}";
    }
}
