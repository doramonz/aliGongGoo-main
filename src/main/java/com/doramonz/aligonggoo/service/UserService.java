package com.doramonz.aligonggoo.service;

import com.doramonz.aligonggoo.domain.User;

public interface UserService {
    boolean existUser(String id);

    User createUser(String id);
}
