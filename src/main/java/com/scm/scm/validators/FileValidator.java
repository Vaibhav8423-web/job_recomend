package com.scm.scm.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileValidator implements ConstraintValidator<ValidFile,MultipartFile>{

    private static final long MAX_FILE_SIZE = 1024*1024*5;//5MB 

    //TYPE

    //HEIGHT

    //WIDTH

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file==null || file.isEmpty()){
            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("File cannot be null").addConstraintViolation();
            return true;
        }

        //size
        if(file.getSize()>MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size should be less than 5mb").addConstraintViolation();
            return false;
        }

        return true;
    }

}
