package com.example.ioc.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ioc.dto.UserDto;
import com.example.ioc.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserController {

  // 알림 서비스 객체

  // 1. 필드 주입
  // @Autowired
  // private NotificationService notificationService;

  // 2. Setter 주입 (Setter의 매개변수로 주입)
  // private NotificationService notificationService;

  // @Autowired
  // public void setNotificationService(NotificationService notificationService) {
  //   this.notificationService = notificationService;
  // }

  // 3. 생성자 주입 (생성자의 매개변수로 주입)
  // private NotificationService notificationService;

  // @Autowired: 스프링 4.3 이후 생성자가 1개인 경우 생략 가능
  // public UserController(NotificationService notificationService) {
  //   this.notificationService = notificationService;
  // }

  // 실무 DI
  // 객체 NPE 방지, 객체 불변성 보장, 순환 참조 방지 (A -> B, B -> A, A -> B, B -> A) 하기 위해
  // 필드 선언 시 final 키워드를 추가합니다.
  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

  // 롬복 사용 시 생성자 자동 생성
  public UserController(@Qualifier("emailNotificationService") NotificationService notificationService, ObjectMapper objectMapper) {
    this.notificationService = notificationService;
    this.objectMapper = objectMapper;
  }

  // 회원 가입 시 알림 서비스 사용
  @RequestMapping("/join")
  public void createUser(){
    notificationService.sendNotification("반갑습니다!");
  }

  // 회원 정보 수정 시 알림 서비스 사용
  @RequestMapping("/modify")
  public void modifyUser(){
    notificationService.sendNotification("수정되었습니다.");
  }

  // ObjectMapper 동작 테스트
  @RequestMapping("/json-test")
  public void jsonTest(){
    try{
      // 1. 자바 객체 -> JSON 문자열 (직렬화: Serialization)
      UserDto userDto = new UserDto("홍길동", 30);
      String jsonString = objectMapper.writeValueAsString(userDto);
      System.out.println("JSON 문자열: " + jsonString);

      // 2. JSON 문자열 -> 자바 객체 (역직렬화: Deserialization)
      String inputJson = "{\"name\":\"홍길동\",\"age\":30}";
      UserDto deserializedUser = objectMapper.readValue(inputJson, UserDto.class);
      System.out.println("역직렬화된 UserDto: " + deserializedUser);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
}
