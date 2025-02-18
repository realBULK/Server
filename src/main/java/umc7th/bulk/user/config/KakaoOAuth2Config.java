package umc7th.bulk.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Configuration
//@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
public class KakaoOAuth2Config {

    private String clientId = "9555ae675b3e27488bd10e7f09ea12bd";

    private String clientSecret = "V1LPr6eQPAHRTKIcmeXczG63dkdlaUrF";

    private String redirectUri = "https://bulkapp.site/home";
}
