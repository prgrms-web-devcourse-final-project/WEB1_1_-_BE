package com.somemore.review.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.imageupload.dto.ImageUploadRequestDto;
import com.somemore.imageupload.usecase.ImageUploadUseCase;
import com.somemore.review.dto.request.ReviewCreateRequestDto;
import com.somemore.review.usecase.CreateReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Review Command API", description = "리뷰 생성 수정 삭제 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ReviewCommandApiController {

    private final CreateReviewUseCase createReviewUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "리뷰 등록", description = "리뷰를 등록합니다.")
    @PostMapping(value = "/review", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> createReview(
            @AuthenticationPrincipal String userId,
            @Valid @RequestPart("data") ReviewCreateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image) {

        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        return ApiResponse.ok(
                201,
                createReviewUseCase.createReview(requestDto, getId(userId), imgUrl),
                "리뷰 등록 성공"
        );
    }

    private static UUID getId(String id) {
        return UUID.fromString(id);
    }

}
