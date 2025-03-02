package com.somemore.global.exception;

public class ImageUploadException extends RuntimeException {

    public ImageUploadException(String message) {
        super(message);
    }

    public ImageUploadException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
