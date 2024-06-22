package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.domain.User;
import com.doramonz.aligonggoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userService")
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean existUser(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public User createUser(String id) {
        if(userRepository.existsById(id)){
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(User.builder().id(id).build());
    }
}
