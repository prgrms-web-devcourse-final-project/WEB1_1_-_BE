package com.somemore.user.repository.usercommonattribute;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.user.domain.QUserCommonAttribute;
import com.somemore.user.domain.UserCommonAttribute;
import com.somemore.user.repository.usercommonattribute.record.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserCommonAttributeRepositoryImpl implements UserCommonAttributeRepository {

    private final JPAQueryFactory queryFactory;
    private final UserCommonAttributeJpaRepository userCommonAttributeJpaRepository;

    private static final QUserCommonAttribute userCommonAttribute = QUserCommonAttribute.userCommonAttribute;

    @Override
    public Optional<UserCommonAttribute> findByUserId(UUID userId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(userCommonAttribute)
                        .where(
                                eqUserId(userId),
                                isNotDeleted())
                        .fetchOne());
    }

    @Override
    public UserCommonAttribute save(UserCommonAttribute userCommonAttribute) {
        return userCommonAttributeJpaRepository.save(userCommonAttribute);
    }

    @Override
    public Optional<Boolean> findIsCustomizedByUserId(UUID userId) {
        return Optional.ofNullable(
                queryFactory.select(userCommonAttribute.isCustomized)
                        .from(userCommonAttribute)
                        .where(
                                eqUserId(userId),
                                isNotDeleted())
                        .fetchOne()
        );
    }

    @Override
    public Optional<UserProfileDto> findUserProfileByUserId(UUID userId) {
        return Optional.ofNullable(
                queryFactory.select(Projections.constructor(UserProfileDto.class,
                                userCommonAttribute.id,
                                userCommonAttribute.userId,
                                userCommonAttribute.name,
                                userCommonAttribute.contactNumber,
                                userCommonAttribute.imgUrl,
                                userCommonAttribute.introduce
                        ))
                        .from(userCommonAttribute)
                        .where(
                                eqUserId(userId),
                                isNotDeleted()
                        )
                        .fetchOne()
        );
    }

    public List<UserCommonAttribute> findAllByUserIds(List<UUID> userIds) {
        return queryFactory
                .selectFrom(userCommonAttribute)
                .where(
                        inUserIds(userIds),
                        isNotDeleted())
                .fetch();
    }

    private static BooleanExpression eqUserId(UUID userId) {
        return userCommonAttribute.userId.eq(userId);
    }

    private static BooleanExpression isNotDeleted() {
        return userCommonAttribute.deleted.eq(false);
    }

    private static BooleanExpression inUserIds(List<UUID> userIds) {
        return userCommonAttribute.userId.in(userIds);
    }

}
