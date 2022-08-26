package org.zerock.apiserverex.security.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// 로그인 후 로그인 정보를 가지고 세션을 만들기 위한 객체
// Object for creating a session with login information after login
public class ClubAuthMemberDTO extends User implements OAuth2User {
  private String email;
  private String password;
  private String name;
  private boolean fromSocial;
  private Map<String, Object> attr; // Google에서 받은 정보 담는 곳

  public ClubAuthMemberDTO(String username, String password,
      boolean fromSocial, Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attr) {
    this(username, password, fromSocial, authorities);
    this.attr = attr;
  }

  public ClubAuthMemberDTO(
      String username, String password, boolean fromSocial,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    email = username; // ★ security와 사용자가 생성한 DB의 계정을 매칭
    this.password = password;
    this.fromSocial = fromSocial;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attr;
  }
}
