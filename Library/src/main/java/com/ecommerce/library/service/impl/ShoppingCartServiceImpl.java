package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CartItemDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.ShoppingCartDto;
import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CartItemRepository;
import com.ecommerce.library.repository.ShoppingCartRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;

    private final CartItemRepository itemRepository;

    private final CustomerService customerService;

    @Override
    @Transactional
    public ShoppingCart addItemToCart(ProductDto productDto, Integer quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }
        Set<CartItem> cartItemList = shoppingCart.getCartItems();
        CartItem cartItem = find(cartItemList, productDto.getId());
        Product product = transfer(productDto);

        Double unitPrice = productDto.getCostPrice() * (1 - productDto.getDiscount()/100.0) ;

        Integer itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(shoppingCart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(shoppingCart);
                cartItemList.add(cartItem);
                itemRepository.save(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                itemRepository.save(cartItem);
            }
        } else {
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(shoppingCart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItemList.add(cartItem);
                itemRepository.save(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                itemRepository.save(cartItem);
            }
        }
        shoppingCart.setCartItems(cartItemList);

        Double totalPrice = totalPrice(shoppingCart.getCartItems());
        Integer totalItem = totalItem(shoppingCart.getCartItems());

        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setCustomer(customer);

        return cartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart updateCart(ProductDto productDto, Integer quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        Set<CartItem> cartItemList = shoppingCart.getCartItems();
        CartItem item = find(cartItemList, productDto.getId());
        Integer itemQuantity = quantity;


        item.setQuantity(itemQuantity);
        itemRepository.save(item);
        shoppingCart.setCartItems(cartItemList);
        Integer totalItem = totalItem(cartItemList);
        Double totalPrice = totalPrice(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ShoppingCart removeItemFromCart(ProductDto productDto, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        Set<CartItem> cartItemList = shoppingCart.getCartItems();
        CartItem item = find(cartItemList, productDto.getId());

        cartItemList.remove(item);
        itemRepository.delete(item);
        Double totalPrice = totalPrice(cartItemList);
        Integer totalItem = totalItem(cartItemList);
        shoppingCart.setCartItems(cartItemList);
        shoppingCart.setTotalPrice(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public void deleteCartById(Integer id) {
        ShoppingCart shoppingCart = cartRepository.getById(id);
        if(!ObjectUtils.isEmpty(shoppingCart) && !ObjectUtils.isEmpty(shoppingCart.getCartItems())){
            itemRepository.deleteAll(shoppingCart.getCartItems());
        }
        shoppingCart.getCartItems().clear();
        shoppingCart.setTotalPrice(0.0);
        shoppingCart.setTotalItems(0);
        cartRepository.save(shoppingCart);
    }

    private CartItem find(Set<CartItem> cartItems, Integer productId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private Integer totalItem(Set<CartItem> cartItemList) {
        Integer totalItem = 0;
        for (CartItem item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private Double totalPrice(Set<CartItem> cartItemList) {
        Double totalPrice = 0.0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity() ;
        }
        return totalPrice;
    }

    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setDiscount(productDto.getDiscount());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());
        product.setCategory(productDto.getCategory());
        return product;
    }

}
