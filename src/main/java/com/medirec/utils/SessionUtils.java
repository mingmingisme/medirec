package com.medirec.utils;

import com.medirec.entity.Image;
import com.medirec.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static final String LOGIN_USER = "loginUser";
    public static final String UPLOADED_IMAGE = "uploadedImage";

    public static HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest().getSession();
    }

    public static void saveLoginUserToSession(User user) {
        getSession().setAttribute(LOGIN_USER, user);
    }

    public static void removeLoginUserFromSession() {
        getSession().removeAttribute(LOGIN_USER);
    }

    public static User getLoginUserFromSession() {
        return (User) getSession().getAttribute(LOGIN_USER);
    }

    public static void saveUploadedImageToSession(Image uploadedImage) {
        getSession().setAttribute(UPLOADED_IMAGE, uploadedImage);
    }

    public static void removeUploadedImageFromSession() {
        getSession().removeAttribute(UPLOADED_IMAGE);
        System.out.println("image removed");
    }
}
