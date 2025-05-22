package com.scm.scm.forms;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.scm.scm.validators.ValidFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    private String description;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;

    //annotation create for validation
    //validate the size
    //resolution 
    //type

    @ValidFile
    private MultipartFile contactImage;

    private String picture;
}
