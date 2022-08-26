package org.zerock.apiserverex.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.apiserverex.dto.ClubMemberDTO;
import org.zerock.apiserverex.dto.PageRequestDTO;
import org.zerock.apiserverex.dto.PageResultDTO;
import org.zerock.apiserverex.entity.ClubMember;
import org.zerock.apiserverex.service.ApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
  
  public final ApiService service;

  @RequestMapping(
    value = "/getAuth", method = RequestMethod.POST, 
    consumes = MediaType.ALL_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> getAuth(
    @RequestBody Map<String, Object> mapObj, 
    @RequestHeader("token") String token)
  {
    String email = mapObj.get("email").toString();
    // log.info("/getAuth... email:" + email);
    Map<String, Object> map = new HashMap<>();
    map.put("dto", service.getClubMemberDTO(email));
    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
  }

  @RequestMapping(value = "/emailCheck", method = RequestMethod.POST, 
  consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Long>> emailCheck(
                              @RequestBody Map<String, Object> mapObj,
                              @RequestHeader("token") String token) {
    String email = mapObj.get("email").toString();
    ClubMemberDTO dto = service.emailCheck(email);

    Map<String, Long> mapForResult = new HashMap<>();
    mapForResult.put("result", (dto == null)?0L:1L);
    
    return new ResponseEntity<Map<String, Long>>(mapForResult, HttpStatus.OK);
  }

  @RequestMapping(value = "/memberRegister", method = RequestMethod.POST, 
  consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> register(@RequestBody ClubMemberDTO dto,
                                         @RequestHeader("token") String token) {
    log.info("api/memberRegister...ClubMemberDTO:" + dto);
    String email = service.register(dto);
    return new ResponseEntity<>(email, HttpStatus.OK);
  }

  @RequestMapping(value = "/getlist", method = RequestMethod.POST, 
  consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ClubMemberDTO>> getList(
    @RequestHeader("token") String token) {
      List<ClubMemberDTO> result = service.getList();
      log.info(result);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
  @RequestMapping(value = "/get-page-list", method = RequestMethod.POST, 
  consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageResultDTO<ClubMemberDTO,ClubMember>> getPagingList(
    @RequestBody PageRequestDTO dto,
    @RequestHeader("token") String token) {
      log.info("PageRequestDTO page: " + dto.getPage());
    return new ResponseEntity<>(service.getPageList(dto), HttpStatus.OK);
  }
}
