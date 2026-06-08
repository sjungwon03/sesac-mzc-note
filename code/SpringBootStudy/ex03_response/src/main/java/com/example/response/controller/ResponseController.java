package com.example.response.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @Controller
@RestController // @Controller + @ResponseBody
@RequestMapping("/api/users")
public class ResponseController {

  // JSON 문자열 응답
  @GetMapping("/v1")
  // @ResponseBody // 반환 값을 View로 해석하지 않고, 데이터로 처리함
  public String responseString() {
    String jsonString = "{\"name\": \"홍길동\", \"age\": 30}";
    return jsonString;
  }

  // 응답 전용 객체 ResponseEntity<T>
  // 1. HTTP 상태 코드 반환 가능
  // 2. 응답 본문 작성 가능
  // 3. @ResponseBody 명시 불필요
  @GetMapping("/v3")
  public ResponseEntity<Map<String, String>> responseEntity() {
    // return ResponseEntity.ok(new UserResponse("홍길동", 30)); 

    // return ResponseEntity.badRequest().body(Map.of("message", "잘못된 요청"));

    // return new ResponseEntity<>(Map.of("message", "잘못된 요청"), HttpStatus.BAD_REQUEST);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "인증 실패"));
  }
}
