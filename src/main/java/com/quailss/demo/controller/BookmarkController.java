package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Bookmark;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.service.AuthService;
import com.quailss.demo.service.BookmarkService;
import com.quailss.demo.service.RecipeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final RecipeService recipeService;
    private final AuthService authService;

    @GetMapping("/member") //멤버가 저장한 레시피들
    public ResponseEntity<List<Bookmark>> getBookmarks(HttpSession session){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new RuntimeException("로그인하지 않은 사용자입니다."));

        List<Bookmark> bookmarkList = bookmarkService.findByMemberId(loggedInMember.getId());
        return ResponseEntity.ok(bookmarkList);
    }

    @GetMapping("/checked/{recipeId}")
    public ResponseEntity<Boolean> getBookmark(HttpSession session,
                                               @PathVariable Long recipeId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new RuntimeException("로그인하지 않은 사용자입니다."));

        boolean isBookmarked = bookmarkService.isRecipeBookmarkedByMember(loggedInMember.getId(), recipeId);
        return ResponseEntity.ok(isBookmarked);
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<String> addBookmark(HttpSession session,
                                              @PathVariable Long recipeId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new RuntimeException("로그인하지 않은 사용자입니다."));

        bookmarkService.addBookmark(recipeId, loggedInMember);

        return ResponseEntity.ok("Bookmark successfully saved");
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<String> deleteBookmark(HttpSession session, @PathVariable Long bookmarkId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new RuntimeException("로그인하지 않은 사용자입니다."));

        Bookmark bookmark = bookmarkService.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        if(!bookmark.getMember().getEmail().equals(loggedInMember.getEmail()))
            throw new AccessDeniedException("북마크 등록자가 아닙니다.");

        try {
            bookmarkService.deleteBookmark(bookmark);
            return ResponseEntity.ok("Rookmark deleted successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
