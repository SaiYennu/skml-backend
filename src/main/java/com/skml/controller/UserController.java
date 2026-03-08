package com.skml.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skml.entity.User;
import com.skml.service.EmailService;
import com.skml.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController 
{

	@Autowired
	private UserService userService;

   
	@PostMapping("/signin")
	public String signin(@RequestBody User user)
	{
		return userService.signin(user);
	}
	
	@PostMapping("/signup/admin")
	public String signup(@RequestBody User user) 
	{
		return userService.signup(user);
	}
	
	
}
