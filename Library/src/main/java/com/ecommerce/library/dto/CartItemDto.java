package com.ecommerce.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Integer id;

    private ShoppingCartDto cart;

    private ProductDto product;

    private Integer quantity;

    private Double unitPrice;

}
