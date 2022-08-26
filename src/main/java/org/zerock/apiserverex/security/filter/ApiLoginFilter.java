package org.zerock.apiserverex.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletInputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StreamUtils;
import org.zerock.apiserverex.dto.ApiSessionDTO;
import org.zerock.apiserverex.security.dto.ClubAuthMemberDTO;
import org.zerock.apiserverex.security.util.JWTUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

  private JWTUtil jwtUtil;

  public ApiLoginFilter(String defaultFilterProcessUrl, JWTUtil jwtUtil){
    super(defaultFilterProcessUrl);
    this.jwtUtil = jwtUtil;
  }
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {
    log.info("ApiLoginFilter........ attemptAuthentication");
    log.info("request.getRequestURI():"+request.getRequestURI());

    ServletInputStream inputStream = request.getInputStream();
    String msgBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    log.info("msgBody:: " + msgBody);
    JSONParser parser = new JSONParser();
    JSONObject jsonObject = null;
    try {
      jsonObject = (JSONObject) parser.parse(msgBody);
    } catch (Exception e) {e.printStackTrace();}

    String email = jsonObject.get("email").toString();
    String pw = jsonObject.get("pw").toString();
    log.info("email: " + email + "/pw: " + pw);
    // String pw = "1";
    // if(email == null) throw new BadCredentialsException("email cannot be null");
    UsernamePasswordAuthenticationToken authToken = 
        new UsernamePasswordAuthenticationToken(email, pw);
    
    return getAuthenticationManager().authenticate(authToken);
  }
  @Override 
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    log.info("successfulAuthentication... authResult:" + authResult.getPrincipal());
    // Jason Web Token publishing
    //인증이 되었으니까 세션을 통해서 email을 획득
    //Since you are authenticated, you get an email through the session.
    String email = ((ClubAuthMemberDTO) authResult.getPrincipal()).getEmail();
    String token = null;
    ObjectMapper mapper = new ObjectMapper();
    String curl = "";
    try {
      token = "Bearer " + jwtUtil.generateToken(email);
      ApiSessionDTO sessionDTO = clubAuthToSessionDTO((ClubAuthMemberDTO) authResult.getPrincipal(), token,curl);
      String res = mapper.writeValueAsString(sessionDTO);
      response.setContentType("application/json;charset=utf-8");
      // response.setCharacterEncoding("UTF-8");
      response.getOutputStream().write(res.getBytes());
      log.info("sessionDTO: " + sessionDTO.getName());
    } catch (Exception e) {e.printStackTrace();}
  }
  private ApiSessionDTO clubAuthToSessionDTO(ClubAuthMemberDTO dto, 
                                              String token, String curl) {
    log.info("dto.getName:" + dto.getName());
    ApiSessionDTO sessionDTO = ApiSessionDTO.builder()
    .email(dto.getEmail())
    .username(dto.getUsername())
    .name(dto.getName())
    .curl(curl)
    .fromSocial(dto.isFromSocial())
    .attr(dto.getAttr())
    .token(token)
    .build();
    return sessionDTO;
  }
}
