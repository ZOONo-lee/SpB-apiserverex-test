package org.zerock.apiserverex.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity {
  @Id
  private String email;

  private String password;
  private String name;
  private boolean fromSocial;

  //jpa에서 연결되는 객체가 collection일 경우 Id와 collection의 element위한 테이블에 추가
  //When the object connected in jpa is a collection
  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private Set<ClubMemberRole> roleSet = new HashSet<>();

  public void addMemberRole(ClubMemberRole role){
    roleSet.add(role);
  }
  public void changeName(String name){this.name = name;}
  public void changePass(String pass){password = pass;}
}
