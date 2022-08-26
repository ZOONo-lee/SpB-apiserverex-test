package org.zerock.apiserverex.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.apiserverex.entity.ClubMemberImage;

public interface ClubMemberImageRepository extends JpaRepository<ClubMemberImage, Long> {

  @Modifying
  @Query("delete from ClubMemberImage cmi where cmi.clubMember.email=:email")
  void deleteByEmail(String email);

  @Transactional
  @Modifying
  @Query("delete from ClubMemberImage cmi where cmi.uuid=:uuid")
  void deleteByUuid(String uuid);

  @Query("select cmi from ClubMemberImage cmi where cmi.clubMember.email=:email")
  List<ClubMemberImage> findByEmail(String email);
}
