package com.somemore.domains.community.service.comment;

import com.somemore.domains.community.domain.CommunityComment;
import com.somemore.domains.community.dto.request.CommunityCommentCreateRequestDto;
import com.somemore.domains.community.event.CommentAddedEvent;
import com.somemore.domains.community.repository.board.CommunityBoardRepository;
import com.somemore.domains.community.repository.comment.CommunityCommentRepository;
import com.somemore.domains.community.usecase.comment.CreateCommunityCommentUseCase;
import com.somemore.global.common.event.ServerEventPublisher;
import com.somemore.global.exception.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_BOARD;
import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_COMMUNITY_COMMENT;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateCommunityCommentService implements CreateCommunityCommentUseCase {

    private final CommunityBoardRepository communityBoardRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final ServerEventPublisher serverEventPublisher;

    @Override
    public Long createCommunityComment(CommunityCommentCreateRequestDto requestDto, UUID writerId, Long communityBoardId) {
        CommunityComment communityComment = requestDto.toEntity(writerId, communityBoardId);

        validateCommunityBoardExists(communityBoardId);

        if (requestDto.parentCommentId() != null) {
            validateParentCommentExists(communityComment.getParentCommentId());
        }

        publishCommentAddedEvent(communityComment, writerId);

        return communityCommentRepository.save(communityComment).getId();
    }

    private void validateCommunityBoardExists(Long communityBoardId) {
        if (communityBoardRepository.doesNotExistById(communityBoardId)) {
            throw new BadRequestException(NOT_EXISTS_COMMUNITY_BOARD.getMessage());
        }
    }

    private void validateParentCommentExists(Long parentCommentId) {
        if (communityCommentRepository.doesNotExistById(parentCommentId)) {
            throw new BadRequestException(NOT_EXISTS_COMMUNITY_COMMENT.getMessage());
        }
    }

    private void publishCommentAddedEvent(CommunityComment communityComment, UUID writerId) {
        Long parentCommentId = communityComment.getParentCommentId();

        UUID targetUserId = getTargetUserId(communityComment, parentCommentId);

        if (writerId == targetUserId) {
            return;
        }

        CommentAddedEvent event = CommentAddedEvent.of(targetUserId, communityComment.getCommunityBoardId());

        serverEventPublisher.publish(event);
    }

    private UUID getTargetUserId(CommunityComment communityComment, Long parentCommentId) {
        UUID targetUserId;

        if (parentCommentId == null) {
            targetUserId = communityBoardRepository.findById(communityComment.getCommunityBoardId())
                    .orElseThrow(EntityNotFoundException::new)
                    .getWriterId();

            return targetUserId;
        }

        targetUserId = communityCommentRepository.findById(parentCommentId)
                .map(CommunityComment::getWriterId)
                .orElse(null);

        return targetUserId;
    }
}
