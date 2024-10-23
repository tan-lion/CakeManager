package com.ecommerce.library.service;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetail;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {
    Order save(ShoppingCart shoppingCart);

    List<Order> findAll(String username);

    List<Order> findALlOrders();

    Order acceptOrder(Integer id);

    void cancelOrder(Integer id);

    BigDecimal getTotalRevenue();

    List<OrderDetail> findByOderDate(Date date);
}
