package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.ShoppingCartDto;
import com.ecommerce.library.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(ProductDto productDto, Integer quantity, String username);

    ShoppingCart updateCart(ProductDto productDto, Integer quantity, String username);

    ShoppingCart removeItemFromCart(ProductDto productDto, String username);


    void deleteCartById(Integer id);

}
