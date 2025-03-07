package ua.ellka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.ellka.auth.BasicAuthEntryPoint;
import ua.ellka.repo.UserRepo;
import ua.ellka.service.ProjectTrackerUserDetailsService;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, BasicAuthEntryPoint basicAuthEntryPoint) throws Exception {
        http.
                csrf(conf -> conf.disable())
                .authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers(antMatcher("/v1/auth/**")).permitAll()
                                .anyRequest().authenticated())
                .httpBasic(conf -> conf.authenticationEntryPoint(basicAuthEntryPoint));


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepo) {
        return new ProjectTrackerUserDetailsService(userRepo);
    }
}
