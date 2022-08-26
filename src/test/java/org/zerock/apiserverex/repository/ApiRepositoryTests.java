package org.zerock.apiserverex.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.apiserverex.dto.ClubMemberDTO;
import org.zerock.apiserverex.entity.ClubMember;

@SpringBootTest

public class ApiRepositoryTests {
  @Autowired
  ApiRepository repository;
  
  @Test
  void testGetPageList() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("email").descending());
    Page<ClubMember> result = repository.getPageList(pageable);
    result.get().forEach(row -> {
      System.out.println(row);
    });
  }
}
