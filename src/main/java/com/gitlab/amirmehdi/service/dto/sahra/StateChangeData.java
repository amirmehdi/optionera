package com.gitlab.amirmehdi.service.dto.sahra;


import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderLockType;
import com.gitlab.amirmehdi.service.dto.sahra.enums.OrderSource;
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
public class StateChangeData {
    private String id;
    private OrderStatus orderStatus;
    private String hon;
    private boolean edited;
    private OrderLockType orderLockType;
    private long blockedCredit;
    private int remain;
    private OrderSource orderSource;

    //PollMessageResponse(hub=OmsClientHub, method=orderStateChange, val=[[1170000000364932, 3, 003078, false, 1, 28169130, 1481, 1]])
    public StateChangeData(ArrayList<Object> s) {
        this(String.valueOf(((Number) s.get(0)).longValue())
            , OrderStatus.get(((Number) s.get(1)).intValue())
            , (String) s.get(2)
            , (Boolean) s.get(3)
            , OrderLockType.get(((Number) s.get(4)).intValue())
            , ((Number) s.get(5)).longValue()
            , ((Number) s.get(6)).intValue()
            , OrderSource.get(((Number) s.get(7)).intValue()));
    }
}
