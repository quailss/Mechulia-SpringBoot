package com.quailss.demo.domain;

import com.quailss.demo.domain.enums.Provider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;
    private String phonenumber;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String provider_id;
    //private String profile_image;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @CreatedDate
    private LocalDateTime created_at;
    @LastModifiedDate
    private LocalDateTime updated_at;

    //private LocalDateTime deleted_at;



    /*public void updateProfile(String nickname, String profileImage) {
        this.name = nickname;
        this.profile_image = profileImage;
    }*/

    //소셜 로그인 생성자
    public Member(String email, String name, Provider provider, String providerId) {
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.provider_id = providerId;
        this.created_at = LocalDateTime.now();
    }

    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = Provider.LOCAL;
        this.created_at = LocalDateTime.now();
    }
}
