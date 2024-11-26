package com.somemore.location.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.location.domain.Location;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LocationRepositoryImpl implements LocationRepository {

    private final LocationJpaRepository locationJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Location save(Location location) {
        return locationJpaRepository.save(location);
    }

    @Override
    public Location saveAndFlush(Location location) {
        return locationJpaRepository.saveAndFlush(location);
    }

    @Override
    public Optional<Location> findById(Long id) {
        return locationJpaRepository.findById(id);
    }

    @Override
    public void deleteAllInBatch() {
        locationJpaRepository.deleteAllInBatch();
    }
}
