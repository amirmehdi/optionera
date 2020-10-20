package com.gitlab.amirmehdi.service.dto;

import com.gitlab.amirmehdi.domain.enumeration.Side;
import com.gitlab.amirmehdi.domain.enumeration.Validity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String isin;
    private Validity validity;
    private Side side;
    private int quantity;
    private int price;
}
