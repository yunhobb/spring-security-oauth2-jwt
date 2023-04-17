package com.example.oauth2jwt.global.config;


import com.example.oauth2jwt.global.security.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.oauth2jwt.global.security.jwt.service.JwtService;
import com.example.oauth2jwt.global.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.oauth2jwt.global.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.oauth2jwt.global.security.oauth2.service.CustomOAuth2UserService;
import com.example.oauth2jwt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin().disable() // FormLogin 사용 X
        .httpBasic().disable() // httpBasic 사용 X
        .csrf().disable() // csrf 보안 사용 X
        .headers().frameOptions().disable()
        .and()

        // 세션 사용하지 않으므로 STATELESS로 설정
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()

        //== URL별 권한 관리 옵션 ==//
        .authorizeRequests()

        // 아이콘, css, js 관련
        // 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능, h2-console에 접근 가능
        .antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
        .anyRequest().authenticated() // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능f
        .and()
        //== 소셜 로그인 설정 ==//
        .oauth2Login()
        .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
        .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
        .userInfoEndpoint().userService(customOAuth2UserService); // customUserService 설정

    // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
    http.addFilter(jwtAuthenticationProcessingFilter());
    return http.build();
  }


  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
    return jwtAuthenticationFilter;
  }
}
