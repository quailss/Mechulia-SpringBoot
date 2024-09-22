package com.quailss.demo.service;

import com.quailss.demo.domain.Bookmark;
import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.exception.EntityNotFoundException;
import com.quailss.demo.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new EntityNotFoundException.RecipeNotFoundException("레시피를 찾을 수 없습니다."));

        Bookmark newBookmark = Bookmark.builder()
                .member(member)
                .recipe(isExistingRecipe)
                .build();
        bookmarkRepository.save(newBookmark);
    }
    public void deleteBookmark(Member loggedInMember, Long bookmarkId) {
        Bookmark bookmark = findById(bookmarkId)
                .orElseThrow(() -> new EntityNotFoundException.BookmarkNotFoundException("북마크를 찾을 수 없습니다."));

        if(!bookmark.getMember().getEmail().equals(loggedInMember.getEmail()))
            throw new AccessDeniedException("북마크 등록자가 아닙니다.");

        bookmarkRepository.delete(bookmark);
    }

    public Optional<Bookmark> findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId);
    }

    public Optional<Bookmark> findByMemberIdAndRecipeId(Long memberId, Long recipeId) {
        Recipe isExistingRecipe = recipeService.getRecipe(recipeId)
                .orElseThrow(() -> new EntityNotFoundException.RecipeNotFoundException("레시피를 찾을 수 없습니다."));

        return bookmarkRepository.findByMemberIdAndRecipeId(memberId, isExistingRecipe.getId());
    }

    public void deleteBookmarkByRecipe(Member loggedInMember, Long recipeId) {
        Optional<Bookmark> bookmarkOptional = findByMemberIdAndRecipeId(loggedInMember.getId(), recipeId);

        if(!bookmarkOptional.get().getMember().getEmail().equals(loggedInMember.getEmail()))
            throw new AccessDeniedException("북마크 등록자가 아닙니다.");

        if(!bookmarkOptional.get().getMember().getEmail().equals(loggedInMember.getEmail()))
            throw new AccessDeniedException("북마크 등록자가 아닙니다.");

        bookmarkRepository.delete(bookmarkOptional.get());
    }
}
