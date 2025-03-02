package com.somemore.support.fixture;

import com.somemore.domains.community.domain.CommunityBoard;

import java.util.UUID;

public class CommunityBoardFixture {

    public static final String TITLE = "테스트 커뮤니티 게시글 제목";
    public static final String CONTENT = "테스트 커뮤니티 게시글 내용";

    private CommunityBoardFixture() {

    }

    public static CommunityBoard createCommunityBoard() {
        return CommunityBoard.builder()
                .title(TITLE)
                .content(CONTENT)
                .writerId(UUID.randomUUID())
                .build();
    }

    public static CommunityBoard createCommunityBoard(UUID writerId) {
        return CommunityBoard.builder()
                .title(TITLE)
                .content(CONTENT)
                .writerId(writerId)
                .build();
    }
    public static CommunityBoard createCommunityBoard(String title, UUID writerId) {
        return CommunityBoard.builder()
                .title(title)
                .content(CONTENT)
                .writerId(writerId)
                .build();
    }
    public static CommunityBoard createCommunityBoard(String title, String content, UUID writerId) {
        return CommunityBoard.builder()
                .title(title)
                .content(content)
                .writerId(writerId)
                .build();
    }
}
