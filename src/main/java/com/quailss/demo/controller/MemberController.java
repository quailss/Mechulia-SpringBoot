package com.quailss.demo.controller;


import com.quailss.demo.domain.dto.ResponseMemberInfoDTO;
import com.quailss.demo.domain.enums.Provider;
import com.quailss.demo.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth/profile")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<ResponseMemberInfoDTO> getMemberinfo(HttpSession httpSession){
                ResponseMemberInfoDTO responseMemberInfoDTO = memberService.getMemberInfoSession(httpSession);
                return ResponseEntity.ok(responseMemberInfoDTO);
    }

    //회원 정보 삭제
    @DeleteMapping("/delete-member")
    public ResponseEntity<String> deleteMember(HttpSession httpSession){
        try{
            memberService.changeWithdrawalMemberstatus(httpSession);
            return ResponseEntity.ok("회원이 탈퇴되었습니다.");
        }catch (NullPointerException e){
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }

    //회원 정보 변경
    @PutMapping("/Memberinformation")
    public ResponseEntity<String> setMemberInfo(@RequestBody ResponseMemberInfoDTO responseMemberInfoDTO, HttpSession httpSession){
        try{
            memberService.changeMemberInfo(responseMemberInfoDTO, httpSession);
            return ResponseEntity.ok("회원정보가 수정되었습니다.");
        }catch (NullPointerException e){
            return ResponseEntity.badRequest().body("회원정보 수정 실패하였습니다.");
        }
    }

}
