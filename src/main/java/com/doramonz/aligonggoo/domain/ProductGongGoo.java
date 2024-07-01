package com.doramonz.aligonggoo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_gongGoo")
public class ProductGongGoo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_gongGoo_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "gongGoo_url")
    private String url;

    @Column(name = "gongGoo_price")
    private Integer price;

    @Column(name = "gongGoo_status")
    private Boolean status;

    @Column(name = "gongGoo_created")
    private LocalDateTime created;
}