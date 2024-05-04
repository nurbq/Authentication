package org.example.userregistr.config.csrf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {

    @GetMapping("manager")
    public String getManagerPage() {
        return "manager";
    }
}
