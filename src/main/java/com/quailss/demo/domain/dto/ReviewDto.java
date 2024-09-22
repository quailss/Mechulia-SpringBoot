package com.quailss.demo.domain.dto;

import com.quailss.demo.domain.enums.MemberStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDto {
    private Long id;
    private BigDecimal score;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberId;
    private String memberName;
    private MemberStatus memberStatus;
    private Long recipeId;
}
