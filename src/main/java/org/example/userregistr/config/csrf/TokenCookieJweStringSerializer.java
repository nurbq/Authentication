package org.example.userregistr.config.csrf;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Date;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userregistr.model.auth.Token;


@Slf4j
@RequiredArgsConstructor
public class TokenCookieJweStringSerializer implements Function<Token, String> {

    private final EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;
    private final JWEEncrypter jweEncrypter;
    private final JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    @Override
    public String apply(Token token) {
        JWEHeader jweHeader = new JWEHeader.Builder(this.jweAlgorithm, this.encryptionMethod)
                .keyID(token.id().toString())
                .build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        EncryptedJWT encryptedJWT = new EncryptedJWT(jweHeader, jwtClaimsSet);
        try {
            encryptedJWT.encrypt(this.jweEncrypter);

            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
