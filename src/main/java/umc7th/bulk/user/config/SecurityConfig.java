package umc7th.bulk.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/oauth2/**", "/user/**", "/test/**").permitAll()
                .anyRequest().authenticated()
        );

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2LoginSuccessHandler) // 핸들러 등록
                .permitAll()
        );

        return http.build();
    }
}
