package com.somemore.domains.volunteerapply.repository;

import com.somemore.domains.volunteerapply.domain.VolunteerApply;
import com.somemore.domains.volunteerapply.dto.condition.VolunteerApplySearchCondition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VolunteerApplyRepository {

    VolunteerApply save(VolunteerApply volunteerApply);

    List<VolunteerApply> saveAll(List<VolunteerApply> volunteerApplies);

    Optional<VolunteerApply> findById(Long id);

    Optional<VolunteerApply> findByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);

    Optional<Long> findRecruitBoardIdById(Long volunteerApplyId);

    boolean existsByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);

    List<UUID> findVolunteerIdsByRecruitIds(List<Long> recruitIds);

    Page<VolunteerApply> findAllByRecruitId(Long recruitId, Pageable pageable);

    List<VolunteerApply> findAllByRecruitId(Long recruitId);

    Page<VolunteerApply> findAllByRecruitId(Long recruitId,
            VolunteerApplySearchCondition condition);

    Page<VolunteerApply> findAllByVolunteerId(UUID volunteerId,
            VolunteerApplySearchCondition condition);

    List<VolunteerApply> findAllByIds(List<Long> ids);

}
