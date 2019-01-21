package org.techtown.just.model;

public class LoginToken {
    private String accessToken;
    private String idToken;
    private String refreshToken;


    public String getAccessToken() {
        return accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * LoginToken은 setter는 구현하지 않습니다.
     */
}
