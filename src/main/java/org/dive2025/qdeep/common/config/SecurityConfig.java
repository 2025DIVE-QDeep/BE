package org.dive2025.qdeep.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dive2025.qdeep.common.security.filter.JwtFilter;
import org.dive2025.qdeep.common.security.filter.JwtLogoutFilter;
import org.dive2025.qdeep.common.security.filter.LoginFilter;
import org.dive2025.qdeep.common.security.service.ReissueService;
import org.dive2025.qdeep.common.security.util.JwtUtil;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final ReissueService reissueService;
    private final AntPathMatcher antPathMatcher;
    private final UserRepository userRepository;

    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager)
            throws Exception{
        return new LoginFilter(objectMapper,
                authenticationManager,
                jwtUtil,
                reissueService);
    }


    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(jwtUtil,
                objectMapper,
                antPathMatcher,
                userRepository);
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtLogoutFilter jwtLogoutFilter() throws Exception{
        return new JwtLogoutFilter(jwtUtil,reissueService);
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration)
            throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   LoginFilter loginFilter,
                                                   JwtFilter jwtFilter,
                                                   JwtLogoutFilter jwtLogoutFilter)
            throws Exception {

        httpSecurity
                .csrf((auth)->auth.disable())
                .formLogin((auth)->auth.disable())
                .httpBasic((auth)->auth.disable());

        httpSecurity.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtFilter, LoginFilter.class);
        httpSecurity.addFilterBefore(jwtLogoutFilter, LogoutFilter.class);

        httpSecurity.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests((auth)->auth
                .requestMatchers("/login", "/user/create", "/user/check-nickname", "/user/check-username").permitAll()
                .requestMatchers("/refresh").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
        );

        httpSecurity.cors((corsCustomizer)->corsCustomizer.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);
            return configuration;
        }));

        return httpSecurity.build();
    }
}
