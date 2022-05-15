package com.medirec.utils;

import com.medirec.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static final String LOGIN_USER = "loginUser";

    public static HttpSession getSession(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest().getSession();
    }

    public static void saveLoginUserToSession(User user) {
        getSession().setAttribute(LOGIN_USER, user);
    }

    public static void removeLoginUserFromSession() {
        getSession().removeAttribute(LOGIN_USER);
    }
}
