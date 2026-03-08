package com.skml.service;

import com.skml.entity.User;

public interface UserService {

	String signin(User user);

	String signup(User user);

}
