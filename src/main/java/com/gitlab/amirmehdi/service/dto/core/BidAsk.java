package com.gitlab.amirmehdi.service.dto.core;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BidAsk {

    private String isin;
    private ArrayList<BidAskItem> items = new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date dateTime;

}
