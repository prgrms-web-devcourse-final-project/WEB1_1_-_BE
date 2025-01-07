package com.somemore.support.fixture;

import com.somemore.domains.recruitboard.domain.RecruitBoard;
import com.somemore.domains.recruitboard.domain.RecruitStatus;
import com.somemore.domains.recruitboard.domain.RecruitmentInfo;
import com.somemore.domains.recruitboard.domain.VolunteerCategory;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.somemore.domains.recruitboard.domain.RecruitStatus.CLOSED;
import static com.somemore.domains.recruitboard.domain.RecruitStatus.COMPLETED;
import static com.somemore.domains.recruitboard.domain.VolunteerCategory.OTHER;
import static com.somemore.support.fixture.LocalDateTimeFixture.createStartDateTime;

public class RecruitBoardFixture {

    private static final String REGION = "경기";
    private static final int RECRUITMENT_COUNT = 1;
    private static final LocalDateTime START_DATE_TIME = createStartDateTime();
    private static final LocalDateTime END_DATE_TIME = START_DATE_TIME.plusHours(1);
    private static final Integer VOLUNTEER_HOURS = 1;
    private static final boolean ADMITTED = true;
    private static final long LOCATION_ID = 1L;
    private static final String TITLE = "봉사모집제목";
    private static final String CONTENT = "봉사모집내용";
    private static final String IMG_URL = "https://image.domain.com/links";
    private static final VolunteerCategory VOLUNTEER_CATEGORY = OTHER;

    private RecruitBoardFixture() {
    }

    public static RecruitBoard createRecruitBoard() {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String title) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String title, UUID centerId, Long locationId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(locationId)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String title, UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(VolunteerCategory category, UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(category)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(Boolean admitted, UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(admitted)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(Long locationId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(locationId)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(UUID centerId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(UUID centerId, Long locationId) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(centerId)
                .locationId(locationId)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(String region,
                                                  VolunteerCategory volunteerCategory) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(region)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(volunteerCategory)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(Long locationId, String title) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(locationId)
                .title(title)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createRecruitBoard(LocalDateTime start, LocalDateTime end) {

        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(start)
                .volunteerEndDateTime(end)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(VOLUNTEER_CATEGORY)
                .admitted(ADMITTED)
                .build();

        return RecruitBoard.builder()
                .centerId(UUID.randomUUID())
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();
    }

    public static RecruitBoard createCompletedRecruitBoard(UUID centerId,
                                                           VolunteerCategory category) {
        RecruitmentInfo recruitmentInfo = RecruitmentInfo.builder()
                .region(REGION)
                .recruitmentCount(RECRUITMENT_COUNT)
                .volunteerStartDateTime(START_DATE_TIME)
                .volunteerEndDateTime(END_DATE_TIME)
                .volunteerHours(VOLUNTEER_HOURS)
                .volunteerCategory(category)
                .admitted(ADMITTED)
                .build();

        RecruitBoard recruitBoard = RecruitBoard.builder()
                .centerId(centerId)
                .locationId(LOCATION_ID)
                .title(TITLE)
                .content(CONTENT)
                .imgUrl(IMG_URL)
                .recruitmentInfo(recruitmentInfo)
                .build();

        setRecruitStatus(recruitBoard, COMPLETED);

        return recruitBoard;
    }

    public static RecruitBoard createCompletedRecruitBoard(VolunteerCategory category) {
        RecruitBoard recruitBoard = createCompletedRecruitBoard(UUID.randomUUID(), category);
        setRecruitStatus(recruitBoard, COMPLETED);
        return recruitBoard;
    }

    public static RecruitBoard createCompletedRecruitBoard() {
        RecruitBoard recruitBoard = createCompletedRecruitBoard(UUID.randomUUID(),
                VOLUNTEER_CATEGORY);
        setRecruitStatus(recruitBoard, COMPLETED);
        return recruitBoard;
    }

    public static RecruitBoard createCloseRecruitBoard() {
        RecruitBoard recruitBoard = createCompletedRecruitBoard(UUID.randomUUID(),
                VOLUNTEER_CATEGORY);
        setRecruitStatus(recruitBoard, CLOSED);
        return recruitBoard;
    }

    private static void setRecruitStatus(RecruitBoard recruitBoard, RecruitStatus status) {
        try {
            Field recruitStatusField = RecruitBoard.class.getDeclaredField("recruitStatus");
            recruitStatusField.setAccessible(true); // private 필드 접근 가능 설정
            recruitStatusField.set(recruitBoard, status); // 필드 값 설정
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("리플렉션으로 recruitStatus를 설정하는 것에 실패했습니다", e);
        }
    }
}
