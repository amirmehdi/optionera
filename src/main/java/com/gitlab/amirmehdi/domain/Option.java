package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
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

    @Column(name = "call_tse_id")
    private String callTseId;

    @Column(name = "put_tse_id")
    private String putTseId;

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
    private boolean callInTheMoney;

    @NotNull
    @Column(name = "call_break_even", nullable = false)
    private float callBreakEven;

    @NotNull
    @Column(name = "put_break_even", nullable = false)
    private float putBreakEven;

    @NotNull
    @Column(name = "call_ask_to_bs", nullable = false)
    private float callAskToBS;

    @NotNull
    @Column(name = "put_ask_to_bs", nullable = false)
    private float putAskToBS;

    @NotNull
    @Column(name = "call_leverage", nullable = false)
    private float callLeverage;

    @NotNull
    @Column(name = "put_leverage", nullable = false)
    private float putLeverage;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instrument_id")
    @NotNull
    @JsonIgnoreProperties("options")
    private Instrument instrument;

    public Option name(String name) {
        this.name = name;
        return this;
    }

    public Option callIsin(String callIsin) {
        this.callIsin = callIsin;
        return this;
    }

    public Option putIsin(String putIsin) {
        this.putIsin = putIsin;
        return this;
    }

    public Option callTseId(String callTseId) {
        this.callTseId = callTseId;
        return this;
    }

    public Option putTseId(String putTseId) {
        this.putTseId = putTseId;
        return this;
    }

    public Option expDate(LocalDate expDate) {
        this.expDate = expDate;
        return this;
    }

    public Option strikePrice(Integer strikePrice) {
        this.strikePrice = strikePrice;
        return this;
    }

    public Option contractSize(Integer contractSize) {
        this.contractSize = contractSize;
        return this;
    }

    public Option callInTheMoney(boolean callInTheMoney) {
        this.callInTheMoney = callInTheMoney;
        return this;
    }

    public Option callBreakEven(float callBreakEven) {
        this.callBreakEven = callBreakEven;
        return this;
    }

    public Option putBreakEven(float putBreakEven) {
        this.putBreakEven = putBreakEven;
        return this;
    }

    public Option callAskToBS(float callAskToBS) {
        this.callAskToBS = callAskToBS;
        return this;
    }

    public Option putAskToBS(float putAskToBS) {
        this.putAskToBS = putAskToBS;
        return this;
    }

    public Option callLeverage(float callLeverage) {
        this.callLeverage = callLeverage;
        return this;
    }

    public Option putLeverage(float putLeverage) {
        this.putLeverage = putLeverage;
        return this;
    }

    public Option instrument(Instrument instrument) {
        this.instrument = instrument;
        return this;
    }

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
