package com.gitlab.amirmehdi.service.dto.core;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BidAsk {

    private String isin;
    private List<BidAskItem> items;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Tehran")
    private Date dateTime;

    @JsonIgnore
    public BidAskItem getBestBidAsk() {
        return items.get(0);
    }

    public void setItems(List<BidAskItem> items) {
        if (items == null || items.isEmpty()) {
            items = new ArrayList<>(Arrays.asList(
                new BidAskItem(),
                new BidAskItem(),
                new BidAskItem(),
                new BidAskItem(),
                new BidAskItem()
            ));
        }
        this.items = items;
    }
}
