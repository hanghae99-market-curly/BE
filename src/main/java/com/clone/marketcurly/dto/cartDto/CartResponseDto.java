package com.clone.marketcurly.dto.cartDto;

import com.clone.marketcurly.dto.productDto.ProductResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDto {

    private String brand;
    private String imgUrl;
    private String name;
    private int price;
    private int quantity;

    public CartResponseDto(ProductResponseDto productResponseDto, int quantity) {
        this.brand = productResponseDto.getBrand();
        this.imgUrl = productResponseDto.getImgUrl();
        this.name = productResponseDto.getName();
        this.price = productResponseDto.getPrice();
        this.quantity = quantity;
    }
}
