package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;
    private String address;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customer_role", joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Collection<Role> roles;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShoppingCart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    public Customer() {
        this.orders = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", roles=" + roles +
                ", cart=" + cart.getId() +
                ", orders=" + orders.size() +
                '}';
    }
}
