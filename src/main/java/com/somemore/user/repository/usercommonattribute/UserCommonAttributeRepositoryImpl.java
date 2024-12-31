package com.somemore.user.repository.usercommonattribute;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.somemore.user.domain.QUserCommonAttribute;
import com.somemore.user.domain.UserCommonAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
                        .fetchOne());
    }

    @Override
    public UserCommonAttribute save(UserCommonAttribute userCommonAttribute) {
        return userCommonAttributeJpaRepository.save(userCommonAttribute);
    }
}
