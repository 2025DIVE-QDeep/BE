package org.dive2025.qdeep.common.security.filter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dive2025.qdeep.common.security.dto.response.LogoutErrorResponse;
import org.dive2025.qdeep.common.security.dto.response.LogoutSuccessResponse;
import org.dive2025.qdeep.common.security.service.ReissueService;
import org.dive2025.qdeep.common.security.util.JwtUtil;
import org.dive2025.qdeep.common.util.JsonResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Predicate;

@Slf4j
@AllArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ReissueService reissueService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        return !("/logout".equals(request
                .getRequestURI())
                &&"POST".equalsIgnoreCase(request
                        .getMethod()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("[ 로그아웃 필터 실행 ] URI : {} , TIME : {}"
                ,request.getRequestURI()
                , LocalDateTime.now());

        String refresh = reissueService.findCookie(request);
        log.info("[ REFERSH ] : {}",refresh);

        if(checkRefresh(refresh).error()){
            log.info(" [ Refresh Token 오류 발생 ] TIME : {} ",LocalDateTime.now());
            filterChain.doFilter(request,response);
            return;
        }

        // refresh 삭제하기
        reissueService.deleteRefresh(refresh);

        // Cookie를 빈 쿠키로 설정하기
        reissueService.zeroCookie(response);

        // 로그아웃 성공 Response 반환
        JsonResponseUtils.writeJsonResponse(HttpStatus.OK,
                response,
                new LogoutSuccessResponse(true,
                        "Method : /logout " + "/n" + " 로그아웃에 성공하였습니다.",
                        LocalDateTime.now().toString())
        );

    }

    private LogoutErrorResponse checkRefresh(String refresh){

        Predicate<String> checkNull = StringParam
                -> StringUtils.isBlank(StringParam);

        Predicate<String> checkExpiration = StringParam
                -> jwtUtil.isExpired(StringParam);

        Predicate<String> checkCategory = StringParam
                -> !"refresh".equals(jwtUtil.getCateory(StringParam));

        String strCheckNull = ""+checkNull.test(refresh);
        String strCheckExpiration = "" + checkExpiration.test(refresh);
        String strCheckCategory = "" + checkCategory.test(refresh);

        log.info("Method : POST , "+
                "Refresh NULL : " + strCheckNull + " ,"
                +"Expiration : " + strCheckExpiration + " ,"
                +"Category : " + strCheckCategory);

        boolean result = (checkNull.test(refresh)
                &&checkCategory.test(refresh)
                &&checkExpiration.test(refresh));

        LogoutErrorResponse response =
                new LogoutErrorResponse(result,
                        "Method : POST",
                        "Method : POST , "+
                                "Refresh NULL : " + strCheckNull + " ,"
                                +"Expiration : " + strCheckExpiration + " ,"
                                +"Category : " + strCheckCategory,
                        LocalDateTime.now().toString());

        return response;
    }


}
