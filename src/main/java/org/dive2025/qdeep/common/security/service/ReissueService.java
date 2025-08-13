package org.dive2025.qdeep.common.security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.dive2025.qdeep.common.security.dto.response.ReissueResponse;
import org.dive2025.qdeep.common.security.entity.Refresh;
import org.dive2025.qdeep.common.security.repository.ReissueRepository;
import org.dive2025.qdeep.common.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ReissueService {

   @Autowired
    private JwtUtil jwtUtil;
   @Autowired
    private ReissueRepository reissueRepository;

   public String findCookie(HttpServletRequest request){

       String refresh = null;
       Cookie[] cookies = request.getCookies();
       if(cookies !=null){
           for(Cookie cookie : cookies){
               if("refresh".equals(cookie.getName())){
                   refresh = cookie.getValue();
               }
           }
       }

       return refresh;
   }


   public ReissueResponse refreshCookies(HttpServletRequest request){

       String refresh = findCookie(request);
       if(refresh == null){
           return new ReissueResponse("Refresh NULL",
                   "Refresh 토큰이 비어있습니다. [ TIME ] : " +
                           LocalDateTime.now(),
                   null,
                   null);
       }

       return new ReissueResponse("Refresh EXISTS",
               "Refresh 토큰이 존재합니다. [ TIME ] : " + LocalDateTime.now(),
               resetAccessToken(request),
               reissueRefresh(request));
   }

   private String resetAccessToken(HttpServletRequest request){

       String refresh = findCookie(request);
       String username = jwtUtil.getUsername(refresh);
       String role = jwtUtil.getRole(refresh);

       return jwtUtil.createJwt("access",username,role,600000L);

   }

   public String reissueRefresh(HttpServletRequest request){


       String refresh = findCookie(request);
       String username = jwtUtil.getUsername(refresh);
       String role = jwtUtil.getRole(refresh);

       String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
       reissueRepository.deleteByRefresh(refresh);

       addRefresh(username, newRefresh, 86400000L);
       return newRefresh;

   }

   @Transactional
    public void addRefresh(String username,String refresh,Long expiredMs){

       LocalDateTime localDateTime = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));

       Refresh refreshToken = Refresh.builder()
               .username(username)
               .refresh(refresh)
               .expiration(localDateTime.toString())
               .build();

       reissueRepository.save(refreshToken);

   }

   public Cookie createCookie(String key,String value){

       Cookie cookie = new Cookie(key,value);
       cookie.setMaxAge(0);
       cookie.setHttpOnly(true);

       return cookie;
   }

  // zeroCookie 메소드

}
