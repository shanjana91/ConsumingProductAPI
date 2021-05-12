package com.example.UserRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByusername(String username); 
}
