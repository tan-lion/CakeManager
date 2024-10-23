package com.ecommerce.admin.controller;


import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/customer-manage")
    public String showProfile(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        } else {

            List<Customer> customer = customerService.findAll();
            model.addAttribute("customers", customer);
            return "customer-manage";
        }
    }

    @RequestMapping(value = "/delete-customer", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedCustomer(Integer id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            customerService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Thất bại!");
        }
        return "redirect:/customer-manage";
    }


}
