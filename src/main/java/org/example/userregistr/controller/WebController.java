package org.example.userregistr.controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/private")
    public String getPrivatePage(Authentication authentication) {
        return "private Page, Welcome : " + authentication.getName() + authentication.getAuthorities();
    }


    @GetMapping
    public String homePage() {
        return "Home Page";
    }

    @GetMapping("/robot")
    public String getRobotPage(Authentication authentication) {
        return "robot Page: " + authentication.getAuthorities();
    }
}
