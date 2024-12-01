package com.somemore.volunteer.service;

import com.somemore.global.exception.BadRequestException;
import com.somemore.volunteer.domain.Volunteer;
import com.somemore.volunteer.dto.request.VolunteerProfileUpdateRequestDto;
import com.somemore.volunteer.repository.VolunteerRepository;
import com.somemore.volunteer.usecase.UpdateVolunteerProfileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_VOLUNTEER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateVolunteerProfileService implements UpdateVolunteerProfileUseCase {

    private final VolunteerRepository volunteerRepository;

    @Override
    public void update(UUID volunteerId, VolunteerProfileUpdateRequestDto requestDto, String imgUrl) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new BadRequestException(NOT_EXISTS_VOLUNTEER));

        volunteer.updateWith(requestDto, imgUrl);
    }

}
