package com.gitlab.amirmehdi.service.dto.sahra;


import com.gitlab.amirmehdi.service.dto.sahra.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

/**
 * author : Amirmehdi Naghavi
 * 6/4/2019
 */

@Data
@AllArgsConstructor
public class OrderData {
    private String id;
    private String instrumentId;
    private String createDate;
    private int quantity;
    private int price;
    private int executedQuantity;
    private OrderType orderSide;
    private OrderValidity validity;
    private Date orderValidityDate;
    private OrderStatus orderStatus;
    private OrderCreditSource creditSource;
    private String HON;
    private boolean edited;
    private OrderLockType orderLockType;
    private int disclosedQuantity;
    private int minimumQuantity;
    private boolean isToday;
    private long blockedCredit;
    private int remain;
    private String error;
    private OrderSource orderSource;
    private String extraData;

    //PollMessageResponse(hub=OmsClientHub, method=orderAdded, val=[[1170000000364934, IRO9MAPN4141, 1399/09/05 11:21:55, 1, 2480, 0, 2, 1, null, 1, 1, 000000, false, 2, 0, 0, true, 9276665, 1, null, 1, null]])
    public OrderData(ArrayList<Object> s) {
        this((String) s.get(0), (String) s.get(1), (String) s.get(2)
            , ((Number) s.get(3)).intValue(), ((Number) s.get(4)).intValue(), ((Number) s.get(5)).intValue()
            , OrderType.get(((Number) s.get(6)).intValue())
            , OrderValidity.get(((Number) s.get(7)).intValue())
            , null
            , OrderStatus.get(((Number) s.get(9)).intValue())
            , OrderCreditSource.get(((Number) s.get(10)).intValue())
            , (String) s.get(10), (Boolean) s.get(11)
            , OrderLockType.get(((Number) s.get(12)).intValue())
            , ((Number) s.get(13)).intValue(), ((Number) s.get(14)).intValue()
            , (Boolean) s.get(15)
            , ((Number) s.get(16)).longValue(), ((Number) s.get(17)).intValue()
            , (String) s.get(18)
            , OrderSource.get(((Number) s.get(19)).intValue())
            , (String) s.get(20)
        );
    }
}
