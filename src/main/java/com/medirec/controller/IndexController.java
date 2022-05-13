package com.medirec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zsm
 */
@Controller
public class IndexController {
    @RequestMapping("/test")
    public String index() {
        return "index";
    }
}
