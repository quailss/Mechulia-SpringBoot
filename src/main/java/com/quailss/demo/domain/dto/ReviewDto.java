package com.quailss.demo.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReviewDto {
    BigDecimal score;
    String content;
}
