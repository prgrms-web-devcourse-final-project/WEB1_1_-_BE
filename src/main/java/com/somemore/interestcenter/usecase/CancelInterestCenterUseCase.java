package com.somemore.interestcenter.usecase;

import java.util.UUID;

public interface CancelInterestCenterUseCase {
    void cancelInterestCenter(UUID volunteerId, UUID centerId);
}
