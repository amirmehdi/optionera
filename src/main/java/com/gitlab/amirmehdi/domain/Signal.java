package com.gitlab.amirmehdi.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A Signal.
 */
@Entity
@Table(name = "signal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Signal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

    @Column(name = "last")
    private Integer last;

    @Column(name = "trade_volume")
    private Long tradeVolume;

    @Column(name = "bid_volume")
    private Integer bidVolume;

    @Column(name = "bid_price")
    private Integer bidPrice;

    @Column(name = "ask_price")
    private Integer askPrice;

    @Column(name = "ask_volume")
    private Integer askVolume;

    @Column(name = "base_instrument_last")
    private Integer baseInstrumentLast;

    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "signal" ,fetch =FetchType.EAGER)
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

    public Signal type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsin() {
        return isin;
    }

    public Signal isin(String isin) {
        this.isin = isin;
        return this;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public Integer getLast() {
        return last;
    }

    public Signal last(Integer last) {
        this.last = last;
        return this;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public Long getTradeVolume() {
        return tradeVolume;
    }

    public Signal tradeVolume(Long tradeVolume) {
        this.tradeVolume = tradeVolume;
        return this;
    }

    public void setTradeVolume(Long tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public Integer getBidVolume() {
        return bidVolume;
    }

    public Signal bidVolume(Integer bidVolume) {
        this.bidVolume = bidVolume;
        return this;
    }

    public void setBidVolume(Integer bidVolume) {
        this.bidVolume = bidVolume;
    }

    public Integer getBidPrice() {
        return bidPrice;
    }

    public Signal bidPrice(Integer bidPrice) {
        this.bidPrice = bidPrice;
        return this;
    }

    public void setBidPrice(Integer bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Integer getAskPrice() {
        return askPrice;
    }

    public Signal askPrice(Integer askPrice) {
        this.askPrice = askPrice;
        return this;
    }

    public void setAskPrice(Integer askPrice) {
        this.askPrice = askPrice;
    }

    public Integer getAskVolume() {
        return askVolume;
    }

    public Signal askVolume(Integer askVolume) {
        this.askVolume = askVolume;
        return this;
    }

    public void setAskVolume(Integer askVolume) {
        this.askVolume = askVolume;
    }

    public Integer getBaseInstrumentLast() {
        return baseInstrumentLast;
    }

    public Signal baseInstrumentLast(Integer baseInstrumentLast) {
        this.baseInstrumentLast = baseInstrumentLast;
        return this;
    }

    public void setBaseInstrumentLast(Integer baseInstrumentLast) {
        this.baseInstrumentLast = baseInstrumentLast;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Signal createdAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public Signal orders(Set<Order> orders) {
        this.orders = orders;
        return this;
    }

    public Signal addOrder(Order order) {
        this.orders.add(order);
        order.setSignal(this);
        return this;
    }

    public Signal removeOrder(Order order) {
        this.orders.remove(order);
        order.setSignal(null);
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
        if (!(o instanceof Signal)) {
            return false;
        }
        return id != null && id.equals(((Signal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Signal{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", isin='" + getIsin() + "'" +
            ", last=" + getLast() +
            ", tradeVolume=" + getTradeVolume() +
            ", bidVolume=" + getBidVolume() +
            ", bidPrice=" + getBidPrice() +
            ", askPrice=" + getAskPrice() +
            ", askVolume=" + getAskVolume() +
            ", baseInstrumentLast=" + getBaseInstrumentLast() +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
