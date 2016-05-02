package com.gw.api;

/**
 * Created by gongwen on 2016/4/24.
 */
public class ApiException extends RuntimeException {
   
    public ApiException(String detailMessage) {
        super(detailMessage);
    }

}
