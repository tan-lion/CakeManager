package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductService {
    List<Product> findAll();

    List<ProductDto> products();

    List<ProductDto> allProduct();

    Product save(MultipartFile imageProduct, ProductDto productDto);

    Product update(MultipartFile imageProduct, ProductDto productDto);

    void enableById(Integer id);

    void disableById(Integer id);

    void deleteById(Integer id);

    ProductDto getById(Integer id);

    List<ProductDto> randomProduct();

    Page<ProductDto> searchProducts(Integer pageNo, String keyword);

    Page<ProductDto> getAllProducts(Integer pageNo);


    List<ProductDto> findAllByCategory(String category);


    List<ProductDto> filterHighProducts();

    List<ProductDto> filterLowerProducts();

    List<ProductDto> listViewProducts();

    List<ProductDto> findByCategoryId(Integer id);

    List<ProductDto> searchProducts(String keyword);

    void reduceProductQuantity(Integer productId, Integer quantity);

    void updateExpiredProducts();

    List<Product> getProductByManufactureDate(Date date);


}
