package com.gitlab.amirmehdi.service.dto.sahra;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class AssetData {
    private String id;
    private int quantity;
    private int avgPrice;
    private int lastTradePrice;

    public AssetData(ArrayList<Object> s) {
        this((String) s.get(0)
            , ((Number) s.get(1)).intValue()
            , ((Number) s.get(2)).intValue()
            , ((Number) s.get(3)).intValue());
    }
}
