package com.dkey.chat.dao;

import com.dkey.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
