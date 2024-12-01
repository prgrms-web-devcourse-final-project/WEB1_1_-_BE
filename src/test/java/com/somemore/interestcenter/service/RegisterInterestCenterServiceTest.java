package com.somemore.interestcenter.service;

import com.somemore.IntegrationTestSupport;
import com.somemore.center.domain.Center;
import com.somemore.center.repository.CenterRepository;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.DuplicateException;
import com.somemore.interestcenter.dto.request.RegisterInterestCenterRequestDto;
import com.somemore.interestcenter.dto.response.RegisterInterestCenterResponseDto;
import com.somemore.interestcenter.repository.InterestCenterRepository;
import com.somemore.interestcenter.usecase.RegisterInterestCenterUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class RegisterInterestCenterServiceTest extends IntegrationTestSupport {

    @Autowired
    private RegisterInterestCenterUseCase registerInterestCenter;

    @Autowired
    private InterestCenterRepository interestCenterRepository;

    @Autowired
    private CenterRepository centerRepository;

    @DisplayName("봉사자는 관심 기관을 등록할 수 있다.")
    @Test
    void RegisterInterestCenter() {
        //given
        Center center = createCenter();
        UUID volunteerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID centerId = center.getId();
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(volunteerId, centerId);

        //when
        RegisterInterestCenterResponseDto responseDto = registerInterestCenter.registerInterestCenter(requestDto);

        //then
        Optional<RegisterInterestCenterResponseDto> result = interestCenterRepository.findInterestCenterResponseById(responseDto.id());
        assertTrue(result.isPresent());
        assertEquals(responseDto.id(), result.get().id());
        assertEquals(volunteerId, result.get().volunteerId());
        assertEquals(centerId, result.get().centerId());
    }

    @DisplayName("이미 관심 표시한 기관에 관심 표시를 시도하면 예외를 던져준다.")
    @Test
    void registerInterestCenter_WithDuplicateCenterId_ShouldThrowException() {
        // given
        Center center = createCenter();
        UUID volunteerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID centerId = center.getId();
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(volunteerId, centerId);

        registerInterestCenter.registerInterestCenter(requestDto);

        // when
        DuplicateException exception = assertThrows(
                DuplicateException.class,
                () -> registerInterestCenter.registerInterestCenter(requestDto)
        );

        // then
        assertEquals("이미 관심 표시한 기관입니다.", exception.getMessage());
    }


    @DisplayName("존재하지 않는 기관 Id로 관심 기관 등록 시 예외가 발생한다.")
    @Test
    void registerInterestCenter_WithInvalidCenterId_ShouldThrowException() {
        // given
        UUID volunteerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID invalidCenterId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        RegisterInterestCenterRequestDto requestDto = new RegisterInterestCenterRequestDto(volunteerId, invalidCenterId);

        // when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            registerInterestCenter.registerInterestCenter(requestDto);
        });

        //then
        assertEquals("존재하지 않는 기관 입니다.", exception.getMessage());
    }

    private Center createCenter() {
        Center center = Center.create(
                "기본 기관 이름",
                "010-1234-5678",
                "http://example.com/image.jpg",
                "기관 소개 내용",
                "http://example.com",
                "account123",
                "password123"
        );

        centerRepository.save(center);

        return center;
    }

}
