package com.scm.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm.entities.User;
import com.scm.scm.helper.Helper;
import com.scm.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @ModelAttribute
        public void addLoggedInUserInformation(Model model, Authentication authentication){
            if(authentication==null) return;
            String name = Helper.getEmailOfLoggedInUser(authentication);
            logger.info("User logged in {}",name);
            User user = userService.getUserByEmail(name);
            model.addAttribute("loggedInUser", user);
        }
}
