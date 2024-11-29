package com.somemore.location.service.command;

import static com.somemore.global.exception.ExceptionMessage.NOT_EXISTS_LOCATION;

import com.somemore.global.exception.BadRequestException;
import com.somemore.location.domain.Location;
import com.somemore.location.dto.request.LocationUpdateRequestDto;
import com.somemore.location.repository.LocationRepository;
import com.somemore.location.usecase.command.UpdateLocationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateLocationService implements UpdateLocationUseCase {

    private final LocationRepository locationRepository;

    @Override
    public void updateLocation(LocationUpdateRequestDto requestDto, Long locationId) {
        Location location = getLocation(locationId);
        location.updateWith(requestDto);
        locationRepository.save(location);
    }

    private Location getLocation(Long locationId) {
        return locationRepository.findById(locationId)
            .orElseThrow(() -> new BadRequestException(NOT_EXISTS_LOCATION.getMessage()));
    }
}
