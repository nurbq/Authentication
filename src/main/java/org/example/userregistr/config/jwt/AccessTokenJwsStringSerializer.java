package org.example.userregistr.config.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Setter
@Slf4j
public class AccessTokenJwsStringSerializer implements Function<Token, String> {

    private final JWSSigner signer;
    private JWSAlgorithm algorithm =JWSAlgorithm.HS256;

    @Override
    public String apply(Token token) {
        JWSHeader jwsHeader = new JWSHeader.Builder(this.algorithm)
                .keyID(token.id().toString())
                .build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        var signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(this.signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
//            throw new RuntimeException(e);
        }
        return null;
    }
}
