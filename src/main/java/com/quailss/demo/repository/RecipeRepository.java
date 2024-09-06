package com.quailss.demo.repository;

import com.quailss.demo.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT rp FROM Recipe rp WHERE rp.keyword1 LIKE %:keyword% OR rp.keyword2 LIKE %:keyword% OR rp.keyword3 LIKE %:keyword% OR rp.keyword4 LIKE %:keyword%")
    Page<Recipe> findAllByKeyword(String keyword, Pageable pageable);
    @Query("SELECT rp FROM Recipe rp WHERE rp.menu.id = :menu_id " +
            "AND (rp.keyword1 LIKE %:keyword% OR rp.keyword2 LIKE %:keyword% OR rp.keyword3 LIKE %:keyword% OR rp.keyword4 LIKE %:keyword%)")
    Page<Recipe> findAllByMenuIdAndKeyword(Long menu_id, String keyword, Pageable pageable);

}
