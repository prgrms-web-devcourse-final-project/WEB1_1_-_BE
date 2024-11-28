package com.somemore.global.handler;


import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ImageUploadException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //예시 코드
    @ExceptionHandler(BadRequestException.class)
    ProblemDetail handleBadRequestException(final BadRequestException e) {

        //status와 에러에 대한 자세한 설명
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

        //아래와 같이 필드 확장 가능
        problemDetail.setTitle("무슨 에러입니다");

        return problemDetail;
    }

    @ExceptionHandler(ImageUploadException.class)
    ProblemDetail handleImageUploadException(final ImageUploadException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

        problemDetail.setTitle("이미지 업로드 실패");
        problemDetail.setDetail("업로드 중 문제가 발생했습니다. 파일 크기나 형식이 올바른지 확인해 주세요.");

        return problemDetail;
    }

}
