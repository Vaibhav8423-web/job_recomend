package com.scm.scm.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm.entities.User;
import com.scm.scm.enums.Providers;
import com.scm.scm.helper.AppConstants;
import com.scm.scm.repository.UserRepo;

@Component
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    Logger logger = LoggerFactory.getLogger(OauthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            logger.info("OauthAuthenticationSuccesHandler");

            //identify the provider
             OAuth2AuthenticationToken OAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

             String authorizedClientRegistrationId = OAuth2AuthenticationToken.getAuthorizedClientRegistrationId();   
   
            DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();

            User user2 = new User();
            user2.setUserId(UUID.randomUUID().toString());
            user2.setRoleList(Arrays.asList(AppConstants.ROLE_USER));
            user2.setEmailVerified(true);

            if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                user2.setEmail(user.getAttribute("email").toString());
                user2.setProfilePic(user.getAttribute("picture").toString());
                user2.setName(user.getAttribute("name").toString());
                user2.setProviderUserId(user.getName());
                user2.setProvider(Providers.GOOGLE);
                user2.setPassword("dummy");
                user2.setAbout("This account is created using google");
            }
            // //save data in DB
            // String email = user.getAttribute("email").toString();
            // String name = user.getAttribute("name").toString();
            // String picture = user.getAttribute("picture").toString();



            // User user2 = new User();
            // user2.setName(name);
            // user2.setEmail(email);
            // user2.setProfilePic(picture);
            // user2.setPassword("password");
            // user2.setUserId(UUID.randomUUID().toString());
            // user2.setProvider(Providers.GOOGLE);
            // user2.setEnabled(true);
            // user2.setEmailVerified(true);
            // user2.setProviderUserId(user.getName());
            // user2.setRoleList(Arrays.asList(AppConstants.ROLE_USER));
            // user2.setAbout("This account is logged by using google");
            // User user3 = userRepo.findByEmail(email).orElse(null);
            // if(user3 == null) {
            //     userRepo.save(user2);
            //     logger.info("User save : "+email);
            // }
            
             User user3 = userRepo.findByEmail(user2.getEmail()).orElse(null);
            if(user3 == null) {
                userRepo.save(user2);
                logger.info("User save : "+user2.getEmail());
            }
            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
        }

}
