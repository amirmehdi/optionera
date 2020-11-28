package com.gitlab.amirmehdi.service.dto.sahra;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class PositionData {
    private String isin;
    private int asset;
    private int buyOrder;
    private int saleOrder;

    private int InitialMarginCount;
    private int InitialMarginAmount;
    private int MarginAmount;
    String cacheStrikeDate;
    String strikeDate;

    //PollMessageResponse(hub=OmsClientHub, method=PositionChange, val=[[IRO9MAPN4141, -1, 0, 0, 0, 0, 9272880, 1399/10/08, 1399/10/09]])
    public PositionData(ArrayList<Object> s) {
        this((String) s.get(0)
            , ((Number) s.get(1)).intValue()
            , ((Number) s.get(2)).intValue()
            , ((Number) s.get(3)).intValue()
            , ((Number) s.get(4)).intValue()
            , ((Number) s.get(5)).intValue()
            , ((Number) s.get(6)).intValue()
            , (String) s.get(7)
            , (String) s.get(8)
        );
    }
}
