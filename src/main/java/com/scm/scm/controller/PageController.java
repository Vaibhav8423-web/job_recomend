package com.scm.scm.controller;

import com.scm.scm.entities.User;
import com.scm.scm.enums.MessageType;
import com.scm.scm.forms.UserForm;
import com.scm.scm.helper.Message;
import com.scm.scm.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    

    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("firstName","Vaibhav");
        model.addAttribute("lastName", "Mishra");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(){
        System.out.println("About section is executing");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("Service section is executing");
        return "services";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @PostMapping("/doRegister") 
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        
        if(rBindingResult.hasErrors()) return "register";
        
        User user = new User();
        user.setAbout(userForm.getAbout());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setProfilePic("https://static.vecteezy.com/system/resources/previews/024/983/914/non_2x/simple-user-default-icon-free-png.png");

        User savedUser=userService.saveUser(user);
        Message message = Message.builder().content("Registration Successful").type(MessageType.blue).build();
        session.setAttribute("message", message);
        return "redirect:/register";
    }
}
