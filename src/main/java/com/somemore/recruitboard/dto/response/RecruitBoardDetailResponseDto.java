package com.somemore.recruitboard.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.somemore.center.dto.response.CenterSimpleInfoResponseDto;
import com.somemore.location.dto.response.LocationResponseDto;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.RecruitStatus;
import com.somemore.recruitboard.domain.RecruitmentInfo;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.recruitboard.repository.mapper.RecruitBoardDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;


@Builder
@JsonNaming(SnakeCaseStrategy.class)
@Schema(description = "봉사 모집글 기관 및 위치 포함 응답 DTO")
public record RecruitBoardDetailResponseDto(
    @Schema(description = "봉사 모집글 ID", example = "123")
    Long id,
    @Schema(description = "모집글 생성 일시", example = "2024-12-01T09:00:00")
    LocalDateTime createdAt,
    @Schema(description = "모집글 수정 일시", example = "2024-12-01T09:00:00")
    LocalDateTime updatedAt,
    @Schema(description = "모집글 제목", example = "환경 정화 봉사")
    String title,
    @Schema(description = "모집글 내용", example = "도시 공원에서 환경 정화 활동")
    String content,
    @Schema(description = "지역 정보", example = "서울특별시")
    String region,
    @Schema(description = "모집 상태", example = "RECRUITING")
    RecruitStatus recruitStatus,
    @Schema(description = "모집 인원 수", example = "15")
    Integer recruitmentCount,
    @Schema(description = "봉사 시작 일시", example = "2024-12-01T09:00:00")
    LocalDateTime volunteerStartDateTime,
    @Schema(description = "봉사 종료 일시", example = "2024-12-01T13:00:00")
    LocalDateTime volunteerEndDateTime,
    @Schema(description = "봉사 유형", example = "LIVING_SUPPORT")
    VolunteerType volunteerType,
    @Schema(description = "봉사 시간", example = "04:00:00")
    LocalTime volunteerTime,
    @Schema(description = "시간 인정 여부", example = "true")
    Boolean admitted,
    @Schema(description = "이미지 URL", example = "https://image.domain.com/links")
    String imgUrl,
    @Schema(description = "센터 간단 정보")
    CenterSimpleInfoResponseDto center,
    @Schema(description = "위치 정보 DTO")
    LocationResponseDto location
) {

    public static RecruitBoardDetailResponseDto from(RecruitBoardDetail recruitBoardDetail) {
        RecruitBoard board = recruitBoardDetail.recruitBoard();
        RecruitmentInfo info = board.getRecruitmentInfo();
        CenterSimpleInfoResponseDto center = CenterSimpleInfoResponseDto.of(board.getCenterId(),
            recruitBoardDetail.centerName());
        LocationResponseDto location = LocationResponseDto.of(
            recruitBoardDetail.address(), recruitBoardDetail.latitude(),
            recruitBoardDetail.longitude());

        return RecruitBoardDetailResponseDto.builder()
            .id(board.getId())
            .createdAt(board.getCreatedAt())
            .updatedAt(board.getUpdatedAt())
            .title(board.getTitle())
            .content(board.getContent())
            .region(info.getRegion())
            .recruitStatus(board.getRecruitStatus())
            .recruitmentCount(info.getRecruitmentCount())
            .volunteerStartDateTime(info.getVolunteerStartDateTime())
            .volunteerEndDateTime(info.getVolunteerEndDateTime())
            .volunteerType(info.getVolunteerType())
            .volunteerTime(info.calculateVolunteerTime())
            .admitted(info.getAdmitted())
            .imgUrl(board.getImgUrl())
            .location(location)
            .center(center)
            .build();
    }

}
