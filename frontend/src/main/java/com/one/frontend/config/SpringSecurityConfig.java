package com.one.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.one.frontend.config.security.JwtAuthenticationFilter;
import com.one.frontend.config.security.JwtTokenProvider;
import com.one.frontend.config.security.oauth2.CustomOAuth2UserService;
import com.one.frontend.config.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.one.frontend.config.security.oauth2.OAuth2AuthenticationSuccessHandler;

import lombok.AllArgsConstructor;

import com.one.frontend.config.security.JwtAuthenticationFilter;
import com.one.frontend.config.security.JwtTokenProvider;
import com.one.frontend.config.security.oauth2.CustomOAuth2UserService;
import com.one.frontend.config.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.one.frontend.config.security.oauth2.OAuth2AuthenticationSuccessHandler;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {
	
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
//                .requestMatchers("/auth/login" , "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**" , "/product/**" , "/productDetail/**" ).permitAll()
//                .requestMatchers("/draw" , "/user").authenticated()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login(oauth2Login -> oauth2Login
						.userInfoEndpoint(
								userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
						.successHandler(oAuth2AuthenticationSuccessHandler)
						.failureHandler(oAuth2AuthenticationFailureHandler))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
