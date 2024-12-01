package com.somemore.volunteerapply.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.volunteerapply.domain.QVolunteerApply;
import com.somemore.volunteerapply.domain.VolunteerApply;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VolunteerApplyRepositoryImpl implements VolunteerApplyRepository {

    private final VolunteerApplyJpaRepository volunteerApplyJpaRepository;
    private final JPAQueryFactory queryFactory;

    private static final QVolunteerApply volunteerApply = QVolunteerApply.volunteerApply;

    @Override
    public VolunteerApply save(VolunteerApply volunteerApply) {
        return volunteerApplyJpaRepository.save(volunteerApply);
    }

    @Override
    public Optional<VolunteerApply> findById(Long id) {
        return findOne(volunteerApply.id.eq(id));
    }

    @Override
    public List<UUID> findVolunteerIdsByRecruitIds(List<Long> recruitIds) {

        BooleanExpression exp = volunteerApply.recruitBoardId
                .in(recruitIds)
                .and(isNotDeleted());

        return queryFactory
                .select(volunteerApply.volunteerId)
                .from(volunteerApply)
                .where(exp)
                .fetch();
    }

    @Override
    public Page<VolunteerApply> findAllByRecruitId(Long recruitId, Pageable pageable) {

        BooleanExpression exp = volunteerApply.recruitBoardId
                .eq(recruitId)
                .and(isNotDeleted());

        List<VolunteerApply> content = queryFactory
                .selectFrom(volunteerApply)
                .where(exp)
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(
                content,
                pageable,
                getCount(exp)
        );
    }

    @Override
    public Optional<VolunteerApply> findByRecruitIdAndVolunteerId(Long recruitId,
            UUID volunteerId) {
        BooleanExpression exp = volunteerApply.recruitBoardId.eq(recruitId)
                .and(volunteerApply.volunteerId.eq(volunteerId));
        return findOne(exp);
    }

    private Long getCount(BooleanExpression exp) {
        return queryFactory
                .select(volunteerApply.count())
                .from(volunteerApply)
                .where(exp)
                .fetchOne();
    }

    private Optional<VolunteerApply> findOne(BooleanExpression condition) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(volunteerApply)
                        .where(
                                condition,
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    private BooleanExpression isNotDeleted() {
        return volunteerApply.deleted.isFalse();
    }

    private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort) {

        return sort.stream()
                .map(order -> {
                    String property = order.getProperty();

                    if ("created_at".equals(property)) {
                        return order.isAscending()
                                ? volunteerApply.createdAt.asc()
                                : volunteerApply.createdAt.desc();
                    } else {
                        throw new IllegalStateException("Invalid sort property: " + property);
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }
}
