package com.wex.purchase.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class ResourceBadRequestException extends RuntimeException {


    public ResourceBadRequestException(String message) {
        super(message);
    }

    public String toString() {
        return "BadRequestException()";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResourceBadRequestException)) {
            return false;
        } else {
            ResourceBadRequestException other = (ResourceBadRequestException)o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ResourceBadRequestException;
    }
}
