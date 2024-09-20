package com.quailss.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="menu_id", nullable = false)
    private Menu menu;

    private BigDecimal average;

    private String image_url;

    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String keyword4;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();
}
