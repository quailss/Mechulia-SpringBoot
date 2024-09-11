package com.quailss.demo.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class ReviewCommand {
    Long recipe_id;
    BigDecimal score;
    String content;
}
