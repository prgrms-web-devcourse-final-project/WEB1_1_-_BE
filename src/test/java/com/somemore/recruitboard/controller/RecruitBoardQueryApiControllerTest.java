package com.somemore.recruitboard.controller;

import static com.somemore.recruitboard.domain.VolunteerType.ADMINISTRATIVE_SUPPORT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.somemore.ControllerTestSupport;
import com.somemore.recruitboard.dto.condition.RecruitBoardNearByCondition;
import com.somemore.recruitboard.dto.condition.RecruitBoardSearchCondition;
import com.somemore.recruitboard.dto.response.RecruitBoardDetailResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithCenterResponseDto;
import com.somemore.recruitboard.dto.response.RecruitBoardWithLocationResponseDto;
import com.somemore.recruitboard.usecase.query.RecruitBoardQueryUseCase;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class RecruitBoardQueryApiControllerTest extends ControllerTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecruitBoardQueryUseCase recruitBoardQueryUseCase;

    @Test
    @DisplayName("모집글 ID로 상세 조회할 수 있다.")
    void getById() throws Exception {
        // given
        Long recruitBoardId = 1L;
        var responseDto = RecruitBoardWithLocationResponseDto.builder().build();
        given(recruitBoardQueryUseCase.getWithLocationById(recruitBoardId)).willReturn(responseDto);

        // when
        // then
        mockMvc.perform(get("/api/recruit-board/{id}", recruitBoardId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("봉사 활동 모집 상세 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getWithLocationById(recruitBoardId);
    }

    @Test
    @DisplayName("모집글 페이징 처리하여 전체 조회 할 수 있다.")
    void getAll() throws Exception {
        // given
        Page<RecruitBoardWithCenterResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(recruitBoardQueryUseCase.getAllWithCenter(any(RecruitBoardSearchCondition.class)))
            .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("봉사 활동 모집글 리스트 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getAllWithCenter(
            any(RecruitBoardSearchCondition.class));
    }

    @Test
    @DisplayName("모집글을 검색 조건으로 페이징 조회할 수 있다.")
    void getAllBySearch() throws Exception {
        // given
        Page<RecruitBoardWithCenterResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(recruitBoardQueryUseCase.getAllWithCenter(any(RecruitBoardSearchCondition.class)))
            .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards/search")
                .param("keyword", "volunteer")
                .param("type", ADMINISTRATIVE_SUPPORT.name())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("봉사 활동 모집글 검색 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getAllWithCenter(
            any(RecruitBoardSearchCondition.class));
    }

    @Test
    @DisplayName("위치 기반으로 근처 있는 모집글 페이징 조회할 수 있다.")
    void getNearby() throws Exception {
        // given
        Page<RecruitBoardDetailResponseDto> page = new PageImpl<>(Collections.emptyList());
        given(recruitBoardQueryUseCase.getRecruitBoardsNearby(
            any(RecruitBoardNearByCondition.class)
        )).willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards/nearby")
                .param("latitude", "37.5665")
                .param("longitude", "126.9780")
                .param("radius", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("근처 봉사 활동 모집글 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getRecruitBoardsNearby(
            any(RecruitBoardNearByCondition.class));
    }

    @Test
    @DisplayName("기관 ID로 모집글 페이징 조회할 수 있다.")
    void getRecruitBoardsByCenterId() throws Exception {
        // given
        UUID centerId = UUID.randomUUID();
        Page<RecruitBoardResponseDto> page = new PageImpl<>(Collections.emptyList());

        given(recruitBoardQueryUseCase.getRecruitBoardsByCenterId(eq(centerId),
            any(RecruitBoardSearchCondition.class)))
            .willReturn(page);

        // when
        // then
        mockMvc.perform(get("/api/recruit-boards/center/{centerId}", centerId)
                .param("keyword", "volunteer")
                .param("type", ADMINISTRATIVE_SUPPORT.name())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("기관 봉사 활동 모집글 조회 성공"));

        verify(recruitBoardQueryUseCase, times(1)).getRecruitBoardsByCenterId(eq(centerId),
            any(RecruitBoardSearchCondition.class));
    }
}
