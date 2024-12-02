package com.somemore.review.service;

import static com.somemore.global.exception.ExceptionMessage.REVIEW_ALREADY_EXISTS;
import static com.somemore.global.exception.ExceptionMessage.REVIEW_RESTRICTED_TO_ATTENDED;

import com.somemore.global.exception.BadRequestException;
import com.somemore.review.domain.Review;
import com.somemore.review.dto.request.ReviewCreateRequestDto;
import com.somemore.review.repository.ReviewRepository;
import com.somemore.review.usecase.CreateReviewUseCase;
import com.somemore.volunteerapply.domain.VolunteerApply;
import com.somemore.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateReviewService implements CreateReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;

    @Override
    public Long createReview(ReviewCreateRequestDto requestDto, UUID volunteerId, String imgUrl) {
        VolunteerApply apply = getVolunteerApply(requestDto.recruitBoardId(), volunteerId);
        validateReviewNotExist(apply);
        validateActivityCompletion(apply);

        Review review = requestDto.toEntity(apply, volunteerId, imgUrl);
        return reviewRepository.save(review).getId();
    }

    private VolunteerApply getVolunteerApply(Long recruitBoardId, UUID volunteerId) {
        return volunteerApplyQueryUseCase.getByRecruitIdAndVolunteerId(recruitBoardId, volunteerId);
    }

    private void validateReviewNotExist(VolunteerApply apply) {
        if (reviewRepository.existsByVolunteerApplyId(apply.getId())) {
            throw new BadRequestException(REVIEW_ALREADY_EXISTS.getMessage());
        }
    }

    private void validateActivityCompletion(VolunteerApply apply) {
        if (apply.isVolunteerActivityCompleted()) {
            return;
        }
        throw new BadRequestException(REVIEW_RESTRICTED_TO_ATTENDED.getMessage());
    }
}
