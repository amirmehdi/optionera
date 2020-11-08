package com.gitlab.amirmehdi.service.dto;

import com.gitlab.amirmehdi.domain.enumeration.AlgorithmSide;
import com.gitlab.amirmehdi.domain.enumeration.AlgorithmState;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gitlab.amirmehdi.domain.Algorithm} entity.
 */
public class AlgorithmDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;

    @NotNull
    private AlgorithmSide side;

    @NotNull
    private AlgorithmState state;

    private String input;

    private Integer tradeVolumeLimit;

    private Long tradeValueLimit;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private String isins;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AlgorithmSide getSide() {
        return side;
    }

    public void setSide(AlgorithmSide side) {
        this.side = side;
    }

    public AlgorithmState getState() {
        return state;
    }

    public void setState(AlgorithmState state) {
        this.state = state;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getTradeVolumeLimit() {
        return tradeVolumeLimit;
    }

    public void setTradeVolumeLimit(Integer tradeVolumeLimit) {
        this.tradeVolumeLimit = tradeVolumeLimit;
    }

    public Long getTradeValueLimit() {
        return tradeValueLimit;
    }

    public void setTradeValueLimit(Long tradeValueLimit) {
        this.tradeValueLimit = tradeValueLimit;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsins() {
        return isins;
    }

    public void setIsins(String isins) {
        this.isins = isins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlgorithmDTO algorithmDTO = (AlgorithmDTO) o;
        if (algorithmDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), algorithmDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlgorithmDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", side='" + getSide() + "'" +
            ", state='" + getState() + "'" +
            ", input='" + getInput() + "'" +
            ", tradeVolumeLimit=" + getTradeVolumeLimit() +
            ", tradeValueLimit=" + getTradeValueLimit() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isins='" + getIsins() + "'" +
            "}";
    }
}
