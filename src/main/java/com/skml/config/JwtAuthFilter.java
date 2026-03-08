package com.skml.config;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skml.entity.User;
import com.skml.repository.UserRepo;
import com.skml.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthFilter extends OncePerRequestFilter{
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired 
	private UserRepo userRepo;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader=request.getHeader("Authorization");
		
		if(authHeader!=null && authHeader.startsWith("Bearer "))
		{
			String token=authHeader.substring(7);
			if (!JwtUtil.isTokenValid(token)) {
			    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    return;
			}

			String username=jwtUtil.extractUsername(token);
			User user=userRepo.findByUsername(username).orElse(null);
			if(user!=null)
			{
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request,response);
		
	}
}
