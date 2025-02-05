package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.BookmarkDto;
import com.quailss.demo.exception.EntityNotFoundException;
import com.quailss.demo.service.AuthService;
import com.quailss.demo.service.BookmarkService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final AuthService authService;

    @GetMapping("/member") //멤버가 저장한 북마크들
    public ResponseEntity<List<BookmarkDto>> getBookmarks(HttpSession session){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));

        List<BookmarkDto> bookmarkList = bookmarkService.findByMemberId(loggedInMember.getId());
        return ResponseEntity.ok(bookmarkList);
    }

    @GetMapping("/checked/{recipeId}")
    public ResponseEntity<Boolean> getBookmark(HttpSession session,
                                               @PathVariable Long recipeId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));
        boolean isBookmarked = bookmarkService.findByMemberIdAndRecipeId(loggedInMember.getId(), recipeId).isPresent();
        return ResponseEntity.ok(isBookmarked);
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<String> addBookmark(HttpSession session,
                                              @PathVariable Long recipeId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));
        bookmarkService.addBookmark(recipeId, loggedInMember);
        return ResponseEntity.ok("Bookmark successfully saved");
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<String> deleteBookmark(HttpSession session, @PathVariable Long bookmarkId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));

        bookmarkService.deleteBookmark(loggedInMember, bookmarkId);
        return ResponseEntity.ok("Bookmark deleted successfully");
    }

    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<String> deleteBookmarkByRecipe(HttpSession session, @PathVariable Long recipeId){
        Member loggedInMember = authService.getLoggedInMember(session)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("로그인하지 않은 사용자입니다."));

        bookmarkService.deleteBookmarkByRecipe(loggedInMember, recipeId);
        return ResponseEntity.ok("Bookmark deleted successfully");
    }
}