package com.gitlab.amirmehdi.service.dto.sahra;

import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderValidity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
public class OrderEdit {
    private String id;
    private int quantity;
    private int price;
    private int minimumQuantity;
    private int disclosedQuantity;
    private OrderValidity validity;
    private Date orderValidityDate;

    public OrderEdit(ArrayList<Object> s) {
        this(String.valueOf(((Number) s.get(0)).longValue())
            , ((Number) s.get(1)).intValue(), ((Number) s.get(2)).intValue()
            , ((Number) s.get(3)).intValue(), ((Number) s.get(4)).intValue()
            , OrderValidity.get(((Number) s.get(5)).intValue())
            , null
        );
    }
}
