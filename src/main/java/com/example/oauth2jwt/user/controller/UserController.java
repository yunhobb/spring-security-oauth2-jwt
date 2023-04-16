package com.example.oauth2jwt.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

//  private final UserService userService;




  @GetMapping("/jwt-test")
  public String jwtTest() {
    return "jwtTest 요청 성공";
  }
}