package com.fultil.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItems> orderLineItems;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // the person who placed the order
}
