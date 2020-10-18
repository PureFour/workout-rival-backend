package com.ruczajsoftware.workoutrival.repositories;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ruczajsoftware.workoutrival.model.database.User;

import java.util.Optional;

public interface UserRepository extends ArangoRepository<User, Integer> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	void deleteUserByUsername(String username);
	void deleteUserByEmail(String email);
}
