package com.example.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.restapi.dto.MemberRequest;
import com.example.restapi.dto.MemberResponse;

@Service
public class MemberServiceImpl implements MemberService {

  // 인 메모리 데이터베이스 맵
  private final Map<Long, MemberResponse> memberDatabase = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  // Mock 데이터 
  public MemberServiceImpl() {
    for(int i = 1; i <= 10; i++) {
      Long id = idGenerator.getAndIncrement();
      MemberResponse member = MemberResponse.builder()
        .id(id)
        .email("user" + id + "@example.com")
        .createdAt(java.time.LocalDateTime.now())
        .build();
      memberDatabase.put(id, member);
    }
  }

  @Override
  public MemberResponse save(MemberRequest request) {
    Long id = idGenerator.getAndIncrement();
    MemberResponse member = MemberResponse.builder()
      .id(id)
      .email(request.email())
      .createdAt(java.time.LocalDateTime.now())
      .build();
    memberDatabase.put(id, member);
    return member;
  }

  @Override
  public List<MemberResponse> findAll() {
    return new ArrayList<>(memberDatabase.values());
  }

  @Override
  public MemberResponse findById(Long id) {
    MemberResponse response = memberDatabase.get(id);
    if(response == null){
      throw new RuntimeException("Member not found with id: " + id);
    }
    return response;
  }

  @Override
  public MemberResponse update(Long id, MemberRequest request) {
    MemberResponse existingMember = memberDatabase.get(id);
    if(existingMember == null){
      throw new RuntimeException("Member not found with id: " + id);
    }
    MemberResponse updatedMember = MemberResponse.builder()
      .id(id)
      .email(request.email())
      .createdAt(existingMember.createdAt())
      .build();
    memberDatabase.put(id, updatedMember);
    return updatedMember;
  }

  @Override
  public void delete(Long id) {
    findById(id); // 존재 여부 확인
    memberDatabase.remove(id);
  }

  
}
