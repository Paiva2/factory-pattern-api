package com.root.pattern.application.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.adapter.repository.MusicianDataProviderImpl;
import com.root.pattern.adapter.repository.UserDataProviderImpl;
import com.root.pattern.adapter.utils.JwtHandler;
import com.root.pattern.domain.entity.CustomUserDetails;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.entity.User;
import com.root.pattern.domain.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class RequestFilterChain extends OncePerRequestFilter {
    private static final List<AntPathRequestMatcher> UNFILTERABLE_ROUTES;

    private final UserDataProviderImpl userDataProvider;
    private final MusicianDataProviderImpl musicianDataProvider;
    private final JwtHandler jwtHandler;

    static {
        UNFILTERABLE_ROUTES = new ArrayList<AntPathRequestMatcher>() {{
            add(new AntPathRequestMatcher("/api/v1/musician/album/list/*", HttpMethod.GET.toString()));
            add(new AntPathRequestMatcher("/api/v1/musician/album/*", HttpMethod.GET.toString()));
            add(new AntPathRequestMatcher("/api/v1/musician", HttpMethod.GET.toString()));

            add(new AntPathRequestMatcher("/api/v1/user/register", HttpMethod.POST.toString()));
            add(new AntPathRequestMatcher("/api/v1/musician/register", HttpMethod.POST.toString()));
            add(new AntPathRequestMatcher("/api/v1/user/login", HttpMethod.POST.toString()));
            add(new AntPathRequestMatcher("/api/v1/musician/login", HttpMethod.POST.toString()));
        }};
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String hasAuthHeader = this.getAuthHeader(request);

        if (Objects.isNull(hasAuthHeader)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        DecodedJWT parseToken = this.jwtHandler.verify(hasAuthHeader);
        String getUserRole = parseToken.getClaim("app-role").asString();

        if (Objects.isNull(getUserRole)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Role role = Role.valueOf(getUserRole);
        UserDetails userDetails = this.findUserHandlingRole(role, parseToken.getSubject());

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private UserDetails findUserHandlingRole(Role role, String subject) {
        CustomUserDetails details = null;

        switch (role) {
            case USER: {
                details = this.buildUser(Long.valueOf(subject), Role.USER);
                break;
            }

            case MUSICIAN: {
                details = this.buildMusician(Long.valueOf(subject));
                break;
            }

            case ADMIN: {
                details = this.buildUser(Long.valueOf(subject), Role.ADMIN);
                break;
            }
        }

        return details;
    }

    private CustomUserDetails buildMusician(Long id) {
        Musician musician = this.musicianDataProvider.findById(id)
            .orElseThrow(() -> new NotFoundException("Musician not found"));

        return CustomUserDetails.builder()
            .id(musician.getId().toString())
            .password(musician.getPassword())
            .role(Role.MUSICIAN)
            .build();
    }

    private CustomUserDetails buildUser(Long id, Role role) {
        User user = this.userDataProvider.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));

        return CustomUserDetails.builder()
            .id(user.getId().toString())
            .password(user.getPassword())
            .role(role)
            .build();
    }

    private String getAuthHeader(HttpServletRequest request) {
        String getBearerToken = request.getHeader("Authorization");

        return Objects.isNull(getBearerToken) ? null : getBearerToken.replaceAll("Bearer ", "");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return UNFILTERABLE_ROUTES.stream().anyMatch(matcher -> matcher.matches(request));
    }
}
