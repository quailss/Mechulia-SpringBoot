package com.quailss.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long review_id;

    private Double score;

    private String content;

    @ManyToOne
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership member; // 리뷰 작성자

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
