package com.scm.scm.helper;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication){
        String username = "";
        if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken aOAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            String clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();   
             OAuth2User oauth2user = aOAuth2AuthenticationToken.getPrincipal();
            
             if(clientId.equalsIgnoreCase("google")){
                username = oauth2user.getAttribute("email");
           }
    }else{
        return authentication.getName();
        }
    return username;
}
}
