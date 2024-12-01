package com.somemore.volunteer.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.imageupload.dto.ImageUploadRequestDto;
import com.somemore.imageupload.usecase.ImageUploadUseCase;
import com.somemore.volunteer.dto.request.VolunteerProfileUpdateRequestDto;
import com.somemore.volunteer.usecase.UpdateVolunteerProfileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@Tag(name = "PUT Volunteer", description = "봉사자 프로필 수정")
public class VolunteerProfileCommandController {

    private final UpdateVolunteerProfileUseCase updateVolunteerProfileUseCase;
    private final ImageUploadUseCase imageUploadUseCase;

    @Secured("ROLE_VOLUNTEER")
    @Operation(summary = "프로필 수정", description = "현재 로그인된 사용자의 프로필을 수정합니다.")
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> updateProfile(
            @AuthenticationPrincipal String volunteerId,
            @Valid @RequestPart("data") VolunteerProfileUpdateRequestDto requestDto,
            @RequestPart(value = "img_file", required = false) MultipartFile image) {

        String imgUrl = imageUploadUseCase.uploadImage(new ImageUploadRequestDto(image));

        updateVolunteerProfileUseCase.update(
                UUID.fromString(volunteerId),
                requestDto,
                imgUrl
        );

        return ApiResponse.ok("프로필 수정 성공");
    }
}
