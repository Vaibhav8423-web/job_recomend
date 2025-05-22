package com.scm.scm.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    SecurityCustomUserDetailService userDetailService;

    @Autowired
    OauthAuthenticationSuccessHandler handler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((request)->request
        .regexMatchers("^/user/.*$").authenticated()
        .anyRequest().permitAll());
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate"); // ✅ Form action matches loginProcessingUrl
            formLogin.defaultSuccessUrl("/user/profile", true); // ✅ Redirect after login
            // formLogin.failureUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
        //     formLogin.failureHandler(new AuthenticationFailureHandler() {

        //         @Override
        //         public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        //                 AuthenticationException exception) throws IOException, ServletException {
        //             // TODO Auto-generated method stub
        //             throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
        //         }
                
        //     });
        //     formLogin.successHandler(new AuthenticationSuccessHandler() {

        //         @Override
        //         public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        //                 Authentication authentication) throws IOException, ServletException {
        //             // TODO Auto-generated method stub
        //             throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
        //         }
                
        //     });
         });
         httpSecurity.csrf().disable();
         httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
         });

         //oathConfiguration
         httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
         });
        return httpSecurity.build();
    }
    
    

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
