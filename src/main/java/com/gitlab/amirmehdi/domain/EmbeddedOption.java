package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A EmbeddedOption.
 */
@Entity
@Table(name = "embedded_option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmbeddedOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

    @NotNull
    @Column(name = "exp_date", nullable = false)
    private LocalDate expDate;

    @NotNull
    @Column(name = "strike_price", nullable = false)
    private Integer strikePrice;

    @Column(name = "tse_id")
    private String tseId;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "underlying_instrument_id")
    @JsonIgnoreProperties("embeddedOptions")
    private Instrument underlyingInstrument;

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

    public EmbeddedOption name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsin() {
        return isin;
    }

    public EmbeddedOption isin(String isin) {
        this.isin = isin;
        return this;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public EmbeddedOption expDate(LocalDate expDate) {
        this.expDate = expDate;
        return this;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public Integer getStrikePrice() {
        return strikePrice;
    }

    public EmbeddedOption strikePrice(Integer strikePrice) {
        this.strikePrice = strikePrice;
        return this;
    }

    public void setStrikePrice(Integer strikePrice) {
        this.strikePrice = strikePrice;
    }

    public String getTseId() {
        return tseId;
    }

    public EmbeddedOption tseId(String tseId) {
        this.tseId = tseId;
        return this;
    }

    public void setTseId(String tseId) {
        this.tseId = tseId;
    }

    public Instrument getUnderlyingInstrument() {
        return underlyingInstrument;
    }

    public EmbeddedOption underlyingInstrument(Instrument instrument) {
        this.underlyingInstrument = instrument;
        return this;
    }

    public void setUnderlyingInstrument(Instrument instrument) {
        this.underlyingInstrument = instrument;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmbeddedOption)) {
            return false;
        }
        return id != null && id.equals(((EmbeddedOption) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmbeddedOption{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isin='" + getIsin() + "'" +
            ", expDate='" + getExpDate() + "'" +
            ", strikePrice=" + getStrikePrice() +
            ", tseId='" + getTseId() + "'" +
            "}";
    }
}
