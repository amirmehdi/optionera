package com.gitlab.amirmehdi.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link com.gitlab.amirmehdi.domain.EmbeddedOption} entity. This class is used
 * in {@link com.gitlab.amirmehdi.web.rest.EmbeddedOptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /embedded-options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmbeddedOptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter isin;

    private LocalDateFilter expDate;

    private IntegerFilter strikePrice;

    private StringFilter tseId;

    private StringFilter underlyingInstrumentId;

    public EmbeddedOptionCriteria() {
    }

    public EmbeddedOptionCriteria(EmbeddedOptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.isin = other.isin == null ? null : other.isin.copy();
        this.expDate = other.expDate == null ? null : other.expDate.copy();
        this.strikePrice = other.strikePrice == null ? null : other.strikePrice.copy();
        this.tseId = other.tseId == null ? null : other.tseId.copy();
        this.underlyingInstrumentId = other.underlyingInstrumentId == null ? null : other.underlyingInstrumentId.copy();
    }

    @Override
    public EmbeddedOptionCriteria copy() {
        return new EmbeddedOptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getIsin() {
        return isin;
    }

    public void setIsin(StringFilter isin) {
        this.isin = isin;
    }

    public LocalDateFilter getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDateFilter expDate) {
        this.expDate = expDate;
    }

    public IntegerFilter getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(IntegerFilter strikePrice) {
        this.strikePrice = strikePrice;
    }

    public StringFilter getTseId() {
        return tseId;
    }

    public void setTseId(StringFilter tseId) {
        this.tseId = tseId;
    }

    public StringFilter getUnderlyingInstrumentId() {
        return underlyingInstrumentId;
    }

    public void setUnderlyingInstrumentId(StringFilter underlyingInstrumentId) {
        this.underlyingInstrumentId = underlyingInstrumentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmbeddedOptionCriteria that = (EmbeddedOptionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isin, that.isin) &&
            Objects.equals(expDate, that.expDate) &&
            Objects.equals(strikePrice, that.strikePrice) &&
            Objects.equals(tseId, that.tseId) &&
            Objects.equals(underlyingInstrumentId, that.underlyingInstrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        isin,
        expDate,
        strikePrice,
        tseId,
        underlyingInstrumentId
        );
    }

    @Override
    public String toString() {
        return "EmbeddedOptionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (isin != null ? "isin=" + isin + ", " : "") +
                (expDate != null ? "expDate=" + expDate + ", " : "") +
                (strikePrice != null ? "strikePrice=" + strikePrice + ", " : "") +
                (tseId != null ? "tseId=" + tseId + ", " : "") +
                (underlyingInstrumentId != null ? "underlyingInstrumentId=" + underlyingInstrumentId + ", " : "") +
            "}";
    }

}
