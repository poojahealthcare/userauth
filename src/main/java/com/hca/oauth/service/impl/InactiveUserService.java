package com.hca.oauth.service.impl;

import com.hca.oauth.dao.InactiveUserRepository;
import com.hca.oauth.dao.UserRepository;
import com.hca.oauth.model.InactiveUser;
import com.hca.oauth.model.User;
import com.hca.oauth.service.UserService;
import com.hca.oauth.util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InactiveUserService{

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private InactiveUserRepository inactiveUserRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	 
	public InactiveUser findById(String code) {
		return inactiveUserRepository.findByCode(code);
	     
	}
	
	public boolean saveUser(InactiveUser inactiveUser) {
		inactiveUser=inactiveUserRepository.save(inactiveUser);
		if(inactiveUser==null)
	    	 return false;
	     else
	    	 return true;
	}

	public void findDuplicateByEmail(String email) {
		// TODO Auto-generated method stub
		InactiveUser inactiveUser=inactiveUserRepository.findByEmail(email);
		if(inactiveUser!=null)
			throw new RuntimeException("User is registered. To activate please check registered email.");
	}

	public void removeUserByEmail(String email) {
		 
		// TODO Auto-generated method stub
		InactiveUser inactiveUser=inactiveUserRepository.findByEmail(email);
		 inactiveUserRepository.deleteById(inactiveUser.getId());
	}

	 
}
