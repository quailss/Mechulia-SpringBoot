package com.quailss.demo.service;

import com.quailss.demo.domain.Bookmark;
import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public List<Bookmark> findByMemberId(Long memberId) {
        return bookmarkRepository.findByMemberId(memberId);
    }

    public void addBookmark(Recipe recipe, Member member) {
        Bookmark newBookmark = Bookmark.builder()
                .member(member)
                .recipe(recipe)
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
}
