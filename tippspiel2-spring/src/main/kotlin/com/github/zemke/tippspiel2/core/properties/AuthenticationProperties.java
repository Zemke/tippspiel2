package com.github.zemke.tippspiel2.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tippspiel2.auth")
public class AuthenticationProperties {

    private String bcryptSalt;
    private JsonWebTokenProperties jwt;

    public String getBcryptSalt() {
        return bcryptSalt;
    }

    public void setBcryptSalt(String bcryptSalt) {
        this.bcryptSalt = bcryptSalt;
    }

    public JsonWebTokenProperties getJwt() {
        return jwt;
    }

    public void setJwt(JsonWebTokenProperties jwt) {
        this.jwt = jwt;
    }

    public static class JsonWebTokenProperties {

        private String header;
        private String secret;
        private Integer expiration;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Integer getExpiration() {
            return expiration;
        }

        public void setExpiration(Integer expiration) {
            this.expiration = expiration;
        }
    }
}
