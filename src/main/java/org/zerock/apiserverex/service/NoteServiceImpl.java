package org.zerock.apiserverex.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.zerock.apiserverex.dto.NoteDTO;
import org.zerock.apiserverex.entity.Note;
import org.zerock.apiserverex.repository.NoteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
  private final NoteRepository repository;

  @Override
  public Long register(NoteDTO dto) {
    Note note = dtoToEntity(dto);
    log.info("register... note: " + note);
    repository.save(note);
    return note.getNum();
  }

  @Override
  public NoteDTO get(Long num) {
    Optional<Note> result = repository.getWithWriter(num);
    if (result.isPresent()) {
      return entityToDTO(result.get());
    }
    return null;
  }

  @Override
  public void modify(NoteDTO dto) {
    Long num = dto.getNum();
    Optional<Note> result = repository.findById(num);
    if(result.isPresent()){
      Note note = result.get();
      note.changeTitle(dto.getTitle());
      note.changeContent(dto.getContent());
      repository.save(note);
    }
  }

  @Override
  public void remove(Long num) {
    log.info("remove... num: "+num);
    repository.deleteById(num);
  }

  @Override
  public List<NoteDTO> getAllWithWriter(String writerEmail) {
    List<Note> list = repository.getList(writerEmail);
    return list.stream().map(new Function<Note,NoteDTO>() {
      @Override
      public NoteDTO apply(Note t) {
        return entityToDTO(t);
      }
    }).collect(Collectors.toList());
  }
}
