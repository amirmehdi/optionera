package com.gitlab.amirmehdi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Portfolio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "portfolio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@IdClass(AssetCompositeKey.class)
public class Portfolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    @NotNull
    private long userId;

    @Id
    @Column(name = "date", nullable = false)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @Id
    @Column(name = "isin", nullable = false)
    @NotNull
    private String isin;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "avg_price")
    private Integer avgPrice;

    @Column(name = "closing_price")
    private Integer closingPrice;

    @Column(name = "last_price")
    private Integer lastPrice;

    @JsonIgnore
    public AssetCompositeKey getId() {
        if (isin == null && date == null) {
            return null;
        }
        return new AssetCompositeKey(userId, isin, date);
    }

}
