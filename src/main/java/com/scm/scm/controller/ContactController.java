package com.scm.scm.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.scm.entities.Contact;
import com.scm.scm.entities.User;
import com.scm.scm.enums.MessageType;
import com.scm.scm.forms.ContactForm;
import com.scm.scm.forms.ContactSearchForm;
import com.scm.scm.helper.AppConstants;
import com.scm.scm.helper.Helper;
import com.scm.scm.helper.Message;
import com.scm.scm.services.ContactService;
import com.scm.scm.services.ImageService;
import com.scm.scm.services.UserService;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;

    @RequestMapping(value = "/add", method=RequestMethod.GET)
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,  Authentication authentication, HttpSession httpSession) {
        //process the form data

        if(result.hasErrors()){ 
            httpSession.setAttribute("message", Message.builder()
            .content("Please correct the following errors")
            .type(MessageType.red)
            .build());
            return "user/add_contact"; 
        }
        String userName = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(userName);

        //Image upload krne ka code likho bhai

       
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        
        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
            String fileName = UUID.randomUUID().toString();
        String fileURL = imageService.uploadImage(contactForm.getContactImage(),fileName);
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(fileName);
        }
        contact.setUser(user);

        contactService.save(contact);
        httpSession.setAttribute("message", Message.builder()
            .content("Contact registered successfully")
            .type(MessageType.green)
            .build());
        return "redirect:/user/contacts/add";
    }
    
    @GetMapping()
    public String viewContacts(
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="size", defaultValue = "10") int size,
        @RequestParam(value="sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction ,Model model, Authentication authentication) {
        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("pageContact", pageContact);
        return "/user/contacts";
    }

    //search handler

    @RequestMapping(value="/search", method=RequestMethod.GET)
    public String requestMethodName(@RequestParam("field") String field,
    @RequestParam("keyword") String value,
     @RequestParam(value="size", defaultValue =  AppConstants.PAGE_SIZE+"") int size,
     @RequestParam(value = "page", defaultValue = "0") int page,
     @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
     @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model, Authentication authentication) {

        Page<Contact> pageContact = null;

        String user = Helper.getEmailOfLoggedInUser(authentication);

        if(field.equalsIgnoreCase("name")){
           pageContact = contactService.searchByName(value, size, page, sortBy, direction, user);
        }else if(field.equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(value, size, page, sortBy, direction, user);
        }else if(field.equalsIgnoreCase("phone")){
            pageContact = contactService.searchByPhoneNumber(value, size, page, sortBy, direction, user);
        }

        model.addAttribute("pageContact", pageContact);

        return new String("/user/search");
    }
    
    @RequestMapping("/delete/{contactId}")
    public String deleteData(@PathVariable String contactId, HttpSession session){
        contactService.delete(contactId);

        session.setAttribute("message", Message
        .builder()
        .content("Contact deleted successfully")
        .build());
        return "redirect:/user/contacts";
    }
    
    //Update conact from view
    @GetMapping("view/{contactId}")
    public String updateContactFormView(@PathVariable String contactId, Model model){


        Contact contact = contactService.getById(contactId);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";
    }

    @RequestMapping(value="/update/{contactId}", method=RequestMethod.POST)
    public String requestMethodName(@PathVariable String contactId, @Valid  @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Model model) {
       
        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }

        Contact contact = contactService.getById(contactId);
        contact.setId(contactId);
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setEmail(contactForm.getEmail());
        contact.setFavorite(contactForm.isFavorite());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setName(contactForm.getName());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
    
        if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }

        Contact updatedContact = contactService.update(contact);
        model.addAttribute("message", Message.builder()
        .content("Contact Updated")
        .build());
        return "redirect:/user/contacts/view/"+contactId;
    }
    
}
