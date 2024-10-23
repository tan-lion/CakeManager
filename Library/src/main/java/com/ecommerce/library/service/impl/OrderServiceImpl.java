package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public Order save(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(8);
        order.setTotalPrice(shoppingCart.getTotalPrice() * (1 + order.getTax() / 100.0));
        order.setAccept(false);
        order.setPaymentMethod("Tiền mặt");
        order.setOrderStatus("Đang xử lý");
        order.setQuantity(shoppingCart.getTotalItems());

        Order saveOrder=orderRepository.save(order);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItems()) {

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetail.setQuantityProduct(item.getQuantity());
            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());
        return saveOrder;
    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        return customer.getOrders();
    }

    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }


    @Override
    public Order acceptOrder(Integer id) {
        Order order = orderRepository.getById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        order.setOrderStatus("Đã xử lý");

        for (OrderDetail orderDetail : order.getOrderDetailList()) {

                productService.reduceProductQuantity(orderDetail.getProduct().getId(), orderDetail.getQuantityProduct());
                Product product = orderDetail.getProduct();
                if (product.getCurrentQuantity() == 0) {
                    product.set_deleted(true);
                    product.set_activated(false);
                }
        }


        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Integer id) {
        Optional<Order> orderOptional=orderRepository.findById(id);
        if(orderOptional.isPresent())
            orderRepository.delete(orderOptional.get());
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return orderRepository.sumTotalRevenue();
    }

    @Override
    public List<OrderDetail> findByOderDate(Date date) {
        return orderDetailRepository.findByOrderDate(date);
    }
//    public static String formatCurrency(BigDecimal amount) {
//
//        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
//        symbols.setGroupingSeparator(',');
//        symbols.setDecimalSeparator('.');
//
//        DecimalFormat decimalFormat = new DecimalFormat("#,##0.##", symbols);
//        return decimalFormat.format(amount);
//    }

}
