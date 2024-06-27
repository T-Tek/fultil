package com.fultil.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItems> cartItems = new ArrayList<>();

    public Cart(User user) {
        this.user = user;
    }

    public void addCartItem(CartItems cartItem) {
        this.cartItems.add(cartItem);
    }

    public void clearItems() {
        this.cartItems.clear();
    }
}
