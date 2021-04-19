package com.okdev.ems.config.jwt;

import com.okdev.ems.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static io.jsonwebtoken.lang.Strings.hasText;

@Component
public class JwtFilter extends GenericFilterBean {

//    public static final String AUTHORIZATION = "Authorization";
//
//    @Autowired
//    private JwtProvider jwtProvider;
//
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        logger.info("do filter...");
//        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
//        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//
//        String authHeader = httpRequest.getHeader(AUTHORIZATION);
//        logger.info("Authorization Header is: " + authHeader);
//        if (authHeader != null) {
//            String[] authHeaderArr = authHeader.split("Bearer ");
//            if (authHeaderArr.length > 0 && authHeaderArr[1] != null) {
//                String token = authHeaderArr[1];
//                try {
//                    String emailFromToken = jwtProvider.getEmailFromToken(token);
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(emailFromToken);
//                    UsernamePasswordAuthenticationToken auth =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                } catch (Exception e) {
//                    httpResponse.sendError(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), "Invalid/expired token");
//                    return;
//                }
//            } else {
//                httpResponse.sendError(HttpStatus.NO_CONTENT.value(), "Authorization token must be Bearer [token]");
//                return;
//            }
//        } else {
//            httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Authorization token must be provided");
//            return;
//        }
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}

    public static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("do filter...");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        if (token != null && jwtProvider.validateToken(token)) {
            String userLogin = jwtProvider.getEmailFromToken(token);
//            String userId = jwtProvider.getIdFromToken(token);
            UserDetails customUserDetails = userDetailsService.loadUserByUsername(userLogin);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            httpRequest.setAttribute("userId", jwtProvider.getIdFromToken(token));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        logger.info("Authorization Header is: " + bearer);
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
