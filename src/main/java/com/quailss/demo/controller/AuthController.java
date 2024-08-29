package com.quailss.demo.controller;

import com.quailss.demo.domain.Member;
import com.quailss.demo.domain.dto.RegisterDto;
import com.quailss.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("member", new RegisterDto());
        return "register";
    }

    //회원 등록
    @PostMapping("/register")
    public String registerMember(@ModelAttribute("member") @Valid RegisterDto registerDto, BindingResult bindingResult,
                                 Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if(bindingResult.hasErrors())
            return "/";

        try {
            authService.register(registerDto);
            redirectAttributes.addFlashAttribute("registerSuccess", true);
            return "redirect:/login";
        } catch (Exception e) {
            return "/";
        }
    }

    //아이디 중복 검사
    @PostMapping("/check-email")
    public Boolean findByEmail(@RequestParam("email") String email){
        Optional<Member> optionalMember = authService.findByEmail(email);

        if(optionalMember.isEmpty())
            return true;    //사용 가능
        return false;
    }
}
