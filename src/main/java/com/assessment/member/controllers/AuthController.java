package com.assessment.member.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.assessment.member.security.JwtRequest;
import com.assessment.member.security.JwtResponse;
import com.assessment.member.security.JwtTokenUtil;
import com.assessment.member.security.JwtUserDetailsService;
import com.assessment.member.security.RefreshTokenRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
        );

        UserDetails userDetails = userDetailsService
				.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }
   
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (jwtTokenUtil.validateToken(refreshToken) && !jwtTokenUtil.isTokenExpired(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newToken = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(newToken));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    @RequestMapping({ "/hello" })
	public String hello() {
		return "Hello World";
	}
}