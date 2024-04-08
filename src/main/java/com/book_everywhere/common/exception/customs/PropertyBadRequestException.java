package com.book_everywhere.common.exception.customs;

public class PropertyBadRequestException extends CustomException{
    public PropertyBadRequestException(CustomErrorCode errorCode) {
        super(errorCode);
    }
}
