package com.scm.scm.helper;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {
    
    public static void removeMessage(){
    try{HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    session.removeAttribute("message");
    }catch(Exception ex){
        ex.printStackTrace();
    }
}
}
