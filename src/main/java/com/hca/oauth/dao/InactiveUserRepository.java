package com.hca.oauth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hca.oauth.model.InactiveUser;
import com.hca.oauth.model.User;

@Repository
public interface InactiveUserRepository extends JpaRepository<InactiveUser, Integer> {

	InactiveUser findByCode(String code);

	InactiveUser findByEmail(String email);

	 
}
