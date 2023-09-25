package com.wex.purchase.transaction.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ExceptionBuilder implements Serializable {
    public static final String TITLE = "title";
    public static final String STATUS = "status";
    public static final String DETAIL = "detail";

    public static Map<String, Object> buildError(String title, HttpStatus status, Object detail) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException(TITLE + " is required");
        }
        if (status == null) {
            throw new IllegalArgumentException(STATUS + " is required");
        }
        if (StringUtils.isEmpty(detail)) {
            throw new IllegalArgumentException(DETAIL + " is required");
        }
        Map<String, Object> map = new HashMap<>();
        map.put(TITLE, title);
        map.put(STATUS, status.value());
        map.put(DETAIL, detail);
        return map;
    }

}
