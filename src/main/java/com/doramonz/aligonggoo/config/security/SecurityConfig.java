package com.doramonz.aligonggoo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable).logout(logout -> logout.logoutSuccessUrl("/index"));
        http.oauth2Login(oauth -> oauth.successHandler((request, response, authentication) -> {
            response.sendRedirect("/index");
        }).loginPage("/login"));
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}
