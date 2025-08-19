package org.dive2025.qdeep.common.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.common.security.auth.UserDetailsImpl;
import org.dive2025.qdeep.common.security.dto.response.ExpiredJwtResponse;
import org.dive2025.qdeep.common.security.dto.response.TokenInvalidCategoryResponse;
import org.dive2025.qdeep.common.security.util.JwtUtil;

import org.dive2025.qdeep.domain.user.entity.User;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher antPathMatcher;
    private final UserRepository userRepository;



    // JwtFilter를 사용하지 않는 URI 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();

        return antPathMatcher.match("/login", path) ||
                antPathMatcher.match("/refresh", path) ||
                antPathMatcher.match("/user/check-nickname", path) ||
                antPathMatcher.match("/user/check-username", path) ||
                antPathMatcher.match("/swagger-ui/**", path) ||
                antPathMatcher.match("/v3/api-docs/**", path);
    }



    // JwtFilter 주요필터 기능
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 토큰 추출하기
        String accessToken = extractToken(request);


        // 토큰 NULL 체크
        if(accessToken==null){
            log.info(" AccessToken NULL 발생 " +
                    "[ TIME ] : " +
                    LocalDateTime
                            .now());
            filterChain.doFilter(request,response);
        }

        // 토큰에서 Category 받아오기
        try{
            jwtUtil.getCateory(accessToken);
        } catch (ExpiredJwtException e){
            handleExpiredJwt(response);
            return;

        }

        // 카테고리 일치여부 확인
        String category = jwtUtil.getCateory(accessToken);
        if(!category.equals("access")){
            log.info("토큰 카테고리 불일치 : " +
                    category +
                    "[ TIME ] : " +
                    LocalDateTime
                            .now());
            handleInvalidTokenCategory(response,category);
            return;
        }

        // Security 컨테이너에 사용자 등록하기

        SecurityContextHolder
                .getContext()
                .setAuthentication(makeAuthenticationFromToken(accessToken));

        filterChain.doFilter(request,response);

    }

    // 헤더에서 토큰을 추출하는 메소드
    private String extractToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            log.info("잘못된 토큰 파싱 발생 : " +
                    header + "[ TIME ] : " +
                    LocalDateTime
                            .now());
            return null;
        }
        return header.substring(7);
    }

    // 파기된 토큰을 다루는 메소드
    private void handleExpiredJwt(HttpServletResponse response)
        throws IOException{

        ExpiredJwtResponse expiredJwtResponse =
                new ExpiredJwtResponse("Jwt토큰이 이미 파기되었습니다.",
                        LocalDateTime
                        .now()
                        .toString());

        String json = objectMapper.writeValueAsString(expiredJwtResponse);
        response.getWriter().write(json);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    // 잘못된 카테고리 토큰을 다루는 메소드
    private void handleInvalidTokenCategory(HttpServletResponse response,
                                            String category)
            throws IOException{

        TokenInvalidCategoryResponse tokenInvalidCategoryResponse =
                new TokenInvalidCategoryResponse(category,
                        "잘못된 카테고리 입니다.",
                        LocalDateTime
                                .now()
                                .toString());


        String json = objectMapper.writeValueAsString(tokenInvalidCategoryResponse);
        response.getWriter().write(json);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

    }

    public Authentication makeAuthenticationFromToken(String token){
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        return new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());
    }


}
