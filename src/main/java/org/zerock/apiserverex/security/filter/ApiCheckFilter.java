package org.zerock.apiserverex.security.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.apiserverex.security.util.JWTUtil;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
  private AntPathMatcher antPathMatcher;
  private String pattern; 
  private JWTUtil jwtUtil;

  public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
    antPathMatcher = new AntPathMatcher();
    this.pattern = pattern;
    this.jwtUtil = jwtUtil;
  }

  //Authorization헤더의 값을 파악해서 사용자가 정상적인 요청을 파악
  //By understanding the value of the Authorization header, 
  //the user understands a normal request.
  @Override 
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    //request는 서버가 클라이언트로부터 요청 정보를 알아냄:: 이게 뭐죠?
    //request is where the server retrieves request information from the client:: What is this?
    
    // log.info("Request URI: " + request.getRequestURI());
    // log.info(">>"+antPathMatcher.match(request.getContextPath()+pattern, request.getRequestURI()));
    
    //필터링 패턴과 사용자의 요청 URI가 같은지 비교
    //Compare filtering pattern and user's request URI for equality
    if(antPathMatcher.match(request.getContextPath()+pattern, request.getRequestURI())){
      log.info("API check filter.......................");
      boolean checkHeader = checkAuthHeader(request);
      log.info("checkHeader: " + checkHeader);
      if(checkHeader){
        filterChain.doFilter(request, response);
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        String message = "FAIL CHECK API TOKEN";
        JSONObject json = new JSONObject();
        json.put("code", "403");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
      }
      return;
    }
    filterChain.doFilter(request, response);//filtering 후에 다음 필터로 넘어가게함.
  }

  //http header의 Authorization의 값을 확인
  private boolean checkAuthHeader(HttpServletRequest request) {
    boolean checkResult = false;
    String authHeader = request.getHeader("Authorization");
    log.info("Authorization: " + authHeader);
    if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
      log.info("Authorization exist: " + authHeader);
      try {
        String email = jwtUtil.validateAndExtract(authHeader.substring(7));
        log.info(("validate result: " + email));
        checkResult = email.length() > 0;
      } catch (Exception e) {
        e.printStackTrace();
      }
      
    }
    return checkResult;
  }
}
