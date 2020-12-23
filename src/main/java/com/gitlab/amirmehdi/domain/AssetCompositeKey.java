package com.gitlab.amirmehdi.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AssetCompositeKey implements Serializable {
    private long userId;
    private String isin;
    private LocalDate date;
}
