package org.example.auth_test.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    @GetMapping("/hello")
    public String hello(OAuth2AuthorizationContext auth2AuthorizationContext) {
        return "hello";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
