package com.quailss.demo.repository;

import com.quailss.demo.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMemberId(Long memberId);

    Optional<Bookmark> findByMemberIdAndRecipeId(Long memberId, Long recipeId);
}
