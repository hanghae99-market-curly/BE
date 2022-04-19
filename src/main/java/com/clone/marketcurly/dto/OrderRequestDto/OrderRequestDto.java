package com.clone.marketcurly.dto.OrderRequestDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderRequestDto {
    private String address;
    private int totalPrice;
}
