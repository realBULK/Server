package umc7th.bulk.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf-> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/login","/swagger-ui/**","/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(Customizer.withDefaults());

        return http.build();
    }

}
