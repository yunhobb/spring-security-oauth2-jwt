package com.example.oauth2jwt.user.controller;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
public class AuthController {

  @GetMapping("/oauth2/authorization/kakao")
  public String kakaoLogin(String code) {
    return code;
  }

  @GetMapping("/oauth2/authorization/google")
  public String GoogleLogin(String code) {
    return code;
  }
}
