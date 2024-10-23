package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;

    private final ShoppingCartService cartService;


    @GetMapping("/check-out")
    public String checkOut(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            CustomerDto customer = customerService.getCustomer(principal.getName());
            if (customer.getAddress() == null || customer.getPhoneNumber() == null) {
                model.addAttribute("information", "Bạn cần cập nhật thông tin trước khi thanh toán");
                model.addAttribute("customer", customer);
                model.addAttribute("title", "Thông tin cá nhân");
                model.addAttribute("page", "Thông tin cá nhân");
                return "customer-information";
            } else {
                ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
                List<String> errorMessages=new ArrayList<>();
                for (CartItem item : cart.getCartItems()) {
                    if (item.getQuantity() > item.getProduct().getCurrentQuantity()){
                        errorMessages.add("Số lượng sản phẩm "+item.getProduct().getName()+" còn lại là "+item.getProduct().getCurrentQuantity());
                    }
                }
                if(!errorMessages.isEmpty()){
                    redirectAttributes.addFlashAttribute("errors",errorMessages);
                    return "redirect:/cart";
                }
                model.addAttribute("customer", customer);
                model.addAttribute("title", "Thanh toán");
                model.addAttribute("page", "Thanh toán");
                model.addAttribute("shoppingCart", cart);
                model.addAttribute("grandTotal", cart.getTotalItems());
                return "checkout";
            }
        }
    }

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            List<Order> orderList = customer.getOrders();
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Đơn đặt hàng");
            model.addAttribute("page", "Đơn đặt hàng");
            return "order";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Integer id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            orderService.cancelOrder(id);
            attributes.addFlashAttribute("success", "Hủy đơn hàng thành công!");
            return "redirect:/orders";
        }
    }


    @RequestMapping(value = "/add-order", method = {RequestMethod.POST})
    public String createOrder(Principal principal,
                              Model model,
                              HttpSession session) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            Order order = orderService.save(cart);
            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Chi tiết đơn hàng");
            model.addAttribute("page", "Chi tiết đơn hàng");
            model.addAttribute("success", "Đặt đơn hàng thành công");
            return "order-detail";
        }
    }

}
