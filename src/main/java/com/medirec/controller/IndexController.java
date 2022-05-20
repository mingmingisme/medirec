package com.medirec.controller;

import com.medirec.utils.SessionUtils;
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
        if (SessionUtils.getLoginUserFromSession() == null) {
            return "login";
        } else {
            return "userhome";
        }

    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }
}
