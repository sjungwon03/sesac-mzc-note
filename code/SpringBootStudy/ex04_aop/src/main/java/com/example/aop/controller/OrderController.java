package com.example.aop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.aop.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor // 생성자 주입을 이용한 DI (final 필드 전용)
@RestController
public class OrderController {
  private final OrderService orderService;

  @GetMapping("/aop-test")
  public String aopTest(@RequestParam("id") String itemId) {
    System.out.println("OrderService 클래스" + orderService.getClass()); 
    System.out.println("=====" + this.getClass());
    String result = orderService.createOrder(itemId);
    System.out.println("=====" + this.getClass());
    return result;
  }
  
}
