package com.example.camel_sql.validation;

import org.springframework.stereotype.Component;

@Component
public class JsonBodyValidator {

    public boolean isValidBody(String content){
        try{
            if(null != content){
                return true;
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid body in the JSON file.");
        }
        return  false;
    }
}
