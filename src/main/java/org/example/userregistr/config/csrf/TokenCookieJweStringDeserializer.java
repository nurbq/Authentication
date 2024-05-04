package org.example.userregistr.config.csrf;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userregistr.model.auth.Token;

@Slf4j
@RequiredArgsConstructor
public class TokenCookieJweStringDeserializer implements Function<String, Token> {

    private final JWEDecrypter jweDecrypter;

    @Override
    public Token apply(String string) {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(string);
            encryptedJWT.decrypt(this.jweDecrypter);
            JWTClaimsSet claimsSet = encryptedJWT.getJWTClaimsSet();
            return new Token(UUID.fromString(claimsSet.getJWTID()),
                    claimsSet.getSubject(),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
