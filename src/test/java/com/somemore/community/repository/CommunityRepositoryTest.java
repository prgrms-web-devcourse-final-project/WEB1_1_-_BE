package com.somemore.community.repository;

import com.somemore.IntegrationTestSupport;
import com.somemore.community.domain.CommunityBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class CommunityRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CommunityBoardRepository communityBoardRepository;

    @DisplayName("커뮤니티 게시글 id로 커뮤니티 상세 정보를 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoardById() {
        //given
        CommunityBoard communityBoard = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목")
                .content("테스트 커뮤니티 게시글 내용")
                .imgUrl("http://community.example.com/123")
                .writerId(UUID.randomUUID())
                .build();

        communityBoardRepository.save(communityBoard);

        //when
        Optional<CommunityBoard> foundCommunityBoard = communityBoardRepository.findById(communityBoard.getId());

        //then
        assertThat(foundCommunityBoard).isNotNull();
        assertThat(foundCommunityBoard.get().getTitle()).isEqualTo("테스트 커뮤니티 게시글 제목");
        assertThat(foundCommunityBoard.get().getContent()).isEqualTo("테스트 커뮤니티 게시글 내용");
        assertThat(foundCommunityBoard.get().getImgUrl()).isEqualTo("http://community.example.com/123");
        assertThat(foundCommunityBoard.get().getWriterId()).isEqualTo(communityBoard.getWriterId());
    }

    @DisplayName("삭제된 커뮤니티 id로 커뮤니티 상세 정보를 조회할 때 예외를 반환할 수 있다. (Repository)")
    @Test
    void getCommunityBoardByDeletedId() {
        //given
        CommunityBoard communityBoard = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목")
                .content("테스트 커뮤니티 게시글 내용")
                .imgUrl("http://community.example.com/123")
                .writerId(UUID.randomUUID())
                .build();

        communityBoardRepository.save(communityBoard);
        communityBoardRepository.deleteAllInBatch();

        //when
        Optional<CommunityBoard> foundCommunityBoard = communityBoardRepository.findById(communityBoard.getId());

        //then
        assertThat(foundCommunityBoard).isEmpty();
    }

    @DisplayName("저장된 전체 커뮤니티 게시글을 목록으로 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoards() {
        //given
        CommunityBoard communityBoard1 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목1")
                .content("테스트 커뮤니티 게시글 내용1")
                .imgUrl("http://community.example.com/123")
                .writerId(UUID.randomUUID())
                .build();

        CommunityBoard communityBoard2 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목2")
                .content("테스트 커뮤니티 게시글 내용2")
                .imgUrl("http://community.example.com/12")
                .writerId(UUID.randomUUID())
                .build();

        communityBoardRepository.save(communityBoard1);
        communityBoardRepository.save(communityBoard2);

        //when
        List<CommunityBoard> communityBoards = communityBoardRepository.getCommunityBoards();

        //then
        assertThat(communityBoards).hasSize(2);
        assertThat(communityBoards.get(0)).isEqualTo(communityBoard2);
        assertThat(communityBoards.get(1)).isEqualTo(communityBoard1);
    }

    @DisplayName("저장된 커뮤니티 게시글 리스트를 작성자별로 조회할 수 있다. (Repository)")
    @Test
    void getCommunityBoardsByWriterId() {
        //given
        CommunityBoard communityBoard1 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목1")
                .content("테스트 커뮤니티 게시글 내용1")
                .imgUrl("http://community.example.com/123")
                .writerId(UUID.randomUUID())
                .build();

        CommunityBoard communityBoard2 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목2")
                .content("테스트 커뮤니티 게시글 내용2")
                .imgUrl("http://community.example.com/12")
                .writerId(communityBoard1.getWriterId())
                .build();

        CommunityBoard communityBoard3 = CommunityBoard.builder()
                .title("테스트 커뮤니티 게시글 제목3")
                .content("테스트 커뮤니티 게시글 내용3")
                .imgUrl("http://community.example.com/1")
                .writerId(UUID.randomUUID())
                .build();

        communityBoardRepository.save(communityBoard1);
        communityBoardRepository.save(communityBoard2);
        communityBoardRepository.save(communityBoard3);

        //when
        List<CommunityBoard> communityBoards = communityBoardRepository.findByWriterId(communityBoard1.getWriterId());

        //then
        assertThat(communityBoards).hasSize(2);
        assertThat(communityBoards.get(0)).isEqualTo(communityBoard2);
        assertThat(communityBoards.get(1)).isEqualTo(communityBoard1);
    }
}
