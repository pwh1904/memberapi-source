package com.assessment.member.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.member.entities.User;
import com.assessment.member.repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
    private UserRepository userRepository;
	
    @GetMapping("/users")
    public ResponseEntity<List<User>>  showUsers() {
    	List<User> users = new ArrayList<User>();
    	userRepository.findAll().forEach(users::add);
    	
    	return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
