package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import com.somemore.domains.review.domain.Review;
import com.somemore.domains.review.repository.ReviewRepository;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyWithReviewStatusResponseDto;
import com.somemore.domains.volunteerapply.repository.VolunteerApplyRepository;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.domain.UserRole;
import com.somemore.user.repository.usercommonattribute.UserCommonAttributeRepository;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.somemore.domains.volunteerapply.domain.ApplyStatus.APPROVED;
import static com.somemore.support.fixture.RecruitBoardFixture.createRecruitBoard;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class VolunteerApplyQueryFacadeServiceTest extends IntegrationTestSupport {

    @Autowired
    private VolunteerApplyQueryFacadeService volunteerApplyQueryFacadeService;
    @Autowired
    private RecruitBoardRepository recruitBoardRepository;
    @Autowired
    private NEWVolunteerRepository volunteerRepository;
    @Autowired
    private UserCommonAttributeRepository userCommonAttributeRepository;
    @Autowired
    private VolunteerApplyRepository volunteerApplyRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @DisplayName("모집글 아이디와 봉사자 아이디로 지원 응답 값을 조회할 수 있다.")
    @Test
    void getVolunteerApplyByRecruitIdAndVolunteerId() {
        // given
        Long recruitBoardId = 1234L;
        UUID volunteerId = UUID.randomUUID();
        VolunteerApply apply = createApply(volunteerId, recruitBoardId);
        volunteerApplyRepository.save(apply);

        Review review = createReview(apply.getId(), volunteerId);
        reviewRepository.save(review);

        // when
        VolunteerApplyWithReviewStatusResponseDto dto =
                volunteerApplyQueryFacadeService.getVolunteerApplyByRecruitIdAndVolunteerId(recruitBoardId, volunteerId);

        // then
        assertThat(dto.recruitBoardId()).isEqualTo(recruitBoardId);
        assertThat(dto.volunteerId()).isEqualTo(volunteerId);
        assertThat(dto.isReviewed()).isTrue();
    }

    @DisplayName("모집글 아이디와 기관 아이디로 필터에 맞는 지원자 간단 정보를 조회할 수 있다.")
    @Test
    void getVolunteerAppliesByRecruitIdAndCenterId() {
        // given
        UUID centerId = UUID.randomUUID();
        RecruitBoard board = createRecruitBoard(centerId);
        recruitBoardRepository.save(board);

        UUID userIdOne = UUID.randomUUID();
        UUID userIdTwo = UUID.randomUUID();

        UserCommonAttribute userOne = createUserCommonAttribute(userIdOne);
        UserCommonAttribute userTwo = createUserCommonAttribute(userIdTwo);
        userCommonAttributeRepository.save(userOne);
        userCommonAttributeRepository.save(userTwo);

        NEWVolunteer volunteerOne = createVolunteer(userIdOne);
        NEWVolunteer volunteerTwo = createVolunteer(userIdTwo);
        volunteerRepository.save(volunteerOne);
        volunteerRepository.save(volunteerTwo);

        VolunteerApply apply1 = createApply(volunteerOne.getId(), board.getId());
        VolunteerApply apply2 = createApply(volunteerTwo.getId(), board.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2));

        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .pageable(getPageable())
                .build();

        // when
        Page<VolunteerApplyVolunteerInfoResponseDto> result =
                volunteerApplyQueryFacadeService.getVolunteerAppliesByRecruitIdAndCenterId(board.getId(), centerId, condition);

        // then
        assertThat(result).hasSize(2);

    }

    @DisplayName("봉사자 아이디로 봉사 지원 리스트를 페이징 조회할 수 있다.")
    @Test
    void getVolunteerAppliesByVolunteerId() {
        // given
        UUID volunteerId = UUID.randomUUID();

        RecruitBoard board1 = createRecruitBoard();
        RecruitBoard board2 = createRecruitBoard();
        RecruitBoard board3 = createRecruitBoard();
        recruitBoardRepository.saveAll(List.of(board1, board2, board3));

        VolunteerApply apply1 = createApply(volunteerId, board1.getId());
        VolunteerApply apply2 = createApply(volunteerId, board2.getId());
        VolunteerApply apply3 = createApply(volunteerId, board3.getId());
        VolunteerApply apply4 = createApply(UUID.randomUUID(), board3.getId());
        volunteerApplyRepository.saveAll(List.of(apply1, apply2, apply3, apply4));

        VolunteerApplySearchCondition condition = VolunteerApplySearchCondition.builder()
                .pageable(getPageable())
                .build();
        // when
        Page<VolunteerApplyRecruitInfoResponseDto> result = volunteerApplyQueryFacadeService
                .getVolunteerAppliesByVolunteerId(volunteerId, condition);

        // then
        assertThat(result).hasSize(3);
    }

    private static Review createReview(Long volunteerApplyId, UUID volunteerId) {
        return Review.builder()
                .volunteerApplyId(volunteerApplyId)
                .volunteerId(volunteerId)
                .title("리뷰 제목")
                .content("리뷰 내용")
                .build();
    }

    private static VolunteerApply createApply(UUID volunteerId, Long recruitId) {
        return VolunteerApply.builder()
                .volunteerId(volunteerId)
                .recruitBoardId(recruitId)
                .status(APPROVED)
                .attended(true)
                .build();
    }

    private static NEWVolunteer createVolunteer(UUID userIdOne) {
        return NEWVolunteer.createDefault(userIdOne);
    }

    private static UserCommonAttribute createUserCommonAttribute(UUID userIdOne) {
        return UserCommonAttribute.createDefault(userIdOne, UserRole.VOLUNTEER);
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 4);
    }
}
