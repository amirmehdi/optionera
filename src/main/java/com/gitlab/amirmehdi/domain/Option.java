package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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

    @NotNull
    @Column(name = "call_in_the_money", nullable = false)
    private Boolean callInTheMoney;

    @NotNull
    @Column(name = "call_break_even", nullable = false)
    private Float callBreakEven;

    @NotNull
    @Column(name = "put_break_even", nullable = false)
    private Float putBreakEven;

    @NotNull
    @Column(name = "call_ask_to_bs", nullable = false)
    private Float callAskToBS;

    @NotNull
    @Column(name = "put_ask_to_bs", nullable = false)
    private Float putAskToBS;

    @NotNull
    @Column(name = "call_leverage", nullable = false)
    private Float callLeverage;

    @NotNull
    @Column(name = "put_leverage", nullable = false)
    private Float putLeverage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instrument_id")
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

    public Boolean isCallInTheMoney() {
        return callInTheMoney;
    }

    public Option callInTheMoney(Boolean callInTheMoney) {
        this.callInTheMoney = callInTheMoney;
        return this;
    }

    public void setCallInTheMoney(Boolean callInTheMoney) {
        this.callInTheMoney = callInTheMoney;
    }

    public Float getCallBreakEven() {
        return callBreakEven;
    }

    public Option callBreakEven(Float callBreakEven) {
        this.callBreakEven = callBreakEven;
        return this;
    }

    public void setCallBreakEven(Float callBreakEven) {
        this.callBreakEven = callBreakEven;
    }

    public Float getPutBreakEven() {
        return putBreakEven;
    }

    public Option putBreakEven(Float putBreakEven) {
        this.putBreakEven = putBreakEven;
        return this;
    }

    public void setPutBreakEven(Float putBreakEven) {
        this.putBreakEven = putBreakEven;
    }

    public Float getCallAskToBS() {
        return callAskToBS;
    }

    public Option callAskToBS(Float callAskToBS) {
        this.callAskToBS = callAskToBS;
        return this;
    }

    public void setCallAskToBS(Float callAskToBS) {
        this.callAskToBS = callAskToBS;
    }

    public Float getPutAskToBS() {
        return putAskToBS;
    }

    public Option putAskToBS(Float putAskToBS) {
        this.putAskToBS = putAskToBS;
        return this;
    }

    public void setPutAskToBS(Float putAskToBS) {
        this.putAskToBS = putAskToBS;
    }

    public Float getCallLeverage() {
        return callLeverage;
    }

    public Option callLeverage(Float callLeverage) {
        this.callLeverage = callLeverage;
        return this;
    }

    public void setCallLeverage(Float callLeverage) {
        this.callLeverage = callLeverage;
    }

    public Float getPutLeverage() {
        return putLeverage;
    }

    public Option putLeverage(Float putLeverage) {
        this.putLeverage = putLeverage;
        return this;
    }

    public void setPutLeverage(Float putLeverage) {
        this.putLeverage = putLeverage;
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
            ", callInTheMoney='" + isCallInTheMoney() + "'" +
            ", callBreakEven=" + getCallBreakEven() +
            ", putBreakEven=" + getPutBreakEven() +
            ", callAskToBS=" + getCallAskToBS() +
            ", putAskToBS=" + getPutAskToBS() +
            ", callLeverage=" + getCallLeverage() +
            ", putLeverage=" + getPutLeverage() +
            "}";
    }
}
