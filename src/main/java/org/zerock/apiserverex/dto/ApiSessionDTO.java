package org.zerock.apiserverex.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ApiSessionDTO {
  private String email;
  private String username;
  private String name;
  private String token;
  private String curl;
  private boolean fromSocial;
  private Map<String, Object> attr; //OAuth from Social
}
