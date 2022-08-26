package org.zerock.apiserverex.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.apiserverex.dto.ClubMemberDTO;
import org.zerock.apiserverex.dto.NoteDTO;
import org.zerock.apiserverex.service.ClubMemberService;
import org.zerock.apiserverex.service.NoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/commonmembers")
@Log4j2
@RequiredArgsConstructor
public class ClubMemberController {
  private final NoteService service;
  private final ClubMemberService cmService;

  @PostMapping(value = "")
  public ResponseEntity<String> register(@RequestBody ClubMemberDTO dto){
    log.info("register... dto: " + dto);
    String email = cmService.register(dto);
    return new ResponseEntity<>(email, HttpStatus.OK);
  }

  @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NoteDTO> read(@PathVariable("num") Long num){
    log.info("read......... num: " + num);
    return new ResponseEntity<>(service.get(num), HttpStatus.OK);
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NoteDTO>> getList(String email){
    log.info("getList...  email: " + email);
    return new ResponseEntity<>(service.getAllWithWriter(email),HttpStatus.OK);
  }

  @DeleteMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> remove(@PathVariable("num") Long num) {
    log.info("remove... num: " + num);
    service.remove(num);
    return new ResponseEntity<>("removed", HttpStatus.OK);
  }

  @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> modify(@RequestBody NoteDTO dto) {
    log.info("moidfy... dto: " + dto);
    service.modify(dto);
    return new ResponseEntity<>("modified", HttpStatus.OK);
  }
}
