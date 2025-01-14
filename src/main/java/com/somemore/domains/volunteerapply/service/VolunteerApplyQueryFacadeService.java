package com.somemore.domains.volunteerapply.service;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.service.validator.RecruitBoardValidator;
import com.somemore.domains.recruitboard.usecase.RecruitBoardQueryUseCase;
import com.somemore.domains.review.usecase.ReviewQueryUseCase;
import com.somemore.domains.volunteer.repository.mapper.VolunteerSimpleInfo;
import com.somemore.domains.volunteer.usecase.VolunteerQueryUseCase;
import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyRecruitInfoResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyWithReviewStatusResponseDto;
import com.somemore.domains.volunteerapply.dto.response.VolunteerApplyVolunteerInfoResponseDto;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryFacadeUseCase;
import com.somemore.domains.volunteerapply.usecase.VolunteerApplyQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VolunteerApplyQueryFacadeService implements VolunteerApplyQueryFacadeUseCase {

    private final VolunteerApplyQueryUseCase volunteerApplyQueryUseCase;
    private final RecruitBoardQueryUseCase recruitBoardQueryUseCase;
    private final VolunteerQueryUseCase volunteerQueryUseCase;
    private final ReviewQueryUseCase reviewQueryUseCase;
    private final RecruitBoardValidator recruitBoardValidator;

    @Override
    public VolunteerApplyWithReviewStatusResponseDto getVolunteerApplyByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId) {
        VolunteerApply apply = volunteerApplyQueryUseCase.getByRecruitIdAndVolunteerId(recruitId, volunteerId);
        boolean isReviewed = checkIfReviewed(apply);

        return VolunteerApplyWithReviewStatusResponseDto.of(apply, isReviewed);
    }

    @Override
    public Page<VolunteerApplyVolunteerInfoResponseDto> getVolunteerAppliesByRecruitIdAndCenterId(
            Long recruitId, UUID centerId, VolunteerApplySearchCondition condition) {
        RecruitBoard recruitBoard = recruitBoardQueryUseCase.getById(recruitId);
        recruitBoardValidator.validateWriter(recruitBoard, centerId);

        Page<VolunteerApply> applies = volunteerApplyQueryUseCase.getAllByRecruitId(recruitId, condition);
        Map<UUID, VolunteerSimpleInfo> volunteerMap = getVolunteerInfoMap(applies);

        return applies.map(
                apply -> VolunteerApplyVolunteerInfoResponseDto.of(apply, volunteerMap.getOrDefault(apply.getVolunteerId(), null)));
    }

    @Override
    public Page<VolunteerApplyRecruitInfoResponseDto> getVolunteerAppliesByVolunteerId(
            UUID volunteerId, VolunteerApplySearchCondition condition) {

        Page<VolunteerApply> applies = volunteerApplyQueryUseCase.getAllByVolunteerId(volunteerId, condition);
        Map<Long, RecruitBoard> boardMap = getRecruitBoardMap(applies);

        return applies.map(
                apply -> VolunteerApplyRecruitInfoResponseDto.of(apply, boardMap.getOrDefault(apply.getRecruitBoardId(), null)));
    }

    private boolean checkIfReviewed(VolunteerApply apply) {
        return apply.isVolunteerActivityCompleted()
                && reviewQueryUseCase.existsByVolunteerApplyId(apply.getId());
    }

    private Map<Long, RecruitBoard> getRecruitBoardMap(Page<VolunteerApply> applies) {
        List<Long> boardIds = applies.getContent().stream()
                .map(VolunteerApply::getRecruitBoardId)
                .toList();
        List<RecruitBoard> boards = recruitBoardQueryUseCase.getAllByIds(boardIds);

        return boards.stream()
                .collect(Collectors.toMap(RecruitBoard::getId,
                        board -> board));
    }

    private Map<UUID, VolunteerSimpleInfo> getVolunteerInfoMap(Page<VolunteerApply> applies) {
        List<UUID> volunteerIds = applies.getContent().stream()
                .map(VolunteerApply::getVolunteerId)
                .toList();

        List<VolunteerSimpleInfo> volunteers = volunteerQueryUseCase.getVolunteerSimpleInfosByIds(volunteerIds);

        return volunteers.stream()
                .collect(Collectors.toMap(VolunteerSimpleInfo::id, volunteer -> volunteer));
    }

}
