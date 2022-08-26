package org.zerock.apiserverex.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.zerock.apiserverex.dto.ClubMemberDTO;
import org.zerock.apiserverex.dto.ClubMemberImageDTO;
import org.zerock.apiserverex.dto.PageRequestDTO;
import org.zerock.apiserverex.dto.PageResultDTO;
import org.zerock.apiserverex.entity.ClubMember;
import org.zerock.apiserverex.entity.ClubMemberImage;
import org.zerock.apiserverex.entity.ClubMemberRole;

public interface ApiService {
  ClubMemberDTO getClubMemberDTO(String email);
  ClubMemberDTO emailCheck(String email);
  String register(ClubMemberDTO dto);
  List<ClubMemberDTO> getList();
  PageResultDTO<ClubMemberDTO, ClubMember> getPageList(PageRequestDTO dto);
  void removeUuid(String uuid);
 
  default ClubMemberDTO entityToDTO(ClubMember cm){
    ClubMemberDTO cmDTO = ClubMemberDTO.builder()
                          .username(cm.getEmail())
                          .email(cm.getEmail())
                          .name(cm.getName())
                          .fromSocial(cm.isFromSocial())
                          .roleSet(cm.getRoleSet().stream().map(
                            role -> new String("ROLE_"+role.name()))
                            .collect(Collectors.toSet()))
                          .regDate(cm.getRegDate())
                          .modDate(cm.getModDate())
                          .build();
    return cmDTO;                      
  }
  default ClubMember dtoToEntity(ClubMemberDTO dto) {
    ClubMember cm = ClubMember.builder()
            .email(dto.getEmail())
            .password(dto.getPassword())
            .name(dto.getName())
            .fromSocial(dto.isFromSocial())
            .roleSet(dto.getRoleSet().stream().map(
              t -> {
                if(t.equals("ROLE_USER"))  return ClubMemberRole.USER;
                else if(t.equals("ROLE_MANAGER")) return ClubMemberRole.MANAGER;
                else if(t.equals("ROLE_ADMIN")) return ClubMemberRole.ADMIN;
                else return ClubMemberRole.USER;
              }).collect(Collectors.toSet()))
            .build();
    return cm;
  }
  default ClubMemberImage imageDtoToEntity(ClubMemberImageDTO dto, String email) {
    ClubMemberImage cmi = ClubMemberImage.builder()
    .uuid(dto.getUuid())
    .path(dto.getPath())
    .imgName(dto.getImgName())
    .clubMember(ClubMember.builder().email(email).build())
    .build();
    return cmi;
  }
}
