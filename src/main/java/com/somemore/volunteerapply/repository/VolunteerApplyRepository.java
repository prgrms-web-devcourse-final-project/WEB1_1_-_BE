package com.somemore.volunteerapply.repository;

import com.somemore.volunteerapply.domain.VolunteerApply;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VolunteerApplyRepository {

    VolunteerApply save(VolunteerApply volunteerApply);
    Optional<VolunteerApply> findById(Long id);
    List<UUID> findVolunteerIdsByRecruitIds(List<Long> recruitIds);
    Page<VolunteerApply> findAllByRecruitId(Long recruitId, Pageable pageable);
    Optional<VolunteerApply> findByRecruitIdAndVolunteerId(Long recruitId, UUID volunteerId);
}
