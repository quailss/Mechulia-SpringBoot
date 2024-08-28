package com.quailss.demo.domain;

import com.quailss.demo.domain.enums.Provider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    private String email;

    private String provider_id;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @CreatedDate
    private LocalDateTime created_at;
    @LastModifiedDate
    private LocalDateTime updated_at;

    private LocalDateTime deleted_at;
}
