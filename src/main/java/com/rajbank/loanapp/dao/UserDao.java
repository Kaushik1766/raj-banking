package com.rajbank.loanapp.dao;

import com.rajbank.loanapp.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findByUsername(String username);

    User save(User user);

    long count();
}
