package com.somemore.community.usecase.comment;

import com.somemore.community.dto.request.CommunityCommentCreateRequestDto;

import java.util.UUID;

public interface CreateCommunityCommentUseCase {
    Long createCommunityComment(
            CommunityCommentCreateRequestDto requestDto,
            UUID writerId);
}
