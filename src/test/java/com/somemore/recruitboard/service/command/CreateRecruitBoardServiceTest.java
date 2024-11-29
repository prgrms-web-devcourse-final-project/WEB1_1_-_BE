package com.somemore.recruitboard.service.command;

import static com.somemore.common.fixture.LocalDateTimeFixture.createStartDateTime;
import static org.assertj.core.api.Assertions.assertThat;

import com.somemore.IntegrationTestSupport;
import com.somemore.location.dto.request.LocationCreateRequestDto;
import com.somemore.location.repository.LocationRepository;
import com.somemore.recruitboard.domain.RecruitBoard;
import com.somemore.recruitboard.domain.VolunteerType;
import com.somemore.recruitboard.dto.request.RecruitBoardCreateRequestDto;
import com.somemore.recruitboard.repository.RecruitBoardJpaRepository;
import com.somemore.recruitboard.repository.RecruitBoardRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateRecruitBoardServiceTest extends IntegrationTestSupport {

    @Autowired
    private CreateRecruitBoardService createRecruitBoardService;

    @Autowired
    private RecruitBoardJpaRepository recruitBoardJpaRepository;

    @Autowired
    private RecruitBoardRepository recruitBoardRepository;

    @Autowired
    private LocationRepository locationRepository;

    @AfterEach
    void tearDown() {
        recruitBoardJpaRepository.deleteAllInBatch();
        locationRepository.deleteAllInBatch();
    }

    @DisplayName("봉사 모집글 생성 정보로 모집글을 저장한다")
    @Test
    void createRecruitBoardWithDto() {
        // given
        LocationCreateRequestDto locationDto = LocationCreateRequestDto.builder()
            .address("도로명 주소 33")
            .latitude(BigDecimal.valueOf(37.4845373748015))
            .longitude(BigDecimal.valueOf(127.010842267696))
            .build();

        LocalDateTime startDateTime = createStartDateTime();
        LocalDateTime endDateTime = startDateTime.plusHours(2);

        RecruitBoardCreateRequestDto dto = RecruitBoardCreateRequestDto.builder()
            .title("봉사 모집글 작성")
            .content("봉사 하실분을 모집합니다. <br>")
            .region("지역")
            .recruitmentCount(10)
            .volunteerStartDateTime(startDateTime)
            .volunteerEndDateTime(endDateTime)
            .volunteerType(VolunteerType.OTHER)
            .admitted(true)
            .location(locationDto)
            .build();

        UUID centerId = UUID.randomUUID();
        String imgUrl = "https://image.domain.com/links";

        // when
        Long saveId = createRecruitBoardService.createRecruitBoard(dto, centerId, imgUrl);

        // then
        Optional<RecruitBoard> recruitBoard = recruitBoardRepository.findById(saveId);

        assertThat(recruitBoard).isPresent();
        assertThat(recruitBoard.get().getId()).isEqualTo(saveId);
        assertThat(recruitBoard.get().getCenterId()).isEqualTo(centerId);
        assertThat(recruitBoard.get().getImgUrl()).isEqualTo(imgUrl);
    }

}
