package org.zerock.apiserverex.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubMemberDTO {
  private String username;
  private String email;
  private String password;
  private String name;
  private boolean fromSocial;

  @Builder.Default
  private List<ClubMemberImageDTO> imageDTOList = new ArrayList<>();

  private LocalDateTime regDate;
  private LocalDateTime modDate;

  @Builder.Default
  private Set<String> roleSet = new HashSet<>();
}
