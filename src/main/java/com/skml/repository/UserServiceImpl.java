package com.skml.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skml.entity.User;
import com.skml.service.UserService;
import com.skml.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	
	@Override
	public String signin(User user) {
		User dbuser=userRepo.findByUsername(user.getUsername()).orElseThrow(()->new RuntimeException("user not found"));
		if(!passwordEncoder.matches(user.getPassword(), dbuser.getPassword()))
		{
			throw new RuntimeException("Invalid Password");
		}
		return jwtUtil.generateToken(user.getUsername());
	}

	@Override
	public String signup(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);
		return "User Register Succcessfull";
	}

}
