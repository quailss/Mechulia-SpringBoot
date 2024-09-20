package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.Bookmark;
import com.quailss.demo.domain.Recipe;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.service.AuthService;
import com.quailss.demo.service.BookmarkService;
import com.quailss.demo.service.MemberService;
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
        String loggedInEmail = (String) session.getAttribute("Email");
        Provider loggedInProvider = (Provider) session.getAttribute("Provider");

        Optional<Member> memberOptional = authService.findByEmailAndProvider(loggedInEmail, loggedInProvider);

        if(memberOptional.isEmpty())
            throw new RuntimeException("로그인하지 않은 사용자입니다.");

        List<Bookmark> bookmarkList = bookmarkService.findByMemberId(memberOptional.get().getId());
        return ResponseEntity.ok(bookmarkList);
    }

    @GetMapping("/checked")
    public ResponseEntity<Boolean> getBookmark(HttpSession session,
                                               @RequestParam Long recipeId){
        String loggedInEmail = (String) session.getAttribute("Email");
        Provider loggedInProvider = (Provider) session.getAttribute("Provider");

        Optional<Member> memberOptional = authService.findByEmailAndProvider(loggedInEmail, loggedInProvider);
        if(memberOptional.isEmpty())
            throw new RuntimeException("로그인하지 않은 사용자입니다.");

        Optional<Recipe> recipeOptional = recipeService.getRecipe(recipeId);
        if(recipeOptional.isEmpty())
            throw new RuntimeException("존재하지 않는 레시피입니다.");

        Optional<Bookmark> bookmarkOptional = bookmarkService.findByMemberIdAndRecipeId(memberOptional.get().getId(), recipeOptional.get().getId());
        if(bookmarkOptional.isEmpty())
            return ResponseEntity.ok(false);
        else
            return ResponseEntity.ok(true);
    }

    @PostMapping("/addBookmark")
    public ResponseEntity<String> addBookmark(HttpSession session,
                                              @RequestParam Long recipeId){
        //유저 정보 가져오기 - 세션
        String loggedInEmail = (String) session.getAttribute("Email");
        Provider loggedInProvider = (Provider) session.getAttribute("Provider");

        Optional<Recipe> recipeOptional = recipeService.getRecipe(recipeId);
        Optional<Member> memberOptional = authService.findByEmailAndProvider(loggedInEmail, loggedInProvider);

        if(memberOptional.isEmpty())
            throw new RuntimeException("로그인하지 않은 사용자입니다.");
        if(recipeOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "레시피를 찾을 수 없습니다.");

        bookmarkService.addBookmark(recipeOptional.get(), memberOptional.get());

        return ResponseEntity.ok("Bookmark successfully saved");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview(HttpSession session, @RequestParam Long bookmarkId){
        String loggedinEmail = (String) session.getAttribute("Email");

        Bookmark bookmark = bookmarkService.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        if(!bookmark.getMember().getEmail().equals(loggedinEmail))
            throw new AccessDeniedException("북마크 등록자가 아닙니다.");

        try {
            bookmarkService.deleteBookmark(bookmark);
            return ResponseEntity.ok("Rookmark deleted successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
