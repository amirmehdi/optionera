package com.gitlab.amirmehdi.service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class InstrumentSearchDTO {
    private String isin;
    private String name;
}
