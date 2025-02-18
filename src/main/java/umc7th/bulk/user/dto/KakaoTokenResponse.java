package umc7th.bulk.user.dto;


import lombok.Getter;

@Getter
public class KakaoTokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
    private int refresh_token_expires_in;

    public KakaoTokenResponse(String access_token, String refresh_token, String token_type, int expires_in, int refresh_token_expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
