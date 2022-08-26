package org.zerock.apiserverex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.apiserverex.dto.ClubMemberDTO;
import org.zerock.apiserverex.entity.ClubMember;
import org.zerock.apiserverex.repository.ClubMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {
  private final ClubMemberRepository repository;
  private final PasswordEncoder encoder;

  @Override
  public String register(ClubMemberDTO dto) {
    log.info("register dto: " + dto);
    Optional<ClubMember> result = repository.findByEmail(dto.getEmail());
    if(result.isPresent()) {
      return null;
    } else {
      repository.save(dtoToEntity(dto));
      return dto.getEmail();
    }
  }
  @Override
  public List<ClubMemberDTO> getList() {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public void modifyClubMember(ClubMemberDTO dto) {
    log.info("modifyClubMember... dto:" + dto);
    Optional<ClubMember> result = 
        repository.findByEmail(dto.getEmail(), dto.isFromSocial());
    if (result.isPresent()) {
      ClubMember cm = result.get();
      cm.changeName(dto.getName());
      cm.changePass(encoder.encode(dto.getPassword()));
      repository.save(cm);
    }
  }
}
