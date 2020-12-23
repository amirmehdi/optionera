package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * A Token.
 */
@Entity
@Table(name = "token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "token", nullable = false)
    private String token;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "broker", nullable = false)
    private Broker broker;

    @Column(name = "created_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToOne(mappedBy = "token")
    @JsonIgnore
    private BourseCode bourseCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public Token token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Broker getBroker() {
        return broker;
    }

    public Token broker(Broker broker) {
        this.broker = broker;
        return this;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Token createdAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BourseCode getBourseCode() {
        return bourseCode;
    }

    public Token bourseCode(BourseCode bourseCode) {
        this.bourseCode = bourseCode;
        return this;
    }

    public void setBourseCode(BourseCode bourseCode) {
        this.bourseCode = bourseCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Token)) {
            return false;
        }
        return id != null && id.equals(((Token) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Token{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", broker='" + getBroker() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
