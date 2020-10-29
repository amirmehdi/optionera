package com.gitlab.amirmehdi.batch.model;

import com.gitlab.amirmehdi.domain.InstrumentTradeHistory;
import lombok.Data;

import java.util.List;

@Data
public class DayCandleBatchItem {
    private String isin;
    private List<InstrumentTradeHistory> minTickDataList;

}
