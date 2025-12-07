package com.guidex.framework.security.thirdparty;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.guidex.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;

/**
 * @author kled2
 * @date 2025/6/17
 */
@Slf4j
@Component
public class AppleTokenVerifier {
    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";
    private static final String ISSUER = "https://appleid.apple.com";
    private static final String AUDIENCE = "com.guidex"; // todo 替换成你的 iOS App Bundle ID

    public AppleUserInfo verify(String identityToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            JWSHeader header = signedJWT.getHeader();
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // 验证签名
            if (!verifySignature(signedJWT, header)) {
                log.warn("Apple token 签名无效");
                return null;
            }

            // 验证一些标准字段
            if (!ISSUER.equals(claims.getIssuer())) {
                log.warn("issuer 不匹配");
                return null;
            }

            if (!claims.getAudience().contains(AUDIENCE)) {
                log.warn("audience 不匹配");
                return null;
            }

            if (claims.getExpirationTime().before(new Date())) {
                log.warn("token 已过期");
                return null;
            }

            // 提取用户唯一 ID 和邮箱
            String sub = claims.getSubject();
            String email = claims.getStringClaim("email"); // 注意：仅首次登录提供

            return new AppleUserInfo(sub, email);

        } catch (Exception e) {
            log.error("验证 Apple token 失败", e);
            throw new ServiceException("Apple token 验证异常");
        }
    }

    private boolean verifySignature(SignedJWT signedJWT, JWSHeader header) throws Exception {
        JWKSet jwkSet = JWKSet.load(new URL(APPLE_PUBLIC_KEYS_URL));
        List<JWK> keys = jwkSet.getKeys();

        for (JWK jwk : keys) {
            if (jwk.getKeyID().equals(header.getKeyID())) {
                RSAKey rsaKey = (RSAKey) jwk;
                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
                JWSVerifier verifier = new RSASSAVerifier(publicKey);
                return signedJWT.verify(verifier);
            }
        }
        return false;
    }
}
