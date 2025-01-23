package com.somemore.domains.review.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.somemore.domains.review.dto.request.ReviewCreateRequestDto;
import com.somemore.domains.review.dto.request.ReviewUpdateRequestDto;
import com.somemore.domains.review.usecase.CreateReviewUseCase;
import com.somemore.domains.review.usecase.DeleteReviewUseCase;
import com.somemore.domains.review.usecase.UpdateReviewUseCase;
import com.somemore.global.auth.annotation.RoleId;
import com.somemore.global.common.response.ApiResponse;
import com.somemore.global.imageupload.dto.ImageUploadRequestDto;
import com.somemore.global.imageupload.usecase.ImageUploadUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final UpdateReviewUseCase updateReviewUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "리뷰 등록", description = "리뷰를 등록합니다.")
    @PostMapping(value = "/review", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> createReview(
            @RoleId UUID volunteerId,
            @Valid @RequestPart("data") ReviewCreateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image) {

        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        return ApiResponse.ok(
                201,
                createReviewUseCase.createReview(requestDto, volunteerId, imgUrl),
                "리뷰 등록 성공"
        );
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @PutMapping(value = "/review/{id}")
    public ApiResponse<String> updateReview(
            @RoleId UUID volunteerId,
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequestDto requestDto) {

        updateReviewUseCase.updateReview(id, volunteerId, requestDto);

        return ApiResponse.ok(
                200,
                "리뷰 수정 성공"
        );
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "리뷰 이미지 수정", description = "리뷰 이미지를 수정합니다.")
    @PutMapping(value = "/review/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateReviewImage(
            @RoleId UUID volunteerId,
            @PathVariable Long id,
            @RequestPart(value = "img_file", required = false) MultipartFile image) {

        String newImgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));
        updateReviewUseCase.updateReviewImageUrl(id, volunteerId, newImgUrl);
        return ApiResponse.ok(
                200,
                "리뷰 이미지 수정 성공"
        );
    }

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping(value = "/review/{id}")
    public ApiResponse<String> createReview(
            @RoleId UUID volunteerId,
            @PathVariable Long id
    ) {
        deleteReviewUseCase.deleteReview(volunteerId, id);
        return ApiResponse.ok("리뷰 삭제 성공");
    }

}
