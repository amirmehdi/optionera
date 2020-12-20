package com.gitlab.amirmehdi.service.dto.sahra;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class OrderError {
    private StateChangeData stateChangeData;
    private String errorMessage;

    //orderError [[[1170000000386480, 5, 000000, false, 1, 0, 0, 1], وضعیت گروه این نماد برای انجام عملیات مورد نظر شما مجاز نیست]]
    public OrderError(ArrayList<Object> s) {
        this(new StateChangeData((ArrayList<Object>) s.get(0))
            , (String) s.get(1)
        );
    }
}
