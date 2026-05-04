package dev.ismoil.chat_service.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ClientRegistrationRepository clientRegistrationRepository
    ) throws Exception {

        OAuth2AuthorizationRequestResolver kakaoResolver =
                kakaoAuthorizationRequestResolver(clientRegistrationRepository);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/websocket.js",
                                "/main.css",
                                "/favicon.ico",
                                "/error",
                                "/oauth2/**",
                                "/login/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(kakaoResolver)
                        )
                        .defaultSuccessUrl("/index.html", true)
                );

        return http.build();
    }

    private OAuth2AuthorizationRequestResolver kakaoAuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        clientRegistrationRepository,
                        "/oauth2/authorization"
                );

        return new OAuth2AuthorizationRequestResolver() {

            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                OAuth2AuthorizationRequest authorizationRequest =
                        defaultResolver.resolve(request);
                return customizeAuthorizationRequest(authorizationRequest);
            }

            @Override
            public OAuth2AuthorizationRequest resolve(
                    HttpServletRequest request,
                    String clientRegistrationId
            ) {
                OAuth2AuthorizationRequest authorizationRequest =
                        defaultResolver.resolve(request, clientRegistrationId);
                return customizeAuthorizationRequest(authorizationRequest);
            }

            private OAuth2AuthorizationRequest customizeAuthorizationRequest(
                    OAuth2AuthorizationRequest authorizationRequest
            ) {
                if (authorizationRequest == null) {
                    return null;
                }

                Map<String, Object> additionalParameters =
                        new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());

                additionalParameters.put("prompt", "login");

                return OAuth2AuthorizationRequest.from(authorizationRequest)
                        .additionalParameters(additionalParameters)
                        .build();
            }
        };
    }
}