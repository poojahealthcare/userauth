package com.hca.oauth.common;

public class Constants {

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    public static final String SIGNING_KEY = "madirajumds";
    public static final String TOKEN_PARAM = "auth_token";
    public static final String homeUrl = "http://localhost:8080/validated/login";
    public static final String changepwd = "http://localhost:8000/auth/changepwd?code=";
}
