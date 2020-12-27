package com.gitlab.amirmehdi.service.dto.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientsInfo {

    private int individualBuyCount;
    private int individualSellCount;
    private long individualBuyVolume;
    private long individualSellVolume;

    private long naturalBuyVolume;
    private long naturalSellVolume;
    private int naturalBuyCount;
    private int naturalSellCount;

    private String isin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Tehran")
    private Date dateTime;
}
