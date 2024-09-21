package com.quailss.demo.service;

import com.quailss.demo.domain.Bookmark;
import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final RecipeService recipeService;

    public List<Bookmark> findByMemberId(Long memberId) {
        return bookmarkRepository.findByMemberId(memberId);
    }

    public void addBookmark(Long recipeId, Member member) {
        Recipe isExistingRecipe = recipeService.getRecipe(recipeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 레시피입니다."));

        Bookmark newBookmark = Bookmark.builder()
                .member(member)
                .recipe(isExistingRecipe)
                .build();
        bookmarkRepository.save(newBookmark);
    }
    public void deleteBookmark(Bookmark bookmark) {
        bookmarkRepository.delete(bookmark);
    }

    public Optional<Bookmark> findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId);
    }

    public Optional<Bookmark> findByMemberIdAndRecipeId(Long memberId, Long recipeId) {
        return bookmarkRepository.findByMemberIdAndRecipeId(memberId, recipeId);
    }

    public boolean isRecipeBookmarkedByMember(Long memberId, Long recipeId) {
        Recipe isExistingRecipe = recipeService.getRecipe(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다."));

        Optional<Bookmark> bookmarkOptional = findByMemberIdAndRecipeId(memberId, isExistingRecipe.getId());
        return bookmarkOptional.isPresent();
    }
}
