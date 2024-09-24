package org.example.auth_test.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth_test.models.dto.RegisterUserDTO;
import org.example.auth_test.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final IUserService userService;

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        RegisterUserDTO userDTO = new RegisterUserDTO();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/register");
        modelAndView.addObject("user", userDTO);

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView processRegisterUser(@ModelAttribute("user") @Valid RegisterUserDTO userDTO,
                                            Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return new ModelAndView("register");
        }

        userService.registerUser(request, userDTO);

        return new ModelAndView("redirect:/hello");
    }
}
