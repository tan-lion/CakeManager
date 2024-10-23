package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.OrderDetailRepository;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageUpload imageUpload;
    private final OrderDetailRepository orderDetailRepository;

//    Admin

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> products() {
        return transferData(productRepository.getAllProduct());
    }

    @Override
    public List<ProductDto> allProduct() {
        List<Product> products = productRepository.findAll();

        return transferData(products);
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        Product product = new Product();
        try {
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setManufactureDate(productDto.getManufactureDate());
            if (productDto.getCurrentQuantity() > 0) {
                product.set_activated(true);
                product.set_deleted(false);
            } else if (productDto.getCurrentQuantity() == 0) {
                product.set_activated(false);
                product.set_deleted(true);
            }
            product.setCostPrice(productDto.getCostPrice());
            product.setDiscount(productDto.getDiscount());
            product.setCategory(productDto.getCategory());
            return productRepository.save(product);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product productUpdate = productRepository.getReferenceById(productDto.getId());
            if (imageProduct.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct)) {
                    productUpdate.setImage(productUpdate.getImage());
                } else {
                    imageUpload.uploadFile(imageProduct);
                    productUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                }
            }
            if (productDto.getCurrentQuantity() > 0) {
                productUpdate.set_activated(true);
                productUpdate.set_deleted(false);
            } else if (productDto.getCurrentQuantity() == 0) {
                productUpdate.set_activated(false);
                productUpdate.set_deleted(true);
            }
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setId(productUpdate.getId());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setDiscount(productDto.getDiscount());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            productUpdate.setManufactureDate(productDto.getManufactureDate());

            return productRepository.save(productUpdate);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void enableById(Integer id) {
        Product product = productRepository.getById(id);
        if (product.getCurrentQuantity() > 0) {
            product.set_activated(true);
            product.set_deleted(false);
            productRepository.save(product);
        }
    }

    @Override
    public void disableById(Integer id) {
        Product product = productRepository.getById(id);
        if (product.getCurrentQuantity() == 0) {
            product.set_deleted(true);
            product.set_activated(false);
            productRepository.save(product);
        }
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }


    @Override
    public ProductDto getById(Integer id) {
        ProductDto productDto = new ProductDto();
        Product product = productRepository.getById(id);
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImage(product.getImage());
        productDto.setManufactureDate(product.getManufactureDate());
        return productDto;
    }

    @Override
    public List<ProductDto> randomProduct() {
        return transferData(productRepository.randomProduct());
    }

    @Override
    public Page<ProductDto> searchProducts(Integer pageNo, String keyword) {
        List<Product> products = productRepository.findAllByNameOrDescription(keyword);
        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    @Override
    public Page<ProductDto> getAllProducts(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDto> productDtoLists = this.allProduct();
        return (Page<ProductDto>) toPage(productDtoLists, pageable);
    }

    @Override
    public List<ProductDto> findAllByCategory(String category) {
        return transferData(productRepository.findAllByCategory(category));
    }

    @Override
    public List<ProductDto> filterHighProducts() {
        return transferData(productRepository.filterHighProducts());
    }

    @Override
    public List<ProductDto> filterLowerProducts() {
        return transferData(productRepository.filterLowerProducts());
    }

    @Override
    public List<ProductDto> listViewProducts() {
        return transferData(productRepository.listViewProduct());
    }

    @Override
    public List<ProductDto> findByCategoryId(Integer id) {
        return transferData(productRepository.getProductByCategoryId(id));
    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        return transferData(productRepository.searchProducts(keyword));
    }


    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setDiscount(product.getDiscount());
            productDto.setDescription(product.getDescription());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setManufactureDate(product.getManufactureDate());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    public void reduceProductQuantity(Integer productId, Integer quantity) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getCurrentQuantity() >= quantity) {
                product.setCurrentQuantity(product.getCurrentQuantity() - quantity);
                productRepository.save(product);
            }
        } else {
            throw new RuntimeException("Không tìm thấy mã sản phẩm: " + productId);
        }
    }


    public void updateExpiredProducts() {
        Date expiredDate = new Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000L);
        List<Product> expiredProducts = productRepository.findExpiredProducts(expiredDate);

        for (Product product : expiredProducts) {

            product.setCurrentQuantity(0);
            product.set_deleted(true);
            product.set_activated(false);

            productRepository.save(product);

        }
    }

    @Override
    public List<Product> getProductByManufactureDate(Date date) {
        return productRepository.findByManufactureDate(date);
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
//    Customer

}
