package com.hca.oauth.service.impl;

import com.hca.oauth.dao.UserRepository;
import com.hca.oauth.model.User;
import com.hca.oauth.service.UserService;
import com.hca.oauth.util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Override
	public String signUp(User user) {
		User dbUser = userRepository.findByEmail(user.getEmail());
		if (dbUser != null) {
			throw new RuntimeException("User already exist.");
		}
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return jwtTokenUtil.generateToken(user);
	}

	@Override
	public String login(User user) {
		User dbUser = userRepository.findByEmail(user.getEmail());
		if (dbUser == null) {
			throw new RuntimeException("User does not exist.");
		}

		return jwtTokenUtil.generateToken(user);
	}
	
	
	@Override
	public User fetchPassword(String email) {
		User dbUser = userRepository.findByEmail(email);
		return dbUser ;
	}

	@Override
	public void updateUser(User curpwd) {
		// TODO Auto-generated method stub
		userRepository.save(curpwd);
	}

	@Override
	public User findDuplicateByEmail(String email) {
		// TODO Auto-generated method stub
		User dbUser = userRepository.findByEmail(email);
		if (dbUser != null) {
			throw new RuntimeException("User Already Registered.");
		}
		return null;
	}
}
