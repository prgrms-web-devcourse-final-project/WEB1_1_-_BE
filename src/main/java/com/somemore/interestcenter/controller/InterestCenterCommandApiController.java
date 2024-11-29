package com.somemore.interestcenter.controller;

import com.somemore.global.common.response.ApiResponse;
import com.somemore.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.interestcenter.usecase.CancelInterestCenterUseCase;
import com.somemore.interestcenter.usecase.RegisterInterestCenterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Interest Center Command API", description = "관심 기관의 등록과 취소 API를 제공합니다")
public class InterestCenterCommandApiController {

    private final RegisterInterestCenterUseCase registerInterestCenterUseCase;
    private final CancelInterestCenterUseCase cancelInterestCenterUseCase;

    @Operation(summary = "관심기관 등록 API")
    @PostMapping("/api/interest-center")
    public ApiResponse<RegisterInterestCenterResponseDto> registerInterestCenter(@Valid @RequestBody RegisterInterestCenterRequestDto requestDto) {

        RegisterInterestCenterResponseDto responseDto = registerInterestCenterUseCase.registerInterestCenter(requestDto);

        return ApiResponse.ok(200, responseDto, "관심 기관 등록 성공");
    }

    @Operation(summary = "관심기관 취소 API")
    @DeleteMapping("/api/interest-center/{interest-center-id}")
    public ApiResponse<String> deleteInterestCenter(@PathVariable("interest-center-id") Long interestCenterId) {

        cancelInterestCenterUseCase.cancelInterestCenter(interestCenterId);

        return ApiResponse.ok("관심 기관 취소 성공");
    }

}
