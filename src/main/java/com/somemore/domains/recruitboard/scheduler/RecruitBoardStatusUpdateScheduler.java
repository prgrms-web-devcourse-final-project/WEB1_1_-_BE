package com.somemore.domains.recruitboard.scheduler;

import com.somemore.domains.recruitboard.repository.RecruitBoardRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class RecruitBoardStatusUpdateScheduler {

    private final RecruitBoardRepository recruitBoardRepository;

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateRecruitBoardStatusToClosed() {
        log.info("봉사 시작일에 해당하는 모집글 상태를 CLOSED로 변경하는 작업 시작");
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime startOfNextDay = LocalDate.now().plusDays(1).atStartOfDay();

        try {
            long updatedCount = recruitBoardRepository.updateRecruitingToClosedByStartDate(
                    startOfDay, startOfNextDay);
            log.info("총 {}개의 모집글 상태를 CLOSED로 변경 완료", updatedCount);
        } catch (Exception e) {
            log.error("봉사 시작일에 해당하는 모집글 상태를 CLOSED로 변경하는 중 오류 발생", e);
            throw e;
        }
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateRecruitBoardStatusToCompleted() {
        log.info("봉사 종료일이 지난 모집글 상태를 COMPLETED로 변경하는 작업 시작");
        LocalDateTime now = LocalDateTime.now();

        try {
            long updatedCount = recruitBoardRepository.updateClosedToCompletedByEndDate(now);
            log.info("총 {}개의 모집글 상태를 COMPLETED로 변경 완료", updatedCount);
        } catch (Exception e) {
            log.error("봉사 종료일이 지난 모집글 상태를 COMPLETED로 변경하는 중 오류 발생", e);
            throw e;
        }
    }

}
