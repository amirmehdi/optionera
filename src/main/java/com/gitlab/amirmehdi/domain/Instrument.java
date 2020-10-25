package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Instrument.
 */
@Entity
@Table(name = "instrument")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Instrument implements Serializable {

    @Id
    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tse_id")
    private String tseId;

    @Column(name = "volatility_30")
    private double volatility30;

    @Column(name = "volatility_60")
    private double volatility60;

    @Column(name = "volatility_90")
    private double volatility90;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public Instrument isin(String id) {
        this.isin = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Instrument name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTseId() {
        return tseId;
    }

    public Instrument tseId(String tseId) {
        this.tseId = tseId;
        return this;
    }

    public void setTseId(String tseId) {
        this.tseId = tseId;
    }

    public double getVolatility30() {
        return volatility30;
    }

    public Instrument volatility30(double volatility30) {
        this.volatility30 = volatility30;
        return this;
    }

    public void setVolatility30(double volatility30) {
        this.volatility30 = volatility30;
    }

    public double getVolatility60() {
        return volatility60;
    }

    public Instrument volatility60(double volatility60) {
        this.volatility60 = volatility60;
        return this;
    }

    public void setVolatility60(double volatility60) {
        this.volatility60 = volatility60;
    }

    public double getVolatility90() {
        return volatility90;
    }

    public Instrument volatility90(double volatility90) {
        this.volatility90 = volatility90;
        return this;
    }

    public void setVolatility90(double volatility90) {
        this.volatility90 = volatility90;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public Instrument updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instrument)) {
            return false;
        }
        return isin != null && isin.equals(((Instrument) o).isin);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Instrument{" +
            ", isin='" + getIsin() + "'" +
            ", name='" + getName() + "'" +
            ", tseId='" + getTseId() + "'" +
            ", volatility30=" + getVolatility30() +
            ", volatility60=" + getVolatility60() +
            ", volatility90=" + getVolatility90() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
