package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String getAll(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<Order> orderList = orderService.findALlOrders();
            model.addAttribute("orders", orderList);
            return "orders";
        }
    }


    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Integer id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Đơn hàng đã duyệt");
            return "redirect:/orders";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Integer id, Principal principal, RedirectAttributes attributes) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.cancelOrder(id);
            attributes.addFlashAttribute("success", "Hủy đơn hàng thành công!");
            return "redirect:/orders";
        }
    }

    @GetMapping("/total-revenue")
    public String showTotalRevenue(Model model, Principal principal){
        BigDecimal totalRevenue=orderService.getTotalRevenue();
        if (principal == null) {
            return "redirect:/login";
        } else {
            List<Order> orderList = orderService.findALlOrders();
            model.addAttribute("orders", orderList);
            model.addAttribute("totalRevenue", totalRevenue !=null ? totalRevenue : 0);
            return "total-revenue";
        }
    }

}
