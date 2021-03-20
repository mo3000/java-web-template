package com.toy.artifact.utils.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtWrapper {

    @Value("${app.admin.jwt.key}")
    private String secret;

    private DecodedJWT token;

    public String sign(Long userid) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var now = new Date();
        return JWT.create()
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(now.getTime() + 3600 * 24 * 30))
            .withClaim("userid", userid)
            .sign(algorithm);
    }

    public void verify(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
            .build();
        token = verifier.verify(jwtToken);
    }

    public DecodedJWT getToken() {
        return token;
    }

    public Long getUserid() {
        var claim = token.getClaim("userid");
        if (claim.isNull()) {
            throw new RuntimeException("jwt token doesn't have userid");
        }
        return claim.asLong();
    }

    public boolean isTokenSet() {
        return ! token.getClaim("userid").isNull();
    }
}
