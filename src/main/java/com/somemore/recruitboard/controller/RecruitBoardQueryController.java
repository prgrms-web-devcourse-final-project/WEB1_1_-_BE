package com.somemore.recruitboard.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recruit Board Query API", description = "봉사 활동 모집 조회 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RecruitBoardQueryController {

    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;

    @GetMapping("/recruit-board/{id}")
    @Operation(summary = "봉사 모집글 상세 조회", description = "특정 모집글의 상세 정보를 조회합니다.")
    public ApiResponse<RecruitBoardWithLocationResponseDto> getById(
        @PathVariable Long id
    ) {
        return ApiResponse.ok(
            200,
            recruitBoardQueryUseCase.getWithLocationById(id),
            "봉사 활동 모집 상세 조회 성공"
        );
    }

    @GetMapping("/recruit-boards")
    @Operation(summary = "전체 모집글 조회", description = "모든 봉사 모집글 목록을 조회합니다.")
    public ApiResponse<Page<RecruitBoardWithCenterResponseDto>> getAll(
        @PageableDefault(sort = "created_at", direction = DESC)
        Pageable pageable
    ) {
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
            .pageable(pageable)
            .build();

        return ApiResponse.ok(
            200,
            recruitBoardQueryUseCase.getAllWithCenter(condition),
            "봉사 활동 모집글 리스트 조회 성공"
        );
    }

    @GetMapping("/recruit-boards/search")
    @Operation(summary = "모집글 검색 조회", description = "검색 조건을 기반으로 모집글을 조회합니다.")
    public ApiResponse<Page<RecruitBoardWithCenterResponseDto>> getAllBySearch(
        @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) VolunteerType type,
        @RequestParam(required = false) String region,
        @RequestParam(required = false) Boolean admitted,
        @RequestParam(required = false) RecruitStatus status
    ) {
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
            .keyword(keyword)
            .type(type)
            .region(region)
            .admitted(admitted)
            .status(status)
            .pageable(pageable)
            .build();

        return ApiResponse.ok(
            200,
            recruitBoardQueryUseCase.getAllWithCenter(condition),
            "봉사 활동 모집글 검색 조회 성공"
        );
    }

    @GetMapping("/recruit-boards/nearby")
    @Operation(summary = "근처 모집글 조회", description = "주변 반경 내의 봉사 모집글을 조회합니다.")
    public ApiResponse<Page<RecruitBoardDetailResponseDto>> getNearby(
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam(required = false, defaultValue = "5") double radius,
        @RequestParam(required = false) String keyword,
        @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable
    ) {
        RecruitBoardNearByCondition condition = RecruitBoardNearByCondition.builder()
            .latitude(latitude)
            .longitude(longitude)
            .radius(radius)
            .keyword(keyword)
            .pageable(pageable)
            .build();

        return ApiResponse.ok(
            200,
            recruitBoardQueryUseCase.getRecruitBoardsNearby(condition),
            "근처 봉사 활동 모집글 조회 성공"
        );
    }

    @GetMapping("/recruit-boards/center/{centerId}")
    @Operation(summary = "특정 기관 모집글 조회", description = "특정 기관의 봉사 모집글을 조회합니다.")
    public ApiResponse<Page<RecruitBoardResponseDto>> getRecruitBoardsByCenterId(
        @PathVariable UUID centerId,
        @PageableDefault(sort = "created_at", direction = DESC) Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) VolunteerType type,
        @RequestParam(required = false) String region,
        @RequestParam(required = false) Boolean admitted,
        @RequestParam(required = false) RecruitStatus status
    ) {
        RecruitBoardSearchCondition condition = RecruitBoardSearchCondition.builder()
            .keyword(keyword)
            .type(type)
            .region(region)
            .admitted(admitted)
            .status(status)
            .pageable(pageable)
            .build();

        return ApiResponse.ok(
            200,
            recruitBoardQueryUseCase.getRecruitBoardsByCenterId(centerId, condition),
            "기관 봉사 활동 모집글 조회 성공"
        );
    }
}
