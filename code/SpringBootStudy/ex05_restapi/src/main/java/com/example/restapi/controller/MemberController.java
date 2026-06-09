package com.example.restapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.dto.MemberRequest;
import com.example.restapi.dto.MemberResponse;
import com.example.restapi.service.MemberService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;
  

  // 1. 등록
  @PostMapping
  public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest request) {
    MemberResponse savedMember = memberService.save(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
  }

  // 2. 전체 조회
  @GetMapping
  public ResponseEntity<List<MemberResponse>> getAllMembers(@RequestParam String param) {
    return ResponseEntity.ok(memberService.findAll());
  }
  
  // 3. 단건 조회
  @GetMapping("/{id}")
  public ResponseEntity<MemberResponse> getMemberById(@PathVariable("id") Long id) {
    try {
      MemberResponse foundMember = memberService.findById(id);
      return ResponseEntity.ok(foundMember);
    }catch(Exception e){
      return ResponseEntity.notFound().build();
    }
  }
  
  // 4. 수정
  @PutMapping("/{id}")
  public ResponseEntity<MemberResponse> update(
    @PathVariable Long id,
    @RequestBody MemberRequest request) {
      try{
        MemberResponse updatedMember = memberService.update(id, request);
        return ResponseEntity.ok(updatedMember);
      }catch(Exception e){
        return ResponseEntity.notFound().build();
      }
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteMember(@PathVariable("id") Long id){
    try {
      memberService.delete(id);
      return ResponseEntity.noContent().build();
    }catch(Exception e){
      return ResponseEntity.notFound().build();
    }
  }
}
