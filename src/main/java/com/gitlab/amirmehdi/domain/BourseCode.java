package com.gitlab.amirmehdi.domain;

import com.gitlab.amirmehdi.domain.enumeration.Broker;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * A BourseCode.
 */
@Entity
@Table(name = "bourse_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BourseCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "broker")
    private Broker broker;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "buying_power")
    private Long buyingPower;

    @Column(name = "blocked")
    private Long blocked;

    @Column(name = "remain")
    private Long remain;

    @Column(name = "credit")
    private Long credit;

    @Column(name = "conditions")
    private String conditions;

    @OneToMany(mappedBy = "bourseCode", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Token> tokens = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Broker getBroker() {
        return broker;
    }

    public BourseCode broker(Broker broker) {
        this.broker = broker;
        return this;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public String getName() {
        return name;
    }

    public BourseCode name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public BourseCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public BourseCode username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public BourseCode password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getBuyingPower() {
        return buyingPower;
    }

    public BourseCode buyingPower(Long buyingPower) {
        this.buyingPower = buyingPower;
        return this;
    }

    public void setBuyingPower(Long buyingPower) {
        this.buyingPower = buyingPower;
    }

    public Long getBlocked() {
        return blocked;
    }

    public BourseCode blocked(Long blocked) {
        this.blocked = blocked;
        return this;
    }

    public void setBlocked(Long blocked) {
        this.blocked = blocked;
    }

    public Long getRemain() {
        return remain;
    }

    public BourseCode remain(Long remain) {
        this.remain = remain;
        return this;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }

    public Long getCredit() {
        return credit;
    }

    public BourseCode credit(Long credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public String getConditions() {
        if (conditions == null) {
            return "";
        }
        return conditions;
    }

    public BourseCode conditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public Optional<Token> getRandomToken() {
        if (tokens.isEmpty()) {
            return Optional.empty();
        }
        Random rand = new Random();
        return Optional.of(tokens.get(rand.nextInt(tokens.size())));
    }

    public BourseCode tokens(List<Token> tokens) {
        this.tokens = tokens;
        return this;
    }

    public BourseCode addToken(Token token) {
        this.tokens.add(token);
        token.setBourseCode(this);
        return this;
    }

    public BourseCode removeToken(Token token) {
        this.tokens.remove(token);
        token.setBourseCode(null);
        return this;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BourseCode)) {
            return false;
        }
        return id != null && id.equals(((BourseCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BourseCode{" +
            "id=" + getId() +
            ", broker='" + getBroker() + "'" +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", buyingPower=" + getBuyingPower() +
            ", blocked=" + getBlocked() +
            ", remain=" + getRemain() +
            ", credit=" + getCredit() +
            ", conditions='" + getConditions() + "'" +
            "}";
    }
}
