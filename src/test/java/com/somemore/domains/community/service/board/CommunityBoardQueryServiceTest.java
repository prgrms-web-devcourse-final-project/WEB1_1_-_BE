package com.somemore.domains.community.service.board;

import com.somemore.domains.center.repository.center.CenterRepository;
import com.somemore.domains.community.domain.CommunityBoard;
import com.somemore.domains.community.dto.response.CommunityBoardDetailResponseDto;
import com.somemore.domains.community.dto.response.CommunityBoardResponseDto;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.usecase.board.CreateCommunityBoardUseCase;
import com.somemore.domains.community.usecase.board.DeleteCommunityBoardUseCase;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.ExceptionMessage;
import com.somemore.support.IntegrationTestSupport;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.UUID;

import static com.somemore.support.fixture.CommunityBoardFixture.createCommunityBoard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class CommunityBoardQueryServiceTest extends IntegrationTestSupport {

    @Autowired
    CommunityBoardRepository communityBoardRepository;
    @Autowired
    NEWVolunteerRepository volunteerRepository;
    @Autowired
    CenterRepository centerRepository;
    @Autowired
    CreateCommunityBoardUseCase createCommunityBoardUseCase;
    @Autowired
    DeleteCommunityBoardUseCase deleteCommunityBoardUseCase;
    @Autowired
    CommunityBoardQueryService communityBoardQueryService;

    private UUID writerId1;
    private Long communityId1;

    @BeforeEach
    void setUp() {
        NEWVolunteer volunteer = NEWVolunteer.createDefault(UUID.randomUUID());
        volunteerRepository.save(volunteer);

        NEWVolunteer volunteer2 = NEWVolunteer.createDefault(UUID.randomUUID());
        volunteerRepository.save(volunteer2);

        writerId1 = volunteer.getId();
        UUID writerId2 = volunteer2.getId();

        CommunityBoard communityBoard1 = createCommunityBoard(writerId1);
        communityBoardRepository.save(communityBoard1);
        communityBoardRepository.save(createCommunityBoard(writerId2));

        communityId1 = communityBoard1.getId();

        for (int i = 1; i <= 11; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, writerId1);
            communityBoardRepository.save(communityBoard);
        }

        for (int i = 1; i <= 20; i++) {
            String title = "제목" + i;
            CommunityBoard communityBoard = createCommunityBoard(title, writerId2);
            communityBoardRepository.save(communityBoard);
        }
    }

    @AfterEach
    void tearDown() {
        communityBoardRepository.deleteAllInBatch();
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 페이지로 조회한다.")
    @Test
    void getAllCommunityBoards() {

        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardQueryService.getCommunityBoards(null, 2);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(33);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(4);
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 작성자자별로 페이지 조회한다.")
    @Test
    void getCommunityBoardsByWriter() {

        //given
        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardQueryService.getCommunityBoardsByWriterId(writerId1, 1);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(12);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(2);
    }

    @DisplayName("커뮤니티 게시글의 상세 정보를 조회한다.")
    @Test
    void getCommunityBoardDetail() {

        //given
        //when
        CommunityBoardDetailResponseDto communityBoard = communityBoardQueryService.getCommunityBoardDetail(communityId1);

        //then
        assertThat(communityBoard).isNotNull();
        assertThat(communityBoard.id()).isEqualTo(communityId1);
        assertThat(communityBoard.title()).isEqualTo("테스트 커뮤니티 게시글 제목");
        assertThat(communityBoard.content()).isEqualTo("테스트 커뮤니티 게시글 내용");
        assertThat(communityBoard.writerId()).isEqualTo(writerId1);
    }

    @DisplayName("삭제된 커뮤니티 게시글의 상세 정보를 조회할 때 예외를 던진다.")
    @Test
    void getCommunityBoardDetailWithDeletedId() {

        //given
        deleteCommunityBoardUseCase.deleteCommunityBoard(writerId1, communityId1);

        //when
        ThrowableAssert.ThrowingCallable callable = () -> communityBoardQueryService.getCommunityBoardDetail(communityId1);

        //then
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(callable)
                .withMessage(ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD.getMessage());
    }

    @DisplayName("검색 키워드가 포함된 커뮤니티 게시글 리스트를 페이지로 조회한다. (rdb)")
    @Test
    void getCommunityBoardsBySearch() {

        //given
        String title = "봉사";
        CommunityBoard communityBoard = createCommunityBoard(title, writerId1);
        communityBoardRepository.save(communityBoard);

        //when
        Page<CommunityBoardResponseDto> dtos = communityBoardQueryService.getCommunityBoards("봉사", 0);

        //then
        assertThat(dtos).isNotNull();
        assertThat(dtos.getContent()).isNotNull();
        assertThat(dtos.getTotalElements()).isEqualTo(1);
        assertThat(dtos.getSize()).isEqualTo(10);
        assertThat(dtos.getTotalPages()).isEqualTo(1);
    }
}

