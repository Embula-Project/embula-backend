package com.embula.embula_backend.services;

import org.springframework.stereotype.Repository;

@Repository
public interface UserService {
    public String setUserInactive(String userId);
}
