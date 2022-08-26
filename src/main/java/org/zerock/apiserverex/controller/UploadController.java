package org.zerock.apiserverex.controller;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.apiserverex.dto.UploadResultDTO;
import org.zerock.apiserverex.service.ApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UploadController {
  
  private final ApiService service;
  
  @Value("${org.zerock.upload.path}")
  private String uploadPath;

  @PostMapping("/api/removeFile")
  public ResponseEntity<Boolean> removeFile(String fileName, String uuid){
    String srcFileName = null;
    boolean result = false;
    log.info(uuid);
    if(!(uuid==null || uuid.equals(""))){
      service.removeUuid(uuid);
    }

    try {
      log.info("removeFile............"+fileName);
      srcFileName = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + srcFileName);
      result = file.delete();
      File thumb = new File(file.getParent(),"s_"+file.getName());
      result = thumb.delete();
      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      log.info("remove file : "+e.getMessage());
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @GetMapping("/display")
  public ResponseEntity<byte[]> getFile(String fileName, String size){
    log.info("display.....................");
    ResponseEntity<byte[]> result = null;
    try {
      String srchFileName = URLDecoder.decode(fileName, "UTF-8");
      log.info("display fileName: "+srchFileName);
      File file = new File(uploadPath+File.separator+srchFileName);
      if(size != null && size.equals("1")){
        file = new File(file.getParent(), file.getName().substring(2));
      }

      HttpHeaders header = new HttpHeaders();
      header.add("Content-Type", Files.probeContentType(file.toPath())); //MIME
      result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return result;
  }

  @PostMapping("/api/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){
    List<UploadResultDTO> result = new ArrayList<>();

    for (MultipartFile uploadFile : uploadFiles) {
      if(uploadFile.getContentType().startsWith("image") == false){
        log.warn("this is not image type");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }

      String orig = uploadFile.getOriginalFilename();
      String fileName = orig.substring(orig.lastIndexOf("\\")+1);
      log.info("Original File Name: " + orig);
      log.info("File Name: " + fileName);

      String folderPath = makeFolder();

      String uuid = UUID.randomUUID().toString();
      String saveName = uploadPath + File.separator + folderPath + File.separator
                          + uuid + "_" + fileName;
      log.info(saveName);
      Path savePath = Paths.get(saveName);
      try {
        uploadFile.transferTo(savePath);
        String thumbnailSaveName = 
                  uploadPath + File.separator + folderPath + File.separator
                  + "s_" + uuid + "_" + fileName;
        File thumbnailFile = new File(thumbnailSaveName);
        Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,100,100);

        result.add(new UploadResultDTO(fileName, uuid, folderPath));
      } catch (Exception e) {e.printStackTrace();}
  }
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  private String makeFolder() {
    String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String folderPath = str.replace("/", File.separator);
    File uploadPathFolder = new File(uploadPath, folderPath);
    if(uploadPathFolder.exists() == false){
      uploadPathFolder.mkdirs();
    }
    return folderPath;
  }
}
