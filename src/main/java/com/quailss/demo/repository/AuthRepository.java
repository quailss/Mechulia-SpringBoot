package com.quailss.demo.repository;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.enums.Provider;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String Email);

    @Query("SELECT m FROM Member m WHERE m.provider = :provider AND m.provider_id = :providerId")
    Optional<Member> findByProviderAndProviderId(@Param("provider") Provider provider, @Param("providerId") String providerId);
}
