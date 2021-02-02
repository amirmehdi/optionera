package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitlab.amirmehdi.service.dto.sahra.SahraSecurityObject;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

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

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "security_fields")
    private String securityFields;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("tokens")
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Token createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getSecurityFields() {
        return securityFields;
    }

    public Token securityFields(String securityFields) {
        this.securityFields = securityFields;
        return this;
    }

    public void setSecurityFields(String securityFields) {
        this.securityFields = securityFields;
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

    public SahraSecurityObject toSecurityFields() {
        return new SahraSecurityObject(securityFields);
    }

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
            ", createdAt='" + getCreatedAt() + "'" +
            ", securityFields='" + getSecurityFields() + "'" +
            "}";
    }
}
