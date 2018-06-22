package com.aek.common.core.exception;

import com.aek.common.core.config.ExceptionResources;

public class ExceptionFactory {

    public static BusinessException create(String errorCode, String... args){
        String exceptionPattern = ExceptionResources.getErrorMessage(errorCode);

        if(args!=null && args.length > 0){
            String errorMsg = String.format(exceptionPattern, args);
            return new BusinessException(errorCode,errorMsg);
        }
        return new BusinessException(errorCode,exceptionPattern);
    }

}	
