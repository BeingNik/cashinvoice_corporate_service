package com.example.camel_sql.validation;

import org.springframework.stereotype.Component;

@Component
public class JsonHeaderValidator {

    public boolean isValidHeader(String content){
        try{
            if(null != content){
                return true;
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid headers in the JSON file.");
        }
        return  false;
    }
}
