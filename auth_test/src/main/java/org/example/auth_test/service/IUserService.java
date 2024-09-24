package org.example.auth_test.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.auth_test.models.dto.RegisterUserDTO;

public interface IUserService {
    public boolean registerUser(HttpServletRequest request, RegisterUserDTO registerUserDTO);
}
