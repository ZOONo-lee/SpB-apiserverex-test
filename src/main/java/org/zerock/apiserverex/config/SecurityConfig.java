package org.zerock.apiserverex.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.apiserverex.security.filter.ApiCheckFilter;
import org.zerock.apiserverex.security.filter.ApiLoginFilter;
import org.zerock.apiserverex.security.handler.ApiLoginFailHandler;
import org.zerock.apiserverex.security.handler.ClubAccessDeniedHandler;
import org.zerock.apiserverex.security.handler.ClubLoginSuccessHandler;
import org.zerock.apiserverex.security.handler.ClubLogoutSuccessHandler;
import org.zerock.apiserverex.security.service.ClubUserDetailsService;
import org.zerock.apiserverex.security.util.JWTUtil;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private ClubUserDetailsService userDetailsService;

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // configure를 하기전에는 모든 것이 다 차단
  // 이것을 적용하는 순간 여기에 적용된 항목들만 security 적용됨
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    
    // http.authorizeRequests()
    //     .antMatchers("/sample/all").hasRole("USER")
    //     .antMatchers("/sample/member").hasRole("MANAGER")
    //     .antMatchers("/sample/admin").hasRole("ADMIN")
    //     .antMatchers("/member/modify").authenticated();

    // http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

    // http.formLogin().loginPage("/member/login")
    //     .loginProcessingUrl("/login").successForwardUrl("/");
    // http.formLogin().loginPage("/member/login").loginProcessingUrl("/login").successHandler(successHandler());

    // 로그아웃 주소와 로그아웃 후에 페이지 지정
    // Specify logout address and page after logout
    // http.logout().logoutUrl("/logout").logoutSuccessUrl("/member/logout");
    // 로그아웃 주소와 로그아웃에 대한 분기
    // Logout address and branch to logout
    // http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler());

    // http://localhost:8080/club/login/oauth2/code/google
    // http.oauth2Login().successHandler(successHandler());//by handler
    // http.oauth2Login().defaultSuccessUrl("/"); //forwarding

    http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);

    http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
    //apiLoginFilter를 활용할 때 기본 security를 적용하겠다.
    //Basic security will be applied when using apiLoginFilter.
    http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public ClubAccessDeniedHandler accessDeniedHandler(){
    return new ClubAccessDeniedHandler();
  }

  @Bean
  public ClubLoginSuccessHandler successHandler() {
    return new ClubLoginSuccessHandler(passwordEncoder());
  }

  @Bean
  public ClubLogoutSuccessHandler logoutSuccessHandler() {
    return new ClubLogoutSuccessHandler();
  }

  @Bean
  public ApiCheckFilter apiCheckFilter(){
    return new ApiCheckFilter("/api/**/*", jwtUtil());
  }

  @Bean
  public ApiLoginFilter apiLoginFilter() throws Exception {
    ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/members/login", jwtUtil());
    apiLoginFilter.setAuthenticationManager(authenticationManager());
    apiLoginFilter.setAuthenticationSuccessHandler(successHandler());
    apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
    return apiLoginFilter;
  }

  @Bean
  public JWTUtil jwtUtil(){
    return new JWTUtil();
  }
}
