package org.zerock.apiserverex.dto;

import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMemberImageDTO {
  private String uuid;
  private String imgName;
  private String path;

  public String getImageURL(){
    try {
      return URLEncoder.encode(path+"/"+uuid+"_"+imgName, "UTF-8");
    } catch (Exception e) {e.printStackTrace();}
    return "";
  }
  //file의 전체 경로를 알기위한 메서드,Method to get the full path of a file
  public String getThumbnailURL(){
    try {
      return URLEncoder.encode(path+"/s_"+uuid+"_"+imgName, "UTF-8");
    } catch (Exception e) {e.printStackTrace();}
    return "";
  }


}
