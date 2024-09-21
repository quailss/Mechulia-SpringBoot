package com.quailss.demo.controller;


import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth/profile")
public class MemberController {

    MemberService memberService;

    //휴대폰 번호 변경
    @PutMapping("/phonenumber")
    public ResponseEntity<String> setPhoneNumber(@RequestParam("phoneNumber") String phonenumber, HttpSession httpSession){
        try{
            String memberEmail = (String) httpSession.getAttribute("Email");
            Provider provider = (Provider) httpSession.getAttribute("Provider");
            memberService.changePhoneNumber(memberEmail,phonenumber, provider);
            return ResponseEntity.ok("휴대폰 번호 설정 변경 성공하였습니다.");
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }

    //이름 변경
    @PutMapping("/name")
    public ResponseEntity<String> setname(@RequestParam("name") String name, HttpSession httpSession){
        try{
            String memberEmail = (String) httpSession.getAttribute("Email");
            Provider provider = (Provider) httpSession.getAttribute("Provider");
            memberService.changeName(memberEmail,name, provider);
            return ResponseEntity.ok("이름 설정 변경 성공하였습니다.");
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }

    //생일 변경
    @PutMapping("/birthday")
    public ResponseEntity<String> setbirthday(@RequestParam("birthday") LocalDate birthday, HttpSession httpSession){
        try{
            String memberEmail = (String) httpSession.getAttribute("Email");
            Provider provider = (Provider) httpSession.getAttribute("Provider");
            memberService.changeBirthday(memberEmail,birthday, provider);
            return ResponseEntity.ok("생일 설정 변경 성공하였습니다.");
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }

    @DeleteMapping("/delete-member")
    public ResponseEntity<String> deleteMember(HttpSession httpSession){
        try{
            String memberEmail = (String) httpSession.getAttribute("Email");
            Provider provider = (Provider) httpSession.getAttribute("Provider");
            memberService.changeWithdrawalMemberstatus(memberEmail,provider);
            return ResponseEntity.ok("회원이 탈퇴되었습니다.");
        }catch (NullPointerException e){
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }

}
