package com.somemore.volunteer.service;

import com.somemore.global.exception.ExceptionMessage;
import com.somemore.global.exception.NoSuchElementException;
import com.somemore.volunteer.domain.NEWVolunteer;
import com.somemore.volunteer.repository.NEWVolunteerRepository;
import com.somemore.volunteer.usecase.NEWVolunteerQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NEWVolunteerQueryService implements NEWVolunteerQueryUseCase {

    private final NEWVolunteerRepository volunteerRepository;

    @Override
    public NEWVolunteer getByUserId(UUID userId) {
        return volunteerRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(ExceptionMessage.NOT_EXISTS_VOLUNTEER));
    }

    @Override
    public UUID getIdByUserId(UUID userId) {
        return getByUserId(userId).getId();
    }
}
