package umc7th.bulk.user.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import umc7th.bulk.global.jwt.filter.JwtFilter;
import umc7th.bulk.global.jwt.handler.JwtAccessDeniedHandler;
import umc7th.bulk.global.jwt.handler.JwtAuthenticationEntryPoint;
import umc7th.bulk.global.jwt.util.JwtProvider;
import umc7th.bulk.user.principal.PrincipalDetailsService;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // 인증을 허용할 URL 배열
    private final String[] allowUrl = {
            "/swagger-ui/**",
            "/oauth2/**",
            "/v3/api-docs/**",
            "/api/user/unlink",
            "/test/**",
            "/api/user/question/isDuplicated/**",
            "/api/user/token",
            "/api/auth/kakao/**",
            "/api/**"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF 보안을 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                // 기본 폼 로그인 기능을 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 기본 HTTP Basic 인증을 비활성화
                .httpBasic(HttpBasicConfigurer::disable)
                // 인증 및 권한 설정
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll() // 지정된 URL은 접근 허용
                        .anyRequest().authenticated()) // 그 외의 모든 요청은 인증 요구
                .addFilterBefore(new CorsFilter(corsConfigurationSource()), UsernamePasswordAuthenticationFilter.class)
                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가하여 인증 절차 진행
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                // 예외 처리 설정: 접근 거부 시 및 인증 실패 시 핸들러 지정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // 접근 거부 시 처리할 핸들러 : jwtAccessDeniedHandler
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        // 인증 실패 시 처리할 핸들러 : jwtAuthenticationEntryPoint
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        ;

        return http.build(); // 이렇게 필터 체인 생성해서 반환!
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000", "https://bulkapp.site", "https://d18yt6eyo83jtp.cloudfront.net"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean // JWTFilter를 빈으로 등록
    // JWT 필터에 jwtProvider와 principalDetailsService 주입
    public Filter jwtFilter() {
        return new JwtFilter(jwtProvider, principalDetailsService);
    }

    @Bean // 비밀번호 암호화를 위한 PasswordEncoder 를 빈으로 등록
    // BCryptPasswordEncoder를 사용하여 비밀번호 암호화
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        // CORS 설정 추가
//        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.IF_REQUIRED)));


        // CORS 필터를 Spring Security 필터보다 먼저 실행하도록 설정
//        http.addFilterBefore(new CorsFilter(corsConfigurationSource()), ChannelProcessingFilter.class);
//
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/oauth2/**", "/api/user/unlink", "/test/**", "/api/user/question/isDuplicated/**", "/api/user/token", "/api/auth/kakao/**","/api/**").permitAll()
//                .anyRequest().authenticated()
//        );
//
//        // OAuth2 로그인 설정
//        http.oauth2Login(oauth2 -> oauth2
//                .successHandler(oAuth2LoginSuccessHandler) // 핸들러 등록
//                .permitAll()
//        );
//
//        return http.build();
//    }

//    // CORS 설정 메서드
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(List.of(
//                "http://localhost:3000",
//                "https://bulkapp.site"
//        ));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
//        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
//        configuration.setAllowCredentials(true); // 쿠키 포함 여부
//
//        // Preflight 요청을 허용하기 위해 Expose Headers 추가
//        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
//
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 적용
//        return source;
//    }
}
