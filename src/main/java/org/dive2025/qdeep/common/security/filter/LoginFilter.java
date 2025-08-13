package org.dive2025.qdeep.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.common.security.dto.request.LoginRequest;
import org.dive2025.qdeep.common.security.dto.response.LoginFailedResponse;
import org.dive2025.qdeep.common.security.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public LoginFilter(ObjectMapper objectMapper,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil){

        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        setAuthenticationManager(authenticationManager); // 부모클래스의 메소드를 통해 필드 초기화
    }

    @Override
    public Authentication attemptAuthentication
            (HttpServletRequest request,
             HttpServletResponse response)
            throws AuthenticationException {

        try{
            // JSON 데이터를 스트림으로 받아오기
            InputStream stream = request.getInputStream();
            LoginRequest loginRequest = objectMapper.readValue(stream,LoginRequest.class);

            // 로그인 정보를 Spring Security 인증 프로세스에 인증 요청하는 객체 생성하기
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loginRequest.username()
                    ,loginRequest.password());

            // AuthenticationManager에게 인증요청
            return authenticationManager.authenticate(token);

        } catch(IOException e) {
            // 인증 실패시 AuthenticationServiceException 발생
            throw new AuthenticationServiceException("JSON 파싱 오류발생",e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        String username = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();

        String role = authority.getAuthority();

        String access = jwtUtil.createJwt("access",username,role,600000L);
        String refresh = jwtUtil.createJwt("refresh",username,role,8640000L);

        response.setHeader("Authorization",access);
        //response.addCookie();
        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        LoginFailedResponse failedResponse =
                new LoginFailedResponse("LOGIN_FAILED","아이디 또는 비밀번호가 일치하지 않습니다.");

        String json = objectMapper.writeValueAsString(failedResponse);

        response.getWriter().write(json);
        response.getWriter().flush();
        response.flushBuffer();

    }
}
