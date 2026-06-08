package com.example.request.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.request.dto.UserRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/api/users")
public class RequestController {
  
  // 테스트 요청 주소
  // http://localhost:8080/api/users/v1?name=홍길동&age=30
  
  // 요청 파라미터 1 (HttpServletRequest 활용하기)
  @GetMapping("/v1")
  public void legacyHandler(HttpServletRequest request) {
    // 모든 요청 파라미터는 String 타입으로 전달
    String name = request.getParameter("name");
    String age = request.getParameter("age");
    System.out.println("이름: " + name + ", 나이: " + age + "살");
  }

  // 요청 파라미터 2 (@RequesteParam)
  @GetMapping("/v2")
  public void parameterHandler(@RequestParam("name") String name, @RequestParam(value = "age", required = false, defaultValue = "0") int age) {
    System.out.println("이름: " + name + ", 나이: " + age + "살");
  }

  // 요청 파라미터 3 (커맨드 객체 이용 - 파라미터를 필드로 이용하는 객체)
  @GetMapping("/v3")
  public void commandObjectHandler(UserRequest userRequest) {
    System.out.println(userRequest);
  }

  // 요청 본문 (요청을 본문에 담아서 보내는 POST 방식)
  // 클라이언트: JSON - 서버: 자바 객체
  // 스프링 부트의 MessageConverter는 Jackson이 기본 설정 (Spring Web)
  @PostMapping("/v4")
  public void requestBodyHandler(@RequestBody UserRequest userRequest) {
    System.out.println(userRequest);
  }

  // 파일 첨부 요청
  // Method: POST
  // EncType: multipart/form-data
  // 부트 서버는 MultipartFile 타입으로 파일을 받을 수 있음
  // 파일을 제외한 나머지 파라미터는 @RequestParam 혹은 커맨드 객체으로 받을 수 있음 - 커맨드 객체 추천
  @PostMapping("/v5")
  public void fileUploadHandler(
    @RequestPart("profile") MultipartFile file, 
    UserRequest request
  ) {
    if(file.isEmpty()) {
      System.out.println("파일이 첨부되지 않았습니다.");
      return;
    } 
    System.out.println("첨부된 파일 이름: " + file.getOriginalFilename());
    System.out.println("첨부된 파일 크기: " + file.getSize() + " Bytes");
    System.out.println("텍스트 데이터: " + request);
  }
}
