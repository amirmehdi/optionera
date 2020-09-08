package com.gitlab.amirmehdi.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A InstrumentHistory.
 */
@Entity
@Table(name = "instrument_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InstrumentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "isin", nullable = false)
    private String isin;

    @Column(name = "closing")
    private Integer closing;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

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

    public InstrumentHistory isin(String isin) {
        this.isin = isin;
        return this;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public Integer getClosing() {
        return closing;
    }

    public InstrumentHistory closing(Integer closing) {
        this.closing = closing;
        return this;
    }

    public void setClosing(Integer closing) {
        this.closing = closing;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public InstrumentHistory updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstrumentHistory)) {
            return false;
        }
        return id != null && id.equals(((InstrumentHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "InstrumentHistory{" +
            "id=" + getId() +
            ", isin='" + getIsin() + "'" +
            ", closing=" + getClosing() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
