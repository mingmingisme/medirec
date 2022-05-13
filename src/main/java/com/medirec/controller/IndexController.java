package com.medirec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zsm
 */
@Controller
public class IndexController {
    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }
}
