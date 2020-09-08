package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "call_isin", nullable = false)
    private String callIsin;

    @NotNull
    @Column(name = "put_isin", nullable = false)
    private String putIsin;

    @NotNull
    @Column(name = "exp_date", nullable = false)
    private LocalDate expDate;

    @NotNull
    @Column(name = "strike_price", nullable = false)
    private Integer strikePrice;

    @Column(name = "contract_size")
    private Integer contractSize;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("options")
    private Instrument instrument;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Option name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallIsin() {
        return callIsin;
    }

    public Option callIsin(String callIsin) {
        this.callIsin = callIsin;
        return this;
    }

    public void setCallIsin(String callIsin) {
        this.callIsin = callIsin;
    }

    public String getPutIsin() {
        return putIsin;
    }

    public Option putIsin(String putIsin) {
        this.putIsin = putIsin;
        return this;
    }

    public void setPutIsin(String putIsin) {
        this.putIsin = putIsin;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public Option expDate(LocalDate expDate) {
        this.expDate = expDate;
        return this;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public Integer getStrikePrice() {
        return strikePrice;
    }

    public Option strikePrice(Integer strikePrice) {
        this.strikePrice = strikePrice;
        return this;
    }

    public void setStrikePrice(Integer strikePrice) {
        this.strikePrice = strikePrice;
    }

    public Integer getContractSize() {
        return contractSize;
    }

    public Option contractSize(Integer contractSize) {
        this.contractSize = contractSize;
        return this;
    }

    public void setContractSize(Integer contractSize) {
        this.contractSize = contractSize;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Option instrument(Instrument instrument) {
        this.instrument = instrument;
        return this;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return id != null && id.equals(((Option) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", callIsin='" + getCallIsin() + "'" +
            ", putIsin='" + getPutIsin() + "'" +
            ", expDate='" + getExpDate() + "'" +
            ", strikePrice=" + getStrikePrice() +
            ", contractSize=" + getContractSize() +
            "}";
    }
}
