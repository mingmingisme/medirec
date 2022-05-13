package com.medirec.controller;

import com.medirec.utils.SessionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.medirec.utils.JsonResponse;
import com.medirec.service.UserService;
import com.medirec.entity.User;


/**
 * UserController类
 *
 * @author zsm
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public JsonResponse login(User user) {
        User loginUser = userService.login(user);
        if (loginUser != null) {
            SessionUtils.saveLoginUserToSession(loginUser);
        }
        return JsonResponse.success(loginUser);
    }

    @PostMapping("/register")
    @ResponseBody
    public JsonResponse register(User user) {
        User registerUser = userService.register(user);
        return JsonResponse.success(registerUser);
    }

    @RequestMapping("/home")
    public String redirectToUserHome() {
        return "userhome";
    }


    /**
    * 描述：根据Id 查询
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id)throws Exception {
        User  user =  userService.getById(id);
        return JsonResponse.success(user);
    }

    /**
    * 描述：根据Id删除
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        userService.removeById(id);
        return JsonResponse.success(null);
    }

    /**
    * 描述：根据Id更新
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonResponse updateUser(@PathVariable("id") Long  id,User  user) throws Exception {
        user.setId(id);
        userService.updateById(user);
        return JsonResponse.success(null);
    }

    /**
    * 描述:创建User
    */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(User  user) throws Exception {
        userService.save(user);
        return JsonResponse.success(null);
    }
}