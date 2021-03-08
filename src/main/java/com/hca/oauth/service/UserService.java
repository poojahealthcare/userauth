package com.hca.oauth.service;

import com.hca.oauth.model.User;

public interface UserService {
    String signUp(User user);
    String login(User user);
	User fetchPassword(String email); 
	void updateUser(User curpwd);
	User findDuplicateByEmail(String email);
}
