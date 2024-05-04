package org.example.userregistr.config.security.jwt.deserializers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.userregistr.model.jwt.Token;

@RequiredArgsConstructor
//@Setter
@Slf4j
public class AccessTokenJwsStringDeserializer implements Function<String, Token> {

    private final JWSVerifier verifier;
//    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    @Override
    public Token apply(String string) {
        try {
            var signedJWT = SignedJWT.parse(string);
            if (signedJWT.verify(this.verifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(
                        UUID.fromString(claimsSet.getJWTID()),
                        claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant()
                );
            }
        } catch (ParseException | JOSEException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
