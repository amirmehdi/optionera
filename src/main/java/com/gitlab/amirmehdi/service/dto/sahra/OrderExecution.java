package com.gitlab.amirmehdi.service.dto.sahra;


import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderLockType;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */
@Data
@AllArgsConstructor
public class OrderExecution {
    private String id;
    private int executedQuantity;
    private OrderStatus orderStatus;
    private OrderLockType orderLockType;
    private long blockedCredit;
    private int remain;
    private int quantity;
    private int price;
    private long draftAmount;//مبلغ حواله

    //PollMessageResponse(hub=OmsClientHub, method=orderExecution, val=[[1170000000364932, 1481, 3, 1, 0, 0, 1481, 18900, 28094805]])
    public OrderExecution(ArrayList<Object> s) {
        this((String) s.get(0), ((Number) s.get(1)).intValue()
            , OrderStatus.get(((Number) s.get(2)).intValue())
            , OrderLockType.get(((Number) s.get(3)).intValue())
            , ((Number) s.get(4)).longValue()
            , ((Number) s.get(5)).intValue()
            , ((Number) s.get(6)).intValue()
            , ((Number) s.get(7)).intValue()
            , ((Number) s.get(8)).longValue());
    }
}
