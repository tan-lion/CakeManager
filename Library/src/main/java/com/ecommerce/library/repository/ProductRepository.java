package com.ecommerce.library.repository;

import com.ecommerce.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Integer> {


    @Query("select p from Product p where p.is_deleted = false and p.is_activated = true")
    List<Product> getAllProduct();

    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> findAllByNameOrDescription(String keyword);


    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.name = ?1 and p.is_activated = true and p.is_deleted = false")
    List<Product> findAllByCategory(String category);

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.discount, p.manufacture_date, p.image, p.is_activated, p.is_deleted " +
            "from products p where p.is_activated = true and p.is_deleted = false order by rand() limit 9", nativeQuery = true)
    List<Product> randomProduct();

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.manufacture_date, p.discount, p.image, p.is_activated, p.is_deleted " +
            "from products p where p.is_deleted = false and p.is_activated = true order by p.cost_price desc limit 9", nativeQuery = true)
    List<Product> filterHighProducts();
    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.manufacture_date, p.discount, p.image, p.is_activated, p.is_deleted " +
            "from products p where p.is_deleted = false and p.is_activated = true order by p.cost_price asc limit 9", nativeQuery = true)
    List<Product> filterLowerProducts();


    @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.manufacture_date, p.category_id, p.discount, p.image, p.is_activated, p.is_deleted from products p where p.is_deleted = false and p.is_activated = true limit 4", nativeQuery = true)
    List<Product> listViewProduct();


    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByCategoryId(Integer id);


    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProducts(String keyword);

    @Query("select p from Product p where p.manufactureDate <= :expiredDate and p.currentQuantity > 0")
    List<Product> findExpiredProducts(@Param("expiredDate") Date expiredDate);

    List<Product> findByManufactureDate(Date date);
}
