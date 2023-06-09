package com.example.oauth2jwt.global.security.oauth2.handler;


import com.example.oauth2jwt.global.security.jwt.service.JwtService;
import com.example.oauth2jwt.global.security.oauth2.CustomOAuth2User;
import com.example.oauth2jwt.user.entity.Role;
import com.example.oauth2jwt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("OAuth2 Login 성공!");
    try {
      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

      loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성

    } catch (Exception e) {
      throw e;
    }

  }

  private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User)
      throws IOException {
    String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
    String refreshToken = jwtService.createRefreshToken();
    response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
    response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
  }
}
