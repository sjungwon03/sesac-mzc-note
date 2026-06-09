package com.example.validation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.validation.dto.MemberCreateRequest;
import com.example.validation.dto.MemberDto;
import com.example.validation.dto.MemberUpdateRequest;
import com.example.validation.exception.CustomException;
import com.example.validation.exception.ErrorCode;

@Service
public class MemberService {
  private final Map<Long, MemberDto> store = new ConcurrentHashMap<>();
  private final AtomicLong sequence = new AtomicLong();

  public MemberService(){
    save(MemberCreateRequest.builder().username("kim").email("kim@test.com").build());
    save(MemberCreateRequest.builder().username("lee").email("lee@test.com").build());
    save(MemberCreateRequest.builder().username("jeong").email("jeong@test.com").build());
  }

  //1. 회원 저장
  public MemberDto save(MemberCreateRequest request){
    //이메일 중복 검증
    boolean isExistEmail = store.values().stream().anyMatch(member -> member.email().equals(request.email()));
    if(isExistEmail) {
      throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
    }

    Long id = sequence.incrementAndGet();
    MemberDto member = MemberDto.builder().id(id).username(request.username()).email(request.email()).createdAt(LocalDateTime.now()).build();
    store.put(id, member);
    return member;
  }

  // Read All
  public List<MemberDto> findAll(){
    return store.values().stream().toList();
  }

  // Read One
  public MemberDto findById(Long id){
    MemberDto foundMember = store.get(id);
    // 없는 회원 예외처리
    if(foundMember == null){
      throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
    }

    return foundMember;
  }

  // Update
  public MemberDto updateMember(Long id, MemberUpdateRequest request){
    MemberDto foundMember = findById(id);
    MemberDto updatedMember = MemberDto.builder().id(foundMember.id()).username(foundMember.username()).email(foundMember.email()).createdAt(foundMember.createdAt()).build();
    store.put(id, updatedMember);
    return updatedMember;
  }

  public void deleteById(Long id){
    findById(id);
    store.remove(id);
  }  
}
