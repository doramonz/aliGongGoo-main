package com.doramonz.aligonggoo.repository;

import com.doramonz.aligonggoo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
}
