package org.zerock.apiserverex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.zerock.apiserverex.entity.ClubMember;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {
  //In case of LOAD,
  //EntityGraph의 attributePaths에 적용된 속성은 EAGER 처리 나머지는 Entity Fetch를 따름
  //Attributes applied to attributePaths of EntityGraph are processed as EAGER, 
  //the rest follow Entity Fetch
  @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraphType.LOAD)
  @Query("select m from ClubMember m where m.email=:email and m.fromSocial=:social ")
  Optional<ClubMember> findByEmail(String email, boolean social);
 
  @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraphType.LOAD)
  @Query("select m from ClubMember m where m.email=:email ")
  Optional<ClubMember> findByEmail(String email);
}
