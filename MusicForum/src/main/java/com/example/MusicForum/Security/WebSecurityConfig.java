package com.example.MusicForum.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.MusicForum.Security.jwt.AuthResponse;
import com.example.MusicForum.Security.jwt.LoginRequest;
import com.example.MusicForum.Security.jwt.UserLoginService;
import com.example.MusicForum.Security.jwt.JwtRequestFilter;
import com.example.MusicForum.Security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // 1. Password Encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Auth Provider: pass the service as a parameter to avoid circular reference
    @Bean
    public DaoAuthenticationProvider authenticationProvider(RepositoryUserDetailsService userDetailService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


  @Bean
@Order(1) // This chain is evaluated first, before the web filter chain
public SecurityFilterChain apiFilterChain(HttpSecurity http, 
        DaoAuthenticationProvider authProvider) throws Exception {

    // Register the authentication provider that validates users against the database
    http.authenticationProvider(authProvider);

    // This chain only applies to requests starting with /api/**
    // All other requests fall through to the web filter chain (@Order 2)
    http.securityMatcher("/api/**");

    http
        .authorizeHttpRequests(authorize -> authorize
            // Only ADMIN role can perform DELETE requests on /api/posts/**
            .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasRole("ADMIN")
            // All other API endpoints are publicly accessible
            // anyRequest() must always be the last rule
            .anyRequest().permitAll()
        );

    // Disable form-based login — APIs don't redirect to login pages
    http.formLogin(formLogin -> formLogin.disable());

    // Disable CSRF protection — not needed for stateless REST APIs
    // CSRF attacks rely on session cookies, which we don't use here
    http.csrf(csrf -> csrf.disable());

    // Enable HTTP Basic Authentication
    // Postman sends credentials in the Authorization header:
    // Authorization: Basic base64(username:password)
    http.httpBasic(Customizer.withDefaults());

    // Configure stateless session management
    // The server won't create or store any HTTP session between requests
    // Each request must authenticate independently
    http.sessionManagement(management -> 
        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
}



    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {

        // Use the injected authProvider
        http.authenticationProvider(authProvider);

        http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        // Public Pages
                        .requestMatchers("/", "/error", "/login", "/register", "/loginerror", "/css/**", "/js/**",
                                "/images/**")
                        .permitAll()
                        .requestMatchers("/first/**", "/album_listing/**", "/album_view/**").permitAll()
                        .requestMatchers("/error.html/**", "/footer.html/**", "/header.html/**").permitAll()
                        .requestMatchers("/post_listing", "/post_listing/**").permitAll()
                        .requestMatchers("/post_view", "/post_view/**", "/post/**").permitAll()
                        .requestMatchers("/posts/**").permitAll()
                        .requestMatchers("/access_denied").permitAll()

                        // Private Pages (USER)
                        .requestMatchers("/edit_profile", "/edit_post", "/new_comment", "/new_post", "/user_profile/**")
                        .hasRole("USER")

                        // Admin Pages
                        .requestMatchers("/user_listing", "/admin_panel", "/albumModal", "/albumCreate", "/user/**")
                        .hasRole("ADMIN")

                        // Any other request
                        .anyRequest().permitAll())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        http.exceptionHandling(handling -> handling
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/access_denied");
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/access_denied");
                }));

        return http.build();
    }
}
