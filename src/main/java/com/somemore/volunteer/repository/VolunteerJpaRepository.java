package com.somemore.volunteer.repository;

import com.somemore.volunteer.domain.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerJpaRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByOauthId(String oauthId);
}
