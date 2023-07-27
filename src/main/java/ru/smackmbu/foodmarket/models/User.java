package ru.smackmbu.foodmarket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    @ElementCollection
    @CollectionTable(name = "users_products",
            joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "count")
    private Map<Product, Integer> products;

    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    private double balance;

    public User(String username, String password, Map<Product, Integer> products, Role role) {
        this.username = username;
        this.password = password;
        this.products = products;
        this.role = role;
        this.orders = new LinkedList<>();
        this.balance = 0.0;
    }

    public User() {

    }
}
