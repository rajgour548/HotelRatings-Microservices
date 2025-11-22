package com.raj.hotel.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // GET requests require manage:user-ratings
                        .requestMatchers(HttpMethod.GET, "/hotels/**").hasAuthority("manage:user-ratings")

                        // POST, PUT, DELETE require manage:hotel-user-ratings
                        .requestMatchers(HttpMethod.POST, "/hotels/**").hasAuthority("manage:hotel-user-ratings")
                        .requestMatchers(HttpMethod.PUT, "/hotels/**").hasAuthority("manage:hotel-user-ratings")
                        .requestMatchers(HttpMethod.DELETE, "/hotels/**").hasAuthority("manage:hotel-user-ratings")

                        // Any other request must be authenticated
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}
