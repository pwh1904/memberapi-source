package com.assessment.member.controllers;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.assessment.member.entities.*;
import com.assessment.member.repositories.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
	
	@Autowired
    private MemberRepository memberRepository;
    
	@PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/all-members")
    public ResponseEntity<List<Member>>  showMembers() {
    	List<Member> members = new ArrayList<Member>();
    	memberRepository.findAll().forEach(members::add);
    	
    	return new ResponseEntity<>(members, HttpStatus.OK);
    }
    

    @PostMapping("/new-member")
    public ResponseEntity<?> addNewMember(@RequestBody Member member) {
    	
    	Map<String, Object> response = new HashMap<>();
        try {
	    	memberRepository.save(member);
	    	response.put("status", 200);
	        response.put("message", "New Member successfully added");
	    	return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
        	response.put("status", 400);
        	response.put("message", exception.getMessage());
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        	
        }
    }
    @PostMapping("/edit-member")
    public ResponseEntity<?> updateMember(@RequestBody Member member) {
    	Map<String, Object> response = new HashMap<>();
    
    	if(member.getId() != null) {
	    	Optional<Member> memberExist = memberRepository.findById(member.getId());
	    	if(memberExist.isPresent()) {
	    		try {
			    	memberRepository.save(member);
			    	response.put("status", 200);
			        response.put("message", "New Member successfully updated");
			    	return ResponseEntity.status(HttpStatus.OK).body(response);
		        } catch (Exception exception) {
		        	response.put("status", 400);
		        	response.put("message", exception.getMessage());
		        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		        	
		        }
	    	} else {
	    		response.put("status", 404);
	    		response.put("message", "Member id Not found");
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    	}
	
	        
    	} else {
    		response.put("status", 400);
    		response.put("message", "Please provide Member Id");
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}
    }
    @PostMapping("/delete-member")
    public ResponseEntity<?> deleteBook(@RequestBody Member memberToDel) {
    	
    	  Map<String, Object> response = new HashMap<>();
	      if(memberToDel.getId() != null) {
	    	Optional<Member> memberExist = memberRepository.findById(memberToDel.getId());
	    	if(memberExist.isPresent()) {
    		 try {
			        memberRepository.delete(memberExist.get());
			    	response.put("status", 200);
			        response.put("message", "Member successfully deleted");
			    	return ResponseEntity.status(HttpStatus.OK).body(response);
		    	 } catch (Exception exception) {
		         	response.put("status", 400);
		         	response.put("message", exception.getMessage());
		         	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		         	
		         }
	        	
	        } else {
	        	response.put("status", 404);
	         	response.put("message", "Member cannot be found.");
	         	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	    } else {
	    	response.put("status", 400);
    		response.put("message", "Please provide Member Id");
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	
	    }
    }
    @PostMapping("/find-member-byname")
    public ResponseEntity<List<Member>> findMember(@RequestBody Member memberToFind) {

    	List<Member> members = new ArrayList<Member>();
    	if(memberToFind.getName() == null ||  memberToFind.getName().isEmpty()) {
    		return new ResponseEntity<>(members,HttpStatus.BAD_REQUEST);
    	} else {
    		members = memberRepository.findByNameAndVip(memberToFind.getName(),"N");
         	
    	}
    	if(!members.isEmpty()) {
    		return new ResponseEntity<>(members, HttpStatus.OK);
    	}
		return null;
        
       
        
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/vip/find-member-byname")
    public ResponseEntity<List<Member>> findVipMember(@RequestBody Member memberToFind) {

    	List<Member> members = new ArrayList<Member>();
    	if(memberToFind.getName() != null &&  !(memberToFind.getName().isEmpty())) {
    			members = memberRepository.findByNameAndVip(memberToFind.getName(),"Y");
    	} else {
    		
         	return new ResponseEntity<>(members,HttpStatus.BAD_REQUEST);
    	}
    	if(!members.isEmpty()) {
    		return new ResponseEntity<>(members, HttpStatus.OK);
    	}
		return null;
        
       
        
    }
}
