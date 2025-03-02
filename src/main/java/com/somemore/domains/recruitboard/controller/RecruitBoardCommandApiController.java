package com.somemore.domains.recruitboard.controller;


import com.somemore.domains.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardLocationUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardStatusUpdateRequestDto;
import com.somemore.domains.recruitboard.dto.request.RecruitBoardUpdateRequestDto;
import com.somemore.domains.recruitboard.usecase.CreateRecruitBoardUseCase;
import com.somemore.domains.recruitboard.usecase.DeleteRecruitBoardUseCase;
import com.somemore.domains.recruitboard.usecase.UpdateRecruitBoardUseCase;
import com.somemore.global.auth.annotation.RoleId;
import com.somemore.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recruit Board Command API", description = "봉사 활동 모집글 생성 수정 삭제 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RecruitBoardCommandApiController {

    private final CreateRecruitBoardUseCase createRecruitBoardUseCase;
    private final UpdateRecruitBoardUseCase updateRecruitBoardUseCase;
    private final DeleteRecruitBoardUseCase deleteRecruitBoardUseCase;

    @Secured("ROLE_CENTER")
    @Operation(summary = "봉사 활동 모집글 등록", description = "봉사 활동 모집글을 등록합니다.")
    @PostMapping(value = "/recruit-board")
    public ApiResponse<Long> createRecruitBoard(
            @RoleId UUID centerId,
            @Valid @RequestBody RecruitBoardCreateRequestDto requestDto
    ) {
        return ApiResponse.ok(
                201,
                createRecruitBoardUseCase.createRecruitBoard(requestDto, centerId),
                "봉사 활동 모집글 등록 성공"
        );
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "봉사 활동 모집글 수정", description = "봉사 활동 모집글을 수정합니다.")
    @PutMapping(value = "/recruit-board/{id}")
    public ApiResponse<String> updateRecruitBoard(
            @RoleId UUID centerId,
            @PathVariable Long id,
            @Valid @RequestBody RecruitBoardUpdateRequestDto requestDto
    ) {
        updateRecruitBoardUseCase.updateRecruitBoard(requestDto, id, centerId);
        return ApiResponse.ok("봉사 활동 모집글 수정 성공");
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "봉사 활동 모집글 위치 수정", description = "봉사 활동 모집글의 위치를 수정합니다.")
    @PutMapping(value = "/recruit-board/{id}/location")
    public ApiResponse<String> updateRecruitBoardLocation(
            @RoleId UUID centerId,
            @PathVariable Long id,
            @Valid @RequestBody RecruitBoardLocationUpdateRequestDto requestDto
    ) {
        updateRecruitBoardUseCase.updateRecruitBoardLocation(requestDto, id, centerId);
        return ApiResponse.ok("봉사 활동 모집글 위치 수정 성공");
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "봉사 활동 모집글 상태 수정", description = "봉사 활동 모집글의 상태를 수정합니다.")
    @PatchMapping(value = "/recruit-board/{id}")
    public ApiResponse<String> updateRecruitBoardStatus(
            @RoleId UUID centerId,
            @PathVariable Long id,
            @RequestBody RecruitBoardStatusUpdateRequestDto requestDto
    ) {
        updateRecruitBoardUseCase.updateRecruitBoardStatus(requestDto.status(), id, centerId);
        return ApiResponse.ok("봉사 활동 모집글 상태 수정 성공");
    }

    @Secured("ROLE_CENTER")
    @Operation(summary = "봉사 활동 모집글 삭제", description = "봉사 활동 모집글을 삭제합니다.")
    @DeleteMapping(value = "/recruit-board/{id}")
    public ApiResponse<String> deleteRecruitBoard(
            @RoleId UUID centerId,
            @PathVariable Long id
    ) {
        deleteRecruitBoardUseCase.deleteRecruitBoard(centerId, id);
        return ApiResponse.ok("봉사 활동 모집글 삭제 성공");
    }
}
