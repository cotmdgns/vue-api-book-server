package com.server.vuebook.jwt;

import com.server.vuebook.domain.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 클라이언트에서 보낸 토큰을 받아서 사용자 확인 후 인증 처리
        String token = parseBearerToken(request);

        if(token!=null && !token.equalsIgnoreCase("null")) {
            Member member = tokenProvider.validate(token);

            // 추출된 인증 정보를 필터링에서 사용할 수 있도록 SecurityContext에 등록
            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(member, member.getMemPwd());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);

            SecurityContextHolder.setContext(securityContext);
        } else {
            log.warn("토큰이 없거나 유효하지 않음");
        }

        filterChain.doFilter(request, response);
    }


    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
