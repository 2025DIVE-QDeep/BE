package org.dive2025.qdeep.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.common.security.filter.JwtFilter;
import org.dive2025.qdeep.common.security.filter.JwtLogoutFilter;
import org.dive2025.qdeep.common.security.filter.LoginFilter;
import org.dive2025.qdeep.common.security.service.AuthService;
import org.dive2025.qdeep.common.security.service.ReissueService;
import org.dive2025.qdeep.common.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Bean
    public AntPathMatcher antPathMatcher(){
        return new AntPathMatcher();
    }


    @Bean
    public LoginFilter loginFilter()
            throws Exception{
        return new LoginFilter(objectMapper,
                authenticationManager(authenticationConfiguration),
                jwtUtil,
                reissueService);
    }


    @Bean
    public JwtFilter jwtFilter(){
        return new JwtFilter(jwtUtil,
                objectMapper,
                antPathMatcher());
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
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain
            (HttpSecurity httpSecurity)
            throws Exception{

        httpSecurity
                .csrf((auth)->auth.disable());
        httpSecurity
                .formLogin((auth)->auth.disable());
        httpSecurity
                .httpBasic((auth)->auth.disable());
        httpSecurity
                .httpBasic((auth)->auth.disable());

        httpSecurity.addFilterAt(loginFilter()
                , UsernamePasswordAuthenticationFilter.class); // 필터 순서 2 ( 로그인 )
        httpSecurity
                .addFilterBefore(jwtFilter(), LoginFilter.class); // 필터 순서 1 ( 로그인 )
        httpSecurity
                .addFilterBefore(jwtLogoutFilter(), LogoutFilter.class); // 필터 순서 1 ( 로그아웃 )


        // 세션을 유지하지 않도록 하는 설정 -> STATELESS
        httpSecurity
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Http 주소허용 여부 설정 -> Default
        httpSecurity
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/login",
                                "/user/create",
                                "/user/check-nickname",
                                "/user/check-username").permitAll()
                        .requestMatchers("/refresh").permitAll()
                        .anyRequest().authenticated()
                );

        // CORS 설정
        httpSecurity
                .cors((corsCustomizer)->corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 허용 출처 지정
                        configuration.setAllowedMethods(Collections.singletonList("*")); // HTTP 메소드 지정
                        configuration.setAllowCredentials(true); // 인증 정보를 포함한 요청을 허용
                        configuration.setAllowedHeaders(Collections.singletonList("*")); // 클라이언트 요청 시 보낼 수 있는 헤더 지정
                        configuration.setMaxAge(3600L); // 브라우저 preflight 요청캐싱 시간 지정

                        return configuration;
                    }
                }));

        return httpSecurity.build();

    }
}
