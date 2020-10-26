package edu.exam.client.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Authenticator {
    private String username;
    private boolean isAuthorized;
    @Builder.Default
    private String token = "";

    public void authorize(String username, String token) {
        this.isAuthorized = true;
        this.username = username;
        this.token = token;
    }

    public void deauthorize() {
        this.username = null;
        this.isAuthorized = false;
        this.token = "";
    }
}
