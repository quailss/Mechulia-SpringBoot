package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.FindIdDto;
import com.quailss.demo.domain.dto.LoginDto;
import com.quailss.demo.domain.dto.RegisterDto;
import com.quailss.demo.domain.dto.SetPasswordDto;
import com.quailss.demo.domain.enums.Provider;
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
        //model.addAttribute("member", new RegisterDto());
        return ResponseEntity.ok("register success");
    }

    @GetMapping("/session-info")
    public ResponseEntity<Map<String, Object>> getMemberInfo(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        String loggedInEmail  = (String)session.getAttribute("Email");
        Provider provider = (Provider) session.getAttribute("Provider");
        //System.out.println("loggedIn "+loggedInEmail);

        if(loggedInEmail == null) {
            response.put("loggedIn", false);
            response.put("nickname", "");
            response.put("email", "");
        }else{
            Optional<Member> memberOptional = authService.findByEmailAndProvider(loggedInEmail, provider);

            if(memberOptional.isEmpty()){
                response.put("loggedIn", false);
                response.put("nickname", "");
                response.put("email", "");
            }else {
                response.put("loggedIn", true);
                response.put("nickname", memberOptional.get().getName());
                response.put("email", memberOptional.get().getEmail());
            }
        }


        return ResponseEntity.ok(response);
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
        Optional<Member> memeberOptional = authService.findByEmailAndProvider(email, Provider.LOCAL);
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
            httpSession.setAttribute("Provider", Provider.LOCAL);

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

    //비밀번호 재설정
    @PostMapping
    public ResponseEntity<String> setPassword(@RequestBody SetPasswordDto setPasswordDto){
        try{
            Long memberPassword = authService.verifyAndResetPassword(setPasswordDto.getEmail(), setPasswordDto.getPhoneNumber(), setPasswordDto.getPassword());
            return ResponseEntity.ok("비밀번호 재설정 완료");
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

    }


}
