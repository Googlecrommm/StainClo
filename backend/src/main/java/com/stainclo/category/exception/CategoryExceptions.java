package com.stainclo.category.exception;

import com.stainclo.common.exception.ApplicationException;
import com.stainclo.common.exception.ErrorCode;

public final class CategoryExceptions {
    private CategoryExceptions() {}

    // ALREADY EXISTS
    public static class AlreadyExists extends ApplicationException {
        public AlreadyExists(String name){
            super("The " + name + " category already exists", ErrorCode.CATEGORY_ALREADY_EXISTS);
        }
    }

    // CATEGORY NOT FOUND
    public static class NotFound extends ApplicationException {
        public NotFound(Long categoryId){
            super("Category with the id: " + categoryId + " doesn't exist", ErrorCode.CATEGORY_NOT_FOUND);
        }
    }
}
