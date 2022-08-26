package org.zerock.apiserverex.security.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.apiserverex.entity.ClubMember;
import org.zerock.apiserverex.entity.ClubMemberRole;
import org.zerock.apiserverex.repository.ClubMemberRepository;
import org.zerock.apiserverex.security.dto.ClubAuthMemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
//Open Auth를 Social로 로그인하기 위한 객체
//Object for logging in with Open Auth as Social
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

  private final ClubMemberRepository repository;
  private final PasswordEncoder encoder;

  @Override
  //사용자가 username, password를 이용해서 정상적 로그인하고 해당정보를 social에서 받는객체
  //An object that a user normally logs in using username and password 
  //and receives the information from social
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("loadUser...... userRequest: " + userRequest);
    String clientName = userRequest.getClientRegistration().getClientName();
    log.info("clientName: " + clientName);
    log.info(userRequest.getAdditionalParameters()); // 구글로 부터 오는 정보를 확인
    OAuth2User oAuth2User = super.loadUser(userRequest);//세션 획득
    oAuth2User.getAttributes().forEach((k, v) -> {
      log.info(k + ":" + v);
    });

    String email = null;
    if (clientName.equals("Google")) {
      email = oAuth2User.getAttribute("email");
    }
    log.info("email: " + email);
    // ClubMember member = saveSocialMember(email);
    // return oAuth2User;
    ClubMember member = saveSocialMember(email);
    ClubAuthMemberDTO dto = new ClubAuthMemberDTO(member.getEmail(),
      member.getPassword(), true, member.getRoleSet().stream().map(
        role -> new SimpleGrantedAuthority("ROLE_" + role.name())
      ).collect(Collectors.toList()), oAuth2User.getAttributes());
    dto.setName(member.getName());
    return dto;
  }

  private ClubMember saveSocialMember(String email) {
    Optional<ClubMember> result = repository.findByEmail(email, true);
    if (result.isPresent()) return result.get();

    ClubMember cm = ClubMember.builder().email(email).name(email)
        .password(encoder.encode("1")).fromSocial(true).build();
    cm.addMemberRole(ClubMemberRole.USER);
    repository.save(cm);
    return cm;
  }
}
