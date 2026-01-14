package mk.ukim.finki.emc.academic_assessment_system_backend.web.filters;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import mk.ukim.finki.emc.academic_assessment_system_backend.constants.JwtConstants;
import mk.ukim.finki.emc.academic_assessment_system_backend.dto.security.JwtUserPrincipal;
import mk.ukim.finki.emc.academic_assessment_system_backend.helpers.JwtHelper;
import mk.ukim.finki.emc.academic_assessment_system_backend.model.domain.User;
import mk.ukim.finki.emc.academic_assessment_system_backend.service.domain.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtFilter(JwtHelper jwtHelper,
                     UserService userService,
                     HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtHelper = jwtHelper;
        this.userService = userService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        if (path.startsWith("/api/user/login") || path.startsWith("/api/user/register")) {
            return true;
        }

        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/h2")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String headerValue = request.getHeader(JwtConstants.HEADER);

        if (headerValue == null || !headerValue.startsWith(JwtConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = headerValue.substring(JwtConstants.TOKEN_PREFIX.length()).trim();

        try {
            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
            if (existingAuth != null && existingAuth.isAuthenticated()) {
                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtHelper.extractUsername(token);
            if (email == null || email.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtHelper.isExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authToken = getUsernamePasswordAuthenticationToken(userOpt);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (JwtException jwtException) {
            handlerExceptionResolver.resolveException(request, response, null, jwtException);
        }
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Optional<User> userOpt) {
        User user = userOpt.get();

        JwtUserPrincipal principal = new JwtUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())
        );

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }
}
