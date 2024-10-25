package com.ecommerce.library.dto;

import com.ecommerce.library.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    private Integer id;

    private Customer customer;

    private double totalPrice;

    private Integer totalItems;

    private double tax;


    private Set<CartItemDto> cartItems;

}
