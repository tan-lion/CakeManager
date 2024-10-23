package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private Integer currentQuantity;
    private Double costPrice;
    private Integer discount;
    private String image;
    private Category category;
    private boolean activated;
    private boolean deleted;
    private String currentPage;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date manufactureDate;

}
