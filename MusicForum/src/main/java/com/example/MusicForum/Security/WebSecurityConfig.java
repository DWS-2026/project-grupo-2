package com.example.MusicForum.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {
        
        // Use the injected authProvider
        http.authenticationProvider(authProvider);

        http
			.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                // Public Pages
                .requestMatchers("/", "/login", "/register", "/loginerror", "/css/**", "/js/**").permitAll()
                .requestMatchers("/first/**", "/album_listing/**", "/album_view/**").permitAll()
                .requestMatchers("/error.html/**", "/footer.html/**", "/header.html/**").permitAll()
                .requestMatchers("/post_listing.html/**", "/post_view.html/**").permitAll()
                
                // Private Pages (USER)
                .requestMatchers("/edit_profile", "/edit_post", "/new_comment", "/new_post", "/user_profile/**").hasRole("USER")

                // Admin Pages
                .requestMatchers("/user_listing", "/admin_panel").hasRole("ADMIN")
                
                // Any other request
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/loginerror")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}
