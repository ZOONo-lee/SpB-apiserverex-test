package org.zerock.apiserverex.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.apiserverex.entity.ClubMember;

public interface ApiRepository extends JpaRepository<ClubMember, String> {
  @EntityGraph(attributePaths = { "roleSet" }, type = EntityGraphType.LOAD)
  @Query("select m from ClubMember m where m.email=:email")
  Optional<ClubMember> findByEmail(String email);

  @EntityGraph(attributePaths = { "roleSet" }, type = EntityGraphType.LOAD)
  @Query(
    "select m from ClubMember m where m.email=:email and m.fromSocial=:social "
  )
  Optional<ClubMember> findByEmail(String email, boolean social);

  @EntityGraph(attributePaths = { "roleSet" }, type = EntityGraphType.LOAD)
  @Query(
    value = "select c from ClubMember c ",
    countQuery = "select count(c) from ClubMember c"
  )
  Page<ClubMember> getPageList(Pageable pageable);
}
