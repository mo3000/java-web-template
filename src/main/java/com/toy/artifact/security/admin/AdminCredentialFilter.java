package com.toy.artifact.security.admin;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.toy.artifact.utils.jwt.JwtWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@WebFilter(urlPatterns = "/admin/**")
@Component
public class AdminCredentialFilter implements Filter {
    private final Set<String> excludes = new HashSet<>(List.of("/admin/login"));
    private Logger logger;
    private final JwtWrapper jwtWrapper;
    public static String BearerTokenPrefix = "Bearer ";

    @Value("${app.admin.jwt.enableTestUserid:false}")
    private boolean enableTestUserid;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger = LoggerFactory.getLogger(getClass());
    }

    public AdminCredentialFilter(JwtWrapper jwtWrapper) {
        this.jwtWrapper = jwtWrapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var req = (HttpServletRequest) servletRequest;
        var resp = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        String authHeader = req.getHeader("Authorization");
        if (!enableTestUserid && !excludes.contains(uri)
            && (authHeader == null
                || ! authHeader.startsWith(BearerTokenPrefix)
                || authHeader.length() < 100
                || ! canDecode(authHeader.substring(BearerTokenPrefix.length() - 1)))) {
            try (var writer = servletResponse.getWriter()) {
                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                writer.write("UNAUTHORIZED");
                writer.flush();
            }

            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean canDecode(String token) {
        try {
            jwtWrapper.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public void destroy() {

    }
}
