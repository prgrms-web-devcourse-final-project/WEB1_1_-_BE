package com.somemore.global.auth.jwt.refresher;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.auth.jwt.domain.EncodedToken;
import com.somemore.global.auth.jwt.domain.RefreshToken;
import com.somemore.global.auth.jwt.domain.TokenType;
import com.somemore.global.auth.jwt.generator.JwtGenerator;
import com.somemore.global.auth.jwt.manager.TokenManager;
import com.somemore.global.auth.jwt.parser.JwtParser;
import com.somemore.global.auth.jwt.validator.JwtValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultJwtRefresher implements JwtRefresher {

    private final TokenManager tokenManager;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final JwtGenerator jwtGenerator;

    @Override
    public EncodedToken refreshAccessToken(EncodedToken accessToken) {
        RefreshToken refreshToken = tokenManager.getRefreshTokenByAccessToken(accessToken);
        EncodedToken refreshTokenValue = EncodedToken.from(refreshToken.getRefreshToken());
        jwtValidator.validateToken(refreshTokenValue);

        Claims claims = jwtParser.parseToken(refreshTokenValue);
        EncodedToken newAccessToken = generateAccessToken(claims);
        refreshToken.updateAccessToken(newAccessToken);
        tokenManager.save(refreshToken);

        return EncodedToken.from(refreshToken.getAccessToken());
    }

    private EncodedToken generateAccessToken(Claims claims) {
        return jwtGenerator.generateToken(
                UserIdentity.from(claims),
                TokenType.ACCESS
        );
    }
}
