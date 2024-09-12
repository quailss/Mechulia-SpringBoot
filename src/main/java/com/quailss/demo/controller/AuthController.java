package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.FindIdDto;
import com.quailss.demo.domain.dto.LoginDto;
import com.quailss.demo.domain.dto.RegisterDto;
import com.quailss.demo.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/register")
    public ResponseEntity<String> showRegisterForm(Model model){
        model.addAttribute("member", new RegisterDto());
        return ResponseEntity.ok("register success");
    }

    //회원 등록
    @PostMapping("/register")
    public ResponseEntity<Boolean> registerMember(@RequestBody @Valid RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
           System.out.println("errors " + bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(false);
        }

        try {
            authService.register(registerDto);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    //아이디 중복 검사
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> findByEmail(@RequestBody Map<String, String> request){
        String email = request.get("email");
        Optional<Member> memeberOptional = authService.findByEmail(email);
        if(memeberOptional.isEmpty())
            return ResponseEntity.ok(true);
        return ResponseEntity.ok(false);
    }

    //로그인
    @PostMapping("login")//
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpSession httpSession){

        try{
            LoginDto verifiedMember = authService.verificationMember(loginDto.getEmail(),loginDto.getPassword());
            httpSession.setAttribute("Email",verifiedMember.getEmail());
            return ResponseEntity.ok("로그인 성공");
        }catch (NullPointerException e){
            return ResponseEntity.badRequest().body("로그인 실패");

        }
    }

    //아이디 찾기
    @PostMapping("find-id")
    public ResponseEntity<String> findByid(@RequestBody FindIdDto findIdDto){
        try{
            String memberEmail = authService.getAuthenticatedMemberId(findIdDto.getName(),findIdDto.getPhoneNumber());
            return ResponseEntity.ok("이메일:" + memberEmail);
        } catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
}
