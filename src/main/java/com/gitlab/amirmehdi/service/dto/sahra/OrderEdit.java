package com.gitlab.amirmehdi.service.dto.sahra;

import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderValidity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
public class OrderEdit {
    private StateChangeData stateChangeData;
    private int quantity;
    private int price;
    private int executedQuantity;
    private OrderValidity validity;
    private Date orderValidityDate;
    private int disclosedQuantity;
    private int minimumQuantity;
    private int remain;

    //orderEdited [[[1170000000399676, 2, 000014, true, 1, 12407622, 10, 1], 10, 740, 0, 1, null, 0, 0, 10]]
    public OrderEdit(ArrayList<Object> s) {
        this(new StateChangeData((ArrayList<Object>) s.get(0))
            , ((Number) s.get(1)).intValue(), ((Number) s.get(2)).intValue()
            , ((Number) s.get(3)).intValue()
            , OrderValidity.get(((Number) s.get(4)).intValue())
            , null
            , ((Number) s.get(6)).intValue(), ((Number) s.get(7)).intValue()
            , ((Number) s.get(8)).intValue()
        );
    }
}
