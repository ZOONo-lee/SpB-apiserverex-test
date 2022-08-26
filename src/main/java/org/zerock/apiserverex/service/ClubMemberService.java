package org.zerock.apiserverex.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.zerock.apiserverex.dto.ClubMemberDTO;
import org.zerock.apiserverex.entity.ClubMember;
import org.zerock.apiserverex.entity.ClubMemberRole;

public interface ClubMemberService {
  String register(ClubMemberDTO dto);
  void modifyClubMember(ClubMemberDTO dto);
  List<ClubMemberDTO> getList();

  default ClubMember dtoToEntity(ClubMemberDTO dto){
    ClubMember clubMember = ClubMember.builder()
    .email(dto.getEmail())
    .name(dto.getName())
    .fromSocial(dto.isFromSocial())
    .roleSet(dto.getRoleSet().stream().map(new Function<String,ClubMemberRole>() {
      @Override
      public ClubMemberRole apply(String t) {
        if(t.equals("ROLE_USER")) return ClubMemberRole.USER;
        else if(t.equals("ROLE_MANAGER")) return ClubMemberRole.MANAGER;
        else if(t.equals("ROLE_ADMIN")) return ClubMemberRole.ADMIN;
        else return ClubMemberRole.USER;
      }
    }).collect(Collectors.toSet()))
    .build();
    return clubMember;
  }

  default ClubMemberDTO entityToDTO(ClubMember cm) {
    ClubMemberDTO dto = ClubMemberDTO.builder()
    .email(cm.getEmail())
    .name(cm.getName())
    .username(cm.getEmail())
    .fromSocial(cm.isFromSocial())
    .roleSet(cm.getRoleSet().stream().map(new Function<ClubMemberRole,String>() {
      @Override
      public String apply(ClubMemberRole t) {
        return new String("ROLE_"+t.name());
      }
    }).collect(Collectors.toSet()))
    .build();
    return dto;
  }
}
