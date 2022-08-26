package org.zerock.apiserverex.dto;

import java.io.Serializable;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {
  private String fileName;
  private String uuid;
  private String folderPath;

  //file의 전체 경로를 알기위한 메서드,Method to get the full path of a file
  public String getImageURL(){
    try {
      return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName, "UTF-8");
    } catch (Exception e) {e.printStackTrace();}
    return "";
  }
  //file의 전체 경로를 알기위한 메서드,Method to get the full path of a file
  public String getThumbnailURL(){
    try {
      return URLEncoder.encode(folderPath+"/s_"+uuid+"_"+fileName, "UTF-8");
    } catch (Exception e) {e.printStackTrace();}
    return "";
  }
}
