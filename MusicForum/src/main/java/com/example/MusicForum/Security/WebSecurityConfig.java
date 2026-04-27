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
@Order(1)
public SecurityFilterChain apiFilterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {

    http.authenticationProvider(authProvider);

    http.securityMatcher("/api/**");

    http
        .authorizeHttpRequests(authorize -> authorize
            // PRIMERO las reglas específicas
            .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasRole("ADMIN")
            // SIEMPRE al final el anyRequest
            .anyRequest().permitAll()
        );

    // Disable Form login
    http.formLogin(formLogin -> formLogin.disable());

    // Disable CSRF
    http.csrf(csrf -> csrf.disable());

    // Activa Basic Auth (en lugar de disable)
    http.httpBasic(Customizer.withDefaults());

    // Stateless session
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
