package com.quailss.demo.controller;


import com.quailss.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            memberService.changePhoneNumber(memberEmail,phonenumber);
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
            memberService.changeName(memberEmail,name);
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
            memberService.changeBirthday(memberEmail,birthday);
            return ResponseEntity.ok("생일 설정 변경 성공하였습니다.");
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }
}
