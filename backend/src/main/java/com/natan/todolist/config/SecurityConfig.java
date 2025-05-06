package com.natan.todolist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.natan.todolist.security.CustomUserDetailsService;
import com.natan.todolist.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private CustomUserDetailsService userDetailsService;
	private final JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf(csrf -> csrf.disable())
          .exceptionHandling(ex -> ex
        		    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
    		)
          .sessionManagement(sm -> sm
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(HttpMethod.POST, "/users").permitAll()
              .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
              .requestMatchers(
                      "/swagger-ui.html",
                      "/swagger-ui/**",
                      "/v3/api-docs/**",
                      "/swagger-resources/**",
                      "/webjars/**"
                  ).permitAll()
              .anyRequest().authenticated()
          )

          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
            PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }
}