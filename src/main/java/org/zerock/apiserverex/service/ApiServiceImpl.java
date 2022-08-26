package org.zerock.apiserverex.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.apiserverex.dto.ClubMemberDTO;
import org.zerock.apiserverex.dto.ClubMemberImageDTO;
import org.zerock.apiserverex.dto.PageRequestDTO;
import org.zerock.apiserverex.dto.PageResultDTO;
import org.zerock.apiserverex.entity.ClubMember;
import org.zerock.apiserverex.entity.ClubMemberImage;
import org.zerock.apiserverex.repository.ApiRepository;
import org.zerock.apiserverex.repository.ClubMemberImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {
  private final ApiRepository repository;
  private final ClubMemberImageRepository cmiRepository;
  private final PasswordEncoder encoder;
  
  @Override
  public void removeUuid(String uuid) {
    log.info("deleteImage...... uuid: "+uuid);
    cmiRepository.deleteByUuid(uuid);
  }

  @Override
  public ClubMemberDTO emailCheck(String email) {
    log.info("emailCheck... email: " + email);    
    ClubMemberDTO dto = null;
    Optional<ClubMember> result = repository.findByEmail(email);
    if(result.isPresent()) {
      ClubMember cm = result.get();
      dto = entityToDTO(cm);
    }
    return dto;
  }
  @Override
  public ClubMemberDTO getClubMemberDTO(String email) {
    ClubMemberDTO dto = entityToDTO(repository.getReferenceById(email));
    return dto;
  }

  @Transactional
  @Override
  public String register(ClubMemberDTO dto) {
    dto.setPassword(encoder.encode(dto.getPassword()));
    ClubMember cm = dtoToEntity(dto);
    repository.save(cm);
    
    List<ClubMemberImageDTO> imgList = dto.getImageDTOList();
    imgList.forEach(new Consumer<ClubMemberImageDTO>() {
      @Override
      public void accept(ClubMemberImageDTO imgDTO) {
        ClubMemberImage cmi = imageDtoToEntity(imgDTO,dto.getEmail());
        cmiRepository.save(cmi);
      }
    });
    return cm.getEmail();
  }

  @Override
  public List<ClubMemberDTO> getList() {
    List<ClubMember> result = repository.findAll();
    return result.stream().map(new Function<ClubMember,ClubMemberDTO>() {
      @Override
      public ClubMemberDTO apply(ClubMember t) {
        return entityToDTO(t);
      }
    }).collect(Collectors.toList());
  }

  @Override
  public PageResultDTO<ClubMemberDTO, ClubMember> getPageList(PageRequestDTO dto) {
    log.info("PageRequestDTO: " + dto);
    Pageable pageable = dto.getPageable(Sort.by("email"));
    Page<ClubMember> result = repository.getPageList(pageable);
    Function<ClubMember, ClubMemberDTO> fn = new Function<ClubMember,ClubMemberDTO>() {
      @Override
      public ClubMemberDTO apply(ClubMember t) {
        return entityToDTO(t);
      }
    };
    return new PageResultDTO<>(result, fn);
  }
}
