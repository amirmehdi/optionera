package com.gitlab.amirmehdi.service.dto.sahra;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class CreditInfoUpdate {
    private long buyPower;
    private long accountRemain;
    private long credit;
    private long block;
    private int InvestorOptionStatus;

    public CreditInfoUpdate(ArrayList<Object> s) {
        this(((Number)s.get(0)).longValue()
        ,((Number)s.get(1)).longValue()
        ,((Number)s.get(2)).longValue()
        ,((Number)s.get(3)).longValue()
        ,((Number)s.get(4)).intValue());
    }
}
