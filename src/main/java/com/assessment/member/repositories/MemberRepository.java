package com.assessment.member.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.assessment.member.entities.Member;



@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
	
	List<Member> findByNameAndVip(String name,String vip);
	

}
