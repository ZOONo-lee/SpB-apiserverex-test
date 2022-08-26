package org.zerock.apiserverex.service;

import java.util.List;

import org.zerock.apiserverex.dto.NoteDTO;
import org.zerock.apiserverex.entity.ClubMember;
import org.zerock.apiserverex.entity.Note;

public interface NoteService {
  Long register(NoteDTO dto);
  NoteDTO get(Long num);
  void modify(NoteDTO dto);
  void remove(Long num);
  List<NoteDTO> getAllWithWriter(String writerEmail);

  default Note dtoToEntity(NoteDTO dto){
    Note note = Note.builder()
      .num(dto.getNum())
      .title(dto.getTitle())
      .content(dto.getContent())
      .writer(ClubMember.builder().email(dto.getWriterEmail()).build())
      .build();
    return note;
  }

  default NoteDTO entityToDTO(Note note){
    NoteDTO dto = NoteDTO.builder()
    .num(note.getNum())
    .title(note.getTitle())
    .content(note.getContent())
    .writerEmail(note.getWriter().getEmail())
    .regDate(note.getRegDate())
    .modDate(note.getModDate())
    .build();
    return dto;
  }
}
